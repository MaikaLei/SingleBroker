import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.time.*;
import java.math.BigDecimal;
import javax.swing.*;
import model.*;
import dao.*;
import controller.*;
import util.*;

/** Nunca utiliza o banco do projeto para escrita: cria e remove um schema de teste próprio. */
@EnabledIfSystemProperty(named="singlebroker.integration",matches="true")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FluxosMySqlTest {
    String schema, servidor, usuario, senha;
    UsuarioModel admin, segundo;
    ClientePfModel cliente;
    ImovelModel imovel;
    @BeforeAll void preparar() throws Exception {
        Properties p=new Properties();
        try(var reader=Files.newBufferedReader(Path.of("singlebroker.local.properties"))) { p.load(reader); }
        usuario=p.getProperty("db.user"); senha=p.getProperty("db.password");
        String url=p.getProperty("db.url");
        servidor=url.substring(0,url.indexOf('/',"jdbc:mysql://".length())+1);
        schema="singlebroker_test_"+UUID.randomUUID().toString().replace("-","");
        try(Connection c=DriverManager.getConnection(servidor,usuario,senha); Statement s=c.createStatement()) { s.executeUpdate("CREATE DATABASE `"+schema+"` CHARACTER SET utf8mb4"); }
        System.setProperty("SINGLEBROKER_DB_URL",servidor+schema);
        System.setProperty("SINGLEBROKER_DB_DDL","update");
        System.setProperty("SINGLEBROKER_DB_USER",usuario); System.setProperty("SINGLEBROKER_DB_PASSWORD",senha);
        JPAUtil.close();
        admin=novoUsuario("Admin Teste","admin@example.test");
        segundo=novoUsuario("Outro Usuário","outro@example.test");
        SessaoUsuario.setUsuarioLogado(admin);
        cliente=new ClientePfModel(); cliente.setNome("Cliente de teste"); cliente.setCpf("52998224725"); cliente.setCidade("Campo Bom"); cliente.setEstado("RS"); cliente.setRua("Rua Preservada"); cliente.setDataNascimento(LocalDate.of(1990,1,2));
        new ClienteController().salvar(cliente);
        imovel=new ImovelModel(); imovel.setTipoImovel("Casa"); imovel.setTransacao("Venda"); imovel.setStatusImovel("Ativo"); imovel.setEndereco("Rua de Teste"); imovel.setCidade("Campo Bom"); imovel.setEstado("RS"); imovel.setProprietario(cliente); imovel.setValor("500.000,00"); imovel.setAreaTotal(new BigDecimal("150.00")); imovel.setAreaPrivativa(new BigDecimal("100.00")); imovel.setPiscinaCasa(true); imovel.setCameras(true);
        new ImovelController().salvar(imovel);
    }
    private UsuarioModel novoUsuario(String nome,String email) {
        UsuarioModel u=new UsuarioModel(); u.setNome(nome); u.setEmail(email); u.setSenha("senha123"); u.setAtivo(true); u.setPerfil(enums.PerfilUsuario.ADMINISTRADOR); u.setDataCadastro(LocalDateTime.now()); new UsuarioDao().salvar(u); return u;
    }
    @BeforeEach void sessao() { SessaoUsuario.setUsuarioLogado(admin); }
    @AfterAll void limpar() throws Exception {
        JPAUtil.close(); SessaoUsuario.encerrarSessao();
        for(String chave:List.of("SINGLEBROKER_DB_URL","SINGLEBROKER_DB_DDL","SINGLEBROKER_DB_USER","SINGLEBROKER_DB_PASSWORD")) System.clearProperty(chave);
        if(schema!=null && schema.matches("singlebroker_test_[a-f0-9]{32}"))
            try(Connection c=DriverManager.getConnection(servidor,usuario,senha); Statement s=c.createStatement()) { s.executeUpdate("DROP DATABASE `"+schema+"`"); }
    }
    @Test @Order(1) void loginLegadoMigraSenhaESenhaIncorretaNaoAutentica() {
        assertNull(new UsuarioDao().autenticar("admin@example.test","incorreta"));
        assertNotNull(new LoginController().entrar("admin@example.test","senha123"));
        assertTrue(Senhas.isHash(new UsuarioDao().buscarPorId(admin.getId()).getSenha()));
        assertNotNull(new UsuarioDao().autenticar("admin@example.test","senha123"));
    }
    @Test @Order(2) void duplicidadeClienteEhPropagadaSemCriarRegistro() {
        ClientePfModel copia=new ClientePfModel(); copia.setNome("Duplicado"); copia.setCpf(cliente.getCpf());
        assertThrows(IllegalArgumentException.class,()->new ClienteController().salvar(copia));
        assertThrows(IllegalStateException.class,()->new ClienteDao().salvar(copia));
        assertEquals(1,new ClienteDao().buscarPf("Cliente","529.982.247-25", "").size());
        assertEquals(1,new ClienteDao().listarPf().size());
    }
    @Test @Order(3) void clientePjPersisteEAtualiza() {
        ClientePjModel pj=new ClientePjModel();pj.setRazaoSocial("Empresa Teste");pj.setCnpj("11222333000181");
        new ClienteController().salvar(pj); pj.setNomeResponsavel("Responsável"); new ClienteController().salvar(pj);
        assertEquals("Responsável",((ClientePjModel)new ClienteDao().buscarPorId(pj.getId())).getNomeResponsavel());
    }
    private Object campo(Object view,String nome) throws Exception { var f=view.getClass().getDeclaredField(nome);f.setAccessible(true);return f.get(view); }
    @Test @Order(4) void telaEdicaoImovelPreservaIdentidadeAreasECaracteristicas() throws Exception {
        SwingUtilities.invokeAndWait(()->{
            view.NovoImovelView tela=new view.NovoImovelView(new ImovelDao().buscarPorId(imovel.getId()));
            try {
                assertFalse(tela.temAlteracoes());
                assertEquals("150.00",((JTextField)campo(tela,"txtArTot")).getText());
                assertEquals("100.00",((JTextField)campo(tela,"txtArPriv")).getText());
                ((JTextField)campo(tela,"txtArPriv")).setText("110,50");
                ((JTextField)campo(tela,"txtIdGar")).setText("G-02");
                assertTrue(tela.temAlteracoes()); assertTrue(tela.salvarAlteracoes());
            } catch(Exception e){throw new RuntimeException(e);} finally {tela.dispose();}
        });
        ImovelModel salvo=new ImovelDao().buscarPorId(imovel.getId());
        assertEquals(1,new ImovelDao().listar().size());
        assertEquals(0,new BigDecimal("110.50").compareTo(salvo.getAreaPrivativa()));
        assertEquals(0,new BigDecimal("150.00").compareTo(salvo.getAreaTotal()));
        assertEquals("G-02",salvo.getIdentificacaoGaragem()); assertTrue(salvo.getPiscinaCasa()); assertTrue(salvo.getCameras());
    }
    @Test @Order(5) void telaClienteCarregaEnderecoENascimento() throws Exception {
        SwingUtilities.invokeAndWait(()->{
            view.NovoClienteModal tela=new view.NovoClienteModal(new ClienteDao().buscarPorId(cliente.getId()));
            try {
                assertEquals("Rua Preservada",((JTextField)campo(tela,"txtRua")).getText());
                assertEquals("02/01/1990",((JTextField)campo(tela,"txtNascimento")).getText());
                assertFalse(((JComboBox<?>)campo(tela,"cbxTipoCliente")).isEnabled());
            } catch(Exception e){throw new RuntimeException(e);} finally {tela.dispose();}
        });
    }
    @Test @Order(6) void agendaPersisteEditaConcluiEIsolaUsuarios() {
        AgendaModel t=new AgendaModel();t.setData(LocalDate.now());t.setHorario(LocalTime.of(9,30));t.setTipo("Visita Comprador");t.setDescricao("Visita ao imóvel");new AgendaController().salvar(t);
        assertEquals(1,new AgendaDao().listar(t.getData(),t.getData()).size());
        SessaoUsuario.setUsuarioLogado(segundo);assertTrue(new AgendaDao().listar(t.getData(),t.getData()).isEmpty());
        assertThrows(SecurityException.class,()->new AgendaDao().excluir(t.getId()));
        assertThrows(SecurityException.class,()->new AgendaController().salvar(t));
        SessaoUsuario.setUsuarioLogado(admin);t.setConcluida(true);t.setDescricao("Visita concluída");new AgendaController().salvar(t);
        assertTrue(new AgendaDao().listar(t.getData(),t.getData()).get(0).isConcluida());
        new AgendaDao().excluir(t.getId());assertTrue(new AgendaDao().listar(t.getData(),t.getData()).isEmpty());
    }
    @Test @Order(7) void fotosDocumentosPersistemERemocaoEhLimitadaAoImovel() throws Exception {
        java.io.ByteArrayOutputStream png=new java.io.ByteArrayOutputStream();
        javax.imageio.ImageIO.write(new java.awt.image.BufferedImage(50,50,java.awt.image.BufferedImage.TYPE_INT_RGB),"png",png);
        FotoModel foto=new FotoModel();foto.setNome("foto.png");foto.setConteudo(png.toByteArray());
        new FotoDao().salvar(imovel.getId(),List.of(foto),List.of());
        assertArrayEquals(png.toByteArray(),new FotoDao().listar(imovel.getId()).get(0).getConteudo());
        DocumentoModel doc=new DocumentoModel();doc.setNome("contrato.txt");doc.setConteudo("contrato teste".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        new DocumentoDao().salvar(imovel.getId(),List.of(doc),List.of());
        assertEquals("contrato.txt",new DocumentoDao().listar(imovel.getId()).get(0).getNome());
        new DocumentoDao().salvar(imovel.getId(),List.of(),List.of(doc.getId()));assertTrue(new DocumentoDao().listar(imovel.getId()).isEmpty());
    }
    @Test @Order(8) void minhaPaginaEhPersistidaPorUsuario() {
        MinhaPaginaModel p=new MinhaPaginaDao().carregar();p.setTitulo("Corretor");p.setDescricao("Apresentação");p.setBibliografia("Trajetória profissional");p.setEmail("contato@example.test");new MinhaPaginaController().salvar(p);
        assertEquals("Corretor",new MinhaPaginaDao().carregar().getTitulo());
        SessaoUsuario.setUsuarioLogado(segundo);assertNull(new MinhaPaginaDao().carregar().getTitulo());
        assertThrows(SecurityException.class,()->new MinhaPaginaDao().salvar(p));
    }
    @Test @Order(9) void relatoriosFiltramStatusUsuarioEDatas() {
        RelatorioController c=new RelatorioController();
        assertEquals(1,c.gerar("Lista de ativos",null,null,null).quantidade());
        assertEquals(0,c.gerar("Lista de imóveis",null,null,segundo.getId()).quantidade());
        assertEquals(1,c.gerar("Lista de Proprietários",null,null,admin.getId()).quantidade());
        assertThrows(IllegalArgumentException.class,()->c.gerar("Lista de imóveis",LocalDate.now(),LocalDate.now().minusDays(1),null));
        ImovelModel i=new ImovelDao().buscarPorId(imovel.getId());i.setStatusImovel("Vendido");new ImovelController().salvar(i);
        assertEquals(1,c.gerar("Vendidos",LocalDate.now(),LocalDate.now(),admin.getId()).quantidade());
        assertEquals(0,c.gerar("Lista de ativos",null,null,null).quantidade());
        i.setStatusImovel("Ativo");new ImovelController().salvar(i);assertNull(new ImovelDao().buscarPorId(i.getId()).getDataVenda());
    }
    @Test @Order(10) void usuarioNaoPodeRemoverSeuAcessoAdministrativo() {
        UsuarioModel u=new UsuarioDao().buscarPorId(admin.getId());u.setAtivo(false);
        assertThrows(IllegalArgumentException.class,()->new UsuarioController().salvar(u,""));
        assertTrue(new UsuarioDao().buscarPorId(admin.getId()).getAtivo());
        SessaoUsuario.encerrarSessao();
        assertThrows(SecurityException.class,()->new UsuarioController().salvar(u,""));
    }
    @Test @Order(11) void telasPrincipaisAbremComDadosECalendarioTemMesCorreto() throws Exception {
        SwingUtilities.invokeAndWait(()->{
            java.util.List<JFrame> telas=new ArrayList<>();
            try {
                telas.add(new view.ListaImovelView());telas.add(new view.ClienteView());
                view.AgendaView agenda=new view.AgendaView();telas.add(agenda);
                ((JComboBox<?>)campo(agenda,"cbxMes")).setSelectedIndex(1);((JComboBox<?>)campo(agenda,"cbxAno")).setSelectedItem("2028");
                int dias=0;for(int n=1;n<=42;n++)if(((JButton)campo(agenda,"btnGrid"+n)).isEnabled())dias++;
                assertEquals(29,dias);
                telas.add(new view.CriativosView());telas.add(new view.MinhaPaginaView());telas.add(new view.RelatoriosView());
                telas.add(new view.ImovelView(new ImovelDao().buscarPorId(imovel.getId())));
                telas.add(new view.FotosModal(imovel.getId()));telas.add(new view.DocumentosModal(imovel.getId()));
                telas.add(new view.LoginView());
                telas.add(new view.NovoClienteModal(cliente));
                telas.add(new view.NovoImovelView(new ImovelDao().buscarPorId(imovel.getId())));
                telas.add(new view.SelecionarClienteView());
                telas.add(new view.ListaUsuarioModal());
                telas.add(new view.cadastroUsuarioModalView());
                telas.add(new view.AgendaModal(LocalDate.now()));
                telas.add(new view.AgendaHoraModal(LocalDate.now(), null));
                Path imagens=Path.of(".work/previews");Files.createDirectories(imagens);
                for(JFrame tela:telas) {
                    util.Tema.aplicar(tela);
                    tela.validate();
                    capturarTela(tela, imagens, tela.getClass().getSimpleName());
                    for (JTabbedPane abas : componentes(tela.getContentPane(), JTabbedPane.class)) {
                        for (int aba = 0; aba < abas.getTabCount(); aba++) {
                            abas.setSelectedIndex(aba); tela.validate();
                            capturarTela(tela, imagens, tela.getClass().getSimpleName()+"-aba"+aba);
                            for (JScrollPane scroll : componentes(abas.getSelectedComponent(), JScrollPane.class)) {
                                scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
                            }
                            capturarTela(tela, imagens, tela.getClass().getSimpleName()+"-aba"+aba+"-fim");
                        }
                    }
                    if (tela instanceof view.NovoClienteModal || tela instanceof view.MinhaPaginaView || tela instanceof view.RelatoriosView || tela instanceof view.ImovelView) {
                        java.util.List<JScrollPane> rolagens = componentes(tela.getContentPane(), JScrollPane.class);
                        if (!rolagens.isEmpty()) rolagens.get(0).getVerticalScrollBar().setValue(rolagens.get(0).getVerticalScrollBar().getMaximum());
                        capturarTela(tela, imagens, tela.getClass().getSimpleName()+"-fim");
                    }
                }
                view.NovoClienteModal empresa = new view.NovoClienteModal(new ClienteDao().listarPj().get(0));
                telas.add(empresa); util.Tema.aplicar(empresa); empresa.validate();
                capturarTela(empresa, imagens, "NovoClienteModal-PJ");
            } catch(Exception e){throw new RuntimeException(e);} finally {telas.forEach(JFrame::dispose);}
        });
    }

    private <T> java.util.List<T> componentes(java.awt.Component raiz, Class<T> tipo) {
        java.util.List<T> encontrados = new ArrayList<>();
        if (tipo.isInstance(raiz)) encontrados.add(tipo.cast(raiz));
        if (raiz instanceof java.awt.Container container)
            for (java.awt.Component filho : container.getComponents()) encontrados.addAll(componentes(filho, tipo));
        return encontrados;
    }
    private void capturarTela(JFrame tela, Path pasta, String nome) throws Exception {
        tela.validate();
        for (var atributo : tela.getClass().getDeclaredFields()) {
            atributo.setAccessible(true);
            Object valor = atributo.get(tela);
            if (!(valor instanceof JTextField || valor instanceof JComboBox<?> || valor instanceof JButton)) continue;
            java.awt.Component controle = (java.awt.Component)valor;
            if (!SwingUtilities.isDescendingFrom(controle, tela.getContentPane())) continue;
            boolean visivel = true;
            for (java.awt.Component c = controle; c != null && c != tela; c = c.getParent()) visivel &= c.isVisible();
            if (visivel) assertTrue(controle.getWidth() >= 24 && controle.getHeight() >= 24,
                    nome+" / "+atributo.getName()+" está comprimido: "+controle.getSize());
        }
        var conteudo = tela.getContentPane();
        java.awt.image.BufferedImage imagem = new java.awt.image.BufferedImage(conteudo.getWidth(), conteudo.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
        var g = imagem.createGraphics(); conteudo.printAll(g); g.dispose();
        javax.imageio.ImageIO.write(imagem, "png", pasta.resolve(nome+".png").toFile());
    }

    private JOptionPane mensagemEm(java.awt.Component componente) {
        if (componente instanceof JOptionPane mensagem) return mensagem;
        if (componente instanceof java.awt.Container container) {
            for (java.awt.Component filho : container.getComponents()) {
                JOptionPane mensagem=mensagemEm(filho);
                if (mensagem!=null) return mensagem;
            }
        }
        return null;
    }
    @Test @Order(12) void botaoSalvarClienteConfirmaGravacaoERejeitaCpfInvalidoSemFechar() throws Exception {
        SwingUtilities.invokeAndWait(()->{
            view.NovoClienteModal tela=new view.NovoClienteModal();
            java.util.List<String> mensagens=new ArrayList<>();
            javax.swing.Timer fecharMensagem=new javax.swing.Timer(100,e->{
                for (java.awt.Window janela:tela.getOwnedWindows()) {
                    if (janela instanceof JDialog && janela.isVisible()) {
                        JOptionPane mensagem=mensagemEm(janela);
                        if(mensagem!=null) { mensagens.add(String.valueOf(mensagem.getMessage())); janela.dispose(); }
                    }
                }
            });
            fecharMensagem.start();
            try {
                ((JComboBox<?>)campo(tela,"cbxTipoCliente")).setSelectedIndex(1);
                ((JTextField)campo(tela,"txtNome")).setText("Cliente novo pelo botão");
                ((JTextField)campo(tela,"txtCpf")).setText("11111111111");
                ((JButton)campo(tela,"btnSalvar")).doClick();
                assertTrue(mensagens.get(0).contains("CPF inválido"));
                assertTrue(tela.isDisplayable(),"O formulário deve permanecer aberto quando o CPF é inválido");
                ((JTextField)campo(tela,"txtCpf")).setText("111.444.777-35");
                ((JTextField)campo(tela,"txtNascimento")).setText("15/02/1960");
                ((JButton)campo(tela,"btnSalvar")).doClick();
                assertTrue(mensagens.get(1).startsWith("Cliente salvo com sucesso! Código: "));
                assertFalse(tela.isDisplayable());
            } catch(Exception e){throw new RuntimeException(e);} finally {fecharMensagem.stop();tela.dispose();}
        });
        try(Connection c=DriverManager.getConnection(servidor+schema,usuario,senha);PreparedStatement q=c.prepareStatement("SELECT pf.nome, pf.cpf FROM cliente_pf pf JOIN cliente c ON c.id=pf.id WHERE pf.cpf=?")) {
            q.setString(1,"11144477735");
            try(ResultSet r=q.executeQuery()) { assertTrue(r.next()); assertEquals("Cliente novo pelo botão",r.getString(1)); assertFalse(r.next()); }
        }
    }
}

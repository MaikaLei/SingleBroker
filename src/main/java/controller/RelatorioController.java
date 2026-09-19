package controller;
import dao.*;
import model.*;
import java.time.LocalDate;
import java.util.*;
public class RelatorioController {
    public record Resultado(String titulo, String texto, int quantidade) { }
    private boolean periodo(LocalDate data,LocalDate inicio,LocalDate fim) {
        if(inicio==null&&fim==null) return true;
        return data!=null&&(inicio==null||!data.isBefore(inicio))&&(fim==null||!data.isAfter(fim));
    }
    private boolean usuario(UsuarioModel dono,Long id) { return id==null||(dono!=null&&id.equals(dono.getId())); }
    private LocalDate dataImovel(ImovelModel i) {
        if(i.getDataCadastro()!=null) return i.getDataCadastro();
        try { return util.Validador.data(i.getDataEntrada(),"entrada"); } catch(IllegalArgumentException e) { return null; }
    }
    public Resultado gerar(String tipo,LocalDate inicio,LocalDate fim,Long usuarioId) {
        if(inicio!=null&&fim!=null&&inicio.isAfter(fim)) throw new IllegalArgumentException("A data inicial deve ser anterior ou igual à final.");
        List<ImovelModel> imoveis=new ImovelDao().listar();
        StringBuilder texto=new StringBuilder("Período: "+(inicio==null?"sem início":inicio)+" a "+(fim==null?"sem fim":fim)+"\n\n");
        int total=0;
        if(tipo.equals("Lista de Clientes")||tipo.equals("Lista de Proprietários")) {
            List<ClienteModel> clientes=new ArrayList<>();
            clientes.addAll(new ClienteDao().listarPf()); clientes.addAll(new ClienteDao().listarPj());
            Set<Long> proprietarios=new HashSet<>();
            for(ImovelModel i:imoveis) if(i.getProprietario()!=null && usuario(i.getUsuarioCadastro(),usuarioId)) proprietarios.add(i.getProprietario().getId());
            for(ClienteModel c:clientes) {
                boolean dono=tipo.equals("Lista de Proprietários");
                if(dono&&!proprietarios.contains(c.getId())) continue;
                if(!dono&&!usuario(c.getUsuarioCadastro(),usuarioId)) continue;
                if(!periodo(c.getDataCadastro(),inicio,fim)) continue;
                String nome=c instanceof ClientePfModel pf?pf.getNome():((ClientePjModel)c).getRazaoSocial();
                texto.append(c.getId()).append(" | ").append(nome).append(" | ").append(Objects.toString(c.getTelefone(), "")).append(" | ").append(Objects.toString(c.getEmail(), "")).append("\n"); total++;
            }
        } else {
            for(ImovelModel i:imoveis) {
                if(!usuario(i.getUsuarioCadastro(),usuarioId)) continue;
                if(tipo.equals("Lista de ativos")&&!"Ativo".equals(i.getStatusImovel())) continue;
                if(tipo.equals("Vendidos")&&!"Vendido".equals(i.getStatusImovel())) continue;
                if(!periodo(tipo.equals("Vendidos")?i.getDataVenda():dataImovel(i),inicio,fim)) continue;
                texto.append("Código ").append(i.getId()).append(" | ").append(i.getTipoImovel()).append(" | ").append(i.getStatusImovel()).append("\n")
                    .append(Objects.toString(i.getEndereco(), "")).append(" - ").append(Objects.toString(i.getCidade(), "")).append("\n")
                    .append("Transação: ").append(i.getTransacao()).append(" | Valor: ").append(Objects.toString(i.getValor(), "")).append("\n\n"); total++;
            }
        }
        texto.append("\nTotal: ").append(total);
        if(inicio!=null||fim!=null||usuarioId!=null) texto.append("\nRegistros antigos sem data ou usuário correspondente não entram no filtro.");
        return new Resultado(tipo,texto.toString(),total);
    }
}

package util;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;

/** Identidade visual compartilhada, fora dos blocos gerados pelo NetBeans. */
public final class Tema {
    public static final Color FUNDO = new Color(0xF3F6FA);
    public static final Color TEXTO = new Color(0x24364B);
    public static final Color SUAVE = new Color(0x60748B);
    public static final Color AZUL = new Color(0x1763A6);
    public static final Color LINHA = new Color(0xDCE4ED);
    public static final Color SELECAO = new Color(0xE6F0FA);
    private static final Map<String, String> TITULOS = Map.ofEntries(
        Map.entry("LoginView", "Acessar sua conta"), Map.entry("ListaImovelView", "Imóveis"),
        Map.entry("NovoImovelView", "Cadastro de imóvel"), Map.entry("ImovelView", "Detalhes do imóvel"),
        Map.entry("ClienteView", "Clientes"), Map.entry("NovoClienteModal", "Cadastro de cliente"),
        Map.entry("SelecionarClienteView", "Selecionar proprietário"), Map.entry("AgendaView", "Minha agenda"),
        Map.entry("AgendaModal", "Agendamentos do dia"), Map.entry("AgendaHoraModal", "Agendamento"),
        Map.entry("RelatoriosView", "Relatórios"), Map.entry("MinhaPaginaView", "Minha página"),
        Map.entry("CriativosView", "Criativos"), Map.entry("ListaUsuarioModal", "Usuários"),
        Map.entry("cadastroUsuarioModalView", "Cadastro de usuário"), Map.entry("FotosModal", "Fotos do imóvel"),
        Map.entry("DocumentosModal", "Documentos do imóvel"));

    private Tema() { }

    public static void instalar() {
        if (UIManager.getLookAndFeel() instanceof FlatLightLaf) return;
        FlatLightLaf.setup();
        UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Component.focusColor", AZUL);
        UIManager.put("Component.borderColor", LINHA);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Button.default.background", AZUL);
        UIManager.put("Button.default.foreground", Color.WHITE);
        UIManager.put("Panel.background", FUNDO);
        UIManager.put("Table.selectionBackground", SELECAO);
        UIManager.put("Table.selectionForeground", TEXTO);
        UIManager.put("Table.alternateRowColor", new Color(0xF8FAFD));
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollPane.smoothScrolling", true);
        UIManager.put("TitlePane.background", Color.WHITE);
        UIManager.put("TitlePane.foreground", TEXTO);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
    }

    public static void aplicar(JFrame tela) {
        instalar();
        String titulo = TITULOS.getOrDefault(tela.getClass().getSimpleName(), "SingleBroker");
        tela.setTitle(titulo + " · SingleBroker");
        estilizar(tela.getContentPane());
        if (!Boolean.TRUE.equals(tela.getRootPane().getClientProperty("tema.janela"))) {
            tela.getRootPane().putClientProperty("tema.janela", true);
            configurarMenu(tela.getContentPane(), tela.getClass().getSimpleName());
            tela.setLocationRelativeTo(null);
        }
        tela.getContentPane().setBackground(FUNDO);
        tela.revalidate();
        tela.repaint();
    }

    public static void estilizar(Component componente) {
        if (!(componente instanceof JComponent c)) return;
        if (!Boolean.TRUE.equals(c.getClientProperty("tema.aplicado"))) {
            c.putClientProperty("tema.aplicado", true);
            Font antiga = c.getFont();
            int tamanho = antiga == null ? 13 : antiga.getSize();
            c.setForeground(TEXTO);
            c.setFont(new Font("Segoe UI", antiga != null && antiga.isBold() ? Font.BOLD : Font.PLAIN,
                    tamanho >= 28 ? 36 : tamanho >= 18 ? 18 : 13));
            if (c instanceof JPanel p) {
                p.setBackground(Color.WHITE);
                if (p.getBorder() != null) p.setBorder(new LineBorder(LINHA, 1, true));
            } else if (c instanceof JTextComponent texto) {
                texto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                texto.setBackground(texto.isEditable() ? Color.WHITE : FUNDO);
                texto.setCaretColor(AZUL);
                texto.setBorder(UIManager.getBorder(texto instanceof JTextArea ? "TextArea.border" : "TextField.border"));
                if (texto instanceof JTextArea area) { area.setLineWrap(true); area.setWrapStyleWord(true); }
            } else if (c instanceof JButton botao) {
                botao.setBorder(UIManager.getBorder("Button.border"));
                botao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                botao.setMargin(new Insets(4, 10, 4, 10));
                botao.setBackground(Color.WHITE);
                botao.setForeground(TEXTO);
                botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                String texto = botao.getText() == null ? "" : botao.getText().toLowerCase(java.util.Locale.ROOT);
                if (texto.matches(".*(salvar|entrar|novo|nova|gerar|agendar|selecionar|buscar|adicionar|editar).*")) {
                    botao.setBackground(AZUL); botao.setForeground(Color.WHITE);
                    botao.setIcon(null);
                }
                if (texto.contains("excluir") || texto.contains("remover")) {
                    botao.setBackground(new Color(0xFFF1F2)); botao.setForeground(new Color(0xB42338));
                }
                if (botao.getClientProperty("data") != null || texto.matches("\\d+( \\(\\d+\\))?")) dia(botao);
            } else if (c instanceof JComboBox<?>) {
                c.setBackground(Color.WHITE);
                c.setBorder(UIManager.getBorder("ComboBox.border"));
            } else if (c instanceof JTable tabela) {
                tabela.setRowHeight(34);
                tabela.setShowVerticalLines(false);
                tabela.setShowHorizontalLines(true);
                tabela.setGridColor(new Color(0xEDF1F6));
                tabela.setIntercellSpacing(new Dimension(0, 1));
                tabela.setBackground(Color.WHITE);
                tabela.setSelectionBackground(SELECAO);
                tabela.setSelectionForeground(TEXTO);
                tabela.setFillsViewportHeight(true);
                tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
                tabela.getTableHeader().setBackground(FUNDO);
                tabela.getTableHeader().setForeground(SUAVE);
                tabela.getTableHeader().setPreferredSize(new Dimension(0, 36));
                tabela.getTableHeader().setReorderingAllowed(false);
            } else if (c instanceof JScrollPane rolagem) {
                rolagem.setBorder(new LineBorder(LINHA));
                rolagem.getViewport().setBackground(Color.WHITE);
                rolagem.getVerticalScrollBar().setUnitIncrement(20);
            } else if (c instanceof JRadioButton || c instanceof JCheckBox) {
                c.setBackground(Color.WHITE);
            } else if (c instanceof JLabel rotulo) {
                if (rotulo.getBorder() != null) rotulo.setBorder(null);
                if (tamanho < 18) rotulo.setForeground(SUAVE);
            } else if (c instanceof JList<?> lista) {
                lista.setBackground(Color.WHITE); lista.setFixedCellHeight(32);
                lista.setSelectionBackground(SELECAO); lista.setSelectionForeground(TEXTO);
            }
        }
        for (Component filho : c.getComponents()) estilizar(filho);
    }

    public static void dia(JButton botao) {
        Object data = botao.getClientProperty("data");
        boolean hoje = java.time.LocalDate.now().equals(data);
        botao.setBackground(hoje ? AZUL : Color.WHITE);
        botao.setForeground(hoje ? Color.WHITE : TEXTO);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBorder(UIManager.getBorder("Button.border"));
    }

    private static void configurarMenu(Container container, String tela) {
        java.util.List<JLabel> links = new java.util.ArrayList<>();
        JLabel logo = null;
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel l) {
                if (secao(l.getText()) != null) links.add(l);
                else if (l.getIcon() != null) logo = l;
            }
        }
        if (links.size() >= 5 && container instanceof JPanel painel) {
            java.util.List<String> ordem = java.util.List.of("Imóveis", "Clientes", "Criativos", "Minha agenda", "Minha página", "Relatórios", "Usuários");
            links.sort(java.util.Comparator.comparingInt(l -> ordem.indexOf(secao(l.getText()))));
            painel.removeAll();
            painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
            painel.setBackground(Color.WHITE);
            painel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 0, 1, LINHA),
                    BorderFactory.createEmptyBorder(20, 14, 20, 14)));
            if (logo != null) {
                logo.setAlignmentX(0.5f); painel.add(logo); painel.add(Box.createVerticalStrut(24));
            }
            String ativa = tela.contains("Imovel") ? "Imóveis" : tela.contains("Cliente") ? "Clientes" :
                tela.contains("Agenda") ? "Minha agenda" : tela.contains("Usuario") ? "Usuários" :
                tela.equals("MinhaPaginaView") ? "Minha página" : tela.equals("RelatoriosView") ? "Relatórios" : "Criativos";
            for (JLabel link : links) {
                String nome = secao(link.getText());
                link.setText(nome);
                link.setOpaque(true); link.setFocusable(true);
                link.setFont(new Font("Segoe UI", Font.BOLD, 14));
                link.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 4));
                link.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
                link.setAlignmentX(0.5f);
                Color fundo = nome.equals(ativa) ? SELECAO : Color.WHITE;
                link.setBackground(fundo); link.setForeground(nome.equals(ativa) ? AZUL : SUAVE);
                link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                link.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { link.setBackground(SELECAO); link.setForeground(AZUL); }
                    public void mouseExited(MouseEvent e) { link.setBackground(fundo); link.setForeground(nome.equals(ativa) ? AZUL : SUAVE); }
                });
                link.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "abrir");
                link.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "abrir");
                link.getActionMap().put("abrir", new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        MouseEvent clique = new MouseEvent(link, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 1, 1, 1, false);
                        for (MouseListener ouvinte : link.getMouseListeners()) ouvinte.mouseClicked(clique);
                    }
                });
                link.addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) { link.setBackground(SELECAO); }
                    public void focusLost(FocusEvent e) { link.setBackground(fundo); }
                });
                painel.add(link); painel.add(Box.createVerticalStrut(4));
            }
            painel.add(Box.createVerticalGlue());
            return;
        }
        for (Component c : container.getComponents()) if (c instanceof Container filho) configurarMenu(filho, tela);
    }

    private static String secao(String texto) {
        if (texto == null) return null;
        return switch (texto.toUpperCase(java.util.Locale.ROOT)) {
            case "IMÓVEIS" -> "Imóveis"; case "CLIENTES" -> "Clientes"; case "CRIATIVOS" -> "Criativos";
            case "MINHA AGENDA" -> "Minha agenda"; case "MINHA PÁGINA" -> "Minha página";
            case "RELATÓRIOS" -> "Relatórios"; case "USUÁRIOS" -> "Usuários"; default -> null;
        };
    }
}

package util;

import java.awt.*;
import javax.swing.*;

/** Composição das telas com os mesmos controles e eventos do editor visual. */
public final class LayoutTela {
    private LayoutTela() { }

    public static JPanel painel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        p.putClientProperty("tema.aplicado", true);
        return p;
    }

    public static JLabel titulo(String texto, int tamanho) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", tamanho >= 16 ? Font.BOLD : Font.PLAIN, tamanho));
        l.setForeground(tamanho >= 16 ? Tema.TEXTO : Tema.SUAVE);
        l.putClientProperty("tema.aplicado", true);
        return l;
    }

    public static JPanel campo(String nome, JComponent controle) {
        JPanel p = painel(new BorderLayout(0, 7));
        JLabel label = titulo(nome, 13);
        label.setLabelFor(controle);
        controle.getAccessibleContext().setAccessibleName(nome);
        if (controle instanceof JTextField || controle instanceof JComboBox<?>) {
            controle.setPreferredSize(new Dimension(150, 36));
            controle.setMinimumSize(new Dimension(70, 36));
        }
        p.add(label, BorderLayout.NORTH);
        p.add(controle, BorderLayout.CENTER);
        return p;
    }

    public static JPanel grade(int colunas, JComponent... itens) {
        JPanel p = painel(new GridLayout(0, colunas, 16, 16));
        for (JComponent item : itens) p.add(item);
        return p;
    }

    public static JPanel coluna(JComponent... itens) {
        JPanel p = painel(new GridBagLayout());
        int linha = 0;
        for (JComponent item : itens) {
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0; g.gridy = linha++; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
            g.anchor = GridBagConstraints.NORTHWEST;
            g.insets = new Insets(0, 0, linha == itens.length ? 0 : 18, 0);
            p.add(item, g);
        }
        return p;
    }

    public static JPanel cartao(String nome, JComponent conteudo) {
        JPanel p = painel(new BorderLayout(0, 18));
        p.setOpaque(true); p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Tema.LINHA),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        if (!nome.isBlank()) p.add(titulo(nome, 17), BorderLayout.NORTH);
        p.add(conteudo, BorderLayout.CENTER);
        return p;
    }

    public static JPanel listagem(JComponent filtros, JComponent tabela) {
        JPanel p = painel(new BorderLayout(0, 18));
        p.putClientProperty("layout.expandir", true);
        p.add(filtros, BorderLayout.NORTH);
        p.add(tabela, BorderLayout.CENTER);
        return p;
    }

    public static JComponent expandir(JComponent conteudo) {
        conteudo.putClientProperty("layout.expandir", true);
        return conteudo;
    }

    public static JPanel resultado(String dica, JScrollPane tabela) {
        JPanel p = painel(new BorderLayout(0, 14));
        p.add(titulo(dica, 13), BorderLayout.NORTH);
        p.add(tabela, BorderLayout.CENTER);
        return p;
    }

    public static JPanel acoes(JComponent... itens) {
        JPanel p = painel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        for (JComponent item : itens) {
            if (item instanceof JButton b) {
                int icone = b.getIcon() == null ? 0 : b.getIcon().getIconWidth() + b.getIconTextGap();
                int largura = Math.max(108, b.getFontMetrics(new Font("Segoe UI", Font.PLAIN, 13)).stringWidth(b.getText()) + 32 + icone);
                b.setPreferredSize(new Dimension(largura, 36));
            }
            p.add(item);
        }
        JPanel alinhado = painel(new BorderLayout());
        alinhado.add(p, BorderLayout.SOUTH);
        return alinhado;
    }

    public static JScrollPane texto(JTextArea area, int altura) {
        area.setLineWrap(true); area.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(240, altura));
        return scroll;
    }

    public static JScrollPane rolar(JComponent conteudo) {
        JScrollPane scroll = new JScrollPane(new PainelRolavel(conteudo));
        scroll.setPreferredSize(new Dimension(800, 490));
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        return scroll;
    }

    public static void pagina(JFrame tela, JPanel menu, String nome, String descricao, JComponent conteudo, JComponent rodape) {
        montar(tela, menu, nome, descricao, conteudo, rodape, 1220, 780);
    }

    public static void modal(JFrame tela, String nome, String descricao, JComponent conteudo, JComponent rodape, int largura, int altura) {
        montar(tela, null, nome, descricao, conteudo, rodape, largura, altura);
    }

    private static void montar(JFrame tela, JPanel menu, String nome, String descricao, JComponent conteudo, JComponent rodape, int largura, int altura) {
        JPanel raiz = painel(new BorderLayout());
        raiz.setOpaque(true); raiz.setBackground(Tema.FUNDO);
        if (menu != null) { menu.setPreferredSize(new Dimension(208, 600)); raiz.add(menu, BorderLayout.WEST); }
        JPanel principal = painel(new BorderLayout(0, 22));
        principal.setBorder(BorderFactory.createEmptyBorder(26, 28, 24, 28));
        JPanel cabecalho = painel(new BorderLayout(0, 7));
        cabecalho.add(titulo(nome, 26), BorderLayout.NORTH);
        if (!descricao.isBlank()) cabecalho.add(titulo(descricao, 13), BorderLayout.SOUTH);
        principal.add(cabecalho, BorderLayout.NORTH);
        PainelRolavel rolavel = new PainelRolavel(conteudo);
        JScrollPane scroll = new JScrollPane(rolavel);
        scroll.setBorder(null); scroll.putClientProperty("tema.aplicado", true);
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        principal.add(conteudo instanceof JTabbedPane || Boolean.TRUE.equals(conteudo.getClientProperty("layout.expandir"))
                ? conteudo : scroll, BorderLayout.CENTER);
        if (rodape != null) principal.add(rodape, BorderLayout.SOUTH);
        raiz.add(principal, BorderLayout.CENTER);
        tela.setContentPane(raiz);
        Dimension disponivel = Toolkit.getDefaultToolkit().getScreenSize();
        tela.setSize(Math.min(largura, disponivel.width - 40), Math.min(altura, disponivel.height - 70));
        int minimo = Math.min(largura, menu == null ? 540 : 980);
        if (rodape != null) minimo = Math.max(minimo, rodape.getPreferredSize().width + (menu == null ? 56 : 264));
        tela.setMinimumSize(new Dimension(minimo, 500));
    }

    private static final class PainelRolavel extends JPanel implements Scrollable {
        PainelRolavel(JComponent conteudo) {
            super(new BorderLayout()); setOpaque(false); putClientProperty("tema.aplicado", true);
            add(conteudo, BorderLayout.NORTH);
        }
        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction) { return 24; }
        public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction) { return Math.max(24, r.height - 24); }
        public boolean getScrollableTracksViewportWidth() { return true; }
        public boolean getScrollableTracksViewportHeight() { return false; }
    }
}

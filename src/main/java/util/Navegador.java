package util;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public final class Navegador {
    private Navegador() { }
    public static boolean podeSair(JFrame tela, boolean alterado) {
        boolean mudou = tela instanceof FormularioEditavel f ? f.temAlteracoes() : alterado;
        if (!mudou) return true;
        if (tela instanceof FormularioEditavel f) {
            int resposta = JOptionPane.showConfirmDialog(tela, "Deseja salvar as alterações antes de sair?", "Alterações não salvas", JOptionPane.YES_NO_CANCEL_OPTION);
            if (resposta == JOptionPane.YES_OPTION) return f.salvarAlteracoes();
            return resposta == JOptionPane.NO_OPTION;
        }
        return JOptionPane.showConfirmDialog(tela, "Descartar as alterações e sair?", "Alterações não salvas", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    public static void abrirTela(JFrame atual, JFrame proxima, boolean alterado) {
        if (!podeSair(atual, alterado)) { proxima.dispose(); return; }
        proxima.setVisible(true);
        atual.dispose();
    }
}

package util;
import java.awt.Component;
import java.awt.Container;
import javax.swing.*;
import javax.swing.event.*;
public final class AlteracoesFormulario {
    private AlteracoesFormulario() { }
    public static void observar(Container raiz, Runnable alterado) {
        for (Component c : raiz.getComponents()) {
            if (c instanceof javax.swing.text.JTextComponent texto) {
                texto.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { alterado.run(); }
                    public void removeUpdate(DocumentEvent e) { alterado.run(); }
                    public void changedUpdate(DocumentEvent e) { alterado.run(); }
                });
            } else if (c instanceof JComboBox<?> combo) combo.addActionListener(e -> alterado.run());
            else if (c instanceof JToggleButton botao) botao.addItemListener(e -> alterado.run());
            if (c instanceof Container filho) observar(filho, alterado);
        }
    }
}

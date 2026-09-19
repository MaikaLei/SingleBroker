package util;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/** Formata datas durante a digitação, preservando os campos do editor do NetBeans. */
public final class MascaraData {
    private MascaraData() { }

    public static void aplicar(JTextField... campos) {
        for (JTextField campo : campos) {
            ((AbstractDocument) campo.getDocument()).setDocumentFilter(new Filtro(campo));
            campo.setToolTipText("Digite dia, mês e ano: dd/mm/aaaa. As barras são automáticas.");
        }
    }

    private static final class Filtro extends DocumentFilter {
        private final JTextField campo;
        private Filtro(JTextField campo) { this.campo = campo; }

        @Override public void insertString(FilterBypass fb, int offset, String texto, AttributeSet attrs)
                throws BadLocationException { replace(fb, offset, 0, texto, attrs); }

        @Override public void remove(FilterBypass fb, int offset, int length)
                throws BadLocationException { replace(fb, offset, length, "", null); }

        @Override public void replace(FilterBypass fb, int offset, int length, String texto, AttributeSet attrs)
                throws BadLocationException {
            String atual = fb.getDocument().getText(0, fb.getDocument().getLength());
            texto = texto == null ? "" : texto.trim();
            // Datas antigas do banco também podem estar no formato ISO.
            if (offset == 0 && length == atual.length() && texto.matches("\\d{4}-\\d{2}-\\d{2}"))
                texto = texto.substring(8) + texto.substring(5, 7) + texto.substring(0, 4);
            if (!texto.matches("[0-9/]*")) return;
            int inicio = digitos(atual.substring(0, offset)).length();
            int fim = digitos(atual.substring(0, offset + length)).length();
            // Apagar uma barra com Backspace também apaga o dígito anterior.
            if (texto.isEmpty() && length == 1 && inicio == fim && inicio > 0) inicio--;
            String numeros = digitos(atual);
            String inseridos = digitos(texto);
            String novo = numeros.substring(0, inicio) + inseridos + numeros.substring(fim);
            if (novo.length() > 8) return;
            StringBuilder formatado = new StringBuilder();
            for (int i = 0; i < novo.length(); i++) {
                if (i == 2 || i == 4) formatado.append('/');
                formatado.append(novo.charAt(i));
            }
            fb.replace(0, atual.length(), formatado.toString(), attrs);
            int cursor = inicio + inseridos.length();
            int posicao = cursor + (cursor > 2 ? 1 : 0) + (cursor > 4 ? 1 : 0);
            campo.setCaretPosition(Math.min(posicao, formatado.length()));
        }

        private static String digitos(String texto) { return texto.replace("/", ""); }
    }
}

import org.junit.jupiter.api.Test;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import static org.junit.jupiter.api.Assertions.*;

class MascaraDataTest {
    @Test void digitarColarEditarEApagarDatas() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JTextField campo = new JTextField();
            util.MascaraData.aplicar(campo);
            for (char numero : "19092026".toCharArray()) campo.replaceSelection(String.valueOf(numero));
            assertEquals("19/09/2026", campo.getText());
            campo.select(3, 5);
            campo.replaceSelection("12");
            assertEquals("19/12/2026", campo.getText());
            campo.selectAll();
            campo.replaceSelection("01022000");
            assertEquals("01/02/2000", campo.getText());
            campo.setText("2026-09-19");
            assertEquals("19/09/2026", campo.getText());
            campo.setText("29/02/2024");
            assertEquals(java.time.LocalDate.of(2024, 2, 29), util.Validador.data(campo.getText(), "a data"));
            campo.setText("31022026");
            assertThrows(IllegalArgumentException.class, () -> util.Validador.data(campo.getText(), "a data"));
            campo.selectAll();
            campo.replaceSelection("");
            assertEquals("", campo.getText());
            assertNull(util.Validador.data(campo.getText(), "a data"));
            campo.replaceSelection("abc");
            assertEquals("", campo.getText());
            campo.setText("19/09/2026");
            while (!campo.getText().isEmpty()) {
                int fim = campo.getText().length();
                campo.select(fim - 1, fim);
                campo.replaceSelection("");
            }
            assertEquals("", campo.getText());
        });
    }
}

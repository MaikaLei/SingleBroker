package util;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public final class Validador {
    private Validador() { }
    public static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("Preencha " + campo + ".");
        return valor.trim();
    }
    public static String email(String valor, boolean obrigatorio) {
        String email = valor == null ? "" : valor.trim();
        if (email.isEmpty() && !obrigatorio) return email;
        if (!email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new IllegalArgumentException("Informe um e-mail válido.");
        return email;
    }
    public static Integer inteiro(String valor, String campo) {
        if (valor == null || valor.isBlank()) return null;
        try {
            int numero = Integer.parseInt(valor.trim());
            if (numero < 0) throw new NumberFormatException();
            return numero;
        } catch (NumberFormatException e) { throw new IllegalArgumentException(campo + " deve ser um inteiro maior ou igual a zero."); }
    }
    public static BigDecimal decimal(String valor, String campo) {
        if (valor == null || valor.isBlank()) return null;
        String normalizado = valor.trim().replace("R$", "").trim();
        if (normalizado.contains(",")) normalizado = normalizado.replace(".", "").replace(',', '.');
        try {
            BigDecimal numero = new BigDecimal(normalizado);
            if (numero.signum() < 0) throw new NumberFormatException();
            return numero;
        } catch (NumberFormatException e) { throw new IllegalArgumentException("Informe um valor válido para " + campo + "."); }
    }
    public static LocalDate data(String valor, String campo) {
        if (valor == null || valor.isBlank()) return null;
        try {
            return valor.contains("/") ? LocalDate.parse(valor.trim(), DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)) : LocalDate.parse(valor.trim());
        } catch (java.time.format.DateTimeParseException e) { throw new IllegalArgumentException("Informe " + campo + " no formato dd/MM/aaaa."); }
    }
    public static String documento(String valor, String tipo) {
        String digitos = obrigatorio(valor, tipo).replaceAll("[.\\-/\\s]", "");
        int tamanho = tipo.equals("CPF") ? 11 : 14;
        if (!digitos.matches("\\d{" + tamanho + "}") || digitos.matches("(\\d)\\1+")) throw new IllegalArgumentException(tipo + " inválido.");
        for (int pos = tamanho - 2; pos < tamanho; pos++) {
            int soma = 0;
            for (int i = 0; i < pos; i++) {
                int peso = tamanho == 11 ? pos + 1 - i : (pos - 1 - i) % 8 + 2;
                soma += (digitos.charAt(i) - '0') * peso;
            }
            int resto = soma % 11;
            if ((resto < 2 ? 0 : 11 - resto) != digitos.charAt(pos) - '0') throw new IllegalArgumentException(tipo + " inválido.");
        }
        return digitos;
    }
}

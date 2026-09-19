package util;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Senhas legadas são migradas somente depois de uma autenticação válida. */
public final class Senhas {
    private static final int ITERACOES = 210000;
    private Senhas() { }
    public static boolean isHash(String valor) { return valor != null && valor.startsWith("pbkdf2$"); }
    public static String gerar(String senha) {
        if (senha == null || senha.length() < 6) throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        return gerarLegada(senha);
    }
    public static String gerarLegada(String senha) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return "pbkdf2$" + ITERACOES + "$" + Base64.getEncoder().encodeToString(salt)
            + "$" + Base64.getEncoder().encodeToString(derivar(senha, salt, ITERACOES));
    }
    public static boolean verificar(String senha, String salvo) {
        if (senha == null || salvo == null) return false;
        if (!isHash(salvo)) return MessageDigest.isEqual(senha.getBytes(java.nio.charset.StandardCharsets.UTF_8), salvo.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        try {
            String[] p = salvo.split("\\$");
            if (p.length != 4) return false;
            int iteracoes = Integer.parseInt(p[1]);
            if (iteracoes < 1 || iteracoes > 1000000) return false;
            return MessageDigest.isEqual(Base64.getDecoder().decode(p[3]), derivar(senha, Base64.getDecoder().decode(p[2]), iteracoes));
        } catch (IllegalArgumentException e) { return false; }
    }
    private static byte[] derivar(String senha, byte[] salt, int iteracoes) {
        PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, iteracoes, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch (java.security.GeneralSecurityException e) { throw new IllegalStateException("Não foi possível proteger a senha.", e); }
        finally { spec.clearPassword(); }
    }
}

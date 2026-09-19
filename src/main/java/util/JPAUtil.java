package util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class JPAUtil {
    private static EntityManagerFactory emf;
    private JPAUtil() { }
    public static synchronized EntityManager getEntityManager() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("SingleBroker-PU", configuracao());
        }
        return emf.createEntityManager();
    }
    private static Map<String, Object> configuracao() {
        Properties local = new Properties();
        Path arquivo = Path.of("singlebroker.local.properties");
        if (Files.exists(arquivo)) {
            try (var reader = Files.newBufferedReader(arquivo)) { local.load(reader); }
            catch (java.io.IOException e) { throw new IllegalStateException("Não foi possível ler a configuração local do banco.", e); }
        }
        Map<String, Object> config = new HashMap<>();
        configurar(config, local, "SINGLEBROKER_DB_URL", "db.url", "jakarta.persistence.jdbc.url");
        configurar(config, local, "SINGLEBROKER_DB_USER", "db.user", "jakarta.persistence.jdbc.user");
        configurar(config, local, "SINGLEBROKER_DB_PASSWORD", "db.password", "jakarta.persistence.jdbc.password");
        configurar(config, local, "SINGLEBROKER_DB_DDL", "db.ddl", "hibernate.hbm2ddl.auto");
        return config;
    }
    private static void configurar(Map<String, Object> config, Properties local, String ambiente, String chave, String propriedade) {
        String valor = System.getProperty(ambiente);
        if (valor == null) valor = System.getenv(ambiente);
        if (valor == null) valor = local.getProperty(chave);
        if (valor != null) config.put(propriedade, valor);
    }
    public static synchronized void close() {
        if (emf != null && emf.isOpen()) emf.close();
        emf = null;
    }
}

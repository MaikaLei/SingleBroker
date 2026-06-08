package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe responsável pelas operações de persistência do SingleBroker. Utiliza
 * JPA para acesso ao banco de dados.
 *
 * @author Maikon
 */
public class JPAUtil {

    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("SingleBroker-PU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}

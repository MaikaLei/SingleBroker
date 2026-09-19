package dao;

import jakarta.persistence.EntityManager;
import model.ClienteModel;
import util.JPAUtil;
import java.util.List;
import model.ClientePfModel;
import model.ClientePjModel;

public class ClienteDao {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ClienteDao.class.getName());

    public void salvar(ClienteModel cliente) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            em.persist(cliente);

            em.getTransaction().commit();
            LOGGER.log(java.util.logging.Level.INFO, "Cliente: transação confirmada no MySQL, código {0}.", cliente.getId());

        } catch (Exception e) {

            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            throw new IllegalStateException("Não foi possível salvar a alteração no banco de dados.", e);

        } finally {

            em.close();

        }
    }

    public List<ClientePfModel> listarPf() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "FROM ClientePfModel",
                    ClientePfModel.class
            ).getResultList();

        } finally {

            em.close();

        }

    }

    public List<ClientePjModel> listarPj() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "FROM ClientePjModel",
                    ClientePjModel.class
            ).getResultList();

        } finally {

            em.close();

        }

    }

    public List<ClientePfModel> buscarPfPorNome(String nome) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "FROM ClientePfModel c WHERE UPPER(c.nome) LIKE UPPER(:nome)",
                    ClientePfModel.class
            )
                    .setParameter("nome", "%" + nome + "%")
                    .getResultList();

        } finally {

            em.close();

        }
    }

    public List<ClientePjModel> buscarPjPorNome(String nome) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "FROM ClientePjModel c WHERE UPPER(c.razaoSocial) LIKE UPPER(:nome)",
                    ClientePjModel.class
            )
                    .setParameter("nome", "%" + nome + "%")
                    .getResultList();

        } finally {

            em.close();

        }
    }

    public List<ClientePfModel> buscarPf(
            String nome,
            String cpf,
            String telefone) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    """
                FROM ClientePfModel c
                WHERE UPPER(c.nome) LIKE UPPER(:nome)
                AND REPLACE(REPLACE(COALESCE(c.cpf, ''), '.', ''), '-', '') LIKE :cpf
                AND COALESCE(c.telefone, '') LIKE :telefone
                """,
                    ClientePfModel.class)
                    .setParameter("nome", "%" + nome + "%")
                    .setParameter("cpf", "%" + cpf.replaceAll("[^0-9]", "") + "%")
                    .setParameter("telefone", "%" + telefone + "%")
                    .getResultList();

        } finally {

            em.close();

        }
    }

    public List<ClientePjModel> buscarPj(
            String nome,
            String cnpj,
            String telefone) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    """
                FROM ClientePjModel c
                WHERE UPPER(c.razaoSocial) LIKE UPPER(:nome)
                AND REPLACE(REPLACE(REPLACE(COALESCE(c.cnpj, ''), '.', ''), '-', ''), '/', '') LIKE :cnpj
                AND COALESCE(c.telefone, '') LIKE :telefone
                """,
                    ClientePjModel.class)
                    .setParameter("nome", "%" + nome + "%")
                    .setParameter("cnpj", "%" + cnpj.replaceAll("[^0-9]", "") + "%")
                    .setParameter("telefone", "%" + telefone + "%")
                    .getResultList();

        } finally {

            em.close();

        }
    }

    public ClienteModel buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.find(ClienteModel.class, id);

        } finally {

            em.close();

        }

    }

    public void atualizar(ClienteModel cliente) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            em.merge(cliente);

            em.getTransaction().commit();
            LOGGER.log(java.util.logging.Level.INFO, "Cliente: transação confirmada no MySQL, código {0}.", cliente.getId());

        } catch (Exception e) {

            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            throw new IllegalStateException("Não foi possível salvar a alteração no banco de dados.", e);

        } finally {

            em.close();

        }
    }

}

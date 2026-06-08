package dao;

import jakarta.persistence.EntityManager;
import model.ClienteModel;
import util.JPAUtil;
import java.util.List;
import model.ClientePfModel;
import model.ClientePjModel;

public class ClienteDao {

    public void salvar(ClienteModel cliente) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            em.persist(cliente);

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

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
                AND c.cpf LIKE :cpf
                AND c.telefone LIKE :telefone
                """,
                    ClientePfModel.class)
                    .setParameter("nome", "%" + nome + "%")
                    .setParameter("cpf", "%" + cpf + "%")
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
                AND c.cnpj LIKE :cnpj
                AND c.telefone LIKE :telefone
                """,
                    ClientePjModel.class)
                    .setParameter("nome", "%" + nome + "%")
                    .setParameter("cnpj", "%" + cnpj + "%")
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

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

        } finally {

            em.close();

        }
    }

}

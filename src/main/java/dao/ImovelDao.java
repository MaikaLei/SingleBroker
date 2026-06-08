package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.ImovelModel;
import util.JPAUtil;

/**
 *
 * @author Maikon
 */
public class ImovelDao {

    public void salvar(ImovelModel imovel) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            em.persist(imovel);

            em.getTransaction().commit();

        } catch (Exception e) {

            em.getTransaction().rollback();

            throw e;

        } finally {

            em.close();

        }
    }

    public List<ImovelModel> listar() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<ImovelModel> query = em.createQuery(
                    "FROM ImovelModel ORDER BY id DESC",
                    ImovelModel.class);

            return query.getResultList();

        } finally {

            em.close();

        }
    }

    public ImovelModel buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT i FROM ImovelModel i "
                    + "LEFT JOIN FETCH i.proprietario "
                    + "WHERE i.id = :id",
                    ImovelModel.class
            )
                    .setParameter("id", id)
                    .getSingleResult();

        } finally {
            em.close();
        }
    }

    public void atualizar(ImovelModel imovel) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            em.merge(imovel);

            em.getTransaction().commit();

        } catch (Exception e) {

            em.getTransaction().rollback();

            throw e;

        } finally {

            em.close();

        }
    }

    public List<ImovelModel> buscar(
            String codigo,
            String status,
            String transacao,
            String tipo,
            String cidade) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            String jpql
                    = "SELECT i FROM ImovelModel i "
                    + "WHERE 1=1 ";

            if (!codigo.isBlank()) {

                jpql += "AND i.id = :id ";
            }

            if (!status.equals("Status")
                    && !status.equals("Todos")) {

                jpql += "AND i.statusImovel = :status ";
            }

            if (!transacao.equals("Transação")) {

                jpql += "AND i.transacao = :transacao ";
            }

            if (!tipo.equals("Tipo")) {

                jpql += "AND i.tipoImovel = :tipo ";
            }

            if (!cidade.equals("Cidade")) {

                jpql += "AND i.cidade = :cidade ";
            }

            jpql += "ORDER BY i.id DESC";

            TypedQuery<ImovelModel> query
                    = em.createQuery(
                            jpql,
                            ImovelModel.class);

            if (!codigo.isBlank()) {

                query.setParameter(
                        "id",
                        Long.valueOf(codigo));
            }

            if (!status.equals("Status")
                    && !status.equals("Todos")) {

                query.setParameter(
                        "status",
                        status);
            }

            if (!transacao.equals("Transação")) {

                query.setParameter(
                        "transacao",
                        transacao);
            }

            if (!tipo.equals("Tipo")) {

                query.setParameter(
                        "tipo",
                        tipo);
            }

            if (!cidade.equals("Cidade")) {

                query.setParameter(
                        "cidade",
                        cidade);
            }

            return query.getResultList();

        } finally {

            em.close();

        }
    }
}

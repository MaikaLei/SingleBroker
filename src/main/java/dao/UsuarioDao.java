package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.UsuarioModel;
import util.JPAUtil;
import enums.PerfilUsuario;

/**
 *
 * @author Maikon
 */
public class UsuarioDao {

    public void salvar(UsuarioModel usuario) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            em.persist(usuario);

            em.getTransaction().commit();

        } catch (Exception e) {

            em.getTransaction().rollback();

            e.printStackTrace();

        } finally {

            em.close();

        }

    }

    public List<UsuarioModel> listar() {

        EntityManager em = JPAUtil.getEntityManager();

        List<UsuarioModel> usuarios = null;

        try {

            TypedQuery<UsuarioModel> query = em.createQuery(
                    "SELECT u FROM UsuarioModel u",
                    UsuarioModel.class
            );

            usuarios = query.getResultList();

        } finally {

            em.close();

        }

        return usuarios;

    }

    public UsuarioModel buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        UsuarioModel usuario = null;

        try {

            usuario = em.find(UsuarioModel.class, id);

        } finally {

            em.close();

        }

        return usuario;

    }

    public void excluir(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            UsuarioModel usuario = em.find(UsuarioModel.class, id);

            if (usuario != null) {

                em.getTransaction().begin();

                em.remove(usuario);

                em.getTransaction().commit();

            }

        } catch (Exception e) {

            em.getTransaction().rollback();

            e.printStackTrace();

        } finally {

            em.close();

        }

    }

    public void atualizar(UsuarioModel usuario) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            em.merge(usuario);

            em.getTransaction().commit();

        } catch (Exception e) {

            em.getTransaction().rollback();

            e.printStackTrace();

        } finally {

            em.close();

        }

    }

    public UsuarioModel autenticar(String email, String senha) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<UsuarioModel> query = em.createQuery(
                    "SELECT u FROM UsuarioModel u "
                    + "WHERE u.email = :email "
                    + "AND u.senha = :senha "
                    + "AND u.ativo = true",
                    UsuarioModel.class);

            query.setParameter("email", email);
            query.setParameter("senha", senha);

            UsuarioModel usuario = query.getSingleResult();

            em.getTransaction().begin();

            usuario.setUltimoLogin(java.time.LocalDateTime.now());

            em.merge(usuario);

            em.getTransaction().commit();

            return usuario;

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            return null;

        } finally {

            em.close();

        }

    }

    public List<UsuarioModel> buscar(
            String perfil,
            String status
    ) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            String jpql
                    = "SELECT u FROM UsuarioModel u WHERE 1=1";

            if (!perfil.equals("TODOS")) {

                jpql += " AND u.perfil = :perfil";
            }

            if (!status.equals("TODOS")) {

                jpql += " AND u.ativo = :ativo";
            }

            TypedQuery<UsuarioModel> query
                    = em.createQuery(
                            jpql,
                            UsuarioModel.class
                    );

            if (!perfil.equals("TODOS")) {

                query.setParameter(
                        "perfil",
                        PerfilUsuario.valueOf(perfil)
                );
            }

            if (!status.equals("TODOS")) {

                query.setParameter(
                        "ativo",
                        status.equals("ATIVO")
                );
            }

            return query.getResultList();

        } finally {

            em.close();

        }
    }

}

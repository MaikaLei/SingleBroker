package dao;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import model.AgendaModel;
import util.*;
public class AgendaDao {
    private Long usuarioId() {
        if (SessaoUsuario.getUsuarioLogado() == null) throw new SecurityException("Entre no sistema para acessar a agenda.");
        return SessaoUsuario.getUsuarioLogado().getId();
    }
    public List<AgendaModel> listar(LocalDate inicio, LocalDate fim) {
        Long usuario = usuarioId();
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("FROM AgendaModel a WHERE a.usuario.id = :usuario AND a.data BETWEEN :inicio AND :fim ORDER BY a.data, a.horario", AgendaModel.class)
                .setParameter("usuario", usuario).setParameter("inicio", inicio).setParameter("fim", fim).getResultList();
        }
    }
    public void salvar(AgendaModel tarefa) {
        Long usuario = usuarioId();
        try (EntityManager em = JPAUtil.getEntityManager()) {
            try {
                em.getTransaction().begin();
                if (tarefa.getId() != null) {
                    AgendaModel atual = em.find(AgendaModel.class, tarefa.getId());
                    if (atual == null || !atual.getUsuario().getId().equals(usuario)) throw new SecurityException("Tarefa não encontrada na sua agenda.");
                }
                tarefa.setUsuario(em.getReference(model.UsuarioModel.class, usuario));
                if (tarefa.getId() == null) em.persist(tarefa); else em.merge(tarefa);
                em.getTransaction().commit();
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
        }
    }
    public void excluir(Long id) {
        Long usuario = usuarioId();
        try (EntityManager em = JPAUtil.getEntityManager()) {
            try {
                em.getTransaction().begin();
                AgendaModel tarefa = em.find(AgendaModel.class, id);
                if (tarefa == null || !tarefa.getUsuario().getId().equals(usuario)) throw new SecurityException("Tarefa não encontrada na sua agenda.");
                em.remove(tarefa);
                em.getTransaction().commit();
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
        }
    }
}

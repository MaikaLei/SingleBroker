package dao;
import jakarta.persistence.EntityManager;
import model.MinhaPaginaModel;
import util.*;
public class MinhaPaginaDao {
    private Long usuarioId() {
        if (SessaoUsuario.getUsuarioLogado() == null) throw new SecurityException("Entre no sistema para acessar sua página.");
        return SessaoUsuario.getUsuarioLogado().getId();
    }
    public MinhaPaginaModel carregar() {
        Long id=usuarioId();
        try (EntityManager em=JPAUtil.getEntityManager()) {
            MinhaPaginaModel pagina=em.find(MinhaPaginaModel.class,id);
            if (pagina==null) { pagina=new MinhaPaginaModel(); pagina.setUsuarioId(id); pagina.setEmail(SessaoUsuario.getUsuarioLogado().getEmail()); }
            return pagina;
        }
    }
    public void salvar(MinhaPaginaModel pagina) {
        if (!usuarioId().equals(pagina.getUsuarioId())) throw new SecurityException("Acesso negado.");
        try (EntityManager em=JPAUtil.getEntityManager()) {
            try { em.getTransaction().begin(); em.merge(pagina); em.getTransaction().commit(); }
            catch (RuntimeException e) { if(em.getTransaction().isActive()) em.getTransaction().rollback(); throw e; }
        }
    }
}

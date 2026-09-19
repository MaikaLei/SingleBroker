package dao;
import jakarta.persistence.EntityManager;
import java.util.List;
import model.*;
import util.JPAUtil;
public class AnexoDao<T extends AnexoModel> {
    private final Class<T> tipo;
    public AnexoDao(Class<T> tipo) { this.tipo=tipo; }
    public List<T> listar(Long imovelId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("FROM " + tipo.getSimpleName() + " a WHERE a.imovel.id = :id ORDER BY a.id", tipo).setParameter("id", imovelId).getResultList();
        }
    }
    public void salvar(Long imovelId, List<T> novos, List<Long> removidos) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            try {
                em.getTransaction().begin();
                ImovelModel imovel = em.find(ImovelModel.class, imovelId);
                if (imovel == null) throw new IllegalArgumentException("Imóvel não encontrado.");
                for (Long id : removidos) {
                    T anexo = em.find(tipo, id);
                    if (anexo == null || !anexo.getImovel().getId().equals(imovelId)) throw new IllegalArgumentException("Anexo não pertence ao imóvel.");
                    em.remove(anexo);
                }
                for (T anexo : novos) {
                    anexo.setImovel(imovel);
                    em.persist(anexo);
                }
                em.getTransaction().commit();
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                for (T anexo : novos) anexo.setId(null);
                throw e;
            }
        }
    }
}

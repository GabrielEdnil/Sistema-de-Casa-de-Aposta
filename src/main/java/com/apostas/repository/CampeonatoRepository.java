package com.apostas.repository;

import com.apostas.model.CampeonatoModel;
import com.apostas.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CampeonatoRepository implements ICampeonatoRepository {

    @Override
    public CampeonatoModel salvar(CampeonatoModel campeonato) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(campeonato);
            em.getTransaction().commit();
            return campeonato;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<CampeonatoModel> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<CampeonatoModel> query =
                    em.createQuery("SELECT c FROM CampeonatoModel c ORDER BY c.nome", CampeonatoModel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

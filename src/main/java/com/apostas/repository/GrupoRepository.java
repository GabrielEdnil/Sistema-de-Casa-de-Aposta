package com.apostas.repository;

import com.apostas.model.GrupoModel;
import com.apostas.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class GrupoRepository implements IGrupoRepository {

    @Override
    public GrupoModel salvar(GrupoModel grupo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(grupo);
            em.getTransaction().commit();
            return grupo;
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
    public List<GrupoModel> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<GrupoModel> query =
                    em.createQuery("SELECT g FROM GrupoModel g ORDER BY g.nome", GrupoModel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long contar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(g) FROM GrupoModel g", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}

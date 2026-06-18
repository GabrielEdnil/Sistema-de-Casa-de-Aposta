package com.apostas.repository;

import com.apostas.model.ClubeModel;
import com.apostas.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ClubeRepository implements IClubeRepository {

    @Override
    public ClubeModel salvar(ClubeModel clube) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(clube);
            em.getTransaction().commit();
            return clube;
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
    public ClubeModel atualizar(ClubeModel clube) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            ClubeModel atualizado = em.merge(clube);
            em.getTransaction().commit();
            return atualizado;
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
    public List<ClubeModel> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ClubeModel> query =
                    em.createQuery("SELECT c FROM ClubeModel c ORDER BY c.nome", ClubeModel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

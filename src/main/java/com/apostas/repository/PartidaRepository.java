package com.apostas.repository;

import com.apostas.model.PartidaModel;
import com.apostas.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PartidaRepository implements IPartidaRepository {

    @Override
    public PartidaModel salvar(PartidaModel partida) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(partida);
            em.getTransaction().commit();
            return partida;
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
    public PartidaModel atualizar(PartidaModel partida) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            PartidaModel atualizada = em.merge(partida);
            em.getTransaction().commit();
            return atualizada;
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
    public List<PartidaModel> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<PartidaModel> query =
                    em.createQuery("SELECT p FROM PartidaModel p ORDER BY p.dataHora", PartidaModel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

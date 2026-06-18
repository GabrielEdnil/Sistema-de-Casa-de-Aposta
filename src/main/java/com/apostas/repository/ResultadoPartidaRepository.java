package com.apostas.repository;

import com.apostas.model.ResultadoPartidaModel;
import com.apostas.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class ResultadoPartidaRepository implements IResultadoPartidaRepository {

    @Override
    public ResultadoPartidaModel salvar(ResultadoPartidaModel resultado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(resultado);
            em.getTransaction().commit();
            return resultado;
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
    public ResultadoPartidaModel buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(ResultadoPartidaModel.class, id);
        } finally {
            em.close();
        }
    }
}

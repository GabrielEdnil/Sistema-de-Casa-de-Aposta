package com.apostas.repository;

import com.apostas.model.ApostaModel;
import com.apostas.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class ApostaRepository implements IApostaRepository {

    @Override
    public ApostaModel salvar(ApostaModel aposta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(aposta);
            em.getTransaction().commit();
            return aposta;
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
    public boolean existe(String participanteId, Long partidaId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long total = em.createQuery(
                            "SELECT COUNT(a) FROM ApostaModel a WHERE a.participante.id = :pid AND a.partida.id = :paid",
                            Long.class)
                    .setParameter("pid", participanteId)
                    .setParameter("paid", partidaId)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }
}

package com.apostas.repository;

import com.apostas.model.ParticipanteModel;
import com.apostas.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ParticipanteRepository implements IParticipanteRepository {

    @Override
    public ParticipanteModel salvar(ParticipanteModel participante) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(participante);
            em.getTransaction().commit();
            return participante;
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
    public ParticipanteModel atualizar(ParticipanteModel participante) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            ParticipanteModel atualizado = em.merge(participante);
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
    public ParticipanteModel buscarPorId(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(ParticipanteModel.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ParticipanteModel> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ParticipanteModel> query =
                    em.createQuery("SELECT p FROM ParticipanteModel p ORDER BY p.nome", ParticipanteModel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long contar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM ParticipanteModel p", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}

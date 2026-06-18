package com.apostas.repository;

import com.apostas.model.ParticipanteModel;

import java.util.List;

public interface IParticipanteRepository {

    ParticipanteModel salvar(ParticipanteModel participante);

    ParticipanteModel atualizar(ParticipanteModel participante);

    ParticipanteModel buscarPorId(String id);

    List<ParticipanteModel> listarTodos();

    long contar();
}

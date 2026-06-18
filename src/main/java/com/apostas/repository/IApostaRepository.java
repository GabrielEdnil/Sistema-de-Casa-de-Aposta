package com.apostas.repository;

import com.apostas.model.ApostaModel;

public interface IApostaRepository {

    ApostaModel salvar(ApostaModel aposta);

    boolean existe(String participanteId, Long partidaId);
}

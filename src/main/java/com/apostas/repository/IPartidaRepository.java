package com.apostas.repository;

import com.apostas.model.PartidaModel;

import java.util.List;

public interface IPartidaRepository {

    PartidaModel salvar(PartidaModel partida);

    PartidaModel atualizar(PartidaModel partida);

    List<PartidaModel> listarTodos();
}

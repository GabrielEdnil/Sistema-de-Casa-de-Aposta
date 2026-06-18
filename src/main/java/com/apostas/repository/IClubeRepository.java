package com.apostas.repository;

import com.apostas.model.ClubeModel;

import java.util.List;

public interface IClubeRepository {

    ClubeModel salvar(ClubeModel clube);

    ClubeModel atualizar(ClubeModel clube);

    List<ClubeModel> listarTodos();
}

package com.apostas.repository;

import com.apostas.model.GrupoModel;

import java.util.List;

public interface IGrupoRepository {

    GrupoModel salvar(GrupoModel grupo);

    List<GrupoModel> listarTodos();

    long contar();
}

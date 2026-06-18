package com.apostas.repository;

import com.apostas.model.ResultadoPartidaModel;

public interface IResultadoPartidaRepository {

    ResultadoPartidaModel salvar(ResultadoPartidaModel resultado);

    ResultadoPartidaModel buscarPorId(Long id);
}

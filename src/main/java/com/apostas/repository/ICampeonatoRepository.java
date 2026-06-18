package com.apostas.repository;

import com.apostas.model.CampeonatoModel;

import java.util.List;

public interface ICampeonatoRepository {

    CampeonatoModel salvar(CampeonatoModel campeonato);

    List<CampeonatoModel> listarTodos();
}

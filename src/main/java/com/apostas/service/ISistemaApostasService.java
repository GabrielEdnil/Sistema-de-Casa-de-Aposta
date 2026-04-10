package com.apostas.service;

import com.apostas.model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ISistemaApostasService {
    // ADMIN
    boolean autenticarAdmin(String senha);

    // CLUBES
    void cadastrarClube(String nome, String sigla);
    List<ClubeModel> getClubes();

    // CAMPEONATOS
    CampeonatoModel cadastrarCampeonato(String nome);
    void adicionarClubeAoCampeonato(CampeonatoModel campeonato, ClubeModel clube);
    List<CampeonatoModel> getCampeonatos();

    // PARTIDAS
    PartidaModel cadastrarPartida(CampeonatoModel campeonato, ClubeModel mandante, ClubeModel visitante, LocalDateTime dataHora);
    List<PartidaModel> getPartidas();

    // GRUPOS E PARTICIPANTES
    GrupoModel criarGrupo(String nome);
    ParticipanteModel cadastrarParticipante(String nome, String id);
    void adicionarParticipanteAoGrupo(GrupoModel grupo, ParticipanteModel participante);
    ParticipanteModel buscarParticipante(String id);
    List<GrupoModel> getGrupos();
    List<ParticipanteModel> getParticipantes();

    // APOSTAS
    void adicionarAposta(ParticipanteModel participante, PartidaModel partida, int golsMandante, int golsVisitante);

    // RESULTADOS
    void registrarResultadoPartida(PartidaModel partida, int golsMandante, int golsVisitante);
}

package com.apostas.service;

import com.apostas.model.*;
import com.apostas.ui.MainFrame;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * IMPLEMENTACAO DO SERVICO SISTEMA APOSTAS EM MEMORIA UTILIZADO APENAS PARA O TRABALHO
 * EM UM CENARIO REAL FARIAMOS UM SERVICO PARA CADA MODEL E UMA ORM PARA O MAPEAMENTO AO BANCO DE DADOS
 */
public class SistemaApostasService implements ISistemaApostasService {

    private final int MAX_GRUPOS = 5;
    private final int MAX_PARTICIPANTES = 5;

    private static SistemaApostasService sistemaApostasService;

    private final AdministradorModel administrador;
    private final List<ClubeModel> clubes;
    private final List<CampeonatoModel> campeonatos;
    private final List<PartidaModel> partidas;
    private final List<GrupoModel> grupos;
    private final List<ParticipanteModel> participantes;

    private SistemaApostasService() {
        administrador = new AdministradorModel("Administrador", "admin", MainFrame.SENHA_ADMIN);
        clubes = new ArrayList<>();
        campeonatos = new ArrayList<>();
        partidas = new ArrayList<>();
        grupos = new ArrayList<>();
        participantes = new ArrayList<>();
    }

    public static SistemaApostasService getInstancia() {
        if (sistemaApostasService == null) {
            sistemaApostasService = new SistemaApostasService();
        }
        return sistemaApostasService;
    }

    // ADMIN
    @Override
    public boolean autenticarAdmin(String senha) {
        return administrador.isSenhaCorreta(senha);
    }

    // CLUBES
    @Override
    public void cadastrarClube(String nome, String sigla) {
        clubes.add(new ClubeModel(nome, sigla));
    }

    @Override
    public List<ClubeModel> getClubes() {
        return clubes;
    }

    // CAMPEONATOS
    @Override
    public CampeonatoModel cadastrarCampeonato(String nome) {
        CampeonatoModel c = new CampeonatoModel(nome);
        campeonatos.add(c);
        return c;
    }

    @Override
    public void adicionarClubeAoCampeonato(CampeonatoModel campeonato, ClubeModel clube) {
        campeonato.adicionarClube(clube);
    }

    @Override
    public List<CampeonatoModel> getCampeonatos() {
        return campeonatos;
    }

    // PARTIDAS
    @Override
    public PartidaModel cadastrarPartida(CampeonatoModel campeonato, ClubeModel mandante, ClubeModel visitante, LocalDateTime dataHora) {
        if (mandante.equals(visitante)) {
            throw new IllegalArgumentException("Mandante e visitante não podem ser o mesmo clube.");
        }
        PartidaModel p = new PartidaModel(campeonato, mandante, visitante, dataHora);
        partidas.add(p);
        return p;
    }

    @Override
    public List<PartidaModel> getPartidas() {
        return partidas;
    }

    // GRUPOS E PARTICIPANTES
    @Override
    public GrupoModel criarGrupo(String nome) {
        if (grupos.size() >= MAX_GRUPOS) {
            throw new IllegalStateException("Limite de " + MAX_GRUPOS + " grupos atingido.");
        }
        GrupoModel g = new GrupoModel(nome);
        grupos.add(g);
        return g;
    }

    @Override
    public ParticipanteModel cadastrarParticipante(String nome, String id) {
        if (participantes.size() >= MAX_PARTICIPANTES) {
            throw new IllegalStateException("Limite de " + MAX_PARTICIPANTES + " participantes atingido.");
        }
        boolean idExistente = participantes.stream().anyMatch(p -> p.getId().equals(id));
        if (idExistente) {
            throw new IllegalArgumentException("ID '" + id + "' já está em uso.");
        }
        ParticipanteModel p = new ParticipanteModel(id, nome);
        participantes.add(p);
        return p;
    }

    @Override
    public void adicionarParticipanteAoGrupo(GrupoModel grupo, ParticipanteModel participante) {
        for (GrupoModel g : grupos) {
            if (g.getParticipantes().contains(participante)) {
                throw new IllegalStateException("Participante já pertence ao grupo '" + g.getNome() + "'.");
            }
        }
        grupo.adicionarParticipante(participante);
    }

    @Override
    public ParticipanteModel buscarParticipante(String id) {
        return participantes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<GrupoModel> getGrupos() {
        return grupos;
    }

    @Override
    public List<ParticipanteModel> getParticipantes() {
        return participantes;
    }

    // APOSTAS
    @Override
    public void adicionarAposta(ParticipanteModel participante, PartidaModel partida, int golsMandante, int golsVisitante) {
        if (!partida.isApostaPermitida()) {
            throw new IllegalStateException("Apostas encerradas (menos de 20 minutos para a partida).");
        }
        if (participante.jaApostouEm(partida)) {
            throw new IllegalStateException("Participante já apostou nessa partida.");
        }
        ApostaModel aposta = new ApostaModel(participante, partida, golsMandante, golsVisitante);
        participante.adicionarAposta(aposta);
    }

    // RESULTADOS
    @Override
    public void registrarResultadoPartida(PartidaModel partida, int golsMandante, int golsVisitante) {
        administrador.registrarResultado(partida, golsMandante, golsVisitante);
    }
}

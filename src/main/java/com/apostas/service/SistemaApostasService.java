package com.apostas.service;

import com.apostas.model.*;
import com.apostas.repository.*;
import com.apostas.ui.MainFrame;

import java.time.LocalDateTime;
import java.util.List;

public class SistemaApostasService implements ISistemaApostasService {

    private final int MAX_GRUPOS = 5;
    private final int MAX_PARTICIPANTES = 5;

    private static SistemaApostasService sistemaApostasService;

    private final IClubeRepository clubeRepository;
    private final ICampeonatoRepository campeonatoRepository;
    private final IPartidaRepository partidaRepository;
    private final IGrupoRepository grupoRepository;
    private final IParticipanteRepository participanteRepository;
    private final IApostaRepository apostaRepository;

    private final AdministradorModel administrador;

    private SistemaApostasService() {
        clubeRepository = new ClubeRepository();
        campeonatoRepository = new CampeonatoRepository();
        partidaRepository = new PartidaRepository();
        grupoRepository = new GrupoRepository();
        participanteRepository = new ParticipanteRepository();
        apostaRepository = new ApostaRepository();

        administrador = new AdministradorModel("Administrador", "admin", MainFrame.SENHA_ADMIN);
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
        ClubeModel clube = new ClubeModel(nome, sigla);
        clubeRepository.salvar(clube);
    }

    @Override
    public List<ClubeModel> getClubes() {
        return clubeRepository.listarTodos();
    }

    // CAMPEONATOS
    @Override
    public CampeonatoModel cadastrarCampeonato(String nome) {
        CampeonatoModel c = new CampeonatoModel(nome);
        return campeonatoRepository.salvar(c);
    }

    @Override
    public void adicionarClubeAoCampeonato(CampeonatoModel campeonato, ClubeModel clube) {
        campeonato.adicionarClube(clube);
        clube.setCampeonato(campeonato);
        clubeRepository.atualizar(clube);
    }

    @Override
    public List<CampeonatoModel> getCampeonatos() {
        return campeonatoRepository.listarTodos();
    }

    // PARTIDAS
    @Override
    public PartidaModel cadastrarPartida(CampeonatoModel campeonato, ClubeModel mandante, ClubeModel visitante, LocalDateTime dataHora) {
        if (mandante.equals(visitante)) {
            throw new IllegalArgumentException("Mandante e visitante não podem ser o mesmo clube.");
        }
        PartidaModel p = new PartidaModel(campeonato, mandante, visitante, dataHora);
        return partidaRepository.salvar(p);
    }

    @Override
    public List<PartidaModel> getPartidas() {
        return partidaRepository.listarTodos();
    }

    // GRUPOS E PARTICIPANTES
    @Override
    public GrupoModel criarGrupo(String nome) {
        if (grupoRepository.contar() >= MAX_GRUPOS) {
            throw new IllegalStateException("Limite de " + MAX_GRUPOS + " grupos atingido.");
        }
        GrupoModel g = new GrupoModel(nome);
        return grupoRepository.salvar(g);
    }

    @Override
    public ParticipanteModel cadastrarParticipante(String nome, String id) {
        if (participanteRepository.contar() >= MAX_PARTICIPANTES) {
            throw new IllegalStateException("Limite de " + MAX_PARTICIPANTES + " participantes atingido.");
        }
        if (participanteRepository.buscarPorId(id) != null) {
            throw new IllegalArgumentException("ID '" + id + "' já está em uso.");
        }
        ParticipanteModel p = new ParticipanteModel(id, nome);
        return participanteRepository.salvar(p);
    }

    @Override
    public void adicionarParticipanteAoGrupo(GrupoModel grupo, ParticipanteModel participante) {
        ParticipanteModel atual = participanteRepository.buscarPorId(participante.getId());
        if (atual != null && atual.getGrupo() != null) {
            throw new IllegalStateException("Participante já pertence ao grupo '" + atual.getGrupo().getNome() + "'.");
        }
        participante.setGrupo(grupo);
        participanteRepository.atualizar(participante);
    }

    @Override
    public ParticipanteModel buscarParticipante(String id) {
        return participanteRepository.buscarPorId(id);
    }

    @Override
    public List<GrupoModel> getGrupos() {
        return grupoRepository.listarTodos();
    }

    @Override
    public List<ParticipanteModel> getParticipantes() {
        return participanteRepository.listarTodos();
    }

    // APOSTAS
    @Override
    public void adicionarAposta(ParticipanteModel participante, PartidaModel partida, int golsMandante, int golsVisitante) {
        if (!partida.isApostaPermitida()) {
            throw new IllegalStateException("Apostas encerradas (menos de 20 minutos para a partida).");
        }
        if (apostaRepository.existe(participante.getId(), partida.getId())) {
            throw new IllegalStateException("Participante já apostou nessa partida.");
        }
        ApostaModel aposta = new ApostaModel(participante, partida, golsMandante, golsVisitante);
        apostaRepository.salvar(aposta);
    }

    // RESULTADOS
    @Override
    public void registrarResultadoPartida(PartidaModel partida, int golsMandante, int golsVisitante) {
        administrador.registrarResultado(partida, golsMandante, golsVisitante);
        partidaRepository.atualizar(partida);
    }
}

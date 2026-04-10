package com.apostas.model;

import java.time.LocalDateTime;

public class PartidaModel {

    private final int MINUTOS_LIMITE_APOSTA = 20;

    private CampeonatoModel campeonato;
    private ClubeModel mandante;
    private ClubeModel visitante;
    private LocalDateTime dataHora;
    private ResultadoPartidaModel resultado;

    public PartidaModel(CampeonatoModel campeonato, ClubeModel mandante, ClubeModel visitante, LocalDateTime dataHora) {
        this.campeonato = campeonato;
        this.mandante = mandante;
        this.visitante = visitante;
        this.dataHora = dataHora;
        this.resultado = null;
    }

    public boolean isApostaPermitida() {
        return LocalDateTime.now().isBefore(dataHora.minusMinutes(MINUTOS_LIMITE_APOSTA));
    }

    public void registrarResultado(int golsMandante, int golsVisitante) {
        this.resultado = new ResultadoPartidaModel(golsMandante, golsVisitante);
    }

    public boolean temResultado() {
        return resultado != null;
    }

    public CampeonatoModel getCampeonato() {
        return campeonato;
    }

    public ClubeModel getMandante() {
        return mandante;
    }

    public ClubeModel getVisitante() {
        return visitante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public ResultadoPartidaModel getResultado() {
        return resultado;
    }

    @Override
    public String toString() {
        return mandante.getSigla() + " x " + visitante.getSigla();
    }
}

package com.apostas.model;

public class ResultadoPartidaModel {

    private int golsMandante;
    private int golsVisitante;

    public ResultadoPartidaModel(int golsMandante, int golsVisitante) {
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
    }

    public int getGolsMandante() {
        return golsMandante;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public String getVencedor() {
        if (golsMandante > golsVisitante) return "MANDANTE";
        if (golsVisitante > golsMandante) return "VISITANTE";
        return "EMPATE";
    }

    @Override
    public String toString() {
        return golsMandante + " x " + golsVisitante;
    }
}

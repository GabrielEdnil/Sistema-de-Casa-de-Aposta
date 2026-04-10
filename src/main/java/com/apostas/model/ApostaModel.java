package com.apostas.model;

public class ApostaModel {

    private ParticipanteModel participante;
    private PartidaModel partida;
    private int golsMandantePrevisto;
    private int golsVisitantePrevisto;

    public ApostaModel(ParticipanteModel participante, PartidaModel partida, int golsMandantePrevisto, int golsVisitantePrevisto) {
        this.participante = participante;
        this.partida = partida;
        this.golsMandantePrevisto = golsMandantePrevisto;
        this.golsVisitantePrevisto = golsVisitantePrevisto;
    }

    public int calcularPontos() {
        if (!partida.temResultado()) return 0;

        ResultadoPartidaModel real = partida.getResultado();
        String vencedorReal = real.getVencedor();

        String vencedorPrevisto;
        if (golsMandantePrevisto > golsVisitantePrevisto) vencedorPrevisto = "MANDANTE";
        else if (golsVisitantePrevisto > golsMandantePrevisto) vencedorPrevisto = "VISITANTE";
        else vencedorPrevisto = "EMPATE";

        boolean acertouResultado = vencedorReal.equals(vencedorPrevisto);
        boolean acertouPlacar = real.getGolsMandante() == golsMandantePrevisto
                && real.getGolsVisitante() == golsVisitantePrevisto;

        if (acertouPlacar) return 10;
        if (acertouResultado) return 5;
        return 0;
    }

    public ParticipanteModel getParticipante() {
        return participante;
    }

    public PartidaModel getPartida() {
        return partida;
    }

    public int getGolsMandantePrevisto() {
        return golsMandantePrevisto;
    }

    public int getGolsVisitantePrevisto() {
        return golsVisitantePrevisto;
    }
}

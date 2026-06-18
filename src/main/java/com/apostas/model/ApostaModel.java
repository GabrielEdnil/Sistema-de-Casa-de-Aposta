package com.apostas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "aposta")
public class ApostaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ParticipanteModel participante;

    @ManyToOne
    private PartidaModel partida;

    private int golsMandantePrevisto;
    private int golsVisitantePrevisto;

    protected ApostaModel() {
    }

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

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApostaModel that = (ApostaModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}

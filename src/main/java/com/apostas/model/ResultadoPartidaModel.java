package com.apostas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "resultado_partida")
public class ResultadoPartidaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int golsMandante;
    private int golsVisitante;

    protected ResultadoPartidaModel() {
    }

    public ResultadoPartidaModel(int golsMandante, int golsVisitante) {
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultadoPartidaModel that = (ResultadoPartidaModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}

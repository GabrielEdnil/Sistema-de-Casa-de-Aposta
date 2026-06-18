package com.apostas.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "partida")
public class PartidaModel {

    private static final int MINUTOS_LIMITE_APOSTA = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CampeonatoModel campeonato;

    @ManyToOne
    @JoinColumn(name = "mandante_id")
    private ClubeModel mandante;

    @ManyToOne
    @JoinColumn(name = "visitante_id")
    private ClubeModel visitante;

    private LocalDateTime dataHora;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resultado_id")
    private ResultadoPartidaModel resultado;

    protected PartidaModel() {
    }

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

    public Long getId() {
        return id;
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

    public void setResultado(ResultadoPartidaModel resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return mandante.getSigla() + " x " + visitante.getSigla();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartidaModel that = (PartidaModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}

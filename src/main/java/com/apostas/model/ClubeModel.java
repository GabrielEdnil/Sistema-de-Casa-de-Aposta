package com.apostas.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "clube")
public class ClubeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 10)
    private String sigla;

    @ManyToOne
    private CampeonatoModel campeonato;

    protected ClubeModel() {
    }

    public ClubeModel(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public CampeonatoModel getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(CampeonatoModel campeonato) {
        this.campeonato = campeonato;
    }

    @Override
    public String toString() {
        return nome + " (" + sigla + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClubeModel that = (ClubeModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}

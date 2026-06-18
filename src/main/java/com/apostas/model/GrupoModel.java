package com.apostas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "grupo")
public class GrupoModel {

    public static int MAX_PARTICIPANTES = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "grupo", fetch = FetchType.EAGER)
    private List<ParticipanteModel> participantes = new ArrayList<>();

    protected GrupoModel() {
    }

    public GrupoModel(String nome) {
        this.nome = nome;
    }

    public void adicionarParticipante(ParticipanteModel participante) {
        if (participantes.size() >= MAX_PARTICIPANTES) {
            throw new IllegalStateException("Grupo já possui o máximo de " + MAX_PARTICIPANTES + " participantes.");
        }
        participantes.add(participante);
    }

    public List<ParticipanteModel> getRanking() {
        List<ParticipanteModel> ranking = new ArrayList<>(participantes);
        ranking.sort(Comparator.comparingInt(ParticipanteModel::getPontuacaoTotal).reversed());
        return ranking;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<ParticipanteModel> getParticipantes() {
        return participantes;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrupoModel that = (GrupoModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}

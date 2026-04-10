package com.apostas.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GrupoModel {

    public static int MAX_PARTICIPANTES = 5;

    private String nome;
    private List<ParticipanteModel> participantes;

    public GrupoModel(String nome) {
        this.nome = nome;
        this.participantes = new ArrayList<ParticipanteModel>();
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
}

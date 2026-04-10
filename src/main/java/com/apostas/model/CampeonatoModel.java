package com.apostas.model;

import java.util.ArrayList;
import java.util.List;

public class CampeonatoModel {

    public static int MAX_CLUBES = 8;

    private String nome;
    private List<ClubeModel> clubes;

    public CampeonatoModel(String nome) {
        this.nome = nome;
        this.clubes = new ArrayList<>();
    }

    public void adicionarClube(ClubeModel clube) {
        if (clubes.size() >= MAX_CLUBES) {
            throw new IllegalStateException("Campeonato já possui o máximo de " + MAX_CLUBES + " clubes.");
        }
        clubes.add(clube);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ClubeModel> getClubes() {
        return clubes;
    }

    @Override
    public String toString() {
        return nome;
    }
}

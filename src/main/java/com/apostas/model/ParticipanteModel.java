package com.apostas.model;

import com.apostas.interfaces.IPontuavel;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteModel extends PessoaModel implements IPontuavel {

    private List<ApostaModel> apostas;

    public ParticipanteModel(String id, String nome) {
        super(id, nome);
        this.apostas = new ArrayList<>();
    }

    public void adicionarAposta(ApostaModel aposta) {
        apostas.add(aposta);
    }

    public boolean jaApostouEm(PartidaModel partida) {
        return apostas.stream()
                .anyMatch(a -> a.getPartida().equals(partida));
    }

    public List<ApostaModel> getApostas() {
        return apostas;
    }

    @Override
    public int calcularPontuacao() {
        return apostas.stream()
                .mapToInt(ApostaModel::calcularPontos)
                .sum();
    }

    @Override
    public int getPontuacaoTotal() {
        return calcularPontuacao();
    }
}

package com.apostas.model;

public class AdministradorModel extends PessoaModel {

    private String senha;

    public AdministradorModel(String id, String nome, String senha) {
        super(id, nome);
        this.senha = senha;
    }

    public boolean isSenhaCorreta(String senha) {
        return this.senha.equals(senha);
    }

    public void registrarResultado(PartidaModel partida, int golsMandante, int golsVisitante) {
        partida.registrarResultado(golsMandante, golsVisitante);
    }
}

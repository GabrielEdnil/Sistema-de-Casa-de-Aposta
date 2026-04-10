package com.apostas.model;

public abstract class PessoaModel {

    private String id;
    private String nome;

    public PessoaModel(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

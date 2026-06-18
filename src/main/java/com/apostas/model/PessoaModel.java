package com.apostas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PessoaModel {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false)
    private String nome;

    protected PessoaModel() {
    }

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

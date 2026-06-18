package com.apostas.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "campeonato")
public class CampeonatoModel {

    public static int MAX_CLUBES = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @OneToMany(mappedBy = "campeonato", fetch = FetchType.EAGER)
    private List<ClubeModel> clubes = new ArrayList<>();

    public CampeonatoModel() {
    }

    public CampeonatoModel(String nome) {
        this.nome = nome;
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

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CampeonatoModel that = (CampeonatoModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}

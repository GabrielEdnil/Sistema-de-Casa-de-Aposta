package com.apostas.model;

import com.apostas.interfaces.IPontuavel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "participante")
public class ParticipanteModel extends PessoaModel implements IPontuavel {

    @OneToMany(mappedBy = "participante", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ApostaModel> apostas = new ArrayList<>();

    @ManyToOne
    private GrupoModel grupo;

    protected ParticipanteModel() {
    }

    public ParticipanteModel(String id, String nome) {
        super(id, nome);
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

    public GrupoModel getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoModel grupo) {
        this.grupo = grupo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipanteModel that = (ParticipanteModel) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}

package com.apostas.ui.panel;

import com.apostas.model.ApostaModel;
import com.apostas.model.GrupoModel;
import com.apostas.model.ParticipanteModel;
import com.apostas.model.PartidaModel;
import com.apostas.service.ISistemaApostasService;
import com.apostas.ui.Logger;
import com.apostas.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantePanel extends JPanel {

    private static final Logger logger = Logger.getLogger(ParticipantePanel.class);

    private final MainFrame mainFrame;
    private final ISistemaApostasService sistemaApostasService;

    private ParticipanteModel participanteLogado = null;

    // CADASTRO E LOGIN
    private JTextField fieldNome;
    private JTextField fieldId;
    private JLabel labelLogado;

    // GRUPOS
    private JTextField fieldNomeGrupo;
    private JComboBox<GrupoModel> comboGrupos;
    private JLabel labelMeusGrupos;

    // APOSTAS
    private JComboBox<PartidaModel> comboPartidas;
    private JLabel labelPartidasInfo;
    private JTextField fieldGolsM;
    private JTextField fieldGolsV;
    private DefaultTableModel modelMinhasApostas;

    // RANKING
    private JComboBox<GrupoModel> comboGruposRanking;
    private DefaultTableModel modelRanking;

    public ParticipantePanel(MainFrame mainFrame, ISistemaApostasService sistemaApostasService) {
        this.mainFrame = mainFrame;
        this.sistemaApostasService = sistemaApostasService;
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Minha conta", buildAbaConta());
        abas.addTab("Grupos", buildAbaGrupos());
        abas.addTab("Apostas", buildAbaApostas());
        abas.addTab("Ranking", buildAbaRanking());

        add(buildTopBar(), BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVoltar = new JButton("Voltar ao menu");
        btnVoltar.addActionListener(e -> {
            participanteLogado = null;
            mainFrame.showCard(MainFrame.CARD_LOGIN);
        });
        bar.add(btnVoltar);
        labelLogado = new JLabel("Nenhum participante selecionado");
        bar.add(labelLogado);
        return bar;
    }

    private JPanel buildAbaConta() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        fieldNome = new JTextField(18);
        fieldId = new JTextField(10);

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEntrar = new JButton("Entrar (já cadastrado)");

        btnCadastrar.addActionListener(e -> {
            String nome = fieldNome.getText().trim();
            String id = fieldId.getText().trim();
            if (nome.isEmpty() || id.isEmpty()) {
                logger.erro("Preencha nome e ID.");
                return;
            }
            try {
                ParticipanteModel participante = sistemaApostasService.cadastrarParticipante(nome, id);
                participanteLogado = participante;
                atualizarLabelLogado();
                logger.info("Participante '" + nome + "' cadastrado com sucesso!");
                fieldNome.setText("");
                fieldId.setText("");
            } catch (IllegalStateException | IllegalArgumentException ex) {
                logger.erro(ex.getMessage());
            }
        });

        btnEntrar.addActionListener(e -> {
            String id = fieldId.getText().trim();
            if (id.isEmpty()) {
                logger.erro("Informe o ID.");
                return;
            }
            ParticipanteModel participante = sistemaApostasService.buscarParticipante(id);
            if (participante == null) {
                logger.erro("Participante com ID '" + id + "' não encontrado.");
                return;
            }
            participanteLogado = participante;
            atualizarLabelLogado();
            logger.info("Bem-vindo, " + participante.getNome() + "!");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(fieldNome, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("ID (login):"), gbc);
        gbc.gridx = 1;
        panel.add(fieldId, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(btnCadastrar, gbc);
        gbc.gridx = 1;
        panel.add(btnEntrar, gbc);

        return panel;
    }

    private JPanel buildAbaGrupos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fieldNomeGrupo = new JTextField(15);
        JButton btnCriar = new JButton("Criar Grupo");
        btnCriar.addActionListener(e -> {
            String nome = fieldNomeGrupo.getText().trim();
            if (nome.isEmpty()) {
                logger.erro("Informe o nome do grupo.");
                return;
            }
            try {
                sistemaApostasService.criarGrupo(nome);
                fieldNomeGrupo.setText("");
                refresh();
                logger.info("Grupo '" + nome + "' criado.");
            } catch (IllegalStateException ex) {
                logger.erro(ex.getMessage());
            }
        });

        comboGrupos = new JComboBox<>();
        JButton btnEntrar = new JButton("Entrar no Grupo");
        btnEntrar.addActionListener(e -> {
            if (participanteLogado == null) {
                logger.erro("Faça login primeiro (aba 'Minha conta').");
                return;
            }
            GrupoModel grupo = (GrupoModel) comboGrupos.getSelectedItem();
            if (grupo == null) {
                logger.erro("Nenhum grupo disponível.");
                return;
            }
            try {
                sistemaApostasService.adicionarParticipanteAoGrupo(grupo, participanteLogado);
                logger.info("Você entrou no grupo '" + grupo.getNome() + "'.");
                atualizarMeusGrupos();
            } catch (IllegalStateException ex) {
                logger.erro(ex.getMessage());
            }
        });

        JPanel formCriar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formCriar.add(new JLabel("Nome do grupo:"));
        formCriar.add(fieldNomeGrupo);
        formCriar.add(btnCriar);

        JPanel formEntrar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formEntrar.add(new JLabel("Grupo:"));
        formEntrar.add(comboGrupos);
        formEntrar.add(btnEntrar);

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(formCriar);
        top.add(formEntrar);

        labelMeusGrupos = new JLabel("Meus grupos: -");
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(labelMeusGrupos);

        panel.add(top, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAbaApostas() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        comboPartidas = new JComboBox<>();
        labelPartidasInfo = new JLabel(" ");
        fieldGolsM = new JTextField("0", 3);
        fieldGolsV = new JTextField("0", 3);

        JButton btnApostar = new JButton("Registrar Aposta");
        btnApostar.addActionListener(e -> {
            if (participanteLogado == null) {
                logger.erro("Faça login primeiro (aba 'Minha conta').");
                return;
            }
            PartidaModel partida = (PartidaModel) comboPartidas.getSelectedItem();
            if (partida == null) {
                logger.erro("Nenhuma partida disponível.");
                return;
            }
            try {
                int gM = Integer.parseInt(fieldGolsM.getText().trim());
                int gV = Integer.parseInt(fieldGolsV.getText().trim());
                if (gM < 0 || gV < 0) {
                    logger.erro("Gols não podem ser negativos.");
                    return;
                }
                sistemaApostasService.adicionarAposta(participanteLogado, partida, gM, gV);
                refresh();
                logger.info("Aposta registrada!");
            } catch (NumberFormatException ex) {
                logger.erro("Informe valores numéricos para os gols.");
            } catch (IllegalStateException ex) {
                logger.erro(ex.getMessage());
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Partida:"));
        form.add(comboPartidas);
        form.add(labelPartidasInfo);
        form.add(new JLabel("Gols mandante:"));
        form.add(fieldGolsM);
        form.add(new JLabel("Gols visitante:"));
        form.add(fieldGolsV);
        form.add(btnApostar);

        modelMinhasApostas = new DefaultTableModel(
                new String[]{"Partida", "Placar apostado", "Resultado real", "Pontos"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabela = new JTable(modelMinhasApostas);

        JButton btnAtualizar = new JButton("Atualizar minhas apostas");
        btnAtualizar.addActionListener(e -> refreshApostas());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnAtualizar);

        panel.add(form, BorderLayout.NORTH);
        panel.add(botoes, BorderLayout.SOUTH);
        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAbaRanking() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        comboGruposRanking = new JComboBox<>();
        JButton btnVer = new JButton("Ver Ranking");

        modelRanking = new DefaultTableModel(
                new String[]{"Posição", "Participante", "Pontos"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabela = new JTable(modelRanking);

        btnVer.addActionListener(e -> {
            GrupoModel grupo = (GrupoModel) comboGruposRanking.getSelectedItem();
            if (grupo == null) {
                logger.erro("Selecione um grupo.");
                return;
            }
            modelRanking.setRowCount(0);
            List<ParticipanteModel> ranking = grupo.getRanking();
            for (int i = 0; i < ranking.size(); i++) {
                ParticipanteModel participante = ranking.get(i);
                modelRanking.addRow(new Object[]{i + 1, participante.getNome(), participante.getPontuacaoTotal()});
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Grupo:"));
        form.add(comboGruposRanking);
        form.add(btnVer);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return panel;
    }

    public void refresh() {
        comboPartidas.removeAllItems();
        sistemaApostasService.getPartidas().stream()
                .filter(p -> !p.temResultado() && p.isApostaPermitida())
                .forEach(comboPartidas::addItem);
        labelPartidasInfo.setText(comboPartidas.getItemCount() == 0
                ? "(apostas encerram 20 min antes da partida)" : " ");

        comboGrupos.removeAllItems();
        sistemaApostasService.getGrupos().forEach(comboGrupos::addItem);

        comboGruposRanking.removeAllItems();
        sistemaApostasService.getGrupos().forEach(comboGruposRanking::addItem);

        refreshApostas();
        atualizarLabelLogado();
        atualizarMeusGrupos();
    }

    private void refreshApostas() {
        modelMinhasApostas.setRowCount(0);
        if (participanteLogado == null) return;
        for (ApostaModel aposta : participanteLogado.getApostas()) {
            PartidaModel partida = aposta.getPartida();
            String placarReal = partida.temResultado() ? partida.getResultado().toString() : "-";
            modelMinhasApostas.addRow(new Object[]{
                    partida.getMandante().getSigla() + " x " + partida.getVisitante().getSigla(),
                    aposta.getGolsMandantePrevisto() + " x " + aposta.getGolsVisitantePrevisto(),
                    placarReal,
                    aposta.calcularPontos()
            });
        }
    }

    private void atualizarLabelLogado() {
        if (participanteLogado != null) {
            labelLogado.setText("Logado como: " + participanteLogado.getNome()
                    + " (" + participanteLogado.getId() + ")");
        } else {
            labelLogado.setText("Nenhum participante selecionado");
        }
    }

    private void atualizarMeusGrupos() {
        if (participanteLogado == null) {
            labelMeusGrupos.setText("Meus grupos: -");
            return;
        }
        List<String> meusGrupos = new ArrayList<>();
        for (GrupoModel grupo : sistemaApostasService.getGrupos()) {
            if (grupo.getParticipantes().contains(participanteLogado)) {
                meusGrupos.add(grupo.getNome());
            }
        }
        if (meusGrupos.isEmpty()) {
            labelMeusGrupos.setText("Meus grupos: Nenhum");
        } else {
            labelMeusGrupos.setText("Meus grupos: " + String.join(", ", meusGrupos));
        }
    }
}

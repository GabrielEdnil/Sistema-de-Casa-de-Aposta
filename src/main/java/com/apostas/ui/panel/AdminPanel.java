package com.apostas.ui.panel;

import com.apostas.model.*;
import com.apostas.service.ISistemaApostasService;
import com.apostas.ui.Logger;
import com.apostas.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AdminPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(AdminPanel.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final MainFrame mainFrame;
    private final ISistemaApostasService sistemaApostasService;

    // CLUBES
    private DefaultListModel<ClubeModel> modelClubes;
    private JList<ClubeModel> listaClubes;

    // CAMPEONATOS
    private DefaultListModel<CampeonatoModel> modelCampeonatos;
    private DefaultListModel<ClubeModel> modelClubesDoCampeonato;
    private JComboBox<CampeonatoModel> comboCampeonatos;
    private JComboBox<ClubeModel> comboClubesCamp;

    // PARTIDAS
    private DefaultTableModel modelPartidas;
    private JComboBox<CampeonatoModel> comboCampeonatoPartida;
    private JComboBox<ClubeModel> comboMandante;
    private JComboBox<ClubeModel> comboVisitante;
    private JTextField fieldDataHora;

    // RESULTADOS
    private JComboBox<PartidaModel> comboPartidasResultado;
    private JTextField fieldGolsMandante;
    private JTextField fieldGolsVisitante;

    // RANKING
    private JComboBox<GrupoModel> comboGruposRanking;
    private DefaultTableModel modelRanking;

    public AdminPanel(MainFrame mainFrame, ISistemaApostasService sistemaApostasService) {
        this.mainFrame = mainFrame;
        this.sistemaApostasService = sistemaApostasService;
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Clubes", buildAbaClube());
        abas.addTab("Campeonatos", buildAbaCampeonato());
        abas.addTab("Partidas", buildAbaPartidas());
        abas.addTab("Resultados", buildAbaResultados());
        abas.addTab("Ranking", buildAbaRanking());

        add(buildTopBar(), BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVoltar = new JButton("Voltar ao menu");
        btnVoltar.addActionListener(e -> mainFrame.showCard(MainFrame.CARD_LOGIN));
        bar.add(btnVoltar);
        JLabel label = new JLabel("Painel do Administrador");
        bar.add(label);
        return bar;
    }

    private JPanel buildAbaClube() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário
        JTextField nomeField = new JTextField(15);
        JTextField siglaField = new JTextField(5);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Nome:"));
        form.add(nomeField);
        form.add(new JLabel("Sigla:"));
        form.add(siglaField);

        JButton btnAdd = new JButton("Cadastrar Clube");
        btnAdd.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String sigla = siglaField.getText().trim();
            if (nome.isEmpty() || sigla.isEmpty()) {
                logger.erro("Preencha nome e sigla.");
                return;
            }
            sistemaApostasService.cadastrarClube(nome, sigla);
            nomeField.setText("");
            siglaField.setText("");
            refresh();
            logger.info("Clube '" + nome + "' cadastrado.");
        });
        form.add(btnAdd);

        modelClubes = new DefaultListModel<>();
        listaClubes = new JList<>(modelClubes);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaClubes), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAbaCampeonato() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nomeField = new JTextField(15);
        JButton btnCriar = new JButton("Criar Campeonato");
        JPanel formCriar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formCriar.add(new JLabel("Nome:"));
        formCriar.add(nomeField);
        formCriar.add(btnCriar);

        modelCampeonatos = new DefaultListModel<>();
        comboCampeonatos = new JComboBox<>();
        comboClubesCamp = new JComboBox<>();

        btnCriar.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                logger.erro("Informe o nome.");
                return;
            }
            CampeonatoModel campeonato = sistemaApostasService.cadastrarCampeonato(nome);
            modelCampeonatos.addElement(campeonato);
            comboCampeonatos.addItem(campeonato);
            comboCampeonatoPartida.addItem(campeonato);
            nomeField.setText("");
            logger.info("Campeonato '" + nome + "' criado.");
        });

        JButton btnAddClube = new JButton("Adicionar Clube ao Campeonato");
        btnAddClube.addActionListener(e -> {
            CampeonatoModel campeonato = (CampeonatoModel) comboCampeonatos.getSelectedItem();
            ClubeModel clube = (ClubeModel) comboClubesCamp.getSelectedItem();
            if (campeonato == null || clube == null) {
                logger.erro("Selecione campeonato e clube.");
                return;
            }
            try {
                sistemaApostasService.adicionarClubeAoCampeonato(campeonato, clube);
                modelClubesDoCampeonato.clear();
                campeonato.getClubes().forEach(modelClubesDoCampeonato::addElement);
                // atualiza combos de mandante/visitante se o campeonato afetado estiver selecionado na aba Partidas
                if (campeonato.equals(comboCampeonatoPartida.getSelectedItem())) {
                    comboMandante.removeAllItems();
                    comboVisitante.removeAllItems();
                    campeonato.getClubes().forEach(c -> {
                        comboMandante.addItem(c);
                        comboVisitante.addItem(c);
                    });
                }
                logger.info("Clube adicionado ao campeonato.");
            } catch (IllegalStateException ex) {
                logger.erro(ex.getMessage());
            }
        });

        JPanel formAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formAdd.add(new JLabel("Campeonato:"));
        formAdd.add(comboCampeonatos);
        formAdd.add(new JLabel("Clube:"));
        formAdd.add(comboClubesCamp);
        formAdd.add(btnAddClube);

        modelClubesDoCampeonato = new DefaultListModel<>();
        comboCampeonatos.addActionListener(e -> {
            modelClubesDoCampeonato.clear();
            CampeonatoModel selecionado = (CampeonatoModel) comboCampeonatos.getSelectedItem();
            if (selecionado != null) {
                selecionado.getClubes().forEach(modelClubesDoCampeonato::addElement);
            }
        });

        JPanel painelCampeonatos = new JPanel(new BorderLayout());
        painelCampeonatos.add(new JLabel("Campeonatos:"), BorderLayout.NORTH);
        painelCampeonatos.add(new JScrollPane(new JList<>(modelCampeonatos)), BorderLayout.CENTER);

        JPanel painelClubes = new JPanel(new BorderLayout());
        painelClubes.add(new JLabel("Clubes do campeonato selecionado:"), BorderLayout.NORTH);
        painelClubes.add(new JScrollPane(new JList<>(modelClubesDoCampeonato)), BorderLayout.CENTER);

        JPanel listasPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        listasPanel.add(painelCampeonatos);
        listasPanel.add(painelClubes);

        JPanel center = new JPanel(new BorderLayout());
        center.add(formAdd, BorderLayout.NORTH);
        center.add(listasPanel, BorderLayout.CENTER);

        panel.add(formCriar, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAbaPartidas() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        comboCampeonatoPartida = new JComboBox<>();
        comboMandante = new JComboBox<>();
        comboVisitante = new JComboBox<>();
        fieldDataHora = new JTextField(LocalDateTime.now().plusDays(1).format(FMT), 13);

        comboCampeonatoPartida.addActionListener(e -> {
            comboMandante.removeAllItems();
            comboVisitante.removeAllItems();
            CampeonatoModel campeonato = (CampeonatoModel) comboCampeonatoPartida.getSelectedItem();
            if (campeonato != null) {
                campeonato.getClubes().forEach(c -> {
                    comboMandante.addItem(c);
                    comboVisitante.addItem(c);
                });
            }
        });

        JButton btnAdd = new JButton("Cadastrar Partida");
        btnAdd.addActionListener(e -> {
            CampeonatoModel campeonato = (CampeonatoModel) comboCampeonatoPartida.getSelectedItem();
            ClubeModel mandante = (ClubeModel) comboMandante.getSelectedItem();
            ClubeModel visitante = (ClubeModel) comboVisitante.getSelectedItem();
            if (campeonato == null) {
                logger.erro("Selecione um campeonato.");
                return;
            }
            if (mandante == null || visitante == null) {
                logger.erro("Selecione os clubes.");
                return;
            }
            if (mandante.equals(visitante)) {
                logger.erro("Mandante e visitante não podem ser iguais.");
                return;
            }
            LocalDateTime dt;
            try {
                dt = LocalDateTime.parse(fieldDataHora.getText().trim(), FMT);
            } catch (DateTimeParseException ex) {
                logger.erro("Data/hora inválida. Use o formato dd/MM/yyyy HH:mm");
                return;
            }
            try {
                sistemaApostasService.cadastrarPartida(campeonato, mandante, visitante, dt);
                refresh();
                logger.info("Partida cadastrada.");
            } catch (IllegalArgumentException ex) {
                logger.erro(ex.getMessage());
            }
        });

        JPanel form1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form1.add(new JLabel("Campeonato:"));
        form1.add(comboCampeonatoPartida);
        form1.add(new JLabel("Mandante:"));
        form1.add(comboMandante);
        form1.add(new JLabel("Visitante:"));
        form1.add(comboVisitante);
        form1.add(new JLabel("Data/Hora (dd/MM/yyyy HH:mm):"));
        form1.add(fieldDataHora);

        JPanel form2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form2.add(btnAdd);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(form1);
        form.add(form2);

        modelPartidas = new DefaultTableModel(
                new String[]{"Campeonato", "Mandante", "Visitante", "Data/Hora", "Resultado"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabela = new JTable(modelPartidas);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAbaResultados() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        comboPartidasResultado = new JComboBox<>();
        fieldGolsMandante = new JTextField("0", 3);
        fieldGolsVisitante = new JTextField("0", 3);

        JButton btnRegistrar = new JButton("Registrar Resultado");
        btnRegistrar.addActionListener(e -> {
            PartidaModel partida = (PartidaModel) comboPartidasResultado.getSelectedItem();
            if (partida == null) {
                logger.erro("Nenhuma partida selecionada.");
                return;
            }
            try {
                int gM = Integer.parseInt(fieldGolsMandante.getText().trim());
                int gV = Integer.parseInt(fieldGolsVisitante.getText().trim());
                if (gM < 0 || gV < 0) {
                    logger.erro("Gols não podem ser negativos.");
                    return;
                }
                sistemaApostasService.registrarResultadoPartida(partida, gM, gV);
                refresh();
                logger.info("Resultado registrado: " + partida);
                JOptionPane.showMessageDialog(AdminPanel.this, "Resultado registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                logger.erro("Informe valores numéricos para os gols.");
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Partida:"));
        form.add(comboPartidasResultado);
        form.add(new JLabel("Gols Mandante:"));
        form.add(fieldGolsMandante);
        form.add(new JLabel("Gols Visitante:"));
        form.add(fieldGolsVisitante);
        form.add(btnRegistrar);

        panel.add(form, BorderLayout.NORTH);
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
                ParticipanteModel p = ranking.get(i);
                modelRanking.addRow(new Object[]{i + 1, p.getNome(), p.getPontuacaoTotal()});
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
        // CLUBES
        modelClubes.clear();
        sistemaApostasService.getClubes().forEach(modelClubes::addElement);

        comboClubesCamp.removeAllItems();
        sistemaApostasService.getClubes().forEach(comboClubesCamp::addItem);

        // CAMPEONATOS
        modelCampeonatos.clear();
        comboCampeonatos.removeAllItems();
        comboCampeonatoPartida.removeAllItems();
        sistemaApostasService.getCampeonatos().forEach(c -> {
            modelCampeonatos.addElement(c);
            comboCampeonatos.addItem(c);
            comboCampeonatoPartida.addItem(c);
        });

        // PARTIDAS
        modelPartidas.setRowCount(0);
        comboPartidasResultado.removeAllItems();
        sistemaApostasService.getPartidas().forEach(p -> {
            modelPartidas.addRow(new Object[]{
                    p.getCampeonato(),
                    p.getMandante(), p.getVisitante(),
                    p.getDataHora().format(FMT),
                    p.temResultado() ? p.getResultado().toString() : "-"
            });
            if (!p.temResultado()) comboPartidasResultado.addItem(p);
        });

        // GRUPOS PARA RANKING
        comboGruposRanking.removeAllItems();
        sistemaApostasService.getGrupos().forEach(comboGruposRanking::addItem);
    }
}

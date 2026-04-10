package com.apostas.ui;

import com.apostas.service.ISistemaApostasService;
import com.apostas.service.SistemaApostasService;
import com.apostas.ui.panel.AdminPanel;
import com.apostas.ui.panel.ParticipantePanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final String SENHA_ADMIN = "admin123";

    public static final String CARD_LOGIN = "LOGIN";
    public static final String CARD_ADMIN = "ADMIN";
    public static final String CARD_PARTICIPANTE = "PARTICIPANTE";

    private final ISistemaApostasService sistemaApostasService = SistemaApostasService.getInstancia();

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private AdminPanel adminPanel;
    private ParticipantePanel participantePanel;

    public MainFrame() {
        super("Sistema de Apostas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        adminPanel = new AdminPanel(this, sistemaApostasService);
        participantePanel = new ParticipantePanel(this, sistemaApostasService);

        cardPanel.add(buildLoginPanel(), CARD_LOGIN);
        cardPanel.add(adminPanel, CARD_ADMIN);
        cardPanel.add(participantePanel, CARD_PARTICIPANTE);

        add(cardPanel);
        cardLayout.show(cardPanel, CARD_LOGIN);
    }

    public void showCard(String card) {
        if (CARD_ADMIN.equals(card)) adminPanel.refresh();
        if (CARD_PARTICIPANTE.equals(card)) participantePanel.refresh();
        cardLayout.show(cardPanel, card);
    }

    // TELA DE LOGIN

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel titulo = new JLabel("Sistema de Apostas", SwingConstants.CENTER);

        JLabel subtitulo = new JLabel("Selecione seu perfil para continuar", SwingConstants.CENTER);

        JButton btnAdmin = new JButton("Entrar como Administrador");
        JButton btnPartic = new JButton("Entrar como Participante");

        btnAdmin.addActionListener(e -> solicitarLoginAdmin());
        btnPartic.addActionListener(e -> showCard(CARD_PARTICIPANTE));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);
        gbc.gridy = 1;
        panel.add(subtitulo, gbc);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(btnAdmin, gbc);
        gbc.gridx = 1;
        panel.add(btnPartic, gbc);

        return panel;
    }

    private void solicitarLoginAdmin() {
        JPasswordField senhaField = new JPasswordField(15);
        int opt = JOptionPane.showConfirmDialog(this,
                new Object[]{"Senha do administrador:", senhaField},
                "Login - Administrador",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (opt == JOptionPane.OK_OPTION) {
            String senha = new String(senhaField.getPassword());
            if (sistemaApostasService.autenticarAdmin(senha)) {
                showCard(CARD_ADMIN);
            } else {
                JOptionPane.showMessageDialog(this, "Senha incorreta.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
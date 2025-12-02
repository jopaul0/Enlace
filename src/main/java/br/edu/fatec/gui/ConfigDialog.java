package br.edu.fatec.gui;

import br.edu.fatec.factory.ConnectionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConfigDialog extends JDialog {

    private final JTextField urlField = new JTextField(35);
    private final JTextField userField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private JButton connectButton;
    private boolean connectionSuccessful = false;

    public ConfigDialog(JFrame parent) {
        super(parent, "Configuração do Banco de Dados MySQL", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Dicas de configuração inicial
        urlField.setText("jdbc:mysql://localhost:3306/seu_nome_do_banco");
        userField.setText("root");

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // URL
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("URL do Banco:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        panel.add(urlField, gbc);

        // Usuário
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        panel.add(userField, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        panel.add(passwordField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        connectButton = new JButton("Testar e Conectar");
        connectButton.addActionListener(this::connectAction);
        panel.add(connectButton);
        return panel;
    }

    private void connectAction(ActionEvent e) {
        String url = urlField.getText().trim();
        String user = userField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (url.isEmpty() || user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "URL e Usuário são obrigatórios.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        connectButton.setEnabled(false);
        connectButton.setText("Testando...");

        // Tenta conectar usando o método utilitário
        boolean success = ConnectionFactory.testConnection(url, user, password);

        if (success) {
            // Salva as configurações para uso futuro por todos os DAOs
            ConnectionFactory.setConnectionParams(url, user, password);
            connectionSuccessful = true;
            JOptionPane.showMessageDialog(this, "Conexão bem-sucedida!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Falha na conexão. Verifique as credenciais e o status do MySQL.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            connectButton.setText("Testar e Conectar");
            connectButton.setEnabled(true);
        }
    }

    public boolean isConnectionSuccessful() {
        return connectionSuccessful;
    }
}
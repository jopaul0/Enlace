package br.edu.fatec.gui;

import br.edu.fatec.model.Service;
import br.edu.fatec.enums.DefaultStatus;
import javax.swing.*;
import java.awt.*;

public class ServiceFormDialog extends JDialog {
    private Service service;
    private boolean saved = false;

    private JTextField nameField = new JTextField(20);
    private JTextArea descriptionArea = new JTextArea(5, 20);

    public ServiceFormDialog(JFrame parent, Service serviceToEdit) {
        super(parent, serviceToEdit == null ? "Adicionar Serviço" : "Editar Serviço", true);
        this.service = serviceToEdit;

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST; gbc.weightx = 0;
        formPanel.add(new JLabel("Descrição:"), gbc);

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(descriptionScrollPane, gbc);

        if (service != null) {
            populateFields(service);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> saveService());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void populateFields(Service s) {
        nameField.setText(s.getName());
        descriptionArea.setText(s.getDescription());
    }

    private void saveService() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (name.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Descrição são campos obrigatórios.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Service newOrExistingService = service == null ? new Service() : service;

        newOrExistingService.setName(name);
        newOrExistingService.setDescription(description);

        if (service == null) {
            newOrExistingService.setStatus(DefaultStatus.active);
        }

        this.service = newOrExistingService;
        this.saved = true;
        dispose();
    }

    public Service getService() {
        return service;
    }

    public boolean isSaved() {
        return saved;
    }
}
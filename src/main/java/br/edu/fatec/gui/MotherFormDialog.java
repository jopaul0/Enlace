package br.edu.fatec.gui;

import br.edu.fatec.enums.DefaultStatus;
import br.edu.fatec.model.Mother;
import br.edu.fatec.util.DateUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MotherFormDialog extends JDialog {
    private Mother mother;
    private boolean saved = false;

    private JTextField nameField = new JTextField(20);
    private JTextField cpfField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JTextField phoneField = new JTextField(20);
    private JTextField addressField = new JTextField(20);
    private JTextField birthdayField = new JTextField(20);

    public MotherFormDialog(JFrame parent, Mother motherToEdit) {
        super(parent, motherToEdit == null ? "Adicionar Mãe" : "Editar Mãe", true);
        this.mother = motherToEdit;

        setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("CPF:"));
        formPanel.add(cpfField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Endereço:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Aniversário (dd/MM/yyyy):"));
        formPanel.add(birthdayField);

        if (mother != null) {
            populateFields(mother);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> saveMother());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void populateFields(Mother m) {
        nameField.setText(m.getName());
        cpfField.setText(m.getCpf());
        emailField.setText(m.getEmail());
        phoneField.setText(m.getPhone());
        addressField.setText(m.getAddress());
        
        nameField.setText(m.getName());

        if (m.getBirthday() != null) {
            birthdayField.setText(DateUtils.formatDate(m.getBirthday()));
        }
    }

    private void saveMother() {
        if (!validateFields()) {
            return;
        }

        Mother newOrExistingMother = mother == null ? new Mother() : mother;

        newOrExistingMother.setName(nameField.getText());
        newOrExistingMother.setCpf(cpfField.getText());
        newOrExistingMother.setEmail(emailField.getText());
        newOrExistingMother.setPhone(phoneField.getText());
        newOrExistingMother.setAddress(addressField.getText());

        try {
            LocalDate birthday = DateUtils.formatIsoDate(birthdayField.getText());
            newOrExistingMother.setBirthday(birthday);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data de aniversário inválida. Use o formato dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (mother == null) {
            newOrExistingMother.setStatus(DefaultStatus.active);
        }

        this.mother = newOrExistingMother;
        this.saved = true;
        dispose();
    }

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty() || cpfField.getText().trim().isEmpty() || birthdayField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, CPF e Aniversário são campos obrigatórios.", "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public Mother getMother() {
        return mother;
    }

    public boolean isSaved() {
        return saved;
    }
}
package br.edu.fatec.gui;

import br.edu.fatec.controllers.MotherController;
import br.edu.fatec.enums.DefaultStatus;
import br.edu.fatec.model.Mother;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MotherPanel extends JPanel {
    private final MotherController controller = new MotherController();
    private JTable motherTable;
    private DefaultTableModel tableModel;
    private JCheckBox showInactiveCheckBox;
    private JButton toggleStatusButton;

    public MotherPanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nome", "CPF", "Email", "Telefone", "Endereço", "Aniversário", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        motherTable = new JTable(tableModel);

        motherTable.getSelectionModel().addListSelectionListener(e -> updateToggleButtonState());

        JScrollPane scrollPane = new JScrollPane(motherTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");

        toggleStatusButton = new JButton("Mudar Status");
        toggleStatusButton.setEnabled(false);

        showInactiveCheckBox = new JCheckBox("Mostrar Inativas");
        JButton loadButton = new JButton("Recarregar");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(toggleStatusButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(showInactiveCheckBox);
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.NORTH);

        loadButton.addActionListener(e -> loadMothers());
        showInactiveCheckBox.addActionListener(e -> loadMothers());

        addButton.addActionListener(e -> addMotherAction());
        editButton.addActionListener(e -> editMotherAction());
        toggleStatusButton.addActionListener(e -> toggleStatusAction());

        loadMothers();
    }

    private String translateStatus(DefaultStatus status) {
        if (status == DefaultStatus.active) {
            return "Ativa";
        } else if (status == DefaultStatus.inactive) {
            return "Inativa";
        }
        return status.name();
    }

    private void updateToggleButtonState() {
        int selectedRow = motherTable.getSelectedRow();
        if (selectedRow == -1 || motherTable.getSelectionModel().getValueIsAdjusting()) {
            toggleStatusButton.setEnabled(false);
            toggleStatusButton.setText("Mudar Status");
            return;
        }

        String currentStatusText = (String) tableModel.getValueAt(selectedRow, 7);

        if ("Ativa".equals(currentStatusText)) {
            toggleStatusButton.setText("Inativar");
            toggleStatusButton.setEnabled(true);
        } else if ("Inativa".equals(currentStatusText)) {
            toggleStatusButton.setText("Ativar");
            toggleStatusButton.setEnabled(true);
        }
    }

    private void loadMothers() {
        tableModel.setRowCount(0);
        toggleStatusButton.setEnabled(false);

        try {
            List<Mother> mothers;
            if (showInactiveCheckBox.isSelected()) {
                mothers = controller.getAll();
            } else {
                mothers = controller.getAllActives();
            }

            for (Mother mother : mothers) {
                tableModel.addRow(new Object[]{
                        mother.getId(),
                        mother.getName(),
                        mother.getCpf(),
                        mother.getEmail(),
                        mother.getPhone(),
                        mother.getAddress(),
                        mother.getBirthday(),
                        translateStatus(mother.getStatus())
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar Mães: " + ex.getMessage(),
                    "Erro de Banco de Dados",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    private void addMotherAction() {
        MotherFormDialog dialog = new MotherFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            try {
                controller.addMother(dialog.getMother());
                JOptionPane.showMessageDialog(this, "Mãe adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadMothers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar Mãe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void editMotherAction() {
        int selectedRow = motherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma mãe para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String statusText = (String) tableModel.getValueAt(selectedRow, 7);
            DefaultStatus originalStatus = statusText.equals("Ativa") ? DefaultStatus.active : DefaultStatus.inactive;

            Mother motherToEdit = new Mother();
            motherToEdit.setId((Long) tableModel.getValueAt(selectedRow, 0));
            motherToEdit.setName((String) tableModel.getValueAt(selectedRow, 1));
            motherToEdit.setCpf((String) tableModel.getValueAt(selectedRow, 2));
            motherToEdit.setEmail((String) tableModel.getValueAt(selectedRow, 3));
            motherToEdit.setPhone((String) tableModel.getValueAt(selectedRow, 4));
            motherToEdit.setAddress((String) tableModel.getValueAt(selectedRow, 5));
            motherToEdit.setBirthday((LocalDate) tableModel.getValueAt(selectedRow, 6));
            motherToEdit.setStatus(originalStatus);


            MotherFormDialog dialog = new MotherFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), motherToEdit);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                controller.updateMother(dialog.getMother());
                JOptionPane.showMessageDialog(this, "Mãe atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadMothers();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar Mãe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void toggleStatusAction() {
        int selectedRow = motherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma mãe.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        String currentStatusText = (String) tableModel.getValueAt(selectedRow, 7);

        String action = ("Ativa".equals(currentStatusText)) ? "inativar" : "ativar";

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja " + action + " a mãe com ID: " + id + "?",
                "Confirmação de Status",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                Mother motherToChangeStatus = new Mother();
                motherToChangeStatus.setId(id);

                if ("Ativa".equals(currentStatusText)) {
                    controller.softDelete(motherToChangeStatus);
                    JOptionPane.showMessageDialog(this, "Mãe inativada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    controller.activateMother(motherToChangeStatus);
                    JOptionPane.showMessageDialog(this, "Mãe ativada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

                loadMothers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao " + action + " Mãe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
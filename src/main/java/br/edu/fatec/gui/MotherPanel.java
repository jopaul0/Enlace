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

    public MotherPanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nome", "CPF", "Email", "Telefone", "Aniversário", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        motherTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(motherTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");
        JButton softDeleteButton = new JButton("Inativar");

        showInactiveCheckBox = new JCheckBox("Mostrar Inativas");
        JButton loadButton = new JButton("Recarregar");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(softDeleteButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(showInactiveCheckBox);
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.NORTH);

        loadButton.addActionListener(e -> loadMothers());
        showInactiveCheckBox.addActionListener(e -> loadMothers());

        addButton.addActionListener(e -> addMotherAction());
        editButton.addActionListener(e -> editMotherAction());
        softDeleteButton.addActionListener(e -> softDeleteMotherAction());

        loadMothers();
    }

    private void loadMothers() {
        tableModel.setRowCount(0);

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
                        mother.getBirthday(),
                        mother.getStatus()
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

    // ---------------------- METHODS ----------------------

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
            Mother motherToEdit = new Mother();
            motherToEdit.setId((Long) tableModel.getValueAt(selectedRow, 0));
            motherToEdit.setName((String) tableModel.getValueAt(selectedRow, 1));
            motherToEdit.setCpf((String) tableModel.getValueAt(selectedRow, 2));
            motherToEdit.setEmail((String) tableModel.getValueAt(selectedRow, 3));
            motherToEdit.setPhone((String) tableModel.getValueAt(selectedRow, 4));
            motherToEdit.setBirthday((LocalDate) tableModel.getValueAt(selectedRow, 5));
            motherToEdit.setStatus((DefaultStatus) tableModel.getValueAt(selectedRow, 6));
            // O campo 'address' está faltando pois não é retornado pelo MotherDAO.mapResultSetToList na sua implementação.
            // O MotherFormDialog lida com isso.

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

    private void softDeleteMotherAction() {
        int selectedRow = motherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma mãe para inativar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja inativar a mãe com ID: " + id + "?",
                "Confirmação de Inativação",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                Mother motherToInactivate = new Mother();
                motherToInactivate.setId(id);

                controller.softDelete(motherToInactivate);
                JOptionPane.showMessageDialog(this, "Mãe inativada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadMothers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao inativar Mãe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
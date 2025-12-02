package br.edu.fatec.gui;

import br.edu.fatec.controllers.MeetController;
import br.edu.fatec.enums.MeetStatus;
import br.edu.fatec.model.Meet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MeetPanel extends JPanel {
    private final MeetController controller = new MeetController();
    private JTable meetTable;
    private DefaultTableModel tableModel;
    private JButton completeStatusButton;
    private JButton cancelStatusButton;
    private JButton viewDetailsButton;
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    public MeetPanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Data/Hora", "Endereço", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        meetTable = new JTable(tableModel);

        meetTable.getSelectionModel().addListSelectionListener(e -> updateButtonStates());

        JScrollPane scrollPane = new JScrollPane(meetTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addButton = new JButton("Adicionar");
        viewDetailsButton = new JButton("Ver Detalhes");
        JButton editButton = new JButton("Editar");
        viewDetailsButton.setEnabled(false);

        completeStatusButton = new JButton("Concluir Encontro");
        completeStatusButton.setEnabled(false);

        cancelStatusButton = new JButton("Cancelar Encontro");
        cancelStatusButton.setEnabled(false);

        JButton loadButton = new JButton("Recarregar");

        buttonPanel.add(addButton);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(editButton);
        buttonPanel.add(completeStatusButton);
        buttonPanel.add(cancelStatusButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.NORTH);

        loadButton.addActionListener(e -> loadMeets());

        addButton.addActionListener(e -> addMeetAction());
        viewDetailsButton.addActionListener(e -> viewDetailsAction());
        editButton.addActionListener(e -> editMeetAction());
        completeStatusButton.addActionListener(e -> toggleStatusAction(MeetStatus.completed));
        cancelStatusButton.addActionListener(e -> toggleStatusAction(MeetStatus.canceled));

        loadMeets();
    }

    private String translateStatus(MeetStatus status) {
        return switch (status) {
            case pending -> "Pendente";
            case completed -> "Concluído";
            case canceled -> "Cancelado";
        };
    }

    private void updateButtonStates() {
        int selectedRow = meetTable.getSelectedRow();
        if (selectedRow == -1 || meetTable.getSelectionModel().getValueIsAdjusting()) {
            completeStatusButton.setEnabled(false);
            cancelStatusButton.setEnabled(false);
            viewDetailsButton.setEnabled(false);
            return;
        }

        viewDetailsButton.setEnabled(true);

        String currentStatusText = (String) tableModel.getValueAt(selectedRow, 3);

        if ("Pendente".equals(currentStatusText)) {
            completeStatusButton.setEnabled(true);
            cancelStatusButton.setEnabled(true);
        } else {
            completeStatusButton.setEnabled(false);
            cancelStatusButton.setEnabled(false);
        }
    }

    private void loadMeets() {
        tableModel.setRowCount(0);
        updateButtonStates();

        try {
            List<Meet> meets = controller.getAllMeets();

            for (Meet meet : meets) {
                tableModel.addRow(new Object[]{
                        meet.getId(),
                        meet.getDate().format(DISPLAY_FORMATTER),
                        meet.getAddress(),
                        translateStatus(meet.getStatus())
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar Encontros: " + ex.getMessage(),
                    "Erro de Banco de Dados",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void editMeetAction() {
        int selectedRow = meetTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um encontro para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long meetId = (Long) tableModel.getValueAt(selectedRow, 0);
            Meet meetToEdit = controller.findById(meetId);

            if (meetToEdit == null) {
                JOptionPane.showMessageDialog(this, "Encontro não encontrado no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            MeetFormDialog dialog = new MeetFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), meetToEdit);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                controller.updateMeet(dialog.getMeet());
                JOptionPane.showMessageDialog(this, "Encontro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadMeets();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar Encontro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void addMeetAction() {
        MeetFormDialog dialog = new MeetFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            try {
                controller.addMeet(dialog.getMeet());
                JOptionPane.showMessageDialog(this, "Encontro adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadMeets();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar Encontro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void viewDetailsAction() {
        int selectedRow = meetTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        try {
            Long meetId = (Long) tableModel.getValueAt(selectedRow, 0);
            Meet meetToView = controller.findById(meetId);

            if (meetToView != null) {
                EnlaceDetailsDialog dialog = new EnlaceDetailsDialog((JFrame) SwingUtilities.getWindowAncestor(this), meetToView);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Encontro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar detalhes do Encontro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void toggleStatusAction(MeetStatus newStatus) {
        int selectedRow = meetTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        String actionName = (newStatus == MeetStatus.completed) ? "concluir" : "cancelar";

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja " + actionName + " o encontro com ID: " + id + "?",
                "Confirmação de Status",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                Meet meetToChangeStatus = new Meet();
                meetToChangeStatus.setId(id);

                boolean success = false;
                if (newStatus == MeetStatus.completed) {
                    success = controller.setCompleted(meetToChangeStatus);
                } else if (newStatus == MeetStatus.canceled) {
                    success = controller.softDelete(meetToChangeStatus);
                }

                if (success) {
                    JOptionPane.showMessageDialog(this, "Encontro " + actionName + "do com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    loadMeets();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Encontro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao tentar " + actionName + " o Encontro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
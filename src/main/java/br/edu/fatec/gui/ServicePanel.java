package br.edu.fatec.gui;

import br.edu.fatec.controllers.ServiceController;
import br.edu.fatec.enums.DefaultStatus;
import br.edu.fatec.model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServicePanel extends JPanel {
    private final ServiceController controller = new ServiceController();
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JCheckBox showInactiveCheckBox;
    private JButton toggleStatusButton;

    public ServicePanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nome", "Descrição", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        serviceTable = new JTable(tableModel);

        serviceTable.getSelectionModel().addListSelectionListener(e -> updateToggleButtonState());

        JScrollPane scrollPane = new JScrollPane(serviceTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");

        toggleStatusButton = new JButton("Mudar Status");
        toggleStatusButton.setEnabled(false);

        showInactiveCheckBox = new JCheckBox("Mostrar Inativos");
        JButton loadButton = new JButton("Recarregar");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(toggleStatusButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(showInactiveCheckBox);
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.NORTH);

        loadButton.addActionListener(e -> loadServices());
        showInactiveCheckBox.addActionListener(e -> loadServices());

        addButton.addActionListener(e -> addServiceAction());
        editButton.addActionListener(e -> editServiceAction());
        toggleStatusButton.addActionListener(e -> toggleStatusAction());

        loadServices();
    }

    private String translateStatus(DefaultStatus status) {
        if (status == DefaultStatus.active) {
            return "Ativo";
        } else if (status == DefaultStatus.inactive) {
            return "Inativo";
        }
        return status.name();
    }

    private void updateToggleButtonState() {
        int selectedRow = serviceTable.getSelectedRow();
        if (selectedRow == -1 || serviceTable.getSelectionModel().getValueIsAdjusting()) {
            toggleStatusButton.setEnabled(false);
            toggleStatusButton.setText("Mudar Status");
            return;
        }

        String currentStatusText = (String) tableModel.getValueAt(selectedRow, 3);

        if ("Ativo".equals(currentStatusText)) {
            toggleStatusButton.setText("Inativar");
            toggleStatusButton.setEnabled(true);
        } else if ("Inativo".equals(currentStatusText)) {
            toggleStatusButton.setText("Ativar");
            toggleStatusButton.setEnabled(true);
        }
    }


    private void loadServices() {
        tableModel.setRowCount(0);
        toggleStatusButton.setEnabled(false);

        try {
            List<Service> services;
            if (showInactiveCheckBox.isSelected()) {
                services = controller.findAllServices();
            } else {
                services = controller.findAllActives();
            }

            for (Service service : services) {
                tableModel.addRow(new Object[]{
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        translateStatus(service.getStatus())
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar Serviços: " + ex.getMessage(),
                    "Erro de Banco de Dados",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void addServiceAction() {
        ServiceFormDialog dialog = new ServiceFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            try {
                controller.addService(dialog.getService());
                JOptionPane.showMessageDialog(this, "Serviço adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadServices();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar Serviço: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void editServiceAction() {
        int selectedRow = serviceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um serviço para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String statusText = (String) tableModel.getValueAt(selectedRow, 3);
            DefaultStatus originalStatus = statusText.equals("Ativo") ? DefaultStatus.active : DefaultStatus.inactive;

            Service serviceToEdit = new Service();
            serviceToEdit.setId((Long) tableModel.getValueAt(selectedRow, 0));
            serviceToEdit.setName((String) tableModel.getValueAt(selectedRow, 1));
            serviceToEdit.setDescription((String) tableModel.getValueAt(selectedRow, 2));
            serviceToEdit.setStatus(originalStatus);

            ServiceFormDialog dialog = new ServiceFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), serviceToEdit);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                controller.updateService(dialog.getService());
                JOptionPane.showMessageDialog(this, "Serviço atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadServices();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar Serviço: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void toggleStatusAction() {
        int selectedRow = serviceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um serviço.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        String currentStatusText = (String) tableModel.getValueAt(selectedRow, 3);

        String action = ("Ativo".equals(currentStatusText)) ? "inativar" : "ativar";

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja " + action + " o serviço com ID: " + id + "?",
                "Confirmação de Status",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                Service serviceToChangeStatus = new Service();
                serviceToChangeStatus.setId(id);

                if ("Ativo".equals(currentStatusText)) {
                    controller.softDelete(serviceToChangeStatus);
                    JOptionPane.showMessageDialog(this, "Serviço inativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    controller.activateService(serviceToChangeStatus);
                    JOptionPane.showMessageDialog(this, "Serviço ativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

                loadServices();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao " + action + " Serviço: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
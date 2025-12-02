package br.edu.fatec.gui;

import br.edu.fatec.controllers.MotherController;
import br.edu.fatec.controllers.ServiceController;
import br.edu.fatec.enums.MeetStatus;
import br.edu.fatec.model.Enlace;
import br.edu.fatec.model.Meet;
import br.edu.fatec.model.Mother;
import br.edu.fatec.model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MeetFormDialog extends JDialog {
    private Meet meet;
    private boolean saved = false;

    private final MotherController motherController = new MotherController();
    private final ServiceController serviceController = new ServiceController();

    private JTextField dateField = new JTextField(15);
    private JTextField timeField = new JTextField(5);
    private JTextField addressField = new JTextField(20);

    private JComboBox<Mother> motherComboBox;
    private JComboBox<Service> serviceComboBox;
    private JTable enlaceTable; // Adicionado para uso na remoção
    private DefaultTableModel enlaceTableModel;

    private List<Mother> activeMothers;
    private List<Service> activeServices;
    private final List<Enlace> currentEnlaces = new ArrayList<>();
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    public MeetFormDialog(JFrame parent, Meet meetToEdit) {
        super(parent, meetToEdit == null ? "Adicionar Encontro" : "Editar Encontro", true);
        this.meet = meetToEdit != null ? meetToEdit : new Meet();

        if (meetToEdit != null) {
            this.currentEnlaces.addAll(meetToEdit.getEnlaces());
        }

        try {
            activeMothers = motherController.getAllActives();
            activeServices = serviceController.findAllActives();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar Mães ou Serviços ativos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            activeMothers = new ArrayList<>();
            activeServices = new ArrayList<>();
        }

        setLayout(new BorderLayout(10, 10));

        add(createFormPanel(), BorderLayout.NORTH);
        add(createEnlacePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        if (meetToEdit != null) {
            populateFields(meetToEdit);
        }

        pack();
        setLocationRelativeTo(parent);
    }

    private void populateFields(Meet m) {
        dateField.setText(m.getDate().format(DATE_FORMATTER));
        timeField.setText(m.getDate().format(TIME_FORMATTER));
        addressField.setText(m.getAddress());

        for(Enlace enlace : m.getEnlaces()) {
            enlaceTableModel.addRow(new Object[]{
                    enlace.getMother().getId(),
                    enlace.getMother().getName(),
                    enlace.getService().getId(),
                    enlace.getService().getName()
            });
        }
    }


    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Encontro"));

        formPanel.add(new JLabel("Data (dd/MM/yyyy):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Hora (HH:mm):"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Endereço:"));
        formPanel.add(addressField);

        return formPanel;
    }

    private JPanel createEnlacePanel() {
        JPanel enlacePanel = new JPanel(new BorderLayout(5, 5));
        enlacePanel.setBorder(BorderFactory.createTitledBorder("Vincular Mães e Serviços"));

        // Painel de Adição
        JPanel addEnlacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // Custom Renderer para exibir apenas o nome
        motherComboBox = new JComboBox<>(activeMothers.toArray(new Mother[0]));
        motherComboBox.setRenderer(new MotherListCellRenderer());

        serviceComboBox = new JComboBox<>(activeServices.toArray(new Service[0]));
        serviceComboBox.setRenderer(new ServiceListCellRenderer());

        JButton addEnlaceButton = new JButton("Adicionar Vínculo");
        JButton removeEnlaceButton = new JButton("Remover Selecionado");

        addEnlacePanel.add(new JLabel("Mãe:"));
        addEnlacePanel.add(motherComboBox);
        addEnlacePanel.add(new JLabel("Serviço:"));
        addEnlacePanel.add(serviceComboBox);
        addEnlacePanel.add(addEnlaceButton);
        addEnlacePanel.add(removeEnlaceButton);

        String[] columnNames = {"Mãe (ID)", "Nome da Mãe", "Serviço (ID)", "Nome do Serviço"};
        enlaceTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enlaceTable = new JTable(enlaceTableModel);
        JScrollPane scrollPane = new JScrollPane(enlaceTable);

        addEnlaceButton.addActionListener(e -> addEnlaceAction());
        removeEnlaceButton.addActionListener(e -> removeEnlaceAction());

        enlacePanel.add(addEnlacePanel, BorderLayout.NORTH);
        enlacePanel.add(scrollPane, BorderLayout.CENTER);

        return enlacePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> saveMeet());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void addEnlaceAction() {
        Mother selectedMother = (Mother) motherComboBox.getSelectedItem();
        Service selectedService = (Service) serviceComboBox.getSelectedItem();

        if (selectedMother == null || selectedService == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma Mãe e um Serviço.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Enlace newEnlace = new Enlace(selectedService, selectedMother);

        for (Enlace existing : currentEnlaces) {
            if (existing.getMother().getId().equals(selectedMother.getId()) && existing.getService().getId().equals(selectedService.getId())) {
                JOptionPane.showMessageDialog(this, "Este vínculo (Mãe + Serviço) já foi adicionado.", "Duplicidade", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        currentEnlaces.add(newEnlace);
        enlaceTableModel.addRow(new Object[]{
                selectedMother.getId(),
                selectedMother.getName(),
                selectedService.getId(),
                selectedService.getName()
        });
    }

    private void removeEnlaceAction() {
        int selectedRow = enlaceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um vínculo na tabela para remover.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long motherId = (Long) enlaceTableModel.getValueAt(selectedRow, 0);
        Long serviceId = (Long) enlaceTableModel.getValueAt(selectedRow, 2);

        currentEnlaces.removeIf(enlace ->
                enlace.getMother().getId().equals(motherId) &&
                        enlace.getService().getId().equals(serviceId)
        );

        enlaceTableModel.removeRow(selectedRow);
    }


    private void saveMeet() {
        String dateStr = dateField.getText().trim();
        String timeStr = timeField.getText().trim();
        String address = addressField.getText().trim();

        if (dateStr.isEmpty() || timeStr.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data, Hora e Endereço são obrigatórios.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentEnlaces.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O Encontro deve ter pelo menos um vínculo (Mãe e Serviço).", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateStr + " " + timeStr, DATETIME_FORMATTER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de Data/Hora inválido. Use dd/MM/yyyy e HH:mm.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.meet.setDate(dateTime);
        this.meet.setAddress(address);

        if (this.meet.getId() == null) {
            this.meet.setStatus(MeetStatus.pending);
        }

        this.meet.setEnlaces(currentEnlaces);

        this.saved = true;
        dispose();
    }

    public Meet getMeet() {
        return meet;
    }

    public boolean isSaved() {
        return saved;
    }

    private static class MotherListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Mother) {
                setText(((Mother) value).getName());
            }
            return this;
        }
    }

    private static class ServiceListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Service) {
                setText(((Service) value).getName());
            }
            return this;
        }
    }
}
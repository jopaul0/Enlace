package br.edu.fatec.gui;

import br.edu.fatec.controllers.MotherController;
import br.edu.fatec.model.Mother;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MotherPanel extends JPanel {
    private final MotherController controller = new MotherController();
    private JTable motherTable;
    private DefaultTableModel tableModel;

    public MotherPanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nome", "CPF", "Email", "Telefone", "Aniversário", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        motherTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(motherTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton("Carregar Mães Ativas");
        buttonPanel.add(loadButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadActiveMothers());

        loadActiveMothers();
    }

    private void loadActiveMothers() {
        tableModel.setRowCount(0);

        try {
            List<Mother> mothers = controller.getAllActives();

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
}

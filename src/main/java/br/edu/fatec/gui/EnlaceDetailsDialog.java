package br.edu.fatec.gui;

import br.edu.fatec.enums.MeetStatus;
import br.edu.fatec.model.Enlace;
import br.edu.fatec.model.Meet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EnlaceDetailsDialog extends JDialog {

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public EnlaceDetailsDialog(JFrame parent, Meet meet) {
        super(parent, "Detalhes do Encontro (ID: " + meet.getId() + ")", true);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(4, 2));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações do Encontro"));

        infoPanel.add(new JLabel("Data/Hora:"));
        infoPanel.add(new JLabel(meet.getDate().format(DISPLAY_FORMATTER)));

        infoPanel.add(new JLabel("Endereço:"));
        infoPanel.add(new JLabel(meet.getAddress()));

        infoPanel.add(new JLabel("Status:"));
        infoPanel.add(new JLabel(translateStatus(meet.getStatus())));

        String[] columnNames = {"Mãe (ID)", "Nome da Mãe", "Serviço (ID)", "Nome do Serviço"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable enlaceTable = new JTable(tableModel);

        List<Enlace> enlaces = meet.getEnlaces();
        if (enlaces != null) {
            for (Enlace enlace : enlaces) {
                tableModel.addRow(new Object[]{
                        enlace.getMother().getId(),
                        enlace.getMother().getName(),
                        enlace.getService().getId(),
                        enlace.getService().getName()
                });
            }
        }

        JScrollPane scrollPane = new JScrollPane(enlaceTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Mães e Serviços Vinculados"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(infoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(700, 400);
        setLocationRelativeTo(parent);
    }

    private String translateStatus(MeetStatus status) {
        return switch (status) {
            case pending -> "Pendente";
            case completed -> "Concluído";
            case canceled -> "Cancelado";
        };
    }
}
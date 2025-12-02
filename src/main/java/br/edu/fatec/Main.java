package br.edu.fatec;

import br.edu.fatec.gui.ConfigDialog;
import br.edu.fatec.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConfigDialog configDialog = new ConfigDialog(null);
            configDialog.setVisible(true);

            if (configDialog.isConnectionSuccessful()) {
                new MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "A configuração do banco de dados não foi completada. O sistema será encerrado.",
                        "Inicialização Interrompida",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}
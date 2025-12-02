package br.edu.fatec.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Gerenciamento - Enlace");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        try {
            String iconPath = "images/logo.png";

            Image icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath)).getImage();
            setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone da aplicação: " + e.getMessage());
        }


        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Mães", new MotherPanel());
        tabbedPane.addTab("Serviços", new ServicePanel());
        tabbedPane.addTab("Encontros", new MeetPanel());

        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Ajuda");
        JMenuItem aboutItem = new JMenuItem("Sobre e Especificações");

        aboutItem.addActionListener(e -> {
            HelpDialog dialog = new HelpDialog(this);
            dialog.setVisible(true);
        });

        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
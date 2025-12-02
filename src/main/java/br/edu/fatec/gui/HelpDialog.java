package br.edu.fatec.gui;

import javax.swing.*;
import java.awt.*;

public class HelpDialog extends JDialog {

    public HelpDialog(JFrame parent) {
        super(parent, "Ajuda e Sobre o Sistema Enlace", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Especificações do Projeto", createSpecsPanel());
        tabbedPane.addTab("Ajuda Rápida", createHelpPanel());

        add(tabbedPane, BorderLayout.CENTER);

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);

        add(closePanel, BorderLayout.SOUTH);

        setSize(600, 450);
        setLocationRelativeTo(parent);
    }

    private JPanel createSpecsPanel() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Informações e citações para as especificações
        String specs = "=================================================================\n"
                + "                  ESPECIFICAÇÕES DO PROJETO ENLACE                 \n"
                + "=================================================================\n"
                + "\n"
                + "OBJETIVO PRINCIPAL:\n"
                + "Sistema de Gerenciamento para o projeto 'Enlace', focado na \n"
                + "gestão de Mães, Serviços e Encontros (Meets/Enlaces).\n"
                + "\n"
                + "TECNOLOGIAS UTILIZADAS:\n"
                + "-----------------------------------------------------------------\n"
                + "Linguagem: Java (versão 8 ou superior)\n"
                + "Interface Gráfica: Swing (Pacote javax.swing)\n"
                + "Banco de Dados: MySQL (Conexão via JDBC)\n"
                + "Padrão de Acesso a Dados: DAO (Data Access Object)\n"
                + "Facilitador de Código: Project Lombok (para Getters/Setters)\n"
                + "Processamento JSON: Google Gson (para Enlaces em MeetDAO)\n"
                + "Manipulação de Data: java.time.* e DateUtils (DD/MM/YYYY)\n"
                + "-----------------------------------------------------------------\n"
                + "\n"
                + "ESTRUTURA DE DADOS PRINCIPAL (Modelos):\n"
                + "* Mother (Mãe)\n"
                + "* Service (Serviço)\n"
                + "* Meet (Encontro)\n"
                + "* Enlace (Vínculo)\n"
                + "=================================================================\n";

        textArea.setText(specs);

        return new JPanel(new BorderLayout(5, 5)) {{
            add(new JScrollPane(textArea), BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }};
    }

    private JPanel createHelpPanel() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Informações de ajuda rápida
        String help = "AJUDA RÁPIDA AO USUÁRIO:\n"
                + "---------------------------------------------------------------------------------------------------------------------------------\n"
                + "1. **Inicialização do Sistema**: O sistema requer a **URL**, **Usuário** e **Senha** válidos do seu banco de dados MySQL para ser iniciado. O formato da URL deve ser: `jdbc:mysql://[host]:[porta]/[nome_do_banco]`.\n"
                + "\n"
                + "2. **Gestão de Status (Soft Delete)**:\n"
                + "   - As telas de **Mães** e **Serviços** utilizam o conceito de *Soft Delete* (Exclusão Lógica).\n"
                + "   - O botão **'Mudar Status'** alterna entre Ativo e Inativo. Itens Inativos são ocultados por padrão nas listagens, mas podem ser reativados a qualquer momento.\n"
                + "\n"
                + "3. **Gerenciamento de Encontros (Meets)**:\n"
                + "   - **Adicionar/Editar**: Permite vincular Mães e Serviços (Enlaces) a um Encontro.\n"
                + "   - **Ver Detalhes**: Exibe todos os vínculos (Mães e Serviços) do encontro selecionado.\n"
                + "   - **Concluir/Cancelar**: Altera o `status` do Encontro de 'Pendente' para 'Concluído' ou 'Cancelado'.\n"
                + "\n"
                + "4. **Formatos de Data/Hora**:\n"
                + "   - Datas de Aniversário: **DD/MM/YYYY**.\n"
                + "   - Data/Hora do Encontro: **DD/MM/YYYY** e **HH:MM** separadamente.\n"
                + "---------------------------------------------------------------------------------------------------------------------------------\n";

        textArea.setText(help);

        return new JPanel(new BorderLayout(5, 5)) {{
            add(new JScrollPane(textArea), BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }};
    }
}
package com.reversedots.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Reverse Dots - Menú Principal");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //centrar ventana
        setLayout(new GridLayout(4, 1, 10, 10)); // 4 filas para botones

        initComponents();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("REVERSE DOTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton btnNewGame = new JButton("Nueva Partida");
        JButton btnLoadGame = new JButton("Cargar Partida");
        JButton btnPlayers = new JButton("Ver Jugadores / Estadísticas");

        //agregar componentes
        add(titleLabel);
        add(btnNewGame);
        add(btnLoadGame);
        add(btnPlayers);

        //eventos básicos
        btnNewGame.addActionListener(e -> startSetup());
    }

    private void startSetup() {
        // Panel contenedor con Grid para organizar etiquetas y campos
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        JTextField p1Field = new JTextField("Jugador 1");
        JTextField p2Field = new JTextField("Jugador 2");

        // Regla 1: El tamaño N debe ser par entre 4 y 20
        Integer[] validSizes = {4, 6, 8, 10, 12, 14, 16, 18, 20};
        JComboBox<Integer> sizeCombo = new JComboBox<>(validSizes);
        sizeCombo.setSelectedItem(8); // Estándar por defecto

        panel.add(new JLabel("Nombre Jugador 1 (Fichas Negras):"));
        panel.add(p1Field);
        panel.add(new JLabel("Nombre Jugador 2 (Fichas Blancas):"));
        panel.add(p2Field);
        panel.add(new JLabel("Tamaño del tablero (N x N):"));
        panel.add(sizeCombo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Configuración de Partida",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String name1 = p1Field.getText().trim();
            String name2 = p2Field.getText().trim();
            int boardSize = (int) sizeCombo.getSelectedItem();

            if (name1.isEmpty() || name2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce nombres válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                startSetup(); // Reabrir si hay error
            } else {
                // 1. Instanciar los Repositorios (Persona A/B)
                // Nota: Asegúrate de tener estas clases creadas en el paquete repository
                com.reversedots.repository.PlayerRepository playerRepo = new com.reversedots.repository.FilePlayerRepository();
                com.reversedots.repository.GameRepository gameRepo = new com.reversedots.repository.FileGameRepository();

                // 2. Crear el Controlador e inyectar repositorios
                com.reversedots.controller.GameController controller = new com.reversedots.controller.GameController(playerRepo, gameRepo);

                // 3. Crear los objetos Jugador (Model)
                com.reversedots.model.Player p1 = new com.reversedots.model.Player(name1);
                com.reversedots.model.Player p2 = new com.reversedots.model.Player(name2);

                // 4. Iniciar la lógica del juego en el controlador
                controller.initNewGame(boardSize, p1, p2);

                // 5. Lanzar la ventana del juego pasándole el controlador
                GameFrame gameWindow = new GameFrame(controller, boardSize);
                gameWindow.setVisible(true);

                // 6. Opcional: Ocultar el menú principal mientras juegan
                this.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
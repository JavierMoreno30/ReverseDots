package com.reversedots.view;

import com.reversedots.controller.GameController;
import com.reversedots.model.Player;
import com.reversedots.repository.FileGameRepository;
import com.reversedots.repository.FilePlayerRepository;
import com.reversedots.repository.GameRepository;
import com.reversedots.repository.PlayerRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Reverse Dots - Menú Principal");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        initComponents();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("REVERSE DOTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton btnNewGame = new JButton("Nueva Partida");
        JButton btnLoadGame = new JButton("Cargar Partida");
        JButton btnPlayers = new JButton("Ver Jugadores / Estadísticas");
        JButton btnExit = new JButton("Salir");

        add(titleLabel);
        add(btnNewGame);
        add(btnLoadGame);
        add(btnPlayers);
        add(btnExit);

        btnNewGame.addActionListener(e -> startSetup());
        btnLoadGame.addActionListener(e -> loadGame());
        btnPlayers.addActionListener(e -> showPlayersStats());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void startSetup() {
        PlayerRepository playerRepo = new FilePlayerRepository(); //lee saves/players.txt

        //cargar nombres existentes *ya probado con Javier y Akil*
        List<Player> existingPlayers = playerRepo.findAll();
        List<String> names = new ArrayList<>();
        names.add("(Nuevo jugador...)");
        for (Player p : existingPlayers) {
            names.add(p.getName());
        }

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        //Jugador 1 (Negras)
        JComboBox<String> p1Combo = new JComboBox<>(names.toArray(new String[0]));
        JTextField p1NewField = new JTextField();
        p1NewField.setEnabled(true);

        //Jugador 2 (Blancas)
        JComboBox<String> p2Combo = new JComboBox<>(names.toArray(new String[0]));
        JTextField p2NewField = new JTextField();
        p2NewField.setEnabled(true);

        //activar/desactivar campos según selección
        p1Combo.addActionListener(e -> {
            boolean isNew = "(Nuevo jugador...)".equals(p1Combo.getSelectedItem());
            p1NewField.setEnabled(isNew);
            if (!isNew) p1NewField.setText("");
        });

        p2Combo.addActionListener(e -> {
            boolean isNew = "(Nuevo jugador...)".equals(p2Combo.getSelectedItem());
            p2NewField.setEnabled(isNew);
            if (!isNew) p2NewField.setText("");
        });

        Integer[] validSizes = new Integer[]{4, 6, 8, 10, 12, 14, 16, 18, 20};
        JComboBox<Integer> sizeCombo = new JComboBox<>(validSizes);
        sizeCombo.setSelectedItem(8);

        panel.add(new JLabel("Jugador 1 (Negras) - Selecciona o crea:"));
        panel.add(p1Combo);
        panel.add(new JLabel("Si es nuevo, escribe el nombre:"));
        panel.add(p1NewField);

        panel.add(new JLabel("Jugador 2 (Blancas) - Selecciona o crea:"));
        panel.add(p2Combo);
        panel.add(new JLabel("Si es nuevo, escribe el nombre:"));
        panel.add(p2NewField);

        panel.add(new JLabel("Tamaño del tablero (N x N):"));
        panel.add(sizeCombo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Configuración de Partida",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String selected1 = (String) p1Combo.getSelectedItem();
        String selected2 = (String) p2Combo.getSelectedItem();

        String name1 = "(Nuevo jugador...)".equals(selected1) ? p1NewField.getText().trim() : selected1;
        String name2 = "(Nuevo jugador...)".equals(selected2) ? p2NewField.getText().trim() : selected2;

        if (name1.isEmpty() || name2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los nombres son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            startSetup();
            return;
        }

        if (name1.equalsIgnoreCase(name2)) {
            JOptionPane.showMessageDialog(this, "Los jugadores deben ser distintos.", "Error", JOptionPane.ERROR_MESSAGE);
            startSetup();
            return;
        }

        int boardSize = (int) sizeCombo.getSelectedItem();

        GameRepository gameRepo = new FileGameRepository();

        //reutilizar o crear jugadores (sin que hayan   duplicados)
        Player p1 = playerRepo.findByName(name1);
        if (p1 == null) {
            p1 = new Player(name1);
            playerRepo.save(p1);
        }

        Player p2 = playerRepo.findByName(name2);
        if (p2 == null) {
            p2 = new Player(name2);
            playerRepo.save(p2);
        }

        GameController controller = new GameController(playerRepo, gameRepo);
        controller.initNewGame(boardSize, p1, p2);

        GameFrame gameWindow = new GameFrame(controller, boardSize);
        gameWindow.setVisible(true);
        this.dispose();
    }

    private void loadGame() {
        File dir = new File("saves");

        if (!dir.exists() || !dir.isDirectory()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No existe la carpeta 'saves/' o no hay partidas guardadas todavía.",
                    "Sin partidas",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".dat"));

        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay partidas guardadas en 'saves/'.",
                    "Sin partidas",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));

        String[] options = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            options[i] = files[i].getName();
        }

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona una partida guardada:",
                "Cargar partida",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selected == null) return;

        try {
            PlayerRepository playerRepo = new FilePlayerRepository();
            GameRepository gameRepo = new FileGameRepository();

            GameController controller = new GameController(playerRepo, gameRepo);
            controller.loadGame(selected.trim());

            int size = controller.getBoard().getSize();

            GameFrame gameWindow = new GameFrame(controller, size);
            gameWindow.setVisible(true);
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cargar la partida: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showPlayersStats() {
        PlayerRepository playerRepo = new FilePlayerRepository();
        List<Player> players = playerRepo.findAll();

        if (players == null || players.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay jugadores registrados todavía.",
                    "Jugadores",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        players.sort((a, b) -> {
            int cmp = Integer.compare(b.getGamesWon(), a.getGamesWon());
            if (cmp != 0) return cmp;
            return Integer.compare(a.getGamesLost(), b.getGamesLost());
        });

        String[] columns = {"Jugador", "Victorias", "Derrotas"};
        Object[][] data = new Object[players.size()][3];

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            data[i][0] = p.getName();
            data[i][1] = p.getGamesWon();
            data[i][2] = p.getGamesLost();
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);

        JDialog dialog = new JDialog(this, "Jugadores / Estadísticas", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Cerrar");
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel south = new JPanel();
        south.add(closeBtn);
        dialog.add(south, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

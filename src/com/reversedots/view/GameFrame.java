package com.reversedots.view;

import com.reversedots.controller.GameController;
import com.reversedots.model.PieceColor;
import com.reversedots.enums.GameResult;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameFrame extends JFrame {
    private GameController controller;
    private JButton[][] buttons;
    private JLabel statusLabel;

    public GameFrame(GameController controller, int size) {
        this.controller = controller;
        this.buttons = new JButton[size][size];

        setTitle("Reverse Dots - Partida en curso");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Al cerrar el juego, cerramos la app o volvemos al menú
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(statusLabel, BorderLayout.NORTH);

        initBoard(size);

        JButton btnSave = new JButton("Guardar Partida");
        btnSave.addActionListener(e -> {
            try {
                controller.saveCurrentGame("savegame.dat");
                JOptionPane.showMessageDialog(this, "Partida guardada.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar.");
            }
        });

        JPanel southPanel = new JPanel();
        southPanel.add(btnSave);
        add(southPanel, BorderLayout.SOUTH);

        updateBoardUI();
    }

    private void initBoard(int size) {
        JPanel boardPanel = new JPanel(new GridLayout(size, size));
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                JButton btn = new JButton();
                btn.setBackground(new Color(34, 139, 34));
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                int row = r, col = c;
                btn.addActionListener(e -> handleMove(row, col));
                buttons[r][c] = btn;
                boardPanel.add(btn);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    private void handleMove(int r, int c) {
        GameResult result = controller.makeMove(r, c);

        switch (result) {
            case SUCCESS: break;
            case TURN_SKIPPED:
                JOptionPane.showMessageDialog(this, "No hay movimientos posibles, se salta el turno.");
                break;
            case GAME_OVER:
                updateBoardUI();
                JOptionPane.showMessageDialog(this, "JUEGO TERMINADO\n" + controller.getWinnerMessage());
                return;
            case INVALID_MOVE:
                return; // Ignorar clics en celdas inválidas
        }
        updateBoardUI();
    }

    public void updateBoardUI() {
        int size = buttons.length;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                PieceColor color = controller.getBoard().getCell(r, c);
                buttons[r][c].setIcon(color == null ? null : createCircleIcon(color == PieceColor.BLACK ? Color.BLACK : Color.WHITE));
            }
        }
        String turn = controller.getCurrentTurn() == PieceColor.BLACK ? "NEGRAS" : "BLANCAS";
        statusLabel.setText("Turno de: " + turn + " | " + controller.getScoreBoard());
    }

    private ImageIcon createCircleIcon(Color color) {
        int size = 40;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillOval(5, 5, size - 10, size - 10);
        g2.dispose();
        return new ImageIcon(img);
    }
}
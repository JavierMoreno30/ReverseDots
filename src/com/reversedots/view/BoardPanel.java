package com.reversedots.view;

import com.reversedots.model.PieceColor;
import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private int size;
    private JButton[][] cells;

    public BoardPanel(int size) {
        this.size = size;
        this.setLayout(new GridLayout(size, size));
        this.cells = new JButton[size][size];

        initializeGrid();
    }

    private void initializeGrid() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                JButton btn = new JButton();
                btn.setBackground(new Color(34, 139, 34));
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[r][c] = btn;
                add(btn);
            }
        }
    }

    //Metodo para actualizar tablero
    public void refreshBoard(PieceColor[][] boardData) {
    }
}
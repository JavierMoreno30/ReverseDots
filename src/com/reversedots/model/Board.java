package com.reversedots.model;

public class Board implements java.io.Serializable {
    private PieceColor[][] cells;
    private int size;

    public Board(int size) {
        if (size < 4 || size % 2 != 0) {
            throw new IllegalArgumentException("El tamaño debe ser par y >= 4");
        }
        this.size = size;
        this.cells = new PieceColor[size][size];
        initializeBoard();
    }
    /**
     * Coloca las fichas iniciales en el centro del tablero.
     */
    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = PieceColor.EMPTY;
            }
        }

        int mid = size / 2;
        //patrón cruzado inicial (Regla 3)
        cells[mid - 1][mid - 1] = PieceColor.WHITE;
        cells[mid][mid]         = PieceColor.WHITE;
        cells[mid - 1][mid]     = PieceColor.BLACK;
        cells[mid][mid - 1]     = PieceColor.BLACK;
    }

    public PieceColor getCell(int row, int col) {
        return cells[row][col];
    }

    /**
     * Verifica si un movimiento es válido según las reglas del juego.
     * @param row Fila destino
     * @param col Columna destino
     * @param color Color del jugador actual
     * @return true si el movimiento encierra al menos una ficha oponente.
     */
    public boolean isValidMove(int row, int col, PieceColor color) {
        // Regla 5 & 6: Debe ser una celda vacía
        if (row < 0 || row >= size || col < 0 || col >= size || cells[row][col] != PieceColor.EMPTY) {
            return false;
        }

        //revisar las 8 direcciones
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;//saltar la celda actual

                if (canFlip(row, col, dr, dc, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Revisa si en una dirección específica se pueden capturar fichas.
     */
    private boolean canFlip(int row, int col, int dr, int dc, PieceColor color) {
        int r = row + dr;
        int c = col + dc;
        boolean hasOpponentBetween = false;

        PieceColor opponent = (color == PieceColor.BLACK) ? PieceColor.WHITE : PieceColor.BLACK;

        while (r >= 0 && r < size && c >= 0 && c < size) {
            if (cells[r][c] == opponent) {
                hasOpponentBetween = true;
            } else if (cells[r][c] == color) {
                return hasOpponentBetween; //se encontró una ficha propia después de las oponentes
            } else {
                break; //celda vacía, no se encierra nada
            }
            r += dr;//avanzar en la dirección
            c += dc;//avanzar en la dirección
        }
        return false;
    }

    /**
     * Coloca una ficha y voltea las del oponente en todas las direcciones válidas.
     */
    public void executeMove(int row, int col, PieceColor color) {
        //1.Colocar la ficha inicial
        cells[row][col] = color;

        //2.Voltear en las 8 direcciones
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                if (canFlip(row, col, dr, dc, color)) {
                    flipDirection(row, col, dr, dc, color);
                }
            }
        }
    }

    /**
     * Cambia el color de las fichas del oponente en una dirección específica.
     */
    private void flipDirection(int row, int col, int dr, int dc, PieceColor color) {
        int r = row + dr;
        int c = col + dc;
        PieceColor opponent = (color == PieceColor.BLACK) ? PieceColor.WHITE : PieceColor.BLACK;

        while (cells[r][c] == opponent) {
            cells[r][c] = color;
            r += dr;
            c += dc;
        }
    }
    /**
     * Verifica si un color específico tiene al menos una jugada válida en todo el tablero.
     */
    public boolean hasValidMoves(PieceColor color) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (isValidMove(i, j, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica si ya no quedan celdas vacías (Regla 10).
     */
    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j] == PieceColor.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Cuenta la cantidad de fichas de un color específico en el tablero.
     */
    public int getCount(PieceColor color) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j] == color) {
                    count++;
                }
            }
        }
        return count;
    }
}
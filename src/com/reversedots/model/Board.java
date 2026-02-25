package com.reversedots.model;

public class Board implements java.io.Serializable {
    private PieceColor[][] cells;
    private int size;
    public int getSize() { return size; }

    public Board(int size) {
        if (size < 4 || size % 2 != 0) {
            throw new IllegalArgumentException("El tamaño debe ser par y >= 4");
        }
        this.size = size;
        this.cells = new PieceColor[size][size];
        initializeBoard();
    }
 //coloca fichas iniciales en el centro del tablero (segun patron cruzado de la regla 3 del proyecto)
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

    //IsValidMove verifica si colocar una ficha en row,col es legal segun reglas de juego
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

 //canFlip verifica si en la dirección dada (dr,dc) se puede encerrar fichas del oponente entre la ficha que se colocará y otra ficha del mismo color
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

    //se ejecuta el movimiento colocando la ficha y volteando las fichas del rival en las direcciones posibles
    public void executeMove(int row, int col, PieceColor color) {
        //colocar la ficha inicial
        cells[row][col] = color;

        //voltear en las 8 direcciones
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                if (canFlip(row, col, dr, dc, color)) {
                    flipDirection(row, col, dr, dc, color);
                }
            }
        }
    }

  //cambia el color de las fichas del rival en la direccion dada, hasta que encuentre una ficha del mismo color o una celda vacía
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
 //verifica si algun color tiene movimiento validos disponibles, si no hay termina.
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

   //verificacion de si quedan celdas vacias, si no hay, fin.
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
    //lleva la cuenta de cuantas fiichas de cada color hay, da un ganador al final del juego (el que tenga mas)
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
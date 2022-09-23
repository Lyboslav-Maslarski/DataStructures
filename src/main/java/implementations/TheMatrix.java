package implementations;

import java.util.Arrays;

public class TheMatrix {
    private char[][] matrix;
    private char fillChar;
    private char toBeReplaced;
    private int startRow;
    private int startCol;

    public TheMatrix(char[][] matrix, char fillChar, int startRow, int startCol) {
        this.matrix = matrix;
        this.fillChar = fillChar;
        this.startRow = startRow;
        this.startCol = startCol;
        this.toBeReplaced = this.matrix[this.startRow][this.startCol];
    }

    public void solve() {
        fillMatrix(startRow, startCol);
    }

    private void fillMatrix(int row, int col) {
        if (this.matrix[row][col] == this.toBeReplaced) {
            this.matrix[row][col] = this.fillChar;
        }
        if (row + 1 < matrix.length && this.matrix[row + 1][col] == this.toBeReplaced) {
            fillMatrix(row + 1, col);
        }
        if (row - 1 >= 0 && this.matrix[row - 1][col] == this.toBeReplaced) {
            fillMatrix(row - 1, col);
        }
        if (col + 1 < matrix[row].length && this.matrix[row][col + 1] == this.toBeReplaced) {
            fillMatrix(row, col + 1);
        }
        if (col - 1 >= 0 && this.matrix[row][col - 1] == this.toBeReplaced) {
            fillMatrix(row, col - 1);
        }
    }

    public String toOutputString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (char[] chars : matrix) {
            stringBuilder.append(Arrays.toString(chars).replaceAll("[\\[\\],\\s]",""));
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString().trim();
    }
}

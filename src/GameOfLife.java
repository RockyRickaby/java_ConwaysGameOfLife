/**
 * The GameOfLife class contains all the logic necessary to simulate
 * Conway's Game of Life. The Game of Life grid can have any size (as
 * long as there's enough memory available to store it).
 */
public class GameOfLife {
    private byte[][] gameGrid;
    private int rows, cols;
    private int updated;

    /**
     * Creates a new GameOfLife.
     * @param rows the number of rows.
     * @param cols the number of columns.
     */
    public GameOfLife(int rows, int cols) {
        if (rows < 0) {
            rows = -rows;
        }
        if (cols < 0) {
            cols = -cols;
        }
        this.rows = rows;
        this.cols = cols;
        this.gameGrid = new byte[rows][cols];
        this.updated = -1;
    }

    /**
     * Changes the state of the given cell.
     * @param i the row.
     * @param j the column.
     * @return the new state.
     */
    public int toggle(int i, int j) {
        if (!validIndex(i, j)) {
            return -1;
        }
        byte cell = gameGrid[i][j];
        return gameGrid[i][j] = (byte) (cell == 1 ? 0 : 1);
    }

    /**
     * Returns the current state of the given cell.
     * @param i the row.
     * @param j the column.
     * @return the state of the cell.
     */
    public byte currentState(int i, int j) {
        if (!validIndex(i, j)) {
            return -1;
        }
        return gameGrid[i][j];
    }

    /**
     * Returns the number of rows in this GameOfLife.
     * @return
     */
    public int getM() { return this.rows; }

    /**
     * Returns the number of columns in this GameOfLife.
     * @return
     */
    public int getN() { return this.cols; }

    /**
     * Checks if the given index is valid.
     * @param i row.
     * @param j column.
     * @return {@code true} if it is.
     */
    private boolean validIndex(int i, int j) {
        return (i >= 0 && i < this.rows) && (j >= 0 && j < this.cols);
    }

    /**
     * Returns the number of live neighbors of the
     * given cell.
     * @param i the row.
     * @param j the column.
     * @return the number of live neighbors.
     */
    private int numberOfLiveNeighborsOf(int i, int j) {
        i--; j--;
        int count = 0;
        for (int rowI = i; rowI < i + 3; rowI++) {
            for (int colJ = j; colJ < j + 3; colJ++) {
                if (!validIndex(rowI, colJ) || (rowI == (i + 1) && colJ == (j + 1))) {
                    continue;
                }
                count += gameGrid[rowI][colJ];
            }
        }
        return count;
    }

    /**
     * Updates the game's state.
     */
    public void tick() {
        byte[][] nextGen = new byte[this.rows][this.cols];
        updated = 0;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                byte currCell = gameGrid[i][j];
                int count = numberOfLiveNeighborsOf(i, j);
                if (currCell == 1 && (count < 2 || count > 3)) {
                    nextGen[i][j] = 0;
                    updated = 1;
                } else if (currCell == 0 && count == 3) {
                    nextGen[i][j] = 1;
                    updated = 1;
                } else {
                    nextGen[i][j] = currCell;
                }
            }
        }
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                gameGrid[i][j] = nextGen[i][j];
            }
        }
    }

    /**
     * Returns a value correspondent to wether the 
     * game has updated or if it is currently static.
     * @return 1 if there was any change. 0 if not. -1 if
     * the game was never started.
     */
    public int updated() { return this.updated; }

    /**
     * Completely erases all live cells from this GameOfLife.
     */
    public void reset() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gameGrid[i][j] = 0;
            }
        }
    }

    /**
     * Prints the GameOfLife grid on the terminal. Useful for
     * checking if stuff is working properly.
     */
    public void print() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                System.out.print(gameGrid[i][j]);       
            }
            System.out.println();
        }
    }
}

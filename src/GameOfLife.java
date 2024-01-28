public class GameOfLife {
    private byte[][] gameGrid;
    private int rows, cols;

    public GameOfLife(int m, int n) {
        if (m < 0) {
            m = -m;
        }
        if (n < 0) {
            n = -n;
        }
        this.rows = m;
        this.cols = n;
        this.gameGrid = new byte[m][n];
    }

    public int toggle(int i, int j) {
        if (!validIndex(i, j)) {
            return -1;
        }
        byte cell = gameGrid[i][j];
        return gameGrid[i][j] = (byte) (cell == 1 ? 0 : 1);
    }

    public byte currentState(int i, int j) {
        if (!validIndex(i, j)) {
            return -1;
        }
        return gameGrid[i][j];
    }

    public int getM() { return this.rows; }
    public int getN() { return this.cols; }

    public boolean validIndex(int i, int j) {
        return (i >= 0 && i < this.rows) && (j >= 0 && j < this.cols);
    }

    private int numberOfLiveNeighborsOf(int i, int j) {
        i--; j--;
        int count = 0;
        for (int rowI = i; rowI < i +  3; rowI++) {
            for (int colJ = j; colJ < j + 3; colJ++) {
                if (!validIndex(rowI, colJ) || (rowI == (i + 1) && colJ == (j + 1))) {
                    continue;
                }
                count += gameGrid[rowI][colJ];
            }
        }
        return count;
    }

    public void tick() {
        byte[][] nextGen = new byte[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                byte currCell = gameGrid[i][j];
                int count = numberOfLiveNeighborsOf(i, j);
                if (currCell == 1 && (count < 2 || count > 3)) {
                    nextGen[i][j] = 0;
                } else if (currCell == 0 && count == 3) {
                    nextGen[i][j] = 1;
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

    public void print() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                System.out.print(gameGrid[i][j]);       
            }
            System.out.println();
        }
    }
}

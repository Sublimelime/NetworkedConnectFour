package clientpackage;

public class CFClientGame {

    private int[][] board;
    public static final int EMPTY = 0, RED = 1, BLACK = 2, DRAW = 3, RED_WINS = 4, BLACK_WINS = 5, PLAYING = 6;

    public CFClientGame() {

        board = new int[6][7];
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                board[r][c] = EMPTY;
            }
        }
    }

    public Object clone() {
        CFClientGame clone = new CFClientGame();
        for (int c = 0; c < 7; c++) {
            int r = 5;
            while (r >= 0 && getSpot(r, c) != EMPTY) {
                clone.dropPiece(c, getSpot(r, c));
                r--;
            }
        }

        return clone;
    }

    public boolean dropPiece(int c, int piece) {
        for (int r = 5; r >= 0; r--) {
            if (board[r][c] == EMPTY) {
                board[r][c] = piece;
                return true;
            }
        }

        return false;
    }

    public int status() {
        // horizontal
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c <= 3; c++) {

                if (board[r][c] == RED && board[r][c + 1] == RED
                        && board[r][c + 2] == RED && board[r][c + 3] == RED) {
                    return RED_WINS;
                } else if (board[r][c] == BLACK && board[r][c + 1] == BLACK
                        && board[r][c + 2] == BLACK && board[r][c + 3] == BLACK) {
                    return BLACK_WINS;
                }
            }
        }

        // veritical
        for (int r = 0; r <= 2; r++) {
            for (int c = 0; c < 7; c++) {
                if (board[r][c] == RED && board[r + 1][c] == RED
                        && board[r + 2][c] == RED && board[r + 3][c] == RED) {
                    return RED_WINS;
                } else if (board[r][c] == BLACK && board[r + 1][c] == BLACK
                        && board[r + 2][c] == BLACK && board[r + 3][c] == BLACK) {
                    return BLACK_WINS;
                }
            }
        }
        //
        //
        //
        //
        for (int r = 0; r <= 2; r++) {
            for (int c = 3; c < 7; c++) {

                if (board[r][c] == RED && board[r + 1][c - 1] == RED
                        && board[r + 2][c - 2] == RED && board[r + 3][c - 3] == RED) {
                    return RED_WINS;
                } else if (board[r][c] == BLACK && board[r + 1][c - 1] == BLACK
                        && board[r + 2][c - 2] == BLACK && board[r + 3][c - 3] == BLACK) {
                    return BLACK_WINS;
                }
            }
        }

        //
        //
        //
        //
        for (int r = 0; r <= 2; r++) {
            for (int c = 0; c <= 3; c++) {

                if (board[r][c] == RED && board[r + 1][c + 1] == RED
                        && board[r + 2][c + 2] == RED && board[r + 3][c + 3] == RED) {
                    return RED_WINS;
                } else if (board[r][c] == BLACK && board[r + 1][c + 1] == BLACK
                        && board[r + 2][c + 2] == BLACK && board[r + 3][c + 3] == BLACK) {
                    return BLACK_WINS;
                }
            }
        }

        // playing
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {

                if (board[r][c] == EMPTY) {
                    return PLAYING;
                }
            }
        }

        return DRAW;
    }

    public int getSpot(int r, int c) {
        if (c < 0 || c > 6 || r < 0 || r > 5) {
            return -1;
        } else {
            return board[r][c];
        }
    }
}

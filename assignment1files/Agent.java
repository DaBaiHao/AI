
import java.awt.Color;
import java.awt.Point;


/** The random gomoku player chooses random squares on the board (using a
 *  uniform distribution) until an unoccupied square is found, which is then
 *  returned as the player's move. It is assumed that the board is not full,
 *  otherwise chooseMove() will get stuck in an infinite loop.
 *	Author: Simon Dixon
 **/
class Agent extends GomokuPlayer {

    private int score_get;

    public Point bestMove;

    private int depth;

    // private final int[][] history;


    public Move chooseMove(Color[][] board, Color me) {


        while (true) {
            int row = (int) (Math.random() * 8);	// values are from 0 to 7
            int col = (int) (Math.random() * 8);
//            if (board[row][col] == null)			// is the square vacant?
//                return new Move(row, col);
            // check first move
            int our_color_num = 0;
            for(int i = 0;i<8;i++ ) {
                for (int j = 0; j < 8; j++) {
                    if(board[i][j] == me){
                        our_color_num++;
                    }
                }
            }
            // first move
            if(our_color_num == 0){
                int i = 3 + (int)(Math.random() * (4-3+1));// values are from 3 to 4
                int j = 3 + (int)(Math.random() * (4-3+1));// values are from 3 to 4
                return new Move(i, j);
            }




            // from here
            int list_my_value[][] = new int[8][8];
            int list_other_value[][] = new int[8][8];
            // initialization
            for(int i = 0;i<8;i++ ){
                for(int j = 0;j<8;j++ ){
                    list_my_value[i][j] = 0;
                    list_other_value[i][j] = 0;
                }
            }
            // search all board
            for(int i = 0;i<8;i++ ){
                for(int j = 0;j<8;j++ ){
                    if (board[row][col] == null){
                        list_my_value[i][j] = get_value(board,i,j,me);
                        list_other_value[i][j] = get_value_forOther(board,i,j,me);
                    }
                }
            }




            if (board[row][col] == null)			// is the square vacant?
                return new Move(row, col);
        }
    } // chooseMove()

    public static int is_Game_Over(Color[][] board, Color me){
        // 0 draw
        // 1 we win
        // 2 we loss
        int is_Game_Over = 0;
        Color color;
        for(int i = 0;i<8;i++ ){
            for(int j = 0;j<8;j++ ){
                if(board[i][j] != null){
                    color = board[i][j];
                    if(color == me){
                        int value = 1;
                        if(j + 1 < 8){//right
                            if(board[i ][j+1] == me){
                                value += search_right(board, i,j+1,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }
                        else if(i + 1 < 8){//down
                            if(board[i +1][j] == me){
                                value += search_down(board, i+1,j,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }
                        else if(j - 1 >= 0){//left
                            if(board[i ][j-1] == me){
                                value += search_left(board, i,j-1,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }
                        else if(i - 1 >= 0){//up
                            if(board[i-1 ][j] == me){
                                value += search_up(board, i -1,j,me);
                            }
                            if(value == 5){
                                return 1;
                            }

                        }
                        else if(j + 1 < 8 && i + 1 <8){//down right
                            if(board[i+1][j+1] == me){
                                value += search_down_right(board, i + 1,j + 1,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }
                        else if(j - 1 >= 0 && i + 1 <8){//down left
                            if(board[i+1][j-1] == me){
                                value += search_down_left(board, i + 1,j - 1,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }
                        else if(j - 1 >= 0 && i - 1 >= 0){//up left
                            if(board[i -1][j-1] == me){
                                value += search_up_left(board, i - 1,j - 1,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }
                        else if(j + 1 < 8 && i - 1 >= 0){//up right
                            if(board[i -1][j + 1] == me){
                                value += search_up_right(board, i - 1,j + 1,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }

                        // else others
                    }else {

                        int value = 1;
                        if(j + 1 < 8){//right
                            if(board[i ][j+1] != me && board[i ][j+1] != null){
                                value += search_right_forOther(board, i,j+1,me);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }
                        else if(i + 1 < 8){//down
                            if(board[i+1 ][j] != me && board[i+1 ][j] != null){
                                value += search_down_forOther(board, i+1,j,me);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }
                        else if(j - 1 >= 0){//left
                            if(board[i ][j-1] != me && board[i ][j-1] != null ){
                                value += search_left_forOthers(board, i,j-1,me);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }
                        else if(i - 1 >= 0){//up
                            if(board[i-1 ][j] != me && board[i-1 ][j] != null){
                                value += search_up_forOthers(board, i -1,j,me);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }
                        else if(j + 1 < 8 && i + 1 <8){//down right
                            if(board[i+1][i+1] != me && board[i+1][j+1] != null){
                                value += search_down_right_forOther(board, i + 1,j + 1,me);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }
                        else if(j - 1 >= 0 && i + 1 <8){//down left
                            if(board[i+1][j-1] != me && board[i+1][j+1] != null){
                                value += search_down_left_forOthers(board, i + 1,j - 1,me);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }

                        else if(j - 1 >= 0 && i - 1 >= 0){//up left
                            if(board[i -1][j-1] != me && board[i -1][j-1] != null ){
                                value += search_up_left_forOthers(board, i - 1,j - 1,me);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }

                        else if(j + 1 < 8 && i - 1 >= 0){//up right
                            if(board[i -1][j + 1] != me && board[i -1][j + 1] != null){
                                value += search_up_right_fotOthers(board, i - 1,j + 1,me);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }
                    }
                }
            }
        }

        return is_Game_Over;
    }

    public static Point alpha_beta_main(Color[][] board, int alpha, int beta, Color me, Point bestMove){



        return bestMove;
    }

    public static Point alpha_beta_search(Color[][] board, int row, int col, Color me, Point bestMove){



        return bestMove;
    }

    /**
     *
     * @param board
     * @param row
     * @param col
     * @param me
     * @return
     */
    public static int get_value(Color[][] board, int row, int col, Color me){
        int value = 0;
        if(col + 1 < 8){//right
            if(board[row ][col+1] == me){
                value += search_right(board, row,col+1,me);
            }
        }
        if(row + 1 < 8){//down
            if(board[row +1][col] == me){
                value += search_down(board, row+1,col,me);
            }
        }
        if(col - 1 >= 0){//left
            if(board[row ][col-1] == me){
                value += search_left(board, row,col-1,me);
            }
        }
        if(row - 1 >= 0){//up
            if(board[row-1 ][col] == me){
                value += search_up(board, row -1,col,me);
            }
        }
        if(col + 1 < 8 && row + 1 <8){//down right
            if(board[row+1][col+1] == me){
                value += search_down_right(board, row + 1,col + 1,me);
            }
        }
        if(col - 1 >= 0 && row + 1 <8){//down left
            if(board[row+1][col-1] == me){
                value += search_down_left(board, row + 1,col - 1,me);
            }
        }

        if(col - 1 >= 0 && row - 1 >= 0){//up left
            if(board[row -1][col-1] == me){
                value += search_up_left(board, row - 1,col - 1,me);
            }
        }

        if(col + 1 < 8 && row - 1 >= 0){//up right
            if(board[row -1][col + 1] == me){
                value += search_up_right(board, row - 1,col + 1,me);
            }
        }


        return value;
    }

    public static int search_up(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(row -1>=0){
            if(board[row-1 ][col] == me) {
                value += search_up(board, row - 1, col, me);
            }
        }
        return value;
    }

    public static int search_down(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(row + 1 < 8){
            if(board[row +1][col] == me) {
                value += search_down(board, row + 1, col, me);
            }
        }
        return value;
    }

    public static int search_right(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8){
            if(board[row ][col+1] == me) {
                value += search_right(board, row, col + 1, me);
            }
        }
        return value;
    }

    public static int search_left(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0){
            if(board[row ][col-1] == me) {
                value += search_left(board, row, col - 1, me);
            }
        }
        return value;
    }

    public static int search_down_right(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8 && row + 1 <8){
            if(board[row+1][col+1] == me) {
                value += search_down_right(board, row + 1, col + 1, me);
            }
        }
        return value;
    }

    public static int search_down_left(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0 && row + 1 <8){
            if(board[row+1][col-1] == me) {
                value += search_down_left(board, row + 1, col - 1, me);
            }
        }
        return value;
    }

    public static int search_up_left(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0 && row - 1 >= 0){
            if(board[row -1][col-1] == me) {
                value += search_up_left(board, row - 1, col - 1, me);
            }
        }
        return value;
    }

    public static int search_up_right(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8 && row - 1 >= 0){
            if(board[row -1][col + 1] == me) {
                value += search_up_right(board, row - 1, col + 1, me);
            }
        }
        return value;
    }


    //
    //
    //

    public static int get_value_forOther(Color[][] board, int row, int col, Color me){
        int value = 0;
        if(col + 1 < 8){//right
            if(board[row ][col+1] != me && board[row ][col+1] != null){
                value += search_right_forOther(board, row,col+1,me);
            }
        }
        if(row + 1 < 8){//down
            if(board[row+1 ][col] != me && board[row+1 ][col] != null){
                value += search_down_forOther(board, row+1,col,me);
            }
        }
        if(col - 1 >= 0){//left
            if(board[row ][col-1] != me && board[row ][col-1] != null ){
                value += search_left_forOthers(board, row,col-1,me);
            }
        }
        if(row - 1 >= 0){//up
            if(board[row-1 ][col] != me && board[row-1 ][col] != null){
                value += search_up_forOthers(board, row -1,col,me);
            }
        }
        if(col + 1 < 8 && row + 1 <8){//down right
            if(board[row+1][col+1] != me && board[row+1][col+1] != null){
                value += search_down_right_forOther(board, row + 1,col + 1,me);
            }
        }
        if(col - 1 >= 0 && row + 1 <8){//down left
            if(board[row+1][col-1] != me && board[row+1][col+1] != null){
                value += search_down_left_forOthers(board, row + 1,col - 1,me);
            }
        }

        if(col - 1 >= 0 && row - 1 >= 0){//up left
            if(board[row -1][col-1] != me && board[row -1][col-1] != null ){
                value += search_up_left_forOthers(board, row - 1,col - 1,me);
            }
        }

        if(col + 1 < 8 && row - 1 >= 0){//up right
            if(board[row -1][col + 1] != me && board[row -1][col + 1] != null){
                value += search_up_right_fotOthers(board, row - 1,col + 1,me);
            }
        }


        return value;
    }

    public static int search_up_forOthers(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(row -1>=0){
            if(board[row-1 ][col] != me && board[row-1 ][col] != null) {
                value += search_up_forOthers(board, row - 1, col, me);
            }
        }
        return value;
    }

    public static int search_down_forOther(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(row + 1 < 8){
            if(board[row+1 ][col] != me && board[row+1 ][col] != null){
                value += search_down_forOther(board, row + 1, col, me);
            }
        }
        return value;
    }

    public static int search_right_forOther(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8){
            if(board[row ][col+1] != me && board[row ][col+1] != null){
                value += search_right_forOther(board, row, col + 1, me);
            }
        }
        return value;
    }

    public static int search_left_forOthers(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0){
            if(board[row ][col-1] != me && board[row ][col-1] != null) {
                value += search_left_forOthers(board, row, col - 1, me);
            }
        }
        return value;
    }

    public static int search_down_right_forOther(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8 && row + 1 <8){
            if(board[row+1][col+1] != me && board[row+1][col+1] != null) {
                value += search_down_right_forOther(board, row + 1, col + 1, me);
            }
        }
        return value;
    }

    public static int search_down_left_forOthers(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0 && row + 1 <8){
            if(board[row+1][col-1] != me && board[row+1][col-1] != null) {
                value += search_down_left_forOthers(board, row + 1, col - 1, me);
            }
        }
        return value;
    }

    public static int search_up_left_forOthers(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0 && row - 1 >= 0){
            if(board[row -1][col-1] != me && board[row -1][col-1] != null) {
                value += search_up_left_forOthers(board, row - 1, col - 1, me);
            }
        }
        return value;
    }

    public static int search_up_right_fotOthers(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8 && row - 1 >= 0){
            if(board[row -1][col + 1] != me && board[row -1][col + 1] != null) {
                value += search_up_right_fotOthers(board, row - 1, col + 1, me);
            }
        }
        return value;
    }
} //

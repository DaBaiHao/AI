import java.awt.Color;

/** The random gomoku player chooses random squares on the board (using a
 *  uniform distribution) until an unoccupied square is found, which is then
 *  returned as the player's move. It is assumed that the board is not full,
 *  otherwise chooseMove() will get stuck in an infinite loop.
 *	Author: Simon Dixon
 **/
class Agent extends GomokuPlayer {

    public Move chooseMove(Color[][] board, Color me) {
        while (true) {
            int row = (int) (Math.random() * 8);	// values are from 0 to 7
            int col = (int) (Math.random() * 8);
            if (board[row][col] == null)			// is the square vacant?
                return new Move(row, col);


            // from here
            int list[][] = new int[8][8];
            // initialization
            for(int i = 0;i<8;i++ ){
                for(int j = 0;j<8;j++ ){
                    list[i][j] = 0;
                }
            }
            // search all board
            for(int i = 0;i<8;i++ ){
                for(int j = 0;j<8;j++ ){

                }
            }
        }
    } // chooseMove()

    public static int get_value(Color[][] board, int row, int col, Color me){
        int value = 0;
        if(row + 1 < 8){//right
            if(board[row + 1][col] == me){
                value += search_right(board,row,col,me);
            }
        }
        if(col + 1 < 8){//down
            if(board[row ][col+1] == me){
                value += search_down(board,row,col,me);
            }
        }
        if(row - 1 >= 0){//left
            if(board[row + 1][col] == me){
                value += search_left(board,row,col,me);
            }
        }
        if(col - 1 >= 0){//up
            if(board[row ][col+1] == me){
                value += search_up(board, row -1,col,me);
            }
        }
        if(col + 1 < 8 && row + 1 <8){//down right
            if(board[row+1][col+1] == me){
                value += search_down_right()right(board,row,col,me);
            }
        }
        if(col - 1 >= 0 && row + 1 <8){//down left
            if(board[row+1][col-1] == me){

            }
        }

        if(col - 1 >= 0 && row - 1 >= 0){//up left
            if(board[row -1][col-1] == me){

            }
        }

        if(col + 1 < 8 && row - 1 >= 0){//up right
            if(board[row -1][col + 1] == me){

            }
        }


        return 0;
    }

    public static int search_up(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(row -1>=0){
            value += search_up(board, row -1,col,me);
        }
        return value;
    }

    public static int search_down(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(row + 1 < 8){
            value += search_down(board, row+1,col,me);
        }
        return value;
    }

    public static int search_right(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8){
            value += search_right(board, row,col+1,me);
        }
        return value;
    }

    public static int search_left(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0){
            value += search_left(board, row,col-1,me);
        }
        return value;
    }

    public static int search_down_right(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8 && row + 1 <8){
            value += search_down_right(board, row + 1,col + 1,me);
        }
        return value;
    }

    public static int search_down_left(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0 && row + 1 <8){
            value += search_down_left(board, row + 1,col - 1,me);
        }
        return value;
    }

    public static int search_up_left(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col - 1 >= 0 && row - 1 >= 0){
            value += search_up_left(board, row - 1,col - 1,me);
        }
        return value;
    }

    public static int search_up_right(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8 && row - 1 >= 0){
            value += search_up_right(board, row - 1,col + 1,me);
        }
        return value;
    }

} //

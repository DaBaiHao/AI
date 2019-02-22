import java.awt.Color;


class Check extends GomokuPlayer {

    public Move chooseMove(Color[][] board, Color me) {
        while (true) {
            int row = (int) (Math.random() * 8);	// values are from 0 to 7
            int col = (int) (Math.random() * 8);
            if (board[row][col] == null)			// is the square vacant?
                board[row][col] = me;
                int win = is_Game_Over(board, me);
                System.out.println(win);
                board[row][col] = null;
                return new Move(row, col);
        }
    } // chooseMove()

    public static int is_Game_Over(Color[][] board, Color me){
        // 0 not over
        // 1 we win
        // 2 we loss
        // 3 draw
        //int is_Game_Over = 0;
        Color color = Color.black;

        //
        int empty_number = 0;
        for(int i = 0;i<8;i++ ){
            for(int j = 0;j<8;j++ ){
                if(board[i][j] != null){
                    color = board[i][j];
                    if(color == me){
                        // find a chese
                        int value = 1;
                        // right search first
                        if(j + 1 < 8){//right
                            if(board[i][j+1] == me){
                                value += search_right(board, i,j+1,me);
                            }
                             System.out.println("the is_Game_Over function calculate value is  "+value);
                            if(value == 5){
                                return 1;
                            }else {
                                value = 1;
                            }
                        }
                        if(i + 1 < 8){//down
                            if(board[i +1][j] == me){
                                value += search_down(board, i+1,j,me);
                            }
                             System.out.println("the is_Game_Over function calculate value is  "+value);

                            if(value == 5){
                                return 1;
                            }else {
                                value = 1;
                            }
                        }
                        if(j + 1 < 8 && i + 1 <8){//down right
                            if(board[i+1][j+1] == me){
                                value += search_down_right(board, i + 1,j + 1,me);
                            }
                            System.out.println("the is_Game_Over function calculate value is  "+value);

                            if(value == 5){
                                return 1;
                            }else {
                                value = 1;
                            }
                        }
                        if(j - 1 >= 0 && i + 1 <8){//down left
                            if(board[i+1][j-1] == me){
                                value += search_down_left(board, i + 1,j - 1,me);
                            }
                            System.out.println("the is_Game_Over function calculate value is  "+value);

                            if(value == 5){
                                return 1;
                            }else {
                                value = 1;
                            }
                        }

                        // else others
                    }else if(color != me && color != null){
                        Color other = Color.black;
                        if(me == Color.black){
                            other = Color.white;
                        }else {
                            other = Color.black;
                        }
                        int value = 1;
                        if(j + 1 < 8){//right
                            if(board[i ][j+1] == other){
                                value += search_right(board, i,j+1,other);
                            }
                            System.out.println("the is_Game_Over function calculate other value is  "+value);

                            if(value == 5){
                                return 2;
                            }else {
                                value = 1;
                            }
                        }
                        if(i + 1 < 8){//down
                            if(board[i+1 ][j] == other){
                                value += search_down(board, i+1,j,other);
                            }
                            System.out.println("the is_Game_Over function calculate other value is  "+value);

                            if(value == 5){
                                return 2;
                            }else {
                                value = 1;
                            }
                        }
                        if(j + 1 < 8 && i + 1 <8){//down right
                            if(board[i+1][i+1] == other){
                                value += search_down_right(board, i + 1,j + 1,other);
                            }
                             System.out.println("the is_Game_Over function calculate other value is  "+value);

                            if(value == 5){
                                return 2;
                            }else {
                                value = 1;
                            }
                        }
                        if(j - 1 >= 0 && i + 1 <8){//down left
                            if(board[i+1][j-1] == other){
                                value += search_down_left(board, i + 1,j - 1,other);
                            }
                            System.out.println("the is_Game_Over function calculate other value is  "+value);

                            if(value == 5){
                                return 2;
                            }else {
                                value = 1;
                            }
                        }

                    }
                }else {
                    empty_number++;
                }
            }
        }
        // draw
        if(empty_number <= 1){
            return 3;
        }else {
            // not over
            return 0;
        }


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

    public static int search_up(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(row -1>=0){
            if(board[row-1 ][col] == me) {
                value += search_up(board, row - 1, col, me);
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

    public static int search_down(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(row + 1 < 8){
            if(board[row +1][col] == me) {
                value += search_down(board, row + 1, col, me);
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



} // class RandomPlayer

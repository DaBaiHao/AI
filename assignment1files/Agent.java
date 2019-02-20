
import java.awt.*;
import java.util.ArrayList;


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
    public int MIN;
    public int MAX;
    public int total = 0;
    public int ABcut = 0;
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
                while (true) {
                    int i = 3 + (int) (Math.random() * (4 - 3 + 1));// values are from 3 to 4
                    int j = 3 + (int) (Math.random() * (4 - 3 + 1));// values are from 3 to 4
                    if (board[i][j] == null) {
                        return new Move(i, j);
                    }
                }
                //return new Move(i, j);
            }

            Point best = minmax(board,me,4,-100000,1000000);
            if( best.x <8 && best.y <8&&best.y >= 0&&best.x >= 0){
                return new Move(best.x, best.y);
            }

//
//            // from here
//            int list_my_value[][] = new int[8][8];
//            int list_other_value[][] = new int[8][8];
//            // initialization
//            for(int i = 0;i<8;i++ ){
//                for(int j = 0;j<8;j++ ){
//                    list_my_value[i][j] = 0;
//                    list_other_value[i][j] = 0;
//                }
//            }
//            // search all board
//            for(int i = 0;i<8;i++ ){
//                for(int j = 0;j<8;j++ ){
//                    if (board[row][col] == null){
//                        list_my_value[i][j] = get_value(board,i,j,me);
//                        list_other_value[i][j] = get_value_forOther(board,i,j,me);
//                    }
//                }
//            }
//
//
//
//
//            if (board[row][col] == null)			// is the square vacant?
//                return new Move(row, col);
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
                            if(value == 5){
                                return 1;
                            }
                        }
                        if(i + 1 < 8){//down
                            if(board[i +1][j] == me){
                                value += search_down(board, i+1,j,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }
                        if(j + 1 < 8 && i + 1 <8){//down right
                            if(board[i+1][j+1] == me){
                                value += search_down_right(board, i + 1,j + 1,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }
                        if(j - 1 >= 0 && i + 1 <8){//down left
                            if(board[i+1][j-1] == me){
                                value += search_down_left(board, i + 1,j - 1,me);
                            }
                            if(value == 5){
                                return 1;
                            }
                        }

                        // else others
                    }else {
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
                            if(value == 5){
                                return 2;
                            }
                        }
                        if(i + 1 < 8){//down
                            if(board[i+1 ][j] == other){
                                value += search_down(board, i+1,j,other);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }
                        if(j + 1 < 8 && i + 1 <8){//down right
                            if(board[i+1][i+1] == other){
                                value += search_down_right(board, i + 1,j + 1,other);
                            }
                            if(value == 5){
                                return 2;
                            }
                        }
                        if(j - 1 >= 0 && i + 1 <8){//down left
                            if(board[i+1][j-1] == other){
                                value += search_down_left(board, i + 1,j - 1,other);
                            }
                            if(value == 5){
                                return 2;
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


    public static int valuation_function(Color[][] board, Color me){
        int score_us=0;
        int score_other=0;
        int winner = is_Game_Over(board, me);
        if (winner ==1 ){
            score_us += 100000000;
        }else if(winner == 2){
            score_other += 100000000;
        }


        for(int i=0;i<8;i++){
            int sum_us=0;
            boolean is_me_side = false;
            int sum_other=0;
            boolean is_other_side = false;
            // heng
            for(int j=0;j<8;j++) {
                if (board[i][j] == me) {

                    sum_us += 1;
                    if (i == 0 || j == 0 || i == 7 || j == 7) {
                        is_me_side = true;
                    }
                } else if (board[i][j] != me && board[i][j] != null) {
                    sum_other += 1;
                    if (i == 0 || j == 0 || i == 7 || j == 7) {
                        is_other_side = true;
                    }
                } else {
                    score_us +=calculate_score(sum_us,is_me_side);
                    //other
                    score_other +=calculate_score(sum_other,is_other_side);

                    if(sum_us != 0){
                        score_us += 1;
                        sum_us = 0;
                        is_me_side = false;
                    }
                    if(sum_other != 0){
                        score_other += 1;
                        sum_other = 0;
                        is_other_side = false;
                    }

                }

                //us
                score_us +=calculate_score(sum_us,is_me_side);
                //other
                score_other +=calculate_score(sum_other,is_other_side);
            }
            sum_us=0;
            is_me_side = false;
            sum_other=0;
            is_other_side = false;
            // up to down, xie
            for(int j=0;i+j< 8;j++){
                if (board[i+j][j] == me) {

                    sum_us += 1;
                    if (i+j == 0 || j == 0 || i+j == 7 || j == 7) {
                        is_me_side = true;
                    }
                } else if (board[i+j][j] != me && board[i+j][j] != null) {
                    sum_other += 1;
                    if (i+j == 0 || j == 0 || i+j == 7 || j == 7) {
                        is_other_side = true;
                    }
                } else {
                    //us
                    score_us +=calculate_score(sum_us,is_me_side);
                    //other
                    score_other +=calculate_score(sum_other,is_other_side);
                    if(sum_us != 0){
                        score_us += 1;
                        sum_us = 0;
                        is_me_side = false;
                    }
                    if(sum_other != 0){
                        score_other += 1;
                        sum_other = 0;
                        is_other_side = false;
                    }

                }

                //us
                score_us +=calculate_score(sum_us,is_me_side);
                //other
                score_other +=calculate_score(sum_other,is_other_side);
            }

            sum_us=0;
            is_me_side = false;
            sum_other=0;
            is_other_side = false;
            //up to down
            for(int j=0;j< 8;j++){
                if (board[j][i] == me) {

                    sum_us += 1;
                    if (i == 0 || j == 0 || i == 7 || j == 7) {
                        is_me_side = true;
                    }
                } else if (board[j][i] != me && board[j][i] != null) {
                    sum_other += 1;
                    if (i == 0 || j == 0 || i == 7 || j == 7) {
                        is_other_side = true;
                    }
                } else {
                    //us
                    score_us +=calculate_score(sum_us,is_me_side);
                    //other
                    score_other +=calculate_score(sum_other,is_other_side);
                    if(sum_us != 0){
                        score_us += 1;
                        sum_us = 0;
                        is_me_side = false;
                    }
                    if(sum_other != 0){
                        score_other += 1;
                        sum_other = 0;
                        is_other_side = false;
                    }

                }

                //us
                score_us +=calculate_score(sum_us,is_me_side);
                //other
                score_other +=calculate_score(sum_other,is_other_side);
            }

            sum_us=0;
            is_me_side = false;
            sum_other=0;
            is_other_side = false;
            // up to down, xie
            for(int j=0;i-j>=0;j++){
                if (board[i-j][j] == me) {

                    sum_us += 1;
                    if (i-j == 0 || j == 0 || i-j == 7 || j == 7) {
                        is_me_side = true;
                    }
                } else if (board[i-j][j] != me && board[i-j][j] != null) {
                    sum_other += 1;
                    if (i-j == 0 || j == 0 || i-j == 7 || j == 7) {
                        is_other_side = true;
                    }
                } else {
                    //us
                    score_us +=calculate_score(sum_us,is_me_side);
                    //other
                    score_other +=calculate_score(sum_other,is_other_side);
                    if(sum_us != 0){
                        score_us += 1;
                        sum_us = 0;
                        is_me_side = false;
                    }
                    if(sum_other != 0){
                        score_other += 1;
                        sum_other = 0;
                        is_other_side = false;
                    }

                }

                //us
                score_us +=calculate_score(sum_us,is_me_side);
                //other
                score_other +=calculate_score(sum_other,is_other_side);
            }


        }




        int score = score_us - score_other;
        return score;
    }
    public static int calculate_score(int sum,boolean is_on_side){
            //us
            // 2
        int score = 0;
            if(sum == 2){
                if(is_on_side){
                    score += 10;
                }else {
                    score += 1000;
                }
                //3
            }else if(sum == 3){
                if(is_on_side){
                    score += 10000;
                }else {
                    score += 100000;
                }
                //4
            }else if(sum == 4){
                if(is_on_side){
                    score += 100000;
                }else {
                    score += 10000000;
                }
            }
            return score;
    }

    public static ArrayList<Point> generate(Color[][] board){
        ArrayList<Point> available = new ArrayList<Point>();
        for(int i = 0;i<8;i++ ){
            for(int j = 0;j<8;j++ ){
                if(board[i][j] == null){
                    Point point = new Point();
                    point.x = i;
                    point.y = j;
                    available.add(point);
                }
            }
        }

        return available;
    }
    public static Point minmax(Color[][] board, Color me,int deep,int alpha,int beta){
        int best = -10;// min
        ArrayList<Point> available = generate(board);
        ArrayList<Point> bestPoints = new ArrayList<Point>();

        for (int i = 0;i<available.size();i++){
            Point point = available.get(i);
            // try
            board[point.x][point.y] = me;
            int value = min(board, me,deep-1, alpha,beta);     //find max value
            // if find a good one same as before
            if(value == best) {
                bestPoints.add(point);
            }
 //           int otherScore_me = get_value(board,point.x,point.y,me);
  //          int otherScore_other = get_value_forOther(board,point.x,point.y,me);
  //          int another_value_function = otherScore_me*100+otherScore_other*100;
  //          value = value+another_value_function;
            // if find a good one
            if(value > best) {
                // clear all
                bestPoints.clear();
                bestPoints.add(point);
                best = value;
            }
            board[point.x][point.y] = null;

        }
        int random = (int)(Math.random()* bestPoints.size());
        Point best_Point = bestPoints.get(random);
        return best_Point;
    }

    public static int min(Color[][] board, Color me, int deep, int alpha, int beta){
        int this_time_value = valuation_function(board,me);
        int is_game_over = is_Game_Over(board,me);

        int worse_case = 1000000000;
        if(deep == 0|| is_game_over == 1||is_game_over == 2||is_game_over == 3){
            return this_time_value;
        }

        // find other color
        Color other = Color.black;;
        if(me == Color.white) {
            other = Color.black;
        }else {
            other  = Color.white;
        }

        // find the available point
        ArrayList<Point> available = generate(board);
        for(int i=0;i<available.size();i++) {
            Point point = available.get(i);
            board[point.x][point.y] = other;
            // max
            this_time_value = max(board, me,deep-1,alpha,beta);
            board[point.x][point.y] = null;

            if(this_time_value < worse_case){
                worse_case = this_time_value;
                beta = worse_case;
            }
            if(this_time_value < alpha){

                break;
            }
        }

        return worse_case;


    }


    public static int max(Color[][] board, Color me, int deep, int alpha, int beta){
        int this_time_value = valuation_function(board,me);
        int is_game_over = is_Game_Over(board,me);
        int worse_case = -1000000000;
        if(deep == 0|| is_game_over == 1||is_game_over==2||is_game_over==3){
            return this_time_value;
        }

        // find the available point
        ArrayList<Point> available = generate(board);
        for(int i=0;i<available.size();i++) {
            Point point = available.get(i);
            // try
            board[point.x][point.y] = me;

            this_time_value = min(board,me,deep-1,alpha,beta);
            board[point.x][point.y] = null;

            // if value is higher than baddest case, then the worse case is that value
            if(this_time_value > worse_case){
                worse_case = this_time_value;
                alpha = worse_case;
            }

            if(worse_case > beta){
                break;
            }

        }
        // return the maxmum value
        return worse_case;
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


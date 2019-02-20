import java.awt.*;
import java.util.ArrayList;

public class Player180223545 extends GomokuPlayer {


    /**
     * The main function to choose move
     * @param board
     * @param me
     * @return
     */
    public Move chooseMove(Color[][] board, Color me) {

        while (true) {
            Point best = alpha_beta_min_max(board, me);
            return new Move(best.x, best.y);

        }

    }


    /**
     * generate an available point list
     * @param board
     * @return
     */
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

    /**
     *
     * @param board
     * @param me
     * @return
     */
    public static Point alpha_beta_min_max(Color[][] board, Color me){

        //init a best value
        int best_value = 0;
        // find all can played chess
        ArrayList<Point> available = generate(board);
        ArrayList<Point> bestPoints = new ArrayList<Point>();

        int alpha = -10000000; // the worst case for maxmizer
        int beta  = 100000000; // the worst case for minimizer
        for (int i = 0;i<available.size();i++){
            Point point = available.get(i);
            // try
            board[point.x][point.y] = me;
            int this_time_value = min(board,me,4,alpha,beta); // get max

            /**
             *
             */
            if(this_time_value == best_value){
                bestPoints.add(point);
            }else if(this_time_value > best_value){
                // remove all unit in the list
                bestPoints.clear();
                // add the best one
                bestPoints.add(point);
                // the value change
                best_value = this_time_value;
            }// otherwise don't think about

            // make the board back to previous
            board[point.x][point.y] = null;
        }

        int random = (int)(Math.random()* bestPoints.size());
        Point best_Point = bestPoints.get(random);
        return best_Point;
        /**
         * here can calculate each point value and then return the best point
         * greedy
         */
//        int best_point_value = 0;
//        ArrayList<Point> bestPoints_final = new ArrayList<Point>();
//        for (int i = 0; i< bestPoints.size();i++){
//            // each point in best point
//            Point point = bestPoints.get(i);
//            // calculate each point value
//            int this_point_value = get_point_value(board, point); // function need implement
//            // if the point value same
//            if(this_point_value == best_point_value){
//                bestPoints_final.add(point);
//            }
//            //if this time value higher best
//            if(this_point_value > best_point_value){
//                // clear list
//                bestPoints_final.clear();
//                // add point
//                bestPoints_final.add(point);
//                // best is this
//                best_point_value = this_point_value;
//            }
//        }
//        int random_final = (int)(Math.random()* bestPoints_final.size());
//
//        Point best_Point = bestPoints.get(random_final);
//        return best_Point;
    }


    /**
     *
     * @param board
     * @param me
     * @param deep
     * @param alpha
     * @param beta
     * @return
     */
    public static int min(Color[][] board, Color me, int deep, int alpha, int beta){
        int this_time_value = value_function(board,me);
        int is_game_over = is_Game_Over(board,me);

        int worse_case = 1000000000;
        if(deep == 0|| is_game_over == 1|| is_game_over == 2|| is_game_over == 3){
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

    /**
     *
     * @param board
     * @param me
     * @param deep
     * @param alpha
     * @param beta
     * @return
     */
    public static int max(Color[][] board, Color me, int deep, int alpha, int beta){
        int this_time_value = value_function(board, me);
        int is_game_over = is_Game_Over(board,me);
        int worse_case = -1000000000;
        if(deep == 0|| is_game_over == 1|| is_game_over == 2|| is_game_over == 3){
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
     * value function
     *              return  100000 is our   win
     *              return -100000 is other win
     *              return       0 is       draw
     * return
     * @param board
     * @param me
     * @return
     */
    public static int value_function(Color[][] board, Color me){


        //
        int winner = is_Game_Over(board, me);
        if(winner == 1 ){
            // winner 1 is us
            return  100000;
        }else if(winner == 2) {
            // winner 2 is other
            return -100000;
        }else if(winner == 3) {
            // winner 3 is draw
            return 0;
        }else {
            // winner 0 is not over
            int score_us = 0;

            // search all board
            for(int i=0;i<8;i++){

                // check side
                int is_me_side = 0;

                // num of connect chese
                int us_num_connect = 0;

                for(int j=0;j<8;j++) {

                    // find our chess
                    if (board[i][j] == me) {
                        us_num_connect++;
                        // check if on side
                        if(j == 0 ||j == 7){
                            is_me_side ++;
                        }
                    }

                    // check if others chess
                    if (board[i][j] != me && board[i][j] != null ) {
                        // check if on side
                        if(us_num_connect != 0){
                            is_me_side ++;
                        }
                    }

                    // if no chess check if is stopped or the last one of the col
                    if(board[i][j] == null || j == 7||((board[i][j] != me &&board[i][j] != null))){
                        // calculate score
                        score_us += calculate_score(us_num_connect,is_me_side);
                        is_me_side = 0;
                        us_num_connect = 0;
                    }


                }

                // check side
                is_me_side = 0;

                // num of connect chese
                us_num_connect = 0;
                for(int j=0;j< 8;j++){

                    // if is ours
                    if (board[j][i] == me) {
                        us_num_connect++;
                        // check if on side
                        // because j i 00 70 is side
                        if(j == 0 ||j == 7){
                            is_me_side ++;
                        }
                    }


                    // check if others chess
                    if (board[j][i] != me && board[j][i] != null ) {
                        // check if on side
                        if(us_num_connect != 0){
                            is_me_side ++;
                        }
                    }

                    // if no chess check if is stopped or the last one of the col
                    if(board[j][i] == null || j == 7 ||((board[j][i] != me && board[j][i] != null))){
                        // calculate score
                        score_us += calculate_score(us_num_connect,is_me_side);
                        is_me_side = 0;
                        us_num_connect = 0;
                    }

                }

                // check side
                is_me_side = 0;

                // num of connect chese
                us_num_connect = 0;
                /**
                 * attention half ---left up----to----right down---
                 */
                // attention half
                for(int j=0;i+j< 8;j++){
                    // find our chess
                    if (board[i+j][j] == me) {
                        us_num_connect++;
                        // check if on side
                        if(j == 0 ||j+i == 7 ){
                            is_me_side ++;
                        }
                    }

                    // check if others chess
                    if (board[i+j][j] != me && board[i+j][j] != null ) {
                        // check if on side
                        if(us_num_connect != 0){
                            is_me_side ++;
                        }
                    }

                    // if no chess check if is stopped or the last one of the col
                    if(board[i+j][j] == null || i+j == 7||((board[j+i][j] != me &&board[i+j][j] != null))){
                        // calculate score
                        score_us += calculate_score(us_num_connect,is_me_side);
                        is_me_side = 0;
                        us_num_connect = 0;
                    }

                }

                // check side
                is_me_side = 0;

                // num of connect chese
                us_num_connect = 0;
                /**
                 * attention another half ---left up----to----right down---
                 */
                // attention half
                for(int j=0;j< 8;j++){
                    // find our chess
                    if (board[j][i+j] == me) {
                        us_num_connect++;
                        // check if on side
                        if(j == 0 ||j+i == 7 ){
                            is_me_side ++;
                        }
                    }

                    // check if others chess
                    if (board[j][j+i] != me && board[j][j+i] != null ) {
                        // check if on side
                        if(us_num_connect != 0){
                            is_me_side ++;
                        }
                    }


                    // if no chess check if is stopped or the last one of the col
                    if(board[j][i+j] == null || i+j == 7||((board[j][i+j] != me &&board[j][i+j] != null))){
                        // calculate score
                        score_us += calculate_score(us_num_connect,is_me_side);
                        is_me_side = 0;
                        us_num_connect = 0;
                    }
                }

                // check side
                is_me_side = 0;

                // num of connect chese
                us_num_connect = 0;
                /**
                 * attention another half ---left up----to----right down---
                 */
                // attention half
                for(int j=0;i-j>=0;j++){
                    // find our chess
                    if (board[j][i-j] == me) {
                        us_num_connect++;
                        // check if on side
                        if(j == 0 ||i-j == 0 ){
                            is_me_side ++;
                        }
                    }

                    // check if others chess
                    if (board[j][i-j] != me && board[j][i-j] != null ) {
                        // check if on side
                        if(us_num_connect != 0){
                            is_me_side ++;
                        }
                    }

                    // if no chess check if is stopped or the last one of the col
                    if(board[j][i-j] == null || i-j == 7||((board[j][i-j] != me &&board[j][i-j] != null))){
                        // calculate score
                        score_us += calculate_score(us_num_connect,is_me_side);
                        is_me_side = 0;
                        us_num_connect = 0;
                    }

                }

                // check side
                is_me_side = 0;

                // num of connect chese
                us_num_connect = 0;
                /**
                 * attention another half ---left up----to----right down---
                 */
                // attention half
                for(int j=0;i+j<8;j++){

                    // find our chess
                    if (board[i+j][7-j] == me) {
                        us_num_connect++;
                        // check if on side
                        if(j == 0 ||i+j == 7 ){
                            is_me_side ++;
                        }
                    }
                    // check if others chess
                    if (board[i+j][7-j] != me && board[i+j][7-j] != null ) {
                        // check if on side
                        if(us_num_connect != 0){
                            is_me_side ++;
                        }
                    }
                    // if no chess check if is stopped or the last one of the col
                    if(board[i+j][7-j] == null || i+j == 7||((board[i+j][7-j] != me &&board[i+j][7-j] != null))){
                        // calculate score
                        score_us += calculate_score(us_num_connect,is_me_side);
                        is_me_side = 0;
                        us_num_connect = 0;
                    }

                }

            }
            Color other = Color.black;
            if(me == Color.black){
                other = Color.white;
            }else {
                other = Color.black;
            }
            int other_score = value_function(board, other);
            int return_value = score_us - other_score;
            return score_us;


        }



    }

    /**
     *
     * @param us_num_connect
     * @param is_me_side
     * @return
     */
    public static int calculate_score(int us_num_connect, int is_me_side){
        // if not connected
        int score_us = 0;
        if(us_num_connect != 0){
            // first check the side
            if(is_me_side == 1){
                /**
                 * side 1
                 * 1 = 1
                 * 2 = 10
                 * 3 = 100
                 * 4 = 1000
                 * 5 = 100000
                 */
                if(us_num_connect == 1){
                    score_us +=1;
                }else if(us_num_connect == 2){
                    score_us +=10;
                }else if(us_num_connect == 3){
                    score_us +=100;
                }else if(us_num_connect == 4){
                    score_us +=1000;
                }else if(us_num_connect == 5){
                    score_us +=100000;
                }

            }else if(is_me_side == 0){// not on side
                /**
                 * not side
                 * 1 = 10
                 * 2 = 100
                 * 3 = 1000
                 * 4 = 10000
                 * 5 = 1000000
                 */
                if(us_num_connect == 1){
                    score_us +=10;
                }else if(us_num_connect == 2){
                    score_us +=100;
                }else if(us_num_connect == 3){
                    score_us +=1000;
                }else if(us_num_connect == 4){
                    score_us +=10000;
                }else if(us_num_connect == 5){
                    score_us +=1000000;
                }

            }else {
                /**
                 * double side
                 * 0 score get
                 */
                score_us +=0;

            }
        }
        return score_us;
    }


    /**
     *
     * @param board
     * @param me
     * @return
     */
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

    public static int search_right(Color[][] board,int row, int col, Color me){
        int value = 1;
        if(col + 1 < 8){
            if(board[row ][col+1] == me) {
                value += search_right(board, row, col + 1, me);
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
}

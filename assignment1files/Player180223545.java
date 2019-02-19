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
        			// is the square vacant?
        board[1][1] = me;
        ArrayList<Point> available = generate(board);
        System.out.println(available.size());
        board[1][1] = null;
        available = generate(board);
        System.out.println(available.size());


        return new Move(4, 3);

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
            int this_time_value = min();

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

        /**
         * here can calculate each point value and then return the best point
         * greedy
         */
        int best_point_value = 0;
        ArrayList<Point> bestPoints_final = new ArrayList<Point>();
        for (int i = 0; i< bestPoints.size();i++){
            // each point in best point
            Point point = bestPoints.get(i);
            // calculate each point value
            int this_point_value = get_point_value(board, point); // function need implement
            // if the point value same
            if(this_point_value == best_point_value){
                bestPoints_final.add(point);
            }
            //if this time value higher best
            if(this_point_value > best_point_value){
                // clear list
                bestPoints_final.clear();
                // add point
                bestPoints_final.add(point);
                // best is this
                best_point_value = this_point_value;
            }
        }
        int random_final = (int)(Math.random()* bestPoints_final.size());

        Point best_Point = bestPoints.get(random_final);
        return best_Point;
    }



}

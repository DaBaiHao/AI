
import java.awt.Color;
import java.util.Arrays;
import java.util.Stack;
//import java.util.*

public class xiongLi extends GomokuPlayer{
    public Move chooseMove(Color[][] board, Color me) {
        // get both side's colour
        Color enemy;
        if(me == Color.black){
            enemy = Color.white;
        }
        else { enemy = Color.black;}


        int[][] best_result = new int[1][2];

        while (true) {

            best_result = min_max(board, 4, me, enemy);


//            int row = (int) (Math.random() * 8);	// values are from 0 to 7
//            int col = (int) (Math.random() * 8);
            int row = best_result[0][0];
            int col = best_result[0][1];
            System.out.println("i = " + row + "j = " +col);
            if (board[row][col] == null){
                return new Move(row, col);
            }      // is the square vacant?


        }
    }


    //--------------------------------------debugged line---------------------------------------//
    public static int[][] min_max(Color[][] board, int deep, Color allay, Color enemy){

        // initialize the alpha and beta
        int best =  -999999999;
        int alpha = -999999999;
        int beta =   999999999;
        int current_board_score = 0;
        int[][] best_result = new int[1][2];
        Color[][] imaginary_board = board;  // take board into this
        int[][] down_points = new int[150][2]; // generate the points where you can place the pieces
        for (int i = 0; i < 150; i++){
            for (int j = 0; j <2; j++){
                down_points[i][j] = -1;
            }
        }
        down_points = gen_point_down(board, allay);


        for (int i = 0; i < 150; i++) {

//            System.out.println(Arrays.toString(down_points[i]));
            if (down_points[i][0] == -1) {
                break;
            }

            if (down_points[0][0] == 100){ // if the 0th move, give 4,4
                best_result[0][0] = 4;
                best_result[0][1] = 4;
                break;
            }

            if (down_points[i][0] == 100) {
                continue;
            }

            imaginary_board[down_points[i][0]][down_points[i][1]] = allay;

            current_board_score = choose_min(board, deep, alpha, beta, enemy, allay);

            if (current_board_score > best){
                best_result[0][0] = down_points[i][0];
                best_result[0][1] = down_points[i][1];
                best = current_board_score;
            }
            imaginary_board[down_points[i][0]][down_points[i][1]] = null;
        }
        return best_result;
    }


    public static int choose_min(Color[][] board, int deep, int alpha, int beta, Color allay, Color enemy){
        // min in minimax

        int best =  999999999;
        int current_board_score_opp = 0;

        int current_board_score = 0;
        Color[][] imaginary_board = board;  // take board into this
        int[][] down_points = new int[150][2];
        for (int i = 0; i < 150; i++){
            for (int j = 0; j <2; j++){
                down_points[i][j] = -1;
            }
        }

        // calculate the score for the board (allay - opposite)
        current_board_score = flat_evaluate_board(board, enemy, allay);
        current_board_score_opp = flat_evaluate_board(board, allay, enemy);
        current_board_score = (current_board_score - current_board_score_opp);
        down_points = gen_point_down(board, allay);

        if (deep <= 0){
            return current_board_score;
        }


        for (int i = 0; i < 150; i++){
            if (down_points[i][0] == -1){
                break;
            }
            if (down_points[i][0] == 100){
                continue;
            }
            imaginary_board[down_points[i][0]][down_points[i][1]] = allay;
            current_board_score = choose_max(imaginary_board,
                    deep-1, best < alpha ? best:alpha,
                    beta, enemy, allay);

            imaginary_board[down_points[i][0]][down_points[i][1]] = null;


            if (current_board_score < best){
                best = current_board_score;
            }

            if (current_board_score < beta){
                break;
            }

        }

        return best;
    }

    public static int choose_max(Color[][] board, int deep, int alpha, int beta, Color allay, Color enemy){
        // max in minimax

        int best =  -999999999;
        int current_board_score = 0;
        int current_board_score_opp = 0;
        int[][] down_points = new int[150][2];         // init down points to all -1 (for judge null or full)
        for (int i = 0; i < 150; i++){
            for (int j = 0; j <2; j++){
                down_points[i][j] = -1;
            }
        }
        Color[][] imaginary_board = board;  // take board into this

        current_board_score = flat_evaluate_board(board, allay, enemy);
        current_board_score_opp = flat_evaluate_board(board, enemy, allay);
        current_board_score = (current_board_score - current_board_score_opp);


        down_points = gen_point_down(board, allay);

        if (deep <= 0){
            return current_board_score;
        }

        for (int i = 0; i < 150; i++){

            if (down_points[i][0] == -1){
                break;
            }
            if (down_points[i][0] == 100){
                continue;
            }

            imaginary_board[down_points[i][0]][down_points[i][1]] = allay;
            current_board_score = choose_min(imaginary_board,
                    deep-1, alpha, best > beta ? best:alpha,
                    enemy, allay);

            imaginary_board[down_points[i][0]][down_points[i][1]] = null;

            if (current_board_score > best){
                best = current_board_score;
            }

            if (current_board_score > alpha){
                break;
            }
        }

        return best;
    }

    public static int[][] gen_point_down(Color[][] board, Color allay){
//        ''' gengerate where you can drop your piece

        int[] place = new int[2];
        int[][] empty_neighbour_arr = new int[150][2];  // search for the neighbour (maxium gap = 2)
        for (int i = 0; i < 150; i++){
            for (int j = 0; j <2; j++){
                empty_neighbour_arr[i][j] = -1;
            }
        }
        Stack<Integer> empty_neighbour = new Stack<Integer>();
        Stack<Integer> empty_2_neighbour = new Stack<Integer>();


        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                place[0] = i;
                place[1] = j;
                if (board[i][j] == null){
                    if (neighbour_search(board, place, allay)){

                        empty_neighbour.push(i);
                        empty_neighbour.push(j);

                    }
                    if (neighbour_2_search(board, place, allay)){
                        empty_2_neighbour.push(i);
                        empty_2_neighbour.push(j);
                    }
                }
            }
        }

//         empty the stack to a array, dist 1 neighbour and dist 2 neighbour are seperated by a '[100,100]'
        int i = 0;


        while (empty_neighbour.isEmpty() == false){
            for (int j = 1; j >= 0; j--){
                empty_neighbour_arr[i][j] = empty_neighbour.pop();
            }
            i++;
        }

        for (int j = 1; j >= 0; j--){
            empty_neighbour_arr[i][j] = 100;
        }

        while (empty_2_neighbour.isEmpty() == false){
            i++;
            for (int j = 1; j >= 0; j--){
                empty_neighbour_arr[i][j] = empty_2_neighbour.pop();
            }
        }
        return empty_neighbour_arr;
    }



    public static boolean neighbour_search(Color[][] board, int[] place, Color allay) {
//        '''judge the distance 1 neighbour of a piece.
//        '''this method is a little bit faster

        int i = place[0];
        int j = place[1];

        if (i > 0 && j > 0 && i < 7 && j < 7) {
            for (int a = i - 1; a <= i + 1; a++) {
                for (int b = j - 1; b <= j + 1; b++) {
//                    if (board[a][b] == allay)
                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        if (i == 0 && j > 0 && j < 7) {
            for (int a = i; a <= i + 1; a++) {
                for (int b = j - 1; b <= j + 1; b++) {
//                    if (board[a][b] == allay)

                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        if (i == 7 && j > 0 && j < 7) {
            for (int a = i -1 ; a <= i; a++) {
                for (int b = j - 1; b <= j + 1; b++) {
//                    if (board[a][b] == allay)
                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        if (j == 0 && i > 0 && i < 7) {
            for (int a = i - 1; a <= i + 1; a++) {
                for (int b = j; b <= j + 1; b++) {
//                    if (board[a][b] == allay)
                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        if (j == 7 && i > 0 && i < 7) {
            for (int a = i - 1; a <= i + 1; a++) {
                for (int b = j - 1; b <= j; b++) {
//                    if (board[a][b] == allay)
                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        if (i == 0 && j == 0){
            for (int a = i; a <= i + 1; a++) {
                for (int b = j; b <= j + 1; b++) {
//                    if (board[a][b] == allay)
                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        if (i == 0 && j == 7){
            for (int a = i; a <= i + 1; a++) {
                for (int b = j - 1; b <= j; b++) {
//                    if (board[a][b] == allay)
                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        if (i == 7 && j == 0){
            for (int a = i - 1; a <= i; a++) {
                for (int b = j; b <= j + 1; b++) {
//                    if (board[a][b] == allay)
                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        if (i == 7 && j == 7){
            for (int a = i - 1; a <= i; a++) {
                for (int b = j - 1; b <= j; b++) {
//                    if (board[a][b] == allay)
                    if (board[a][b] != null)
                        return true;
                }
            }
        }

        return false;
    }

    public static boolean neighbour_2_search(Color[][] board, int[] place, Color allay) {
//        ''' this functions gives a judgement of whether a piece has a distance 2 neighbour.
//        ''' use this to evaluate the distance of neighbour.

        int i = place[0];
        int j = place[1];
        double dist = 0.0;

        for (int a = 0; a < board.length; a++){
            for (int b = 0; b < board.length; b++) {
                if (board[a][b] != null){
                    dist = Math.sqrt(Math.pow( ((double)a - (double)i), 2) + Math.pow( ((double)b - (double)j), 2));
                    if (dist>1.5 && dist<2.9){
                        return true;
                    }
                }
            }
        }
        return false;
    }



    public static int flat_evaluate_board(Color[][] board, Color allay, Color enemy){ //working
        // score to the board
        int ONE = 10;
        int TWO = 100;
        int THREE = 1000;
        int FOUR = 100000;
        int FIVE = 10000000;
        int ONE_B = 1;
        int TWO_B = 10;
        int THREE_B = 100;
        int FOUR_B = 50000;

        int fiver = 0;
        int score_cache = 0;
        int score_out = 0;
        int open_end = 0;
        int n = 7;
        int m = 7;

//        //'-' search
        for(int i=0; i<8; i++){
            for (int j=1; j<8; j++){
                if(board[i][j] == allay){
                    fiver += 1;
                    if (board[i][j-1] == null){
                        open_end = 1;
                    }
                    if (board[i][j-1] == enemy || j == 7){
                        open_end = 0;
                    }
                    if (board[i][j-1] == allay && j == 1){
                        fiver += 1;
                        open_end = 0;
                    }
                }
                if (board[i][j] != allay || j == 7) {
                    if (board[i][j-1] == allay && j == 1){
                        fiver += 1;
                    }
                    if (board[i][j] == null){
                        open_end += 1;
                    }
                    if (board[i][j] == enemy){
                        open_end += 0;
                    }

                    if(open_end == 2){
                        switch (fiver){
                            case 1: score_cache = ONE; break;
                            case 2: score_cache = TWO; break;
                            case 3: score_cache = THREE; break;
                            case 4: score_cache = FOUR; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }
                    else{
                        switch (fiver){
                            case 1: score_cache = ONE_B; break;
                            case 2: score_cache = TWO_B; break;
                            case 3: score_cache = THREE_B; break;
                            case 4: score_cache = FOUR_B; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }

                    if(score_cache != 0){
                        score_out = score_out + score_cache;
                        score_cache = 0;
                    }
                }
            }
        }
//        // '|' search
        for(int i=0; i<8; i++){
            for (int j=1; j<8; j++){
                if(board[j][i] == allay){
                    fiver += 1;
                    if (board[j - 1][i] == null && j < 7){
                        open_end = 1;
                    }
                    if (board[j - 1][i] == enemy || j == 7){
                        open_end = 0;
                    }
                    if (board[j - 1][i] == allay && j == 1){
                        fiver += 1;
                        open_end = 0;
                    }
                }
                if (board[j][i] != allay || j == 7) {
                    if (board[j - 1][i] == allay && j == 1){
                        fiver = 1;
                    }
                    if (board[j][i] == null){
                        open_end += 1;
                    }
                    if (board[j][i] == enemy){
                        open_end += 0;
                    }

                    if(open_end == 2){
                        switch (fiver){
                            case 1: score_cache = ONE; break;
                            case 2: score_cache = TWO; break;
                            case 3: score_cache = THREE; break;
                            case 4: score_cache = FOUR; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }
                    else{
                        switch (fiver){
                            case 1: score_cache = ONE_B; break;
                            case 2: score_cache = TWO_B; break;
                            case 3: score_cache = THREE_B; break;
                            case 4: score_cache = FOUR_B; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }
                    if(score_cache != 0){
                        score_out = score_out + score_cache;
                        score_cache = 0;
                    }
                }
            }
        }

//         '\' search
        for(int i = 0; i <= m+n-2; i++)
        {
            for(int x=0;  x <= i && (x<n) && i-x < m  ;x++) {

                if(board[x + 1][m + x - i] == allay){
                    fiver += 1;
                    if (board[x][m + x - i - 1] == null){
                        open_end = 1;
                    }
                    if (board[x][m + x - i - 1] == enemy || m + x - i == 7){
                        open_end = 0;
                    }
                    if (board[x][m + x - i - 1] == allay && x + 1 == 1){
                        fiver += 1;
                        open_end = 0;
                    }
                }
                if (board[x + 1][m + x - i] != allay || m + x - i == 7){
                    if (board[x][m + x - i - 1] == allay && x + 1 == 1){
                        fiver = 1;
                    }
                    if (board[x + 1][m + x - i] == null){
                        open_end += 1;
                    }
                    if (board[x + 1][m + x - i] == enemy){
                        open_end += 0;
                    }


                    if(open_end == 2){
                        switch (fiver){
                            case 1: score_cache = ONE; break;
                            case 2: score_cache = TWO; break;
                            case 3: score_cache = THREE; break;
                            case 4: score_cache = FOUR; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }
                    else{
                        switch (fiver){
                            case 1: score_cache = ONE_B; break;
                            case 2: score_cache = TWO_B; break;
                            case 3: score_cache = THREE_B; break;
                            case 4: score_cache = FOUR_B; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }

                    if(score_cache != 0){
                        score_out = score_out + score_cache;
                        score_cache = 0;
                    }
                }
            }

            for(int y = m - 1;  y >= 0 && i - y >= 0 && i - y < n && i - y > 0; y--) {
//                System.out.print( (i - y + 1) + " " + (m - y));
//                System.out.print("  ");

                if(board[i - y + 1][m - y] == allay){
                    fiver += 1;
                    if (board[i - y][m - y - 1] == null){
                        open_end = 1;
                    }
                    if (board[i - y][m - y - 1] == enemy || i - y + 1 == 7){
                        open_end = 0;
                    }
                    if (board[i - y][m - y - 1] == allay && m - y == 1){
                        fiver += 1;
                        open_end = 0;
                    }
                }
                if (board[i - y + 1][m - y] != allay || i - y + 1 == 7){
                    if (board[i - y][m - y - 1] == allay && m - y == 1){
                        fiver = 1;
                    }
                    if (board[i - y + 1][m - y] == null){
                        open_end += 1;
                    }
                    if (board[i - y + 1][m - y] == enemy){
                        open_end += 0;
                    }

                    if(open_end == 2){
                        switch (fiver){
                            case 1: score_cache = ONE; break;
                            case 2: score_cache = TWO; break;
                            case 3: score_cache = THREE; break;
                            case 4: score_cache = FOUR; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }
                    else{
                        switch (fiver){
                            case 1: score_cache = ONE_B; break;
                            case 2: score_cache = TWO_B; break;
                            case 3: score_cache = THREE_B; break;
                            case 4: score_cache = FOUR_B; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }

                    if(score_cache != 0){
                        score_out = score_out + score_cache;
                        score_cache = 0;
                    }
                }
            }
        }


//         '/' search
        for (int i = 0; i <= m + n - 2; i++) {
            for (int x = 0; x <= i && (x < n) && i - x < m; x++) {
               if(board[x + 1][i - x] == allay){
                    fiver += 1;
                    if (board[x][i - x + 1] == null){
                        open_end = 1;
                    }
                    if (board[x][i - x + 1] == enemy || i - x == 0){
                        open_end = 0;
                    }
                    if (board[x][i - x + 1] == allay && x + 1 == 1){
                        fiver += 1;
                        open_end = 0;
                    }
                }
                if (board[x + 1][i - x] != allay || i - x == 0){
                    if (board[x][i - x + 1] == allay && x + 1 == 1){
                        fiver = 1;
                    }
                    if (board[x + 1][i - x] == null){
                        open_end += 1;
                    }
                    if (board[x + 1][i - x] == enemy){
                        open_end += 0;
                    }

                    if(open_end == 2){
                        switch (fiver){
                            case 1: score_cache = ONE; break;
                            case 2: score_cache = TWO; break;
                            case 3: score_cache = THREE; break;
                            case 4: score_cache = FOUR; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }
                    else{
                        switch (fiver){
                            case 1: score_cache = ONE_B; break;
                            case 2: score_cache = TWO_B; break;
                            case 3: score_cache = THREE_B; break;
                            case 4: score_cache = FOUR_B; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }

                    if(score_cache != 0){
                        score_out = score_out + score_cache;
                        score_cache = 0;
                    }
                }

            }
            for (int y = m - 1; y >= 0 && i - y >= 0 && i - y < n && i - y > 0; y--) {
                if(board[i - y + 1][y] == allay){
                    fiver += 1;
                    if (board[i - y][y + 1] == null){
                        open_end = 1;
                    }
                    if (board[i - y][y + 1] == enemy || i - y + 1 == 7){
                        open_end = 0;
                    }
                    if (board[i - y][y + 1] == allay && y == 6 ){
                        fiver += 1;
                        open_end = 0;
                    }
                }
                if (board[i - y + 1][y] != allay || i - y + 1 == 7){
                    if (board[i - y][y + 1] == allay && y == 6){
                        fiver = 1;
                    }
                    if (board[i - y + 1][y] == null){
                        open_end += 1;
                    }
                    if (board[i - y + 1][y] == enemy){
                        open_end += 0;
                    }

                    if(open_end == 2){
                        switch (fiver){
                            case 1: score_cache = ONE; break;
                            case 2: score_cache = TWO; break;
                            case 3: score_cache = THREE; break;
                            case 4: score_cache = FOUR; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }
                    else{
                        switch (fiver){
                            case 1: score_cache = ONE_B; break;
                            case 2: score_cache = TWO_B; break;
                            case 3: score_cache = THREE_B; break;
                            case 4: score_cache = FOUR_B; break;
                            case 5: score_cache = FIVE; break;
                        }
                        open_end = 0;
                        fiver = 0;
                    }

                    if(score_cache != 0){
                        score_out = score_out + score_cache;
                        score_cache = 0;
                    }
                }
            }
        }
        return score_out;
    }
}




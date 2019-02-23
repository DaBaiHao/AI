//import org.omg.CORBA.CODESET_INCOMPATIBLE;

//import sun.awt.SunHints;
//import sun.invoke.util.VerifyAccess;

import java.awt.*;

public class ABBot extends GomokuPlayer{

    private int winner;

    @Override
    public Move chooseMove(Color[][] board, Color color) {
/*
        board[0][0] = Color.WHITE;
        board[1][1] = Color.WHITE;
        board[2][2] = Color.WHITE;
        board[6][6] = Color.WHITE;
        board[4][4] = Color.WHITE;
*/
      /*  board[5][5] = Color.BLACK;
        board[6][2] = Color.BLACK;
        board[6][3] = Color.BLACK;
        board[6][4] = Color.BLACK;
        board[6][5] = Color.BLACK;
        board[6][6] = Color.BLACK;
        board[7][7] = Color.BLACK;*/



        //int value = analyzeGameState(board, Color.WHITE);

        //System.out.print(value);
        //Point moveToPoint = new Point(7,7);

        Point moveToPoint = forwardModel(board, color);


        return new Move(moveToPoint.x, moveToPoint.y);
    }

    private Point forwardModel(Color[][] board, Color player){
        Point bestPoint = new Point();
        Point[] CandidatePoints = getCandidatePoints(board, 1);
        double[] values = new double[CandidatePoints.length];

        //System.out.print("The length of points: " + CandidatePoints.length + "\n");

        //for (int i = 0; i<CandidatePoints.length; i++)
            //System.out.print("; x" + CandidatePoints[i].x +" y" + CandidatePoints[i].y );

        if (CandidatePoints.length == 0){
            return new Point(4,4);
        }
        else{
            for (int i = 0;  i<CandidatePoints.length; i++)
            {
                values[i]= search(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, CandidatePoints[i], 4, player, player, board);
            }
        }
        int maxValueIndex = 0;
        double maxValue = 0;
        for (int i=0; i<values.length; i++){
            if (values[i] > maxValue)
            {
                maxValue = values[i];
                maxValueIndex = i;
            }
            //System.out.print("All value in value array: " + values[i] + " " + i + "\n");
        }
        return CandidatePoints[maxValueIndex];
    }

    private double search(double alpha, double beta, Point point, int depth, Color rootPlayer, Color currentPlayer, Color[][] board){

        int terminal_node = analyzeGameState(board, currentPlayer);
        //.out.print("value when start: "+ terminal_node + "\n");
        if (terminal_node >= 1000 || depth < 0){
            return terminal_node; }

        board[point.x][point.y] = currentPlayer;

        Point[] childPoints = getCandidatePoints(board, 1);
        if (currentPlayer == rootPlayer) {
            if (currentPlayer == Color.BLACK)
                currentPlayer = Color.WHITE;
            else
                currentPlayer = Color.BLACK;
            double value = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < childPoints.length; i++) {
                value = Math.max(value, search(alpha, beta, childPoints[i], depth-1, rootPlayer, currentPlayer, board));
                //System.out.print("Max value: " + value + "\n");
                alpha = Math.max(alpha, value);
                board[point.x][point.y] = null;
                if (alpha >= beta){
                    break;
                }
            }
            return value;
        }
        else{
            if (currentPlayer == Color.BLACK)
                currentPlayer = Color.WHITE;
            else
                currentPlayer = Color.BLACK;
            double value = Double.POSITIVE_INFINITY;
            for (int i = 0; i < childPoints.length; i++) {
                value = Math.min(value, search(alpha, beta, childPoints[i], depth-1, rootPlayer, currentPlayer, board));
                //System.out.print("Min value: " + value + "\n");
                beta = Math.min(value, beta);
                board[point.x][point.y] = null;
                if (alpha >= beta){
                    break;
                }
            }
            return value;
        }
    }

    // Range = 0 means browse all the map
    private Point[] getCandidatePoints(Color[][] board, int range){
        boolean isEmpty[][] = new boolean[board.length][board.length];
        int numCandidates = 0;

        for(int i=0; i<board.length; i++) {
            for (int j=0; j<board.length; j++){
                 if (board[i][j] == null){
                    continue;
                }
                else {
                    int verUp = Math.max(0, i-range);
                    int verDown = Math.min(7, i+range);
                    int horLeft = Math.max(0, j-range);
                    int horRight = Math.min(7, j+range);
                    for (int neiVInd=verUp; neiVInd<=verDown;neiVInd++ ) {
                        for (int neiHInd=horLeft; neiHInd<=horRight; neiHInd++ )
                        {
                             if (!isEmpty[neiVInd][neiHInd] && board[neiVInd][neiHInd] == null){
                                isEmpty[neiVInd][neiHInd] = true;
                                numCandidates++;
                            }
                        }
                    }
                }
            }
        }
        int candiInd = 0;
        Point[] Candidates = new Point[numCandidates];
        for (int i=0; i<board.length; i++){
            for (int j=0; j<board.length; j++){
                if (isEmpty[i][j]){
                    Candidates[candiInd++] = new Point(i,j);
                }
            }
        }
        return Candidates;
    }



    private int analyzeGameState(Color[][] board, Color player){
        int value = 0;
        int value_white = 0;
        int value_black = 0;
        int consecutive_white = 0;
        int consecutive_black = 0;
        int openEnds_white = 0;
        int openEnds_black = 0;

        // Horizontal consecutive and openends
        for (int verIndex=0; verIndex<board.length; verIndex++){
            for (int horIndex=0; horIndex<board.length; horIndex++){
                while (horIndex<board.length && board[verIndex][horIndex]==Color.WHITE){
                    consecutive_white = consecutive_white + 1;
                    horIndex = horIndex + 1;
                }
                if (horIndex >= board.length) {
                    if (consecutive_white>0 && horIndex-consecutive_white-1 >=0 && board[verIndex][horIndex-consecutive_white-1] == null)
                    {openEnds_white = openEnds_white +1;}
                    value_white = value_white + evaluationFunction(consecutive_white, openEnds_white);
                    //System.out.print("Horizontal white end: " + openEnds_white + "_" + "connect: " + consecutive_white + "\n");
                    consecutive_white = 0;
                    openEnds_white = 0;
                    break;
                }
                 if (board[verIndex][horIndex] == null && consecutive_white > 0) {
                     openEnds_white = openEnds_white + 1;
                 }
                 if (consecutive_white>0 && horIndex-consecutive_white-1 >=0 && board[verIndex][horIndex-consecutive_white-1] == null)
                 {openEnds_white = openEnds_white +1;}
                value_white = value_white + evaluationFunction(consecutive_white, openEnds_white);
                //System.out.print("Horizontal white end: " + openEnds_white + "_" + "connect: " + consecutive_white+ "\n");
                consecutive_white = 0;
                openEnds_white = 0;

                while (horIndex<board.length && board[verIndex][horIndex] == Color.BLACK){
                    consecutive_black = consecutive_black + 1;
                    horIndex = horIndex + 1; }
                if (horIndex >= board.length){
                    if (horIndex-consecutive_black-1 >= 0 && consecutive_black >0 && board[verIndex][horIndex-consecutive_black-1] == null ){
                        openEnds_black = openEnds_black +1; }
                    value_black = value_black + evaluationFunction(consecutive_black, openEnds_black);
                    //System.out.print("Horizontal black end: " + openEnds_black + "_" + "connect: " + consecutive_black+ "\n");
                    consecutive_black = 0;
                    openEnds_black = 0;
                    break; }
                if (board[verIndex][horIndex] == null && consecutive_black > 0) {
                    openEnds_black = openEnds_black + 1;
                }
                if (horIndex-consecutive_black-1 >= 0 && consecutive_black >0 && board[verIndex][horIndex-consecutive_black-1] == null ){
                        openEnds_black = openEnds_black +1;
                }
                value_black = value_black + evaluationFunction(consecutive_black, openEnds_black);
                //System.out.print("Horizontal black end: " + openEnds_black + "_" + "connect: " + consecutive_black+ "\n");
                consecutive_black = 0;
                openEnds_black = 0;
            }
        }
        //System.out.println(value_black);
        //System.out.print("Horizon finished | ");

        // Vertical consecutive and openends
        for (int horIndex=0; horIndex<board.length; horIndex++){
            for (int verIndex=0; verIndex<board.length; verIndex++){
                while (verIndex<board.length && board[verIndex][horIndex] == Color.WHITE){
                    consecutive_white = consecutive_white + 1;
                    verIndex = verIndex+1;
                }
                if (verIndex >= board.length){
                    if (verIndex-consecutive_white-1 >= 0 && consecutive_white > 0 && board[verIndex-consecutive_white-1][horIndex] == null)
                    {openEnds_white = openEnds_white +1;}
                    value_white = value_white + evaluationFunction(consecutive_white, openEnds_white);
                    //System.out.print("Ver white end: " + openEnds_white + "_" + "connect: " + consecutive_white+ "\n");
                    consecutive_white = 0;
                    openEnds_white = 0;
                    break;
                }
                if (board[verIndex][horIndex] == null && consecutive_white > 0) {
                    openEnds_white = openEnds_white + 1;
                }
                if (verIndex-consecutive_white-1 >= 0 && consecutive_white > 0 && board[verIndex-consecutive_white-1][horIndex] == null)
                    {openEnds_white = openEnds_white +1;}

                value_white = value_white + evaluationFunction(consecutive_white, openEnds_white);
                //System.out.print("Ver white end: " + openEnds_white + "_" + "connect: " + consecutive_white+ "\n");
                consecutive_white = 0;
                openEnds_white = 0;

                while ((verIndex<board.length) && (board[verIndex][horIndex]==Color.BLACK)){
                    consecutive_black = consecutive_black + 1;
                    verIndex = verIndex + 1;
                }
                if (verIndex >= board.length){
                    if (consecutive_black>0 && verIndex-consecutive_black-1 >= 0 && board[verIndex-consecutive_black-1][horIndex] == null){
                        openEnds_black = openEnds_black +1; }
                    value_black = value_black + evaluationFunction(consecutive_black, openEnds_black);
                    //System.out.print("Ver black end: " + openEnds_black + "_" + "connect: " + consecutive_black+ "\n");
                    consecutive_black = 0;
                    openEnds_black = 0;
                    break;
                }
                 if (consecutive_black > 0 && board[verIndex][horIndex] == null) {
                     openEnds_black = openEnds_black + 1;
                 }
                 if (consecutive_black > 0 &&verIndex-consecutive_black-1 >= 0 && board[verIndex-consecutive_black-1][horIndex] == null){
                        openEnds_black = openEnds_black +1;
                 }

                value_black = value_black + evaluationFunction(consecutive_black, openEnds_black);
                //System.out.print("Ver black end: " + openEnds_black + "_" + "connect: " + consecutive_black+ "\n");
                consecutive_black = 0;
                openEnds_black = 0;
            }
        }
        //System.out.print("Vertical finished | ");

        // slash diagonal consecutive and openends
        // start from 4th vertical and horizontal, for it is no possible to form a 5 connected in corner
        int horIndex = 0;
        for (int num = 4; num<board.length*2-4-1; num++){
            for (int verIndex = 0; verIndex<board.length; verIndex++){
                horIndex = num-verIndex;
                while (horIndex<board.length && horIndex>=0 && verIndex<board.length && board[verIndex][horIndex] == Color.WHITE){
                    consecutive_white = consecutive_white + 1;
                    verIndex = verIndex+1;
                    horIndex = num - verIndex;
                }
                if (horIndex >= board.length || horIndex<0 || verIndex >= board.length){
                    if (consecutive_white>0 && verIndex-consecutive_white-1>=0 && horIndex+consecutive_white+1<board.length && board[verIndex-consecutive_white-1][horIndex+consecutive_white+1] == null)
                    {openEnds_white = openEnds_white +1; }
                    value_white = value_white + evaluationFunction(consecutive_white, openEnds_white);
                    //System.out.print("Slash white end: " + openEnds_white + "_" + "connect: " + consecutive_white+ "\n");
                    consecutive_white = 0;
                    openEnds_white = 0;
                    break; }
                 if (board[verIndex][horIndex] == null && consecutive_white >0){
                    openEnds_white = openEnds_white + 1;
                 }
                 if (consecutive_white>0 && verIndex-consecutive_white-1>=0 && horIndex+consecutive_white+1<board.length && board[verIndex-consecutive_white-1][horIndex+consecutive_white+1] == null)
                     {openEnds_white = openEnds_white +1; }

                value_white = value_white + evaluationFunction(consecutive_white, openEnds_white);
                //System.out.print("Slash white end: " + openEnds_white + "_" + "connect: " + consecutive_white+ "\n");
                consecutive_white = 0;
                openEnds_white = 0;

                while (horIndex<board.length && horIndex>=0 && verIndex<board.length && board[verIndex][horIndex] == Color.BLACK){
                    consecutive_black = consecutive_black + 1;
                    verIndex = verIndex + 1;
                    horIndex = num - verIndex;
                }
                if (horIndex >= board.length || horIndex<0 || verIndex >= board.length){
                    if (consecutive_black>0 && verIndex-consecutive_black-1>=0 && horIndex+consecutive_black+1<board.length && board[verIndex-consecutive_black-1][horIndex+consecutive_black+1] == null){
                        openEnds_black = openEnds_black +1;
                    }
                    value_black = value_black + evaluationFunction(consecutive_black, openEnds_black);
                    consecutive_black = 0;
                    openEnds_black = 0;
                    //System.out.print("Slash black end: " + openEnds_black + "_" + "connect: " + consecutive_black+ "\n");

                    break; }
                 if (consecutive_black>0 && board[verIndex][horIndex] == null) {
                     openEnds_black = openEnds_black + 1;
                 }
                 if (consecutive_black>0 && verIndex-consecutive_black-1>=0 && horIndex+consecutive_black+1<board.length && board[verIndex-consecutive_black-1][horIndex+consecutive_black+1] == null){
                        openEnds_black = openEnds_black +1;
                    }

                value_black = value_black + evaluationFunction(consecutive_black, openEnds_black);
                //System.out.print("Slash black end: " + openEnds_black + "_" + "connect: " + consecutive_black+ "\n");
                consecutive_black = 0;
                openEnds_black = 0;

            }
        }
        //System.out.print("Slash finished | ");

        // backslash diagonal consecutive and openends
        int verIndex = 0;
        for (int num = 4; num<board.length*2-4-1; num++){
            int diff = board.length - num - 1;      // the offset of vertical position and horizontal position
            for (horIndex = 0; horIndex<board.length; horIndex++){
                verIndex = horIndex - diff;

/*
                System.out.print("The position: ");
                System.out.print(verIndex);
                System.out.print("V, ");
                System.out.print(horIndex);
                System.out.print("H\n");
*/
                if (verIndex < 0 || verIndex >= board.length) {
                    break; }
                while (verIndex>=0 && verIndex<board.length && horIndex < board.length && board[verIndex][horIndex] == Color.WHITE){
                    consecutive_white = consecutive_white + 1;
                    horIndex = horIndex+1;
                    verIndex = horIndex - diff;
                }
                if (verIndex < 0 || verIndex >= board.length || horIndex>=board.length) {
                    if (consecutive_white > 0 && verIndex-consecutive_white-1 >=0 && horIndex-consecutive_white-1 >=0 && board[verIndex-consecutive_white-1][horIndex-consecutive_white-1] == null)
                    {openEnds_white = openEnds_white +1;}
                    value_white = value_white + evaluationFunction(consecutive_white, openEnds_white);
                    //System.out.print("BackSlash White end: " + openEnds_white + "_" + "connect: " + consecutive_white+ "White value"+ value_white +"\n");
                    consecutive_white = 0;
                    openEnds_white = 0;
                    break; }
                 if (consecutive_white > 0 && board[verIndex][horIndex] == null) {
                     openEnds_white = openEnds_white + 1;
                 }
                 if (consecutive_white > 0 && verIndex-consecutive_white-1 >=0 && horIndex-consecutive_white-1 >=0 && board[verIndex-consecutive_white-1][horIndex-consecutive_white-1] == null)
                     {openEnds_white = openEnds_white +1;}
                value_white = value_white + evaluationFunction(consecutive_white, openEnds_white);
                //System.out.print("BackSlash White end: " + openEnds_white + "_" + "connect: " + consecutive_white+ " White value"+ value_white + "\n");
                consecutive_white = 0;
                openEnds_white = 0;

                while (verIndex>=0 && verIndex<board.length && horIndex < board.length && board[verIndex][horIndex] == Color.BLACK){
                    consecutive_black = consecutive_black + 1;
                    horIndex = horIndex + 1;
                    verIndex = horIndex - diff;
                }
                if (verIndex <0 || verIndex >= board.length || horIndex>=board.length){
                    if (consecutive_black >0 && verIndex-consecutive_black-1 >=0 && horIndex-consecutive_black-1 >=0 && board[verIndex - consecutive_black - 1][horIndex - consecutive_black - 1] == null) {
                        openEnds_black = openEnds_black + 1;
                    }
                    value_black = value_black + evaluationFunction(consecutive_black, openEnds_black);
                    //System.out.print("BackSlash Black end: " + openEnds_black + "_" + "connect: " + consecutive_black+ "\n");

                    consecutive_black = 0;
                    openEnds_black = 0;
                    break; }
                 if (consecutive_black >0 && board[verIndex][horIndex] == null) {
                     openEnds_black = openEnds_black + 1;
                 }
                 if (consecutive_black >0 && verIndex-consecutive_black-1 >=0 && horIndex-consecutive_black-1 >=0 && board[verIndex - consecutive_black - 1][horIndex - consecutive_black - 1] == null) {
                     openEnds_black = openEnds_black + 1;
                 }

                value_black = value_black + evaluationFunction(consecutive_black, openEnds_black);
                //System.out.print("BackSlash Black end: " + openEnds_black + "_" + "connect: " + consecutive_black+ "\n");
                consecutive_black = 0;
                openEnds_black = 0;
            }
        }
        //System.out.print("Back Slash finished  ");

        value = value_black + value_white;
        //System.out.println("value of this: " + value );
        return value;
    }


    private int evaluationFunction(int consecutive, int openends) {
        if ((consecutive<5 && openends==0) || consecutive==0)
            return 0;
        switch (consecutive)
        {
            case 4:{
                switch (openends){
                    case 1:{
                        return 100;
                    }
                    case 2:{
                        return 200;
                    }
                }
            }
            case 3: {
                switch (openends){
                    case 1:{
                        return 30;
                    }
                    case 2:{
                        return 50;
                    }
                }
            }
            case 2:{
                switch (openends){
                    case 1:{
                        return 5;
                    }
                    case 2:{
                        return 10;
                    }
                }
            }
            case 1:{
                switch (openends){
                    case 1:{
                        return 1;
                    }
                    case 2:{
                        return 3;
                    }
                }
            }
            default:
                //System.out.println("The consecutive with 200k: "+ consecutive);
                return  1000;
        }

    }
}


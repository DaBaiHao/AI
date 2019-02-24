# BlphaGomoku

<p align="right">Name: Hao Bai<br/>
ID: 180223545</p>

## Algorithm
1. This Gomoku bot is using the minmax search and alpha-beta pruning.
 - Search will find all available points on the board.
 - Firstly, try each available points, and calculate using minmax search and alpha-beta pruning. If the trying location can lead our win. This time move is this Point.
 - The alpha value is the worst case for maxmizer. The aim of the maxmizer function maxmum our round board value. If the value of the board is less than the alpha value, the search will never search the following available points. Which reduced search time.
 - The beta value is the worst case for minimizer. The aim of the minimizer function is to minimize the opponent round board value.If the value of the board is higher than the beta value, the search will never search the following available points. Which reduced search time.
 - There are 4 layers for each bot to search and calculate the best point for this time to go.
   - Sequence:
     - Maxmizer(bot round)
     - Minimizer(opponent round)
     - Maxmizer(bot round)
     - Minimizer(opponent round)
 - After calculate, the algorithm will return a list of the candidate points. And calculate the point value of each candidate point. And return the best one.

2. The value function:
 - The board value is calculate the number of contiguous point.
     - The blocked contiguous point:
       - 1 point  got 1
       - 2 points got 10
       - 3 points got 500
       - 4 points got 10000
       - 5 points got 100000000
     - The unblocked contiguous point:
       - 1 point  got 1
       - 2 points got 100
       - 3 points got 9900
       - 4 points got 10099
       - 5 points got 100000000

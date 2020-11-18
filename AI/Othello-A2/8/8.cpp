/*
* @file botTemplate.cpp
* @author Arun Tejasvi Chaganty <arunchaganty@gmail.com>
* @date 2010-02-04
* Template for users to create their own bots
*/

#include "Othello.h"
#include "OthelloBoard.h"
#include "OthelloPlayer.h"
#include <cstdlib>
#include <bits/stdc++.h>
using namespace std;
using namespace Desdemona;

class MyBot: public OthelloPlayer
{
    public:
        /**
         * Initialisation routines here
         * This could do anything from open up a cache of "best moves" to
         * spawning a background processing thread. 
         */
        OthelloBoard custom_board;
        Turn opp_turn;
        double weights[8][8]  = {{20.0, -3.0, 11.0, 8.0, 8.0, 11.0, -3.0, 20.0},
                        {-3.0, -7.0, -4.0, 1.0, 1.0, -4.0, -7.0, -3.0},
                        {11.0, -4.0, 2.0, 2.0, 2.0, 2.0, -4.0, 11.0},
                        {8.0, 1.0, 2.0, -3.0, -3.0, 2.0, 1.0, 8.0},
                        {8.0, 1.0, 2.0, -3.0, -3.0, 2.0, 1.0, 8.0},
                        {11.0, -4.0, 2.0, 2.0, 2.0, 2.0, -4.0, 11.0},
                        {-3.0, -7.0, -4.0, 1.0, 1.0, -4.0, -7.0, -3.0},
                        {20.0, -3.0, 11.0, 8.0, 8.0, 11.0, -3.0, 20.0}};
        MyBot( Turn turn );

        /**
         * Play something 
         */
        virtual Move play( const OthelloBoard& board );
        virtual double alfaBeta(OthelloBoard board, Move move,double alfa ,double beta, int depth, int max_depth);
        virtual double diffHuristic(OthelloBoard board);
    private:
};

MyBot::MyBot( Turn turn )
    : OthelloPlayer( turn )
{
    opp_turn = other(turn);
}

Move MyBot::play( const OthelloBoard& board )
{
    list<Move> moves = board.getValidMoves( turn );
    double best_score = (double)INT_MIN;
    double beta  = (double)INT_MAX;
    Move best_move = *moves.begin();
    int max_depth = 6;
    for(auto it = moves.begin();it != moves.end();++it){
        double score = alfaBeta(board, *it, best_score, beta, 1, max_depth);
        if (score > best_score){
            best_score = score;
            best_move = *it;
        }
    }
    return best_move;
}

double MyBot::diffHuristic(OthelloBoard board){
    double stability = 0.0, pl= 0.0, opp= 0.0, deno = 0.0;
    double pl_c = 0, op_c = 0;
    for(int i = 0;i < 8;++i){
        for(int j = 0; j < 8;++j){
            if(board.get(i,j) == turn){
                stability += weights[i][j];
                deno += weights[i][j];
                pl++;
            }
            else if(board.get(i,j) == opp_turn){
                stability -= weights[i][j];
                deno += weights[i][j];
                opp++;
            }
        }
    }
    if(board.get(0,0) == turn) pl_c++;
    else if(board.get(0,0) == opp_turn) op_c++;
    if(board.get(0,7) == turn) pl_c++;
    else if(board.get(0,7) == opp_turn) op_c++;
    if(board.get(7,0) == turn) pl_c++;
    else if(board.get(7,0) == opp_turn) op_c++;
    if(board.get(7,7) == turn) pl_c++;
    else if(board.get(7,7) == opp_turn) op_c++;
    double pl_moves = (double)board.getValidMoves(turn).size();
    double opp_moves = (double)board.getValidMoves(opp_turn).size();
    double s  = 0.0, p = 0.0, m = 0.0,c = 0.0;
    if (deno != 0.0){
        s = (double)(stability/deno)*100.0;
    }
    if(pl+opp != 0.0){
        p = (double)(pl-opp)/(pl+opp)*100.0;
    }
    if(pl_moves + opp_moves != 0.0){
        m = (double)(pl_moves-opp_moves)/(pl_moves+opp_moves)*100.0;
    }
    if(pl_c+op_c != 0.0){
        c = (double)(pl_c-op_c)/(pl_c+op_c)*100;
    }
    double result = (0.40*s)+(0.25*p)+(0.05*m)+(0.30*c);
    return result;
}

double MyBot::alfaBeta(OthelloBoard board, Move move,double alfa ,double beta, int depth, int max_depth){
    if(depth == max_depth){
        return diffHuristic(board);
    }
    if(depth%2 == 0){
        board.makeMove(opp_turn, move);
        list<Move> moves = board.getValidMoves( turn );
        if(moves.empty() && board.getValidMoves(opp_turn).empty()){
            return diffHuristic(board);
        }
        for (auto it = moves.begin();it != moves.end();++it){
            alfa = max(alfa, alfaBeta(board, *it, alfa, beta, depth+1, max_depth));
            if(alfa >= beta){
                return beta;
            }
        }
        return alfa;
    }
    else{
        board.makeMove(turn, move);
        list<Move> moves = board.getValidMoves( opp_turn );
        if(moves.empty() && board.getValidMoves(turn).empty()){
            return diffHuristic(board);
        }
        for (auto it = moves.begin();it != moves.end();++it){
            beta = min(beta, alfaBeta(board, *it, alfa, beta, depth+1, max_depth));
            if(alfa >= beta){
                return alfa;
            }
        }
        return beta;
    }
    return -1;
}


// The following lines are _very_ important to create a bot module for Desdemona

extern "C" {
    OthelloPlayer* createBot( Turn turn )
    {
        return new MyBot( turn );
    }

    void destroyBot( OthelloPlayer* bot )
    {
        delete bot;
    }
}



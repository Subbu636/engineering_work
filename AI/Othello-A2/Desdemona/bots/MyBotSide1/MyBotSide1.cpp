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
        Turn opponents_turn;
        MyBot( Turn turn );

        /**
         * Play something 
         */
        virtual Move play( const OthelloBoard& board );
        virtual int alfaBeta(OthelloBoard board, Move move,int alfa ,int beta, int depth, int max_depth);
        virtual int diffHuristic(OthelloBoard board);
    private:
};

MyBot::MyBot( Turn turn )
    : OthelloPlayer( turn )
{
    opponents_turn = other(turn);
}

Move MyBot::play( const OthelloBoard& board )
{
    list<Move> moves = board.getValidMoves( turn );
    list<Move>::iterator it = moves.begin();
    int max_depth = 3;
    int best_score = alfaBeta(board, *it, INT_MIN, INT_MAX, 1, max_depth);
    Move best_move = *it;
    ++it;
    for(;it != moves.end();++it){
        int score = alfaBeta(board, *it, best_score, INT_MAX, 1, max_depth);
        if (score > best_score){
            best_score = score;
            best_move = *it;
        }
    }
    return best_move;
}

int MyBot::diffHuristic(OthelloBoard board){
    int ans = board.getBlackCount()-board.getRedCount();
    if (turn == RED){
        ans  = -1*ans;
    }
    return ans;
}

int MyBot::alfaBeta(OthelloBoard board, Move move,int alfa ,int beta, int depth, int max_depth){
    if(depth == max_depth){
        return diffHuristic(board);
    }
    if(depth%2 == 0){
        board.makeMove(opponents_turn, move);
        list<Move> moves = board.getValidMoves( turn );
        if(moves.empty() && board.getValidMoves(opponents_turn).empty()){
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
        list<Move> moves = board.getValidMoves( opponents_turn );
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



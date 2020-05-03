package com.nafkhanzam.checkmatefinder;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class CheckmateFinder {
    private Board board;

    public CheckmateFinder(Board board) {
        this.board = board;
    }

    public Answer findAnswer(int depth) throws MoveGeneratorException {
        Answer answer = new Answer();
        _answerMove(answer, depth);
        return answer;
    }

    private boolean _answerMove(Answer answer, int depth) throws MoveGeneratorException {
        for (Move move : MoveGenerator.generateLegalMoves(board)) {
            board.doMove(move);
            boolean found = _opponentMove(answer, depth);
            board.undoMove();
            if (found) {
                answer.setAnswerMove(move);
                return true;
            }
        }
        return false;
    }

    private boolean _opponentMove(Answer answer, int depth) throws MoveGeneratorException {
        MoveList availableMoves = MoveGenerator.generateLegalMoves(board);
        if (availableMoves.size() == 0) {
            return board.isKingAttacked();
        } else if (depth == 1) {
            return false;
        }
        for (Move move : availableMoves) {
            board.doMove(move);
            Answer next = new Answer();
            boolean found = _answerMove(next, depth - 1);
            board.undoMove();
            if (!found) {
                return false;
            }
            answer.putAnswer(move, next);
        }
        return true;
    }

}
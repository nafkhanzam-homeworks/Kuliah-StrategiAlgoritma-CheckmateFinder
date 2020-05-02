package com.nafkhanzam.checkmatefinder;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

public class CheckmateFinder {
    private Board board;

    public CheckmateFinder(Board board) {
        this.board = board;
    }

    public Answer findAnswer(int depth) throws MoveGeneratorException {
        Answer answer = new Answer();
        _answerMove(answer, depth, true);
        return answer;
    }

    private boolean _answerMove(Answer answer, int depth, boolean root) throws MoveGeneratorException {
        for (Move move : MoveGenerator.generateLegalMoves(board)) {
            board.doMove(move);
            boolean found = depth > 1 ? _opponentMove(answer, depth) : board.isMated();
            board.undoMove();
            if (found) {
                answer.setAnswerMove(move);
                return true;
            }
        }
        return false;
    }

    private boolean _opponentMove(Answer answer, int depth) throws MoveGeneratorException {
        if (_noAvailableMove()) {
            return board.isKingAttacked();
        }
        for (Move move : MoveGenerator.generateLegalMoves(board)) {
            board.doMove(move);
            Answer next = new Answer();
            boolean found = _answerMove(next, depth - 1, false);
            board.undoMove();
            if (!found) {
                return false;
            }
            answer.putAnswer(move, next);
        }
        return true;
    }

    private boolean _noAvailableMove() throws MoveGeneratorException {
        return MoveGenerator.generateLegalMoves(board).size() == 0;
    }

}
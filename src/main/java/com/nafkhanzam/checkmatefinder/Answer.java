package com.nafkhanzam.checkmatefinder;

import java.util.HashMap;
import java.util.Map;

import com.github.bhlangonijr.chesslib.move.Move;

public class Answer {
    private Move answerMove;
    private Map<Move, Answer> nextAnswer;

    public Answer() {
        nextAnswer = new HashMap<>();
    }

    public Move getAnswerMove() {
        return this.answerMove;
    }

    public void setAnswerMove(Move move) {
        this.answerMove = move;
    }

    public void setNextAnswer(Map<Move, Answer> nextAnswer) {
        this.nextAnswer = nextAnswer;
    }

    public Map<Move, Answer> getAnswers() {
        return nextAnswer;
    }

    public void putAnswer(Move move, Answer answer) {
        nextAnswer.put(move, answer);
    }

    public Answer getNextAnswer(Move move) {
        return nextAnswer.get(move);
    }

    public boolean end() {
        return nextAnswer.isEmpty();
    }

}
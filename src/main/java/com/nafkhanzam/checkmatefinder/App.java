package com.nafkhanzam.checkmatefinder;

import java.util.Scanner;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

public class App {
    public static void main(String[] args) throws MoveGeneratorException {
        Board board = new Board();
        board.loadFromFen("8/8/8/8/1Q6/1K6/8/2Nk5 w - - 0 0"); // 1
        // board.loadFromFen("8/8/5R2/8/2P1k3/2K5/5P2/2B5 w - - 0 0"); // 2
        // board.loadFromFen("5b1k/7p/4QK2/2qN3R/2B3p1/7r/8/8 w - - 0 0"); // 7532
        // board.loadFromFen("5k2/5p1r/3Kn3/2pb1q2/Q7/5pR1/3n3P/4R3 w - - 0 0"); // ?
        // board.loadFromFen("8/3b3p/5k2/2R5/8/3QP1K1/6P1/8 w - - 0 0"); // ?
        CheckmateFinder finder = new CheckmateFinder(board);
        long time = System.currentTimeMillis();
        Answer answer = finder.findAnswer(2);
        time = System.currentTimeMillis() - time;

        if (answer.end()) {
            System.out.println("Answer cannot be found!");
        } else {
            System.out.printf("Answer found in %dms.\n", time);
            try (Scanner in = new Scanner(System.in)) {
                while (!answer.end()) {
                    Move answerMove = answer.getAnswerMove();
                    System.out.printf("Move %s!\n", answerMove);
                    board.doMove(answerMove);

                    System.out.printf("Opponent's move: ");
                    Move move = new Move(in.nextLine(), board.getSideToMove());
                    System.out.println();

                    answer = answer.getNextAnswer(move);
                    board.doMove(move);
                }
                System.out.printf("Move %s and checkmate!\n", answer.getAnswerMove());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

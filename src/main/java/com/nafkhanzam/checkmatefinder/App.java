package com.nafkhanzam.checkmatefinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.BoardEvent;
import com.github.bhlangonijr.chesslib.BoardEventType;
import com.github.bhlangonijr.chesslib.MoveBackup;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

public class App {

    private static class AnswerTime {
        public final Board board;
        public final Answer answer;
        public final long timeInMs;

        public AnswerTime(Board board, Answer answer, long timeInMs) {
            this.board = board;
            this.answer = answer;
            this.timeInMs = timeInMs;
        }
    }

    private static void writeMove(Writer writer, BoardEvent e) {
        try {
            Move move = null;
            if (e instanceof MoveBackup) {
                MoveBackup mb = (MoveBackup) e;
                move = mb.getMove();
            } else if (e instanceof Move) {
                move = (Move) e;
            }
            writer.append(move.toString()).append('\n');
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static AnswerTime getAnswer(String fen, int depth) throws MoveGeneratorException, IOException {
        Board board = new Board();
        board.loadFromFen(fen);
        Writer writer = new FileWriter(new File("output_moves.txt"));
        board.addEventListener(BoardEventType.ON_MOVE, e -> App.writeMove(writer, e));
        board.addEventListener(BoardEventType.ON_UNDO_MOVE, e -> App.writeMove(writer, e));
        CheckmateFinder finder = new CheckmateFinder(board);
        long timeInMs = System.currentTimeMillis();
        Answer answer = finder.findAnswer(depth);
        timeInMs = System.currentTimeMillis() - timeInMs;
        writer.close();
        return new AnswerTime(board, answer, timeInMs);
    }

    public static void main(String[] args) throws MoveGeneratorException, IOException {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Number of moves: ");
            int depth = Integer.parseInt(in.nextLine());
            System.out.print("Chess board state in FEN: ");
            String fen = in.nextLine();
            AnswerTime ans = getAnswer(fen, depth);
            Board board = ans.board;
            Answer answer = ans.answer;
            if (answer.end()) {
                System.out.println("Answer cannot be found!");
            } else {
                System.out.printf("Answer found in %dms.\n", ans.timeInMs);
                Side side = board.getSideToMove();
                while (!answer.end()) {
                    Move answerMove = answer.getAnswerMove();
                    System.out.printf("Move %s!\n", answerMove);

                    System.out.printf("Opponent's move: ");
                    Move move = new Move(in.nextLine(), side);

                    answer = answer.getNextAnswer(move);
                    side = Side.values()[(side.ordinal() + 1) % 2];
                }
                System.out.printf("Move %s and checkmate!\n", answer.getAnswerMove());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

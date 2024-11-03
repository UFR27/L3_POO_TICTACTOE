package fr.pantheonsorbonne.miage.game.tictactoe;

import java.util.Random;

public class TicTacToeImpl extends AbstractTicTacToe{
    private static final Random rand = new Random();

    public TicTacToeImpl(int size) {
        super(size);
    }

    public TicTacToeImpl(String flat) {
        super(flat);
    }

    @Override
    public void addRand(char c) throws FullBoardException {

        
        if (isFull()) {
            throw new FullBoardException();
        }
        for (; ; ) {
            int x = rand.nextInt(board.length);
            int y = rand.nextInt(board.length);
            if (board[x][y] == '.') {
                board[x][y] = c;
                break;
            }

        }


    }
}

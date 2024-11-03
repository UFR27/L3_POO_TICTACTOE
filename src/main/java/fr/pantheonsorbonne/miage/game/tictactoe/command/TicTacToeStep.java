package fr.pantheonsorbonne.miage.game.tictactoe.command;

import fr.pantheonsorbonne.miage.PlayerFacade;
import fr.pantheonsorbonne.miage.game.tictactoe.TicTacToe;
import fr.pantheonsorbonne.miage.model.Game;

public abstract class TicTacToeStep {
    protected Game game;
    protected TicTacToe board;
    protected PlayerFacade playerFacade;

    public TicTacToeStep(Game game, TicTacToe board, PlayerFacade playerFacade) {
        this.game = game;
        this.board = board;
        this.playerFacade = playerFacade;
    }

    public abstract void perform();

}

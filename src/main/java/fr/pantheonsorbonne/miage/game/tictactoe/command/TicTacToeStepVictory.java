package fr.pantheonsorbonne.miage.game.tictactoe.command;

import fr.pantheonsorbonne.miage.PlayerFacade;
import fr.pantheonsorbonne.miage.game.tictactoe.TicTacToe;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;

public class TicTacToeStepVictory extends TicTacToeStep {

    public TicTacToeStepVictory(Game game, TicTacToe board, PlayerFacade playerFacade) {
        super(game, board, playerFacade);
    }

    @Override
    public void perform() {
        playerFacade.sendGameCommandToAll(game, new GameCommand(Command.GAME_OVER.name(), "defeat"));
        playerFacade.sendGameCommandToAll(game, new GameCommand(Command.BOARD.name(), board.toFlatString()));
        System.out.println("victory!\n" + board);
    }

}

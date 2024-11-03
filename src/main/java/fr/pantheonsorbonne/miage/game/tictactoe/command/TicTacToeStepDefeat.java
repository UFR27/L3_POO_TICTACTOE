package fr.pantheonsorbonne.miage.game.tictactoe.command;

import fr.pantheonsorbonne.miage.PlayerFacade;
import fr.pantheonsorbonne.miage.game.tictactoe.TicTacToe;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;

public class TicTacToeStepDefeat extends TicTacToeStep {

    public TicTacToeStepDefeat(Game game, TicTacToe board, PlayerFacade playerFacade) {
        super(game, board, playerFacade);
    }

    @Override
    public void perform() {
        playerFacade.sendGameCommandToAll(game, new GameCommand(Command.GAME_OVER.name(), "victory"));
        playerFacade.sendGameCommandToAll(game, new GameCommand(Command.BOARD.name(), board.toFlatString()));
        System.out.println("defeat!\n" + board);
    }

}

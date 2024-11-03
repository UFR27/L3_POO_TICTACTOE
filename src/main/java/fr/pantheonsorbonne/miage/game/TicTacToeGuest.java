package fr.pantheonsorbonne.miage.game;

import fr.pantheonsorbonne.miage.Command;
import fr.pantheonsorbonne.miage.Facade;
import fr.pantheonsorbonne.miage.PlayerFacade;
import fr.pantheonsorbonne.miage.game.tictactoe.FullBoardException;
import fr.pantheonsorbonne.miage.game.tictactoe.TicTacToe;
import fr.pantheonsorbonne.miage.game.tictactoe.TicTacToeImpl;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;

import java.util.Random;

/**
 * this is an example for the Guest in the tictactoe game
 */
public class TicTacToeGuest {
    public static void main(String[] args) throws Exception, FullBoardException {
        // get the facade as a player
        PlayerFacade facade = Facade.getFacade();
        facade.waitReady();
        // set our palyer name
        final String playerName = "Nicolas-" + new Random().nextInt();
        facade.createNewPlayer(playerName);
        System.out.println("I am: " + playerName);
        // wait until we are able to join a new game
        Game currentGame = facade.autoJoinGame("tictactoe2");

        // get our mark
        GameCommand command = facade.receiveGameCommand(currentGame);
        Command receivedCommand = Command.valueOf(command.name());
        if (!receivedCommand.equals(Command.YOU_ARE)) {
            throw new RuntimeException();
        }
        final char myMark = command.body().charAt(0);

        while (true) {
            // get a command from someone else
            GameCommand commandLoop = facade.receiveGameCommand(currentGame);
            receivedCommand = Command.valueOf(commandLoop.name());
            switch (receivedCommand) {
                case Command.BOARD:
                    // if we receive a new board, the game is not other and we should player one
                    // more round
                    handleNewBoard(facade, currentGame, myMark, commandLoop);
                    break;
                case Command.GAME_OVER:
                    // if the game is other, stop playing and show the results
                    handleGameOver(facade, currentGame, commandLoop);
                    System.exit(0);
                default:
                    break;
            }

        }

    }

    /**
     * react uppon gameover command received
     *
     * @param facade          the player facade
     * @param currentGame     the game currently played
     * @param gameOverCommand
     */
    private static void handleGameOver(PlayerFacade facade, Game currentGame, GameCommand gameOverCommand) {

        // show a message depending on our victory or not
        switch (gameOverCommand.body()) {
            case "victory":
                break;
            case "defeat":
                System.out.println("I've lost\n");
                break;
            case "tie":
                System.out.println("It's a tie\n");
        }

        // after the game over command, we should receive the final version of the board
        // and display it
        TicTacToe finalTicTacToe = new TicTacToeImpl(facade.receiveGameCommand(currentGame).body());
        System.out.println("final board: \n " + finalTicTacToe);
    }

    private static void handleNewBoard(PlayerFacade facade, Game currentGame, char myMark, GameCommand commandLoop)
            throws FullBoardException {
        TicTacToe board;
        board = new TicTacToeImpl(commandLoop.body());
        System.out.println("---------------------\n" + board);
        board.addRand(myMark);
        facade.sendGameCommandToAll(currentGame, new GameCommand(Command.BOARD.name(), board.toFlatString()));
    }

}

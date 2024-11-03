/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.pantheonsorbonne.miage.game;

import fr.pantheonsorbonne.miage.Command;
import fr.pantheonsorbonne.miage.Facade;
import fr.pantheonsorbonne.miage.HostFacade;
import fr.pantheonsorbonne.miage.PlayerFacade;
import fr.pantheonsorbonne.miage.game.tictactoe.FullBoardException;
import fr.pantheonsorbonne.miage.game.tictactoe.TicTacToe;
import fr.pantheonsorbonne.miage.game.tictactoe.TicTacToeImpl;
import fr.pantheonsorbonne.miage.game.tictactoe.command.TicTacToeStep;
import fr.pantheonsorbonne.miage.game.tictactoe.command.TicTacToeStepDefeat;
import fr.pantheonsorbonne.miage.game.tictactoe.command.TicTacToeStepTie;
import fr.pantheonsorbonne.miage.game.tictactoe.command.TicTacToeStepVictory;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;

import java.util.Random;

/**
 * This is an example for the host in the tictactoe game
 */
public final class TicTacToeHost {

    private TicTacToeHost() {
    }

    public static void main(String[] args) throws Exception, FullBoardException {
        // get the player facade, to interact with other player
        PlayerFacade playerFacade = (PlayerFacade) Facade.getFacade();
        // get the host facade, to manage the game
        HostFacade hostFacade = (HostFacade) Facade.getFacade();
        // wait until we are ready to use the host facade
        hostFacade.waitReady();
        // set our player name
        playerFacade.createNewPlayer("Nicolas" + new Random().nextInt());

        // play the game until the program quits
        while (true) {
            // creata a new game
            Game game = hostFacade.createNewGame("tictactoe2");
            // wait for another player to join
            hostFacade.waitForExtraPlayerCount(2);
            // play the game using the player facade
            playTheGame(playerFacade, game);

        }
    }

    private static void playTheGame(PlayerFacade playerFacade, Game game) throws FullBoardException {
        // create a new board
        TicTacToe board = new TicTacToeImpl(6);
        // I'll be X, the other player will be O
        char myMark = 'X';
        // send its mark to the other player
        playerFacade.sendGameCommandToAll(game, new GameCommand(Command.YOU_ARE.name(), "O"));

        // loop until the game is other
        while (true) {

            // check if the game is over
            if (handleGameOver(playerFacade, game, board, myMark))
                break;

            // if the game is not over, use my mark on the board
            board.addRand(myMark);
            System.out.println("-----------------------\n" + board);
            // send the board to the other player
            playerFacade.sendGameCommandToAll(game, new GameCommand(Command.BOARD.name(), board.toFlatString()));

            // get the other player's move and retreive the board
            GameCommand command = playerFacade.receiveGameCommand(game);
            board = new TicTacToeImpl(command.body());
        }
    }

    private static boolean handleGameOver(PlayerFacade playerFacade, Game game, TicTacToe board, char myMark) {

        TicTacToeStep nextStep = null;
        // check if the game is over
        if (board.getWinner() == myMark) {
            // we've won :-)
            nextStep = new TicTacToeStepVictory(game, board, playerFacade);

        } else if (board.getWinner() == 'O') {
            // we've lost :-(
            nextStep = new TicTacToeStepDefeat(game, board, playerFacade);

        } else if (board.isFull()) {
            // it's a tie :-/
            nextStep = new TicTacToeStepTie(game, board, playerFacade);

        }

        if (nextStep == null) {
            // game is not over
            return false;
        } else {
            nextStep.perform();
            return true;
        }
    }

}

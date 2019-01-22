package com.epam.game.gamemodel.gamehandler;

import com.epam.game.gamemodel.model.GameInstance;

public class GameThread implements Runnable {

    private final long turnDelayMs;

    private GameInstance game;

    public GameThread(GameInstance game, long turnDelayMs) {
        this.game = game;
        this.turnDelayMs = turnDelayMs;
    }

    public void run() {
        try {
            game.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (game.isStarted() && !game.isFinished()) {            
            try {
                Thread.sleep(turnDelayMs);
                game.nextTurn();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("Exiting game thread");
    }
}

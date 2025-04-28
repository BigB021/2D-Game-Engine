package com;//package com;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.IOException;

import static constants.FramesConstants.WINDOW_HEIGHT;
import static constants.FramesConstants.WINDOW_WIDTH;

public class GameLauncher {
    public static void main(String[] args) throws IOException {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Test Game");
        config.setWindowedMode(WINDOW_WIDTH,WINDOW_HEIGHT);
        config.setForegroundFPS(120);

        // Start the game
        Game game = new Game();
        new Lwjgl3Application(game, config);
    }
}

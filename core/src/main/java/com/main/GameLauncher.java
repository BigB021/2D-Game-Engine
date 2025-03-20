package com.main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import utilities.Constants;

public class GameLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Test Game"); // Window title
        config.setWindowedMode(Constants.WIDTH, Constants.HEIGHT); // full screen
        config.setForegroundFPS(120); // Target FPS

        // Start the game
        Game game = new Game();
        new Lwjgl3Application(game, config);
    }
}

package com.main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class GameLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Test Game"); // Window title
        config.setWindowedMode(1920, 1080); // full screen
        config.setForegroundFPS(120); // Target FPS

        // Start the game
        Game game = new Game();
        new Lwjgl3Application(game, config);
    }
}

package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import core.Game;
import core.GameState;

public class GameScreen {
    private Game game;

    public GameScreen(final Game game) {
        this.game = game;
    }

    public void render(SpriteBatch batch) {
        // Vérifiez si le bouton pause est pressé
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setGameState(GameState.PAUSE);
        }

    }

    public void resize(int width, int height) {
        // Gérer le redimensionnement si nécessaire
    }

    public void dispose() {
        // Nettoyer les ressources si nécessaire
    }
}

package menu;

import core.Game;
import core.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static utilities.constants.FramesConstants.WINDOW_HEIGHT;
import static utilities.constants.FramesConstants.WINDOW_WIDTH;

public class GameOverScreen {
    private final Game game;
    private final Stage stage;
    private final Viewport viewport;
    private final BitmapFont font;
    private final TextButton.TextButtonStyle buttonStyle;
    private final ShapeRenderer shapeRenderer;

    public GameOverScreen(final Game game) {
        this.game = game;

        viewport = new FitViewport(WINDOW_WIDTH,WINDOW_HEIGHT);
        stage = new Stage(viewport);

        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.getData().setScale(2);

        // Style pour boutons
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.LIGHT_GRAY;

        // Style pour label
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.RED;

        Label gameOverLabel = new Label("GAME OVER", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Ajouter le label "Game Over"
        table.add(gameOverLabel).padBottom(60).row();

        // Boutons
        TextButton retryButton = new TextButton("Rejouer", buttonStyle);
        TextButton quitButton = new TextButton("Quitter", buttonStyle);

        table.add(retryButton).padBottom(20).row();

        table.add(quitButton).padBottom(20).row();

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.restartGame();
                game.setGameState(GameState.GAME_PLAYING);

            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.quitter(); // Quitter proprement
            }
        });
    }

    public void render(SpriteBatch batch) {
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.75f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}

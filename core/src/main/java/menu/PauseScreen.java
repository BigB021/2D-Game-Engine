package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import game.Game;
import game.GameState;

import static utilities.constants.FramesConstants.WINDOW_HEIGHT;
import static utilities.constants.FramesConstants.WINDOW_WIDTH;

public class PauseScreen {
    private Game game;
    private Stage stage;
    private Viewport viewport;
    private BitmapFont font;
    private TextButton.TextButtonStyle buttonStyle;
    private ShapeRenderer shapeRenderer;

    public PauseScreen(final Game game) {
        this.game = game;

        // Initialisation de la vue et du stage
        viewport = new FitViewport(WINDOW_WIDTH,WINDOW_HEIGHT);
        stage = new Stage(viewport);
        // Ne pas définir l'input processor ici
        // Gdx.input.setInputProcessor(stage); - RETIRÉ

        // Initialiser le shapeRenderer pour le fond semi-transparent
        shapeRenderer = new ShapeRenderer();

        // Initialisation de la police et du style du bouton
        font = new BitmapFont();
        font.getData().setScale(2);

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.LIGHT_GRAY;

        // Créez une table pour organiser les boutons
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Créez les boutons
        TextButton resumeButton = new TextButton("Resume", buttonStyle);
        TextButton optionsButton = new TextButton("Options", buttonStyle);
        TextButton mainMenuButton = new TextButton("Main menu", buttonStyle);

        // Ajoutez les boutons à la table
        table.add(resumeButton).padBottom(20).row();
        table.add(optionsButton).padBottom(20).row();
        table.add(mainMenuButton).padBottom(20).row();

        // Ajoutez des écouteurs pour les boutons
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setGameState(GameState.GAME_PLAYING);
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setGameState(GameState.OPTIONS);
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setGameState(GameState.MAIN_MENU);
            }
        });
    }

    public void render(SpriteBatch batch) {
        // Terminer le batch actuel pour dessiner l'overlay semi-transparent
        batch.end();

        // Dessiner un overlay semi-transparent
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f); // Couleur noire semi-transparente
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Redémarrer le batch pour le menu
        batch.begin();

        // Mettez à jour et dessinez le stage
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

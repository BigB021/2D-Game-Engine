package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
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

public class MainMenuScreen {
    private Game game;
    private Stage stage;
    private Viewport viewport;
    private BitmapFont font;
    private TextButton.TextButtonStyle buttonStyle;

    public MainMenuScreen(Game game) {
        this.game = game;

        // Créez un viewport qui s'adapte à l'écran
        viewport = new FitViewport(WINDOW_WIDTH,WINDOW_HEIGHT);
        stage = new Stage(viewport);
        // Ne pas définir l'input processor ici
        // Gdx.input.setInputProcessor(stage); - RETIRÉ

        // Créez une police et un style de bouton simple
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
        TextButton playButton = new TextButton("Play", buttonStyle);
        TextButton optionsButton = new TextButton("Options", buttonStyle);
        TextButton quitButton = new TextButton("Quit", buttonStyle);

        // Ajoutez les boutons à la table
        table.add(playButton).padBottom(20).row();
        table.add(optionsButton).padBottom(20).row();
        table.add(quitButton).padBottom(20).row();

        // Ajoutez des écouteurs pour les boutons
        playButton.addListener(new ClickListener() {
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

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.quitter();
            }
        });
    }

    public void render(SpriteBatch batch) {
        // Le SpriteBatch est déjà commencé et sera fini dans la méthode appelante

        Gdx.gl.glClearColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Mise à jour et rendu du stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        font.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}

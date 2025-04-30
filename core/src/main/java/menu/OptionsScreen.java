package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import core.Game;
import core.GameState;

public class OptionsScreen {
    private final Game game;
    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;
    private Stage stage;
    private Viewport viewport;
    private Label musicVolumeLabel;
    private Label soundVolumeLabel;

    public OptionsScreen(Game game) {
        this.game = game;
        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();

        // Initialisation de la vue et du stage
        viewport = new FitViewport(1600,760);
        stage = new Stage(viewport);

        // Configuration des styles de texte
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.LIGHT_GRAY;

        // Création des libellés pour les volumes avec des valeurs par défaut
        musicVolumeLabel = new Label("Music Volume: 0.00", labelStyle);
        soundVolumeLabel = new Label("Sound Volume: 0.00", labelStyle);

        // Création du bouton retour
        TextButton backButton = new TextButton("Resume", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setGameState(GameState.GAME_PLAYING);
            }
        });

        // Création de la table et ajout des composants
        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("OPTIONS", labelStyle)).padBottom(50).row();
        table.add(musicVolumeLabel).padBottom(10).row();
        table.add(new Label("Use UP/DOWN arrows to adjust music volume", labelStyle)).padBottom(30).row();
        table.add(soundVolumeLabel).padBottom(10).row();
        table.add(new Label("Use LEFT/RIGHT arrows to adjust sound volume", labelStyle)).padBottom(50).row();
        table.add(backButton).padBottom(20).row();

        stage.addActor(table);
    }
    public void render(SpriteBatch batch) {
        // Le batch est déjà commencé dans la méthode appelante
        Gdx.gl.glClearColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Mise à jour des libellés avec les valeurs actuelles
        musicVolumeLabel.setText("Music Volume: " + String.format("%.2f", game.getMusicController().getVolume()));
        soundVolumeLabel.setText("Sound Volume: " + String.format("%.2f", game.getSoundController().getMasterVolume()));

        // Mise à jour et rendu du stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Gestion des entrées clavier
        handleInput();
    }

    private void handleInput() {
        // MUSIC VOLUME
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            float vol = game.getMusicController().setVolume(game.getMusicController().getVolume() + 0.05f);
            Gdx.app.log("Options", "Music Volume: " + vol);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            float vol = game.getMusicController().setVolume(game.getMusicController().getVolume() - 0.05f);
            Gdx.app.log("Options", "Music Volume: " + vol);
        }

        // SOUND VOLUME
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            float vol = game.getSoundController().getMasterVolume() + 0.05f;
            game.getSoundController().setMasterVolume(vol);
            Gdx.app.log("Options", "Sound Volume: " + vol);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            float vol = game.getSoundController().getMasterVolume() - 0.05f;
            game.getSoundController().setMasterVolume(vol);
            Gdx.app.log("Options", "Sound Volume: " + vol);
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}

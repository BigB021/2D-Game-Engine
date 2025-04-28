package com;

import audio.MusicController;
import audio.SoundController;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import entities.Enemy;
import entities.Player;
import inputs.InputsManager;
import mapManager.tileManager.TileManager;
import menu.*;

import java.io.IOException;

import static constants.AudiConstants.*;
import static constants.EntityConstants.*;
import static constants.FramesConstants.FRAME_HEIGHT;
import static constants.FramesConstants.FRAME_WIDTH;
import static constants.MapTilesConstants.*;
import static constants.TextureConstants.PLAYER_IDLE_ANIMATION;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private Enemy enemy;

    private float animationTimer = 0f;
    public TileManager tileManager = new TileManager();
    // Testing hitBox
    private ShapeRenderer shape;
    private audio.MusicController musicController;
    private audio.SoundController soundController;
    private GameState currentState;
    // Simulons des "screens"
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    private OptionsScreen optionsScreen;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen;
    private InputsManager playerInput;

    //FIXME:



    public Game() throws IOException {
    }

    OrthographicCamera camera;
    Viewport viewport;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.zoom = CAMERA_ZOOM;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        tileManager.getTilesFromFolder();
        tileManager.initTiles();
        tileManager.loadBackground();
        int CalculatedHeight = (int) (FRAME_HEIGHT * camera.zoom);
        int CalculatedWidth = (int) (FRAME_WIDTH * camera.zoom);
        player = new Player(PLAYER_SPAWN_X, PLAYER_SPAWN_Y, CalculatedWidth, CalculatedHeight, 3.5, 10, tileManager);
        enemy = new Enemy(ENEMY_SPAWN_X, ENEMY_SPAWN_Y, CalculatedWidth, CalculatedHeight, 1., 5, player, tileManager);

        // Inputs initialization
        /*
        InputsManager playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

         */
        playerInput = new InputsManager(player);

        // Init player
        player.setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        player.setCooldown(player.getAttackAnimationDuration() * 1000);

        // Init enemy
        enemy.setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        System.out.println("Create Call");

        // Init ShapeRenderer
        shape = new ShapeRenderer();

        // Init batch
        batch = new SpriteBatch();




        //⚠️
        //currentState = GameState.MAIN_MENU;
        // Init screens
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        optionsScreen = new OptionsScreen(this);
        pauseScreen = new PauseScreen(this);
        gameOverScreen = new GameOverScreen(this);



        musicController = new MusicController();
        musicController.addMusic("BACKGROUND_MUSIC", musicController.generateMusicFromPath(BACKGROUND_MUSIC));
        musicController.addMusic("HORROR_SCENE", musicController.generateMusicFromPath(HORROR_SCENE));
        musicController.playMusic("HORROR_SCENE", 0.5f, false);
        //musicController.playMusic("BACKGROUND_MUSIC", 0.1f, true);

        /*
        if(currentState == GameState.GAME_PLAYING){
            musicController.playMusic("BACKGROUND_MUSIC",0.5f,  true);
        }else{
            musicController.playMusic("RUNNING_SOUND",0.5f,  true);
        }

         */


        soundController = new SoundController();
        soundController.addNewSound("RUNNING_SOUND", soundController.generateSoundFromPath(RUNNING_SOUND));

        setGameState(GameState.MAIN_MENU);

    }

    @Override
    public void render() {
        float mapWidth = MAX_SCREEN_COL * TILE_SIZE;
        float mapHeight = MAX_SCREEN_ROW * TILE_SIZE;

        float screenwidth = camera.zoom * mapWidth;
        float screenheight = camera.zoom * mapHeight;
        float playerCenterX = player.getX() + player.getHitBox().width / 2f;
        float playerCenterY = player.getHitBox().y + player.getHitBox().height / 2f;

        float halfViewportWidth = camera.viewportWidth * camera.zoom / 2f;
        float halfViewportHeight = camera.viewportHeight * camera.zoom / 2f;

        camera.position.x = Math.max(halfViewportWidth, Math.min(playerCenterX, mapWidth - halfViewportWidth));
        camera.position.y = Math.max(halfViewportHeight, Math.min(playerCenterY, mapHeight - halfViewportHeight));
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        // Set animation timer to current time
        animationTimer += Gdx.graphics.getDeltaTime();

        // Check if enough time has passed
        float FRAME_DELAY = 0.1f;
        if (animationTimer >= FRAME_DELAY) {
            player.setAnimation_index(player.getAnimation_index() + FRAME_WIDTH);
            if (player.getAnimation_index() > player.getSprite().getWidth() - FRAME_WIDTH) player.setAnimation_index(0);
            enemy.setAnimation_index(enemy.getAnimation_index() + FRAME_WIDTH);
            if (enemy.getAnimation_index() > enemy.getSprite().getWidth() - FRAME_WIDTH) enemy.setAnimation_index(0);
            animationTimer = 0f;  // Reset timer
        }
        ScreenUtils.clear(0.8f, 0.85f, 0.8f, 0.00f);

        // Effectuer le rendu du jeu uniquement quand on est en mode GAME_PLAYING
        if(currentState == GameState.GAME_PLAYING) {
            // Mettre à jour la logique du jeu
            player.movePlayer();
            enemy.moveEnemy();

            // Rendre le fond et les éléments de jeu
            batch.begin();
            shape.begin(ShapeRenderer.ShapeType.Line);
            tileManager.render(batch, camera, shape);
            shape.end();

            TextureRegion playerRegion = player.loadAnimation(player.getAnimation_index(), 0, (FRAME_WIDTH), FRAME_HEIGHT);
            TextureRegion enemyRegion = enemy.loadAnimation(enemy.getAnimation_index(), 0, FRAME_WIDTH, FRAME_HEIGHT);
            batch.draw(playerRegion, player.getX(), player.getY(), FRAME_WIDTH * camera.zoom, FRAME_HEIGHT * camera.zoom);
            batch.draw(enemyRegion, (int) enemy.getX(), (int) enemy.getY(), FRAME_WIDTH * camera.zoom, FRAME_HEIGHT * camera.zoom);
            batch.end();

            // Rendre les hitboxes pour le debug
            shape.setProjectionMatrix(camera.combined);

            // Debug: Draw player hitBox rect
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.BLUE);
            shape.rect(player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);
            shape.end();

            // Debug: Draw Enemy hitBox rect
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.RED);
            shape.rect(enemy.getHitBox().x, enemy.getHitBox().y, enemy.getHitBox().width, enemy.getHitBox().height);
            shape.end();

            // Debug: Draw Player attack hitBox
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.YELLOW);
            shape.rect(player.getAttackHitBox().x, player.getAttackHitBox().y, player.getAttackHitBox().width, player.getAttackHitBox().height);
            shape.end();

            // Debug: Draw Enemy attack hitBox
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.GREEN);
            shape.rect(enemy.getAttackHitBox().x, enemy.getAttackHitBox().y, enemy.getAttackHitBox().width, enemy.getAttackHitBox().height);
            shape.end();
        }

        if(player.isDead()){
            setGameState(GameState.GAME_OVER);
        }



        // Maintenant, on rend les UI des différents écrans avec un nouveau begin/end
        batch.begin();
        switch (currentState) {
            case MAIN_MENU:
                mainMenuScreen.render(batch);
                break;
            case GAME_PLAYING:
                gameScreen.render(batch);
                break;
            case OPTIONS:
                optionsScreen.render(batch);
                break;
            case PAUSE:
                pauseScreen.render(batch);
                break;
            case GAME_OVER:
                gameOverScreen.render(batch);
                break;
            default:
                break;
        }
        batch.end();
    }


    // todo : ⚠️ We Can use this methode to control menu's music


/*
    public void setGameState(GameState state) {
        // Handle music changes based on state transitions
        if (state == GameState.GAME_PLAYING && currentState != GameState.GAME_PLAYING) {
            musicController.stopMusic();
            musicController.playMusic("BACKGROUND_MUSIC", 0.5f, true);
        } else if (state != GameState.GAME_PLAYING && currentState == GameState.GAME_PLAYING) {
            musicController.stopMusic();
            musicController.playMusic("RUNNING_SOUND", 0.5f, true);
        }

        this.currentState = state;
    }



 */
/*
    public void setGameState(GameState state) {
        this.currentState = state;
    }

 */
/* setGameState Version 3
    public void setGameState(GameState state) {
// Handle input processor changes
        if (state == GameState.MAIN_MENU) {
            Gdx.input.setInputProcessor(mainMenuScreen.getStage());
            musicController.stopMusic();
            musicController.playMusic("HORROR_SCENE", 0.5f, true);
        } else if (state == GameState.PAUSE) {
            Gdx.input.setInputProcessor(pauseScreen.getStage());
        } else if (state == GameState.OPTIONS) {
            Gdx.input.setInputProcessor(optionsScreen.getStage());
        } else if (state == GameState.GAME_PLAYING) {
            Gdx.input.setInputProcessor(playerInput);
            if (currentState != GameState.PAUSE) {
                musicController.stopMusic();
                musicController.playMusic("BACKGROUND_MUSIC", 0.5f, true);
            }
        }else if(currentState == GameState.GAME_OVER){
            Gdx.input.setInputProcessor(optionsScreen.getStage());
            musicController.stopMusic();
            musicController.playMusic("HORROR_SCENE", 0.5f, true);
        }

        this.currentState = state;
    }

 */

    public void setGameState(GameState state) {
        switch (state) {
            case MAIN_MENU:
                Gdx.input.setInputProcessor(mainMenuScreen.getStage());
                musicController.stopMusic();
                musicController.playMusic("HORROR_SCENE", 0.5f, true);
                break;
            case PAUSE:
                Gdx.input.setInputProcessor(pauseScreen.getStage());
                break;
            case OPTIONS:
                Gdx.input.setInputProcessor(optionsScreen.getStage());
                break;
            case GAME_PLAYING:
                Gdx.input.setInputProcessor(playerInput);
                if (currentState != GameState.PAUSE) {
                    musicController.stopMusic();
                    musicController.playMusic("BACKGROUND_MUSIC", 0.5f, true);
                }
                break;
            case GAME_OVER:
                Gdx.input.setInputProcessor(gameOverScreen.getStage());
                playerInput.getSoundController().dispose();
                musicController.stopMusic();
                musicController.playMusic("HORROR_SCENE", 0.5f, true);
                break;
        }

        this.currentState = state;
    }




    public void quitter() {
        Gdx.app.exit();
        System.exit(0);
    }

    public MusicController getMusicController() {
        return musicController;
    }

    public SoundController getSoundController() {
        return soundController;
    }

    public Player getPlayer(){
        return player;
    }



    public void restartGame() {
        // Réinitialiser les objets essentiels
        int CalculatedHeight = (int) (FRAME_HEIGHT * camera.zoom);
        int CalculatedWidth = (int) (FRAME_WIDTH * camera.zoom);
        player = new Player(PLAYER_SPAWN_X, PLAYER_SPAWN_Y, CalculatedWidth, CalculatedHeight, 3.5, 10, tileManager);
        enemy = new Enemy(ENEMY_SPAWN_X, ENEMY_SPAWN_Y, CalculatedWidth, CalculatedHeight, 1.0, 5, player, tileManager);

        player.setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        player.setCooldown(player.getAttackAnimationDuration() * 1000);

        enemy.setSprite(new Texture(PLAYER_IDLE_ANIMATION));

        animationTimer = 0f;

        playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

        setGameState(GameState.GAME_PLAYING);
    }



    @Override
    public void dispose() {
        try {
            if (batch != null) {
                batch.dispose();
                mainMenuScreen.dispose();
                gameScreen.dispose();
                optionsScreen.dispose();
                pauseScreen.dispose();
                gameOverScreen.dispose();
            }
        } catch (Exception e) {
            Gdx.app.error("GameMain", "Error during disposal", e);
        }
    }
}

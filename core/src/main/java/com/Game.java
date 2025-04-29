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

import static utilities.constants.EntityConstants.*;
import static utilities.constants.MapTilesConstants.*;
import static utilities.constants.TextureConstants.*;
import static utilities.constants.FramesConstants.FRAME_HEIGHT;
import static utilities.constants.FramesConstants.FRAME_WIDTH;

import static utilities.constants.AudiConstants.*;
import static utilities.constants.EntityConstants.*;
import static utilities.constants.FramesConstants.FRAME_HEIGHT;
import static utilities.constants.FramesConstants.FRAME_WIDTH;
import static utilities.constants.MapTilesConstants.*;
import static utilities.constants.TextureConstants.PLAYER_IDLE_ANIMATION;

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



    private OrthographicCamera camera;
    public Viewport viewport;

    /**
     * Empty Constructor (removable)
     */
    public Game() {
    }
    OrthographicCamera camera;
    Viewport viewport;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.zoom = CAMERA_ZOOM;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        // Initialize tiles and load backgrounds
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

        // Inputs initialization
        InputsManager playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

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

    /**
     * The main game loop that updates the game state, applies gravity, and renders the game screen.
     */
    @Override
    public void render() {
        // Calculate the map size and player's center position for camera control
        float mapWidth = MAX_SCREEN_COL * TILE_SIZE;
        float mapHeight = MAX_SCREEN_ROW * TILE_SIZE;
        float playerCenterX = player.getX() + player.getHitBox().width / 2f;
        float playerCenterY = player.getHitBox().y + player.getHitBox().height / 2f;

        float halfViewportWidth = camera.viewportWidth * camera.zoom / 2f;
        float halfViewportHeight = camera.viewportHeight * camera.zoom / 2f;

        camera.position.x = Math.max(halfViewportWidth, Math.min(playerCenterX, mapWidth - halfViewportWidth));
        camera.position.y = Math.max(halfViewportHeight, Math.min(playerCenterY, mapHeight - halfViewportHeight));
        camera.update();

////// we can implement a camera but for now we stick to this
        System.out.println("centerX "+playerCenterX+ "CAMERAx "+ camera.position.x+"viewport "+viewport.getScreenWidth());

        batch.setProjectionMatrix(camera.combined);




        // Set animation timer to current time
        animationTimer += Gdx.graphics.getDeltaTime();

        // Apply gravity to player and enemy
        GravityPhysics.applyGravity(player);
        GravityPhysics.applyGravity(enemy);

        // Update animation frames if enough time has passed
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

            // Rendre le fond et les éléments de jeu
            batch.begin();
            shape.begin(ShapeRenderer.ShapeType.Line);
            tileManager.render(batch,camera,shape);
            shape.end();

            // Draw player and enemy animations
            TextureRegion playerRegion = player.loadAnimation(player.getAnimation_index(), 0, (FRAME_WIDTH), FRAME_HEIGHT);
            TextureRegion enemyRegion = enemy.loadAnimation(enemy.getAnimation_index(),0,FRAME_WIDTH, FRAME_HEIGHT);
            batch.draw(playerRegion, player.getX(),player.getY(), FRAME_WIDTH*camera.zoom, FRAME_HEIGHT*camera.zoom);
            batch.draw(enemyRegion,(int)enemy.getX(),(int) enemy.getY(), FRAME_WIDTH*camera.zoom, FRAME_HEIGHT*camera.zoom);

            player.movePlayer();
            enemy.moveEnemy();

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


        // Debug: Draw Entities hitboxes
        renderDebugHitboxes();


    }

    /**
     * Renders debug hitboxes for player and enemy.
     */
    private void renderDebugHitboxes() {
        shape.begin(ShapeRenderer.ShapeType.Line);

        // Player hitbox
        shape.setColor(Color.BLUE);
        shape.rect(player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);
            // Debug: Draw player hitBox rect
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.BLUE);
            shape.rect(player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);

        // Enemy hitbox
        shape.setColor(Color.RED);
        shape.rect(enemy.getHitBox().x, enemy.getHitBox().y, enemy.getHitBox().width, enemy.getHitBox().height);

        // Player attack hitbox
        shape.setColor(Color.YELLOW);
        shape.rect(player.getAttackHitBox().x, player.getAttackHitBox().y, player.getAttackHitBox().width, player.getAttackHitBox().height);

        // Enemy attack hitbox
        shape.setColor(Color.GREEN);
        shape.rect(enemy.getAttackHitBox().x, enemy.getAttackHitBox().y, enemy.getAttackHitBox().width, enemy.getAttackHitBox().height);

        shape.end();




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



    /**
     * Disposes of game resources to free up memory.
     */
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
}}

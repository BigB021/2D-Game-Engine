package core;

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
import menu.*;
import tileManager.TileManager;
import physics.GravityPhysics;

import java.util.ArrayList;

import static utilities.constants.EntityConstants.*;
import static utilities.constants.MapTilesConstants.*;
import static utilities.constants.TextureConstants.*;
import static utilities.constants.FramesConstants.FRAME_HEIGHT;
import static utilities.constants.FramesConstants.FRAME_WIDTH;
import static utilities.constants.AudiConstants.*;

/**
 * Main game class that extends ApplicationAdapter. Handles game initialization, rendering, and cleanup.
 */
public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    //private Enemy enemy1;
    //private Enemy enemy2;
    private ArrayList<Enemy> enemies;
    private float animationTimer = 0f;
    private float cords ;
    public TileManager tileManager = new TileManager();

    // Testing hitBox
    private ShapeRenderer shape;
    private MusicController musicController;
    private SoundController soundController;
    private GameState currentState;

    // Screens for different game states
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    private OptionsScreen optionsScreen;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen;
    private InputsManager playerInput;

    private OrthographicCamera camera;
    public Viewport viewport;
    private int enemy_num = 2;
    private int[] coordsX = {ENEMY_SPAWN_X,ENEMY2_SPAWN_X};
    private int[] coordsY = {ENEMY_SPAWN_Y,ENEMY2_SPAWN_Y};



    /**
     * Empty Constructor
     */
    public Game() {
    }

    /**
     * Initializes the game, setting up the camera, player, enemy, tile manager, and UI screens.
     */
    @Override
    public void create() {

        // Initialize camera and viewport
        camera = new OrthographicCamera();
        camera.zoom = CAMERA_ZOOM;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        // Initialize tiles and load backgrounds
        tileManager.getTilesFromFolder();
        tileManager.initTiles();
        tileManager.loadBackground();

        // Calculate dimensions based on camera zoom
        int CalculatedHeight = (int) (FRAME_HEIGHT * camera.zoom);
        int CalculatedWidth = (int) (FRAME_WIDTH * camera.zoom);

        // Initialize player and enemy
        player = new Player(PLAYER_SPAWN_X, PLAYER_SPAWN_Y, CalculatedWidth, CalculatedHeight, 3.5, 10, tileManager);

        //Init enemies array
        enemies = new ArrayList<>();

        for (int i = 0; i < enemy_num; i++) {
            enemies.add(new Enemy(coordsX[i], coordsY[i], CalculatedWidth, CalculatedHeight, 1.0, 5, player, tileManager));
        }

        // Inputs initialization
        playerInput = new InputsManager(player);

        // Init player and enemy sprites
        player.setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        player.setCooldown(player.getAttackAnimationDuration() * 1000);
        for (int i = 0; i < enemy_num; i++) {
            enemies.get(i).setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        }

        // Init ShapeRenderer and batch
        shape = new ShapeRenderer();
        batch = new SpriteBatch();

        // Init screens
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        optionsScreen = new OptionsScreen(this);
        pauseScreen = new PauseScreen(this);
        gameOverScreen = new GameOverScreen(this);

        // Initialize audio
        musicController = new MusicController();
        musicController.addMusic("BACKGROUND_MUSIC", musicController.createMusicFile(BACKGROUND_MUSIC));
        musicController.addMusic("HORROR_SCENE", musicController.createMusicFile(HORROR_SCENE));

        soundController = new SoundController();
        soundController.addNewSound("RUNNING_SOUND", soundController.generateSoundFromFile(RUNNING_SOUND));

        // Set initial game state to main menu
        setGameState(GameState.MAIN_MENU);
    }

    /**
     * The main game loop that updates the game state, applies gravity, and renders the game screen.
     */
    @Override
    public void render() {

        // Calculate the map size and camera boundaries
        float mapWidth = MAX_SCREEN_COL * TILE_SIZE;
        float mapHeight = MAX_SCREEN_ROW * TILE_SIZE;
        float playerCenterX = player.getX() + player.getHitBox().width / 2f;
        float playerCenterY = player.getHitBox().y + player.getHitBox().height / 2f;

        float halfViewportWidth = camera.viewportWidth * camera.zoom / 2f;
        float halfViewportHeight = camera.viewportHeight * camera.zoom / 2f;

        // Adjust camera position to follow player within map boundaries
        camera.position.x = Math.max(halfViewportWidth, Math.min(playerCenterX, mapWidth - halfViewportWidth));
        camera.position.y = Math.max(halfViewportHeight, Math.min(playerCenterY, mapHeight - halfViewportHeight));
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        for (int i = 0; i <tileManager.getOverlappingTiles(0,player).size() ; i++) {
            if (tileManager.getOverlappingTiles(0,player).get(i).prototype!=null){


            if (tileManager.getOverlappingTiles(0,player).get(i)
                .prototype.image.toString()
                .equals("assets/tilesAssets/tiles/190.png")){
                // debug
                // System.out.println("les/190.png "+tileManager.getOverlappingTiles(0,player).get(i).collisionBox.getX());
                // System.out.println("cords X "+cords);
                setCords(tileManager.getOverlappingTiles(0,player).get(i).collisionBox.getX());

            }
            }
        }
        //System.out.println("Player coords:"+ player.getX() + "," + player.getY());
        // Update animation timer
        animationTimer += Gdx.graphics.getDeltaTime();

        // Update animation frames if enough time has passed
        float FRAME_DELAY = 0.1f;
        if (animationTimer >= FRAME_DELAY) {
            player.setAnimation_index(player.getAnimation_index() + FRAME_WIDTH);
            if (player.getAnimation_index() > player.getSprite().getWidth() - FRAME_WIDTH) player.setAnimation_index(0);
            for (Enemy enemy : enemies) {
                enemy.setAnimation_index(enemy.getAnimation_index() + FRAME_WIDTH);
                if (enemy.getAnimation_index() > enemy.getSprite().getWidth() - FRAME_WIDTH)
                    enemy.setAnimation_index(0);
            }

            animationTimer = 0f;  // Reset timer
        }

        // clears screen
        ScreenUtils.clear(0.f, 0.f, 0.f, 0.00f);

        // Render game elements only when in GAME_PLAYING state
        if (currentState == GameState.GAME_PLAYING) {
            // Apply gravity to player and enemy and update their position
            GravityPhysics.applyGravity(player);
            player.movePlayer();



            // Render background and game elements
            batch.begin();
            shape.begin(ShapeRenderer.ShapeType.Line);
            tileManager.render(batch, camera, shape);
            shape.end();

            TextureRegion[] enemiesRegions = new TextureRegion[enemies.size()];
            for (int i = 0; i < enemies.size(); i++) {
                GravityPhysics.applyGravity(enemies.get(i));
                enemies.get(i).moveEnemy();
                enemiesRegions[i] = enemies.get(i).loadAnimation(enemies.get(i).getAnimation_index(), 0, FRAME_WIDTH, FRAME_HEIGHT);
                batch.draw(enemiesRegions[i], enemies.get(i).getX(), enemies.get(i).getY(), FRAME_WIDTH * camera.zoom, FRAME_HEIGHT * camera.zoom);

            }
            // Draw player and enemy animations
            TextureRegion playerRegion = player.loadAnimation(player.getAnimation_index(), 0, (FRAME_WIDTH), FRAME_HEIGHT);
            //TextureRegion enemy2Region = enemy2.loadAnimation(enemy2.getAnimation_index(), 0, FRAME_WIDTH, FRAME_HEIGHT);
            batch.draw(playerRegion, player.getX(), player.getY(), FRAME_WIDTH * camera.zoom, FRAME_HEIGHT * camera.zoom);
            //batch.draw(enemy2Region, enemy2.getX(), enemy2.getY(), FRAME_WIDTH * camera.zoom, FRAME_HEIGHT * camera.zoom);

            if(player.getHitBox().y <= 40){
                player.setDead(true);
            }

            // Dispose enemy if deadx
            enemies.removeIf(Enemy::isDead);


            batch.end();

            // Render debug hitboxes
            renderDebugHitboxes();
        }

        // Check if player is dead and update game state

        if (player.isDead()) {
            setGameState(GameState.GAME_OVER);
        }

        // Render UI for current game state
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

    /**
     * Renders debug hitboxes for player and enemy.
     */
    private void renderDebugHitboxes() {
        shape.setProjectionMatrix(camera.combined);

        // Player hitbox
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.BLUE);
        shape.rect(player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);
        shape.end();

        // Enemy hitbox
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.RED);
        for (int i = 0; i < enemies.size(); i++) {
            shape.rect(enemies.get(i).getHitBox().x, enemies.get(i).getHitBox().y, enemies.get(i).getHitBox().width, enemies.get(i).getHitBox().height);

        }        shape.end();

        // Player attack hitbox
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.YELLOW);
        shape.rect(player.getAttackHitBox().x, player.getAttackHitBox().y, player.getAttackHitBox().width, player.getAttackHitBox().height);
        shape.end();

        // Enemy attack hitbox
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.GREEN);
        for (int i = 0; i < enemies.size(); i++) {
            shape.rect(enemies.get(i).getAttackHitBox().x, enemies.get(i).getAttackHitBox().y, enemies.get(i).getAttackHitBox().width, enemies.get(i).getAttackHitBox().height);

        }
        shape.end();
    }

    /**
     * Updates the current game state and handles transitions.
     *
     * @param state The new game state
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
                    musicController.playMusic("BACKGROUND_MUSIC", musicController.getVolume(), true);
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

    /**
     * Quits the game and exits the application.
     */
    public void quitter() {
        Gdx.app.exit();
        System.exit(0);
    }

    /**
     * Restarts the game by reinitializing player and enemy.
     */
    public void restartGame() {
        // Reinitialize essential objects
        int CalculatedHeight = (int) (FRAME_HEIGHT * camera.zoom);
        int CalculatedWidth = (int) (FRAME_WIDTH * camera.zoom);
        player = new Player((int)getCords(), PLAYER_SPAWN_Y, CalculatedWidth, CalculatedHeight, 3.5, 10, tileManager);

        player.setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        player.setCooldown(player.getAttackAnimationDuration() * 1000);
        //System.out.println(getCords());
        player.setX(getCords());
        for (int i = 0; i < enemies.size(); i++) {
            enemies.set(i, new Enemy(coordsX[i], coordsY[i], CalculatedWidth, CalculatedHeight, 1.0, 5, player, tileManager));
            enemies.get(i).setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        }


        animationTimer = 0f;

        playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

        setGameState(GameState.GAME_PLAYING);
    }

    /**
     *  Restarts the game when player dies
     * @param coords : player checkpoint coordinates
     */
    public void restartGame(float coords) {
        // Reinitialize essential objects
        int CalculatedHeight = (int) (FRAME_HEIGHT * camera.zoom);
        int CalculatedWidth = (int) (FRAME_WIDTH * camera.zoom);
        System.out.println(getCords());
        player = new Player((int)getCords(), PLAYER_SPAWN_Y, CalculatedWidth, CalculatedHeight, 3.5, 10, tileManager);
        player.setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        player.setCooldown(player.getAttackAnimationDuration() * 1000);

        for (int i = 0; i < enemies.size(); i++) {
            enemies.set(i, new Enemy(coordsX[i], coordsY[i], CalculatedWidth, CalculatedHeight, 1.0, 5, player, tileManager));
            enemies.get(i).setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        }

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

    //===================== Getters & Setters =====================
    public MusicController getMusicController() {
        return musicController;
    }

    public SoundController getSoundController() {
        return soundController;
    }

    public Player getPlayer() {
        return player;
    }

    public float getCords() {
        return cords;
    }

    public void setCords(float value) {
        cords = value;
    }
    //=============================================================
}

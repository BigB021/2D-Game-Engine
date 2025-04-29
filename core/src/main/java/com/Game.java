package com;

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
import tileManager.TileManager;
import physics.GravityPhysics;

import static utilities.constants.EntityConstants.*;
import static utilities.constants.MapTilesConstants.*;
import static utilities.constants.TextureConstants.*;
import static utilities.constants.FramesConstants.FRAME_HEIGHT;
import static utilities.constants.FramesConstants.FRAME_WIDTH;

/**
 * Main game class that extends ApplicationAdapter. Handles game initialization, rendering, and cleanup.
 */
public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private  Player player;
    private Enemy enemy;
    private float animationTimer = 0f;
    public TileManager tileManager = new TileManager();

    // Testing hitBox
    private ShapeRenderer shape;

    private OrthographicCamera camera;
    public Viewport viewport;

    /**
     * Empty Constructor (removable)
     */
    public Game() {
    }

    /**
     * Initializes the game, setting up the camera, player, enemy, and tile manager.
     */
    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.zoom = CAMERA_ZOOM;
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        viewport = new FitViewport(SCREEN_WIDTH,SCREEN_HEIGHT,camera);

        // Initialize tiles and load backgrounds
        tileManager.getTilesFromFolder();
        tileManager.initTiles();
        tileManager.loadBackground();

        int CalculatedHeight = (int)(FRAME_HEIGHT * camera.zoom);
        int CalculatedWidth = (int)(FRAME_WIDTH * camera.zoom);

        // Initialize player and enemy
        player = new Player(PLAYER_SPAWN_X, PLAYER_SPAWN_Y,CalculatedWidth,CalculatedHeight,3.5,10,tileManager);
        enemy = new Enemy(ENEMY_SPAWN_X,ENEMY_SPAWN_Y,CalculatedWidth,CalculatedHeight,1., 5,player,tileManager);
        player.setSprite(new Texture(PLAYER_IDLE_ANIMATION));
        player.setCooldown(player.getAttackAnimationDuration() * 1000);
        enemy.setSprite(new Texture(PLAYER_IDLE_ANIMATION));

        // Inputs initialization
        InputsManager playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

        // Init ShapeRenderer
        shape = new ShapeRenderer();

        // Init batch
        batch = new SpriteBatch();

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

        // Adjust camera position to follow player within map boundaries
        camera.position.x = Math.max(halfViewportWidth, Math.min(playerCenterX, mapWidth - halfViewportWidth));
        camera.position.y = Math.max(halfViewportHeight, Math.min(playerCenterY, mapHeight - halfViewportHeight));
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        // Update animation timer to current time
        animationTimer += Gdx.graphics.getDeltaTime();

        // Apply gravity to player and enemy
        GravityPhysics.applyGravity(player);
        GravityPhysics.applyGravity(enemy);

        // Update animation frames if enough time has passed
        float FRAME_DELAY = 0.1f;
        if (animationTimer >= FRAME_DELAY) {
            player.setAnimation_index(player.getAnimation_index()+ FRAME_WIDTH);
            if (player.getAnimation_index() > player.getSprite().getWidth() - FRAME_WIDTH) player.setAnimation_index(0);
            enemy.setAnimation_index(enemy.getAnimation_index()+ FRAME_WIDTH);
            if (enemy.getAnimation_index() > enemy.getSprite().getWidth() - FRAME_WIDTH) enemy.setAnimation_index(0);
            animationTimer = 0f;  // Reset timer
        }

        // Clear the screen with a color
        //ScreenUtils.clear(0.8f, 0.85f, 0.8f, 0.00f);

        // Start drawing textures
        batch.begin();

        // Render tiles and hitboxes
        shape.begin(ShapeRenderer.ShapeType.Line);
        tileManager.render(batch,camera,shape);
        shape.end();

        // Draw player and enemy animations
        TextureRegion playerRegion = player.loadAnimation(player.getAnimation_index(), 0, (FRAME_WIDTH), FRAME_HEIGHT);
        TextureRegion enemyRegion = enemy.loadAnimation(enemy.getAnimation_index(),0,FRAME_WIDTH, FRAME_HEIGHT);
        batch.draw(playerRegion, player.getX(),player.getY(), FRAME_WIDTH*camera.zoom, FRAME_HEIGHT*camera.zoom);
        batch.draw(enemyRegion,(int)enemy.getX(),(int) enemy.getY(), FRAME_WIDTH*camera.zoom, FRAME_HEIGHT*camera.zoom);

        // Move player and enemy based on their respective logic
        player.movePlayer();
        enemy.moveEnemy();

        batch.end();
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

        // Debug: Log player health status
        //System.out.println("Player health: " + player.getEntityHealth());
        //System.out.println("Is player dead: " + player.isDead());
    }

    /**
     * Disposes of game resources to free up memory.
     */
    @Override
    public void dispose() {
        try {
            if (batch != null) {
                batch.dispose();
            }
        } catch (Exception e) {
            Gdx.app.error("GameMain", "Error during disposal", e);
        }
    }
}

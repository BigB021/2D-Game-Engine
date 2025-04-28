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
import mapManager.tileManager.TileManager;
import physics.GravityPhysics;
import physics.JumpPhysics;

import java.io.IOException;

import static constants.EntityConstants.*;
import static constants.FramesConstants.FRAME_HEIGHT;
import static constants.FramesConstants.FRAME_WIDTH;
import static constants.MapTilesConstants.*;
import static constants.TextureConstants.PLAYER_IDLE_ANIMATION;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private  Player player;
    private Enemy enemy;
    private float animationTimer = 0f;
    public TileManager tileManager = new TileManager();
    // Testing hitBox
    private ShapeRenderer shape;

    public Game() throws IOException {
    }
    OrthographicCamera camera;
    Viewport viewport;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.zoom = CAMERA_ZOOM;
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        viewport = new FitViewport(SCREEN_WIDTH,SCREEN_HEIGHT,camera);

        tileManager.getTilesFromFolder();
        tileManager.initTiles();
        tileManager.loadBackground();
        int CalculatedHeight=(int)(FRAME_HEIGHT*camera.zoom);
        int CalculatedWidth=(int)(FRAME_WIDTH*camera.zoom);
        player = new Player(PLAYER_SPAWN_X, PLAYER_SPAWN_Y,CalculatedWidth,CalculatedHeight,3.5,10,tileManager);
        enemy = new Enemy(ENEMY_SPAWN_X,ENEMY_SPAWN_Y,CalculatedWidth,CalculatedHeight,1., 5,player,tileManager);

        // Inputs initialization
        InputsManager playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

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

    }

    @Override
    public void render() {
        float mapWidth = MAX_SCREEN_COL * TILE_SIZE;
        float mapHeight = MAX_SCREEN_ROW * TILE_SIZE;
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

        GravityPhysics.applyGravity(player);
        GravityPhysics.applyGravity(enemy);

        // Check if enough time has passed
        float FRAME_DELAY = 0.1f;
        if (animationTimer >= FRAME_DELAY) {
            player.setAnimation_index(player.getAnimation_index()+ FRAME_WIDTH);
            if (player.getAnimation_index() > player.getSprite().getWidth() - FRAME_WIDTH) player.setAnimation_index(0);
            enemy.setAnimation_index(enemy.getAnimation_index()+ FRAME_WIDTH);
            if (enemy.getAnimation_index() > enemy.getSprite().getWidth() - FRAME_WIDTH) enemy.setAnimation_index(0);
            animationTimer = 0f;  // Reset timer
        }
        ScreenUtils.clear(0.8f, 0.85f, 0.8f, 0.00f);

        batch.begin();

        shape.begin(ShapeRenderer.ShapeType.Line);
        tileManager.render(batch,camera,shape);
        shape.end();
        TextureRegion playerRegion = player.loadAnimation(player.getAnimation_index(), 0, (FRAME_WIDTH), FRAME_HEIGHT);
        TextureRegion enemyRegion = enemy.loadAnimation(enemy.getAnimation_index(),0,FRAME_WIDTH, FRAME_HEIGHT);
        batch.draw(playerRegion, player.getX(),player.getY(), FRAME_WIDTH*camera.zoom, FRAME_HEIGHT*camera.zoom);
        batch.draw(enemyRegion,(int)enemy.getX(),(int) enemy.getY(), FRAME_WIDTH*camera.zoom, FRAME_HEIGHT*camera.zoom);
        player.movePlayer();
        enemy.moveEnemy();

        batch.end();
        shape.setProjectionMatrix(camera.combined);

        // Debug: Gravity
        System.out.println("Grounded"+ player.isGrounded());

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

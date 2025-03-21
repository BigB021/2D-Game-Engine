package com.main;

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
import mapManager.tileManager.Tile;
import mapManager.tileManager.TileManager;
import utilities.Constants;

import java.io.IOException;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private  Player player;
    private Enemy enemy;
    private float animationTimer = 0f;
    private final float FRAME_DELAY = 0.1f;
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
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        viewport = new FitViewport(Constants.screenWidth,Constants.screenHeight,camera);

        tileManager.getTilesFromFolder();
        tileManager.gettileimage();
        int x = 140, y = 210;
        player = new Player(x+500,y,Constants.FRAME_WIDTH,Constants.FRAME_HEIGHT,3.5);
        enemy = new Enemy(x,y,Constants.FRAME_WIDTH,Constants.FRAME_HEIGHT,1., player);

        // Inputs initialization
        InputsManager playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

        // Init player
        player.setSprite(new Texture(Constants.IDLE_ANIMATION));
        player.setCooldown(player.getAttackAnimationDuration() * 1000);

        // Init enemy
        enemy.setSprite(new Texture(Constants.IDLE_ANIMATION));
        System.out.println("Create Call");

        // Init ShapeRenderer
        shape = new ShapeRenderer();

        // Init batch
        batch = new SpriteBatch();

    }

    @Override
    public void render() {
        float mapWidth = Constants.maxScreenCol * Constants.tileSize;
        float mapHeight = Constants.maxScreenrow * Constants.tileSize;
        camera.position.x = Math.max(camera.viewportWidth / 2, Math.min(player.getX(), mapWidth - camera.viewportWidth / 2));
        camera.position.y = Math.max(camera.viewportHeight / 2, Math.min(player.getY(), mapHeight - camera.viewportHeight / 2));

        camera.update();
////// we can implement a camera but for now we stick to this
        System.out.println(player.getX());
        camera.zoom = 1f;

        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0.5f, 0.15f, 0.2f, 1f);

        player.movePlayer();
        enemy.moveEnemy();

        // Set animation timer to current time
        animationTimer += Gdx.graphics.getDeltaTime();

        // Check if enough time has passed
        if (animationTimer >= FRAME_DELAY) {
            player.setAnimation_index(player.getAnimation_index()+ Constants.FRAME_WIDTH);
            if (player.getAnimation_index() > player.getSprite().getWidth() - Constants.FRAME_WIDTH) player.setAnimation_index(0);
            enemy.setAnimation_index(enemy.getAnimation_index()+ Constants.FRAME_WIDTH);
            if (enemy.getAnimation_index() > enemy.getSprite().getWidth() - Constants.FRAME_WIDTH) enemy.setAnimation_index(0);
            animationTimer = 0f;  // Reset timer
        }

        batch.begin();
        tileManager.render(batch,camera);
        TextureRegion playerRegion = player.loadAnimation(player.getAnimation_index(), 0, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        TextureRegion enemyRegion = enemy.loadAnimation(enemy.getAnimation_index(),0,Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        batch.draw(playerRegion, (int)player.getX(),(int) player.getY(), Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        batch.draw(enemyRegion,(int)enemy.getX(),(int) enemy.getY(), Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);

        batch.end();

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

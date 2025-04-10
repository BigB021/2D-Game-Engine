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

    public Game()  {
    }
    OrthographicCamera camera;
    Viewport viewport;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.zoom =0.4f;
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        viewport = new FitViewport(Constants.screenWidth,Constants.screenHeight,camera);

        tileManager.getTilesFromFolder();
        tileManager.gettileimage();
        tileManager.loadbackgroundimg();
        int x = 1, y = 192;
        player = new Player(viewport.getScreenWidth(), y,(Constants.FRAME_WIDTH),Constants.FRAME_HEIGHT,3.5);
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

        float screenwidth=camera.zoom*mapWidth;//912
        float screenheight=camera.zoom*mapHeight;//912
        float playerCenterX = player.getHitBox().x + player.getHitBox().width / 2f;
        float playerCenterY = player.getHitBox().y + player.getHitBox().height / 2f;

           float halfViewportWidth = camera.viewportWidth * camera.zoom / 2f;
          float halfViewportHeight = camera.viewportHeight * camera.zoom / 2f;

        camera.position.x = Math.max(halfViewportWidth, Math.min(playerCenterX, mapWidth - halfViewportWidth));
        camera.position.y = Math.max(halfViewportHeight, Math.min(playerCenterY, mapHeight - halfViewportHeight));
        camera.update();
////// we can implement a camera but for now we stick to this
        System.out.println("playerX"+player.getX()+"Hitbox X :"+player.getHitBox().x);

        batch.setProjectionMatrix(camera.combined);

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
        ScreenUtils.clear(0.8f, 0.85f, 0.8f, 0.00f);

        batch.begin();
        tileManager.render(batch,camera);
        TextureRegion playerRegion = player.loadAnimation(player.getAnimation_index(), 0, (Constants.FRAME_WIDTH), Constants.FRAME_HEIGHT);
        TextureRegion enemyRegion = enemy.loadAnimation(enemy.getAnimation_index(),0,Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        batch.draw(playerRegion, player.getX(),player.getY(), Constants.FRAME_WIDTH*camera.zoom, Constants.FRAME_HEIGHT*camera.zoom);
        batch.draw(enemyRegion,(int)enemy.getX(),(int) enemy.getY(), Constants.FRAME_WIDTH*camera.zoom, Constants.FRAME_HEIGHT*camera.zoom);

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

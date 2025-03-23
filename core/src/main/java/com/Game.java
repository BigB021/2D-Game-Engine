package com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import entities.Enemy;
import entities.Player;
import inputs.InputsManager;
import utilities.Constants;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private  Player player;
    private Enemy enemy;
    private float animationTimer = 0f;

    // Testing hitBox
    private ShapeRenderer shape;

    @Override
    public void create() {
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

        ScreenUtils.clear(0.5f, 0.15f, 0.2f, 1f);

        player.movePlayer();
        enemy.moveEnemy();

        // Set animation timer to current time
        animationTimer += Gdx.graphics.getDeltaTime();

        // Check if enough time has passed
        float FRAME_DELAY = 0.1f;
        if (animationTimer >= FRAME_DELAY) {
            player.setAnimation_index(player.getAnimation_index()+ Constants.FRAME_WIDTH);
            if (player.getAnimation_index() > player.getSprite().getWidth() - Constants.FRAME_WIDTH) player.setAnimation_index(0);
            enemy.setAnimation_index(enemy.getAnimation_index()+ Constants.FRAME_WIDTH);
            if (enemy.getAnimation_index() > enemy.getSprite().getWidth() - Constants.FRAME_WIDTH) enemy.setAnimation_index(0);
            animationTimer = 0f;  // Reset timer
        }

        batch.begin();
        TextureRegion playerRegion = player.loadAnimation(player.getAnimation_index(), 0, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        TextureRegion enemyRegion = enemy.loadAnimation(enemy.getAnimation_index(),0,Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        batch.draw(playerRegion, player.getX(),player.getY(), Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        batch.draw(enemyRegion,enemy.getX(), enemy.getY(), Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);

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

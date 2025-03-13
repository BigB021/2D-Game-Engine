package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import entities.Player;
import inputs.InputsManager;
import utilities.Constants;


public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private final int width = 128;
    private final int height = 128;
    private int x_start = 0,y_start = 0;
    private  Player player;
    private float animationTimer = 0f;
    private final float FRAME_DELAY = 0.1f;


    @Override
    public void create() {
        int x = 140, y = 210;
        player = new Player(x,y,width,height,3.);

        // Inputs initialization
        InputsManager playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

        // Init player
        player.setSprite(new Texture(Constants.IDLE_ANIMATION));
        player.setCooldown(player.getAttackAnimationDuration());


        batch = new SpriteBatch();

    }

    @Override
    public void render() {

        ScreenUtils.clear(0.5f, 0.15f, 0.2f, 1f);

        player.movePlayer();

        animationTimer += Gdx.graphics.getDeltaTime();

        if (animationTimer >= FRAME_DELAY) {  // Check if enough time has passed
            x_start = x_start + width;
            if (x_start > player.getSprite().getWidth() - width) x_start = 0;  // Reset to first frame
            animationTimer = 0f;  // Reset timer
        }

        batch.begin();
        TextureRegion region = player.loadAnimation(x_start, y_start, width, height);
        batch.draw(region, (int)player.getX(),(int) player.getY(), width, height);
        batch.end();


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

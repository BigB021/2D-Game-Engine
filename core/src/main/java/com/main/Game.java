package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import entities.Player;
import inputs.InputsManager;
import mapManager.tileManager.TileManager;
import utilities.Constants;

import java.io.IOException;


public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private final int width = 128;
    private final int height = 128;
    private int x_start = 0,y_start = 0;
    private InputsManager inputsManager;
    private  int i=0;
    private  Player player;
    private float animationTimer = 0f;
    private final float FRAME_DELAY = 0.1f;


    @Override
    public void create() {



    }

    @Override
    public void render() {
        TileManager tileManager = null;
        try {
            tileManager = new TileManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ScreenUtils.clear(0.5f, 0.15f, 0.2f, 1f);

        player.movePlayer();

        animationTimer += Gdx.graphics.getDeltaTime();

        if (animationTimer >= FRAME_DELAY) {  // Check if enough time has passed
            x_start = x_start + width;
            if (x_start > player.getSprite().getWidth() - width) x_start = 0;  // Reset to first frame
            animationTimer = 0f;  // Reset timer
        }

        batch.begin();
        tileManager.render(batch);

        TextureRegion region = player.loadAnimation(x_start, y_start, width, height);
        batch.draw(region, (int)player.getX(),(int) player.getY(), width, height);

        batch.end();
        // Texture Regionrender
        //region = new TextureRegion(player.getSprite(),x_start,y_start, width, height);
        region = new TextureRegion(player.getSprite(),x_start,y_start, width, height);
//        background = new Texture(Constants.BACKGROUND);
//        batch.draw(background,1,1);

//        if ( i % 4 == 0){
//            if (image != null) {
//                ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//                player.setSprite(new Texture(Constants.IDLE_ANIMATION));
//                x_start = x_start + width;
//                if (x_start > image.getWidth() - width) x_start = 0;
//
//                batch.draw(region, x, y);
//                if (Gdx.input.isKeyPressed(Input.Keys.A))
//                {
//                    x -= player.getPlayerSpeed();
//                    player.setSprite(new Texture(Constants.RUN_ANIMATION));
//                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//                    x+= player.getPlayerSpeed();
//                    player.setSprite(new Texture(Constants.WALK_ANIMATION));
//
//
//                }
//            }





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

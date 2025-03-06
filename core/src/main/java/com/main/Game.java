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
import utilities.Constants;


public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private TextureRegion region;
    private int x = 140, y = 210;
    private int width = 128,height = 128;
    private int x_start = 0,y_start = 0;
    private InputsManager inputsManager;
    private  int i=0;
    private  Player player;
    @Override
    public void create() {
        player = new Player((int)x,(int)y,width,height,5);
        batch = new SpriteBatch();
        String filePath = Constants.IDLE_ANIMATION;
        //Gdx.app.log("GameMain", "Loading texture from: " + filePath);

        if (Gdx.files.internal(filePath).exists()) {
            Gdx.app.log("GameMain", "File exists");
            image = new Texture(filePath);

        } else {
            Gdx.app.error("GameMain", "File not found: " + filePath);
        }
        // Inputs initialization
        //Gdx.input.setInputProcessor(new InputsManager());
        inputsManager = new InputsManager();

        // Init player
        player.setSprite(new Texture(Constants.IDLE_ANIMATION));



    }

    @Override
    public void render() {

        batch.begin();
        region = player.loadAnimation(x_start,y_start ,width,height);

        //System.out.println(i);
        //if ( i % 4 == 0){
            if (image != null) {
                ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
                player.setSprite(new Texture(Constants.IDLE_ANIMATION));
                x_start = x_start + width;
                if (x_start > image.getWidth() - width) x_start = 0;


                if (Gdx.input.isKeyPressed(Input.Keys.A))
                {
                    x -= player.getPlayerSpeed();
                    player.setSprite(new Texture(Constants.RUN_ANIMATION));
                    batch.draw(region, x+width, y, -width, height);
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {

                    x += player.getPlayerSpeed();
                    player.setSprite(new Texture(Constants.WALK_ANIMATION));
                    batch.draw(region, x, y,width,height);

                }
                else batch.draw(region, x, y,width,height);

                i = 0;
            }
        //}
        i++;
        batch.end();



    }

    @Override
    public void dispose() {
        try {
            if (batch != null) {
                batch.dispose();
            }
            if (image != null) {
                image.dispose();
            }
        } catch (Exception e) {
            Gdx.app.error("GameMain", "Error during disposal", e);
        }
    }
}

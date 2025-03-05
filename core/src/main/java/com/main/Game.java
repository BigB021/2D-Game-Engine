package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import inputs.InputsManager;


public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private TextureRegion region;
    private int x = 140,y = 210;
    private int width = 128,height = 128;
    private int x_start = 0,y_start = 0;
    private InputsManager inputsManager;
    private  int i=0;
    @Override
    public void create() {
        batch = new SpriteBatch();
        String filePath = "assets/test.png";
        Gdx.app.log("GameMain", "Loading texture from: " + filePath);

        if (Gdx.files.internal(filePath).exists()) {
            Gdx.app.log("GameMain", "File exists");
            image = new Texture(filePath);

        } else {
            Gdx.app.error("GameMain", "File not found: " + filePath);
        }
        // Inputs initialization
        //Gdx.input.setInputProcessor(new InputsManager());
        inputsManager = new InputsManager();




    }

    @Override
    public void render() {



        System.out.println(i);

        batch.begin();
        // Texture Region
        region = new TextureRegion(image,x_start,y_start, width, height);


        if (i%3 == 0){
            if (image != null) {
                ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
                x_start = x_start + width;
                if (x_start > 896) x_start = 0;

                batch.draw(region, x, y);
                if (Gdx.input.isKeyPressed(Input.Keys.A))
                {x+=5;}
            }
        }

        batch.end();

        i++;


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

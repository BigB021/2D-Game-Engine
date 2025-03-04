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
        if (x_start > 896) x_start = 0;
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        // Texture Region
        region = new TextureRegion(image,x_start,y_start, width, height);

        x_start = x_start + width;
        if (image != null) {
            batch.draw(region, x, y);
        }
        batch.end();
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {x++;}

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

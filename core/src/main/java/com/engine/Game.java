package com.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import inputs.InputsManager;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    @Override
    public void create() {
        batch = new SpriteBatch();
        String filePath = "assets/Test.png";
        Gdx.app.log("GameMain", "Loading texture from: " + filePath);

        if (Gdx.files.internal(filePath).exists()) {
            Gdx.app.log("GameMain", "File exists");
            image = new Texture(filePath);
        } else {
            Gdx.app.error("GameMain", "File not found: " + filePath);
        }
        Gdx.input.setInputProcessor(new InputsManager());

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        if (image != null) {
            batch.draw(image, 140, 210);
        }
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

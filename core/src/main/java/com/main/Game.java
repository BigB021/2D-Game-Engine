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
    private int x = 140, y = 210;
    private final int width = 128;
    private final int height = 128;
    private int x_start = 0,y_start = 0;
    private InputsManager playerInput;
    private  Player player;
    private float animationTimer = 0f;  // Timer to track elapsed time
    private final float FRAME_DELAY = 0.1f;  // Delay between frames (adjust as needed)


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
        playerInput = new InputsManager(player);
        Gdx.input.setInputProcessor(playerInput);

        // Init player
        player.setSprite(new Texture(Constants.IDLE_ANIMATION));




    }

    @Override
    public void render() {

        ScreenUtils.clear(0.5f, 0.15f, 0.2f, 1f);

        player.movePlayer(); // Move the player every frame

        animationTimer += Gdx.graphics.getDeltaTime(); // Increment timer

        if (animationTimer >= FRAME_DELAY) {  // Check if enough time has passed
            x_start = x_start + width;  // Move to the next frame
            if (x_start > image.getWidth() - width) x_start = 0;  // Reset to first frame
            animationTimer = 0f;  // Reset timer
        }

        batch.begin();
        TextureRegion region = player.loadAnimation(x_start, y_start, width, height);
        batch.draw(region, player.getX(), player.getY(), width, height);
        batch.end();

//        if (Gdx.input.isKeyPressed(Input.Keys.A))
//        {
//            x -= player.getPlayerSpeed();
//            player.setSprite(new Texture(Constants.RUN_ANIMATION));
//            batch.draw(region, x+width, y, -width, height);
//        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            x += player.getPlayerSpeed();
//            player.setSprite(new Texture(Constants.WALK_ANIMATION));
//            batch.draw(region, x, y,width,height);
//        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//            y += player.getPlayerSpeed();
//            player.setSprite(new Texture(Constants.JUMP_ANIMATION));
//            batch.draw(region, x, y,width,height);
//        } else batch.draw(region, x, y,width,height);





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

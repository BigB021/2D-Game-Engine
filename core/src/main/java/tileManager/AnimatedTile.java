package tileManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class representing a tile with animation capabilities.
 * This class extends the basic `Tile` class and adds support for animated tiles by using `Animation<TextureRegion>`.
 */
public class AnimatedTile extends Tile {
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    /**
     * Constructs an animated tile using a texture, specifying the number of frames in both columns and rows,
     * and the frame duration for the animation.
     *
     * @param texture The texture that contains the frames for the animation.
     * @param frameCols The number of columns of frames in the texture.
     * @param frameRows The number of rows of frames in the texture.
     * @param frameDuration The duration in seconds for each frame to be displayed.
     */
    public AnimatedTile(Texture texture, int frameCols, int frameRows, float frameDuration) {
        TextureRegion[][] tmp = TextureRegion.split(texture,
            texture.getWidth() / frameCols,
            texture.getHeight() / frameRows);

        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        animation = new Animation<>(1f / frameDuration, frames);
    }

    /**
     * Returns the current frame of the animation based on the elapsed time (deltaTime).
     *
     * @param deltaTime The time in seconds since the last frame was drawn. This is used to update the animation's state.
     * @return The current frame of the animation.
     */
    public TextureRegion getCurrentFrame(float deltaTime) {
        stateTime += deltaTime;
        //System.out.println("Drawing frame: " + animation.getKeyFrame(stateTime, true).getRegionX() + ", " + animation.getKeyFrame(stateTime, true).getRegionY());
        return animation.getKeyFrame(stateTime, true);

    }
}

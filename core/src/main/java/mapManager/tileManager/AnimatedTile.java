package mapManager.tileManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedTile extends Tile {
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

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

    public TextureRegion getCurrentFrame(float deltaTime) {
        stateTime += deltaTime;
        System.out.println("Drawing frame: " + animation.getKeyFrame(stateTime, true).getRegionX() + ", " + animation.getKeyFrame(stateTime, true).getRegionY());

        return animation.getKeyFrame(stateTime, true); // true = looping

    }
}

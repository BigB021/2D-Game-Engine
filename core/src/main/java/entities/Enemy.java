package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import utilities.Constants;

public class Enemy extends Entity {
    private int enemyAction;
    private boolean isMoving;
    private boolean isDead;
    private boolean isAttacking;
    //private boolean isJumping;
    private int enemyDirection;
    private double enemySpeed;
    private double cooldown;
    private long lastAttackTime = 0;
    private int animation_index;

    private Player player;

    // Cached textures for animations
    private Texture idleTexture;
    private Texture walkTexture;
    private Texture runTexture;
    private Texture jumpTexture;
    private Texture attack1Texture;

    public Enemy(int x, int y, int width, int height,double enemySpeed, Player player) {
        super(x, y, width, height);
        this.enemyAction = Constants.IDLE;
        this.isMoving = false;
        this.isDead = false;
        this.enemyDirection = Constants.RIGHT; //player.getPlayerDirection();
        this.enemySpeed = enemySpeed;
        this.animation_index = 0;
        this.player = player;

        loadTextures();
        // Initialize sprite with the idle texture
        setSprite(idleTexture);
    }

    /**
     * Loads animation textures once and caches them.
     */
    private void loadTextures() {
        idleTexture = new Texture(Constants.IDLE_ANIMATION);
        walkTexture = new Texture(Constants.WALK_ANIMATION);
        runTexture = new Texture(Constants.RUN_ANIMATION);
        jumpTexture = new Texture(Constants.JUMP_ANIMATION);
        attack1Texture = new Texture(Constants.ATTACK_1_ANIMATION);
    }

    /**
     * Updates the enemy's animation based on the current action.
     */
    public void updateAnimation() {
        switch (enemyAction) {
            case Constants.IDLE:
                if (getSprite() != idleTexture) {
                    setSprite(idleTexture);
                }
                break;
            case Constants.WALK:
                if (getSprite() != walkTexture) {
                    setSprite(walkTexture);
                }
                break;
            case Constants.RUN:
                if (getSprite() != runTexture) {
                    setSprite(runTexture);
                }
                break;
            case Constants.JUMP:
                if (getSprite() != jumpTexture) {
                    setSprite(jumpTexture);
                }
                break;
            case Constants.ATTACK_1:
                if (getSprite() != attack1Texture) {
                    setSprite(attack1Texture);
                }
                break;
        }
    }

    /**
     * Loads and returns the appropriate animation frame based on direction.
     *
     * @param x      X-coordinate of the animation frame.
     * @param y      Y-coordinate of the animation frame.
     * @param width  Width of the animation frame.
     * @param height Height of the animation frame.
     * @return TextureRegion containing the selected frame.
     */
    public TextureRegion loadAnimation(int x, int y, int width, int height) {
        if (getEnemyDirection() == Constants.RIGHT) {

            return new TextureRegion(getSprite() ,x,y, getEnemyDirection() * width,height);
        }
        return new TextureRegion(getSprite() ,x + width,y, getEnemyDirection() * width,height);
    }

    // Move enemy
    // todo: implement path finding algorithm or calculate distat Player-Enemy to pursue player
    public void moveEnemy(){

        this.setEnemyAction(Constants.RUN);
        this.updateAnimation();

        // Checking collision with screen borders

        if(this.getHitBox().getX() >= (Constants.WIDTH - Constants.FRAME_WIDTH * 0.3f)){
            this.setEnemyDirection(Constants.LEFT);
        }
        if(this.getHitBox().getX()<= 0){
            this.setEnemyDirection(Constants.RIGHT);

        }
        checkCollision();
        float speed = (float) ((enemyAction == Constants.RUN) ? enemySpeed * 2 : enemySpeed);
        this.setX(this.getX() + speed * getEnemyDirection());
        updateHitbox();
    }

    public void calculateDistance() {
        float epsilon = 0.05f;
        if (Math.abs((player.getHitBox().x + player.hitBox.width) - this.getHitBox().x) <= epsilon) {
            this.setEnemyDirection(Constants.LEFT);
        }
        if(Math.abs((this.getHitBox().x + this.hitBox.width) - player.getHitBox().x) <= epsilon){
            this.setEnemyDirection(Constants.RIGHT);

        }

    }

    // Check collision with player
    public void checkCollision() {
        float epsilon = 0.05f;

        if ((this.getHitBox().x + this.hitBox.width) - player.getHitBox().x  >= epsilon && this.getEnemyDirection() == Constants.RIGHT) {
            // todo: Should implement attackinhg Player
            this.setX(getX() - 10);
        }else if((player.getHitBox().x + player.hitBox.width) - this.getHitBox().x >= epsilon && this.getEnemyDirection() == Constants.LEFT){
            // todo: Should implement attackinhg Player
            this.setX(getX() + 10);


        }
    }

    //NEW CHANGE blbla

    // Getters & Setters

    public int getEnemyAction() {
        return enemyAction;
    }

    public void setEnemyAction(int enemyAction) {
        this.enemyAction = enemyAction;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public int getEnemyDirection() {
        return enemyDirection;
    }

    public void setEnemyDirection(int enemyDirection) {
        this.enemyDirection = enemyDirection;
    }

    public double getEnemySpeed() {
        return enemySpeed;
    }

    public void setEnemySpeed(double enemySpeed) {
        this.enemySpeed = enemySpeed;
    }

    public int getAnimation_index() {
        return animation_index;
    }

    public void setAnimation_index(int animation_index) {
        this.animation_index = animation_index;
    }
}

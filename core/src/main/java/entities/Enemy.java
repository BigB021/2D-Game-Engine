package entities;

import collision.CollisionSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import tileManager.TileManager;

import static utilities.constants.EntityConstants.*;
import static utilities.constants.TextureConstants.*;
import static utilities.constants.FramesConstants.FRAME_DELAY;
import static utilities.constants.FramesConstants.FRAME_WIDTH;
import static utilities.constants.MapTilesConstants.TILE_SIZE;

public class Enemy extends Entity {

    private int enemyAction;
    private double enemySpeed;
    private long lastAttackTime = 0;
    private long hurtStartTime = 0;
    private int animation_index;

    private final Player player;

    // Cached textures for animations
    private Texture idleTexture;
    private Texture walkTexture;
    private Texture runTexture;
    private Texture jumpTexture;
    private Texture attack1Texture;
    private Texture hurtTexture;
    private Texture deadTexture;

    /**
     * Constructs an Enemy object.
     *
     * @param x           Initial x-coordinate.
     * @param y           Initial y-coordinate.
     * @param width       Width of the enemy.
     * @param height      Height of the enemy.
     * @param enemySpeed  Movement speed of the enemy.
     * @param health      Health of the enemy.
     * @param player      The player object the enemy will interact with.
     * @param tileManager TileManager instance for collision checks.
     */
    public Enemy(int x, int y, int width, int height,double enemySpeed,int health ,Player player, TileManager tileManager) {
        super(x, y, width, height,health,tileManager);
        this.player = player;
        this.enemyAction = IDLE;
        this.isMoving = false;
        this.isDead = false;
        this.enemySpeed = enemySpeed;
        this.animation_index = 0;

        initDirection();
        loadTextures();
        // Initialize sprite with the idle texture
        setSprite(idleTexture);
    }

    /**
     * Initializes the enemy's direction towards the player.
     */
    public void initDirection(){
        if (player.x - this.x > 0)
            this.entityDirection = RIGHT;
        else
            this.entityDirection = LEFT;
    }

    /**
     * Loads animation textures once and caches them.
     */
    private void loadTextures() {
        idleTexture = new Texture(ENEMY_IDLE_ANIMATION);
        walkTexture = new Texture(ENEMY_WALK_ANIMATION);
        runTexture = new Texture(ENEMY_RUN_ANIMATION);
        jumpTexture = new Texture(ENEMY_JUMP_ANIMATION);
        attack1Texture = new Texture(ENEMY_ATTACK_1_ANIMATION);
        hurtTexture = new Texture(ENEMY_HURT_ANIMATION);
        deadTexture = new Texture(ENEMY_DEAD_ANIMATION);
    }

    /**
     * Updates the enemy's animation based on the current action.
     */
    public void updateAnimation() {
        switch (enemyAction) {
            case IDLE:
                if (getSprite() != idleTexture) {
                    setSprite(idleTexture);
                }
                break;
            case WALK:
                if (getSprite() != walkTexture) {
                    setSprite(walkTexture);
                }
                break;
            case RUN:
                if (getSprite() != runTexture) {
                    setSprite(runTexture);
                }
                break;
            case JUMP:
                if (getSprite() != jumpTexture) {
                    setSprite(jumpTexture);
                }
                break;
            case ATTACK_1:
                if (getSprite() != attack1Texture) {
                    setSprite(attack1Texture);
                }
                break;
            case DEAD:
                if (getSprite() != deadTexture) {
                    setSprite(deadTexture);
                }
                break;
            case HURT:
                if (getSprite() != hurtTexture) {
                    setSprite(hurtTexture);
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
        if (getEntityDirection() == RIGHT) {

            return new TextureRegion(getSprite() ,x,y, getEntityDirection() * width,height);
        }
        return new TextureRegion(getSprite() ,x + width,y, getEntityDirection() * width,height);
    }

    /**
     * Moves the enemy based on its current state and interactions with the player.
     */
    public void moveEnemy(){
        if(canMove(DX,DY)) {
            handleDeath();
            updateHitboxes();

            if (isDying()) {
                updateAnimation();
                updateHitboxes();
                return;
            }

            if (enemyAction == HURT) {
                long elapsed = System.currentTimeMillis() - hurtStartTime;
                if (elapsed < getHurtDuration()) {
                    updateAnimation();
                    updateHitboxes();
                    return;
                }
            }

            if (enemyAction == DEAD) {
                setAttacking(false);
            }

            if (shouldPursuePlayer()) {
                updateMovementBasedOnPlayer();
                handleCombat();
            } else {
                setIdleState();
            }

            updateAnimation();
            updateHitboxes();
        }
    }

    // =====================Move Enemy submethods=====================

    /**
     * Respawns enemy to spawn point after his death
     */
    private void respawnEnemy(int x, int y) {
        this.setX(x);
        this.setY(y);
        setEntityHealth(10);
        setDead(false);
        initDirection();
        setEnemyAction(IDLE);
    }

    /**
     * Checks if death animation is still on going
     * @return if elapsed time is greater than death animation duration
     */
    private boolean isDying() {
        if (enemyAction == DEAD) {
            long elapsed = System.currentTimeMillis() - deathStartTime;
            return elapsed < getDeathDuration();
        }
        return false;
    }

    /**
     * Handles enemy's death
     */
    private void handleDeath() {
        if(getEntityHealth() <= 0 && enemyAction != DEAD){
            setEnemyAction(DEAD);
            this.setDead(true);
            deathStartTime = System.currentTimeMillis();
            // todo : drop sound and loot
        }
    }

    /**
     * set enemy's direction towards the player
     */
    private void updateDirectionTowardsPlayer(){

        if(this.getHitBox().x - player.getHitBox().x >= 0){
            this.setEntityDirection(LEFT);
        }
        else if(this.getHitBox().x - player.getHitBox().x <= 0){
            this.setEntityDirection(RIGHT);
        }
    }

    /**
     *  Checks if player is within enemy's pursue distance
     * @return if distance between player and enemy is greater than pursue distance
     */
    private  boolean shouldPursuePlayer(){
        float distanceX = Math.abs(this.getHitBox().x - player.getHitBox().x);
        if(Math.abs(this.getY() - player.getY()) <= TILE_SIZE) return distanceX <= DISTANCE;
        return false;
    }

    /**
     * Update enemy's position based on if player is within pursue distance
     */
    private  void updateMovementBasedOnPlayer(){
        updateDirectionTowardsPlayer();
        setRunningState();
        pursuePlayer();
    }

    /**
     * Handles combat between enemy and player
     */
    private void handleCombat(){
        // do nothing if collision is not detected
        if(!CollisionSystem.checkPlayerCollision(player, this)) return ;

        long currentTime = System.currentTimeMillis();
        this.setEnemyAction(ATTACK_1);
        this.setMoving(false);
        this.setAttacking(true);

        if (currentTime - this.lastAttackTime >= getAttackAnimationDuration()) {
            handlePlayerAttack();
            handleEnemyAttack(currentTime);
        }
    }

    /**
     * When attacked by player, enemy takes damage and loads HURT animation
     */
    private void handlePlayerAttack(){
        if(player.isAttacking() && player.getAttackHitBox().overlaps(this.getHitBox())){
            this.setEntityHealth(this.getEntityHealth() - PLAYER_ATTACK_DAMAGE);
            this.setEnemyAction(HURT);
            this.hurtStartTime = System.currentTimeMillis();
            this.setAttacking(false);
        }
    }

    /**
     * Attacks player if enemy collides with him and attack cooldown is elapsed
     * @param currentTime: current time to check with last attack time
     */
    private void handleEnemyAttack(long currentTime){
        this.lastAttackTime = currentTime;
        this.setAnimation_index(3 * FRAME_WIDTH);

        player.setPlayerAction(HURT);
        player.updateAnimation();
        player.setEntityHealth(player.getEntityHealth() - ENEMY_ATTACK_DAMAGE);
        if (player.getEntityHealth() <= 0) {
            player.setDead(true);
        }
    }

    /**
     * Moves enemy towards the player if he's within pursue distance
     */
    private void pursuePlayer(){
        float speed = (enemyAction == RUN) ? (float) (enemySpeed * 2) : (float) enemySpeed;
        this.setX(this.getX() + speed * getEntityDirection());
    }

    // =============================================================

    /**
     * Calculates the total duration of the attack animation in milliseconds.
     *
     * @return the attack duration in milliseconds.
     */
    public long getAttackAnimationDuration() {
        int frames = attack1Texture.getWidth() / FRAME_WIDTH;
        return (long)(frames * FRAME_DELAY * 1000);
    }

    /**
     * Calculates the total duration of the hurt animation in milliseconds.
     *
     * @return the hurt duration in milliseconds.
     */
    public long getHurtDuration() {
        int frames = hurtTexture.getWidth() / FRAME_WIDTH;
        return (long)(frames * FRAME_DELAY * 1000);
    }

    /**
     * Calculate total duration of the death animation in ms.
     * Derives frame count from sprite width / FRAME_WIDTH.
     * @return the death duration in milliseconds.
     */
    public long getDeathDuration() {
        // number of frames in the death sprite sheet:
        int frames = deadTexture.getWidth() / FRAME_WIDTH;
        return (long)(frames * FRAME_DELAY * 1000);
    }




    //=====================Getters & Setters=====================

    public int getEnemyAction() {
        return enemyAction;
    }

    public void setEnemyAction(int enemyAction) {
        this.enemyAction = enemyAction;
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

    private void setRunningState() {
        this.setEnemyAction(RUN);
    }

    private void setIdleState() {
        this.setEnemyAction(IDLE);
    }




}

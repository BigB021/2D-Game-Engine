package entities;

import collision.CollisionSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utilities.Constants;

public class Enemy extends Entity {

    private int enemyAction;
    private int enemyDirection;
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

    public Enemy(int x, int y, int width, int height,double enemySpeed,int health ,Player player) {
        super(x, y, width, height,health);
        this.player = player;
        this.enemyAction = Constants.IDLE;
        this.isMoving = false;
        this.isDead = false;
        this.enemySpeed = enemySpeed;
        this.animation_index = 0;

        initDirection();
        loadTextures();
        // Initialize sprite with the idle texture
        setSprite(idleTexture);
    }

    // Set enemy's initial direction towards the player
    public void initDirection(){
        if (player.x - this.x > 0)
            this.enemyDirection = Constants.RIGHT;
        else
            this.enemyDirection = Constants.LEFT;
    }

    /**
     * Loads animation textures once and caches them.
     */
    private void loadTextures() {
        idleTexture = new Texture(Constants.ENEMY_IDLE_ANIMATION);
        walkTexture = new Texture(Constants.ENEMY_WALK_ANIMATION);
        runTexture = new Texture(Constants.ENEMY_RUN_ANIMATION);
        jumpTexture = new Texture(Constants.ENEMY_JUMP_ANIMATION);
        attack1Texture = new Texture(Constants.ENEMY_ATTACK_1_ANIMATION);
        hurtTexture = new Texture(Constants.ENEMY_HURT_ANIMATION);
        deadTexture = new Texture(Constants.ENEMY_DEAD_ANIMATION);
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
            case Constants.DEAD:
                if (getSprite() != deadTexture) {
                    setSprite(deadTexture);
                }
                break;
            case Constants.HURT:
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
        if (getEnemyDirection() == Constants.RIGHT) {

            return new TextureRegion(getSprite() ,x,y, getEnemyDirection() * width,height);
        }
        return new TextureRegion(getSprite() ,x + width,y, getEnemyDirection() * width,height);
    }

    // Move enemy
    // todo: OPTIMIZE fighting mechanism
    public void moveEnemy(){
        handleDeath();
        if (isDeathAnimationOngoing()){
            updateAnimation();
            updateHitboxes();
            return;
        }

        if(enemyAction == Constants.HURT){
            long elapsed = System.currentTimeMillis() - hurtStartTime;
            if (elapsed < getHurtDuration()){
                updateAnimation();
                updateHitboxes();
                return;
            }
        }

        if(enemyAction == Constants.DEAD ) {
            respawnEnemy();
        }

        if(shouldPursuePlayer()){
            updateMovementBasedOnPlayer();
            handleCombat();
        }else {
            setIdleState();
        }

        updateAnimation();
        updateHitboxes();
    }

    // =====================Move Enemy submethods=====================

    private void respawnEnemy(){
        this.setX(Constants.ENEMY_SPAWN_X);
        this.setY(Constants.ENEMY_SPAWN_Y);
        setEntityHealth(10);
        setDead(false);
        initDirection();
        setEnemyAction(Constants.IDLE);
    }

    private boolean isDeathAnimationOngoing() {
        if (enemyAction == Constants.DEAD) {
            long elapsed = System.currentTimeMillis() - deathStartTime;
            return elapsed < getDeathDuration();
        }
        return false;
    }

    private void handleDeath() {
        if(getEntityHealth() <= 0 && enemyAction != Constants.DEAD){
            setEnemyAction(Constants.DEAD);
            this.setDead(true);
            deathStartTime = System.currentTimeMillis();
            // todo : drop sound and loot
        }
    }

    
    private void updateDirectionTowardsPlayer(){

        if(this.getHitBox().x - player.getHitBox().x >= 0){
            this.setEnemyDirection(Constants.LEFT);
        }
        else if(this.getHitBox().x - player.getHitBox().x <= 0){
            this.setEnemyDirection(Constants.RIGHT);
        }
    }

    private  boolean shouldPursuePlayer(){
        float distance = Math.abs(this.getHitBox().x - player.getHitBox().x);
        return distance <= Constants.DISTANCE;
    }

    private  void updateMovementBasedOnPlayer(){
        updateDirectionTowardsPlayer();
        setRunningState();
        pursuePlayer();
    }

    private void handleCombat(){
        // do nothing if collision is not detected
        if(!CollisionSystem.checkPlayerCollision(player, this)) return ;

        long currentTime = System.currentTimeMillis();
        this.setEnemyAction(Constants.ATTACK_1);
        this.setMoving(false);
        this.setAttacking(true);

        if (currentTime - this.lastAttackTime >= getAttackAnimationDuration()) {
            handlePlayerAttack();
            handleEnemyAttack(currentTime);
        }
    }

    private void handlePlayerAttack(){
        if(player.isAttacking && player.getAttackHitBox().overlaps(this.getHitBox())){
            this.setEntityHealth(this.getEntityHealth() - 1);
            this.setEnemyAction(Constants.HURT);
            this.hurtStartTime = System.currentTimeMillis();
            this.setAttacking(false);
            // debug System.out
            System.out.println("Enemy health:"+ this.getEntityHealth());
        }
    }

    private void handleEnemyAttack(long currentTime){
        // debug System.out
        System.out.println("Cooldown Detected:" + player.getCooldown());
        this.lastAttackTime = currentTime;
        this.setAnimation_index(3 * Constants.FRAME_WIDTH);

        player.setPlayerAction(Constants.HURT);
        player.updateAnimation();
        player.setEntityHealth(player.getEntityHealth() - 1);
        if (player.getEntityHealth() <= 0) {
            player.setDead(true);
        }
    }

    private void pursuePlayer(){
        float speed = (enemyAction == Constants.RUN) ? (float) (enemySpeed * 2) : (float) enemySpeed;
        this.setX(this.getX() + speed * getEnemyDirection());
    }

    // =============================================================

    /**
     * Calculates the total duration of the attack animation in milliseconds.
     *
     * @return the attack duration in milliseconds.
     */
    public long getAttackAnimationDuration() {
        int frames = attack1Texture.getWidth() / Constants.FRAME_WIDTH;
        return (long)(frames * Constants.FRAME_DELAY * 1000);
    }

    public long getHurtDuration() {
        int frames = hurtTexture.getWidth() / Constants.FRAME_WIDTH;
        return (long)(frames * Constants.FRAME_DELAY * 1000);
    }

    /**
     * Calculate total duration of the death animation in ms.
     * Derives frame count from sprite width / FRAME_WIDTH.
     */
    public long getDeathDuration() {
        // number of frames in the death sprite sheet:
        int frames = deadTexture.getWidth() / Constants.FRAME_WIDTH;
        return (long)(frames * Constants.FRAME_DELAY * 1000);
    }


    //=====================Getters & Setters=====================

    public int getEnemyAction() {
        return enemyAction;
    }

    public void setEnemyAction(int enemyAction) {
        this.enemyAction = enemyAction;
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

    // Set Enemy State
    private void setRunningState() {
        this.setEnemyAction(Constants.RUN);
    }

    private void setIdleState() {
        this.setEnemyAction(Constants.IDLE);
    }




}

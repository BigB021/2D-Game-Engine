package mapManager.tileManager;
import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import utilities.AnimatedConfig;
import utilities.TilesSet;
import java.io.*;
import java.util.Map;
import static constants.MapTilesConstants.*;
import static constants.TextureConstants.TILE_FOLDER;


/**
 * Manages loading, storing, and rendering of tile-based maps and their associated textures.
 * Supports static and animated tiles, multi-layer maps, and collision querying.
 */
public class TileManager {
    /** Array of all Tile instances (indexed by tile ID). */
    public Tile[] tiles;
    /** Tensor holding map data: [layer][column][row]. */
    public static int[][][] mapTileLayers;
    public Texture backgroundImage;

    /**
     * Constructs a TileManager and loads the default maps.
     */
    public TileManager() {
        tiles =new Tile[TOTAL_TILE_IDS];
        mapTileLayers = new int [NUM_LAYERS][MAX_SCREEN_COL][MAX_SCREEN_ROW];

        loadMapLayer(MAP_1,0);
        loadMapLayer(MAP_2,1);
    }

    /**
     * Loads a map file into the specified layer.
     * @param mapPath path to the map text file
     * @param layer   index of the layer to populate
     */
    private void loadMapLayer(String mapPath, int layer)  {
        try(BufferedReader br = new BufferedReader( new FileReader(mapPath))){
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < MAX_SCREEN_ROW) {
                    line = line.trim();
                    if(line.isEmpty()){
                        System.out.println("⚠ Skipped empty line at row " + row);
                continue;}

                    String[] numbers = line.split("\\s+");

                if (numbers.length != MAX_SCREEN_COL) {
                    System.out.println("  Invalid number of columns at row " + row + ": " + numbers.length);
                    System.out.println("   Line content: \"" + line + "\"");
                    throw new RuntimeException("Invalid map file format.");
                }
                    for (int col = 0; col < MAX_SCREEN_COL; col++) {
                    mapTileLayers[layer][col][row] = Integer.parseInt(numbers[col]);
                }
                    row++;


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the background image for rendering.
     */
    public void loadBackground(){
        backgroundImage = new Texture(Gdx.files.internal(BACKGROUND_IMAGE));
    }

    /**
     * Scans the TILE_FOLDER and populates the TILES_MAP with file paths.
     */
    public void getTilesFromFolder(){
        File folder = new File(TILE_FOLDER);
        File[] directoryListing = folder.listFiles();

        try {
            if (directoryListing != null && directoryListing.length > 0) {
                for (File child : directoryListing) {
                    if (child.isFile()) {
                        String tileName = child.getName().replaceFirst("[.][^.]+$", "");
                        addTile(tileName, child.getPath());
                    }
                }
            } else {
                System.err.println("Directory is empty or could not be read: " + folder.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error while loading tiles from directory: " + e.getMessage());
        }

    }

    /**
     * Initializes Tile instances (static and animated) based on TILES_MAP and config.
     */
    public void initTiles(){
        final int defaultSpeed = AnimatedConfig.DEFAULT_ANIMATION_SPEED;
        for (Map.Entry<String,String> entry : TILES_MAP.entrySet()) {
            int id = Integer.parseInt(entry.getKey());
            if (id == 0) {
                tiles[id] = null;
                continue;
            }

            // load texture once
            Texture texture = new Texture(Gdx.files.internal(entry.getValue()));
            tiles[id] = createTile(id, texture, TILE_SIZE, defaultSpeed);
            // ensure collisionBox is never null
            tiles[id].collisionBox = new Rectangle();
        }
    }

    /**
     * Creates a Tile or AnimatedTile based on ID, texture, and configs.
     */
    public Tile createTile(int id, Texture texture, int tileSize, int defaultSpeed) {
        AnimatedConfig cfg = AnimatedConfig.animConfigs.get(id);
        if (cfg != null) {
            return new AnimatedTile(texture, cfg.getCols(), cfg.getRows(), cfg.getSpeed());
        }
        if (ANIMATED_ITEMS.contains(id)) {
            int cols = texture.getWidth() / tileSize;
            int rows = texture.getHeight() / tileSize;
            return new AnimatedTile(texture, cols, rows, defaultSpeed);
        }
        Tile t = new Tile();
        t.image = texture;
        t.collision = TilesSet.COLLIDABLE_TILES.contains(id);
        return t;
    }


    /**
     * Renders all map layers and the background.
     * @param batch  sprite batch for drawing
     * @param camera camera for viewport info
     * @param shape  shape renderer for collision debug
     */
    public void render(SpriteBatch batch, Camera camera, ShapeRenderer shape) {
        drawBackground(batch, camera);
        for (int[][] mapTileLayer : mapTileLayers) {
            renderLayer(batch, camera, shape, mapTileLayer);
        }
    }

    private void drawBackground(SpriteBatch batch, Camera camera) {
        float x = (camera.position.x - camera.viewportWidth/2) * 1.1f;
        float y = (camera.position.y - camera.viewportHeight/2) * 1.1f;
        batch.draw(backgroundImage, x, y, camera.viewportWidth, camera.viewportHeight);
    }

    private void renderLayer(SpriteBatch batch, Camera camera, ShapeRenderer shape, int[][] layerData) {
        int tileSize = TILE_SIZE;
        int minCol = clamp((int)(camera.position.x - camera.viewportWidth/2) / tileSize, 0, MAX_SCREEN_COL);
        int maxCol = clamp((int)(camera.position.x + camera.viewportWidth/2) / tileSize + 1, 0, MAX_SCREEN_COL);
        int minRow = clamp((int)(camera.position.y - camera.viewportHeight/2) / tileSize, 0, MAX_SCREEN_ROW);
        int maxRow = clamp((int)(camera.position.y + camera.viewportHeight/2) / tileSize + 1, 0, MAX_SCREEN_ROW);

        for (int row = minRow; row < maxRow; row++) {
            for (int col = minCol; col < maxCol; col++) {
                int dataRow = MAX_SCREEN_ROW - 1 - row;
                int id = layerData[col][dataRow];
                Tile t = tiles[id];
                if (t == null) continue;

                if (t instanceof AnimatedTile) {
                    TextureRegion frame = ((AnimatedTile) t).getCurrentFrame(Gdx.graphics.getDeltaTime());
                    batch.draw(frame, col*tileSize, row*tileSize);
                } else if (t.image != null) {
                    Rectangle box = t.collisionBox;
                    box.set(col*tileSize, row*tileSize, tileSize, tileSize);
                    batch.draw(t.image, col*tileSize, row*tileSize);
                    shape.setColor(t.collision ? Color.YELLOW : Color.RED);
                    shape.rect(box.x, box.y, box.width, box.height);
                }
            }
        }
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * @param worldX  The x‑position in pixels
     * @param worldY  The y‑position in pixels
     * @param layer   Which layer to query (0=ground, 1=objects)
     * @return        The Tile instance under that pixel, or null if out of bounds
     */
    public Tile getTile(int worldX, int worldY, int layer) {
        int tileSize = TILE_SIZE;
        int col = worldX / tileSize;
        int row = worldY / tileSize;
        // safety check
        if (col < 0 || col >= MAX_SCREEN_COL ||
            row < 0 || row >= MAX_SCREEN_ROW ||
            layer < 0 || layer >= mapTileLayers.length) {
            return null;
        }
        int renderRow = MAX_SCREEN_ROW - 1 - row;
        int tileNum = mapTileLayers[layer][col][renderRow];
        return tiles[tileNum];
    }

    /**
     * Disposes of all loaded textures.
     */
    public void dispose(){
        for (Tile t : tiles) {
            if (t != null && t.image != null) t.image.dispose();
        }
    }
}

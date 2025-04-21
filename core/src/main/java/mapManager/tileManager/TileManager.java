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
import utilities.Constants;
import utilities.TilesSet;

import java.io.*;
import java.util.Map;

public class TileManager {
    public Tile[] tile;
    public static int[][][] mapTileLayers;
    private Texture backgroundImage;


    public TileManager() {
        tile=new Tile[360];

        mapTileLayers = new int [Constants.NUM_LAYERS][Constants.maxScreenCol][Constants.maxScreenrow];

        loadMap(Constants.Map1,0);
        loadMap(Constants.Map2,1);
    }
    public void loadBackgroundImage(){
        backgroundImage = new Texture(Gdx.files.internal(Constants.backgoundImg));
    }
    public void loadMap(String mapName,int layer)  {
        int[][]mapData= new int[Constants.maxScreenCol][Constants.maxScreenrow];
        try(BufferedReader br = new BufferedReader( new FileReader(mapName))){
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < Constants.maxScreenrow) {
                    line = line.trim();
                    if(line.isEmpty()){
                        System.out.println("⚠ Skipped empty line at row " + row);
                continue;}

                    String[] numbers = line.split("\\s+");

                if (numbers.length != Constants.maxScreenCol) {
                    System.out.println("  Invalid number of columns at row " + row + ": " + numbers.length);
                    System.out.println("   Line content: \"" + line + "\"");
                    throw new RuntimeException("Invalid map file format.");
                }
                    for (int col = 0; col < Constants.maxScreenCol; col++) {
                    mapTileLayers[layer][col][row] = Integer.parseInt(numbers[col]);
                }
                    row++;


            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getTilesFromFolder(){
        File folder = new File(Constants.tileFolder);
        File[] directoryListing = folder.listFiles();

        try {
            if (directoryListing != null && directoryListing.length > 0) {
                for (File child : directoryListing) {
                    if (child.isFile()) {
                        String tileName = child.getName().replaceFirst("[.][^.]+$", "");
                        Constants.addTile(tileName, child.getPath());
                    }
                }
            } else {
                System.err.println("Directory is empty or could not be read: " + folder.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error while loading tiles from directory: " + e.getMessage());
        }

    }

    public void getTileImage() {
        final int tileSize      = Constants.tileSize;
        final int defaultSpeed  = AnimatedConfig.DEFAULT_ANIMATION_SPEED;                  // fallback FPS
        for (Map.Entry<String,String> entry : Constants.tilesMap.entrySet()) {
            int   id   = Integer.parseInt(entry.getKey());
            String fn = entry.getValue();

            // skip the “empty” slot
            if (id == 0) {
                tile[id] = null;
                continue;
            }

            // load texture once
            Texture tex = new Texture(Gdx.files.internal(fn));

            // 1) config override?
            AnimatedConfig cfg = AnimatedConfig.animConfigs.get(id);
            if (cfg != null) {
                tile[id] = new AnimatedTile(tex, cfg.getCols(), cfg.getRows(), cfg.getSpeed());

                // 2) generic sprite‑sheet?
            } else if (Constants.animateditems.contains(id)) {
                int cols = tex.getWidth()  / tileSize;
                int rows = tex.getHeight() / tileSize;
                tile[id] = new AnimatedTile(tex, cols, rows, defaultSpeed);

                // 3) static tile
            } else {
                Tile t = new Tile();
                t.image     = tex;
                t.collision = TilesSet.COLLIDABLE_TILES.contains(id);
                tile[id] = t;
            }

            // every tile (even animated ones) needs a Rectangle to dodge NPEs
            tile[id].collisionBox = new Rectangle();
        }
    }

    public void render (SpriteBatch batch, Camera camera, ShapeRenderer shape) {
        float bgX = (camera.position.x - camera.viewportWidth / 2)*1.1f;
        float bgY = (camera.position.y - camera.viewportHeight / 2)*1.1f;

        batch.draw(backgroundImage, bgX, bgY, camera.viewportWidth, camera.viewportHeight);

        int tileSize=Constants.tileSize;
        int colStart=Math.max(0,(int)(camera.position.x-camera.viewportWidth/2)/tileSize);
        int colEnd=Math.min(Constants.maxScreenCol,(int)(camera.position.x+camera.viewportWidth/2)/tileSize+1);
        int rowStart=Math.max(0,(int)(camera.position.y-camera.viewportHeight/2)/tileSize);
        int rowEnd=Math.min(Constants.maxScreenrow,(int)(camera.position.y+camera.viewportWidth/2)/tileSize+1);
        int w=colEnd-colStart;
        System.out.println("col end - colStart "+ w);
        int row = rowStart;
        for (int layer = 0; layer < mapTileLayers.length; layer++) {
            for (row = rowStart; row < rowEnd; row++) {
                for (int col = colStart; col < colEnd; col++) {
                    int renderRow = Constants.maxScreenrow - 1 - row;
                    int tilenum = mapTileLayers[layer][col][renderRow];
                    if (tile[tilenum] instanceof AnimatedTile) {
                        System.out.println("Tilenum: " + tilenum + " | Class: " + tile[tilenum].getClass().getSimpleName());
                        TextureRegion frame = ((AnimatedTile) tile[tilenum]).getCurrentFrame(Gdx.graphics.getDeltaTime());
                        System.out.println("Drawing frame: " + frame.getRegionX() + ", " + frame.getRegionY());
                        batch.draw(frame, col * tileSize, row * tileSize);

                    } else if (tilenum >= 0 && tilenum < tile.length && tile[tilenum] != null && tile[tilenum].image != null) {
                        tile[tilenum].collisionBox = new Rectangle(col*tileSize, row*tileSize, tileSize, tileSize);
                        batch.draw(tile[tilenum].image, col * tileSize, row * tileSize);
                        if (tile[tilenum].collision) {
                            shape.setColor(Color.YELLOW);
                        }else
                            shape.setColor(Color.RED);
                        shape.rect(tile[tilenum].collisionBox.x, tile[tilenum].collisionBox.y, tile[tilenum].collisionBox.width, tile[tilenum].collisionBox.height);

                    }


                }
            }
        }

       }

    public void dispose(){
        for (Tile t : tile) {
            if (t != null && t.image != null) t.image.dispose();
        }
    }

    public Tile[] getTiles(){
        if (tile == null) {
            System.out.println("tile is null");
        }
        else {
            for (int i = 1; i < tile.length; i++) {
                if (tile[i] != null){
                    // debug:
                    System.out.println("Collision box x:" + tile[i].collisionBox.x);
                }
            }
        }
        return tile;
    }

    /**
     * @param worldX  The x‑position in pixels (e.g. entity.x or hitBox.x)
     * @param worldY  The y‑position in pixels (e.g. entity.y or hitBox.y)
     * @param layer   Which layer to query (0=ground, 1=objects, etc)
     * @return        The Tile instance under that pixel, or null if out of bounds
     */
    public Tile getTile(int worldX, int worldY, int layer) {
        int tileSize = Constants.tileSize;
        int col = worldX / tileSize;
        int row = worldY / tileSize;
        // safety check
        if (col < 0 || col >= Constants.maxScreenCol ||
            row < 0 || row >= Constants.maxScreenrow ||
            layer < 0 || layer >= mapTileLayers.length) {
            return null;
        }
        int renderRow = Constants.maxScreenrow - 1 - row;
        int tileNum = mapTileLayers[layer][col][renderRow];
        return tile[tileNum];
    }

}

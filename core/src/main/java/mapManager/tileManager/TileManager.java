package mapManager.tileManager;
import java.awt.*;
import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import utilities.Constants;

import java.io.*;
import java.io.File;
import java.util.Map;

public class TileManager {
    public Tile[] tile;
    public static int  mapTileLayers[][][];
    private Texture backgroundImage;


    public TileManager() {
        tile=new Tile[360];

//        mapTilenum= new int [Constants.maxScreenCol][Constants.maxScreenrow];
        mapTileLayers = new int [Constants.NUM_LAYERS][Constants.maxScreenCol][Constants.maxScreenrow];

        loadMap(Constants.Map1,0);
        loadMap(Constants.Map2,1);
    }
    public void loadbackgroundimg(){
        backgroundImage = new Texture(Gdx.files.internal(Constants.backgoundImg));
    }
    public void loadMap(String mapname,int layer)  {
        int[][]mapData= new int[Constants.maxScreenCol][Constants.maxScreenrow];
        try(BufferedReader br = new BufferedReader( new FileReader(mapname))){
            String line;


            int row=0;

            while ((line = br.readLine()) != null && row < Constants.maxScreenrow) {
                    line=line.trim();
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
        } }

        public  void getTilesFromFolder(){
        File dir = new File(Constants.tileFolder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Do something with child
                String tileName = child.getName().replaceFirst("[.][^.]+$", "");
                Constants.addTile(tileName,child.getPath());

            }
        } else {
            // Handle the case where dir is not really a directory.

        }
    }
    public void gettileimage() {

        for (Map.Entry<String,String> entry :Constants.tilesMap.entrySet()) {

            // debugging tile collision


            if(Constants.animateditems.contains(Integer.parseInt(entry.getKey()))){
                if(Integer.parseInt(entry.getKey())==22){
                    //22
                    tile[Integer.parseInt(entry.getKey())]=new AnimatedTile(new Texture(entry.getValue()),4,1,5);
                }
                else {
                tile[Integer.parseInt(entry.getKey())]=new AnimatedTile(new Texture(entry.getValue()),8,1,5);
            }}
            else if(Integer.parseInt(entry.getKey())==0){
                tile[Integer.parseInt(entry.getKey())]= null;
            }
            else {
                if(Integer.parseInt(entry.getKey())==155){
                    tile[Integer.parseInt(entry.getKey())]=new Tile();
                    tile[Integer.parseInt(entry.getKey())].image = new Texture(entry.getValue());
                    tile[Integer.parseInt(entry.getKey())].collision = true;
                }
                else {
                    tile[Integer.parseInt(entry.getKey())]=new Tile();
                    tile[Integer.parseInt(entry.getKey())].image = new Texture(entry.getValue());
                }
                {
                if(Integer.parseInt(entry.getKey())==155){
                    tile[Integer.parseInt(entry.getKey())]=new Tile();
                    tile[Integer.parseInt(entry.getKey())].image = new Texture(entry.getValue());
                    tile[Integer.parseInt(entry.getKey())].collision = true;
                }
                else {
                    tile[Integer.parseInt(entry.getKey())]=new Tile();
                    tile[Integer.parseInt(entry.getKey())].image = new Texture(entry.getValue());
                }


            }


            }

            // debug: initialise empty collisionBox rectange
            tile[Integer.parseInt(entry.getKey())].collisionBox = new Rectangle();
            System.out.println(entry.getValue());
        }

    }

    public void render (SpriteBatch batch, Camera camera, ShapeRenderer shape) {
        float bgX = (camera.position.x - camera.viewportWidth / 2)*1.1f;
        float bgY = (camera.position.y - camera.viewportHeight / 2)*1.1f;

        batch.draw(backgroundImage, bgX, bgY, camera.viewportWidth, camera.viewportHeight);

        int tilesize=Constants.tileSize;
        int colstart=Math.max(0,(int)(camera.position.x-camera.viewportWidth/2)/tilesize);
        int colend=Math.min(Constants.maxScreenCol,(int)(camera.position.x+camera.viewportWidth/2)/tilesize+1);
        int rowstart=Math.max(0,(int)(camera.position.y-camera.viewportHeight/2)/tilesize);
        int rowend=Math.min(Constants.maxScreenrow,(int)(camera.position.y+camera.viewportWidth/2)/tilesize+1);
        int w=colend-colstart;
        System.out.println("col end - colstart "+ w);
        int row = rowstart;
        for (int layer = 0; layer < mapTileLayers.length; layer++) {
            for (row = rowstart; row < rowend; row++) {
                for (int col = colstart; col < colend; col++) {
                    int renderRow = Constants.maxScreenrow - 1 - row;
                    int tilenum = mapTileLayers[layer][col][renderRow];
                    if (tile[tilenum] instanceof AnimatedTile) {
                        System.out.println("Tilenum: " + tilenum + " | Class: " + tile[tilenum].getClass().getSimpleName());
                        TextureRegion frame = ((AnimatedTile) tile[tilenum]).getCurrentFrame(Gdx.graphics.getDeltaTime());
                        System.out.println("Drawing frame: " + frame.getRegionX() + ", " + frame.getRegionY());
                        batch.draw(frame, col * tilesize, row * tilesize);

                    } else if (tilenum >= 0 && tilenum < tile.length && tile[tilenum] != null && tile[tilenum].image != null) {
                        tile[tilenum].collisionBox = new Rectangle(col*tilesize, row*tilesize, tilesize, tilesize);
                        batch.draw(tile[tilenum].image, col * tilesize, row * tilesize);
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

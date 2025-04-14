package mapManager.tileManager;
import java.awt.*;
import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utilities.Constants;

import java.io.*;
import java.io.File;
import java.util.Map;

public class TileManager {
    Tile[] tile;
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
//            tile[Integer.parseInt(entry.getKey())] = null;
            //animatedtiles
            if(Constants.animateditems.contains(Integer.parseInt(entry.getKey()))){
                tile[Integer.parseInt(entry.getKey())]=new AnimatedTile(new Texture(entry.getValue()),8,1,5);
//                tile[Integer.parseInt(entry.getKey())].image = new Texture(entry.getValue());
            }
            else if(Integer.parseInt(entry.getKey())==0){
                tile[Integer.parseInt(entry.getKey())]= null;
            }
            else {
                tile[Integer.parseInt(entry.getKey())]=new Tile();
                tile[Integer.parseInt(entry.getKey())].image = new Texture(entry.getValue());
            }


            System.out.println(entry.getValue());
//            tile[1] = new Tile();
//            tile[1].image = new Texture(Constants.tile1);
//            tile[59] = new Tile();
//            tile[59].image = new Texture(Constants.tile59);
        }

    }

    public void render (SpriteBatch batch, Camera camera) {
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
//                    if (tilenum >= 0 && tilenum < tile.length && tile[tilenum] != null && tile[tilenum].image != null) {
                    if (tile[tilenum] instanceof AnimatedTile) {
                        System.out.println("Tilenum: " + tilenum + " | Class: " + tile[tilenum].getClass().getSimpleName());
                        TextureRegion frame = ((AnimatedTile) tile[tilenum]).getCurrentFrame(Gdx.graphics.getDeltaTime());
                        System.out.println("Drawing frame: " + frame.getRegionX() + ", " + frame.getRegionY());
                        batch.draw(frame, col * tilesize, row * tilesize);

//                    batch.draw(frame, col * tilesize, row * tilesize);
                    } else if (tilenum >= 0 && tilenum < tile.length && tile[tilenum] != null && tile[tilenum].image != null) {
                        batch.draw(tile[tilenum].image, col * tilesize, row * tilesize);
                    }



                }
                col++;
            }
            row++;
        }

        ///old version
//        int col = 0;
//        int row = 0;
//        int x = 0;
//        int y = 0;
//        while (col < Constants.maxScreenCol && row < Constants.maxScreenrow) {
//            int tilenum = mapTilenum[col][row];
//            int flippedY = (Constants.maxScreenrow - 1 - row) * tile[tilenum].image.getHeight();
//
//            batch.draw(tile[tilenum].image, Constants.tileSize*col, row*Constants.tileSize);
//            col++;
//            x += tile[tilenum].image.getWidth();
//            if (col == Constants.maxScreenCol) {
//                col = 0;
//                x = 0;
//                row++;
//                y += tile[tilenum].image.getHeight();
//            }
//        }

//
//        batch.draw(tile[0].image,tile[0].image.getWidth()*2,0);
//        batch.draw(tile[0].image,tile[0].image.getWidth()*3,0);
//        batch.draw(tile[0].image,tile[0].image.getWidth()*4 ,0);
       }

    public void dispose(){
        for (Tile t : tile) {
            if (t != null && t.image != null) t.image.dispose();
        }
    }



}

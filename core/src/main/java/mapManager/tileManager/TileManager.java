package mapManager.tileManager;
import java.io.File;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import utilities.Constants;

import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.util.Map;

public class TileManager {
    Tile[] tile;
    int mapTilenum[][];



    public TileManager() throws IOException {
        tile=new Tile[85];
        getTilesFromFolder();
        gettileimage();
        mapTilenum= new int [Constants.maxScreenCol][Constants.maxScreenrow];
        loadMap();
    }
    public void loadMap() throws IOException {
        try{

            FileReader is = new FileReader(Constants.Map1);
            BufferedReader br = new BufferedReader(is);
            int col=0;
            int row=0;

            while(col<Constants.maxScreenCol && row<Constants.maxScreenrow) {
                String line = br.readLine();
                System.out.println(col+":"+row);
                while (col < Constants.maxScreenCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTilenum[col][row] = num;
                    col++;
                }
                if (col == Constants.maxScreenCol) {
                    col = 0;
                    row++;
                }

            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

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
    private void gettileimage() {

        for (Map.Entry<String,String> entry :Constants.tilesMap.entrySet()) {
            tile[Integer.parseInt(entry.getKey())] = new Tile();
            tile[Integer.parseInt(entry.getKey())].image = new Texture(entry.getValue());
            System.out.println(entry.getValue());
//            tile[1] = new Tile();
//            tile[1].image = new Texture(Constants.tile1);
//            tile[59] = new Tile();
//            tile[59].image = new Texture(Constants.tile59);
        }

    }

    public void render (SpriteBatch batch) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        while (col < Constants.maxScreenCol && row < Constants.maxScreenrow) {
            int tilenum = mapTilenum[col][row];
            int flippedY = (Constants.maxScreenrow - 1 - row) * tile[tilenum].image.getHeight();

            batch.draw(tile[tilenum].image, x, flippedY);
            col++;
            x += tile[tilenum].image.getWidth();
            if (col == Constants.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += tile[tilenum].image.getHeight();
            }
        }

//
//        batch.draw(tile[0].image,tile[0].image.getWidth()*2,0);
//        batch.draw(tile[0].image,tile[0].image.getWidth()*3,0);
//        batch.draw(tile[0].image,tile[0].image.getWidth()*4 ,0);
       }

    public void dispose(){
        tile[0].image.dispose();
    }



}

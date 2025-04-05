/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import com.sun.j3d.utils.geometry.Box;
import java.util.ArrayList;
import java.util.Random;
import javax.media.j3d.TransformGroup;
import main.CollisionBox;
import main.TG;
import main.TX;

/**
 *
 * @author Israel
 */
public class Cafe2 extends TransformGroup{
    public ArrayList<CollisionBox> collBoxes;
    public Cafe2(){
        Random rand = new Random();
        collBoxes = new ArrayList<>();
        
        Box tileFloor = new Box(6f, 0.05f, 6f, TX.texCoords, TX.createAppearance("floor.png"));
        //TX.setTextureScale(tileFloor.getShape(Box.TOP), 4.0f, 4.0f);
        TransformGroup tgFloor = new TransformGroup();
        tgFloor.addChild(tileFloor);
        TG.moveTG(tgFloor, 0, 0.2f, 0);
        this.addChild(tgFloor);
        
        
        //Left n right walls
        placeWall(0.2f, 1f, 6f, -6, 0, 0, "white_concrete.png");
        placeWall(0.2f, 1f, 6f, 6, 0, 0, "white_concrete.png");
        
        //Back n front walls
        placeWall(6f, 1f, 0.2f, 0, 0, -6, "white_concrete.png");
        placeWall(4.8f, 1f, 0.2f, -1.2f, 0, 6, "white_concrete.png");
        
        //Windows left
        for(int i = 0; i < 4; i++){
            TransformGroup w = new CafeWindow();
            TG.moveTG(w, -6f, 1, 4.5f - (i*3f));
            TG.rotateTG(w, 0, 90, 0);
            this.addChild(w);
        }
        
        //Windows front
        CafeWindow cw = new CafeWindow();
        this.addChild(cw.getPanel(-5.4f, 1, 6f, 180));
        this.addChild(cw.getPanel(3.0f, 1, 6f, 180));
        for(int i = 0; i < 3; i++){
            TransformGroup w = new CafeWindow();
            TG.moveTG(w, -3.8f + (i*2.6f), 1, 6f);
            this.addChild(w);
        }
        
        //Windows back
        this.addChild(cw.getPanel(-5.4f, 1, -6f, 180));
        this.addChild(cw.getPanel(3.0f, 1, -6f, 180));
        for(int i = 0; i < 3; i++){
            TransformGroup w = new CafeWindow();
            TG.moveTG(w, -3.8f + (i*2.6f), 1, -6f);
            this.addChild(w);
        }
        
        //Pillars and sht
        for(int i = 0; i < 5; i++){
            TG.placeCylinder(this, 0.25f, 2.8f, -6f, 1.4f, 6f-(i*3f), 0, 0, 0, "orange_concrete.png");
        }
        for(int i = 0; i < 5; i++){
            TG.placeCylinder(this, 0.25f, 4f, 3.6f, 2.0f, 6f-(i*3f), 0, 0, 0, "orange_concrete.png");
            collBoxes.add(new CollisionBox(3.6f, 6f-(i*3f), 0.2f, 0.2f));
        }
        TG.placeCylinder(this, 0.25f, 3f, 6f, 1.5f, 6f, 0, 0, 0, "orange_concrete.png");
        
        //Roof (woof!)
        TG.placeBox(this, 5f, 0.1f, 8f, -3f, 3.5f, 0, 0, 0, 5, "wood.png");
        TG.placeBox(this, 3f, 0.1f, 8f, 4.6f, 3.8f, 0, 0, 0, -5, "wood.png");
        
        //Tables
        for(int i = 0; i < 4; i++){
            TransformGroup tgTable1 = new TableAndChairs();
            TG.moveTG(tgTable1, -4, 0, 4-i*2.5f);
            TG.rotateTG(tgTable1, 0, -20+rand.nextInt(40), 0);
            this.addChild(tgTable1);
            
            TransformGroup tgTable2 = new TableAndChairs();
            TG.moveTG(tgTable2, -1, 0, 4-i*2.5f);
            TG.rotateTG(tgTable2, 0, -20+rand.nextInt(40), 0);
            this.addChild(tgTable2);
            
            collBoxes.add(new CollisionBox(-4f, 4f-i*2.5f, 0.8f, 0.6f));
            collBoxes.add(new CollisionBox(-1f, 4f-i*2.5f, 0.8f, 0.6f));
        }
    }
    
    private void placeWall(float sx, float sy, float sz, float x, float y, float z, String tex){
        TransformGroup tgWall = TG.customBox(sx, sy, sz, tex);
        TG.moveTG(tgWall, x, y, z);
        this.addChild(tgWall);
        collBoxes.add(new CollisionBox(x, z, sx, sz));
    }
}

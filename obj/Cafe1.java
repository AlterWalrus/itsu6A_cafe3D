/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import com.sun.j3d.utils.geometry.Box;
import java.util.Random;
import javax.media.j3d.TransformGroup;
import main.TG;
import main.TX;

/**
 *
 * @author Israel
 */
public class Cafe1 extends TransformGroup{
    public Cafe1(){
        Random rand = new Random();
        
        Box tileFloor = new Box(6f, 0.01f, 6f, TX.texCoords, TX.createAppearance("floor.png"));
        TX.setTextureScale(tileFloor.getShape(Box.TOP), 4.0f, 4.0f);
        TransformGroup tgFloor = new TransformGroup();
        tgFloor.addChild(tileFloor);
        TG.moveTG(tgFloor, 0, 0.3f, 0);
        this.addChild(tgFloor);
        
        Box tileFloor2 = new Box(2f, 0.01f, 3f, TX.texCoords, TX.createAppearance("floor.png"));
        TX.setTextureScale(tileFloor2.getShape(Box.TOP), 1.0f, 2.0f);
        TransformGroup tgFloor2 = new TransformGroup();
        tgFloor2.addChild(tileFloor2);
        TG.moveTG(tgFloor2, 8f, 0.3f, -3);
        this.addChild(tgFloor2);
        
        //Left n right
        placeWall(0.2f, 1f, 6f, -6, 0, 0, "blueish_concrete.png");
        placeWall(0.2f, 1f, 6f, 6, 0, 0, "blueish_concrete.png");
        
        //Back n front
        placeWall(6f, 1f, 0.2f, 0, 0, -6, "blueish_concrete.png");
        placeWall(4.8f, 1f, 0.2f, -1.2f, 0, 6, "blueish_concrete.png");
        
        //Kitchen
        placeWall(0.2f, 2f, 3f, 10f, 1f, -3f, "blueish_concrete.png");
        placeWall(2f, 3f, 0.2f, 8, 0, -6, "blueish_concrete.png");
        placeWall(2f, 3f, 0.2f, 8, 0, 0, "blueish_concrete.png");
        
        //Roof (woof!)
        
        
        //Window
        for(int i = 0; i < 4; i++){
            TransformGroup w = new CafeWindow();
            TG.moveTG(w, -6f, 1, 4.5f - (i*3f));
            TG.rotateTG(w, 0, 90, 0);
            this.addChild(w);
        }
        
        TransformGroup w1 = new CafeWindow();
        TG.moveTG(w1, -4.5f, 1, 6f);
        this.addChild(w1);
        
        TransformGroup w2 = new CafeWindow();
        TG.moveTG(w2, -1.8f, 1, 6f);
        this.addChild(w2);
        
        TransformGroup w3 = new CafeWindow();
        TG.moveTG(w3, 0.9f, 1, 6f);
        this.addChild(w3);
        
        //Pillars and sht
        TG.placeCylinder(this, 0.25f, 4f, 6f, 1.5f, 6f, 0, 0, 0, "red_concrete.png");
        for(int i = 0; i < 5; i++){
            TG.placeCylinder(this, 0.25f, 4f, 3.6f, 1.5f, 6f-(i*3f), 0, 0, 0, "red_concrete.png");
        }
        for(int i = 0; i < 5; i++){
            TG.placeCylinder(this, 0.25f, 4f, -6f, 1.5f, 6f-(i*3f), 0, 0, 0, "red_concrete.png");
        }
        
        for(int i = 0; i < 4; i++){
            TransformGroup tgTable1 = new TableAndChairs();
            TG.moveTG(tgTable1, -4, 0, 4-i*2.5f);
            TG.rotateTG(tgTable1, 0, -20+rand.nextInt(40), 0);
            this.addChild(tgTable1);
            
            TransformGroup tgTable2 = new TableAndChairs();
            TG.moveTG(tgTable2, -1, 0, 4-i*2.5f);
            TG.rotateTG(tgTable2, 0, -20+rand.nextInt(40), 0);
            this.addChild(tgTable2);
        }
    }
    
    private void placeWall(float sx, float sy, float sz, float x, float y, float z, String tex){
        TransformGroup tgWall = TG.customBox(sx, sy, sz, tex);
        TG.moveTG(tgWall, x, y, z);
        this.addChild(tgWall);
    }
}

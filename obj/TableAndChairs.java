/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import javax.media.j3d.TransformGroup;
import main.TG;

/**
 *
 * @author Israel
 */
public class TableAndChairs extends TransformGroup{
    public TableAndChairs(){
        TransformGroup tgTable = new Table();
        this.addChild(tgTable);
        
        placeChair(-0.4f, 0, -1, 0);
        placeChair(0.4f, 0, -1, 0);
        
        placeChair(-0.4f, 0, 1, 180);
        placeChair(0.4f, 0, 1, 180);
        
        placeChair(-1.4f, 0, 0, 90);
        placeChair(1.4f, 0, 0, -90);
    }
    
    private void placeChair(float x, float y, float z, int rotation){
        TransformGroup tgChair = new Chair();
        TG.moveTG(tgChair, x, y, z);
        TG.rotateTG(tgChair, 0, rotation, 0);
        this.addChild(tgChair);
    }
}

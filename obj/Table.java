/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import com.sun.j3d.utils.geometry.Box;
import javax.media.j3d.TransformGroup;
import main.TG;
import main.TX;

/**
 *
 * @author Israel
 */
public class Table extends TransformGroup{
    public Table(){
        Box base = new Box(1.2f, 0.05f, 0.6f, TX.texCoords, TX.createAppearance("wood.png"));
        
        TransformGroup tgLeg1 = TG.customBox(0.1f, 0.75f, 0.1f, "metal.png");
        TG.moveTG(tgLeg1, 0, -0.75f, 0);
        
        TransformGroup tgLeg2 = TG.customBox(0.8f, 0.05f, 0.1f, "metal.png");
        TG.moveTG(tgLeg2, 0, -0.95f, 0);
        TG.rotateTG(tgLeg2, 0, 35, 0);
        
        TransformGroup tgLeg3 = TG.customBox(0.8f, 0.05f, 0.1f, "metal.png");
        TG.moveTG(tgLeg3, 0, -0.95f, 0);
        TG.rotateTG(tgLeg3, 0, -35, 0);
        
        TG.scaleTG(this, 0.6f);
        TG.moveTG(this, 0, 1.5f, 0);
        this.addChild(base);
        this.addChild(tgLeg1);
        this.addChild(tgLeg2);
        this.addChild(tgLeg3);
    }
}

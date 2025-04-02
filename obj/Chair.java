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
public class Chair extends TransformGroup{
    public Chair(){
        Box base = new Box(0.3f, 0.04f, 0.3f, TX.texCoords, TX.createAppearance("wood.png"));
        
        //LEGS!!!
        TG.placeBox(this, 0.04f, 0.4f, 0.04f, 0.25f, -0.4f, -0.2f, 0, 0, 4, "metal.png");
        TG.placeBox(this, 0.04f, 0.4f, 0.04f, -0.25f, -0.4f, -0.2f, 0, 0, -4, "metal.png");
        TG.placeBox(this, 0.04f, 0.4f, 0.04f, 0.25f, -0.4f, 0.2f, 0, 0, 4, "metal.png");
        TG.placeBox(this, 0.04f, 0.4f, 0.04f, -0.25f, -0.4f, 0.2f, 0, 0, -4, "metal.png");
        
        //BACK!!!
        TG.placeBox(this, 0.04f, 0.4f, 0.04f, 0.25f, 0.4f, -0.2f, 0, 0, -4, "metal.png");
        TG.placeBox(this, 0.04f, 0.4f, 0.04f, -0.25f, 0.4f, -0.2f, 0, 0, 4, "metal.png");
        TG.placeBox(this, 0.25f, 0.05f, 0.003f, 0f, 0.4f, -0.2f, 0, 0, 0, "metal.png");
        TG.placeBox(this, 0.25f, 0.05f, 0.003f, 0f, 0.6f, -0.2f, 0, 0, 0, "metal.png");
        
        TG.scaleTG(this, 0.6f);
        TG.moveTG(this, 0, 1, 0);
        this.addChild(base);
    }
}

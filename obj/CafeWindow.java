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
public class CafeWindow extends TransformGroup{
    public CafeWindow(){
        TG.placeBox(this, 1.24f, 0.06f, 0.06f, 0f, 0f, 0f, 0, 0, 0, "white_metal.png");
        TG.placeBox(this, 1.24f, 0.06f, 0.06f, 0f, 1.28f, 0f, 0, 0, 0, "white_metal.png");
        
        placePanel(this, -0.96f);
        placePanel(this, 0.96f);
        
        TransformGroup tgPanelL = new TransformGroup();
        TransformGroup tgPanelR = new TransformGroup();
        TG.moveTG(tgPanelL, -0.32f, 0, -0.02f);
        TG.moveTG(tgPanelR, 0.32f, 0, -0.02f);
        
        placePanel(tgPanelL, 0);
        placePanel(tgPanelR, 0);
        
        this.addChild(tgPanelL);
        this.addChild(tgPanelR);
    }
    
    private void placePanel(TransformGroup dest, float x_offset){
        TG.placeBox(dest, 0.32f, 0.06f, 0.05f, 0f+x_offset, 0.64f, 0f, 0, 0, 0, "white_metal.png");
        TG.placeBox(dest, 0.05f, 0.64f, 0.05f, 0.32f+x_offset, 0.64f, 0f, 0, 0, 0, "white_metal.png");
        TG.placeBox(dest, 0.05f, 0.64f, 0.05f, -0.32f+x_offset, 0.64f, 0f, 0, 0, 0, "white_metal.png");
    }
    
    public TransformGroup getPanel(float x, float y, float z, int rotation){
        TransformGroup tg = new TransformGroup();
        placePanel(tg, 0f);
        TG.moveTG(tg, x, y, z);
        TG.rotateTG(tg, 0, rotation, 0);
        return tg;
    }
}

 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import com.sun.j3d.utils.geometry.Box;
import java.util.Random;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import obj.*;

/**
 *
 * @author Israel
 */
class MainScene {
    private BranchGroup bgRoot;
    public TransformGroup tgPlayer;
    public TransformGroup tgGround;
    public TransformGroup tgGroundCam;
    
    public MainScene(){
        Random rand = new Random();
        bgRoot = new BranchGroup();
        
        Box ground = new Box(32.0f, 0.25f, 32.0f, TX.texCoords, TX.createAppearance("grass.png"));
        TX.setTextureScale(ground.getShape(Box.TOP), 4.0f, 4.0f);
        tgGround = new TransformGroup();
        tgGround.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TG.moveTG(tgGround, 0.0f, -1.4f, 0.0f);
        tgGround.addChild(ground);
        
        
        tgGroundCam = new TransformGroup();
        tgGroundCam.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgGroundCam.addChild(tgGround);
        
        //Buildings and shit go here xp-----------------------------------------
        
        
        /*
        
        for(int i = 0; i < 4; i++){
            TransformGroup tg = TG.SBBPlane(0, 0, 0, 10, 10, "tree.png");
            TG.moveTG(tg, -15 + rand.nextFloat()*30, 0, -15 + rand.nextFloat()*30);
            TG.rotateTG(tg, 0, rand.nextInt()%90, 0);
            tgGround.addChild(tg);
            
            TransformGroup tg2 = TG.SBBPlane(0, 0, 0, 3, 2, "bush.png");
            TG.moveTG(tg2, -15 + rand.nextFloat()*30, 0, -15 + rand.nextFloat()*30);
            TG.rotateTG(tg2, 0, rand.nextInt()%90, 0);
            tgGround.addChild(tg2);
        }
        */

        
        TransformGroup tgCafe1 = new Cafe1();
        TG.moveTG(tgCafe1, 0, 0, -10);
        tgGround.addChild(tgCafe1);
        
//        Text2D text = new Text2D("Interactuar", new Color3f(1.0f, 1.0f, 1.0f), "Consolas", 32, 1);
//        TransformGroup tgText = new TransformGroup();
//        tgText.addChild(text);
//        TG.moveTG(tgFloor, 0.0f, 0.1f, 0.0f);
//        TG.moveTG(tgText, -0.4f, 0.5f, 0.0f);
        
        //MouseRotate mouseRot = new MouseRotate();
        //mouseRot.setSchedulingBounds(new BoundingSphere());
        //mouseRot.setTransformGroup(tgGroundCam);
        
        float skyboxSize = 28f;
        Box skybox = new Box(-skyboxSize, skyboxSize, skyboxSize, TX.texCoords, TX.createSkyBoxAppearance("skybox\\tile01.png"));
        skybox.setAppearance(Box.LEFT, TX.createSkyBoxAppearance("skybox\\tile04.png"));
        skybox.setAppearance(Box.FRONT, TX.createSkyBoxAppearance("skybox\\tile05.png"));
        skybox.setAppearance(Box.RIGHT, TX.createSkyBoxAppearance("skybox\\tile06.png"));
        skybox.setAppearance(Box.BACK, TX.createSkyBoxAppearance("skybox\\tile07.png"));
        skybox.setAppearance(Box.BOTTOM, TX.createSkyBoxAppearance("skybox\\tile09.png"));
        tgGroundCam.addChild(skybox);
        
        //Player----------------------------------------------------------------
        tgPlayer = TG.customBox(0.25f, 0.5f, 0.25f, "test.png");
        TG.moveTG(tgPlayer, 0, -0.6f, 0);
        tgGroundCam.addChild(tgPlayer);
        
        bgRoot.addChild(tgGroundCam);

        //tgBox.addChild(mouseRot);
        //bgRoot.addChild(tgText);
        
        tgGround.addChild(TG.light(-4f, 6f, 6f, 6f));
    }

    public BranchGroup get(){
        return bgRoot;
    }
}

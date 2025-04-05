 /*x
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;    
import com.sun.j3d.utils.geometry.Box;
import java.util.ArrayList;
import java.util.Random;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
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
    public ArrayList<CollisionBox> collBoxes;
    public ArrayList<CollisionBox> interBoxes;
    
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
        
        Cafe1 tgCafe1 = new Cafe1();
        TG.moveTG(tgCafe1, 0f, 0.05f, -16f);
        tgGround.addChild(tgCafe1);
        
        Cafe2 tgCafe2 = new Cafe2();
        TG.moveTG(tgCafe2, -16f, 0.05f, 0f);
        tgGround.addChild(tgCafe2);
        
        Cafe2 tgCafe3 = new Cafe2();
        TG.moveTG(tgCafe3, 16f, 0.05f, 0f);
        tgGround.addChild(tgCafe3);
        
//        Text2D text = new Text2D("Interactuar", new Color3f(1.0f, 1.0f, 1.0f), "Consolas", 32, 1);
//        TransformGroup tgText = new TransformGroup();
//        tgText.addChild(text);
//        TG.moveTG(tgFloor, 0.0f, 0.1f, 0.0f);
//        TG.moveTG(tgText, -0.4f, 0.5f, 0.0f);
        
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
        
        //Collisions!!! IWANNAKILLMYSELF----------------------------------------
        collBoxes = new ArrayList<>();
        interBoxes = new ArrayList<>();
        Transform3D t3d = new Transform3D();
        Vector3f posCafe = new Vector3f();
        
        //Cafe 1
        tgCafe1.getTransform(t3d);
        t3d.get(posCafe);
        for(int i = 0; i < tgCafe1.collBoxes.size(); i++){
            tgCafe1.collBoxes.get(i).setOffset(posCafe);
            collBoxes.add(tgCafe1.collBoxes.get(i));
        }
        
        //Cafe 2
        tgCafe2.getTransform(t3d);
        t3d.get(posCafe);
        for(int i = 0; i < tgCafe2.collBoxes.size(); i++){
            tgCafe2.collBoxes.get(i).setOffset(posCafe);
            collBoxes.add(tgCafe2.collBoxes.get(i));
        }
        
        
        
        //Outer walls, clunky but works xp
        Box wallBox = new Box(32f, 2f, 0.5f, TX.texCoords, TX.createAppearance("wall.png"));
        TX.setTextureScale(wallBox.getShape(Box.FRONT), 8f, 1.0f);
        placeOuterWall(0f, -32f, 0);
        placeOuterWall(0f, 32f, 180);
        placeOuterWall(32f, 0f, 270);
        placeOuterWall(-32f, 0f, 90);
        collBoxes.add(new CollisionBox(0f, -32f, 32f, 0.5f));
        collBoxes.add(new CollisionBox(0f, 32f, 32f, 0.5f));
        collBoxes.add(new CollisionBox(32f, 0f, 0.5f, 32f));
        collBoxes.add(new CollisionBox(-32f, 0f, 0.5f, 32f));
        
        //Interactables nyehehehe
        interBoxes.add(new CollisionBox(2, 2, 4, 4));
    }
    
    private void placeOuterWall(float x, float z, int rotation){
        Box wallBox = new Box(32f, 2f, 0.5f, TX.texCoords, TX.createAppearance("wall.png"));
        TransformGroup tg = new TransformGroup();
        tg.addChild(wallBox);
        TG.moveTG(tg, x, 2f, z);
        TG.rotateTG(tg, 0, rotation, 0);
        tgGround.addChild(tg);
    }

    public BranchGroup get(){
        return bgRoot;
    }
}

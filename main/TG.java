/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.PointLight;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

/**
 *
 * @author Israel
 */
public class TG{
    public static TransformGroup customBox(float sx, float sy, float sz, String tex){
        Box box = new Box(sx, sy, sz, TX.texCoords, TX.createAppearance(tex));
        TransformGroup tgBox = new TransformGroup();
        tgBox.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBox.addChild(box);
        return tgBox;
    }
    
    public static void placeBox(TransformGroup dest, float sx, float sy, float sz, float x, float y, float z, int rx, int ry, int rz, String tex){
        TransformGroup tgBox = customBox(sx, sy, sz, tex);
        TG.moveTG(tgBox, x, y, z);
        TG.rotateTG(tgBox, rx, ry, rz);
        dest.addChild(tgBox);
    }
    
    public static TransformGroup customCylinder(float radius, float height, String tex){
        Cylinder cylinder = new Cylinder(radius, height, TX.texCoords, TX.createAppearance(tex));
        TransformGroup tgCylinder = new TransformGroup();
        tgCylinder.addChild(cylinder);
        return tgCylinder;
    }
    
    public static void placeCylinder(TransformGroup dest, float width, float height, float x, float y, float z, int rx, int ry, int rz, String tex){
        TransformGroup tgCyl = customCylinder(width, height, tex);
        TG.moveTG(tgCyl, x, y, z);
        TG.rotateTG(tgCyl, rx, ry, rz);
        dest.addChild(tgCyl);
    }
    
    public static Shape3D customPlane(float x, float y, float z, float width, float height, String tex){
        Appearance app = TX.createAppearance(tex);
        
        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.0f);
        app.setTransparencyAttributes(ta);
        
        QuadArray plane = new QuadArray(4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
        plane.setCoordinate(0, new Point3f(x, y, z));
        plane.setCoordinate(1, new Point3f(x+width, y, z));
        plane.setCoordinate(2, new Point3f(x+width, y+height, z));
        plane.setCoordinate(3, new Point3f(x, y+height, z));
            
        plane.setTextureCoordinate(0, 0, new TexCoord2f(0.0f, 0.0f));
        plane.setTextureCoordinate(0, 1, new TexCoord2f(1.0f, 0.0f));
        plane.setTextureCoordinate(0, 2, new TexCoord2f(1.0f, 1.0f));
        plane.setTextureCoordinate(0, 3, new TexCoord2f(0.0f, 1.0f));
        return new Shape3D(plane, app);
    }
    
    public static TransformGroup SBBPlane(float x, float y, float z, float width, float height, String tex){
        TransformGroup tgBillboard = new TransformGroup();
        
        TransformGroup tgTree1 = new TransformGroup();
        tgTree1.addChild(TG.customPlane(x, y, z, width, height, tex));
        TG.rotateTG(tgTree1, 0, 90, 0);
        TG.moveTG(tgTree1, -width/2, 0, width/2);
        tgBillboard.addChild(tgTree1);
        
        TransformGroup tgTree2 = new TransformGroup();
        tgTree2.addChild(TG.customPlane(x, y, z, width, height, tex));
        TG.rotateTG(tgTree2, 0, 180, 0);
        TG.moveTG(tgTree2, -width, 0, 0);
        tgBillboard.addChild(tgTree2);
        
        TransformGroup tgTree3 = new TransformGroup();
        tgTree3.addChild(TG.customPlane(x, y, z, width, height, tex));
        TG.rotateTG(tgTree3, 0, 270, 0);
        TG.moveTG(tgTree3, -width/2, 0, -width/2);
        tgBillboard.addChild(tgTree3);
        
        tgBillboard.addChild(TG.customPlane(x, y, z, width, height, tex));
        return tgBillboard;
    }
    
    public static PointLight light(float x, float y, float z, float intensity){
        return colorLight(x, y, z, intensity, intensity, intensity);
    }
    
    public static PointLight colorLight(float x, float y, float z, float r, float g, float b){
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        PointLight pointLight = new PointLight(
            new Color3f(r, g, b),
            new Point3f(x, y, z),
            new Point3f(1.0f, 0.0f, 0.0f)
        );
        pointLight.setInfluencingBounds(bounds);
        return pointLight;
    }
    
    public static void moveTG(TransformGroup tg, float x, float y, float z){
        Transform3D t3dRead = new Transform3D();
        Transform3D t3dWrite = new Transform3D();
        
        tg.getTransform(t3dRead);
        t3dWrite.set(new Vector3f(x, y, z));
        
        t3dRead.mul(t3dWrite);
        tg.setTransform(t3dRead);
    }
    
    public static void rotateTG(TransformGroup tg, int x, int y, int z){
        Transform3D t3dRead = new Transform3D();
        Transform3D t3dWrite = new Transform3D();
        
        tg.getTransform(t3dRead);
        t3dWrite.rotX(Math.PI / 180 * x);
        t3dRead.mul(t3dWrite);
        t3dWrite.rotY(Math.PI / 180 * y);
        t3dRead.mul(t3dWrite);
        t3dWrite.rotZ(Math.PI / 180 * z);
        t3dRead.mul(t3dWrite);
        
        t3dRead.mul(t3dWrite);
        tg.setTransform(t3dRead);
    }
    
    public static void scaleTG(TransformGroup tg, float x){
        Transform3D t3dRead = new Transform3D();
        Transform3D t3dWrite = new Transform3D();
        
        tg.getTransform(t3dRead);
        t3dWrite.setScale(x);
        
        t3dRead.mul(t3dWrite);
        tg.setTransform(t3dRead);
    }
}

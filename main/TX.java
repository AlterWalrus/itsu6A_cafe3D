/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import java.awt.Container;
import java.io.File;
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.TexCoord2f;

/**
 *
 * @author Israel
 */
public class TX{
    public static int texCoords = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
    
    public static Appearance createAppearance(String tex){
        Appearance app = new Appearance();
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\img\\";
        TextureLoader loader = new TextureLoader((path+tex), new Container());
        Texture texture = loader.getTexture();
        
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        app.setTexture(texture);
        
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.2f, 0.2f, 0.2f));
        material.setDiffuseColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setSpecularColor(new Color3f(0.1f, 0.1f, 0.1f));
        //material.setShininess(50.0f);
        app.setMaterial(material);
        
        return app;
    }
    
    public static void setTextureScale(Shape3D shape, float scaleX, float scaleY) {
        GeometryArray geom = (GeometryArray) shape.getGeometry();

        int vertexCount = geom.getVertexCount();
        TexCoord2f[] texCoords = new TexCoord2f[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            // Retrieve original texture coordinates
            TexCoord2f coord = new TexCoord2f();
            geom.getTextureCoordinate(0, i, coord);

            // Scale texture coordinates
            coord.set(coord.x * scaleX, coord.y * scaleY);
            texCoords[i] = coord;
        }

        // Apply new texture coordinates
        geom.setTextureCoordinates(0, 0, texCoords);
    }
    
    public static Appearance createSkyBoxAppearance(String tex){
        Appearance app = new Appearance();
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\img\\";
        TextureLoader loader = new TextureLoader((path+tex), new Container());
        Texture texture = loader.getTexture();
        app.setTexture(texture);
        
        RenderingAttributes ra = new RenderingAttributes();
        ra.setDepthBufferEnable(false);
        ra.setDepthBufferWriteEnable(false);
        
        app.setRenderingAttributes(ra);
        return app;
    }
}

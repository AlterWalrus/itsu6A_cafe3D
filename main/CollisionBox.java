/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.vecmath.Vector3f;

/**
 *
 * @author Israel
 */
public class CollisionBox{
    public float x1, z1 ,x2, z2;
    public CollisionBox(float x, float z, float width, float length){
        x *= -1;
        z *= -1;
        width += 0.25f;
        length += 0.25f;
        this.x1 = x-width;
        this.z1 = z-length;
        this.x2 = x+width;
        this.z2 = z+length;
    }
    
    public void setOffset(Vector3f offset){
        x1 -= offset.x;
        z1 -= offset.z;
        x2 -= offset.x;
        z2 -= offset.z;
    }
}

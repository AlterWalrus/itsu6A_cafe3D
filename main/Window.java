/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author Israel
 */
public class Window extends javax.swing.JFrame implements KeyListener{
    private HashMap<Integer, Boolean> keyStates = new HashMap<>();
    
    private float spd = 0.08f;
    private MainScene scene;
    private int playerRotation = 0;
     
    /**
     * Creates new form Window
     */
    public Window() {
        initComponents();
        
        int screenWidth = 660;
        int screenHeight = 660;
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        canvas.setBounds(0, 0, screenWidth, screenHeight);
        this.add(canvas);
        this.setBounds(480, 0, screenWidth, screenHeight);
        scene = new MainScene();
        BranchGroup bgScene = scene.get();
        SimpleUniverse n = new SimpleUniverse(canvas);
        n.getViewingPlatform().setNominalViewingTransform();
        n.addBranchGraph(bgScene);
        
        Joystick joystick = new Joystick();
        joystick.start();
        
        this.addKeyListener(this);
        new Thread(this::updateMovement).start();
        new Thread(this::updatePlayerRotation).start();
        
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keyStates.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyStates.put(e.getKeyCode(), false);
    }
    
    private void updateMovement(){
        Transform3D t3d = new Transform3D();
        Matrix3f mt = new Matrix3f();
        float moveX = 0f;
        float moveZ = 0f;
        int keys = 0;
        while(true){
            keys = 0;
            moveX = 0f;
            moveZ = 0f;
            scene.tgGroundCam.getTransform(t3d);
            t3d.getRotationScale(mt);
            
            //WASD
            if(keyStates.getOrDefault(KeyEvent.VK_W, false)){
                moveX += -spd*mt.m02;
                moveZ += spd*mt.m00;
                keys++;
            }
            if(keyStates.getOrDefault(KeyEvent.VK_S, false)){
                moveX += spd*mt.m02;
                moveZ += -spd*mt.m00;
                keys++;
            }
            if(keyStates.getOrDefault(KeyEvent.VK_A, false)){
                moveX += spd*mt.m00;
                moveZ += spd*mt.m02;
                keys++;
            }
            if(keyStates.getOrDefault(KeyEvent.VK_D, false)){
                moveX += -spd*mt.m00;
                moveZ += -spd*mt.m02;
                keys++;
            }
            
            //Diagonal fix
            if(keys > 1){
                moveX *= 0.7071f;
                moveZ *= 0.7071f;
            }
            
            //Collision (i wanna kms)
            scene.tgGround.getTransform(t3d);
            Vector3f pos = new Vector3f();
            t3d.get(pos);
            if(isColliding(pos.x+moveX, pos.z+moveZ)){
                moveX = 0f;
                moveZ = 0f;
            }
            TG.moveTG(scene.tgGround, moveX, 0, moveZ);
            
            //Arrows
            if(keyStates.getOrDefault(KeyEvent.VK_RIGHT, false)){
                TG.rotateTG(scene.tgGroundCam, 0, 5, 0);
                playerRotation -= 5;
            }
            if(keyStates.getOrDefault(KeyEvent.VK_LEFT, false)){
                TG.rotateTG(scene.tgGroundCam, 0, -5, 0);
                playerRotation += 5;
            }
            if(keyStates.getOrDefault(KeyEvent.VK_UP, false)){
                TG.rotateTG(scene.tgGroundCam, (int)(-mt.m00*2.0f), 0, (int)(-mt.m02*2.0f));
            }
            if(keyStates.getOrDefault(KeyEvent.VK_DOWN, false)){
                TG.rotateTG(scene.tgGroundCam, (int)(mt.m00*2.0f), 0, (int)(mt.m02*2.0f));
            }

            try{
                Thread.sleep(16);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    
    private boolean isColliding(float posX, float posZ){
        for(int i = 0; i < scene.collBoxes.size(); i++){
            float x1 = scene.collBoxes.get(i).x1;
            float z1 = scene.collBoxes.get(i).z1;
            float x2 = scene.collBoxes.get(i).x2;
            float z2 = scene.collBoxes.get(i).z2;
            if(isCollidingAt(posX, posZ, x1, z1, x2, z2)){
                return true;
            }
        }
        return false;
    }
    
    private boolean isCollidingAt(float posX, float posZ, float x1, float z1, float x2, float z2){
        boolean colliding = false;
        if((posX >= x1 && posZ >= z1) && (posX <= x2 && posZ <= z2)){
            colliding = true;
        }
        return colliding;
    }
    
    private class Joystick extends Thread{
        private SerialPort serialPort;

        @Override
        public void run(){
            Transform3D t3d = new Transform3D();
            Matrix3f mt = new Matrix3f();
            serialPort = SerialPort.getCommPort("COM4");
            serialPort.setBaudRate(9600);
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

            if(!serialPort.openPort()){
                System.out.println("epic fail lmao");
                return;
            }
            System.out.println("gud Bv");

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()))){
                while(true){
                    if(serialPort.bytesAvailable() > 0){
                        String line = reader.readLine();
                        if(line.matches("\\d+:\\d+:\\d+:\\d+")){
                            scene.tgGroundCam.getTransform(t3d);
                            t3d.getRotationScale(mt);
                            String[] values = line.split(":");
                            float x = ((Integer.parseInt(values[0]) / 50) - 10) / 6f * spd;
                            float y = ((Integer.parseInt(values[1]) / 50) - 10) / 6f * spd;
                            int rotX = (Integer.parseInt(values[2]) / 100) - 5;
                            //int rotY = (Integer.parseInt(values[3]) / 100) - 5;

                            //Movement
                            float moveX = (-x * mt.m00 + y * mt.m02);
                            float moveY = (-x * mt.m02 - y * mt.m00);
                            
                            //Collision (i wanna kms) this one uses Y instead of Z so beware dat
                            scene.tgGround.getTransform(t3d);
                            Vector3f pos = new Vector3f();
                            t3d.get(pos);
                            if(isColliding(pos.x+moveX, pos.z+moveY)){
                                moveX = 0f;
                                moveY = 0f;
                            }
                            
                            TG.moveTG(scene.tgGround, moveX, 0, moveY);
                            
                            //Camera
                            TG.rotateTG(scene.tgGroundCam, 0, rotX, 0);
                            playerRotation -= rotX;
                        }
                    }
                    Thread.sleep(16);
                }
            }catch(Exception e){
                System.err.println("Serial Error: " + e.getMessage());
            }finally{
                serialPort.closePort();
            }
        }
    }
    
    private void updatePlayerRotation(){
        int rot = 0;
        int intensity = 4;
        while(true){
            if(rot < playerRotation){
                TG.rotateTG(scene.tgPlayer, 0, intensity, 0);
                rot += intensity;
            }
            
            if(rot > playerRotation){
                TG.rotateTG(scene.tgPlayer, 0, -intensity, 0);
                rot -= intensity;
            }
            
            try{
                Thread.sleep(16);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

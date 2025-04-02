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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Transform3D;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.vecmath.Matrix3f;

/**
 *
 * @author Israel
 */
public class Window extends javax.swing.JFrame implements KeyListener{
    private HashMap<Integer, Boolean> keyStates = new HashMap<>();
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
        //float spd = 0.9f;
        float spd = 0.08f;
        Transform3D t3d = new Transform3D();
        Matrix3f mt = new Matrix3f();
        while(true){
            scene.tgGroundCam.getTransform(t3d);
            t3d.getRotationScale(mt);
            
            //WASD
            if(keyStates.getOrDefault(KeyEvent.VK_W, false)){
                TG.moveTG(scene.tgGround, -spd*mt.m02, 0, spd*mt.m00);
            }
            if(keyStates.getOrDefault(KeyEvent.VK_S, false)){
                TG.moveTG(scene.tgGround, spd*mt.m02, 0, -spd*mt.m00);
            }
            if(keyStates.getOrDefault(KeyEvent.VK_A, false)){
                TG.moveTG(scene.tgGround, spd*mt.m00, 0, spd*mt.m02);
            }
            if(keyStates.getOrDefault(KeyEvent.VK_D, false)){
                TG.moveTG(scene.tgGround, -spd*mt.m00, 0, -spd*mt.m02);
            }
            
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
    
    private class Joystick extends Thread{
        private SerialPort serialPort;
        @Override
        public void run(){
            Transform3D t3d = new Transform3D();
            Matrix3f mt = new Matrix3f();
            
            serialPort = SerialPort.getCommPort("COM4");
            if(serialPort.openPort()){
                System.out.println("W");
            }

            if(serialPort == null || !serialPort.isOpen()){
                System.out.println("epic fail lol");
                return;
            }
            serialPort.setBaudRate(9600);
            
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            InputStream inputStream = serialPort.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            try{
                while(reader.ready()){
                    reader.readLine();
                }
                
                while(true){
                    if(serialPort.bytesAvailable() > 0){
                        line = reader.readLine();
                        if(line != null && !line.isEmpty()){
                            line = line.replaceAll("[^0-9:\\-]", "").trim();
                            if(line.matches("\\d+:\\d+")){
                                scene.tgGroundCam.getTransform(t3d);
                                t3d.getRotationScale(mt);
                                
                                String[] values = line.split(":");
                                int x = (Integer.parseInt(values[0]) / 100) - 5;
                                int y = (Integer.parseInt(values[1]) / 100) - 5;
                                
                                float moveX = -x * mt.m00 + y * mt.m02;
                                float moveY = -x * mt.m02 - y * mt.m00;
                                
                                TG.moveTG(scene.tgGround, (moveX/30.0f), 0, (moveY/30.0f));
                                //TG.rotateTG(scene.tgGroundCam, 0, x, 0);
                            }else{
                                System.out.println("shit went bad: " + line);
                            }
                        }
                    }else{
                        Thread.sleep(10);
                    }
                }
            }catch (IOException | InterruptedException ex) {
                System.out.println("Error reading from serial port: " + ex.getMessage());
            } finally {
                try {
                    reader.close();
                    inputStream.close();
                    serialPort.closePort();
                } catch (IOException ex) {
                    System.out.println("Failed to close resources: " + ex.getMessage());
                }
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

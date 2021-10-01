/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sphynx.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author josue
 */
public class UIScrollBar extends BasicScrollBarUI implements MouseListener {
    private Graphics2D graphicsBarra;
    private Rectangle rectangleBarra;
    private JComponent barra;
    // private JComponent 
    
    public UIScrollBar() {
        super();
    }
    
    // Al usar UIManager.put este metodo tiene que estar si o si en las clases heredadas
    public static ComponentUI createUI(JComponent c) { 
        return new UIScrollBar(); 
    } 

    @Override 
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        BufferedImage image = new BufferedImage (16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = (Graphics2D)image.getGraphics();
        g1.setColor(Color.decode("#FFFFFF"));
        g1.fillRect(0, 0, 16, 16);
        g1.dispose();

        g.drawImage(image, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, null);
    } 

    @Override 
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) { 
        c.addMouseListener(this);

        graphicsBarra = (Graphics2D)g;
        rectangleBarra = thumbBounds;
        BufferedImage image = new BufferedImage (16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = (Graphics2D)image.getGraphics ();
        g1.setColor (Color.decode("#006699AA"));
        g1.fillRect (0, 0, 16, 16);
        g1.dispose ();

        graphicsBarra.drawImage(image, thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, null);
    } 

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("jiji");
        BufferedImage image = new BufferedImage (16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = (Graphics2D)image.getGraphics ();
        g1.setColor (Color.BLACK);
        g1.fillRect (0, 0, 16, 16);
        g1.dispose ();

        graphicsBarra.drawImage(image, rectangleBarra.x, rectangleBarra.y, rectangleBarra.width, rectangleBarra.height, null);
        
        // barra.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}

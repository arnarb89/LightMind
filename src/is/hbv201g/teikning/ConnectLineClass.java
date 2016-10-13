/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hbv201g.teikning;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;

/**
 *
 * @author arnarbarri
 */
public class ConnectLineClass implements teiknanlegt{
    TeikniPanel06 teiknari;
    
    public int xPos;
    public int yPos;
    public int xPos2;
    public int yPos2;
    
    
    //Smiðurinn fyrir línurnar.
    public ConnectLineClass(TeikniPanel06 teiknir){
        teiknari = teiknir;
    }
    
    
    public int GetX(){
        //if(xPos<xPos2) return this.xPos;
        //else return this.xPos2;
        return this.xPos;
    }
    public int GetY(){
        //if(yPos<yPos2) return this.yPos;
        //else return this.yPos2;
        return this.yPos;
    }
    public int GetX2(){
        //if(xPos<xPos2) return this.xPos2;
        //else return this.xPos;
        return this.xPos2;
    }
    public int GetY2(){
        //if(yPos<yPos2) return this.yPos2;
        //else return this.yPos;
        return this.yPos2;
    }
    
    // Þetta fall paintar línurnar
    public void painT(Graphics g){
        /*Graphics2D g2 = (Graphics2D) g.create();
        
        g.setColor(Color.BLACK);
        Graphics2D g2dashed = (Graphics2D)g.create();
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 4);
        g2dashed.setStroke(dashed);
        g2dashed.drawRect(this.GetX(),this.GetY(),(this.GetX2()-this.GetX()),(this.GetY2()-this.GetY()));
        g2dashed.dispose();*/
        //Graphics2D g2 = (Graphics2D)g;
        
        
        Graphics2D g2 = (Graphics2D) g.create();
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(renderHints);
        
        CubicCurve2D c = new CubicCurve2D.Double();
        c.setCurve(this.GetX(), this.GetY(),this.GetX()+(this.GetX2()-this.GetX())/2, this.GetY(), this.GetX()+(this.GetX2()-this.GetX())/2, this.GetY2(),this.GetX2(), this.GetY2());
        
        g2.setPaint( new GradientPaint(xPos, yPos, Color.GRAY, xPos2, yPos2, Color.black, false));
        g2.draw(c); 
        g2.setColor(Color.black);
        g2.fillOval(xPos2-2, yPos2-2, 4, 4);
        g2.drawOval(xPos2-4, yPos2-4, 8, 8);
        g2.dispose();
    }
}

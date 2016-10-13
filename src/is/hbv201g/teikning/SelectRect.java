/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hbv201g.teikning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;

/**
 *
 * @author arnarbarri
 */
public class SelectRect implements teiknanlegt{
    TeikniPanel06 teiknari;
    
    public int xPos;
    public int yPos;
    public int xPos2;
    public int yPos2;
    
    public int GetX(){
        if(xPos<xPos2) return this.xPos;
        else return this.xPos2;
    }
    public int GetY(){
        if(yPos<yPos2) return this.yPos;
        else return this.yPos2;
    }
    public int GetX2(){
        if(xPos<xPos2) return this.xPos2;
        else return this.xPos;
    }
    public int GetY2(){
        if(yPos<yPos2) return this.yPos2;
        else return this.yPos;
    }
    
    
    //Smiðurinn fyrir línurnar.
    public SelectRect(TeikniPanel06 teiknir){
        teiknari = teiknir;
    }
    
    // Þetta fall paintar línurnar
    public void painT(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        
        g.setColor(Color.BLACK);
        Graphics2D g2dashed = (Graphics2D)g.create();
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 4);
        g2dashed.setStroke(dashed);
        g2dashed.drawRect(this.GetX(),this.GetY(),(this.GetX2()-this.GetX()),(this.GetY2()-this.GetY()));
        g2dashed.dispose();
    }
}

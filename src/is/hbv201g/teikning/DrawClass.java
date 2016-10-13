/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hbv201g.teikning;

import java.awt.BasicStroke;
import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_ROUND;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 *
 * @author arnarbarri
 */
public class DrawClass implements teiknanlegt{
    TeikniPanel06 teiknari;
    
    public int OriginHlutur = 0;
    public Color litur;
    public Point2D[] pointarray = new Point2D[10];
    public int pointcounter = 0;
    public int drawlinewidth = 5;
    
    
    public DrawClass(TeikniPanel06 teiknir){
        teiknari = teiknir;
    }
    
    
    public void AddPoint(Point2D x){
        {
            if(pointcounter==pointarray.length)ResizePoints();
            pointarray[pointcounter++] = x;
        }
        
        
        
    }
    public void ResizePoints(){
        Point2D[] pointarray2 = new Point2D[pointarray.length*2];
        for(int i = 0;i<pointcounter;i++){
            pointarray2[i] = pointarray[i];
        }
        pointarray = pointarray2;
        
        
        
    }

    @Override
    public void painT(Graphics g) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setColor(litur);
        Stroke dashed = new BasicStroke(drawlinewidth,CAP_ROUND,JOIN_ROUND);
        g2.setStroke(dashed);
        if(pointcounter==1){
            g2.drawLine((int)pointarray[0].getX()+teiknari.fletir[OriginHlutur].xPos,(int)pointarray[0].getY()+teiknari.fletir[OriginHlutur].yPos,(int)pointarray[0].getX()+1+teiknari.fletir[OriginHlutur].xPos,(int)pointarray[0].getY()+teiknari.fletir[OriginHlutur].yPos);
            
        }
        if(pointcounter>1){
            for(int i = 0;i<(pointcounter-1);i++){
                g2.drawLine((int)pointarray[i].getX()+teiknari.fletir[OriginHlutur].xPos,(int)pointarray[i].getY()+teiknari.fletir[OriginHlutur].yPos,(int)pointarray[i+1].getX()+teiknari.fletir[OriginHlutur].xPos,(int)pointarray[i+1].getY()+teiknari.fletir[OriginHlutur].yPos);
                
            }
        }
        g2.setColor(Color.black);
    
    }
    
}

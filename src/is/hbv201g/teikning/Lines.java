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
import java.awt.geom.*;

/**
 *
 * @author arnarbarri
 */
public class Lines implements teiknanlegt{
    TeikniPanel06 teiknari;
    
    // Breyta sem heldur utan um hvaða hlutur línan tengist frá
    public int OriginHlutur;
    // Breyta sem heldur utan um hvaða hlutur línan tengist til
    public int EndHlutur;
    
    // Fall sem nær í X hnit hlutarins sem línan tengist frá
    public int GetLineOriginX(){
        int x = teiknari.fletir[OriginHlutur].xPos+teiknari.fletir[OriginHlutur].getWidth()/2;
        return x;
    }
    // Fall sem nær í Y hnit hlutarins sem línan tengist frá
    public int GetLineOriginY(){
        int x = teiknari.fletir[OriginHlutur].yPos+teiknari.fletir[OriginHlutur].getHeightNew()/2;
        return x;
    }
    // Fall sem nær í X hnit hlutarins sem línan tengist til
    public int GetLineEndX(){
        int x = teiknari.fletir[EndHlutur].xPos+teiknari.fletir[EndHlutur].getWidth()/2;
        //int x = teiknari.fletir[EndHlutur].xPos;
        return x;
    }
    // Fall sem nær í Y hnit hlutarins sem línan tengist til
    public int GetLineEndY(){
        int x = teiknari.fletir[EndHlutur].yPos+teiknari.fletir[EndHlutur].getHeightNew()/2;
        //int x = teiknari.fletir[EndHlutur].yPos;
        return x;
    }
    
    
    
    //Smiðurinn fyrir línurnar.
    public Lines(TeikniPanel06 teiknir){
        teiknari = teiknir;
    }
    public Lines(TeikniPanel06 teiknir, int x, int y){
        teiknari = teiknir;
        OriginHlutur = x;
        EndHlutur = y;
    }
    // Þetta fall paintar línurnar
    public void painT(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(renderHints);
        CubicCurve2D c = new CubicCurve2D.Double();
        c.setCurve(this.GetLineOriginX(), this.GetLineOriginY(),this.GetLineOriginX()+(this.GetLineEndX()-this.GetLineOriginX())/2, this.GetLineOriginY(), this.GetLineOriginX()+(this.GetLineEndX()-this.GetLineOriginX())/2, this.GetLineEndY(),this.GetLineEndX(), this.GetLineEndY());
        //g2.setPaint( new GradientPaint(GetLineOriginX(), GetLineOriginY(), Color.GRAY, GetLineEndX(), GetLineEndY(), Color.black, false));
        g2.setPaint( new GradientPaint(GetLineOriginX(), GetLineOriginY(), Color.lightGray, GetLineEndX(), GetLineEndY(), Color.black, false));
        g2.draw(c);      
        
    }
}

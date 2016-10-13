/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hbv201g.teikning;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
/**
 *
 * @author arnarbarri
 */

// Klassi Flotur sem býr til hlutina.
public class Flotur implements teiknanlegt{
    TeikniPanel06 teiknari;
    Random randomtala = new Random(); //Breyta sem geymir random tölur.
    
    
    // Smiðurinn fyrir hlutina.
    public Flotur(TeikniPanel06 panel){
        teiknari = panel;
    }
    
    
    public int shape = 0; // Breyta sem heldur utan um lögun hlutarins
    public void SetShape(int x){ // Fall sem setur lögun hlutarins
        this.shape = x;
    }
    
    
    
    public int IsSelected = 0; // Breyta sem segir til hvort hlutur sé valinn eða ekki.
    public void setValdi(int i) // Fall til að breyta því hvort hlutur sé valinn eða óvalinn.
    {
        this.IsSelected = i;
    }
    
    
    
    public int xPos = randomtala.nextInt(357)+5; // x hnit hlutar
    public void setX(int xPos){ // Fall til að setja X hnit hlutar
        this.xPos = xPos;
    }
    public int getX(){ // Fall til að ná í X hnit hlutar
        return xPos;
    }
    
    
    
    
    public int yPos = randomtala.nextInt(168)+3; // y hnit hlutar
    public void setY(int yPos){// Fall til að setja Y hnit hlutar
        this.yPos = yPos;
    }
    public int getY(){// Fall til að ná í Y hnit hlutar
        return yPos;
    }
    
    
    
    public double width = 10; // Breydd hlutar
    public int getWidth(){// Fall til að ná í breidd hlutar.
        double x = fontwidth*this.StringWidth+width*2;
        //if(shape==1) x = fontwidth*this.StringWidth;
        double z = ImageResize;
        z = z/100;
        z = ImageWidth*z;
        
        z = z + width*2;
        //z = z + fontwidth*this.StringWidth;
        
        if(z<40&&x<40) return 40;
        if(z<x) return (int)x;
        else return (int)z;
        //if(x<40) x = 40;
        //if(x<20+this.ImageWidth*ImageResize/100) x = this.ImageWidth*ImageResize/100+20;
        //return (int)z;
    } 

    
    
    public double height = 5;  // hæð hlutar
    public int getHeight(){ // Fall til að ná í hæð hlutar án hæðar myndarinnar.
        int x = (int)height*2+fontheight*this.StringHeight;
        //if(shape==1) x = fontheight*this.StringHeight;
        if(x<30) x = 30;
        return x;
    }
    // Fall til að ná í hæð hlutar með hæð myndarinnar innifalda
    public int getHeightNew(){
        double z = ImageResize;
        z = z/100;
        z = ImageHeight*z;
        int x = (int)(height+getHeight()+z);
        //int x = (int)(height+getHeight()+ImageHeight*(ImageResize/100));
        return x;
    }
    
    
    
    public int fontheight = 12;
    public int fontwidth = 7;
    
    
   
    
    public String floturstring = "Type\ntext\nhere"; // Þessi breyta geymir streng hlutarins
    public int StringWidth = 0; // Þessi breyta geymir mestu breydd streng hlutarins.
    public int StringHeight = 0; // Þessi breyta geymir mestu hæð streng hlutarins.
    public void setFloturString(String texti){ // Þetta fall setur strenginn í hlutinn.
        this.floturstring = texti;
    }
    public void setStringWidth(int x){ // Þetta fall setur breydd strengsins.
        this.StringWidth = x;
    }
    public void setStringHeight(int x){ // þetta fall setur hæð strengsins.
        this.StringHeight = x;
    }
    private void DrawString(Graphics g, String text, int x, int y){ // Þetta fall teiknar streng hlutarins.
        x = x + (int)width;
        y = y + (int)height;
        
        int teljari = 0;
        int teljari2 = 0;
        boolean b = false;
        for (String line : text.split("\n")){
            if(b==false){
                teljari = line.length();
                b=true;
            }
            if(line.length()>teljari)teljari = line.length();
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
            if(this.shape==4)g.drawLine(this.xPos+1, y+2, (int)(x+this.getWidth()-this.width-1), y+2);
            teljari2++;
        }
        fontheight = g.getFontMetrics().getHeight();
        fontwidth = g.getFontMetrics().charWidth('A');
        
        setStringWidth(teljari);
        setStringHeight(teljari2);
    }
    
    
    
    
    public BufferedImage img = null; // Þessi breyta geymir bufferuðu myndina.
    public int ImageHeight; // Þessi breyta geymir hæð myndarinnar
    public int ImageWidth; // Þessi breyta geymir breydd myndarinnar.
    public int ImageResize = 100; // Þessi breyda geymir hversu mikið notandi er að resize-a myndina með Slider-num.
    public void setImage(File x){ // Þetta fall setur myndina í hlutinn og stillir aðrar myndastillingar.
        try {
            //this.img = ImageIO.read(x);
            
            String test = teiknari.ImageToString(ImageIO.read(x));
            img = teiknari.StringToImage(test);
            
            ImageWidth = img.getWidth();
            ImageHeight = img.getHeight();
            int y;
            if(this.ImageHeight>200|| this.ImageWidth>200)
            {
                if(this.ImageHeight>this.ImageWidth)
                {
                    y = this.ImageHeight/200;
                    this.ImageHeight = this.ImageHeight/y;
                    this.ImageWidth = this.ImageWidth/y;
                }
                else
                {
                    y = this.ImageWidth/200;
                    this.ImageHeight = this.ImageHeight/y;
                    this.ImageWidth = this.ImageWidth/y;
                }
            }
            
        } 
        catch (IOException e) 
        {
        }
    }
    
    public void setImage(BufferedImage bimagetemp){ String test = teiknari.ImageToString(bimagetemp);
    img = teiknari.StringToImage(test);
    ImageWidth = img.getWidth();
    ImageHeight = img.getHeight();
    int y;
    if(this.ImageHeight>200|| this.ImageWidth>200)
    {
        if(this.ImageHeight>this.ImageWidth) 
        {
            y = this.ImageHeight/200;
            this.ImageHeight = this.ImageHeight/y;
            this.ImageWidth = this.ImageWidth/y;
        }
        else
        {
            y = this.ImageWidth/200;
            this.ImageHeight = this.ImageHeight/y;
            this.ImageWidth = this.ImageWidth/y;
        }
    }
    }
    
    
    
    public void ImageResizeR(int x){ // Þeta fall setur ImageResize breytuna.
        this.ImageResize = x;
    }
    public void removeImage(){ // Þetta fall eyðir mynd frá hlut.
        this.img = null;
        this.ImageHeight = 0;
        this.ImageWidth = 0;
        ImageResizeR(100);
    }
    
    
    
    
    public Color bordercolor = Color.black;
    //Color litur = new Color(Integer.parseInt("-16711936")); // Breyta sem geymir liti fyrir hluti af tagi Flotur, Hringur og Ferningur.
    Color litur = new Color(-16711936);
    public void setColor(Color x){ // Fall sem setur lit hlutarins.
        this.litur = x;
    }
    public void SetBorderColor(Color x){
        this.bordercolor = x;
    }
    

    
    
    public void moveFletir(int x, int y){ // Fall til að færa hluti á teikniborðinu.
        setX(x);
        setY(y);
        teiknari.repaint(0,0,2000,2000);
        if(x<=5)setX(5);
        if(y<=3)setY(3);
    }
    
    public void DrawShadow(Graphics g){
        for(int i = 0; i<5;i++){
            int xs = 0;
            Color shadowtest = new Color(xs,xs,xs,15);
            g.setColor(shadowtest);
            if(shape==0)g.fillRoundRect(xPos-i+1, yPos-i+1,getWidth()+i*2+1,getHeightNew()+i*2+1,16,16); 
            if(shape==2)g.fillRoundRect(xPos-i+1, yPos-i+1,getWidth()+i*2+1,getHeightNew()+i*2+1,10,10);
            if(shape==3)g.fillRoundRect(xPos-i-3, yPos-i-3,getWidth()+i*2+9,getHeightNew()+i*2+9,25,25);
            if(shape==1)g.fillOval((int)(xPos-((getWidth()/Math.sqrt(2))-(getWidth()/2)))-i+1,(int)(yPos-((getHeightNew()/Math.sqrt(2))-(getHeightNew()/2)))-i+1,(int)(2*getWidth()/Math.sqrt(2))+i*2+1,(int)(2*getHeightNew()/Math.sqrt(2))+i*2+1);
            if(shape==4)g.fillRoundRect(xPos-i+1, yPos-i+1,getWidth()+i*2+1,getHeightNew()+i*2+1,10,10);
        }
    }
    public void DrawRoundRect(Graphics g, Graphics2D g2){
            //g2.fillRoundRect(xPos, yPos,getWidth(),getHeightNew(),13,13); 
            //g.setColor(litur.darker()); 
            //g.drawRoundRect(xPos, yPos,getWidth(),getHeightNew(),13,13);
            int x = (int)(getWidth()*0.25);
            int y = (int)(getHeightNew()*0.25);
            int z;
            if(x<y) z = x;
            else z = y;
                    
            //g2.fillRoundRect(xPos, yPos,getWidth(),getHeightNew(),z,z); 
            g2.fillRoundRect(xPos, yPos,getWidth(),getHeightNew(),16,16); 
            g.setColor(litur.darker()); 
            //g.drawRoundRect(xPos, yPos,getWidth(),getHeightNew(),z,z);
            g.drawRoundRect(xPos, yPos,getWidth(),getHeightNew(),16,16);
    }
    public void DrawOval(Graphics g, Graphics2D g2){
            g2.fillOval((int)(xPos-((getWidth()/Math.sqrt(2))-(getWidth()/2))),(int)(yPos-((getHeightNew()/Math.sqrt(2))-(getHeightNew()/2))),(int)(2*getWidth()/Math.sqrt(2)),(int)(2*getHeightNew()/Math.sqrt(2)));
            g.setColor(litur.darker());
            g.drawOval((int)(xPos-((getWidth()/Math.sqrt(2))-(getWidth()/2))),(int)(yPos-((getHeightNew()/Math.sqrt(2))-(getHeightNew()/2))),(int)(2*getWidth()/Math.sqrt(2)),(int)(2*getHeightNew()/Math.sqrt(2)));
    }
    public void DrawRect(Graphics g, Graphics2D g2){
            g2.fillRect(xPos, yPos,getWidth(),getHeightNew());
            g.setColor(litur.darker());
            g.drawRect(xPos, yPos,getWidth(),getHeightNew());
    }
    
    public void DrawNote(Graphics g, Graphics2D g2){
            g.setColor(litur);
            g2.fillRect(xPos, yPos,getWidth(),getHeightNew());
            g.setColor(litur.darker());
            g.drawRect(xPos, yPos,getWidth(),getHeightNew());
    }
    
    public void DrawCloud(Graphics g, Graphics2D g2){
            int iHeight = 14;
            int iWidth = 14;
            if(getHeightNew()<80) iHeight = 10;
            if(getHeightNew()<60) iHeight = 8;
            if(getHeightNew()<40) iHeight = 6;
            if(getWidth()<80) iWidth = 10;
            if(getWidth()<60) iWidth = 8;
            if(getWidth()<40) iWidth = 6;
            
            GeneralPath path = new GeneralPath();
            
            path.moveTo(xPos, yPos);
            for(int i = 0;i<iWidth;i++){
                        path.curveTo(this.xPos+((getWidth()*i)/iWidth), this.yPos-5, 
                        this.xPos+((getWidth()*(i+1))/iWidth), this.yPos-5, 
                        this.xPos+((getWidth()*(i+1))/iWidth), this.yPos);
                }
            for(int i = 0;i<iHeight;i++){
                        path.curveTo(this.xPos+5+getWidth(), this.yPos+((getHeightNew()*i)/iHeight), 
                        this.xPos+5+getWidth(), this.yPos+((getHeightNew()*(i+1))/iHeight), 
                        this.xPos+getWidth(), this.yPos+((getHeightNew()*(i+1))/iHeight));
                }
            GeneralPath path2 = new GeneralPath();
            path2.moveTo(xPos, yPos-1);
            for(int i = 0;i<iHeight;i++){
                path2.curveTo(this.xPos-5, this.yPos+((getHeightNew()*i)/iHeight), this.xPos-5, this.yPos+((getHeightNew()*(i+1))/iHeight), this.xPos, this.yPos+((getHeightNew()*(i+1))/iHeight));
                }
            for(int i = 0;i<iWidth;i++){
                path2.curveTo(this.xPos+((getWidth()*i)/iWidth), this.yPos+getHeightNew()+5, this.xPos+((getWidth()*(i+1))/iWidth), this.yPos+getHeightNew()+5, this.xPos+((getWidth()*(i+1))/iWidth), this.yPos+getHeightNew());
                }
            path2.lineTo(xPos+getWidth(),yPos+getHeightNew()-0);
            SetGradient(g2);
            //heeree
            g2.fill(path);
            g2.fill(path2);
            
            g.setColor(litur.darker());
            g2.draw(path);
            g2.draw(path2);
            
            
            g.setColor(bordercolor);
            g.setColor(litur.darker());
            CubicCurve2D c = new CubicCurve2D.Double();
    }
    public void SetGradient(Graphics2D g2){
        if(getHeightNew()<=100)g2.setPaint( new GradientPaint(xPos, yPos, Color.white, xPos, yPos+getHeightNew(), litur, false));
            else g2.setPaint( new GradientPaint(xPos, yPos, Color.white, xPos, yPos+100, litur, false));
    }
    
    
    public void DrawSelection(Graphics g){
            g.setColor(Color.BLACK);
            Graphics2D g2dashed = (Graphics2D)g.create();
            Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 4);
            g2dashed.setStroke(dashed);
            if(shape==0)g2dashed.drawRoundRect(xPos-2, yPos-2,getWidth()+4,getHeightNew()+4,13,13);
            if(shape==1)g2dashed.drawOval((int)(xPos-((getWidth()/Math.sqrt(2))-(getWidth()/2)))-2,(int)(yPos-((getHeightNew()/Math.sqrt(2))-(getHeightNew()/2)))-2,(int)(2*getWidth()/Math.sqrt(2))+4,(int)(2*getHeightNew()/Math.sqrt(2))+4);
            
            if(shape==2)g2dashed.drawRect(xPos-2, yPos-2,getWidth()+4,getHeightNew()+4);
            if(shape==3)g2dashed.drawRoundRect(xPos-6, yPos-6,getWidth()+12,getHeightNew()+12,13,13);
            if(shape==4)g2dashed.drawRect(xPos-2, yPos-2,getWidth()+4,getHeightNew()+4);
            g2dashed.dispose();
    }
    public void DrawTinyWhiteSquare(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(xPos-2, yPos-2, 5, 5);
        g.setColor(Color.WHITE);
        g.fill3DRect(xPos-1, yPos-1, 3, 3, true);
    }
    // Þetta fall sér um að teikna hlutina á panelinn.
    public void painT(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(renderHints);
        
        // To set bubble size to account for strings
        g.setFont(new Font("Lucida Console", Font.PLAIN, 11));
        DrawString(g, floturstring, xPos, yPos);
        
        DrawShadow(g);
        
        SetGradient(g2);
        
        if(shape==0) DrawRoundRect(g,g2);
        if(shape==1) DrawOval(g,g2);
        if(shape==2) DrawRect(g,g2);
        if(shape==3) DrawCloud(g,g2);
        if(shape==4) DrawNote(g,g2);
        
        
        g.setColor(Color.black);
        g.setFont(new Font("Lucida Console", Font.PLAIN, 11));
        DrawString(g, floturstring, xPos, yPos);
        
        
        g.drawImage(img, xPos+(int)width, yPos+getHeight(), ImageWidth*ImageResize/100, ImageHeight*ImageResize/100, null);
        if(IsSelected==1) DrawSelection(g);
        
        //this.DrawTinyWhiteSquare(g);
    }
}

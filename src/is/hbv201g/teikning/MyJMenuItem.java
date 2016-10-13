/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hbv201g.teikning;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

/**
 *
 * @author arnarbarri
 */
public class MyJMenuItem extends JMenuItem{
    public String path = "";
    public TeikniPanel06 TP;
    public String name = "a";
    public AbstractAction AbsA;

    MyJMenuItem(TeikniPanel06 temp,AbstractAction abstractAction) {
        this.configurePropertiesFromAction(abstractAction);
        setAction(abstractAction);
        TP = temp;
        //System.out.println("test inside constructor in custom jmenuitem");
        //AbsA = abstractAction;
        //this.addActionListener(actionListener);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }
    MyJMenuItem(TeikniPanel06 temp,String text, String x) {
        init(text, null);
        setText(text);
        //setAction(AbsA);
        name = text;
        TP = temp;
        path = x;
        AbsA = new AbstractAction(name) {
                public void actionPerformed(ActionEvent e) {
                    TP.SetPath(path);   
                    try {
                        TP.LoadFromFile(3);
                    } catch (IOException ex) {
                        Logger.getLogger(MyJMenuItem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                       
                }};
        setAction(AbsA);
        //this.configurePropertiesFromAction(abstractAction);
        //setAction(abstractAction);
        //System.out.println("test inside constructor in custom jmenuitem");
        //AbsA = abstractAction;
        //this.addActionListener(actionListener);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }
    
    /*public void actionPerformed(ActionEvent e) {
        //AbsA.actionPerformed(e);
        System.out.println("inside");
         //numClicks++;
         //text.setText("Button Clicked " + numClicks + " times");
    }*/
    
    public void SetPath(String x){
        this.path = x;
    }
    public String GetPath(){
        return this.path;
    }
}

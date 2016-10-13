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

/**
 *
 * @author arnarbarri
 */
public class ActionClass extends AbstractAction {

    private String cmd;
    public TeikniGluggi gluggi;

    public ActionClass(String cmd, TeikniGluggi tk) {
        this.cmd = cmd;
        this.gluggi = tk;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (cmd.equalsIgnoreCase("undo")) {
            try {
                gluggi.teikniPanel061.LoadFromFile(1);
                gluggi.LoadSaveMessage(3);
            } catch (IOException ex) {
                Logger.getLogger(ActionClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        if (cmd.equalsIgnoreCase("redo")) {
            try {
                gluggi.teikniPanel061.LoadFromFile(2);
                gluggi.LoadSaveMessage(4);
            } catch (IOException ex) {
                Logger.getLogger(ActionClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
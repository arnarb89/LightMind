package is.hbv201g.teikning;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//import com.sun.javafx.iio.ImageLoader;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
//import java.io.BufferedInputStream;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
//import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
//import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * TeikniGluggi inniheldur TeikniPanel og nokkra takka.
 * @author Ebba Þóra Hvannberg, Háskóli Íslands, ebba@hi.is
 * @version
 */
public class TeikniGluggi extends javax.swing.JFrame {
    /**
     * Creates new form TeikniGluggi
     */
    
    public BufferedImage WallPaper = null;
    public Color TGlitur = Color.black;
    public boolean IsDrawing = false;
    public AffineTransform AT1;
    public AffineTransform AT2;
    public Graphics2D G1;
    public Graphics2D G2;
    public String LoadPath = null;
    final public Thread mainThread = Thread.currentThread();
    final public MyJMenuItem[]a = new MyJMenuItem[5];
    public String [] LastOpened = {"","","","",""};
    public int LastOpenedNumber = 0;
    public int TempInt = 0;
    
    
    public TeikniGluggi() throws URISyntaxException, IOException {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        
        InputMap im = teikniPanel061.getInputMap(teikniPanel061.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = teikniPanel061.getActionMap();
        
        im.put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
        im.put(KeyStroke.getKeyStroke("ctrl alt Z"), "redo");
        am.put("undo", new ActionClass("undo",this));
        am.put("redo", new ActionClass("redo",this));
        
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false), "Delete pressed");
        //true == on release, false == on pressed
        am.put("Delete pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Delete pressed");
                teikniPanel061.RemoveBubble();
                teikniPanel061.repaint();
            }
        });
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, KeyEvent.SHIFT_DOWN_MASK, false), "Shift pressed");
        //true == on release, false == on pressed
        am.put("Shift pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                teikniPanel061.ShiftOn = true;
                System.out.println("Shift pressed :"+teikniPanel061.ShiftOn);
            }
        });
        
       im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, true), "Shift released");
        //true == on release, false == on pressed
        am.put("Shift released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                teikniPanel061.ShiftOn = false;
                System.out.println("Shift released :"+teikniPanel061.ShiftOn);
            }
        });
        
        
        
        
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.CTRL_DOWN_MASK, false), "Control pressed");
        //true == on release, false == on pressed
        am.put("Control pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                teikniPanel061.CTRLOn = true;
                System.out.println("CTRL pressed :"+teikniPanel061.CTRLOn);
            }
        });
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, 0, true), "Control released");
        //true == on release, false == on pressed
        am.put("Control released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                teikniPanel061.CTRLOn = false;
                System.out.println("Control released :"+teikniPanel061.CTRLOn);
            }
        });
        
        
        
        
        InputMap TextAreaInputMap = teikniPanel061.area.getInputMap(teikniPanel061.area.WHEN_FOCUSED);
        ActionMap TextAreaActionMap = teikniPanel061.area.getActionMap();
        
        TextAreaInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK, true), "Shift+Enter released");
        //true == on release, false == on pressed
        TextAreaActionMap.put("Shift+Enter released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Shift+Enter released");
                //teikniPanel061.area.append("\n");
                teikniPanel061.area.replaceSelection("\n");
                teikniPanel061.SaveToBackup();
            }
        });
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK, false), "CTRL Save");
        //true == on release, false == on pressed
        am.put("CTRL Save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("HIHIHIHIHIHIHI");
                if(LoadPath==null){
                    try {
                        teikniPanel061.SaveToFile();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                else {
                    try {
                        teikniPanel061.CTRLSave();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                teikniPanel061.requestFocusInWindow();
                teikniPanel061.LoadSaveMessage(1);
            }
        });
        
        
        TextAreaInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "Enter Pressed");
        //true == on release, false == on pressed
        TextAreaActionMap.put("Enter Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Enter Pressed");
                teikniPanel061.TextPopup.setVisible(false);
                teikniPanel061.SaveToBackup();
                //teikniPanel061.area.append("\n");
            }
        });
        
        Image papertest = ImageIO.read(getClass().getResource("/paper.jpg"));
        WallPaper = teikniPanel061.ToBufferedImage(papertest);
        
        /*BufferedImage bImage = teikniPanel061.cursorImg;
        Graphics2D g3 = (Graphics2D) bImage.getGraphics();
        g3.setColor(TGlitur);
        g3.fillRect(0, 0, bImage.getWidth(), bImage.getHeight());
        DrawingCheckBox.setIcon(new ImageIcon(bImage));
        g3.dispose();*/
        
        this.jMenu1.setVisible(false);
        this.jMenu1.setFocusable(false);
        teikniPanel061.LoadSaveMessage(2);
        setTitle("LightMind");
        
        InitiateIcon();
        
        
        
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent ev){
                
                try {
                    OnClose(true);
                } catch (IOException ex) {
                    Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        
        menuOptions.add(new JMenuItem(new AbstractAction("Open Project Folder") {
            public void actionPerformed(ActionEvent e) {
                
                String x = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
                new File(x+"/LightMind Projects").mkdir();
                x = x + "\\LightMind Projects";
                File y = new File(x);
                try {
                    Desktop.getDesktop().open(y);
                } catch (IOException ex) {
                    Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
        }));
        
        
        
        
        
        
        
        DatMaker(false);
        LoadDat();
        LoadMenu();
        
    }
    public void LoadMenu(){
        
        menuFile.removeAll();
        menuFile.add(LoadButton);
        menuFile.add(SaveButton);
        menuFile.add(menuRestart);
        menuFile.add(new JSeparator());
        for(int i = 0;i<LastOpened.length;i++){
                if(LastOpened[i]==null) break;
                if(LastOpened[i].compareTo("")==0) break;
                if(LastOpened[i].compareTo("null")==0) break;
                String x = LastOpened[i];
		Path p = Paths.get(x);
		String filename = i+"           "+p.getFileName().toString();
		a[i] = new MyJMenuItem(teikniPanel061,filename,LastOpened[i]);
                menuFile.add(a[i]);
                
        }
        menuFile.add(new JSeparator());
        menuFile.add(menuExit);
        System.out.println("lallallalll");
        
    }
    
    public void LoadDat() throws IOException{
        String x = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        x = x + "\\LightMind Projects\\recentfiles.Ldat";
        String content = new String(Files.readAllBytes(Paths.get(x+"")));
        int column = 0;
        for(String line : content.split(".!.")){
            if(line.compareTo("")==0) return;
            if(line==null) return;
            LastOpened[column++] = line;
        }
    }
    
    public void DatMaker(boolean b) throws FileNotFoundException, UnsupportedEncodingException{
        String x = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        new File(x+"/LightMind Projects").mkdir();
        x = x + "\\LightMind Projects\\recentfiles.Ldat";
        Path path = Paths.get(x);
        File y = new File (x);
        if(!y.exists()){
            PrintWriter writer = new PrintWriter(x, "UTF-8");
            writer.print(".!..!..!..!.");
            writer.close();
        }
        
        
        
        else if(y.exists()&&b==true){
                try {
                    Files.setAttribute(path, "dos:hidden", false);

                } catch (IOException ex) {
                    Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
            PrintWriter writer = new PrintWriter(x, "UTF-8");
            for(int i = 0;i<LastOpened.length;i++){
                writer.print(LastOpened[i]);
                if(i==4)break;
                writer.print(".!.");
            }
            writer.close();
        }
        
        try {
            Files.setAttribute(path, "dos:hidden", true);
            
            
        } catch (IOException ex) {
            Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void OnClose(boolean IsClosing) throws IOException{
        
        if(teikniPanel061.UnSaved==false){
                    System.out.println("unsaved = false, exiting");
                    if(IsClosing){
                        this.dispose();
                        System.exit(0);
                    }
                    else {
                        teikniPanel061.LoadFromFile(0);
                        teikniPanel061.UnSaved=false;
                    }
        }
        else {
                    
            System.out.println("Unsaved = true, do nothing");
            int reply = JOptionPane.showConfirmDialog(null, "Do you wish to save your progress?", "You have unsaved progress", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
            //JOptionPane.showConfirm
            if (reply == JOptionPane.YES_OPTION) {
                try {
                //JOptionPane.showMessageDialog(null, "HELLO");
                    boolean b = teikniPanel061.SaveToFile();
                    System.out.println("before dispose");
                    if(IsClosing&&b){
                        this.dispose();
                        System.exit(0);
                    }
                    else if(IsClosing==false)teikniPanel061.LoadFromFile(0);
                    System.out.println("After dispose");
                } catch (FileNotFoundException ex) {
                Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            else if (reply == JOptionPane.NO_OPTION) {
               //JOptionPane.showMessageDialog(null, "GOODBYE");
               if(IsClosing){
                        this.dispose();
                        System.exit(0);
                    }
                    else teikniPanel061.LoadFromFile(0);
            }
            else {
                System.out.println("Cancel");
            }
        
        }
        
    }
    
    
    public void InitiateIcon() throws IOException{
        Image icontemp = ImageIO.read(getClass().getResource("/iconmaybe6.png"));
        BufferedImage icontemp2 = teikniPanel061.ToBufferedImage(icontemp);
        this.setIconImage(icontemp);
    }
    
    public boolean SmartSelectOn(){
        if(SmartSelect.isSelected()==true) return true;
        else return false;
    }
    
        
    
    
    
    
    
    // Þetta fall býr til screenshot af teikniborðinu
    public void makeScreenshot() throws IOException {
        String x = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        new File(x+"/LightMind Projects").mkdir();
        x = x + "\\LightMind Projects";
        new File(x+"/LightMind Screenshots").mkdir();
        x = x + "\\LightMind Screenshots";
        Rectangle rec = teikniPanel061.getBounds();
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height,BufferedImage.TYPE_INT_ARGB);
        
        teikniPanel061.paint(bufferedImage.getGraphics());
        
        BufferedImage bufferedImage2 = bufferedImage.getSubimage(bufferedImage.getMinX()+5, bufferedImage.getMinY()+3, bufferedImage.getWidth()-9, bufferedImage.getHeight()-8);
        JFileChooser jFile = new JFileChooser(x);
        jFile.addChoosableFileFilter(new FileNameExtensionFilter("PNG (.png)", "png"));
        jFile.setFileFilter(jFile.getChoosableFileFilters()[1]);
        if(jFile.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
        File f = jFile.getSelectedFile();
        String xin = f+"";
        if (xin.indexOf(".") > 0) xin = xin.substring(0, xin.lastIndexOf("."));
        File newfile = new File (xin+".png");
        ImageIO.write(bufferedImage2, "png", newfile);
        
}
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        teikniPanel061 = new is.hbv201g.teikning.TeikniPanel06(this);
        MenuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        LoadButton = new javax.swing.JMenuItem();
        SaveButton = new javax.swing.JMenuItem();
        menuRestart = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        menuOptions = new javax.swing.JMenu();
        SmartSelect = new javax.swing.JCheckBoxMenuItem();
        TakeScreenshot = new javax.swing.JMenuItem();
        menuInfo = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        DrawingCheckBox = new javax.swing.JCheckBoxMenuItem();
        BrushColor = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MindMap Teikniforrit");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(700, 600));

        teikniPanel061.setBackground(new java.awt.Color(255, 255, 255));
        teikniPanel061.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout teikniPanel061Layout = new javax.swing.GroupLayout(teikniPanel061);
        teikniPanel061.setLayout(teikniPanel061Layout);
        teikniPanel061Layout.setHorizontalGroup(
            teikniPanel061Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        teikniPanel061Layout.setVerticalGroup(
            teikniPanel061Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        menuFile.setText("File");

        LoadButton.setText("Load map");
        LoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadButtonActionPerformed(evt);
            }
        });
        menuFile.add(LoadButton);

        SaveButton.setText("Save map");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });
        menuFile.add(SaveButton);

        menuRestart.setText("Clear map");
        menuRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRestartActionPerformed(evt);
            }
        });
        menuFile.add(menuRestart);

        menuExit.setText("Quit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        menuFile.add(menuExit);

        MenuBar.add(menuFile);

        menuOptions.setText("Options");

        SmartSelect.setSelected(true);
        SmartSelect.setText("Smart Select On/Off");
        SmartSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SmartSelectActionPerformed(evt);
            }
        });
        menuOptions.add(SmartSelect);

        TakeScreenshot.setText("Take Screenshot");
        TakeScreenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakeScreenshotActionPerformed(evt);
            }
        });
        menuOptions.add(TakeScreenshot);

        menuInfo.setText("Information");
        menuInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInfoActionPerformed(evt);
            }
        });
        menuOptions.add(menuInfo);

        menuHelp.setText("Help");
        menuHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHelpActionPerformed(evt);
            }
        });
        menuOptions.add(menuHelp);

        MenuBar.add(menuOptions);

        jMenu1.setText("Drawing");

        DrawingCheckBox.setText("Drawing On/Off");
        DrawingCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                DrawingCheckBoxStateChanged(evt);
            }
        });
        DrawingCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DrawingCheckBoxActionPerformed(evt);
            }
        });
        jMenu1.add(DrawingCheckBox);

        BrushColor.setText("Change Brush Color");
        BrushColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrushColorActionPerformed(evt);
            }
        });
        jMenu1.add(BrushColor);

        MenuBar.add(jMenu1);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(teikniPanel061, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(teikniPanel061, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Þetta fall er Exit takkinn.
    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
        try {
            // TODO add your handling code here:
            OnClose(true);
        } catch (IOException ex) {
            Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuExitActionPerformed

    //Þetta fall er Help takkinn.
    private void menuHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHelpActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, 
                "Drag with the Left Mouse Button to create a selection area to select the objects that fall under it.\n" +
                "Left Mouse Click on the text areas within objects to edit the texts.\n" +
"Left Mouse Click and drag the upper right corner of objects to create a line. If you release this line on another object the\n" +
                "two objects will form a connection. If you release it outside any object you will create a new object at that location and form a connection to it.\n" +
"Drag with the Right Mouse Button to move selected objects around.\n" +
"Right Click on selected objects to show options. Right Clicking outside objects will show an option to create another object.\n" +
"Drag with the Mouse Wheel Button to pan the view in any direction.\n" +
"Scroll in and out with the Mouse Wheel to zoom in and out.\n" +
"CTRL + Z will undo.\n" +
"CTRL + ALT + Z will redo.\n" +
                        "Shift+Enter while editing texts within objects will create a new line.\n" +
                        "Pressing Enter while editing texts within objects will exit the text editing.\n" +
"Holding SHIFT and clicking clicking or dragging selections over objects will add these objects to your current selection.\n" +
"Pressing the Delete button will delete currently selected objects.\n" +
"Holding CTRL and clicking objects will select them if they are unselected and unselect them if they are selected.\n"
                , "Help", -1);
    }//GEN-LAST:event_menuHelpActionPerformed

    // Þetta fall er Information takkinn.
    private void menuInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuInfoActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Information about the system that was used to create this program:\n" +
"\n" +
"Product Version: NetBeans IDE 8.0.2 (Build 201411181905)\n" +
"Java: 1.8.0_20; Java HotSpot(TM) 64-Bit Server VM 25.20-b23\n" +
"Runtime: Java(TM) SE Runtime Environment 1.8.0_20-b26\n" +
"System: Windows 7 version 6.1 running on amd64; Cp1252; en_US (nb)", "Information", -1);
        
        
    }//GEN-LAST:event_menuInfoActionPerformed
    // Þetta fall er "Clear map" takkinn.
    private void menuRestartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRestartActionPerformed
        // TODO add your handling code here:
        
        
        
        teikniPanel061.restarT(false);
        
        
        
    }//GEN-LAST:event_menuRestartActionPerformed

    
    // Þetta fall er "Save map" takkinn.
    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        
        
        
        try {
            teikniPanel061.SaveToFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }//GEN-LAST:event_SaveButtonActionPerformed

    public void LoadSaveMessage(int b){
        //b = 0 = loading
        //b = 1 = saving
        //b = 2 = Welcome
        //b = 3 = Undo
        //b = 4 = redo
        teikniPanel061.SystemMessageAlpha = 255;
        if(b==0)teikniPanel061.SystemMessage = "File Loaded: " +LoadPath;
        if(b==1)teikniPanel061.SystemMessage = "File Saved: " +LoadPath;
        if(b==2)teikniPanel061.SystemMessage = "Welcome!";
        if(b==3)teikniPanel061.SystemMessage = "Undo";
        if(b==4)teikniPanel061.SystemMessage = "Redo";
        final Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                teikniPanel061.SystemMessageAlpha = teikniPanel061.SystemMessageAlpha -1;
                if (teikniPanel061.SystemMessageAlpha <= 0) {
                  teikniPanel061.SystemMessageAlpha = 0;
                  timer.cancel();
                }
                teikniPanel061.repaint();
                teikniPanel061.requestFocusInWindow();
                
            }
        },3000,10);
        
    }
    
    // Þetta fall er "Load map" takkinn.
    private void LoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadButtonActionPerformed
        
        
        try {
            OnClose(false);
        } catch (IOException ex) {
            Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*try {
            // TODO add your handling code here:
            teikniPanel061.LoadFromFile(0);
        } catch (IOException ex) {
            Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        
        
        
    }//GEN-LAST:event_LoadButtonActionPerformed
    //Takkinn fyrir Take Screenshot
    private void TakeScreenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakeScreenshotActionPerformed
        
        
        
        try {
            // TODO add your handling code here:
            makeScreenshot();
        } catch (IOException ex) {
            Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }//GEN-LAST:event_TakeScreenshotActionPerformed

    private void SmartSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SmartSelectActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_SmartSelectActionPerformed

    private void BrushColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrushColorActionPerformed
        // TODO add your handling code here:
        Color colortemp = JColorChooser.showDialog(this,"Select a color",TGlitur);
        if(colortemp==null) return;
        TGlitur = colortemp;
        BufferedImage bImage = teikniPanel061.cursorImg;
        Graphics2D g = (Graphics2D) bImage.getGraphics();
        g.setColor(TGlitur);
        g.fillRect(0, 0, bImage.getWidth(), bImage.getHeight());
        //BrushColor.setIcon(new ImageIcon(bImage));
        DrawingCheckBox.setIcon(new ImageIcon(bImage));
        g.dispose();
        if(IsDrawing==true){
            BufferedImage ImageTemp = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) ImageTemp.getGraphics();
            g2.setColor(TGlitur);
            g2.fillRect(0, 0, 3, 3);
            Cursor ColorCursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageTemp, new Point(0, 0), "Color Cursor");
            this.setCursor(ColorCursor);
            this.teikniPanel061.setCursor(ColorCursor);
            g2.dispose();
        }
    }//GEN-LAST:event_BrushColorActionPerformed

    private void DrawingCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DrawingCheckBoxActionPerformed
        // TODO add your handling code here:
        if(IsDrawing==false){
            IsDrawing=true;
            BufferedImage ImageTemp = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) ImageTemp.getGraphics();
            g.setColor(TGlitur);
            g.fillRect(0, 0, 3, 3);
            Cursor ColorCursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageTemp, new Point(0, 0), "Color Cursor");
            this.setCursor(ColorCursor);
            this.teikniPanel061.setCursor(ColorCursor);
            g.dispose();
        }
        else if(IsDrawing==true){
            IsDrawing=false;
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            this.teikniPanel061.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_DrawingCheckBoxActionPerformed

    private void DrawingCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_DrawingCheckBoxStateChanged
        // TODO add your handling code here:
        /*if(IsDrawing==false){
            IsDrawing=true;
            BufferedImage ImageTemp = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) ImageTemp.getGraphics();
            g.setColor(TGlitur);
            g.fillRoundRect(0, 0, ImageTemp.getWidth(), ImageTemp.getHeight(),1,1);
            Cursor ColorCursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageTemp, new Point(0, 0), "Color Cursor");
            this.setCursor(ColorCursor);
            g.dispose();
        }
        if(IsDrawing==true){
            IsDrawing=false;
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }*/
    }//GEN-LAST:event_DrawingCheckBoxStateChanged

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
            java.util.logging.Logger.getLogger(TeikniGluggi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TeikniGluggi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TeikniGluggi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TeikniGluggi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TeikniGluggi().setVisible(true);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TeikniGluggi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
        
        
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem BrushColor;
    private javax.swing.JCheckBoxMenuItem DrawingCheckBox;
    private javax.swing.JMenuItem LoadButton;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenuItem SaveButton;
    private javax.swing.JCheckBoxMenuItem SmartSelect;
    private javax.swing.JMenuItem TakeScreenshot;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuHelp;
    private javax.swing.JMenuItem menuInfo;
    private javax.swing.JMenu menuOptions;
    private javax.swing.JMenuItem menuRestart;
    public is.hbv201g.teikning.TeikniPanel06 teikniPanel061;
    // End of variables declaration//GEN-END:variables
}

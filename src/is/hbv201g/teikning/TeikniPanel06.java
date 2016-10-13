/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hbv201g.teikning;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author arnarbarri
 */
public class TeikniPanel06 extends javax.swing.JPanel {
    
    
    public Flotur[] fletir; // Fylki sem geymir alla hlutina í hugkortinu
    public Lines[] Strik = new Lines [10]; //Fylki sem geymir allar línur milli hluta
    public int linecounter = 0; // Breyta sem fylgist með því hversu margar línur eru í hugkortinu.
    public int fylkicounter; // Breyta sem fylgist með hversu margir hlutir eru komnir í fylkið fletir.
    public int xnew;
    public int ynew;
    public boolean leftclick = false;
    public boolean rightclick = false;
    public boolean mwheelclick = false;
    public SelectRect selectrect = new SelectRect(this);
    public ConnectLineClass connectline = new ConnectLineClass(this);
    public int SingleSelectedItem;
    public BufferedImage paper = null;
    public double ATscale = 1;
    public double AToffsetX = 0;
    public double AToffsetY = 0;
    public AffineTransform AToriginal;
    public AffineTransform ATchanged;
    private double currentX;
    private double currentY;
    private double previousX;
    private double previousY;
    public double NEWx;
    public double NEWy;
    public double newX;
    public double newY;
    public int maximumXupper = 2000;
    public int maximumXlower = -2040;
    public int maximumYupper = 1040;
    public int maximumYlower =  -1100;
    public int Xbounds = 2070;
    public int Ybounds = 1118;
    public boolean ResizeHover = false;
    public boolean ConnectHover = false;
    public int HoverHlutur = 0;
    public boolean IsConnecting = false;
    public BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Cursor BlankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
    public Rectangle[] ResizeRect;
    public Rectangle[] ConnectRect;
    public Rectangle[] FullRect;
    public Rectangle[] TextRect;
    public String[] UndoArray = new String[]{"","","","","","","","","",""};
    public int UndoCounter = 0;
    public JSlider testerslider = new JSlider(JSlider.HORIZONTAL, 0, 100, 12);
    public JTextArea area = new JTextArea(3,4);
    public final JPopupMenu TextPopup = new JPopupMenu();
    public double ATdouble = 0.0;
    public boolean ShiftOn = false;
    public boolean CTRLOn = false;
    public DrawClass[]drawfylki = new DrawClass [10];
    public int drawcounter = 0;
    public boolean DrawPressed = false;
    public int DrawnItem;
    public boolean NoPopups = false;
    public String SystemMessage = "";
    public String SystemMessage2 = "";
    public int SystemMessageAlpha = 255;
    
    public Timer timer = new Timer();
    public Timer timer2 = new Timer();
    public int timer2counter = 5550;
    
    public boolean UnSaved= false;
    
    public String menuitempath = "";
    
    /**
     * Creates new form TeikniPanel06
     */
    TeikniGluggi tk;
    public TeikniPanel06(TeikniGluggi bubb) {
        /*final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                if(UnSaved==false){
                    System.out.println("unsaved = false, exiting");
                }
                else {
                    
                    System.out.println("Unsaved = true, do noting");
                    try {   
                        mainThread.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    int reply = JOptionPane.showConfirmDialog(null, "Message", "title", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        try {
                            //JOptionPane.showMessageDialog(null, "HELLO");
                            SaveToFile();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else {
                       JOptionPane.showMessageDialog(null, "GOODBYE");
                    }
                }
            }
        });*/
        
        
        
        
        final JPopupMenu popupoutside = new JPopupMenu();
        final JPopupMenu popupinside = new JPopupMenu();
        //final JPopupMenu TextPopup = new JPopupMenu();
        final JMenu ShapeMenu = new JMenu("Shape");
        final JMenu DrawMenu = new JMenu("Freehand Drawing");
        final JMenu ConnectionMenu = new JMenu("Remove Connections");
        
        area.setOpaque(false);
        TextPopup.setOpaque(false);
        
        testerslider.setMajorTickSpacing(5);
        testerslider.setMinimum(10);
        testerslider.setSnapToTicks(true);
        testerslider.setValue(100);
        testerslider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                testersliderStateChanged(evt);
            }

            private void testersliderStateChanged(ChangeEvent evt) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].ImageResizeR(testerslider.getValue());
                }
                repaint();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
        
        area.setFont(new java.awt.Font("Lucida Console", 0, 12));
        this.tk = bubb;
        fletir = new Flotur[10];
        fletir[fylkicounter++] = new Flotur (this);
        fletir[0].xPos = -430;
        fletir[0].yPos = -141;
        try 
        {
            PreviewMaker();
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (UnsupportedEncodingException ex) 
        {
            Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //paper = tk.WallPaper;
        /*URL url = (this.getClass().getClassLoader().getResource("\\paper.jpg"));
        
        try 
        {
            paper = ImageIO.read(url);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        SaveToBackup();
        UnSaved = false;
        

        ShapeMenu.add(new JMenuItem(new AbstractAction("Rounded Square") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                int x = 0;
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].SetShape(x);
                }
                SaveToBackup();
                repaint();

            }
        }));
        
        
       /* popupoutside.add(new JMenuItem(new AbstractAction("Restart") {
            public void actionPerformed(ActionEvent e) {
                tk.setVisible(false);
                tk.setVisible(true);
                tk.teikniPanel061.setVisible(false);
                tk.teikniPanel061.getX();
                tk.teikniPanel061.getY();
                tk.teikniPanel061.getHeight();
                tk.teikniPanel061.getWidth();

            }
        }));*/
        
        
        

        ShapeMenu.add(new JMenuItem(new AbstractAction("Square") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                int x = 2;
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].SetShape(x);
                }
                SaveToBackup();
                repaint();
            }
        }));

        ShapeMenu.add(new JMenuItem(new AbstractAction("Oval") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                int x = 1;
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].SetShape(x);
                }
                SaveToBackup();
                repaint();
            }
        }));

        ShapeMenu.add(new JMenuItem(new AbstractAction("Cloud") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                int x = 3;
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].SetShape(x);
                }
                SaveToBackup();
                repaint();
            }
        }));
        
        ShapeMenu.add(new JMenuItem(new AbstractAction("Note") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                int x = 4;
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1){
                        fletir[i].SetShape(x);
                        fletir[i].litur = new Color(-103);
                    }
                }
                SaveToBackup();
                repaint();
            }
        }));
        
        DrawMenu.add(new JMenuItem(new AbstractAction("Draw On Object") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                if(TPCountSelected()>1) {
                    TooManyBubbles();
                    return;
                }
                DrawOnObject();
                /*for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) {
                        DrawnItem = i;
                        break;
                    }
                }
                BufferedImage ImageTemp = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) ImageTemp.getGraphics();
                g.setColor(tk.TGlitur);
                g.fillRect(0, 0, 3, 3);
                Cursor ColorCursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageTemp, new Point(0, 0), "Color Cursor");
                tk.setCursor(ColorCursor);
                tk.teikniPanel061.setCursor(ColorCursor);
                g.dispose();
                
                tk.IsDrawing = true;
                repaint();*/
            }
        }));
        
        DrawMenu.add(new JMenuItem(new AbstractAction("Clear Drawings") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) {
                        RemoveDrawing(i);
                    }
                }
                SaveToBackup();
                repaint();
            }
        }));
        
        
        DrawMenu.add(new JMenuItem(new AbstractAction("Change Brush Color") {
            public void actionPerformed(ActionEvent e) {
                
                Color colortemp = JColorChooser.showDialog(tk,"Select a color",tk.TGlitur);
                if(colortemp==null) return;
                tk.TGlitur = colortemp;
                BufferedImage bImage = tk.teikniPanel061.cursorImg;
                Graphics2D g = (Graphics2D) bImage.getGraphics();
                g.setColor(tk.TGlitur);
                g.fillRect(0, 0, bImage.getWidth(), bImage.getHeight());
                //BrushColor.setIcon(new ImageIcon(bImage));
                //tk.DrawingCheckBox.setIcon(new ImageIcon(bImage));
                g.dispose();
                if(tk.IsDrawing==true){
                    BufferedImage ImageTemp = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = (Graphics2D) ImageTemp.getGraphics();
                    g2.setColor(tk.TGlitur);
                    g2.fillRect(0, 0, 3, 3);
                    Cursor ColorCursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageTemp, new Point(0, 0), "Color Cursor");
                    tk.setCursor(ColorCursor);
                    tk.teikniPanel061.setCursor(ColorCursor);
                    g2.dispose();
                }
                if(TPCountSelected()==1) {
                    DrawOnObject();
                }
                repaint();
            }
        }));
        
        
        
        
        popupoutside.add(DrawMenu);
        popupoutside.add(ShapeMenu);
        
        
        popupoutside.add(new JMenuItem(new AbstractAction("Change Color") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                Color colortemp = JColorChooser.showDialog(tk.teikniPanel061,"Select a color",fletir[TPGetSingleSelected()].litur);


                if(colortemp==null) return;
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].setColor(colortemp);
                }
                SaveToBackup();
                repaint();
            }
        }));
        
        final JMenu ImageMenu = new JMenu("Image");
        
        ImageMenu.add(new JMenuItem(new AbstractAction("Image from PC") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                int o = fileChooser.showOpenDialog(tk.teikniPanel061);
                if(o==JFileChooser.APPROVE_OPTION){
                File f = fileChooser.getSelectedFile();
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].setImage(f);
                }
                repaint();
                SaveToBackup();
                }
            }
        }));
        
        
        ImageMenu.add(new JMenuItem(new AbstractAction("Raw Image from ClipBoard") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                BufferedImage Imagetemp = null;
                Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
                {
                    try {
                        Imagetemp = ToBufferedImage((Image)transferable.getTransferData(DataFlavor.imageFlavor));
                    } catch (UnsupportedFlavorException ex) {
                        Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for(int i = 0;i<fylkicounter;i++){
                        if(fletir[i].IsSelected==1) 
                        {
                            fletir[i].setImage(Imagetemp);
                        }
                    }
                    SaveToBackup();
                    repaint();
                }
                
                
            }
        }));
        
        ImageMenu.add(new JMenuItem(new AbstractAction("Image from URL from ClipBoard") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                URL url = null;
                BufferedImage c = null;
                String teststring = null;
                Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                try {
                    teststring = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    url = new URL(teststring);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    c = ImageIO.read(url);
                } catch (IOException ex) {
                    Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
                }
                for(int i = 0;i<fylkicounter;i++){
                        if(fletir[i].IsSelected==1) 
                        {
                            fletir[i].setImage(c);
                        }
                }
                SaveToBackup();
                repaint();
                
            }
        }));
        
        
        
        
        ImageMenu.add(new JMenuItem(new AbstractAction("Remove Image") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].removeImage();
                }
                SaveToBackup();
                repaint();
            }
        }));
        
        ImageMenu.add(testerslider);
        
        popupoutside.add(ImageMenu);
        
        popupoutside.add(new JMenuItem(new AbstractAction("Remove Bubble(s)") {
            public void actionPerformed(ActionEvent e) {
                RemoveBubble();
                repaint();
            }
        }));
        
        popupinside.add(new JMenuItem(new AbstractAction("Add New Bubble") {
            public void actionPerformed(ActionEvent e) {
                adda();
                Point2D temp = MouseInfo.getPointerInfo().getLocation();
                temp = getTranslatedPoint(temp.getX(),temp.getY());
                fletir[fylkicounter-1].xPos = (int)temp.getX();
                fletir[fylkicounter-1].yPos = (int)temp.getY()-50;
                SaveToBackup();
                repaint();
            }
        }));
        
        
        ConnectionMenu.add(new JMenuItem(new AbstractAction("To This Object") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                if(TPCountSelected()>1) {
                    TooManyBubbles();
                    return;
                }
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) RemoveLinesTo(i);
                }
                SaveToBackup();
                repaint();
            }
        }));
        ConnectionMenu.add(new JMenuItem(new AbstractAction("From This Object") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                if(TPCountSelected()>1) {
                    TooManyBubbles();
                    return;
                }
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) RemoveLinesFrom(i);
                }
                SaveToBackup();
                repaint();
            }
        }));
        
        ConnectionMenu.add(new JMenuItem(new AbstractAction("To And From This Object") {
            public void actionPerformed(ActionEvent e) {
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) RemoveLines(i);
                }
                SaveToBackup();
                repaint();
            }
        }));
        popupoutside.add(ConnectionMenu);
        
        
        /*popupoutside.add(new JMenuItem(new AbstractAction("Change Border Color") {
            public void actionPerformed(ActionEvent e) {
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                Color colortemp = JColorChooser.showDialog(tk.teikniPanel061,"Select a color",fletir[TPGetSingleSelected()].bordercolor);
                if(colortemp==null) return;
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) fletir[i].SetBorderColor(colortemp);
                }
                repaint();
            }
        }));*/
        
        //final JMenu ChangeText = new JMenu("Change Text");
        area.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                areaKeyReleased(evt);
            }

            private void areaKeyReleased(KeyEvent evt) {
                if(TPCountSelected()>1) {
                    TooManyBubbles();
                    return;
                }
                if(TPCountSelected()<1) {
                    TooFewBubbles();
                    return;
                }
                fletir[TPGetSingleSelected()].setFloturString(area.getText());
                {
                    area.setRows(fletir[TPGetSingleSelected()].StringHeight);
                    area.setColumns(fletir[TPGetSingleSelected()].StringWidth);
                    if(fletir[TPGetSingleSelected()].StringWidth<2) area.setColumns(2);
                    Point2D temp2 = getTranslatedPoint2((fletir[TPGetSingleSelected()].getX()+fletir[TPGetSingleSelected()].width),(fletir[TPGetSingleSelected()].getY()+fletir[TPGetSingleSelected()].height+5));
                    TextPopup.setVisible(false);
                    TextPopup.show(tk.teikniPanel061, (int)temp2.getX(), (int)temp2.getY());
                    area.requestFocusInWindow();
                }
                repaint();
                area.repaint();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        TextPopup.add(area);
        TextPopup.setBorder(BorderFactory.createEmptyBorder());
        
        
        
        
        
        
        addMouseMotionListener(new MouseAdapter(){
        @Override public void mouseMoved(MouseEvent e) {
            Point2D temp = getTranslatedPoint(e.getPoint().getX(),e.getPoint().getY());
            if(tk.IsDrawing) return;
            if(fylkicounter==0)return;
            
            
            
            ConnectRect = new Rectangle[fylkicounter];
            ResizeHover = false;
            ConnectHover = false;
            for(int i = 0;i<fylkicounter;i++){
                ConnectRect[i] = new Rectangle(fletir[i].xPos+fletir[i].getWidth()-8,fletir[i].yPos-8,16,16);
                if(ConnectRect[i].contains(temp)){
                    ConnectHover = true;
                    HoverHlutur = i;
                    break;
                }
            }
            /*
            ResizeRect = new Rectangle[fylkicounter];
            for(int i = 0;i<fylkicounter;i++){
                ResizeRect[i] = new Rectangle(fletir[i].xPos+fletir[i].getWidth()-8,fletir[i].yPos+fletir[i].getHeightNew()-8,16,16);
                if(ResizeRect[i].contains(temp)){
                    ResizeHover = true;
                    HoverHlutur = i;
                    break;
                }
            }
            if(ResizeHover==true) tk.teikniPanel061.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)); else 
            */
            if(ConnectHover==true) tk.teikniPanel061.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            else tk.teikniPanel061.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 

    }

    });
        
        addMouseWheelListener(new MouseAdapter(){
        @Override public void mouseWheelMoved(MouseWheelEvent e) {
            int rotation = e.getWheelRotation();
            if (rotation < 0) {
                ATscale = ATscale + 0.02;
                if(ATscale<0.3) ATscale = 0.3;
                if(ATscale>1) ATscale = 1;
                tk.teikniPanel061.ATdouble += 0.12;
            } 
            else {
                //System.out.println("Mouse wheel moved DOWN ");
                ATscale = ATscale - 0.02;
                if(ATscale<0.3) ATscale = 0.3;
                if(ATscale>1) ATscale = 1;
                tk.teikniPanel061.ATdouble -= 0.12;
                
            }
            
            
            
            //System.out.println(ATscale);
            repaint();
            //AdjustCamera();
        }});
        // Setjum handler sem bregst við músarsmelli (e. pressed)
        addMouseListener(new MouseAdapter() {
        @Override public void mousePressed(MouseEvent e) {
            previousX = e.getX();
            previousY = e.getY();
            xnew = e.getX();
            ynew = e.getY();
            
            if(e.getButton() == MouseEvent.BUTTON1)
            {   
                leftclick = true;
                if(CTRLOn){
                    
                    Point2D temp = getTranslatedPoint(e.getPoint().getX(),e.getPoint().getY());
                    FullRect = new Rectangle[fylkicounter];
                    for(int i = 0;i<fylkicounter;i++){
                        FullRect[i] = new Rectangle(fletir[i].xPos,fletir[i].yPos,fletir[i].getWidth(),fletir[i].getHeightNew());
                        if(FullRect[i].contains(temp)&&fletir[i].IsSelected==0){
                            fletir[i].IsSelected=1;
                            tk.teikniPanel061.NoPopups = true;
                            return;
                        }
                        if(FullRect[i].contains(temp)&&fletir[i].IsSelected==1){
                            fletir[i].IsSelected=0;
                            tk.teikniPanel061.NoPopups = true;
                            return;
                        }
                        
                    }
                    return;
                }
                
                
                if(tk.IsDrawing==true){
                        int xPos2 = (int)getTranslatedPoint(e.getX(),e.getY()).getX();
                        int yPos2 = (int)getTranslatedPoint(e.getX(),e.getY()).getY();
                        
                        if(drawcounter==drawfylki.length) Resizedrawfylki();
                        drawfylki[drawcounter] = new DrawClass(tk.teikniPanel061);
                        drawfylki[drawcounter].OriginHlutur = DrawnItem;
                        drawfylki[drawcounter].litur = tk.TGlitur;
                        {
                            Point2D temp = new Point2D.Double(xPos2-fletir[drawfylki[drawcounter].OriginHlutur].xPos, yPos2-fletir[drawfylki[drawcounter].OriginHlutur].yPos);
                            drawfylki[drawcounter].AddPoint(temp);
                            System.out.println("Test1: "+fletir[drawfylki[drawcounter].OriginHlutur].xPos);
                            System.out.println("Test2: "+fletir[drawfylki[drawcounter].OriginHlutur].yPos);
                        }
                        
                        drawcounter++;       
                        DrawPressed = true;
                        return;
                }
                if(ResizeHover==false&&ConnectHover==false){
                    selectrect.xPos = e.getX();
                    selectrect.yPos = e.getY();
                    selectrect.xPos2 = e.getX();
                    selectrect.yPos2 = e.getY();
                    
                    
                    Rectangle REC = new Rectangle(selectrect.GetX(),selectrect.GetY(),(selectrect.GetX2()-selectrect.GetX())+1,(selectrect.GetY2()-selectrect.GetY())+1);
                    for(int i = 0;i<fylkicounter;i++){
                        Rectangle REC2 = new Rectangle(fletir[i].xPos,fletir[i].yPos,fletir[i].getWidth(),fletir[i].getHeightNew());
                    
                        Rectangle REC3 = ATchanged.createTransformedShape(REC2).getBounds();
                    
                        if(REC.contains(REC3)||REC3.contains(REC)||REC.intersects(REC3)) {
                            fletir[i].setValdi(1);
                        }
                        else if(ShiftOn==false)fletir[i].setValdi(0);
                    }
                    
                    
                
                    
                }
                if(ConnectHover==true){
                        connectline.xPos = fletir[HoverHlutur].xPos+(fletir[HoverHlutur].getWidth())/2;
                        connectline.yPos = fletir[HoverHlutur].yPos+(fletir[HoverHlutur].getHeightNew())/2;
                        connectline.xPos2 = (int)getTranslatedPoint(e.getX(),e.getY()).getX();
                        connectline.yPos2 = (int)getTranslatedPoint(e.getX(),e.getY()).getY();
                        IsConnecting = true;
                        tk.teikniPanel061.setCursor(BlankCursor);
                }
            }
            
            if(e.getButton() == MouseEvent.BUTTON2){
                mwheelclick = true;
	    }
            if(e.getButton() == MouseEvent.BUTTON3){
                if(tk.IsDrawing==true){
                    tk.IsDrawing=false;
                    tk.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    tk.teikniPanel061.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    NoPopups = true;
                    
                    return;
                }
                rightclick = true;
                System.out.println("RightClick is: "+rightclick);
                Point2D temp = getTranslatedPoint(e.getPoint().getX(),e.getPoint().getY());
                    FullRect = new Rectangle[fylkicounter];
                    for(int i = 0;i<fylkicounter;i++){
                        FullRect[i] = new Rectangle(fletir[i].xPos,fletir[i].yPos,fletir[i].getWidth(),fletir[i].getHeightNew());
                        if(FullRect[i].contains(temp)&&tk.teikniPanel061.TPCountSelected()==0){
                            fletir[i].IsSelected=1;
                            break;
                        }
                    }
	    
                }
        }});

        // Setjum handler sem bregst við færslu músar (e. dragged) 
        addMouseMotionListener(new MouseAdapter() {
        @Override public void mouseDragged(MouseEvent e) {
            if(CTRLOn) return;
            Point2D adjPreviousPoint = getTranslatedPoint(previousX, previousY);
            Point2D adjNewPoint = getTranslatedPoint(e.getX(), e.getY());
            newX = adjNewPoint.getX() - adjPreviousPoint.getX();
            newY = adjNewPoint.getY() - adjPreviousPoint.getY();
            previousX = e.getX();
            previousY = e.getY();
            currentX += newX;
            currentY += newY;
            
            if(fylkicounter!=0)
            {
                if(DrawPressed==true){
                        int xPos2 = (int)getTranslatedPoint(e.getX(),e.getY()).getX();
                        int yPos2 = (int)getTranslatedPoint(e.getX(),e.getY()).getY();
                        
                        Point2D temp = new Point2D.Double(xPos2-fletir[drawfylki[drawcounter-1].OriginHlutur].xPos, yPos2-fletir[drawfylki[drawcounter-1].OriginHlutur].yPos);
                        drawfylki[drawcounter-1].AddPoint(temp);
                }
                
                if(leftclick==true&&rightclick==false &&mwheelclick==false)
                {
                    int dx = e.getX() - xnew;
                    int dy = e.getY() - ynew;
                    
                    xnew += dx;
                    ynew += dy;
                    if(ResizeHover==false&&IsConnecting==false&&tk.IsDrawing==false){
                        selectrect.xPos2 += dx;
                        selectrect.yPos2 += dy;
                        
                        
                        Rectangle REC = new Rectangle(selectrect.GetX(),selectrect.GetY(),(selectrect.GetX2()-selectrect.GetX())+1,(selectrect.GetY2()-selectrect.GetY())+1);
                    for(int i = 0;i<fylkicounter;i++){
                        Rectangle REC2 = new Rectangle(fletir[i].xPos,fletir[i].yPos,fletir[i].getWidth(),fletir[i].getHeightNew());
                    
                        Rectangle REC3 = ATchanged.createTransformedShape(REC2).getBounds();
                    
                        if(REC.contains(REC3)||REC3.contains(REC)||REC.intersects(REC3)) {
                            fletir[i].setValdi(1);
                        }
                        else if(ShiftOn==false)fletir[i].setValdi(0);
                    }
                        
                        
                        
                    }
                    if(ConnectHover&&tk.IsDrawing==false){
                        connectline.xPos2  += dx;
                        connectline.yPos2  += dy;
                        
                    }
                    if(ResizeHover==true&&tk.IsDrawing==false){
                        fletir[HoverHlutur].width +=newX/2;
                        fletir[HoverHlutur].height += newY/3;
                        if(fletir[HoverHlutur].width<10)fletir[HoverHlutur].width = 10;
                        if(fletir[HoverHlutur].height<5)fletir[HoverHlutur].height = 5;
                    }
                    
                }
                if(rightclick==true && leftclick==false&&mwheelclick==false&&tk.IsDrawing==false)
                {
                    for(int i = 0;i<fylkicounter;i++){
                        if(fletir[i].IsSelected==1){
                            fletir[i].xPos += newX;
                            fletir[i].yPos += newY;
                            //if(fletir[i].xPos>Xbounds-fletir[i].getWidth()-15)fletir[i].xPos = Xbounds-fletir[i].getWidth()-15;
                            //if(fletir[i].xPos<Xbounds*(-1)+13)fletir[i].xPos = Xbounds*(-1)+13;
                            //if(fletir[i].yPos>Ybounds-fletir[i].getHeightNew()-20)fletir[i].yPos = Ybounds-fletir[i].getHeightNew()-20; 
                            //if(fletir[i].yPos<Ybounds*(-1)+7)fletir[i].yPos = Ybounds*(-1)+7;
                        }
                    }
                }
                
                if(mwheelclick==true&&rightclick==false && leftclick==false){
                    int dx = e.getX() - xnew;
                    int dy = e.getY() - ynew;
                    
                    AToffsetX += dx;
                    AToffsetY += dy;
                    xnew += dx;
                    ynew += dy;
                }
                repaint();
            }  
            
        }});
        
        // Setjum handler sem bregst við lausn músarsmellis (e. released)
        addMouseListener(new MouseAdapter() {
        @Override public void mouseReleased(MouseEvent e) {
            DrawPressed = false;
            if(tk.IsDrawing) SaveToBackup();
            
           
            
            
            if(e.getButton() == MouseEvent.BUTTON1)
	    {   
                leftclick = false;
                if(tk.IsDrawing==false){
                    /*Rectangle REC = new Rectangle(selectrect.GetX(),selectrect.GetY(),(selectrect.GetX2()-selectrect.GetX())+1,(selectrect.GetY2()-selectrect.GetY())+1);
                    boolean b = false;
                    for(int i = 0;i<fylkicounter;i++){
                        Rectangle REC2 = new Rectangle(fletir[i].xPos,fletir[i].yPos,fletir[i].getWidth(),fletir[i].getHeightNew());
                    
                        Rectangle REC3 = ATchanged.createTransformedShape(REC2).getBounds();
                    
                        if(REC.contains(REC3)||REC3.contains(REC)||REC.intersects(REC3)) {
                            fletir[i].setValdi(1);
                            b = true;
                        }
                        else fletir[i].setValdi(0);
                    }*/
                
                
                
                if(tk.SmartSelectOn()==true&&ResizeHover==false&&IsConnecting==false&&TPCountSelected()==0){
                    veljahlut(e.getX(),e.getY());
                }
                if(ResizeHover==true&&tk.SmartSelectOn()==true){
                    fletir[HoverHlutur].IsSelected = 1;
                }
                    
                
                
                if(IsConnecting){
                    
                    IsConnecting=false;
                    Point2D temp = getTranslatedPoint(e.getPoint().getX(),e.getPoint().getY());
                    FullRect = new Rectangle[fylkicounter];
                    boolean temp1 = false;
                    for(int i = 0;i<fylkicounter;i++){
                        FullRect[i] = new Rectangle(fletir[i].xPos,fletir[i].yPos,fletir[i].getWidth(),fletir[i].getHeightNew());
                        if(FullRect[i].contains(temp)){
                            if(!ContainsLine(HoverHlutur,i)==true)AddLine(HoverHlutur,i);
                            temp1 = true;
                            break;
                        }
                   
                    }
                    if(temp1==false){
                        adda();
                        fletir[fylkicounter-1].xPos = (int)temp.getX();
                        fletir[fylkicounter-1].yPos = (int)temp.getY();
                        AddLine(HoverHlutur,fylkicounter-1);
                        BubbleLevel();
                    }
                    SaveToBackup();
                    repaint();
                }
                
                selectrect.xPos = 0;
                selectrect.yPos = 0;
                selectrect.xPos2 = 0;
                selectrect.yPos2 = 0;
                
                connectline.xPos = 0;
                connectline.xPos2 = 0;
                connectline.yPos = 0;
                connectline.yPos2 = 0;
                }
	    }
            if(e.getButton() == MouseEvent.BUTTON2)//Mouse3
	    {   
                mwheelclick = false;
	    }
            if(e.getButton() == MouseEvent.BUTTON3)//Rightclick
	    {   
                
                SaveToBackup();
                rightclick = false;
                
	    }
            //AdjustCamera();
            
            
          repaint();
                    
        }});
        
        
        
        
        
        
        
        addMouseListener(new MouseAdapter() {
        @Override public void mouseClicked(MouseEvent e) {
            System.out.println("MOUSE IS CLICKED");
            
            if(NoPopups){
                NoPopups=false;
                return;
            }
            
            if(tk.IsDrawing) return;
            
            previousX = e.getX();
            previousY = e.getY();
            xnew = e.getX();
            ynew = e.getY();
            
            
                    
            if(e.getButton() == MouseEvent.BUTTON1)
            {   
                
                    Point2D temp = getTranslatedPoint(e.getPoint().getX(),e.getPoint().getY());
                    TextRect = new Rectangle[fylkicounter];
                    for(int i = 0;i<fylkicounter;i++){
                        TextRect[i] = new Rectangle((int)(fletir[i].xPos+fletir[i].width),(int)(fletir[i].yPos+fletir[i].height+5),(int)(fletir[i].getWidth()-fletir[i].width*2),(int)(fletir[i].getHeightNew()-fletir[i].height*2-5*2));
                        if(TextRect[i].contains(temp)){
                            for(int p = 0;p<fylkicounter;p++){
                                fletir[p].IsSelected = 0;
                            }
                            fletir[i].IsSelected =1;
                            Point2D temp2 = getTranslatedPoint2((fletir[i].getX()+fletir[i].width),(fletir[i].getY()+fletir[i].height+5));
                            area.setRows(fletir[TPGetSingleSelected()].StringHeight);
                            area.setColumns(fletir[TPGetSingleSelected()].StringWidth);
                            if(fletir[TPGetSingleSelected()].StringWidth<2)area.setColumns(2);
                                        if(TPCountSelected()==1){
                                            for(int pi = 0;pi<fylkicounter;pi++){
                                                if(fletir[pi].IsSelected==1){

                                                    SetTextArea(fletir[pi].floturstring);
                                                    SetSlider(fletir[pi].ImageResize);
                                                    break;
                                                }
                                            }
                                        }
                            TextPopup.show(e.getComponent(), (int)temp2.getX(), (int)temp2.getY());
                            area.requestFocusInWindow();
                            if(fletir[i].floturstring.compareTo("Type\ntext\nhere")==0)area.setSelectionStart(0);
                            repaint();
                            break;

                        }
                        else {
                        }
                    }
                
                
                
                
            }
            
            if(e.getButton() == MouseEvent.BUTTON2){
                
	    }
            if(e.getButton() == MouseEvent.BUTTON3){
                //rightclick
                
                    Point2D temp = getTranslatedPoint(e.getPoint().getX(),e.getPoint().getY());
                    FullRect = new Rectangle[fylkicounter];
                    boolean temp3 = false;
                    for(int i = 0;i<fylkicounter;i++){
                        FullRect[i] = new Rectangle(fletir[i].xPos,fletir[i].yPos,fletir[i].getWidth(),fletir[i].getHeightNew());
                        if(FullRect[i].contains(temp)){
                            //if(ContainsLine(HoverHlutur,i)==false)SaveToBackup();
                            //AddLine(HoverHlutur,i);
                            System.out.println("Popup menu");
                            popupoutside.show(e.getComponent(), e.getX()-10, e.getY()-10);
                            temp3 = true;
                            break;
                        }
                    }
                    if(temp3==false){
                        popupinside.show(e.getComponent(), e.getX(), e.getY());
                    }
                
                
                
                
	    }
            repaint();
    }});
        
        
        
        //tk.LoadSaveMessage(2);
    }
    
    public BufferedImage ToBufferedImage(Image img){
        if(img instanceof BufferedImage){
            return (BufferedImage) img;
        }
        
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img,0,0,null);
        bGr.dispose();
        
        return bimage;
    }
    
    
    
    
    public void AdjustCamera(){
            if(getTranslatedPoint(getSize().getWidth(), getSize().getHeight()).getX()>2070){
                double p = getTranslatedPoint(getSize().getWidth(), getSize().getHeight()).getX() - 2070;
                //p = p/3.3;
                p = p*ATscale;
                //System.out.println("p: "+p);
                AToffsetX = AToffsetX + p;
            }
            if(getTranslatedPoint(0,0).getX()<-2070){
                double p = getTranslatedPoint(0,0).getX() + 2070;
                //p = p/3.3;
                p = p*ATscale;
                //System.out.println("p: "+p);
                AToffsetX = AToffsetX + p;
            }
            if(getTranslatedPoint(getSize().getWidth(), getSize().getHeight()).getY()>1118){
                double p = getTranslatedPoint(getSize().getWidth(), getSize().getHeight()).getY() - 1118;
                //p = p/3.3;
                p = p*ATscale;
                //System.out.println("p: "+p);
                AToffsetY = AToffsetY + p;
            }
            if(getTranslatedPoint(0,0).getY()<-1118){
                double p = getTranslatedPoint(0,0).getY() + 1118;
                //System.out.println("p before: "+p);      
                //p = p/3.3;
                p = p*ATscale;
                //System.out.println("p after: "+p);
                AToffsetY = AToffsetY + p;
            }
            repaint();
    }
        
    public void DrawOnObject(){
                for(int i = 0;i<fylkicounter;i++){
                    if(fletir[i].IsSelected==1) {
                        DrawnItem = i;
                        break;
                    }
                }
                BufferedImage ImageTemp = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) ImageTemp.getGraphics();
                g.setColor(tk.TGlitur);
                g.fillRect(0, 0, 3, 3);
                Cursor ColorCursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageTemp, new Point(0, 0), "Color Cursor");
                tk.setCursor(ColorCursor);
                tk.teikniPanel061.setCursor(ColorCursor);
                g.dispose();
                
                tk.IsDrawing = true;
                repaint();
}
    
    public int TPGetSingleSelected(){
        for(int i = 0;i<fylkicounter;i++){
            if(fletir[i].IsSelected==1) return i;
        }
        return 0;
    }
    
    public int TPCountSelected(){
        int x = 0;
        for(int i = 0;i<fylkicounter;i++){
            if(fletir[i].IsSelected==1) x++;
        }
        return x;
    }
    
    public void TooManyBubbles(){
        JOptionPane.showMessageDialog(this, "Too many bubbles selected for this action.", "Warning", 1);
    }
    public void TooFewBubbles(){
        JOptionPane.showMessageDialog(this, "You must select at least one bubble for this action.", "Warning", 1);
    }
    
    
    // Þetta fall sér um að breyta textanum í TextArea.
    public void SetTextArea(String x){
        area.setText(x);
    }
    //Þetta fall sér um að breyta value Slider-sins.
    public void SetSlider(int x){
        testerslider.setValue(x);
    }
    
    
    public void veljahlut(int x, int y){ //Fann sem ser um að "velja" hlutinn sem er næst músaclickinu á panelinum.
        if(fylkicounter==0)return;
        int counter = 0;
        Point2D temp = getTranslatedPoint(x,y);
        double distance = 100000;
        for(int i = 0; i<fylkicounter;i++)
        {
            double pythagoras = Math.sqrt(Math.pow(temp.getX()-fletir[i].xPos,2)+Math.pow(temp.getY()-fletir[i].yPos,2));
            if(pythagoras<distance)
            {
                counter = i;
                distance = pythagoras;
            }
        }
        fletir[counter].setValdi(1);
    }
    
    
    // Fall sem núllstillir teikniborðið
    public void restarT(boolean IsLoading) 
    {
        for(int i = 0; i<fylkicounter;i++)
        {
            fletir[i] = null;
        }
        fylkicounter = 0;
        if(!IsLoading){
            adda();
            fletir[0].xPos = -430;
            fletir[0].yPos = -141;
        }
        
        for(int i = 0; i<linecounter;i++)
        {
            Strik[i] = null;
        }
        linecounter = 0;
        //valdihlutur = 0;
        UndoArray = new String[]{"","","","","","","","","",""};
        UndoCounter = 0;
        drawfylki = new DrawClass [10];
        drawcounter = 0;
        UnSaved = false;
        repaint();
    }
    public void LoadFromFile(int FromFile) throws FileNotFoundException, IOException{ // Þetta fall opnar hugkort frá skrá.
        String content = "";
        
        String[][] a = new String[1000][15];
        String[][] b = new String[1000][3];
        String[][] c = new String[1000][1000];
        int column = 0;
        int row = 0;
        int column2 = 0;
        int row2 = 0;
        int column3 = 0;
        int row3 = 0;
        int ObjectOrLine = 0;
        if(FromFile==0){
            String x = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
            new File(x+"/LightMind Projects").mkdir();
            x = x + "\\LightMind Projects";
            JFileChooser jFile = new JFileChooser(x);
            jFile.addChoosableFileFilter(new FileNameExtensionFilter("LightMind (.Lmap)", "Lmap"));
            jFile.setFileFilter(jFile.getChoosableFileFilters()[1]);
            if(jFile.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
            File f = jFile.getSelectedFile();
            tk.LoadPath = f.toPath()+"";
            tk.setTitle("LightMind "+f.toPath());
            System.out.println(tk.LoadPath);
            content = new String(Files.readAllBytes(Paths.get(f+"")));
        }
        else if(FromFile==1&&UndoCounter>1)content = UndoArray[(--UndoCounter)-1];
        else if(FromFile==2&&UndoCounter<UndoArray.length&&UndoArray[UndoCounter].compareTo("")!=0) content = UndoArray[(++UndoCounter)-1];
        else if(FromFile==3) {
            content = new String(Files.readAllBytes(Paths.get(menuitempath)));
            tk.LoadPath = menuitempath;
            tk.setTitle("LightMind "+menuitempath);
        }
        else return;
        //System.out.println("hello: "+content);
        for(String line0 : content.split(".!!!.")){
            if(ObjectOrLine == 0){
                for(String line1 : line0.split(".!!.")){
                    if(line1==null) break;
                    column = 0;
                    for(String item : line1.split(".!.")){
                        a[row][column]=item;
                        column++;
                    }
                    row++;
                }
            }
            if(ObjectOrLine==1){
                for(String line1 : line0.split(".!!.")){
                    if(line1==null) break;
                    column2 = 0;
                    for(String item : line1.split(".!.")){
                        b[row2][column2]=item;
                        column2++;
                    }
                    row2++;
                }
            }
            if(ObjectOrLine==2){
                for(String line1 : line0.split(".!!.")){
                    if(line1==null) break;
                    column3 = 0;
                    for(String item : line1.split(".!.")){
                        c[row3][column3]=item;
                        column3++;
                    }
                    row3++;
                }
            }
            ObjectOrLine++;
        }
        restarT(true);
        for(int i = 0;i<row;i++){
            if(a[i][1]==null)break;
            adda();
            fletir[i].xPos = Integer.parseInt(a[i][1]);
            fletir[i].yPos = Integer.parseInt(a[i][2]);
            fletir[i].width = Double.parseDouble(a[i][3]);  
            fletir[i].height = Double.parseDouble(a[i][4]);
            fletir[i].StringWidth = Integer.parseInt(a[i][5]);
            fletir[i].StringHeight = Integer.parseInt(a[i][6]);
            fletir[i].ImageResize = Integer.parseInt(a[i][7]);
            
            fletir[i].litur = new Color(Integer.parseInt(a[i][8]));
            
            fletir[i].floturstring = a[i][9];
            String image = a[i][10];
            fletir[i].img = StringToImage(image);
            
            if(a[i][11]!=null) fletir[i].shape = Integer.parseInt(a[i][11]);
            else fletir[i].shape = 0;
            if(a[i][12]!=null) fletir[i].bordercolor = new Color(Integer.parseInt(a[i][12]));
            if(a[i][13]!=null) fletir[i].ImageWidth = Integer.parseInt(a[i][13]);
            if(a[i][14]!=null) fletir[i].ImageHeight = Integer.parseInt(a[i][14]);
        }
        for(int i = 0;i<row2;i++){
            if(b[i][1]==null)break;
            AddLine(Integer.parseInt(b[i][1]),Integer.parseInt(b[i][2]));
        }
        for(int i = 0;i<row3;i++){
            //System.out.println("....1");
            if(c[i][0]==null)break;
            //System.out.println("....2");
            int tempcounter = 0;
            //System.out.println("....3");
            if(drawcounter==drawfylki.length) Resizedrawfylki();
            //System.out.println("....4");
            drawfylki[i] = new DrawClass(tk.teikniPanel061);
            //System.out.println("....5");
            drawfylki[i].litur = new Color(Integer.parseInt(c[i][1]));
            //System.out.println("....6");
            drawfylki[i].OriginHlutur = Integer.parseInt(c[i][2]);
            //System.out.println("....7");
            tempcounter = Integer.parseInt(c[i][3]);
            //System.out.println("....8");
            for(int kok = 4;kok<(tempcounter+4);kok++){
                if( c[i][kok]!=null ) drawfylki[i].AddPoint(ToPoint(c[i][kok]));
                //System.out.println("....9 "+"kok: "+kok);
                if( c[i][kok]==null)break;
            }
            drawcounter++;
        }
        if(FromFile==0){
            ClearBackup();
            SaveToBackup();
            LoadSaveMessage(0);
        }
        if(FromFile==0||FromFile==3){
            UpdateLastOpened();
        }
    }
    
    public void UpdateLastOpened(){
        String temp[] = new String[5];
            temp[0] = tk.LoadPath;
            int counter = 1;
            for(int i = 0; i<tk.LastOpened.length;i++){
                if(tk.LastOpened[i]==null) break;
                if(tk.LastOpened[i].compareTo("")==0) break;
                if(tk.LastOpened[i].compareTo("null")==0) break;
                if(counter==5)break;
                if(
                        tk.LastOpened[i].compareTo(temp[0])!=0
                        &&
                        DoesNotContain(tk.LastOpened[i],temp
                        )) temp[counter++] = tk.LastOpened[i];
            }
            tk.LastOpened = temp;
        try {
            tk.DatMaker(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TeikniPanel06.class.getName()).log(Level.SEVERE, null, ex);
        }
        tk.LoadMenu();
    }
    
    public boolean DoesNotContain(String x,String temp[]){
        for(int i = 0; i<tk.LastOpened.length;i++){
                if(temp[i]==null) break;
                if(temp[i].compareTo("")==0) break;
                if(temp[i].compareTo("null")==0) break;
                if(temp[i].compareTo(x)==0) return false;
            }
        return true;
    }
    public void SetPath(String x){
        menuitempath = x;
    }
    
    public Point2D ToPoint(String x){
        //System.out.println("....11");
        boolean b = false;
        //System.out.println("....12");
        double d1 = 0;
        //System.out.println("....13");
        double d2 = 0;
        //System.out.println("....14");
        for(String line : x.split(",")){
            if(b==false){
                d1 = Double.parseDouble(line);
                b = true;
            }
            else d2 = Double.parseDouble(line);
        }
        //System.out.println("....15");
        Point2D y = new Point2D.Double(d1, d2);
        //System.out.println("....16");
        return y;
    }
    
    //Býr til preview hugkort til að hægt sé að skoða.
    public void PreviewMaker() throws FileNotFoundException, UnsupportedEncodingException{
        String x = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        new File(x+"/LightMind Projects").mkdir();
        x = x + "\\LightMind Projects\\Preview.Lmap";
        File y = new File (x);
        if(!y.exists()){
            PrintWriter writer = new PrintWriter(x, "UTF-8");
            writer.print("0.!.545.!.369.!.5.!.5.!.7.!.1.!.100.!.-65281.!.Magenta.!.null.!.0.!!.1.!.544.!.320.!.5.!.5.!.6.!.1.!.100.!.-256.!.Yellow.!.null.!.0.!!.2.!.544.!.409.!.5.!.5.!.3.!.1.!.100.!.-65536.!.Red.!.null.!.0.!!.3.!.33.!.339.!.5.!.5.!.7.!.1.!.100.!.-65536.!.Objects.!.null.!.1.!!.4.!.211.!.491.!.5.!.5.!.13.!.1.!.100.!.-65536.!.Object Shapes.!.null.!.2.!!.5.!.353.!.434.!.5.!.5.!.4.!.1.!.100.!.-65281.!.Oval.!.null.!.1.!!.6.!.351.!.535.!.5.!.5.!.6.!.1.!.100.!.-65281.!.Square.!.null.!.2.!!.7.!.348.!.491.!.5.!.5.!.14.!.1.!.100.!.-65281.!.Rounded Square.!.null.!.0.!!.8.!.189.!.191.!.5.!.5.!.13.!.1.!.100.!.-65536.!.Object Colors.!.null.!.2.!!.9.!.545.!.452.!.5.!.5.!.5.!.1.!.100.!.-16711936.!.Green.!.null.!.0.!!.10.!.546.!.35.!.5.!.5.!.5.!.1.!.100.!.-16777216.!.Black.!.null.!.0.!!.11.!.547.!.122.!.5.!.5.!.4.!.1.!.100.!.-8355712.!.Gray.!.null.!.0.!!.12.!.545.!.80.!.5.!.5.!.9.!.1.!.100.!.-12566464.!.Dark Gray.!.null.!.0.!!.13.!.543.!.496.!.5.!.5.!.6.!.1.!.100.!.-14336.!.Orange.!.null.!.0.!!.14.!.548.!.215.!.5.!.5.!.4.!.1.!.100.!.-20561.!.Pink.!.null.!.0.!!.15.!.547.!.166.!.5.!.5.!.10.!.1.!.100.!.-4144960.!.Light Gray.!.null.!.0.!!.16.!.544.!.266.!.5.!.5.!.4.!.1.!.100.!.-16711681.!.Cyan.!.null.!.0.!!..!!!.0.!.3.!.4.!!.1.!.4.!.5.!!.2.!.4.!.6.!!.3.!.4.!.7.!!.4.!.3.!.8.!!.5.!.8.!.0.!!.6.!.8.!.2.!!.7.!.8.!.1.!!.8.!.8.!.13.!!.9.!.8.!.11.!!.10.!.8.!.10.!!.11.!.8.!.9.!!.12.!.8.!.12.!!.13.!.8.!.15.!!.14.!.8.!.16.!!.15.!.8.!.14.!!.");
            writer.close();
        }
    }
    
    

    
    
    public void CTRLSave() throws FileNotFoundException, UnsupportedEncodingException{
        
        PrintWriter writer = new PrintWriter(tk.LoadPath, "UTF-8");
        writer.print(BackupString());
        writer.close();
        LoadSaveMessage(1);
        UnSaved=false;
    }
    
    
    // Þetta fall vistar hugkortið í skrá.
    public boolean SaveToFile() throws FileNotFoundException, UnsupportedEncodingException{
        
        
        
        String x = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        new File(x+"/LightMind Projects").mkdir();
        x = x + "\\LightMind Projects";
        JFileChooser jFile = new JFileChooser(x);
        jFile.addChoosableFileFilter(new FileNameExtensionFilter("LightMind (.Lmap)", "Lmap"));
        jFile.setFileFilter(jFile.getChoosableFileFilters()[1]);
        if(jFile.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return false ;
        File f = jFile.getSelectedFile();
        tk.LoadPath = f+"";
        String xin = f+"";
        if(xin.indexOf(".") > 0) xin = xin.substring(0, xin.lastIndexOf("."));
        PrintWriter writer = new PrintWriter(xin+".Lmap", "UTF-8");
        writer.print(BackupString());
        writer.close();
        UnSaved=false;
        LoadSaveMessage(1);
        UpdateLastOpened();
        
        //tk.teikniPanel061.requestFocusInWindow();
        return true;
    }
    
    public void LoadSaveMessage(int b){
        //b = 0 = loading
        //b = 1 = saving
        //b = 2 = Welcome
        //b = 3 = Undo
        //b = 4 = redo
        timer.cancel();
        timer2.cancel();
        SystemMessageAlpha = 255;
        repaint();
        if(b==0)SystemMessage = "File Loaded: " +tk.LoadPath;
        if(b==1)SystemMessage = "File Saved: " +tk.LoadPath;
        if(b==2)SystemMessage = "Welcome!";
        if(b==3)SystemMessage = "Undo";
        if(b==4)SystemMessage = "Redo";
        
        //final Timer timer = new Timer();
        timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                SystemMessageAlpha = SystemMessageAlpha -1;
                if (SystemMessageAlpha <= 0) {
                  SystemMessageAlpha = 0;
                  timer.cancel();
                }
                repaint();
                
            }
        },3000,10);
        timer2 = new Timer();
        SystemMessage2 = "";
        timer2counter = 5550;
        repaint();
        timer2.schedule(new TimerTask(){
            @Override
            public void run(){
                timer2counter = timer2counter-10;
                if (timer2counter <= 0) {
                    timer2counter = 0;
                    timer2.cancel();
                }
                else if (timer2counter <= 314) {
                    SystemMessage2 = "";
                }
                else if (timer2counter <= 622) {
                    SystemMessage2 = ")";
                }
                else if (timer2counter <= 930) {
                    SystemMessage2 = "■)";
                }
                else if (timer2counter <= 1238) {
                    SystemMessage2 = "_■)";
                }
                else if (timer2counter <= 1546) {
                    SystemMessage2 = "■_■)";
                }
                else if (timer2counter <= 1854) {
                    SystemMessage2 = "⌐■_■)";
                }
                else if (timer2counter <= 2162) {
                    SystemMessage2 = "(⌐■_■)";
                }
                else if (timer2counter <= 2470) {
                    SystemMessage2 = "(⌐■_■)";
                }
                else if (timer2counter <= 2778) {
                    SystemMessage2 = "(⌐■_■)";
                }
                else if (timer2counter <= 3086) {
                    SystemMessage2 = "( •_•)>⌐■-■";
                }
                else if (timer2counter <= 3394) {
                    SystemMessage2 = "( •_•)>⌐■-■";
                }
                else if (timer2counter <= 3702) {
                    SystemMessage2 = "( •_•)";
                }
                else if (timer2counter <= 4010) {
                    SystemMessage2 = " •_•)";
                }
                else if (timer2counter <= 4318) {
                    SystemMessage2 = "•_•)";
                }
                else if (timer2counter <= 4626) {
                    SystemMessage2 = "_•)";
                }
                else if (timer2counter <= 4934) {
                    SystemMessage2 = "•)";
                }
                else if (timer2counter <= 5242) {
                    SystemMessage2 = ")";
                }
                
                
                
                repaint();
                
            }
        },0,10);
    }
    
    
    
    public String BackupString(){
        String TheSave = "";
        
        if(fylkicounter>0)
        {
            for(int i = 0;i<fylkicounter;i++)
            {
                String image = "";
                if(fletir[i].img!=null)image = ImageToString(fletir[i].img);
                TheSave = TheSave.concat(i+".!."+
                        fletir[i].xPos+".!."+
                        fletir[i].yPos+".!."+
                        fletir[i].width+".!."+
                        fletir[i].height+".!."+
                        fletir[i].StringWidth+".!."+
                        fletir[i].StringHeight+".!."+
                        fletir[i].ImageResize+".!."+
                        Integer.toString(fletir[i].litur.getRGB())+".!."+
                        fletir[i].floturstring+".!."+
                        image+".!."+
                        fletir[i].shape+".!."+
                        Integer.toString(fletir[i].bordercolor.getRGB())+".!."+
                        fletir[i].ImageWidth+".!."+
                        fletir[i].ImageHeight+".!!."
                
                );
                
            }
            TheSave = TheSave.concat(".!!!.");
            if(linecounter>0)
            {
                for(int i = 0;i<linecounter;i++)
                {
                    TheSave = TheSave.concat(i+".!."+
                            Strik[i].OriginHlutur+".!."+
                            Strik[i].EndHlutur+".!!.");
                }
            }
            TheSave = TheSave.concat(".!!!.");
            if(drawcounter>0)
            {
                for(int i = 0;i<drawcounter;i++)
                {
                    TheSave = TheSave.concat(i+".!."+
                            Integer.toString(drawfylki[i].litur.getRGB())+".!."+
                            drawfylki[i].OriginHlutur+".!."+
                            drawfylki[i].pointcounter);
                    for(int pup = 0;pup<drawfylki[i].pointcounter;pup++){
                        TheSave = TheSave.concat(".!."+drawfylki[i].pointarray[pup].getX()+","+drawfylki[i].pointarray[pup].getY());
                    }
                    TheSave = TheSave.concat(".!!.");
                }
                
            }
        
        }
        return TheSave;
    }
    public void SaveToBackup(){
        //System.out.println("SaveToBackup(), Before UndoCounter: "+UndoCounter);
        if(UndoCounter==UndoArray.length){
            for(int i = 0;i<=(UndoCounter-2);i++){
                UndoArray[i] = UndoArray[i+1];
            }
            UndoArray[UndoCounter-1] = BackupString();
        }
        else UndoArray[UndoCounter++] = BackupString();
        UnSaved=true;
    }
    
    public void ClearBackup(){
        for(int i = 0;i<UndoArray.length;i++){
            UndoArray[i] = "";
        }
        UndoCounter = 0;
    }
    

    public boolean ContainsLine(int x, int y){
        boolean b = false;
        for(int i = 0;i<linecounter;i++){
            if(Strik[i].OriginHlutur==x&&Strik[i].EndHlutur ==y) return true;
        }
        return b;
    }
    
    
    public void AddLine(int x, int y){ // Þetta fall sér um að bæta við tengingum milli hluta.
        if(x==y)return;
        for(int i = 0;i<linecounter;i++){
            if(Strik[i].OriginHlutur==x&&Strik[i].EndHlutur ==y) return;
        }
        if(linecounter==Strik.length) ResizeLine();
        Strik[linecounter] = new Lines (this);
        Strik[linecounter].OriginHlutur = x;
        Strik[linecounter].EndHlutur = y;
        linecounter++;
    }
    public void ResizeLine(){ // Þetta fall sér um að stækka fylkið sem heldur utan um línurnar ef plássið í fylkinu klárast.
        Lines [] Strik2 = new Lines [Strik.length*2];
        for(int i = 0;i<linecounter;i++){
            Strik2[i] = Strik[i];
        }
        Strik = Strik2;
    }
    public void Resizedrawfylki(){ // Þetta fall sér um að stækka fylkið sem heldur utan um línurnar ef plássið í fylkinu klárast.
        DrawClass [] drawfylki2 = new DrawClass [drawfylki.length*2];
        for(int i = 0;i<drawcounter;i++){
            drawfylki2[i] = drawfylki[i];
        }
        drawfylki = drawfylki2;
    }
    
    public void RemoveOneLine(int x, int y){ // Þetta fall um að eyða tengingu milli tveggja hluta.
        if(linecounter==0) return;
        Lines[] Strik2 = new Lines [Strik.length];
        int newcounter = 0;
        for(int i = 0;i<linecounter;i++){
            if((Strik[i].OriginHlutur==x&&Strik[i].EndHlutur==y)||(Strik[i].OriginHlutur==y&&Strik[i].EndHlutur==x)) ;
            else Strik2[newcounter++] = Strik[i];
        }
        Strik = Strik2;
        linecounter = newcounter;
    }
    
    public void RemoveLines(int x){ //Þetta fall sér um að eyða öllum línum frá völdum hlut.
        if(linecounter==0) return;
        Lines[] Strik2 = new Lines [Strik.length];
        int newcounter = 0;
        for(int i = 0;i<linecounter;i++){
            if(Strik[i].OriginHlutur!=x&&Strik[i].EndHlutur!=x) Strik2[newcounter++] = Strik[i];
        }
        Strik = Strik2;
        linecounter = newcounter;
    }
    public void RemoveLinesTo(int x){ //Þetta fall sér um að eyða öllum línum frá völdum hlut.
        if(linecounter==0) return;
        Lines[] Strik2 = new Lines [Strik.length];
        int newcounter = 0;
        for(int i = 0;i<linecounter;i++){
            if(Strik[i].EndHlutur!=x) Strik2[newcounter++] = Strik[i];
        }
        Strik = Strik2;
        linecounter = newcounter;
    }
    
    public void RemoveLinesFrom(int x){ //Þetta fall sér um að eyða öllum línum frá völdum hlut.
        if(linecounter==0) return;
        Lines[] Strik2 = new Lines [Strik.length];
        int newcounter = 0;
        for(int i = 0;i<linecounter;i++){
            if(Strik[i].OriginHlutur!=x) Strik2[newcounter++] = Strik[i];
        }
        Strik = Strik2;
        linecounter = newcounter;
    }
    
    public void RemoveDrawing(int x){ //Þetta fall sér um að eyða öllum línum frá völdum hlut.
        if(drawcounter==0) return;
        DrawClass[] drawfylki2 = new DrawClass [drawfylki.length];
        int newcounter = 0;
        for(int i = 0;i<drawcounter;i++){
            if(drawfylki[i].OriginHlutur==x) ;
            else drawfylki2[newcounter++] = drawfylki[i];
        }
        drawfylki = drawfylki2;
        drawcounter = newcounter;
    }
    
    
    public void RemoveBubble(){ // Þetta fall sér um að eyða völdum hlut.
        if(fylkicounter<1)return;
        for(int i = fylkicounter-1;i>=0;i--){
            if(fletir[i].IsSelected==1) {
                RemoveLines(i);
                RemoveDrawing(i);
                
                fletir[i] = fletir[fylkicounter-1];
                fletir[fylkicounter-1] = null;
                for(int p = 0;p<linecounter;p++){
                    if(Strik[p].OriginHlutur==fylkicounter-1) Strik[p].OriginHlutur = i;       
                    if(Strik[p].EndHlutur==fylkicounter-1) Strik[p].EndHlutur = i;
                }
                for(int p = 0;p<drawcounter;p++){
                    if(drawfylki[p].OriginHlutur==fylkicounter-1) drawfylki[p].OriginHlutur = i;
                }
                fylkicounter--;
            }
        }
        SaveToBackup();
        
    }
    public void ResizeFlotur(){ //Þetta fall sér um að stækka fylkið sem heldur utan um hlutina ef plássið klárast.
        Flotur [] fletir2 = new Flotur [fletir.length*2];
        for(int i = 0;i<fylkicounter;i++){
            fletir2[i] = fletir[i];
        }
        fletir = fletir2;
    }
    
    
    public void BubbleLevel(){
        int x = fylkicounter-1;
        System.out.println("Fylkicounter: "+fylkicounter);
        int counter = 1;
        System.out.println("Level Counter: PREEEEE");
        for(int i = 0;i<linecounter;i++){
            
            if(Strik[i].EndHlutur==x){
                x = Strik[i].OriginHlutur;
                counter++;
                System.out.println("Level Counter: "+counter);
                i = 0;
            }
            if(counter>100)return;
        }
        if(counter==1)fletir[fylkicounter-1].litur = new Color(-16711936);
        else if(counter==2)fletir[fylkicounter-1].litur = new Color(-256);
        else if(counter==3)fletir[fylkicounter-1].litur = new Color(-10066177);
        else if(counter==4)fletir[fylkicounter-1].litur = new Color(-14336);
        else if(counter==5)fletir[fylkicounter-1].litur = new Color(-65281);
        else if(counter>5)fletir[fylkicounter-1].litur = new Color(-16711936);
    }
    
// Fall sem bætir við fleiri hlutum í fylkið fletir.
    public void adda(){
        if(fylkicounter>=fletir.length) ResizeFlotur();
        fletir[fylkicounter] = new Flotur (this);
        
        fylkicounter++;
        
        repaint();
    }
    
    // Convert the panel coordinates into the cooresponding coordinates on the translated image.
    public Point2D getTranslatedPoint(double panelX, double panelY) {
         
        //AffineTransform tx = getCurrentTransform();
        AffineTransform tx = ATchanged;
        Point2D point2d = new Point2D.Double(panelX, panelY);
        try {
            return tx.inverseTransform(point2d, null);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
            return null;
        }
         
    }
    public Point2D getTranslatedPoint2(double panelX, double panelY) {
         
        //AffineTransform tx = getCurrentTransform();
        AffineTransform tx = ATchanged;
        Point2D point2d = new Point2D.Double(panelX, panelY);
        tx.transform(point2d, point2d);
        return point2d;
         
    }
    
    
    
    public String ImageToString(BufferedImage bImage)   {
        String imageString = null;

        //String formatName = path.substring(path.lastIndexOf('.')+1, path.length());

        //image to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "png", baos);
            baos.flush();
            byte[] imageAsRawBytes = baos.toByteArray();
            baos.close();

            //bytes to string
            imageString = new String(Base64Coder.encode(imageAsRawBytes));
            //don't do this!!!
            //imageString = new String(imageAsRawBytes);*/
        } catch (IOException ex) {
            //Logger.getLogger(ImageToXML.class.getName()).log(Level.SEVERE, null, ex);
        }

        return imageString;
    }
    
     public BufferedImage StringToImage(String imageString)    {
        //string to ByteArrayInputStream
        BufferedImage bImage = null;
        try {
            byte[] output = Base64Coder.decode(imageString);
            ByteArrayInputStream bais = new ByteArrayInputStream(output);
            bImage = ImageIO.read(bais);
        } catch (IOException ex) {
            //Logger.getLogger(ImageToXML.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bImage;
    }
    
    
   
    /** 
    * Færir ferninginn raudur í punktinn (x,y) 
    * @param x x gildi á punktinum (x,y) sem á að færa ferningin í  
    * @param y y gildi á punktinum (x,y) sem á að færa ferningin í  
    */

    

@Override 
    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
  /**
   * Teiknar hluti á kompónentinn 
   * @param g Graphics hlutur sem á að teikna á 
   */
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g); 
        //System.out.println(tk.WallPaper);
        /*for (int x = 0; x < this.getWidth(); x += paper.getWidth()) {
            for (int y = 0; y < this.getHeight(); y += paper.getHeight()) {
                g.drawImage(paper,0,0,null);
                g.drawImage(paper, x, y, paper.getWidth(), paper.getHeight(), this);
            }
        }*/
        for (int x = 0; x < this.getWidth(); x += tk.WallPaper.getWidth()) {
            for (int y = 0; y < this.getHeight(); y += tk.WallPaper.getHeight()) {
                g.drawImage(tk.WallPaper,0,0,null);
                g.drawImage(tk.WallPaper, x, y, tk.WallPaper.getWidth(), tk.WallPaper.getHeight(), this);
            }
        }
        
        
        
        
        
        /*Color shadowtest = new Color(0,128,255,100);
        Color red = Color.red;
            g.setColor(shadowtest);
            g.fillRect(0, 0,2000,2000);*/
        
        Graphics2D g2d = (Graphics2D) g;
        AToriginal = g2d.getTransform();
        ATchanged = new AffineTransform();
        double centerX = (double)getWidth() / 2;
        double centerY = (double)getHeight() / 2;
        ATchanged.translate(AToffsetX,AToffsetY);
        ATchanged.translate(centerX,centerY);
        ATchanged.scale(ATscale, ATscale);
        //ATchanged.translate(currentX, currentY); ///////////////// The Problem ///////////////////////
        g2d.transform(ATchanged);
        //System.out.println(g2d.getTransform());
        
        //AffineTransform AT2 = new AffineTransform(1.0+ATdouble, 0+ATdouble, 0+ATdouble, 1+ATdouble, 0+ATdouble,0+ATdouble);
        //g2d.transform(AT2);
        if(fylkicounter!=0)
        {
            for(int i = 0; i<fylkicounter;i++)
            {

                    fletir[i].painT(g);

            }
        }
        
        if(linecounter!=0){
            for(int i = 0;i<linecounter;i++){
                Strik[i].painT(g);
            }
        }
        if(IsConnecting)connectline.painT(g);
        if(fylkicounter!=0)
        {
            for(int i = 0; i<fylkicounter;i++)
            {

                    fletir[i].painT(g);

            }
        }
        if(drawcounter!=0){
            for(int i = 0; i<drawcounter;i++)
            {

                    drawfylki[i].painT(g);

            }
        }
        g2d.setTransform(AToriginal);
        
        selectrect.painT(g);
        
        
        
        int xt = 255;
        int xs = 0;
        Color shadowtest = new Color(xs,xs,xs,SystemMessageAlpha);
        Color whiteshadow = new Color(xt,xt,xt,SystemMessageAlpha);
        Color shadowtest2 = new Color(xs,xs,xs,SystemMessageAlpha/4);
        g.setColor(shadowtest2);
        g.fillRoundRect(-30, tk.teikniPanel061.getHeight()-60, 800, 55, 50, 50);
        g.setFont(new Font("Lucida Console", Font.BOLD, 13));
        
        g.setColor(whiteshadow);
        g.fillRoundRect(-30, tk.teikniPanel061.getHeight()-60, 800, 50, 50, 50);
        
        
        g.setColor(shadowtest);
        g.drawRoundRect(-30, tk.teikniPanel061.getHeight()-60, 800, 50, 50, 50);
        g.drawString(SystemMessage, 20, tk.teikniPanel061.getHeight()-20);
        g.setColor(Color.black);
        g.drawString(SystemMessage2, 20, tk.teikniPanel061.getHeight()-40);
        /*if(drawcounter!=0){
            for(int i = 0; i<drawcounter;i++)
            {

                    drawfylki[i].painT(g);

            }
        }*/
        
        
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

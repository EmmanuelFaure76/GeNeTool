/*
**    Copyright (C) 2010-2014 Emmanuel FAURE, 
**                California Institute of Technology
**                            Pasadena, California, USA.
**
**    This library is free software; you can redistribute it and/or
**    modify it under the terms of the GNU Lesser General Public
**    License as published by the Free Software Foundation; either
**    version 2.1 of the License, or (at your option) any later version.
**
**    This library is distributed in the hope that it will be useful,
**    but WITHOUT ANY WARRANTY; without even the implied warranty of
**    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
**    Lesser General Public License for more details.
**
**    You should have received a copy of the GNU Lesser General Public
**    License along with this library; if not, write to the Free Software
**    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
**
**    GeNeTool Port to Eclipse Framework: William J. R. Longabaugh
**    Copyright (C) 2014 Institute for Systems Biology 
**                       Seattle, Washington, USA. 
**
**
*/

package grnboolmodel;

import java.awt.Event;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import processing.core.PApplet;
import processing.core.PImage;
import processing.pdf.PGraphicsPDF;

//import sojamo.drop.DropEvent;
//import sojamo.drop.SDrop;


public class GRNBoolModel extends PApplet {

 // SDrop drop; //For dropping a Model file inside the window
  
  // pulled from ModelDomain.pde

  int maxStep=100;

  // pulled from Objet.pde

  Objet ObjetDrag=null;

  PImage Good, Wrong, Unknown,Ubiquitous,Redo,Reset,Lock,Unlock,Duplicate,colorWheel;
  
  PImage logo;
  
  int verbose=2;
  String NameApplication="GRNBoolModel";
  String pkgName="grnboolmodel.";
  
  int ScreenX;int ScreenY; float Scale=1; int frameR=20;
  String Version="ISB Thu Oct 2 16:21:21 PDT 2014"; //"2011-10-04-14-37-02";
  
  String DirData="data/";//dataPath("");
  
  ObjectManager om;
  RuleReader rr;
  SaveManager sm;
  DomainMenuManager dmm;
  GeneMenuManager gmm;
  ModelMenuManager mmm;
  NanoMenuManager nmm;
  MenuBarManager mbm;
  SearchMenuManager smm;
  CompareMenuManager cmm;
  DataMenuManager tmm;
  EventHandler eh;
  ColorMenu cm;
  RegionManager rm;
  DomainManager dm;
  MenuManager mm;
  GeneManager gm;
  ModelManager lm;
  OperatorManager pm;
  UtilityFuncs uf;
  
  public GRNBoolModel() {
    om = new ObjectManager(this);
    rr = new RuleReader(this);
    sm = new SaveManager(this);
    mbm = new MenuBarManager(this);
    dmm = new DomainMenuManager(this);
    gmm = new GeneMenuManager(this);
    mmm = new ModelMenuManager(this);
    nmm = new NanoMenuManager(this);
    smm = new SearchMenuManager(this);
    cmm = new CompareMenuManager(this);
    tmm = new DataMenuManager(this);
    eh = new EventHandler(this);
    cm = new ColorMenu(this);
    // GM, RM must init before DM:
    gm = new GeneManager(this);
    rm = new RegionManager(this);
    dm = new DomainManager(this);
    mm = new MenuManager(this);
    lm = new ModelManager(this);
    pm = new OperatorManager(this);
    uf = new UtilityFuncs(this);
    
  }

  @Override
  public void setup() {
    ScreenX=displayWidth;ScreenY=displayHeight-100;
    size(ScreenX,ScreenY); 
     
    this.frame = (Frame)SwingUtilities.getWindowAncestor(this);

    cm.initFonts();
    eh.initMouse();
    pm.initOperators();
    mm.initMenu();
    
    frameRate(frameR); 
       
    
     //DO NOT WORK RESIZE WAIT FOR NEXT VERSION OF PROCESSING
    frame.setResizable(true);  
    textMode(MODEL);
    //size(ScreenX,ScreenY, OPENGL);
    
   
  
 //   drop = new SDrop(this); //Drop XML Element in the frame
    ellipseMode(CENTER); 
  
    //Load Image
    mm.trashEmpty=loadImage(DirData+"trash_empty.png");
    mm.trashFull=loadImage(DirData+"trash_full.png");
   
   
    Good=loadImage(DirData+"Good.png");
    Wrong=loadImage(DirData+"Wrong.png");
    Unknown=loadImage(DirData+"unknown.png");
    Ubiquitous=loadImage(DirData+"Ubiquitous.png");
    Redo=loadImage(DirData+"Redo.png");  
    Reset=loadImage(DirData+"Reset.png");
    Duplicate=loadImage(DirData+"duplicate.png");
    Lock=loadImage(DirData+"lock.png");
    Unlock=loadImage(DirData+"unlock.png");
    colorWheel=loadImage(DirData+"WheelColor.png"); colorWheel.resize(mm.SizeWheelColor,mm.SizeWheelColor);
   
   logo=loadImage(DirData+"LogoGRN.png"); //NOT USE UET
   ImageIcon titlebaricon = new ImageIcon(loadBytes(DirData+"LogoGRN.png"));
   frame.setIconImage(titlebaricon.getImage());
  
   
   initAbout();
   
   
   String NameModel="";
   NameModel="Model-Soxb1.xml";
   NameModel="Model-Transplant-Soxb1-ES.xml";
   NameModel="Model-Transplant-Soxb1.xml";
   NameModel="Model.xml";
   NameModel="Model+Perturbation.xml";
   NameModel="ModelHours-transplant.xml";
   NameModel="TempModel.xml";
     //frame.setIconImage(logo.image);
    // initRegion();
    
   // ReadRules("/Users/emmanuelfaure/Documents/Processing/USA/GRNBoolModelData/"+NameModel);
  // ReadRules("/Users/emmanuelfaure/Desktop/ee.xml");
   //try{ ReadNanoString(DirModel+"TC-DATA.csv");} catch (Exception e){addMessage("INIT There is no NanoString file");}
   
   // try {ReadRules(DirModel+"Model-Transplant-Soxb1-ES.xml");} catch (Exception e){addMessage("INIT ERROR There is not valid "+DirModel+"Rules.xml files in the application"); exit();}
  
   
   //try {ReadData(DirModel+"ExprByRegions-02-11-10.csv");} catch (Exception e){addMessage("INIT There is no Expression file");}
   
  
   // try{ ReadNanoString(DirModel+"TC-DATA.csv");} catch (Exception e){addMessage("INIT There is no NanoString file");}
  
  
  
     // for(int i=0;i<fontList.length;i++)   println(" " + i + " -> " +fontList[i]);
  
   //ExportRulesPDF("/Users/emmanuelfaure/cherche/Project/USA/GRNBoolModel/111121/Rules.pdf");
   
   //exit();
   
  }
  
  int toDo=0,toDoMenu=0;
  
  @Override
  public void draw() {
    
    background(cm.colorBackground);
    scale(Scale);
    eh.MouseCursor=ARROW;
    
      //MESSAGE AT THE BEGIN
     // if(nbGene==0) bigMessage("Add some genes...",200,100);
     // if(nbDomain==0) bigMessage("Add domains...",200,height-200);
  
  
    //Calcul the hidden part (by the others) of each Menu 
    for(int i=0;i<mm.nbMenu;i++){
      int NumMenu=mm.OrderMenu[i];
      mm.HideMouse[i][0]=mm.OrginMenuXY[NumMenu][0]; mm.HideMouse[i][1]=mm.OrginMenuXY[NumMenu][1]; //Origin
      int [] SizeMXY=mm.sizeMenu(NumMenu);
      mm.HideMouse[i][2]=SizeMXY[0]; mm.HideMouse[i][3]=SizeMXY[1]; //Size
    }
  
    for(int i=mm.nbMenu-1;i>=0;i--){
         mm.CurrentMenu=i;
         mm.displayMenu(mm.OrginMenuXY[mm.OrderMenu[i]][0],mm.OrginMenuXY[mm.OrderMenu[i]][1],mm.OrderMenu[i]);
    }
  
   
    if(toDo==0){
      if(ObjetDrag!=null && mousePressed)  ObjetDrag.draw(eh.mouseX()-20,eh.mouseY()-10,-1);
      noTint();
      if(eh.mouseOn(10,height-50,40,40))image(mm.trashFull,10,height-50,40,40);
      else image(mm.trashEmpty,10,height-50,40,40);
      
      cursor(eh.MouseCursor); //Mouse Cursor
    }
    
    
   
   if(mm.reOrderMenu>=0) mm.ReOrderMenu(mm.reOrderMenu);  
   
    //For showing help when survole of mouse
    if(mm.TimeExplanation<=0 && !mm.Explanation.equals("")){
       if(mm.StepExplanation>0){
          textFont(cm.myFont, 14);
          int nb=uf.countString(mm.Explanation,"\n"); if(nb==0) nb=1;
          int XX=50;int YY=50;
          mm.rect(XX-5,YY-15,textWidth(mm.Explanation)+10,nb*20,cm.colorExplanation,cm.colorExplanation);
          //noStroke(); fill(colorExplanation);  rect(XX-5,YY-15,textWidth(Explanation)+10,nb*20);
          //if(Explanation.indexOf("<BR>")<0){ fill(Textcolor); text(Explanation,XX,YY);   }  else{
          String []TExplanation=split(mm.Explanation,"\n");
          for(int i=0;i<TExplanation.length;i++){
            fill(cm.Textcolor,150);
            if(TExplanation[i].indexOf("<BR>")>=0){
               TExplanation[i]=uf.replaceString(TExplanation[i],"<BR>","");
               fill(cm.Textcolor,255);
            }
            text(TExplanation[i],XX,YY+20*i); 
          }//}
       }
       else{
           textFont(cm.myFont, 10);
           int nb=uf.countString(mm.Explanation,"\n"); if(nb==0) nb=1;
           mm.rect(eh.mouseX()-5+20,eh.mouseY()-10+20,textWidth(mm.Explanation)+10,nb*16,cm.colorExplanation,cm.colorBoxBackground);
          // stroke(colorBoxBackground); fill(colorExplanation);  rect(mouseX()-5+20,mouseY()-10+20,textWidth(Explanation)+10,nb*16);
           fill(cm.Textcolor); text(mm.Explanation,eh.mouseX()+20,eh.mouseY()+20); 
       }
       textFont(cm.myFont, 12);
    }
    
    //if(TimeExplanation<=-50) delay(500); //Just to have not CPU consumation during no activitie
    if(cm.colorMenu) cm.MenuColor(); //Draw the color Menu 
    if(smm.searchMenu) smm.MenuSearch(); //Draw the Search Menu
  
  
  //THINGS TODO
   switch(toDo){
  
     case 2 : {//Save a Snapshot
       toDo=0;
       uf.selectOutput("Choose a file", 9);
     }
     break;
     
     case 4 : // Restart the model until the current step
           if(lm.MyModel.step<lm.MyModel.ActualStep) lm.MyModel.Step();
     else toDo=0;
     
     break;
     
     case 5 : //Save a SnapShot for the model or Data of each Domains
        if(mm.CaptureNb>=0){
            loadPixels();
              PImage im=new PImage(width,height);
              for(int i=0;i<width*height;i++)  im.pixels[i]=pixels[i];
              im.updatePixels();
              PImage imcrop=im.get(0,0,mm.SizeMenuXY[mm.MenuCapture][0]+1,mm.SizeMenuXY[mm.MenuCapture][1]+1);
              imcrop.updatePixels();
              PImage img = createImage(mm.SizeMenuXY[mm.MenuCapture][0]+1,mm.SizeMenuXY[mm.MenuCapture][1]+1, ARGB);
              img.loadPixels();
              for (int i = 0; i < img.pixels.length; i++)  img.pixels[i] =imcrop.pixels[i]; 
              img.updatePixels();
  
              img.save(dataPath("")+"GRN"+mm.CaptureNb+".tif");
              mm.pdfCapture.image(imcrop,0,0); 
              PGraphicsPDF pdfg = (PGraphicsPDF) mm.pdfCapture;  // Get the renderer
              if(mm.CaptureNb<dm.Domains.size()-1)  pdfg.nextPage();  // Tell it to go to the next page
         }
         mm.CaptureNb ++;
         if(mm.CaptureNb<dm.nbDomain) { lm.MyModel.ActiveDomain=lm.MyModel.getDomain(mm.CaptureNb); }//Activate the Next  Domain
         else { //End Recording
             mm.pdfCapture.dispose();
             mm.pdfCapture.endDraw();
             //Delete Temporatry files
             for(int i=0;i<mm.CaptureNb;i++)uf.delete(dataPath("")+"GRN"+i+".tif");
             mm.CaptureNb=0; 
             mm.restoreMenu(); 
             toDo=0;
         }
         break;
       case 6 : { //OPEN AN XML FILE
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {  
           try { rr.ReadRules(myName); } catch (Exception e){mm.addMessage("There is not valid "+myName+" files in the application"); rr.deleteModel();}
         }
         toDo=0; 
         break;
       }
       case 7 : { //OPEN NANOSTRING
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {  
           try{ nmm.ReadNanoString(myName);} catch (Exception e){mm.addMessage("INIT There is no NanoString file");}
         }
         toDo=0; 
         break;         
       }
       case 8 : { //SAVE CURRENT MODEL AN XML FILE
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {  
           lm.lastModelLoaded=myName; sm.saveXML(myName);
         }
         toDo=0;
         break;
       }
       case 9 : { // FINISH SAVING A SNAPSHOT
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {  
           PImage im=new PImage(width,height);
           loadPixels();
           for(int i=0;i<width*height;i++)  im.pixels[i]=pixels[i];
           im.updatePixels();
           im.save(myName);
           mm.addMessage("Save "+myName + ".tif"); 
         }
         toDo=0;
         break; 
       }
       case 10 : { //EXPORT RULES AS A TXT FILE
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {
           sm.ExportRulesTXT(myName);
         }
         toDo=0;
       
         break; 
       }
       case 11 : { //EXPORT RULES AS A PDF FILE
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;  
         if (myName!=null && !myName.equals("")) {
           sm.ExportRulesPDF(myName);
         }
         toDo=0;
         break;
       }
       case 12 : { //EXPORT MODEL AS A CSV FILE
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {
           sm.saveCSV(myName);
         }
         toDo=0;
         break;
       }
       case 13 : { 
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {
           if(!uf.getFileExtension(myName).equals("pdf")) myName=myName+".pdf";
           mm.MenuCapture=mm.getNumMenu("model"); 
           mm.saveMenu();
           mm.ResizeMenuXY(mm.MenuCapture,0,20*gm.nbGene+30+dm.SizeDrawDomain);  //Resize Menu X
           mm.ResizeMenuXY(mm.MenuCapture,1,100+(20*(1+lm.MyModel.ActualStep)+30));  //Resize Menu Y
           mm.pdfCapture = createGraphics(mm.SizeMenuXY[mm.MenuCapture][0]+1,mm.SizeMenuXY[mm.MenuCapture][1]+1, PDF, myName);
           mm.pdfCapture.beginDraw();
           mm.ReOrderMenu(mm.MenuCapture); //Put the Model Menu in front 
           mm.OrginMenuXY[mm.MenuCapture][0]=1;mm.OrginMenuXY[mm.MenuCapture][1]=1; //Change the Coordinate to the top
           for(int i=0;i<mm.nbMenu;i++) mm.MenuActive[i]=-1;  mm.MenuActive[mm.MenuCapture]=1 ; //Hide and unhide the menu
           mm.CaptureNb=-1;
           toDo=5;
         } else {
           toDo=0;
         }
         break;
       }
       case 14 : { 
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {
           if(!uf.getFileExtension(myName).equals("pdf")) myName=myName+".pdf";
           mm.MenuCapture=mm.getNumMenu("data"); 
           mm.saveMenu();
           mm.ResizeMenuXY(mm.MenuCapture,0,20*gm.nbGene+30+dm.SizeDrawDomain);  //Resize Menu X
           mm.ResizeMenuXY(mm.MenuCapture,1,50+dm.SizeDrawDomain+21*rm.MaxTime);  //Resize Menu Y
           mm.pdfCapture = createGraphics(mm.SizeMenuXY[mm.MenuCapture][0]+1,mm.SizeMenuXY[mm.MenuCapture][1]+1, PDF, myName);
           mm.pdfCapture.beginDraw();
           mm.ReOrderMenu(mm.MenuCapture); //Put the Model Menu in front 
           mm.OrginMenuXY[mm.MenuCapture][0]=1;mm.OrginMenuXY[mm.MenuCapture][1]=1; //Change the Coordinate to the top
           for(int i=0;i<mm.nbMenu;i++)  mm.MenuActive[i]=-1 ;  mm.MenuActive[mm.MenuCapture]=1 ; //Hide and unhide the menu
           mm.CaptureNb=-1;
           toDo=5;
         } else {
           toDo=0;
         }
         break;
       }       
       case 15 : { 
         String myName = (uf.Gselection!=null) ? uf.Gselection.getAbsolutePath() : null;
         if (myName!=null && !myName.equals("")) {
           if(!uf.getFileExtension(myName).equals("pdf")) myName=myName+".pdf";
           mm.pdfCapture = createGraphics(mm.SizeMenuXY[mm.MenuCapture][0]+1,mm.SizeMenuXY[mm.MenuCapture][1]+1, PDF, myName);
           mm.pdfCapture.beginDraw();
           toDo=5;
           cmm.MenuCompare(1,1,mm.MenuCapture);
           toDo=0;
           PImage im=new PImage(width,height);
           loadPixels();
           for(int i=0;i<width*height;i++)  im.pixels[i]=pixels[i];
           im.updatePixels();
           PImage imcrop=im.get(0,0,mm.SizeMenuXY[mm.MenuCapture][0]+1,mm.SizeMenuXY[mm.MenuCapture][1]+1);
           mm.pdfCapture.image(imcrop,0,0); 
           mm.pdfCapture.dispose();
           mm.pdfCapture.endDraw(); 
         }
         toDo=0;
         break;
       }
     }
     
     switch(toDoMenu){
       ///////////////////////////////////////  FILE  ///////////////////////////////////////
       case 1:  //OPEN AN XML FILE
           uf.selectInput("Choose a xml file", 6); 
           toDoMenu=0;
       break;
       case 2: //SAVE CURRENT MODEL AN XML FILE
           if(!lm.lastModelLoaded.equals("")) {
             sm.saveXML(lm.lastModelLoaded);
           } else { //SAVE AS
             uf.selectOutput("Choose a xml file", 8);
           }
           toDoMenu=0;
       break;
       case 3:  //SAVE CURRENT MODEL AS A NEW XML FILE
          uf.selectOutput("Choose a xml file", 8);
          toDoMenu=0;
       break;
       case 4 :   //OPEN EXPRESSION DATA 
       // try {ReadConfig(new XML(this,DirData+"Config.xml")); } catch (Exception e){addMessage("There is no config file, open in standard");}
           toDoMenu=0;
       break;
       case 5 :  //OPEN NANOSTRING
           uf.selectInput("Choose a cvs file", 7);
           toDoMenu=0; 
       break;
  
      ///////////////////////////////////////  EXPORT  ///////////////////////////////////////
       case 10 :    //EXPORT RULES AS A TXT FILE
           uf.selectOutput("Choose a txt file", 10); 
           toDoMenu=0;
       break; 
       case 11 :    //EXPORT RULES AS A PDF FILE
           uf.selectOutput("Choose a pdf file", 11); 
           toDoMenu=0;
       break;
       case 12 : //EXPORT MODEL AS A CSV FILE
          uf.selectOutput("Choose a csv file", 12); 
          toDoMenu=0;
       break;
       case 13: //EXPORT MODEL AS A PDF FILE
          uf.selectOutput("Choose a pdf file", 13);  
          toDoMenu=0;
       break;
       case 14: //EXPORT DATA AS A PDF FILE
          uf.selectOutput("Choose a pdf file", 14);  
          toDoMenu=0;
       break;
       case 15 : //EXPORT COMPARE AS A PDF FILE
          mm.MenuCapture=mm.getNumMenu("compare"); 
          if(mm.MenuActive[mm.MenuCapture]!=1) eh.alert("Menu Compare should be active");
          else {
            uf.selectOutput("Choose a pdf file", 15);
          }
          toDoMenu=0;
       break;
        ///////////////////////////////////////  WINDOWS  ///////////////////////////////////////
       case 20 :  //OPEN ALL WINDOWS IN CASCADE
          for(int i=0;i<mm.nbMenu;i++) {  mm.OrginMenuXY[i][0]=mm.SizeMenu*i;mm.OrginMenuXY[i][1]=20*i; mm.OrderMenu[i]=mm.nbMenu-i-1; mm.active(i); }        
          toDoMenu=0;
       break;
        
        
     }
   //resizeScreen();
   mm.TimeExplanation--;
  }
  
  /*
  void resizeScreen(){
    if(width != frame.getWidth() || frame.getHeight() != height){
        addMessage(" From to " + width + " X " +  height);
        addMessage(" From to " + ScreenX + " X " +  ScreenY);
        addMessage(" Was to " + frame.getWidth()  + " X " +  frame.getHeight());
        width=frame.getWidth();
        height=frame.getHeight();
        ScreenX=width;ScreenY=height;
        setPreferredSize(new Dimension(width, height));
        initFonts(); //I need to reninit the fonts otherwise the fucked up
        addMessage(" Resize to " + width + " X " +  height);
    }
  }*/
  
  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // File selection handler. Thinking this MUST be in the applet?
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

  public void fileSelected(File selection) {
    toDo = uf.pendingToDo;
    uf.Gselection=selection;
  }
  
  
  String getFileCallbackFunctionName(int todo) {
    // Use the same function for all actions
    return ("fileSelected");
  }

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Pulled from About.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

 String osname=System.getProperty("os.name");

  void initAbout(){
    if(osname.indexOf("Mac")>=0){
        BufferedImage icon=null;
         try { 
           //println(" Read "+dataPath("")+"/aboutGRN.png");
           icon = ImageIO.read(new File(dataPath("")+"/aboutGRN.png"));
           new JavaMacDock(this, icon,width/2-250,height/2-250,500,500);
         } catch (IOException ex) {println(" Error in About" +ex);  }
        
    }
  }
 
  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Changed from Event.pde. Code is in the Event Handler, but these need to be in the applet
  // itself
  //
  /////////////////////////////////////////////////////////////////////////////////////////////
 
  @Override
  public void mouseReleased() {
    eh.mouseReleasedEH();
  }
 
  @Override
  public void mouseDragged() { 
    eh.mouseDraggedEH(); 
  }
  
  @Override
  public void mouseMoved() {
    eh.mouseMovedEH(); 
  }
  
  @Override
  public void mousePressed(){
    eh.mousePressedEH();  
  }

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Not sure but perhaps the applet needs to have this?
  //
  /////////////////////////////////////////////////////////////////////////////////////////////  
  
  //////////////////////// DROP EVENT //////////////////////////////////////////////////////////////////////// 
/*  void dropEvent(DropEvent theDropEvent) {
    if(theDropEvent.isFile()){
      File NameFile=theDropEvent.file();
      String Name=NameFile.getAbsoluteFile().toString();
      if(uf.getFileExtension(Name).equals("xml")) {
        try { rr.ReadRules(Name); } catch (Exception e){mm.addMessage("ERROR There is not valid "+Name+" files in the application"); exit();}
      }
      else mm.addMessage("ERROR Not a valid extenstion for " + Name);
      
    }
  }*/
  
  
  //Return true when it's a double click
  public boolean doubleClick(){return mouseEvent.getClickCount()==2;}
   
  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Changed from MenuBar.pde. Applet needs to have the function
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public boolean action(Event me, Object arg) {
  if (mbm.handleAppletAction(me, arg)) {
    return (true);
  }
  return super.action(me,arg);
}

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // EXIT OPS
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

  //THINGS TO DO ON EXIT
  
  @Override
  public void stop()
  {
    println(" stop");
    super.stop();
  } 
  
  @Override
  public void exit(){
     //saveConfig(DirData+"Config.xml"); 
     if(!lm.lastModelLoaded.equals("") && eh.confirm("Do you want to save you project?")){
        sm.saveXML(lm.lastModelLoaded);
     }
    // println(" exit");
    super.exit();
  } 
  
  @Override
  public void dispose(){
    // println("dispose");
    super.dispose();
  }
  

}

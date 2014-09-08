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
*/

import processing.pdf.*;
import processing.video.*;
import processing.opengl.*;
import java.nio.*;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent; 
import java.awt.Event;

Model []MyModels=null; //List of all versions
Model MyModel=null; //Current Model-> version Loaded

PImage Good, Wrong, Unknown,Ubiquitous,Redo,Reset,Lock,Unlock,Duplicate,colorWheel;

PImage logo;

int verbose=2;
String NameApplication="GeNeTool";

int ScreenX;int ScreenY; float Scale=1; int frameR=10;
String Version="2011-10-04-14-37-02";

String DirData="data/";//dataPath("");
void setup(){
   ScreenX=displayWidth;ScreenY=displayHeight-100;
  size(ScreenX,ScreenY); 
   
  initFonts();
  initMouse();
  initOperators();
  initMenu();
  
  frameRate(frameR); 
  
  
  
   //DO NOT WORK RESIZE WAIT FOR NEXT VERSION OF PROCESSING
  frame.setResizable(true);  
  textMode(MODEL);
  //size(ScreenX,ScreenY, OPENGL);
  
 

  ellipseMode(CENTER); 

  //Load Image
  trashEmpty=loadImage(DirData+"trash_empty.png");
  trashFull=loadImage(DirData+"trash_full.png");
 
 
  Good=loadImage(DirData+"Good.png");
  Wrong=loadImage(DirData+"Wrong.png");
  Unknown=loadImage(DirData+"unknown.png");
  Ubiquitous=loadImage(DirData+"Ubiquitous.png");
  Redo=loadImage(DirData+"Redo.png");  
  Reset=loadImage(DirData+"Reset.png");
  Duplicate=loadImage(DirData+"duplicate.png");
  Lock=loadImage(DirData+"lock.png");
  Unlock=loadImage(DirData+"unlock.png");
  colorWheel=loadImage(DirData+"WheelColor.png"); colorWheel.resize(SizeWheelColor,SizeWheelColor);
 
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

void draw(){
  
  background(colorBackground);
  scale(Scale);
  MouseCursor=ARROW;
  
    //MESSAGE AT THE BEGIN
   // if(nbGene==0) bigMessage("Add some genes...",200,100);
   // if(nbDomain==0) bigMessage("Add domains...",200,height-200);


  //Calcul the hidden part (by the others) of each Menu 
  for(int i=0;i<nbMenu;i++){
    int NumMenu=OrderMenu[i];
    HideMouse[i][0]=OrginMenuXY[NumMenu][0]; HideMouse[i][1]=OrginMenuXY[NumMenu][1]; //Origin
    int [] SizeMXY=sizeMenu(NumMenu);
    HideMouse[i][2]=SizeMXY[0]; HideMouse[i][3]=SizeMXY[1]; //Size
  }

  for(int i=nbMenu-1;i>=0;i--){
       CurrentMenu=i;
       displayMenu(OrginMenuXY[OrderMenu[i]][0],OrginMenuXY[OrderMenu[i]][1],OrderMenu[i]);
  }

 
  if(toDo==0){
    if(ObjetDrag!=null && mousePressed)  ObjetDrag.draw(mouseX()-20,mouseY()-10,-1);
    noTint();
    if(mouseOn(10,height-50,40,40))image(trashFull,10,height-50,40,40);
    else image(trashEmpty,10,height-50,40,40);
    
    cursor(MouseCursor); //Mouse Cursor
  }
  
  
 
 if(reOrderMenu>=0) ReOrderMenu(reOrderMenu);  
 
  //For showing help when survole of mouse
  if(TimeExplanation<=0 && !Explanation.equals("")){
     if(StepExplanation>0){
        textFont(myFont, 14);
        int nb=countString(Explanation,"\n"); if(nb==0) nb=1;
        int XX=50;int YY=50;
        rect(XX-5,YY-15,textWidth(Explanation)+10,nb*20,colorExplanation,colorExplanation);
        //noStroke(); fill(colorExplanation);  rect(XX-5,YY-15,textWidth(Explanation)+10,nb*20);
        //if(Explanation.indexOf("<BR>")<0){ fill(Textcolor); text(Explanation,XX,YY);   }  else{
        String []TExplanation=split(Explanation,"\n");
        for(int i=0;i<TExplanation.length;i++){
          fill(Textcolor,150);
          if(TExplanation[i].indexOf("<BR>")>=0){
             TExplanation[i]=replaceString(TExplanation[i],"<BR>","");
             fill(Textcolor,255);
          }
          text(TExplanation[i],XX,YY+20*i); 
        }//}
     }
     else{
         textFont(myFont, 10);
         int nb=countString(Explanation,"\n"); if(nb==0) nb=1;
         rect(mouseX()-5+20,mouseY()-10+20,textWidth(Explanation)+10,nb*16,colorExplanation,colorBoxBackground);
        // stroke(colorBoxBackground); fill(colorExplanation);  rect(mouseX()-5+20,mouseY()-10+20,textWidth(Explanation)+10,nb*16);
         fill(Textcolor); text(Explanation,mouseX()+20,mouseY()+20); 
     }
     textFont(myFont, 12);
  }
  
  //if(TimeExplanation<=-50) delay(500); //Just to have not CPU consumation during no activitie
  if(colorMenu) MenuColor(); //Draw the color Menu 
  if(searchMenu) MenuSearch(); //Draw the Search Menu


//THINGS TODO
 switch(toDo){

   case 2 : //Save a Snapshot
         selectOutput("Choose a file","SnapShot");  
       toDo=0;
   break;
   
   case 4 : // Restart the model until the current step
         if(MyModel.step<MyModel.ActualStep) MyModel.Step();
   else toDo=0;
   
   break;
   
   case 5 : //Save a SnapShot for the model or Data of each Domains
      if(CaptureNb>=0){
          loadPixels();
            PImage im=new PImage(width,height);
            for(int i=0;i<width*height;i++)  im.pixels[i]=pixels[i];
            im.updatePixels();
            PImage imcrop=im.get(0,0,SizeMenuXY[MenuCapture][0]+1,SizeMenuXY[MenuCapture][1]+1);
            imcrop.updatePixels();
            PImage img = createImage(SizeMenuXY[MenuCapture][0]+1,SizeMenuXY[MenuCapture][1]+1, ARGB);
            img.loadPixels();
            for (int i = 0; i < img.pixels.length; i++)  img.pixels[i] =imcrop.pixels[i]; 
            img.updatePixels();

            img.save(dataPath("")+"GRN"+CaptureNb+".tif");
            pdfCapture.image(imcrop,0,0); 
            PGraphicsPDF pdfg = (PGraphicsPDF) pdfCapture;  // Get the renderer
            if(CaptureNb<Domains.size()-1)  pdfg.nextPage();  // Tell it to go to the next page
       }
       CaptureNb ++;
       if(CaptureNb<nbDomain) { MyModel.ActiveDomain=MyModel.getDomain(CaptureNb); }//Activate the Next  Domain
       else { //End Recording
           pdfCapture.dispose();
           pdfCapture.endDraw();
           //Delete Temporatry files
           for(int i=0;i<CaptureNb;i++)delete(dataPath("")+"GRN"+i+".tif");
           CaptureNb=0; 
           restoreMenu(); 
           toDo=0;
       }
       break;
   }
   
   String Name="";
   switch(toDoMenu){
     ///////////////////////////////////////  FILE  ///////////////////////////////////////
     case 1:  //OPEN AN XML FILE
         selectInput("Choose a xml file","SelectAModelFile");
       toDoMenu=0;
     break;
     case 2: //SAVE CURRENT MODEL AN XML FILE
         if(!lastModelLoaded.equals(""))  saveXML(lastModelLoaded);
         else selectOutput("Choose a xml file","saveXMLFile"); //Save as
         toDoMenu=0;
     break;
     case 3:  //SAVE CURRENT MODEL AS A NEW XML FILE
           selectOutput("Choose a xml file","saveXMLFile");
           toDoMenu=0;
     break;
     case 4 :   //OPEN EXPRESSION DATA 
     // try {ReadConfig(new XML(this,DirData+"Config.xml")); } catch (Exception e){addMessage("There is no config file, open in standard");}
         toDoMenu=0;
     break;
     case 5 :  //OPEN NANOSTRING
         selectInput("Choose a cvs file","ReadNanoStringFile");
        toDoMenu=0; 
     break;

    ///////////////////////////////////////  EXPORT  ///////////////////////////////////////
    case 10 :    //EXPORT RULES AS A TXT FILE
         selectOutput("Choose a txt file","ExportRulesTXTFile"); 
         toDoMenu=0;
     break; 
     case 11 :    //EXPORT RULES AS A PDF FILE
         selectOutput("Choose a pdf file","ExportRulesPDFFile"); 
         toDoMenu=0;
     break;
     case 12 : //EXPORT MODEL AS A CSV FILE
           selectOutput("Choose a csv file","saveCSVFile"); 
           toDoMenu=0;
     break;
     case 13: //EXPORT MODEL AS A PDF FILE
            selectOutput("Choose a pdf file","ExportModelasPDF");  
            toDoMenu=0;
    break;
     case 14: //EXPORT DATA AS A PDF FILE
          selectOutput("Choose a pdf file","ExportDataasPDF");  
         toDoMenu=0;
        break;
     case 15 : //EXPORT COMPARE AS A PDF FILE
          MenuCapture=getNumMenu("compare"); 
          if(MenuActive[MenuCapture]!=1) alert("Menu Compare should be active");
          else selectOutput("Choose a pdf file","ExportCompareasPDF"); 
          toDoMenu=0;
          break;
      ///////////////////////////////////////  WINDOWS  ///////////////////////////////////////
      case 20 :  //OPEN ALL WINDOWS IN CASCADE
        for(int i=0;i<nbMenu;i++) {  OrginMenuXY[i][0]=SizeMenu*i;OrginMenuXY[i][1]=20*i; OrderMenu[i]=nbMenu-i-1; active(i); } 
        
       toDoMenu=0;
      break;
      
      
   }
 //resizeScreen();
 TimeExplanation--;
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

//THINGS TO DO ON EXIT
void stop()
{
  println(" stop");
  super.stop();
} 

void exit(){
   //saveConfig(DirData+"Config.xml"); 
   if(!lastModelLoaded.equals("") && confirm("Do you want to save you project?")){
      saveXML(lastModelLoaded);
   }
  // println(" exit");
  super.exit();
} 

void dispose(){
  // println("dispose");
  super.dispose();
}


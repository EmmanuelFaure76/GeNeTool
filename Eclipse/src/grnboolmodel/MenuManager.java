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


package grnboolmodel;

import java.util.ArrayList;

import processing.core.PGraphics;
import processing.core.PImage;

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Menu.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public class MenuManager {

  private GRNBoolModel p;
  
  MenuManager(GRNBoolModel p) {
    this.p = p;   
  } 

  //Display Transparency when button activate or not
  int boutonInactive=150;
  int boutonActive=255;
  
  //When we pass under a button without moving more than 2 secondes
  String Explanation="";
  int TimeExplanation;
  int StepExplanation=-1;
  ArrayList GenesInvolve; //When a mouse on a box, list of genes invovle in this logic 
  int []CoordExplanation=new int[2];
  
  int InactiveColor=100;
  int ActiveColor=255;
  
  PImage trashEmpty;
  PImage trashFull;
  
  
  int LigneGeneDefBoxX,LigneGeneDefBoxY; //Gene Box for Definition
  int LigneDomDefBoxX,LigneDomDefBoxY; //Domains Box Definition
  int LigneModelBoxX,LigneModelBoxY; //Model Box Definition
  
  int []CoordMenuDrag=new int[2]; //When we dragg a Menu
  int DragMod=-1; //0 From a List (Gene/Operator/Domain) , 1 From the Definition List, 2, From the Model Gene
  
  
  int SizeMenu=70; // Size of the menu when is ranged
  int ShiftMenu=5; //Shift between Menus
  int nbMenu=9; //Number of Menus
  int []MenuActive; //All the menus acivate , 0 close, 1 Open, -1 No possible
  int [] OrderMenu; //Order to print the menu
  
  int numLogicDrag; //Number of Logic where object was drag 
  int nbLogicDef=0; //Numbe of logic in definition
  int []LogicDefSize; //Size each Logic in Definition
  Logic LogicDefActive=null; //??
  int MenuDragActive; //When we click inside  a menu to dragg an obkect
  
  int [][] OrginMenuXY; //Coordinate of the Upper Left Corner of each Menu
  int [][] SizeMenuXY; //Size of the Menu
  int [][] MiniMenuSizeXY; //Minimum Size of the Menu
  int [] MenuReSizeXY; //When we click to Resize a Menu
  int MenuDrag; //When we click on a Menu to move it
  int DragPosition=-1; //Position When you drag an Element in the list of others elements
  
  int ShiftGenesY=0; //When roll on list genes;
  int ShiftDomainsY=0; //When rool on list Domains
  int ShiftGeneDomainsY=0; //When rool on gene list in domain definition
  
  int HighLithX,HighLithY; //When we are on a ligne/colone hight light this
  
  int [][] HideMouse=new int[nbMenu][4]; //To Hide the mouse on windows in order inverse that the appear 
  int CurrentMenu=-1; //The Menu display 
  
  Gene geneNano=null; //Gene Active to dispaply the Nano String plot
  float [] CoordinateLineNano=null; //When we are drawing a line on NanoString
   
  int []ExternWindow=null;
  
  int SizeWheelColor=200;
  
  
  
  LogicGene LogicDeroule=null;
  int[] LogicDerouleCoord=new int[2];
  int SizeMaxDeroule=0;
  
  int CaptureNb=0; //The Number of the Actual Capture
  int MenuCapture=0; //The number of the menu to Capture
  PGraphics pdfCapture; //The PDF while recording all the model
  int indCapture=0; //To Shift Indice of Capture
  PImage CaptureImage;
  
  
  
  int scrollMenu=-1; //When we use the elevator
  int oldScroll=0; //Y Value of Scroll 
  
  void initMenu(){  
    MenuDragActive=-1; MenuDrag=-1;
    MenuReSizeXY=new int[2]; MenuReSizeXY[0]=-1;MenuReSizeXY[1]=-1;
    OrderMenu=new int[nbMenu];  for(int i=0;i<nbMenu;i++)OrderMenu[i]=i;
    MenuActive=new int[nbMenu];  for(int i=0;i<nbMenu;i++)MenuActive[i]=-1; 
    MenuActive[0]=1;MenuActive[1]=1; MenuActive[8]=1; //Start Only with Domain and Genes Open
    MiniMenuSizeXY=new int[nbMenu][2];    
    OrginMenuXY=new int[nbMenu][2];  
    SizeMenuXY=new int[nbMenu][2]; 
    
    p.mbm.initMenuBar();
    bestMenuOrder();
  }
  
  //Save the actual Configuration
  int [][] SAVEOrginMenuXY; //Coordinate of the Upper Left Corner of each Menu
  int [][] SAVESizeMenuXY; //Size of the Menu
  int []SAVEOrderMenu;
  int []SAVEMenuActive;
  
  void saveMenu(){
    SAVEOrginMenuXY=new int[nbMenu][2];
    SAVESizeMenuXY=new int[nbMenu][2];
    SAVEOrderMenu=new int[nbMenu];
    SAVEMenuActive=new int[nbMenu];
    
     for(int i=0;i<nbMenu;i++){
         SAVEOrderMenu[i]=OrderMenu[i];
         SAVEMenuActive[i]=MenuActive[i];
         for(int j=0;j<2;j++){
           SAVEOrginMenuXY[i][j]=OrginMenuXY[i][j];
           SAVESizeMenuXY[i][j]=SizeMenuXY[i][j];
         }
     }
  }
  
  void restoreMenu(){
    for(int i=0;i<nbMenu;i++){
         OrderMenu[i]=SAVEOrderMenu[i];
         MenuActive[i]=SAVEMenuActive[i];
         for(int j=0;j<2;j++){
           OrginMenuXY[i][j]=SAVEOrginMenuXY[i][j];
           SizeMenuXY[i][j]=SAVESizeMenuXY[i][j];
         }
     }
    
  }
  int getNumMenu(String name){
    name=name.toLowerCase();
    if(name.equals("genes") || name.equals("gene")) return 0;
    if(name.equals("domain") || name.equals("domains")) return 1;
    if(name.equals("model")) return 2;
    if(name.equals("operator") || name.equals("operators"))return 3;
    if(name.equals("expression")) return 4;
    if(name.equals("data")) return 5;
    if(name.equals("nanostring")) return 6;
    if(name.equals("compare")) return 7;
    if(name.equals("tree")) return 8;
    return -1; 
  }
  boolean isActive(String name){ return isActive(getNumMenu(name));}
  boolean isActive(int numMenu) { return MenuActive[numMenu]>=0;}
  boolean isOpen(String name) { return isOpen(getNumMenu(name));}
  boolean isOpen(int numMenu) { return MenuActive[numMenu]==1;}
  boolean isClose(String name) { return isClose(getNumMenu(name));}
  boolean isClose(int numMenu) { return MenuActive[numMenu]==0;}
  void active(String name){active(getNumMenu(name),1);}
  void active(int numMenu){active(numMenu,1);}
  void desActive(String name){active(getNumMenu(name),-1);}
  void desActive(int numMenu){active(numMenu,-1);}
  void inActive(String name){active(getNumMenu(name),0);}
  void inActive(int numMenu){active(numMenu,0);}
  public void active(String name,int state){ active(getNumMenu(name),state); }
  void active(int numMenu,int state){ MenuActive[numMenu]=state;p.mbm.activeItem(numMenu,state); }
  
  //Reorder all the windows in the "best" configuration
  void bestMenuOrder(){
    OrginMenuXY[0][0]=0; OrginMenuXY[0][1]=0; SizeMenuXY[0][0]=p.width-100; SizeMenuXY[0][1]=380;if(MenuActive[0]>=0) active(0,0); //GENE
    OrginMenuXY[1][0]=0; OrginMenuXY[1][1]=385; SizeMenuXY[1][0]=400; SizeMenuXY[1][1]=360;if(MenuActive[1]>=0) active(1,0); //DOMAINS
    OrginMenuXY[2][0]=210; OrginMenuXY[2][1]=0; SizeMenuXY[2][0]=1075; SizeMenuXY[2][1]=150;if(MenuActive[2]>=0) active(2,1); //MODEL
    OrginMenuXY[3][0]=124; OrginMenuXY[3][1]=435; SizeMenuXY[3][0]=360; SizeMenuXY[3][1]=150;if(MenuActive[3]>=0) active(3,1);//OPERATORS
    OrginMenuXY[4][0]=124; OrginMenuXY[4][1]=385; SizeMenuXY[4][0]=750; SizeMenuXY[4][1]=450;if(MenuActive[4]>=0) active(4,0); //EXPRESSION
    OrginMenuXY[5][0]=124; OrginMenuXY[5][1]=70; SizeMenuXY[5][0]=1090; SizeMenuXY[5][1]=100;if(MenuActive[5]>=0) active(5,0); //DATA
    OrginMenuXY[6][0]=124; OrginMenuXY[6][1]=110; SizeMenuXY[6][0]=1170; SizeMenuXY[6][1]=180;if(MenuActive[6]>=0) active(6,0); //NANOSTRING
    OrginMenuXY[7][0]=124; OrginMenuXY[7][1]=150; SizeMenuXY[7][0]=110; SizeMenuXY[7][1]=100;if(MenuActive[7]>=0) active(7,0); //COMPARE
    OrginMenuXY[8][0]=124; OrginMenuXY[8][1]=410; SizeMenuXY[8][0]=250; SizeMenuXY[8][1]=450;if(MenuActive[8]>=0) active(8,0); //TREE
    
    for(int i=0;i<nbMenu;i++){  MiniMenuSizeXY[i][0]=300;  MiniMenuSizeXY[i][1]=150; }
    MiniMenuSizeXY[4][0]=320; MiniMenuSizeXY[4][0]=450; MiniMenuSizeXY[8][1]=50;
  }
  
  //Display all the menu in order of apparence
  void displayMenu(int X,int Y,int num){
     if(MenuActive[num]>=0) 
     switch(num){
          case 0 :  p.gmm.MenuGenes(X,Y,num); break;
          case 1 :  p.dmm.MenuDomains(X,Y,num);  break;
          case 2 :  p.mmm.MenuModel(X,Y,num);  break;
          case 3:   MenuOperators(X,Y,num);  break;
          case 4:   p.tmm.MenuExpression(X,Y,num);  break;
          case 5:   p.tmm.MenuData(X,Y,num);  break;
          case 6:   p.nmm.MenuNanoString(X,Y,num);  break;
          case 7:   p.cmm.MenuCompare(X,Y,num);  break;
          case 8:   p.tmm.MenuTree(X,Y,num);  break;
       }
  }
  //Pass the new menu in front of
  int reOrderMenu=-1;
  void ReOrderMenu(int numMenu){
    int [] NewOrderMenu=new int[nbMenu]; NewOrderMenu[0]=numMenu;
    int id=1;
    for(int i=0;i<nbMenu;i++)
        if(OrderMenu[i]!=numMenu) 
          NewOrderMenu[id++]=OrderMenu[i];
    OrderMenu=NewOrderMenu;
    reOrderMenu=-1;
  }
  //Pass the menu in the back !!
  void InvReOrderMenu(int numMenu){
    int [] NewOrderMenu=new int[nbMenu]; NewOrderMenu[nbMenu-1]=numMenu;
    int id=0;
    for(int i=0;i<nbMenu;i++)
        if(OrderMenu[i]!=numMenu) 
          NewOrderMenu[i-id]=OrderMenu[i];
        else id=1;
    OrderMenu=NewOrderMenu;
    reOrderMenu=-1;
  }
  
  //TO Resize Menu 
  void ResizeMenuXY(int MenuA,int XY,int v){
      //if(v<400) v=400;
      MiniMenuSizeXY[MenuA][XY]=v;
      SizeMenuXY[MenuA][XY]=p.max(SizeMenuXY[MenuA][XY],MiniMenuSizeXY[MenuA][XY]); 
  }
  //Return the Curent size of the Menu
  int [] sizeMenu(int NumMenu){
    int [] SizeMXY=new int[2];SizeMXY[0]=SizeMenu;  SizeMXY[1]=SizeMenuXY[NumMenu][1];
     switch(NumMenu){
      case 0 :  SizeMXY[0]=p.gm.SizeDrawGene+20; break; //Genes
      case 1:   SizeMXY[0]=p.dm.SizeDrawDomain+20; break; //Domain
      //case 2:   SizeMXY[0]=SizeDrawDomain+10; break; //Model
    }
    
    if(MenuActive[NumMenu]==1)   SizeMXY[0]=SizeMenuXY[NumMenu][0]; 
    else  if(NumMenu>=2) SizeMXY[1]=20;   
    return SizeMXY;
  }
  
  
  
  //Global Onglet Functions // To open or close a onglet
  void MenuOnglet(int ligneX,int ligneY,int NumMenu,String Label){
  
    int [] SizeMXY=sizeMenu(NumMenu);
    
    boolean ActiveColorMenu=false;
    
    if(MenuActive[NumMenu]==1)  {
          ActiveColorMenu=true;
           //To modify the size of some windows
        if(p.eh.mouseOn(ligneX+SizeMXY[0]-5,ligneY+SizeMXY[1]-5,10,10)) { p.eh.MouseCursor=p.CROSS;   if( p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; MenuReSizeXY[0]=NumMenu;  MenuReSizeXY[1]=NumMenu;reOrderMenu=NumMenu;}}
        if(p.eh.mouseOn(ligneX+SizeMXY[0]-5,ligneY,10,SizeMXY[1])) { p.eh.MouseCursor=p.CROSS;  if( p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; MenuReSizeXY[0]=NumMenu; reOrderMenu=NumMenu; }}
        if(p.eh.mouseOn(ligneX,ligneY+SizeMXY[1]-5,SizeMXY[0],10)) { p.eh.MouseCursor=p.CROSS;   if( p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; MenuReSizeXY[1]=NumMenu;  reOrderMenu=NumMenu;}}
    }
    
    
     if(p.eh.mouseOn(ligneX,ligneY,SizeMXY[0],SizeMXY[1]) && p.mousePressed && p.eh.AcceptEvent) {reOrderMenu=NumMenu; } //Click on all the Menu, pass it on the Front 
     if(SizeMXY[0]!=SizeMenu)  rect(ligneX,ligneY,SizeMXY[0],SizeMXY[1],p.cm.colorBackground,p.cm.colorOngletBackground); //Draw a rect size of the menu
     
    if(p.eh.mouseOn(ligneX,ligneY,SizeMenu,20)) { ActiveColorMenu=true; p.eh.MouseCursor=p.HAND;}
    if(ActiveColorMenu) p.fill(p.cm.colorOnglet); else   p.fill(p.cm.colorOnglet,100); p.noStroke();  buttonBound(ligneX,ligneY,SizeMenu,20); // rect(ligneX,ligneY,SizeMenu,20);  //Draw a box
    
    p.fill(p.cm.Textcolor); p.text(Label,ligneX+SizeMenu/2-p.textWidth(Label)/2,ligneY+14);   //Draw the label
    if(p.eh.mousePressOn(ligneX,ligneY,SizeMenu,20)) { MenuDrag=NumMenu; reOrderMenu=NumMenu; CoordMenuDrag[0]=ligneX; CoordMenuDrag[1]=ligneY; } //Drag a Menu
    
            
  
  }
  
  
  ///////////////////////////////////////////////  CONSOLE MESSAGE
  public void addMessage(String mess){
    p.println(mess);
  }
  
  ///////////////////////////////////////////////  OPERATORS 
  void MenuOperators(int ligneX,int ligneY, int NumMenu){
    MenuOnglet(ligneX,ligneY,NumMenu,"Operators");
  
     if(MenuActive[NumMenu]==1) {
       ligneY+=30; ligneX+=10;
       for(int b=0;b<2;b++){ //Bracket Menu
           p.uf.drawString(p.pm.Bracket[b].getString(),ligneX+b*25,ligneY,10,p.cm.colorBoxGene);
            if(p.eh.mousePressOn(ligneX+b*25,ligneY,10,20)) {
                p.ObjetDrag=p.pm.Bracket[b]; 
                MenuDragActive=NumMenu;
                DragMod=0;
          }
       }
       ligneY-=30; ligneX-=10;
       
       MenuOperator(ligneX+70,ligneY,1,0);
       MenuOperator(ligneX+130,ligneY,2,0);
       MenuOperator(ligneX+190,ligneY,3,0);
       MenuOperator(ligneX+250,ligneY,4,0); 
       MenuOperator(ligneX+250,ligneY+50,5,0); 
       MenuOperator(ligneX+310,ligneY,6,0); 
     }
    
  }
  
  
  //Draw a specific operator
  public void MenuOperator(int ligneX,int ligneY,int numOp,int DragMenu){
    String textop="";
    switch(numOp){
      case 1: textop="Unary"; break;
      case 2: textop="Binary"; break;
      case 3: textop="Spatial"; break;
      case 4: textop="Temporal"; break;
      case 5: textop=""; break;
      case 6: textop="Hpf"; break;
    }
   
   p.fill(p.cm.Textcolor,250);  p.text(textop,ligneX-p.textWidth(textop)/2+20,ligneY+14);
    ligneY+=30;  
    int ix=0;
    for(int i=0;i<p.pm.NbLogicOperator;i++)
       if(p.pm.LogicOperator[i].comp==numOp) { 
           p.pm.LogicOperator[i].draw(ligneX,ligneY+ix*25,p.cm.colorOperators);
           if(p.eh.mousePressOn(ligneX,ligneY+ix*25,40,20)) {
                p.ObjetDrag=new Objet(p, p.pm.LogicOperator[i]); 
                MenuDragActive=DragMenu;
                DragMod=0;
          }
           ix++;
       }
  }
  
  
  
  
  
  
  
  
  //Draw a minus to hide elements
  boolean mouseHide(int x,int y,int l,int w,String explanation){ if(mouseOnHide(x,y,l,w,explanation) && p.mousePressed && p.eh.AcceptEvent){ p.eh.AcceptEvent=false;  return true;  } return false;   }
  boolean mouseOnHide(int x,int y,int l,int w,String explanation){
    boolean actif=false;
    if(p.eh.mouseOn(x,y,l,w)){ if(TimeExplanation<0)Explanation=explanation; actif=true; }
    int c=p.cm.colorButton;
    if(!actif)  c=p.color(p.cm.colorButton,boutonInactive);
    p.noStroke();p.fill(c); p.rect(x,y,l,w);
    return actif;
  }
  
  
  //Draw a Plus to unhide elemetns
  boolean mouseUnHide(int x,int y,int l,int w,String explanation){ if(mouseOnUnHide(x,y,l,w,explanation) && p.mousePressed && p.eh.AcceptEvent){ p.eh.AcceptEvent=false;  return true;  }return false;   }
  boolean mouseOnUnHide(int x,int y,int l,int w,String explanation){
    boolean actif=false;
    if(p.eh.mouseOn(x,y,l,l)){  if(TimeExplanation<0)Explanation=explanation; actif=true; }
    int c=p.cm.colorButton;
    if(!actif)  c=p.color(p.cm.colorButton,boutonInactive);
    float h=l/2-w/2;
    p.noStroke();p.fill(c); p.rect(x,y+h,l,w); p.rect(x+h,y,w,l);
    return actif;
  }
  
  
  
  boolean ImageClicked(int x,int y,int l,int w,PImage im,String explanation){
    if(ImageClick(x,y,l,w,im,explanation) && p.mousePressed && p.eh.AcceptEvent){ p.eh.AcceptEvent=false;  return true;  }
    return false;
  }
  boolean ImageClick(int x,int y,int l,int w,PImage im,String explanation){
    boolean actif=false;
    
    if(p.eh.mouseOn(x,y,l,w)){
      if(TimeExplanation<0)Explanation=explanation;
      actif=true;
    }
    if(actif)  p.tint(p.cm.colorButton); else p.tint(p.cm.colorButton,boutonInactive);
    p.image(im,x,y,l,w); 
    p.noTint();
    return actif;
  }
  
  
  boolean boutonClicked(int x,int y,int l,int w,int c,String label,String explanation,boolean ActifValue){
    if(boutonClick(x,y,l,w,c,p.color(p.cm.Textcolor),label,explanation,ActifValue) && p.mousePressed && p.eh.AcceptEvent){
      p.eh.AcceptEvent=false;
      return true;
    }
    return false;
  }
  
  boolean boutonClicked(int x,int y,int l,int w,int c,int colorTexte,String label,String explanation,boolean ActifValue){
    if(boutonClick(x,y,l,w,c,colorTexte,label,explanation,ActifValue) && p.mousePressed && p.eh.AcceptEvent){
      p.eh.AcceptEvent=false;
      return true;
    }
    return false;
  }
  
  boolean boutonClick(int x,int y,int l,int w,int c,String label,String explanation,boolean ActifValue){  
    return boutonClick(x,y,l,w,c,p.color(p.cm.Textcolor),label,explanation,ActifValue);
  }
  boolean boutonClick(int x,int y,int l,int w,int c,int colorTexte,String label,String explanation, boolean ActifValue){ 
    boolean actif=false;
    int valueBouton=boutonInactive;
    if(ActifValue)   valueBouton=boutonActive;
    if(p.eh.mouseOn(x,y,l,w)){
      if(TimeExplanation<0)Explanation=explanation;
      if(ActifValue)  valueBouton=boutonInactive;
      else valueBouton=boutonActive;
      actif=true;
    }
    p.noStroke();
    p.fill(c,valueBouton); p.rect(x, y, l, w);
    p.fill(colorTexte,valueBouton);  p.text(label,x+3,y+12);
    return actif;
  
  }
  
  
  
  boolean boutonCoche(int x,int y,int h, int c,String explTrue,String explFalse, boolean ActifValue,PImage imOk,PImage imNO){
    boolean actif=false;
    if(p.eh.mouseOn(x,y,h,h)){
      if(TimeExplanation<0) {if(ActifValue) Explanation=explTrue; else Explanation=explFalse;}
      actif=true;
    }
    int hh=imOk.height*h/imOk.width;
    if(ActifValue){if(imOk!=null){ p.tint(c); p.image(imOk,x,y-(hh-h)/2,h,hh); }else {p.stroke(c); p.noFill(); p.rect(x, y, h, h);} }
    else {if(imNO!=null) {p.tint(c);p.image(imNO,x,y-(hh-h)/2,h,hh);} else {p.stroke(c); p.noFill();p. rect(x, y, h, h);}}
    
    return actif;
  }
  
  int [] OnMenuDefile=new int[4];
  int [] BlockMenuDefile=null;
  int MenuDefile(int x,int y, int l ,int w, int c, String label, String []  ListSubMenu){
    int actif=-1;
    int oldL=l;
    for(int i=0;i<ListSubMenu.length;i++) if(p.textWidth(ListSubMenu[i])+4>l)l=p.round(p.textWidth(ListSubMenu[i]))+4;
    if(p.eh.AcceptEvent && BlockMenuDefile==null && p.eh.mouseOnMenu() && p.eh.mouseX()<x+l && p.eh.mouseX()>x &&p.eh.mouseY()<y+w && p.eh.mouseY()>y){
        BlockMenuDefile=new int[4]; BlockMenuDefile[0]=x; BlockMenuDefile[1]=y; BlockMenuDefile[2]=l; BlockMenuDefile[3]=w*(1+ListSubMenu.length);
        OnMenuDefile[0]=x;OnMenuDefile[1]=y;OnMenuDefile[2]=l;OnMenuDefile[3]=w;
        actif=0;
     }
    p.noStroke();  p.fill(c,boutonInactive); p.rect(x, y, oldL, w);
    p.fill(p.cm.Textcolor);  p.text(label,x+3,y+12);
    
     
   
    if(OnMenuDefile[0]==x && OnMenuDefile[1]==y && OnMenuDefile[2]==l && OnMenuDefile[3]==w){ //Previous on the menu
        if(p.eh.mouseX()<x+l && p.eh.mouseX()>x &&p.eh.mouseY()<y+w*(1+ListSubMenu.length) && p.eh.mouseY()>y){
         for(int i=0;i<ListSubMenu.length;i++){
           if(p.eh.mouseX()<x+l && p.eh.mouseX()>x &&p.eh.mouseY()<y+w*i+w+w && p.eh.mouseY()>y+w*i+w){
             p.stroke(p.cm.Textcolor);
             if(p.mousePressed && p.eh.AcceptEvent){ p.eh.AcceptEvent=false; for(int j=0;j<4;j++) OnMenuDefile[j]=0;BlockMenuDefile=null;  actif=i+1;}
           } else  p.noStroke(); 
           p.fill(c);  p.rect(x, y+w*i+w, l, w);
           p.fill(p.cm.Textcolor); p.noStroke();  p.text(ListSubMenu[i],x+3,y+w*i+w+14);
         }
         p.stroke(p.cm.Textcolor,boutonInactive); p.noFill(); p.rect(x, y+w, l, w*ListSubMenu.length);
       }
       else  {BlockMenuDefile=null;  for(int j=0;j<4;j++) OnMenuDefile[j]=0;} //Reset the mouve over
    }
    
    return actif;
    
    
    
    
  }
  
  void buttonBound(float x,float y,float w,float l){
    //Now draw a complete back ground
    p.noStroke();
    p.rect(x,y,w,l);
  }
  
  void rectGradHori(float x,float y,float w,float l,int in,int out){
    p.beginShape();  
        p.fill(in); p.vertex(x,y);  p.vertex(x+w, y);
        p.fill(out);  p.vertex(x+w,y+l); p.vertex(x,y+l);  
    p.endShape(p.CLOSE);
    
  }
  void rectGradVerti(float x,float y,float w,float l,int in,int out){
    p.beginShape();
        p.fill(in);  p.vertex(x,y);   p.vertex(x,y+l);  
        p.fill(out); p.vertex(x+w,y+l);  p.vertex(x+w, y);
    p.endShape(p.CLOSE);
    
  }
  //0, upRight, 1, up Left,2,down,Left,3,down,Right
  void arcGrad(int mod,float x,float y,float r,int in, int out){
   switch(mod){
     case 0:  p.beginShape();
        p.fill(in);  p.vertex(x,y);   p.fill(out); p.vertex(x+r,y);  
         p.vertex(x+r,y-r);   p.vertex(x, y-r);
      p.endShape(p.CLOSE);
      break;
     case 1:  p.beginShape();
        p.fill(in);  p.vertex(x,y);   p.fill(out); p.vertex(x-r,y);  
         p.vertex(x-r,y-r);   p.vertex(x, y-r);
      p.endShape(p.CLOSE);
      break;
     case 2:
     p.beginShape();
        p.fill(in);  p.vertex(x,y);   p.fill(out); p.vertex(x-r,y);  
         p.vertex(x-r,y+r);   p.vertex(x, y+r);
       p.endShape(p.CLOSE);
     break;
     case 3:
      p.beginShape();
        p.fill(in);  p.vertex(x,y);   p.fill(out); p.vertex(x+r,y);  
         p.vertex(x+r,y+r);   p.vertex(x, y+r);
      p.endShape(p.CLOSE);
     break;
   }
   
       /* beginShape();
          fill(in);
          curveVertex(x,y-r);
          curveVertex(x,y-r);
          curveVertex(x+r/2,y-r);
          curveVertex(x+r,y-r/2);
           fill(out);
          curveVertex(x+r, y);
          curveVertex(x+r, y);
      endShape();*/
  
  }
  
  //Create a recteangle 
  void rect(float x,float y,float w,float l,int colorFill,int colorStroke){
    p.fill(colorFill);  if(colorStroke==0) p.noStroke(); else p.stroke(colorStroke);
    p.rect(x,y,w,l);
  }
  
  void drawGradiedntDisc(float x,float y, float radiusX, float radiusY, int innerCol, int outerCol) {
    p.noStroke();
    p.beginShape(p.TRIANGLE_STRIP);
    for(float theta=0; theta<p.TWO_PI; theta+=p.TWO_PI/36) {
      p.fill(innerCol);
      p.vertex(x,y);
      p.fill(outerCol);
      p.vertex(x+radiusX*p.cos(theta),y+radiusY*p.sin(theta));
    }
    p.endShape();
  } 
  
  int h_slice(int x,int y,int Min,int Max,int hcursor,int cBack,int cBox,String label,String explanation)
  {
    p.fill(cBack);
    int d=Max-Min;
    p.stroke(255-p.red(p.cm.colorBackground),255-p.green(p.cm.colorBackground),255-p.blue(p.cm.colorBackground));
    p.rect(x-1, y-1, d+12, 12);
    if (p.eh.mouseX()<x+d+10 &&p.eh.mouseX()>x &&p.eh.mouseY()<y+13&&p.eh.mouseY()>y-1){
      if(TimeExplanation<0)Explanation=explanation;
      p.fill(cBox);
      if(p.mousePressed == true){
        hcursor=p.max(p.min((p.eh.mouseX()-x-5),d),0)+Min;
      }
    }
    else  p.fill(cBox,150);
    
    p.rect(x+hcursor-Min, y, 10, 10);
    p.fill(p.cm.Textcolor);
  
    p.text(label,x+d+14,y+10);
    return hcursor;//-Min;
  }
  int v_slice(int x,int y,int Min,int Max,int hcursor,int cBack,int cBox,String label,String explanation)
  {
    p.fill(cBack);
    int d=Max-Min;
    p.stroke(255-p.red(p.cm.colorBackground),255-p.green(p.cm.colorBackground),255-p.blue(p.cm.colorBackground));
    p.rect(x-1, y-1, 12, d+12);
    if (p.eh.mouseX()<x+d+10 &&p.eh.mouseX()>x &&p.eh.mouseY()<y+13&&p.eh.mouseY()>y-1){
          if(TimeExplanation<0)Explanation=explanation;
          p.fill(cBox);
          if(p.mousePressed == true)  hcursor=p.max(p.min((p.eh.mouseY()-y-5),d),0)+Min;
    }
    else  p.fill(cBox,150);
    
    p.rect(x, y+hcursor-Min, 10, 10);
    p.fill(p.cm.Textcolor);
  
    p.text(label,x,y+d+14);
    return hcursor;//-Min;
  }


  
}

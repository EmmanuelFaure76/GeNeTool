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

import processing.core.PFont;

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MenuColor.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

public class ColorMenu {

  private GRNBoolModel p;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  COLORS
  
  int ChangeColor; 
  int colorBackground; //Global Color Background 
  
  int colorOngletBackground; //Color Background Onglet
  int colorBoxBackground; //Color Background Definition Box
  int colorButton;//Colorize the button
  int colorOnglet; //Color Menu Onglet
  
  int Textcolor; //Color of the texte
  int TextNocolor; //Color of the texte when it's on No Gene background
  
  int colorUnknownGene; //a unknown Gene
  
  int colorOperators; //Display Color of Operator
  
  int colorDomain; 
  
  int colorExplanation;
  //Gene  
  int colorBoxGene; //Color of the box behind the gene name
  int colorBoxNoGene; //Color of the box behind the gene name
  
  int colorGeneOn; // Color of the gene when is activate
  int colorGeneOff;//Color of the gene when is repress
  int colorMaternal; //Color of Maternal things
  int colorNoData;  //Color of No Data
  int colorUbiquitous;  //Color of genes which are ubiquitous
 
  ColorMenu(GRNBoolModel p) {
    this.p = p;
    initColors();
  }  

  
  //Draw a light color Menu on the rest
  boolean offMenu=false; //Desactivate the mouse for all the others things
  boolean colorMenu=false;
  boolean ActiveMenuFont=false;
  
  
  private void initColors() {
  
    ChangeColor=0; 
    colorBackground=p.color(255,255,255); //Global Color Background 
    
    colorOngletBackground=p.color(120,120,120); //Color Background Onglet
    colorBoxBackground=p.color(120,120,120); //Color Background Definition Box
    colorButton=p.color(64,196,246);//Colorize the button
    colorOnglet=p.color(64,196,246); //Color Menu Onglet
    
    Textcolor=p.color(0,0,0); //Color of the texte
    TextNocolor=p.color(255,255,255); //Color of the texte when it's on No Gene background
    
    colorUnknownGene=p.color(255,255,255); //a unknown Gene
    
    colorOperators=p.color(120,120,120); //Display Color of Operator
    
    colorDomain=p.color(150,150,150,100); 
    
    colorExplanation=p.color(253,253,150,240);
    //Gene  
    colorBoxGene=p.color(150,150,150,100); //Color of the box behind the gene name
    colorBoxNoGene=p.color(100,100,100); //Color of the box behind the gene name
    
    colorGeneOn=p.color(64,196,246); // Color of the gene when is activate
    colorGeneOff=p.color(200,200,200);//Color of the gene when is repress
    colorMaternal=p.color(0,150,0); //Color of Maternal things
    colorNoData=p.color(255,255,255);  //Color of No Data
    colorUbiquitous=p.color(200,0,0);  //Color of genes which are ubiquitous
  
  }
  
  
  public void changeMenuColor(){
    colorMenu=!colorMenu;
    p.mbm.mWCo.setState(colorMenu);
    if(colorMenu && p.smm.searchMenu) p.smm.changeMenuSearch(); //Remove search Menu if it was here
    offMenu=colorMenu;
  }
  
  public void MenuColor(){
    p.fill(colorBackground,175); p.noStroke(); p.rect(0,0,p.width,p.height); //Draw a white cover on everything
    
    
    int sizeHeightColor=500; int sizeWidthColor=500;
    int ligneX=p.round(p.width-sizeWidthColor)/2; int ligneY=p.round(p.height-sizeHeightColor)/2;
    
    p.mm.rect(ligneX-5,ligneY,sizeWidthColor,sizeHeightColor,colorBackground,colorOngletBackground); 
    if(p.mousePressed && p.eh.AcceptEvent && !mouseOnMenuOff(ligneX-5,ligneY,sizeWidthColor,sizeHeightColor)) {   p.eh.AcceptEvent=false;  changeMenuColor();  } //If mouse pressOut out the color Menu come back
    
    
     //COLOR
     p.noStroke();
     int colorchoose=p.color(0,0,0);
     for(int Comp=1;Comp<=MenuColorList.length;Comp++){
            colorchoose=colorMenu(Comp);
            if(mouseOnMenuOff(ligneX,ligneY+Comp*25,100,20)) p.stroke(Textcolor,255); else p.noStroke();
            if(mousePressOnMenuOff(ligneX,ligneY+Comp*25,100,20)) {ChangeColor=Comp;p.smm.textSearch=new ArrayList();ActiveMenuFont=false;}
            p.fill(colorchoose);  p.rect(ligneX,ligneY+Comp*25,100,20);
            p.textFont(myFont,12); p.fill(Textcolor);   p.text(MenuColorList[Comp-1],ligneX+45-p.textWidth(MenuColorList[Comp-1])/2,ligneY+Comp*25+14);
            
     }
      if(ChangeColor>=1){
         colorchoose=colorMenu(ChangeColor);
         int  Newcolorchoose=colorchoose;
         int NewValue=0;
         NewValue=p.mm.h_slice(ligneX+150,ligneY+70,0,255,p.round(p.red(colorchoose)),p.color(p.red(colorchoose),0,0,p.alpha(colorchoose)),p.color(colorBackground),"red","choose red");
         if(NewValue!=p.round(p.red(colorchoose))) Newcolorchoose=p.color(NewValue,p.green(Newcolorchoose),p.blue(Newcolorchoose),p.alpha(colorchoose));
         NewValue=p.mm.h_slice(ligneX+150,ligneY+100,0,255,p.round(p.green(colorchoose)),p.color(0,p.green(colorchoose),0,p.alpha(colorchoose)),p.color(colorBackground),"green","choose green");
         if(NewValue!=p.round(p.green(colorchoose))) Newcolorchoose=p.color(p.red(Newcolorchoose),NewValue,p.blue(Newcolorchoose),p.alpha(colorchoose));
         NewValue=p.mm.h_slice(ligneX+150,ligneY+130,0,255,p.round(p.blue(colorchoose)),p.color(0,0,p.blue(colorchoose),p.alpha(colorchoose)),p.color(colorBackground),"blue","choose blue");
         if(NewValue!=p.round(p.blue(colorchoose))) Newcolorchoose=p.color(p.red(Newcolorchoose),p.green(Newcolorchoose),NewValue,p.alpha(colorchoose));
         NewValue=p.mm.h_slice(ligneX+150,ligneY+160,0,255,p.round(p.alpha(colorchoose)),colorchoose,p.color(colorBackground),"alpha","choose alpha");
         if(NewValue!=p.round(p.alpha(colorchoose))) Newcolorchoose=p.color(p.red(Newcolorchoose),p.green(Newcolorchoose),p.blue(Newcolorchoose),NewValue);
         
         p.image(p.colorWheel,ligneX+150,ligneY+180,p.mm.SizeWheelColor,p.mm.SizeWheelColor);
         if(mousePressOnMenuOff(ligneX+150,ligneY+180,p.mm.SizeWheelColor,p.mm.SizeWheelColor)){
           Newcolorchoose=p.colorWheel.pixels[p.round(p.eh.mouseX()-(ligneX+150)+(p.eh.mouseY()-(ligneY+180))*p.mm.SizeWheelColor)];
         }
         if(Newcolorchoose!=colorchoose)  assignColorMenu(ChangeColor,Newcolorchoose);
      }
      int tligneY=ligneY+(MenuColorList.length+1)*25;
      //FONT
       
       if(mouseOnMenuOff(ligneX,tligneY,100,20)) p.textFont(myFont,14); else p.textFont(myFont,12); 
       p.text((String)allFonts.get(font),ligneX+10,tligneY+15);
       if(mousePressOnMenuOff(ligneX,tligneY,100,20)){
           ChangeColor=0;p.smm.textSearch=new ArrayList(); //To delete the other
           ActiveMenuFont=true;
       }
       if(ActiveMenuFont){
           p.noFill();p.stroke(Textcolor);
           for(int i=0;i<allFonts.size();i++){
               p.textFont((PFont)allmyFonts.get(i),12);
               p.text((String)allFonts.get(i),ligneX+150,ligneY+15+i*20); 
               if(mouseOnMenuOff(ligneX+140,ligneY+1+i*20,100,20)){
                   p.rect(ligneX+140,ligneY+1+i*20,100,20);
                   if(mousePressOnMenuOff(ligneX+140,ligneY+1+i*20,100,20)){
                       font=i;
                      myFont = (PFont)allmyFonts.get(font);
                   }
                }
            }
       }
       p.textFont(myFont,12);
       tligneY+=25;
       
       
  
  
  }
  
  
  public boolean mouseOnMenuOff(float x,float y,float w,float h){
    if (p.eh.mouseX()<=x+w && p.eh.mouseX()>=x &&p.eh.mouseY()<=y+h && p.eh.mouseY()>=y) return true;
    return false;
  
  }
  
  public boolean mousePressOnMenuOff(float x,float y,float w,float h){
    if(p.mousePressed && p.eh.AcceptEvent && mouseOnMenuOff(x,y,w,h)) {
      p.eh.AcceptEvent=false; 
      return true;
    }
    return false;
  
  }
  
  
  

  
  int ExpressionColor(int c) {
    int cc=p.color(255,255,255);
    switch(c) {
    case -1 :   cc=p.color(colorNoData);   break; //Nothing 
    case 0  :   cc=p.color(colorNoData);   break; //No data;
    case 1  :   cc=p.color(colorGeneOff);  break; //No expression;
    case 2  :   cc=p.color(p.red(colorGeneOn),p.green(colorGeneOn),p.blue(colorGeneOn), p.alpha(colorGeneOn)/2);  break; //Weak expression;
    case 3  :   cc=p.color(colorGeneOn);  break; //Expression;
    case 4  :   cc=p.color(colorMaternal);  break; //Maternal;
    }
    return cc;
  }
  
  
  String [] MenuColorList = {"background","tab","tab background","box background","button","text","manual","operators","domain","box gene","box no gene","gene on","gene off","maternal","no data","text no gene","ubiquitous"};
     
  void assignColorMenu(int Comp,int Newcolorchoose){
     switch(Comp){
             case 1: colorBackground=Newcolorchoose;break;
             case 2: colorOnglet=Newcolorchoose;break;
             case 3: colorOngletBackground=Newcolorchoose;break;
             case 4: colorBoxBackground=Newcolorchoose;break;
             case 5: colorButton=Newcolorchoose;break;
             case 6: Textcolor=Newcolorchoose;break;
             case 7: colorUnknownGene=Newcolorchoose;break;
             case 8: colorOperators=Newcolorchoose;break;
             case 9: colorDomain=Newcolorchoose;break;
             case 10: colorBoxGene=Newcolorchoose;break;
             case 11: colorBoxNoGene=Newcolorchoose;break;
             case 12: colorGeneOn=Newcolorchoose;break;
             case 13: colorGeneOff=Newcolorchoose;break;
             case 14: colorMaternal=Newcolorchoose;break;
             case 15: colorNoData=Newcolorchoose;break;
             case 16: TextNocolor=Newcolorchoose;break;
             case 17: colorUbiquitous=Newcolorchoose;break;
             
             
         }
  }
  int colorMenu(int Comp){
    int colorchoose=p.color(0,0,0);
        switch(Comp){
              case 1: colorchoose=colorBackground;break;
              case 2: colorchoose=colorOnglet;break;
              case 3: colorchoose=colorOngletBackground;break;
              case 4: colorchoose=colorBoxBackground;break;
              case 5: colorchoose=colorButton;break;
              case 6: colorchoose=Textcolor;break;
              case 7: colorchoose=colorUnknownGene;break;
              case 8: colorchoose=colorOperators;break;
              case 9: colorchoose=colorDomain;break;
              case 10: colorchoose=colorBoxGene;break;
              case 11: colorchoose=colorBoxNoGene;break;
              case 12: colorchoose=colorGeneOn;break;
              case 13: colorchoose=colorGeneOff;break; 
              case 14: colorchoose=colorMaternal; break;
              case 15: colorchoose=colorNoData; break;
              case 16: colorchoose=TextNocolor; break;
              case 17: colorchoose=colorUbiquitous; break;
            }
       return colorchoose;
  }
  
  int colorLine(int i){
    while(i>=64)i-=64;
    int col=p.color(0,0,0);
    switch(i){
    case 0: col=p.color(0,0,0);break;
  case 1: col=p.color(0,128,0);break;
  case 2: col=p.color(255,0,0);break;
  case 3: col=p.color(0,191,191);break;
  case 4: col=p.color(191,0,191);break;
  case 5: col=p.color(191,191,0);break;
  case 6: col=p.color(64,64,64);break;
  case 7: col=p.color(0,0,0);break;
  case 8: col=p.color(0,128,0);break;
  case 9: col=p.color(255,0,0);break;
  case 10: col=p.color(0,191,191);break;
  case 11: col=p.color(191,0,191);break;
  case 12: col=p.color(191,191,0);break;
  case 13: col=p.color(64,64,64);break;
  case 14: col=p.color(0,0,0);break;
  case 15: col=p.color(0,128,0);break;
  case 16: col=p.color(255,0,0);break;
  case 17: col=p.color(0,191,191);break;
  case 18: col=p.color(191,0,191);break;
  case 19: col=p.color(191,191,0);break;
  case 20: col=p.color(64,64,64);break;
  case 21: col=p.color(0,0,0);break;
  case 22: col=p.color(0,128,0);break;
  case 23: col=p.color(255,0,0);break;
  case 24: col=p.color(0,191,191);break;
  case 25: col=p.color(191,0,191);break;
  case 26: col=p.color(191,191,0);break;
  case 27: col=p.color(64,64,64);break;
  case 28: col=p.color(0,0,0);break;
  case 29: col=p.color(0,128,0);break;
  case 30: col=p.color(255,0,0);break;
  case 31: col=p.color(0,191,191);break;
  case 32: col=p.color(191,0,191);break;
  case 33: col=p.color(191,191,0);break;
  case 34: col=p.color(64,64,64);break;
  case 35: col=p.color(0,0,0);break;
  case 36: col=p.color(0,128,0);break;
  case 37: col=p.color(255,0,0);break;
  case 38: col=p.color(0,191,191);break;
  case 39: col=p.color(191,0,191);break;
  case 40: col=p.color(191,191,0);break;
  case 41: col=p.color(64,64,64);break;
  case 42: col=p.color(0,0,0);break;
  case 43: col=p.color(0,128,0);break;
  case 44: col=p.color(255,0,0);break;
  case 45: col=p.color(0,191,191);break;
  case 46: col=p.color(191,0,191);break;
  case 47: col=p.color(191,191,0);break;
  case 48: col=p.color(64,64,64);break;
  case 49: col=p.color(0,0,0);break;
  case 50: col=p.color(0,128,0);break;
  case 51: col=p.color(255,0,0);break;
  case 52: col=p.color(0,191,191);break;
  case 53: col=p.color(191,0,191);break;
  case 54: col=p.color(191,191,0);break;
  case 55: col=p.color(64,64,64);break;
  case 56: col=p.color(0,0,0);break;
  case 57: col=p.color(0,128,0);break;
  case 58: col=p.color(255,0,0);break;
  case 59: col=p.color(0,191,191);break;
  case 60: col=p.color(191,0,191);break;
  case 61: col=p.color(191,191,0);break;
  case 62: col=p.color(64,64,64);break;
  case 63: col=p.color(0,0,0);break;
  default : col=p.color(0,0,0);break;
  }
  
  return col;
  }
  
  
  
  
  
  //Initialise Fonts
  int font;
  PFont myFont;
  ArrayList allFonts;
  ArrayList allmyFonts;
  
  
  public void initFonts(){
     allFonts=new ArrayList();
    
    allFonts.add("AndaleMono");
  allFonts.add("AppleCasual");
  allFonts.add("ArialMT");
  allFonts.add("Baghdad");
  allFonts.add("Calibri");
  allFonts.add("Century");
  allFonts.add("Chalkboard");
  allFonts.add("Cochin");
  allFonts.add("ComicSansMS");
  allFonts.add("Constantia");
  allFonts.add("Courier");
  allFonts.add("Didot-Bold");
  allFonts.add("Gabriola");
  allFonts.add("GillSans");
  allFonts.add("Helvetica");
  allFonts.add("Impact");
  allFonts.add("Kokonor");
  allFonts.add("LucidaSans");
  allFonts.add("Marlett");
  allFonts.add("Rockwell");
  allFonts.add("SansSerif");
  allFonts.add("Skia-Regular");
  allFonts.add("Tahoma");
  allFonts.add("Times-Roman");
  allFonts.add("Verdana");
  
   allmyFonts=new ArrayList();
   for(int i=0;i<allFonts.size();i++)  {
        String ftname=(String)allFonts.get(i);
        allmyFonts.add(p.createFont(ftname,34));
   }
   
    font=20;
    myFont = (PFont)allmyFonts.get(font);
    p.textFont(myFont, 12);
  }
  
    
    
    
}

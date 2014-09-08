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



///////////////////////////////////////////////  LIST  GENE 
void MenuGenes(int ligneX,int ligneY,int NumMenu){
 
  MenuOnglet(ligneX,ligneY,NumMenu,"Genes");
  
  ligneX+=10;
  ligneY+=30;
 

  textFont(myFont, 12); 
  for(int i=0;i<Genes.size();i++){
    Gene g=(Gene)Genes.get(i);
    int coordY=ligneY+i*25+ShiftGenesY;
    if(coordY>=ligneY && coordY<ligneY+SizeMenuXY[0][1]-50){
      g.draw(ligneX+5,coordY,false,SizeDrawGene,true,true); 
      if(mousePressOn(ligneX+5,coordY,SizeDrawGene,20)) {
        if(doubleClick()){
           String Name=ask("Give a new name",g.Name); 
           if(Name!=null) g.Name=Name;
        }
        else{ //Drag the gene
          ObjetDrag=new Objet(g); 
          DragMod=0;
          MenuDragActive=0;
        }
      }
    }
  }
  
 //Scroll Bar
 int sizeBar=SizeMenuXY[NumMenu][1]-40;
  float ratio=Genes.size()*25/(float)sizeBar;
  if(ratio>1){
     float sizeBarr=sizeBar/ratio;
     int barV=100;
     float yl=ligneY+ShiftGenesY*(sizeBar-sizeBarr)/(sizeBar-Genes.size()*25);
     if(mouseOn(ligneX-8,yl, 10,sizeBarr)){
         barV=250;
         if(mousePressed && AcceptEvent){   AcceptEvent=false; scrollMenu=NumMenu;oldScroll=ShiftGenesY; }
     }
     if(scrollMenu==NumMenu) barV=250;
     fill(colorOnglet,barV);  rect(ligneX-8,yl, 10,sizeBarr); //Draw 
  }

  if(MenuActive[NumMenu]==1)   MenuDefinitionGene(ligneX,ligneY-25);


  
}




///////////////////////////////////////////////   GENE  DEFINITION
public void MenuDefinitionGene(int ligneX,int ligneY){
   
  if(mouseUnHide(ligneX+70,ligneY-2,15,5,"Add a new gene"))  {
    String Name=ask("Give a name to this new gene "); 
    if(Name!=null){
      Gene g=getGene(Name);
      if(g==null) {
        g=new Gene(Name); //Create a new Gene
        addGene(g);
        Genes=sortGene(Genes);
      } 
      else alert("The gene "+ Name + " already exist");
    }
  }

  
  ligneX+=250;
  
  //Draw the Definition Box
  LigneGeneDefBoxX=ligneX;  LigneGeneDefBoxY=ligneY;
 // fill(Textcolor);  text("Definition",ligneX-textWidth("Definition")-10,ligneY+14);
  fill(colorBoxBackground,50);    stroke(colorBoxBackground);  rect(ligneX,ligneY,SizeDrawGene,20);
  

  //Gene Definition if active
  if(GeneDef!=null){
    //Draw the Gene Name
    GeneDef.draw(ligneX,ligneY,false,SizeDrawGene,true,false); //boolean rot,int siz, boolean cent,boolean activ
    if(boutonCoche(ligneX-100,ligneY-2,20, colorButton,""+GeneDef.Name+" is a gene" ,""+GeneDef.Name+" is not a gene"  ,GeneDef.isGene,Good,Wrong) && mousePressed && AcceptEvent){AcceptEvent=false; GeneDef.isGene=!GeneDef.isGene;}
    if(boutonCoche(ligneX-75,ligneY,15, colorButton,""+GeneDef.Name+" is an unknown gene" ,""+GeneDef.Name+" is a known gene"  ,GeneDef.isUnknown,Unknown,null) && mousePressed && AcceptEvent){AcceptEvent=false; GeneDef.isUnknown=!GeneDef.isUnknown;}
    if(boutonCoche(ligneX-50,ligneY,15, colorButton,""+GeneDef.Name+" is ubiquitous" ,""+GeneDef.Name+" is not ubiquitous"  ,GeneDef.isUbiquitous,Ubiquitous,null) && mousePressed && AcceptEvent){AcceptEvent=false; GeneDef.isUbiquitous=!GeneDef.isUbiquitous;}
    
    //No Expression Data
    if(GeneDef.Expression==null){
          fill(colorBoxBackground,50);    stroke(colorBoxBackground);  rect(ligneX+SizeDrawGene+50,ligneY,SizeDrawGene,20);
          text("No Expression",ligneX+SizeDrawGene+50+(SizeDrawGene-textWidth("No Expression"))/2,ligneY+14);
    }
    
    ligneY+=30;
     for(int l=0;l<GeneDef.nbLogic;l++){
         ArrayList Objets=GeneDef.getObjets(l);
         MetaLogic logi=GeneDef.getMetaLogic(Objets);
         ObjetsDefinition(ligneX,ligneY,logi,Objets,l);
        ligneY+=35;
      }
     
     if(LogicDeroule!=null) { //A gene Logic is deployad
         rect(LogicDerouleCoord[0],LogicDerouleCoord[1]+28,SizeMaxDeroule+30,LogicDeroule.g.nbLogic*30+4,color(colorBackground),color(colorBoxBackground));
         ExternWindow=new int[4];ExternWindow[0]=LogicDerouleCoord[0];ExternWindow[1]=LogicDerouleCoord[1]+28;ExternWindow[2]=SizeMaxDeroule+30;ExternWindow[3]=LogicDeroule.g.nbLogic*30+4;
         boolean cancelLogic=false;
         for(int l=0;l<LogicDeroule.g.nbLogic;l++){
             ArrayList Objets=LogicDeroule.g.getObjets(l);
             MetaLogic logi=LogicDeroule.g.getMetaLogic(Objets);
             String Name=logi.getName();
             color c=colorGeneOn; if(LogicDeroule.v) c=colorGeneOff;
             if(Name.equals("")) Name=LogicDeroule.g.Name;
             float minSizeBox=textWidth(Name); SizeMaxDeroule=max(round(minSizeBox),SizeMaxDeroule);
             fill(c,50);  stroke(c);   rect(LogicDerouleCoord[0]+10,LogicDerouleCoord[1]+(l+1)*30+2,minSizeBox+10,26); //Display the square arround the name 
             fill(Textcolor,255);      text(Name,LogicDerouleCoord[0]+15,LogicDerouleCoord[1]+(l+1)*30+17); //Display the name of the logic
            if(mouseX()<LogicDerouleCoord[0]+10+minSizeBox+10 && mouseX()>LogicDerouleCoord[0]+10 &&mouseY()<LogicDerouleCoord[1]+(l+1)*30+2+26 && mouseY()>LogicDerouleCoord[1]+(l+1)*30+2  && mousePressed && AcceptEvent){AcceptEvent=false;   LogicDeroule.logicName=logi.getName();cancelLogic=true;  }
          } 
         // if(cancelLogic) { LogicDeroule=null;ExternWindow=null;}
    }

   if(mouseUnHide(ligneX-33,ligneY+3,15,5,"Add a new definition"))  {
       // String Name=ask("Give a name to the logic "); 
       // if(Name!=null){
          if(GeneDef.nbLogic==0) GeneDef.addLogic("","","1","0",false,true); else GeneDef.addLogic("","","1","0",false,false);
          nbLogicDef=GeneDef.nbLogic; LogicDefSize=new int[nbLogicDef];
       // }
      }
    
  }

}

//Draw the menu of a Definition Genes
void ObjetsDefinition(int ligneX,int ligneY,MetaLogic logi,ArrayList Objets,int numLogic){

   fill(Textcolor,150);  text("if",ligneX-textWidth("if")-5,ligneY+14);
   
   int sizeX=0;  //Calcul the size of the logic
   if(Objets!=null)  
       for(int i=0;i<Objets.size();i++)  
           sizeX+=((Objet)Objets.get(i)).size(); 
   
   if(sizeX==0) sizeX=50; //MinimumSize
   if(Objets!=null && mouseDraggOn(ligneX,ligneY,sizeX+50,20)){  //If We drag something from the list to the definition
       if(DragMod==0) sizeX+=ObjetDrag.size();   //From All the List
       if(DragMod==1 && MenuDragActive==1) sizeX+=ObjetDrag.size();   //From The Domain Input Definition
       if(DragMod==2 && numLogicDrag!=numLogic)  sizeX+=ObjetDrag.size(); //From The Logic 
       
   } 
     
   LogicDefSize[numLogic]=sizeX; //The Size of the Definition box of this logic
   String Name=logi.getName();
   float minSizeBox=textWidth(Name);
   
   int BckActi=50; //Background color of the defnition
   if(mouseOn(ligneX-30-minSizeBox,ligneY-5,minSizeBox+10,30)) BckActi=150;
   if(mousePressOn(ligneX-30-minSizeBox,ligneY-5,minSizeBox+10,30)){ //Change the name of the logic or Dragg it
         if(doubleClick()){
             String NewName=ask("Give a name to the logic "+Name); 
             if(NewName!=null)logi.setName(NewName);
         }
         else{
             ObjetDrag=new Objet(logi);
             DragMod=2; MenuDragActive=0; numLogicDrag=numLogic;    
         }
   }
   fill(colorBoxBackground,BckActi);  stroke(colorBoxBackground);   rect(ligneX-30-minSizeBox,ligneY-5,minSizeBox+10,30); //Display the square arround the name 
   fill(Textcolor,255);      text(Name,ligneX-textWidth(Name)-25,ligneY+14); //Display the name of the logic
   
   
   //Blue Module
   int leftX=round(ligneX-textWidth(Name))-50;
   if(boutonCoche(leftX,ligneY-3,10, colorButton,""+Name+" is an unknown" ,""+Name+" is known"  ,logi.isBlue,Unknown,null) && mousePressed && AcceptEvent){AcceptEvent=false; logi.isBlue=!logi.isBlue;}
   
  
   //Default logic ?
   if(GeneDef.nbLogic>1){
     if(logi.dft){  fill(Textcolor,100);text("default",leftX-25,ligneY+22);}
     else{  
       noFill(); stroke(Textcolor,100);rect(leftX,ligneY+12,10,10);
       if(mousePressOn(leftX,ligneY+12,10,10)){ 
        logi.dft=true;
        for(int ll=0;ll<GeneDef.nbLogic;ll++) //Switch the other logic to false
             if(numLogic!=ll){
                 MetaLogic logis=GeneDef.getMetaLogic(GeneDef.getObjets(ll));
                 logis.dft=false;
             }
         }
     }
   }
   
   fill(colorBoxBackground,50);   stroke(colorBoxBackground);   rect(ligneX,ligneY-5,sizeX+4,30);  //Draw a box for the definition of the logic
  
  int ix=0; int Position=0;
  if(Objets!=null) {
  for(int i=0;i<Objets.size();i++)  {
      Objet obj=(Objet)Objets.get(i);
       int ObjetSize=obj.size();
       if(mouseDraggOn(ligneX+ix+5,ligneY,ObjetSize,20))   //Shift the object if we are dragginf somthing on it
             if((numLogicDrag==numLogic  && DragMod!=2) 
                 || numLogicDrag==-1
                 || (numLogicDrag!=numLogic  && DragMod==2 )) {   DragPosition=Position; ix+=ObjetDrag.size();  }
                 
       if(mousePressOn(ligneX+ix+5,ligneY,ObjetSize,20))  { //When we click on it to drag somewere
           ObjetDrag=obj;
           DragMod=1; MenuDragActive=0; numLogicDrag=numLogic;
       }
       if(ObjetDrag==obj)  ix-=ObjetSize;
       else { obj.draw(ligneX+ix+5,ligneY,-1);Position++;}
       ix+=ObjetSize;
   }
   //In case the Object is drag after all the objetcs
    if(ObjetDrag!=null && mouseDraggOn(ligneX+ix+5,ligneY,ObjetDrag.size(),20))  
             if((numLogicDrag==numLogic  && DragMod!=2) 
                 || numLogicDrag==-1
                 ||  (numLogicDrag!=numLogic  && DragMod==2 ))   DragPosition=Objets.size(); //Set the Last position of Drag in case it is a the last !
  }
  
   //Resize Menu X
   ResizeMenuXY(0,0,ix+330);
   
     
   //Plot The 1 Else 0
   if(mousePressOn(ligneX+sizeX+5+28,ligneY,10,20)) logi.invThen();
   color c=colorGeneOff; if(parseBool(logi.getThen())) c=colorGeneOn;
   fill(c,50);   noStroke();   rect(ligneX+sizeX+5+28,ligneY,10,20);
   
   if(mousePressOn(ligneX+sizeX+5+68,ligneY,10,20)) logi.invElse();
   c=colorGeneOff; if(parseBool(logi.getElse())) c=colorGeneOn;
   fill(c,50);   noStroke();   rect(ligneX+sizeX+71,ligneY,10,20);
   fill(Textcolor,150);    text("then " +logi.getThen() + " else " +  logi.getElse(),ligneX+sizeX+5,ligneY+14); //Draw then else

   //Icon when Logic is valid
   if(logi.valid)  image(Good,ligneX+sizeX+50+textWidth("then 0 else 1"),ligneY);
   else image(Wrong,ligneX+sizeX+50+textWidth("then 0 else 1"),ligneY);
}



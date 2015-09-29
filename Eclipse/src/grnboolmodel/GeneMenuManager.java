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

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    // MenuGenes.pde
    //
    /////////////////////////////////////////////////////////////////////////////////////////////


public class GeneMenuManager {

  private GRNBoolModel p;
  
  GeneMenuManager(GRNBoolModel p) {
    this.p = p;   
  }
  
  ///////////////////////////////////////////////  LIST  GENE 
  void MenuGenes(int ligneX,int ligneY,int NumMenu){
   
    p.mm.MenuOnglet(ligneX,ligneY,NumMenu,"Genes");
    
    ligneX+=10;
    ligneY+=30;
   
  
    p.textFont(p.cm.myFont, 12); 
    for(int i=0;i<p.gm.Genes.size();i++){
      Gene g=(Gene)p.gm.Genes.get(i);
      int coordY=ligneY+i*25+p.mm.ShiftGenesY;
      if(coordY>=ligneY && coordY<ligneY+p.mm.SizeMenuXY[0][1]-50){
        g.draw(ligneX+5,coordY,false,p.gm.SizeDrawGene,true,true); 
        if(p.eh.mousePressOn(ligneX+5,coordY,p.gm.SizeDrawGene,20)) {
          if(p.doubleClick()){
            this.renameGeneAction(g);
          }
          else{ //Drag the gene
            p.ObjetDrag=new Objet(p, g); 
            p.mm.DragMod=0;
            p.mm.MenuDragActive=0;
          }
        }
      }
    }
    
   //Scroll Bar
   int sizeBar=p.mm.SizeMenuXY[NumMenu][1]-40;
    float ratio=p.gm.Genes.size()*25/(float)sizeBar;
    if(ratio>1){
       float sizeBarr=sizeBar/ratio;
       int barV=100;
       float yl=ligneY+p.mm.ShiftGenesY*(sizeBar-sizeBarr)/(sizeBar-p.gm.Genes.size()*25);
       if(p.eh.mouseOn(ligneX-8,yl, 10,sizeBarr)){
           barV=250;
           if(p.mousePressed && p.eh.AcceptEvent){   p.eh.AcceptEvent=false; p.mm.scrollMenu=NumMenu;p.mm.oldScroll=p.mm.ShiftGenesY; }
       }
       if(p.mm.scrollMenu==NumMenu) barV=250;
       p.fill(p.cm.colorOnglet,barV);  p.rect(ligneX-8,yl, 10,sizeBarr); //Draw 
    }
  
    if(p.mm.MenuActive[NumMenu]==1)   MenuDefinitionGene(ligneX,ligneY-25);
  }

  private void renameGeneAction(Gene gene) {
    String newName = p.eh.ask("Give a new name", gene.Name);

    if (newName == null) {
      return;
    }

    if (newName.equals(gene.Name)) {
      p.mm.addMessage("Same name given for renaming gene \'" + gene.Name + "\'");
      return;
    }

    // Trim and truncate whitespace. This name will be used for the gene
    // if it is not in use already.
    newName = p.gm.trimGeneName(newName);

    String duplicate = p.gm.getDuplicateGeneName(newName);

    if (duplicate != null) {
      p.eh.alert("\'" + newName + "\' is already in use by gene \'" + duplicate + "\'");
      return;
    }

    p.mm.addMessage("Renaming gene \'" + gene.Name + "\' to \'" + newName + "\'");
    gene.Name = newName;
  }

  ///////////////////////////////////////////////   GENE  DEFINITION
  public void MenuDefinitionGene(int ligneX,int ligneY){
     
    if(p.mm.mouseUnHide(ligneX+70,ligneY-2,15,5,"Add a new gene"))  {
        String name = p.eh.ask("Give a name to this new gene ");

        if (name == null) {
          return;
        }

        // Trim and truncate whitespace. This name will be used for the gene
        // if it is not in use already.
        name = p.gm.trimGeneName(name);

        String duplicate = p.gm.getDuplicateGeneName(name);

        if (duplicate != null) {
            p.eh.alert("The gene \'" + name + "\' already exist as gene \'" + duplicate + "\'");
            return;
        }

        Gene g = new Gene(p, name); //Create a new Gene
        p.gm.addGene(g);
        p.gm.Genes = p.gm.sortGene(p.gm.Genes);
    }

    ligneX+=250;
    
    //Draw the Definition Box
    p.mm.LigneGeneDefBoxX=ligneX;  p.mm.LigneGeneDefBoxY=ligneY;
   // fill(Textcolor);  text("Definition",ligneX-textWidth("Definition")-10,ligneY+14);
    p.fill(p.cm.colorBoxBackground,50);    p.stroke(p.cm.colorBoxBackground);  p.rect(ligneX,ligneY,p.gm.SizeDrawGene,20);
    
  
    //Gene Definition if active
    if(p.eh.GeneDef!=null){
      //Draw the Gene Name
      p.eh.GeneDef.draw(ligneX,ligneY,false,p.gm.SizeDrawGene,true,false); //boolean rot,int siz, boolean cent,boolean activ
      if(p.mm.boutonCoche(ligneX-100,ligneY-2,20, p.cm.colorButton,""+p.eh.GeneDef.Name+" is a gene" ,""+p.eh.GeneDef.Name+" is not a gene"  ,p.eh.GeneDef.isGene,p.Good,p.Wrong) && p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; p.eh.GeneDef.isGene=!p.eh.GeneDef.isGene;}
      if(p.mm.boutonCoche(ligneX-75,ligneY,15, p.cm.colorButton,""+p.eh.GeneDef.Name+" is an unknown gene" ,""+p.eh.GeneDef.Name+" is a known gene"  ,p.eh.GeneDef.isUnknown,p.Unknown,null) && p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; p.eh.GeneDef.isUnknown=!p.eh.GeneDef.isUnknown;}
      if(p.mm.boutonCoche(ligneX-50,ligneY,15, p.cm.colorButton,""+p.eh.GeneDef.Name+" is ubiquitous" ,""+p.eh.GeneDef.Name+" is not ubiquitous"  ,p.eh.GeneDef.isUbiquitous,p.Ubiquitous,null) && p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; p.eh.GeneDef.isUbiquitous=!p.eh.GeneDef.isUbiquitous;}
      
      //No Expression Data
      if(p.eh.GeneDef.Expression==null){
            p.fill(p.cm.colorBoxBackground,50);    p.stroke(p.cm.colorBoxBackground);  p.rect(ligneX+p.gm.SizeDrawGene+50,ligneY,p.gm.SizeDrawGene,20);
            p.text("No Expression",ligneX+p.gm.SizeDrawGene+50+(p.gm.SizeDrawGene-p.textWidth("No Expression"))/2,ligneY+14);
      }
      
      ligneY+=30;
       for(int l=0;l<p.eh.GeneDef.nbLogic;l++){
           ArrayList Objets=p.eh.GeneDef.getObjets(l);
           MetaLogic logi=p.eh.GeneDef.getMetaLogic(Objets);
           ObjetsDefinition(ligneX,ligneY,logi,Objets,l);
          ligneY+=35;
        }
       
       if(p.mm.LogicDeroule!=null) { //A gene Logic is deployad
           p.mm.rect(p.mm.LogicDerouleCoord[0],p.mm.LogicDerouleCoord[1]+28,p.mm.SizeMaxDeroule+30,p.mm.LogicDeroule.g.nbLogic*30+4,p.color(p.cm.colorBackground),p.color(p.cm.colorBoxBackground));
           p.mm.ExternWindow=new int[4];p.mm.ExternWindow[0]=p.mm.LogicDerouleCoord[0];p.mm.ExternWindow[1]=p.mm.LogicDerouleCoord[1]+28;p.mm.ExternWindow[2]=p.mm.SizeMaxDeroule+30;p.mm.ExternWindow[3]=p.mm.LogicDeroule.g.nbLogic*30+4;
           boolean cancelLogic=false;
           for(int l=0;l<p.mm.LogicDeroule.g.nbLogic;l++){
               ArrayList Objets=p.mm.LogicDeroule.g.getObjets(l);
               MetaLogic logi=p.mm.LogicDeroule.g.getMetaLogic(Objets);
               String Name=logi.getName();
               int c=p.cm.colorGeneOn; if(p.mm.LogicDeroule.v) c=p.cm.colorGeneOff;
               if(Name.equals("")) Name=p.mm.LogicDeroule.g.Name;
               float minSizeBox=p.textWidth(Name); p.mm.SizeMaxDeroule=p.max(p.round(minSizeBox),p.mm.SizeMaxDeroule);
               p.fill(c,50);  p.stroke(c);   p.rect(p.mm.LogicDerouleCoord[0]+10,p.mm.LogicDerouleCoord[1]+(l+1)*30+2,minSizeBox+10,26); //Display the square arround the name 
               p.fill(p.cm.Textcolor,255);      p.text(Name,p.mm.LogicDerouleCoord[0]+15,p.mm.LogicDerouleCoord[1]+(l+1)*30+17); //Display the name of the logic
              if(p.eh.mouseX()<p.mm.LogicDerouleCoord[0]+10+minSizeBox+10 && p.eh.mouseX()>p.mm.LogicDerouleCoord[0]+10 &&p.eh.mouseY()<p.mm.LogicDerouleCoord[1]+(l+1)*30+2+26 && p.eh.mouseY()>p.mm.LogicDerouleCoord[1]+(l+1)*30+2  && p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false;   p.mm.LogicDeroule.logicName=logi.getName();cancelLogic=true;  }
            } 
           // if(cancelLogic) { LogicDeroule=null;ExternWindow=null;}
      }
  
     if(p.mm.mouseUnHide(ligneX-33,ligneY+3,15,5,"Add a new definition"))  {
         // String Name=ask("Give a name to the logic "); 
         // if(Name!=null){
            if(p.eh.GeneDef.nbLogic==0) p.eh.GeneDef.addLogic("","","1","0",false,true); else p.eh.GeneDef.addLogic("","","1","0",false,false);
            p.mm.nbLogicDef=p.eh.GeneDef.nbLogic; p.mm.LogicDefSize=new int[p.mm.nbLogicDef];
         // }
        }
      
    }
  
  }
  
  //Draw the menu of a Definition Genes
  void ObjetsDefinition(int ligneX,int ligneY,MetaLogic logi,ArrayList Objets,int numLogic){
  
     p.fill(p.cm.Textcolor,150);  p.text("if",ligneX-p.textWidth("if")-5,ligneY+14);
     
     int sizeX=0;  //Calcul the size of the logic
     if(Objets!=null)  
         for(int i=0;i<Objets.size();i++)  
             sizeX+=((Objet)Objets.get(i)).size(); 
     
     if(sizeX==0) sizeX=50; //MinimumSize
     if(Objets!=null && p.eh.mouseDraggOn(ligneX,ligneY,sizeX+50,20)){  //If We drag something from the list to the definition
         if(p.mm.DragMod==0) sizeX+=p.ObjetDrag.size();   //From All the List
         if(p.mm.DragMod==1 && p.mm.MenuDragActive==1) sizeX+=p.ObjetDrag.size();   //From The Domain Input Definition
         if(p.mm.DragMod==2 && p.mm.numLogicDrag!=numLogic)  sizeX+=p.ObjetDrag.size(); //From The Logic 
         
     } 
       
     p.mm.LogicDefSize[numLogic]=sizeX; //The Size of the Definition box of this logic
     String Name=logi.getName();
     float minSizeBox=p.textWidth(Name);
     
     int BckActi=50; //Background color of the defnition
     if(p.eh.mouseOn(ligneX-30-minSizeBox,ligneY-5,minSizeBox+10,30)) BckActi=150;
     if(p.eh.mousePressOn(ligneX-30-minSizeBox,ligneY-5,minSizeBox+10,30)){ //Change the name of the logic or Dragg it
           if(p.doubleClick()){
               String NewName=p.eh.ask("Give a name to the logic "+Name); 
               if(NewName!=null)logi.setName(NewName);
           }
           else{
               p.ObjetDrag=new Objet(p, logi);
               p.mm.DragMod=2; p.mm.MenuDragActive=0; p.mm.numLogicDrag=numLogic;    
           }
     }
     p.fill(p.cm.colorBoxBackground,BckActi);  p.stroke(p.cm.colorBoxBackground);   p.rect(ligneX-30-minSizeBox,ligneY-5,minSizeBox+10,30); //Display the square arround the name 
     p.fill(p.cm.Textcolor,255);      p.text(Name,ligneX-p.textWidth(Name)-25,ligneY+14); //Display the name of the logic
     
     
     //Blue Module
     int leftX=p.round(ligneX-p.textWidth(Name))-50;
     if(p.mm.boutonCoche(leftX,ligneY-3,10, p.cm.colorButton,""+Name+" is an unknown" ,""+Name+" is known"  ,logi.isBlue,p.Unknown,null) && p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; logi.isBlue=!logi.isBlue;}
     
    
     //Default logic ?
     if(p.eh.GeneDef.nbLogic>1){
       if(logi.dft){  p.fill(p.cm.Textcolor,100);p.text("default",leftX-25,ligneY+22);}
       else{  
         p.noFill(); p.stroke(p.cm.Textcolor,100);p.rect(leftX,ligneY+12,10,10);
         if(p.eh.mousePressOn(leftX,ligneY+12,10,10)){ 
          logi.dft=true;
          for(int ll=0;ll<p.eh.GeneDef.nbLogic;ll++) //Switch the other logic to false
               if(numLogic!=ll){
                   MetaLogic logis=p.eh.GeneDef.getMetaLogic(p.eh.GeneDef.getObjets(ll));
                   logis.dft=false;
               }
           }
       }
     }
     
     p.fill(p.cm.colorBoxBackground,50);   p.stroke(p.cm.colorBoxBackground);   p.rect(ligneX,ligneY-5,sizeX+4,30);  //Draw a box for the definition of the logic
    
    int ix=0; int Position=0;
    if(Objets!=null) {
    for(int i=0;i<Objets.size();i++)  {
        Objet obj=(Objet)Objets.get(i);
         int ObjetSize=obj.size();
         if(p.eh.mouseDraggOn(ligneX+ix+5,ligneY,ObjetSize,20))   //Shift the object if we are dragginf somthing on it
               if((p.mm.numLogicDrag==numLogic  && p.mm.DragMod!=2) 
                   || p.mm.numLogicDrag==-1
                   || (p.mm.numLogicDrag!=numLogic  && p.mm.DragMod==2 )) {   p.mm.DragPosition=Position; ix+=p.ObjetDrag.size();  }
                   
         if(p.eh.mousePressOn(ligneX+ix+5,ligneY,ObjetSize,20))  { //When we click on it to drag somewere
             p.ObjetDrag=obj;
             p.mm.DragMod=1; p.mm.MenuDragActive=0; p.mm.numLogicDrag=numLogic;
         }
         if(p.ObjetDrag==obj)  ix-=ObjetSize;
         else { obj.draw(ligneX+ix+5,ligneY,-1);Position++;}
         ix+=ObjetSize;
     }
     //In case the Object is drag after all the objetcs
      if(p.ObjetDrag!=null && p.eh.mouseDraggOn(ligneX+ix+5,ligneY,p.ObjetDrag.size(),20))  
               if((p.mm.numLogicDrag==numLogic  && p.mm.DragMod!=2) 
                   || p.mm.numLogicDrag==-1
                   ||  (p.mm.numLogicDrag!=numLogic  && p.mm.DragMod==2 ))   p.mm.DragPosition=Objets.size(); //Set the Last position of Drag in case it is a the last !
    }
    
     //Resize Menu X
     p.mm.ResizeMenuXY(0,0,ix+330);
     
       
     //Plot The 1 Else 0
     if(p.eh.mousePressOn(ligneX+sizeX+5+28,ligneY,10,20)) logi.invThen();
     int c=p.cm.colorGeneOff; if(p.uf.parseBool(logi.getThen())) c=p.cm.colorGeneOn;
     p.fill(c,50);   p.noStroke();   p.rect(ligneX+sizeX+5+28,ligneY,10,20);
     
     if(p.eh.mousePressOn(ligneX+sizeX+5+68,ligneY,10,20)) logi.invElse();
     c=p.cm.colorGeneOff; if(p.uf.parseBool(logi.getElse())) c=p.cm.colorGeneOn;
     p.fill(c,50);   p.noStroke();   p.rect(ligneX+sizeX+71,ligneY,10,20);
     p.fill(p.cm.Textcolor,150);    p.text("then " +logi.getThen() + " else " +  logi.getElse(),ligneX+sizeX+5,ligneY+14); //Draw then else
  
     //Icon when Logic is valid
     if(logi.valid)  p.image(p.Good,ligneX+sizeX+50+p.textWidth("then 0 else 1"),ligneY);
     else p.image(p.Wrong,ligneX+sizeX+50+p.textWidth("then 0 else 1"),ligneY);
  }
    
    
}

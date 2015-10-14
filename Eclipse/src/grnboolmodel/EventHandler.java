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

import javax.swing.*;

/////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Event.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

public class EventHandler {

  private GRNBoolModel p;
  
  EventHandler(GRNBoolModel p) {
    this.p = p;   
  } 

  boolean AcceptEvent=true;
  
  Gene GeneDef=null; //Gene current show in the definition
  int [] CoordinateMouseXY=new int[2];
  
  int MouseCursor=p.ARROW;
  
  //////////////////////// KEY PRESS //////////////////////////
  int OldKeyCode=-1;
  public void keyPressed(){
    if(OldKeyCode==p.ALT || OldKeyCode==157){
       if(p.keyCode==83)  p.sm.saveXML(p.lm.lastModelLoaded); //POMME || ALT S
    }
    
    if(p.key=='+') {p.Scale*=1.1;p.mm.addMessage("Scale is "+p.Scale);} 
    if(p.key=='-') {p.Scale*=0.9;p.mm.addMessage("Scale is "+p.Scale);} 
    
    if(p.key=='F'){p.frameR++;p.frameRate(p.frameR);p.mm.addMessage("FrameRate is " +p.frameR);}
    if(p.key=='f'){if(p.frameR>1) p.frameR--;p.frameRate(p.frameR);p.mm.addMessage("FrameRate is " +p.frameR);}
    OldKeyCode=p.keyCode;
    
    //Keyboard activate for search function
    if(p.smm.searchMenu && p.lm.ModelLoad!=null){
        if(p.keyCode==p.BACKSPACE) {if(p.smm.textSearchInput.length()>0)  p.smm.textSearchInput=p.smm.textSearchInput.substring(0,p.smm.textSearchInput.length()-1);}
        else if(p.key>=32 && p.key<=126) p.smm.textSearchInput=p.smm.textSearchInput+p.key;
        //ChangeColor=0;ActiveMenuFont=false;
        p.smm.textSearch=new ArrayList();
        for(int j=0;j<p.lm.ModelLoad.length;j++)
          if(p.lm.ModelLoad[j].toLowerCase().indexOf(p.smm.textSearchInput.toLowerCase())>=0)  //No more thant 20 result
              p.smm.textSearch.add(p.lm.ModelLoad[j]);
        }
  }
  
  //////////////////////// MOUSE RELEASE //////////////////////////////////////////////////////////////////////// 
  public void mouseReleasedEH(){
    //addMessage("Release");
    AcceptEvent=true;
    
    //Simple Click
    if(CoordinateMouseXY[0]==mouseX() && CoordinateMouseXY[1]==mouseY()){
      if(p.mm.MenuDrag>=0)  {   //Simple Click on a menu
            p.mm.MenuActive[p.mm.MenuDrag]=1-p.mm.MenuActive[p.mm.MenuDrag];
            p.mbm.activeItem(p.mm.MenuDrag,p.mm.MenuActive[p.mm.MenuDrag]); //Update the Menu CheckBox
            if(p.mm.MenuDrag==2 || p.mm.MenuDrag==8 || p.mm.MenuDrag==6 || p.mm.MenuDrag==7) p.mm.SizeMenuXY[p.mm.MenuDrag][1]=100; //To Automatic Rezise
        }
      if(p.ObjetDrag!=null && p.mm.DragMod==0 && p.mm.MenuDragActive==0 && p.ObjetDrag.isGene()) { //Click a Gene from the List
            GeneDef=p.ObjetDrag.getGene();
            if(GeneDef.Logics!=null)  p.mm.nbLogicDef=GeneDef.nbLogic;    else  p.mm.nbLogicDef=0; 
            p.mm.LogicDefSize=new int[p.mm.nbLogicDef];
            p.mm.ResizeMenuXY(p.mm.MenuDragActive,0,300);
            p.ObjetDrag=null;
      }
      if(p.ObjetDrag!=null && p.mm.DragMod==0 && p.mm.MenuDragActive==1 && p.ObjetDrag.isDomain()) { //Click a Domain from the List
            Domain DomainDrag=p.ObjetDrag.getDomain();
             if(DomainDrag==p.dm.GenericDomain)  alert("You can not edit this generic domain "+ DomainDrag.Name); 
             else p.dm.DomainDef=p.ObjetDrag.getDomain();
             p.ObjetDrag=null;
             p.mm.ResizeMenuXY(p.mm.MenuDragActive,0,300);
      }
       if(p.ObjetDrag!=null && p.mm.DragMod==1 && p.mm.MenuDragActive==0 && p.ObjetDrag.isLogicGene()) { //Click on a Logic Gene  in the definition
             if (p.doubleClick()) {
                 p.mm.LogicDeroule=p.ObjetDrag.getLogicGene();
                 p.mm.SizeMaxDeroule=0;
                 if(p.mm.LogicDeroule.g.nbLogic> 1)   p.ObjetDrag=null; //More than one Logic so we deplioy
                 else  { p.mm.LogicDeroule=null;p.mm.ExternWindow=null;}
             } else   { p.mm.LogicDeroule=null;p.mm.ExternWindow=null;}
      }else  { p.mm.LogicDeroule=null;p.mm.ExternWindow=null;}
    }else  { p.mm.LogicDeroule=null;p.mm.ExternWindow=null;}
  
     //When we drag a gene in Gene  Definition Box
     if(p.ObjetDrag!=null && mouseOn(p.mm.LigneGeneDefBoxX-10,p.mm.LigneGeneDefBoxY-10,p.gm.SizeDrawGene+20,45)){ 
          if(p.mm.MenuDragActive==0 && p.ObjetDrag.isGene()){ //Grom Gene List
              GeneDef=p.ObjetDrag.getGene();   
              if(GeneDef.Logics!=null)  p.mm.nbLogicDef=GeneDef.nbLogic;    else  p.mm.nbLogicDef=0; 
             p.mm.LogicDefSize=new int[p.mm.nbLogicDef];
              p.ObjetDrag=null; 
              p.mm.ResizeMenuXY(p.mm.MenuDragActive,0,300);
          }
    }
    
    
    //Drag Gene on Definition Input Box
    if(p.ObjetDrag!=null && GeneDef!=null){
          for(int l=0;l<p.mm.nbLogicDef;l++){ //For All Active Logic
              if(mouseOn(p.mm.LigneGeneDefBoxX-5,p.mm.LigneGeneDefBoxY-10+35*(l+1),p.mm.LogicDefSize[l]+10,30)) {
                  int Position=p.mm.DragPosition;
                    switch(p.mm.DragMod){ 
                          case 0:  //Drag from a list (Gene/Domain/Operator)
                            Objet obj=null;//ObjetDrag;
                            if(p.ObjetDrag.isGene()) { Gene gg=p.ObjetDrag.getGene(); obj=new Objet(p, new LogicGene(p, gg,true,gg.getDefaultLogicName()));  }
                            if(p.ObjetDrag.isDomain())  obj=new Objet(p, p.ObjetDrag.getDomain());
                            if(p.ObjetDrag.isOperator()){ Operator op=new Operator(p.ObjetDrag.getOperator()); if(op.comp==4 || op.comp==5 || op.comp==6) op.step=0; obj=new Objet(p, op);}
                            if(p.ObjetDrag.isString()) {  obj=new Objet(p, p.ObjetDrag.getString());  }

                            // Any other object, such as a region, should not be draggable to a vector equation
                            if (obj != null) {
                              GeneDef.addObjet(l, Position, obj);  //Gene Definition Add
                            }
                        
                          break;
                          case 1:   //Drag From Input Definition Box (Gene/Domains)
                            if(p.mm.MenuDragActive==0 )  {
                                   if(p.mm.numLogicDrag==l) GeneDef.switchObject(l,Position,p.ObjetDrag);  //Drag From Gene Input to a Gene Input          
                            }
                         break;
                         case 2 : //Drag a Logic (Meta Logic)
                           if(p.mm.MenuDragActive==0 && p.ObjetDrag.isMetaLogic() && p.mm.numLogicDrag!=l) {
                                LogicLogic lo=new LogicLogic(p, p.ObjetDrag.getMetaLogic(),true);
                                 GeneDef.addObjet(l,Position,new Objet(p, lo)); //From a Domain Input to a normal Gene List
                           }
                    break;
                    }
                   p.ObjetDrag=null; 
              }
        }
         //Drag a Modul to change the order of is position
        if(p.ObjetDrag!=null && GeneDef!=null && p.mm.DragMod==2 && p.mm.MenuDragActive==0){
          if(mouseOn(p.mm.LigneGeneDefBoxX-150,p.mm.LigneGeneDefBoxY+20,150,35*p.mm.nbLogicDef)) {
            int ldrag=p.mm.nbLogicDef-p.round(((p.mm.LigneGeneDefBoxY+20+35*p.mm.nbLogicDef)-mouseY())/35)-1;
            if(ldrag!=p.mm.numLogicDrag)  GeneDef.swtich(p.mm.numLogicDrag,ldrag); //Swithc Logic Position
          }
        }
  
    }
    
     //Draw on a the NO EXPRESSION data Box
   /* if(ObjetDrag!=null && GeneDef!=null && GeneDef.Expression==null && mouseOn(LigneGeneDefBoxX-10+SizeDrawGene+50,LigneGeneDefBoxY-10,SizeDrawGene+20,45) && MenuDragActive==0 && ObjetDrag.isGene()){ 
           Gene GeneData=ObjetDrag.getGene();   
           GeneDef.GeneData=GeneData;
           GeneData.GeneData=GeneDef;
    }*/
  
    
  
   
  
     //MENU DOMAIN    
    //Drag on Spatial Domain Definition
     if(p.ObjetDrag!=null &&  p.dm.DomainDef!=null && p.dm.DomainDef.DefObjets!=null && !p.ObjetDrag.isGene()) 
        for(int i=0;i<p.dm.DomainDef.DefObjets.length;i++) { //On each Spatial Definition
         int ix=0; 
          for(int j=0;j<2;j++)  {
           int siY=p.dm.SizeDrawDomain+6; if(j==0) siY=p.pm.SizeDrawOperator+6+30;
           if(mouseOn(p.mm.LigneDomDefBoxX+ix,p.mm.LigneDomDefBoxY+140-5+i*30,siY,30)) {
              if(j==0 && p.ObjetDrag.isOperator()){
                  Operator op=new Operator(p.ObjetDrag.getOperator()); op.hmin=0; op.hmax=p.rm.MaxTime;
                   if(op.Name.equals("CC") || op.Name.equals("NCC_n") || op.Name.equals("NCC_d"))   p.dm.DomainDef.DefObjets[i][j]=new Objet(p, op);
              }else if(j==1 && p.ObjetDrag.isDomain()){  p.dm.DomainDef.DefObjets[i][j]=p.ObjetDrag; }
           }
           ix+=siY;
          }
        }
       
       
       //On Tree Domain Definition
      if(p.ObjetDrag!=null &&  p.dm.DomainDef!=null && p.ObjetDrag.isRegion()) {
         int SizeTree=p.max(p.dm.DomainDef.getTreeSize(),50); 
         if(mouseOn(p.mm.LigneDomDefBoxX-5,p.mm.LigneDomDefBoxY+70,SizeTree+10,30)) 
             p.dm.DomainDef.addTree(p.ObjetDrag.getRegion());
          
      }
   
  
  
      
   
       //On the Trash
    if(p.ObjetDrag!=null && mouseOn(10,p.height-50,40,40)) {
        //addMessage("on Trash with DragMod="+DragMod + " and MenuDragActive="+MenuDragActive);
        switch(p.mm.DragMod){
        case 0:  //From a General List -> Complety Delete
          if(p.ObjetDrag.isGene()){ //A Gene
            Gene GeneDrag=p.ObjetDrag.getGene();
            if (confirm("Are you sure you want to delete  " + GeneDrag.Name+ " ?")) {
              if (p.gm.isGeneReferencedInVectorEquations(GeneDrag)) {
                p.gmm.displayGeneReferencedErrorDialog(GeneDrag);
              }
              else {
                p.gm.delGene(GeneDrag);
              }
            }

            p.ObjetDrag=null;
          } 
          else if(p.ObjetDrag.isDomain()){ // Domain
            Domain DomainDrag=p.ObjetDrag.getDomain();
            if(DomainDrag==p.dm.GenericDomain)   alert("You can not delete this generic domain "+ DomainDrag.Name); 
            else{
              if(p.mm.MenuDragActive==1) { // From the Domain List
                if(confirm("Are you sure you want to delete  " + DomainDrag.Name+ " ?")) p.dm.delDomain(DomainDrag); 
              }
            }
            p.ObjetDrag=null;
          }
          else if(p.ObjetDrag.isRegion()){ //Region
                Region regDrag=p.ObjetDrag.getRegion();
                if(confirm("Are you sure you want to delete this region " + regDrag.Name+ " ?")) p.rm.delRegion(regDrag); 
          }
          break;
        case 1: //From a definition (Gene / Domain ) 
            if(p.mm.MenuDragActive==0) 
                GeneDef.delObject(p.mm.numLogicDrag,p.ObjetDrag); //From Gene Definition
            break;
        case 2 : //Delete a logic
             if(p.mm.MenuDragActive==0  && GeneDef!=null && p.ObjetDrag.isMetaLogic()) //&& DomainGeneDef==null
                  GeneDef.del(p.ObjetDrag.getMetaLogic());
             
             //Delete an Element from the Domain TRee
             if(p.mm.MenuDragActive==1 && p.dm.DomainDef!=null && p.ObjetDrag.isRegion())
                   p.dm.DomainDef.delTree(p.ObjetDrag.getRegion());
        break;
        }
        p.ObjetDrag=null;
    }
    
    
    
    p.mm.DragPosition=-1;
    p.mm.LogicDefActive=null;
    p.mm.DragMod=-1;
    p.mm.numLogicDrag=-1; 
    p.ObjetDrag=null;
    p.mm.MenuDragActive=-1;
    p.mm.MenuDrag=-1;
    p.mm.MenuReSizeXY[0]=-1;p.mm.MenuReSizeXY[1]=-1;
    p.mm.TimeExplanation=30;p.mm.Explanation="";p.mm.GenesInvolve=null;
    p.mm.scrollMenu=-1;
    
  }
  
  
  //When we drag an object
  public void mouseDraggedEH(){
     MouseCursor=p.HAND; 
     
    //Drag a Menu
    if(p.mm.MenuDrag>=0){
      p.mm.OrginMenuXY[p.mm.MenuDrag][0]=p.mm.CoordMenuDrag[0]+mouseX()-CoordinateMouseXY[0];
      if(p.mm.OrginMenuXY[p.mm.MenuDrag][0]<0) p.mm.OrginMenuXY[p.mm.MenuDrag][0]=0;
      if(p.mm.OrginMenuXY[p.mm.MenuDrag][0]>p.width) p.mm.OrginMenuXY[p.mm.MenuDrag][0]=p.width;
       p.mm.OrginMenuXY[p.mm.MenuDrag][1]=p.mm.CoordMenuDrag[1]+mouseY()-CoordinateMouseXY[1];
      if(p.mm.OrginMenuXY[p.mm.MenuDrag][1]<0) p.mm.OrginMenuXY[p.mm.MenuDrag][1]=0;
      if(p.mm.OrginMenuXY[p.mm.MenuDrag][1]>p.height) p.mm.OrginMenuXY[p.mm.MenuDrag][1]=p.height;
    }
    //When we resize a Menu in X
    if(p.mm.MenuReSizeXY[0]>=0){  MouseCursor=p.CROSS;    p.mm.SizeMenuXY[p.mm.MenuReSizeXY[0]][0]=p.max(mouseX()-p.mm.OrginMenuXY[p.mm.MenuReSizeXY[0]][0],p.mm.MiniMenuSizeXY[p.mm.MenuReSizeXY[0]][0]);  }
    //When we resize a Menu in Y
    if(p.mm.MenuReSizeXY[1]>=0){   MouseCursor=p.CROSS;  p.mm.SizeMenuXY[p.mm.MenuReSizeXY[1]][1]=p.max(mouseY()-p.mm.OrginMenuXY[p.mm.MenuReSizeXY[1]][1],p.mm.MiniMenuSizeXY[p.mm.MenuReSizeXY[1]][1]); p.mm.scrollMenu=p.mm.MenuReSizeXY[1];}
    
   
     if(p.mm.scrollMenu>=0){
        float sizeBar=p.mm.SizeMenuXY[p.mm.scrollMenu][1]-40;
       switch(p.mm.scrollMenu){
         case 0 :  //Menu Genes
             if(p.mm.SizeMenuXY[p.mm.scrollMenu][1]-50<p.gm.Genes.size()*25){
                 p.mm.ShiftGenesY=p.mm.oldScroll+p.round((p.mouseY-CoordinateMouseXY[1])*(sizeBar-p.gm.Genes.size()*25)/(sizeBar-sizeBar/(p.gm.Genes.size()*25/sizeBar)));
               if(p.mm.ShiftGenesY>0)p.mm.ShiftGenesY=0;
               else if(p.mm.ShiftGenesY<p.mm.SizeMenuXY[p.mm.scrollMenu][1]-p.gm.Genes.size()*25-50) p.mm.ShiftGenesY=p.mm.SizeMenuXY[p.mm.scrollMenu][1]-p.gm.Genes.size()*25-50; 
             }else p.mm.ShiftGenesY=0;
         break;
         case 1 :  //Menu Domains
            if(p.mm.SizeMenuXY[p.mm.scrollMenu][1]-50<p.dm.Domains.size()*25){
                p.mm.ShiftDomainsY=p.mm.oldScroll+p.round((p.mouseY-CoordinateMouseXY[1])*(sizeBar-p.dm.Domains.size()*25)/(sizeBar-sizeBar/(p.dm.Domains.size()*25/sizeBar)));
                 if(p.mm.ShiftDomainsY>0)p.mm.ShiftDomainsY=0;
             else if(p.mm.ShiftDomainsY<p.mm.SizeMenuXY[p.mm.scrollMenu][1]-p.dm.Domains.size()*25-50) p.mm.ShiftDomainsY=p.mm.SizeMenuXY[p.mm.scrollMenu][1]-p.dm.Domains.size()*25-50; 
            }else p.mm.ShiftDomainsY=0;
         break;
       }
         
       
       
     }
    
  }
  
  public void mouseMovedEH(){
    p.mm.TimeExplanation=30;p.mm.Explanation="";p.mm.GenesInvolve=null;
    p.mm.StepExplanation=0;
  }
  
  public void mousePressedEH(){
    CoordinateMouseXY[0]=mouseX(); CoordinateMouseXY[1]=mouseY();  
  }
  
  public boolean mouseDraggOn(float x,float y,float w,float h){
    if(p.ObjetDrag!=null && mouseOn(x,y,w,h)) return true;
    return false;
  }
  public boolean mouseOn(float x,float y,float w,float h){
    if (mouseOnMenu() && mouseX()<=x+w && mouseX()>=x &&mouseY()<=y+h && mouseY()>=y) return true;
    return false;
  
  }
  public boolean mousePressOn(float x,float y,float w,float h){
    if(p.mousePressed && AcceptEvent && mouseOn(x,y,w,h)) {
      AcceptEvent=false; 
      return true;
    }
    return false;
  
  }
  //Check if the Menu is not on a hidden place
  public boolean mouseOnMenu(){
    if(p.cm.offMenu) return false;
    for(int i=0;i<p.mm.CurrentMenu;i++)
       if (mouseX()>=p.mm.HideMouse[i][0] && mouseX()<=p.mm.HideMouse[i][0]+p.mm.HideMouse[i][2] && mouseY()>=p.mm.HideMouse[i][1] && mouseY()<=p.mm.HideMouse[i][1]+p.mm.HideMouse[i][3]) return false; 
    if(p.mm.ExternWindow!=null)  
       if (mouseX()>=p.mm.ExternWindow[0] && mouseX()<=p.mm.ExternWindow[0]+p.mm.ExternWindow[2] && mouseY()>=p.mm.ExternWindow[1] && mouseY()<=p.mm.ExternWindow[1]+p.mm.ExternWindow[3]) return false; 
    if(p.mm.BlockMenuDefile!=null){
      if (mouseX()>=p.mm.BlockMenuDefile[0] && mouseX()<=p.mm.BlockMenuDefile[0]+p.mm.BlockMenuDefile[2] && mouseY()>=p.mm.BlockMenuDefile[1] && mouseY()<=p.mm.BlockMenuDefile[1]+p.mm.BlockMenuDefile[3]) return false; else{p.mm.BlockMenuDefile=null;  }
    }
    
   return true;
  }
  
     
  
  //////////////////////// MOUSE WHEEL ////////////////////////////////////////////////////////////////////////
  int not=5;
  void initMouse(){
     p.addMouseWheelListener(new java.awt.event.MouseWheelListener() { 
      public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) { 
        int notches = evt.getWheelRotation(); 
        //Genes List 
        if(mouseX()>p.mm.OrginMenuXY[0][0] && mouseX()<p.mm.OrginMenuXY[0][0]+p.gm.SizeDrawGene && mouseY()>p.mm.OrginMenuXY[0][1]+50 && mouseY()<p.mm.OrginMenuXY[0][1]+p.mm.SizeMenuXY[0][1]){
          if(p.mm.SizeMenuXY[0][1]-50<p.gm.Genes.size()*25){
             if(notches>0) p.mm.ShiftGenesY-=not; else p.mm.ShiftGenesY+=not;
             if(p.mm.ShiftGenesY>0) p.mm.ShiftGenesY=0; 
             else if(p.mm.ShiftGenesY<p.mm.SizeMenuXY[0][1]-p.gm.Genes.size()*25-50) p.mm.ShiftGenesY=p.mm.SizeMenuXY[0][1]-p.gm.Genes.size()*25-50;
          }else p.mm.ShiftGenesY=0;
        }
        //Domain List
        if(mouseX()>p.mm.OrginMenuXY[1][0] && mouseX()<p.mm.OrginMenuXY[1][0]+p.dm.SizeDrawDomain && mouseY()>p.mm.OrginMenuXY[1][1]+50 && mouseY()<p.mm.OrginMenuXY[1][1]+p.mm.SizeMenuXY[1][1]){
          if(p.mm.SizeMenuXY[1][1]-50<p.dm.Domains.size()*25){
            if(notches>0) p.mm.ShiftDomainsY-=not; else p.mm.ShiftDomainsY+=not;
            if(p.mm.ShiftDomainsY>0) p.mm.ShiftDomainsY=0; 
            else if(p.mm.ShiftDomainsY<p.mm.SizeMenuXY[1][1]-50-p.dm.Domains.size()*25) p.mm.ShiftDomainsY=p.mm.SizeMenuXY[1][1]-50-p.dm.Domains.size()*25;
          }else p.mm.ShiftDomainsY=0;
        } 
      }
    }
    ); 
  }
  
  int mouseX(){if(p.Scale==1) return p.mouseX; return p.round(p.mouseX/p.Scale);}
  int mouseY(){if(p.Scale==1) return p.mouseY; return p.round(p.mouseY/p.Scale);}
  
   
  public void bigMessage(String mes){bigMessage(mes, p.width / 2, p.height / 2);}
  public void bigMessage(String mes,float x,float y){ 
    p.fill(p.cm.Textcolor); p.textFont(p.cm.myFont,36); p.text(mes,x,y+30);
    p.image(p.logo,x-45,y,40,40);p.textFont(p.cm.myFont, 12);
  }
  
  ///FUNCTION MESSAGE / ALERT
  public void alert(final String mes) {
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          JOptionPane.showMessageDialog(null, mes);
        }
      });
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //FUNCTION ASK QUESTION CONFIRM YES OR NO
  public Boolean confirm(final String mess){
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          p.jopConfirmRetVal = JOptionPane.showConfirmDialog(null, mess, "GRN Boolean Model", JOptionPane.YES_NO_OPTION); //0 -> Yes, 1 No
        }
      });
      return p.jopConfirmRetVal == 0;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  //FUNCTION ASK STRIN INPUT
  public String ask(final String mess){
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          p.jopAskRetVal = JOptionPane.showInputDialog(mess);
        }
      });

      String name = p.jopAskRetVal;
      if (name != null && !name.equals("")) return name;
      return null;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String ask(final String mess, final String defaultMess){
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          p.jopAskRetVal = JOptionPane.showInputDialog(mess, defaultMess);
        }
      });

      String name = p.jopAskRetVal;
      if (name != null && !name.equals("")) return name;
      return null;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void handleModelNotValidForExport(GRNBoolModel.MODEL_EXPORT_ERROR errorCode) {
    switch (errorCode) {
      case INCOMPLETE_DOMAINS:
        p.dmm.displayIncompleteDomainDefsErrorDialog();
        break;
    }
  }
}

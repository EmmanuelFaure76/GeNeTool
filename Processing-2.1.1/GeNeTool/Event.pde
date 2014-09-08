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

boolean AcceptEvent=true;

Gene GeneDef=null; //Gene current show in the definition
int [] CoordinateMouseXY=new int[2];

int MouseCursor=ARROW;
//////////////////////// KEY PRESS //////////////////////////
int OldKeyCode=-1;
void keyPressed(){
  if(OldKeyCode==ALT || OldKeyCode==157){
     if(keyCode==83)  saveXML(lastModelLoaded); //POMME || ALT S
  }
  
  if(key=='+') {Scale*=1.1;addMessage("Scale is "+Scale);} 
  if(key=='-') {Scale*=0.9;addMessage("Scale is "+Scale);} 
  
  if(key=='F'){frameR++;frameRate(frameR);addMessage("FrameRate is " +frameR);}
  if(key=='f'){if(frameR>1) frameR--;frameRate(frameR);addMessage("FrameRate is " +frameR);}
  OldKeyCode=keyCode;
  
  //Keyboard activate for search function
  if(searchMenu && ModelLoad!=null){
      if(keyCode==BACKSPACE) {if(textSearchInput.length()>0)  textSearchInput=textSearchInput.substring(0,textSearchInput.length()-1);}
      else if(key>=32 && key<=126) textSearchInput=textSearchInput+key;
      //ChangeColor=0;ActiveMenuFont=false;
      textSearch=new ArrayList();
      for(int j=0;j<ModelLoad.length;j++)
        if(ModelLoad[j].toLowerCase().indexOf(textSearchInput.toLowerCase())>=0)  //No more thant 20 result
            textSearch.add(ModelLoad[j]);
      }
}
//////////////////////// MOUSE RELEASE //////////////////////////////////////////////////////////////////////// 
void mouseReleased(){
  //addMessage("Release");
  AcceptEvent=true;
  
  //Simple Click
  if(CoordinateMouseXY[0]==mouseX() && CoordinateMouseXY[1]==mouseY()){
    if(MenuDrag>=0)  {   //Simple Click on a menu
          MenuActive[MenuDrag]=1-MenuActive[MenuDrag];
          activeItem(MenuDrag,MenuActive[MenuDrag]); //Update the Menu CheckBox
          if(MenuDrag==2 || MenuDrag==8 || MenuDrag==6 || MenuDrag==7) SizeMenuXY[MenuDrag][1]=100; //To Automatic Rezise
      }
    if(ObjetDrag!=null && DragMod==0 && MenuDragActive==0 && ObjetDrag.isGene()) { //Click a Gene from the List
          GeneDef=ObjetDrag.getGene();
          if(GeneDef.Logics!=null)  nbLogicDef=GeneDef.nbLogic;    else  nbLogicDef=0; 
          LogicDefSize=new int[nbLogicDef];
          ResizeMenuXY(MenuDragActive,0,300);
          ObjetDrag=null;
    }
    if(ObjetDrag!=null && DragMod==0 && MenuDragActive==1 && ObjetDrag.isDomain()) { //Click a Domain from the List
          Domain DomainDrag=ObjetDrag.getDomain();
           if(DomainDrag==GenericDomain)  alert("You can not edit this generic domain "+ DomainDrag.Name); 
           else DomainDef=ObjetDrag.getDomain();
           ObjetDrag=null;
           ResizeMenuXY(MenuDragActive,0,300);
    }
     if(ObjetDrag!=null && DragMod==1 && MenuDragActive==0 && ObjetDrag.isLogicGene()) { //Click on a Logic Gene  in the definition
           if (mouseEvent.getClickCount()==2) {
               LogicDeroule=ObjetDrag.getLogicGene();
               SizeMaxDeroule=0;
               if(LogicDeroule.g.nbLogic> 1)   ObjetDrag=null; //More than one Logic so we deplioy
               else  { LogicDeroule=null;ExternWindow=null;}
           } else   { LogicDeroule=null;ExternWindow=null;}
    }else  { LogicDeroule=null;ExternWindow=null;}
  }else  { LogicDeroule=null;ExternWindow=null;}

   //When we drag a gene in Gene  Definition Box
   if(ObjetDrag!=null && mouseOn(LigneGeneDefBoxX-10,LigneGeneDefBoxY-10,SizeDrawGene+20,45)){ 
        if(MenuDragActive==0 && ObjetDrag.isGene()){ //Grom Gene List
            GeneDef=ObjetDrag.getGene();   
            if(GeneDef.Logics!=null)  nbLogicDef=GeneDef.nbLogic;    else  nbLogicDef=0; 
            LogicDefSize=new int[nbLogicDef];
            ObjetDrag=null; 
            ResizeMenuXY(MenuDragActive,0,300);
        }
  }
  
  
  //Drag Gene on Definition Input Box
  if(ObjetDrag!=null && GeneDef!=null){
        for(int l=0;l<nbLogicDef;l++){ //For All Active Logic
            if(mouseOn(LigneGeneDefBoxX-5,LigneGeneDefBoxY-10+35*(l+1),LogicDefSize[l]+10,30)) {
                int Position=DragPosition;
                  switch(DragMod){ 
                        case 0:  //Drag from a list (Gene/Domain/Operator)
                          Objet obj=null;//ObjetDrag;
                          if(ObjetDrag.isGene()) { Gene gg=ObjetDrag.getGene(); obj=new Objet(new LogicGene(gg,true,gg.getDefaultLogicName()));  }
                          if(ObjetDrag.isDomain())  obj=new Objet(ObjetDrag.getDomain());
                          if(ObjetDrag.isOperator()){ Operator op=new Operator(ObjetDrag.getOperator()); if(op.comp==4 || op.comp==5 || op.comp==6) op.step=0; obj=new Objet(op);}
                          if(ObjetDrag.isString()) {  obj=new Objet(ObjetDrag.getString());  }
                          GeneDef.addObjet(l,Position,obj);  //Gene Definition Add 
                      
                        break;
                        case 1:   //Drag From Input Definition Box (Gene/Domains)
                          if(MenuDragActive==0 )  {
                                 if(numLogicDrag==l) GeneDef.switchObject(l,Position,ObjetDrag);  //Drag From Gene Input to a Gene Input          
                          }
                       break;
                       case 2 : //Drag a Logic (Meta Logic)
                         if(MenuDragActive==0 && ObjetDrag.isMetaLogic() && numLogicDrag!=l) {
                              LogicLogic lo=new LogicLogic(ObjetDrag.getMetaLogic(),true);
                               GeneDef.addObjet(l,Position,new Objet(lo)); //From a Domain Input to a normal Gene List
                         }
                  break;
                  }
                 ObjetDrag=null; 
            }
      }
       //Drag a Modul to change the order of is position
      if(ObjetDrag!=null && GeneDef!=null && DragMod==2 && MenuDragActive==0){
        if(mouseOn(LigneGeneDefBoxX-150,LigneGeneDefBoxY+20,150,35*nbLogicDef)) {
          int ldrag=nbLogicDef-round(((LigneGeneDefBoxY+20+35*nbLogicDef)-mouseY())/35)-1;
          if(ldrag!=numLogicDrag)  GeneDef.swtich(numLogicDrag,ldrag); //Swithc Logic Position
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
   if(ObjetDrag!=null &&  DomainDef!=null && DomainDef.DefObjets!=null && !ObjetDrag.isGene()) 
      for(int i=0;i<DomainDef.DefObjets.length;i++) { //On each Spatial Definition
       int ix=0; 
        for(int j=0;j<2;j++)  {
         int siY=SizeDrawDomain+6; if(j==0) siY=SizeDrawOperator+6+30;
         if(mouseOn(LigneDomDefBoxX+ix,LigneDomDefBoxY+140-5+i*30,siY,30)) {
            if(j==0 && ObjetDrag.isOperator()){
                Operator op=new Operator(ObjetDrag.getOperator()); op.hmin=0; op.hmax=MaxTime;
                 if(op.Name.equals("CC") || op.Name.equals("NCC_n") || op.Name.equals("NCC_d"))   DomainDef.DefObjets[i][j]=new Objet(op);
            }else if(j==1 && ObjetDrag.isDomain()){  DomainDef.DefObjets[i][j]=ObjetDrag; }
         }
         ix+=siY;
        }
      }
     
     
     //On Tree Domain Definition
    if(ObjetDrag!=null &&  DomainDef!=null && ObjetDrag.isRegion()) {
       int SizeTree=max(DomainDef.getTreeSize(),50); 
       if(mouseOn(LigneDomDefBoxX-5,LigneDomDefBoxY+70,SizeTree+10,30)) 
           DomainDef.addTree(ObjetDrag.getRegion());
        
    }
 


    
 
     //On the Trash
  if(ObjetDrag!=null && mouseOn(10,height-50,40,40)) {
      //addMessage("on Trash with DragMod="+DragMod + " and MenuDragActive="+MenuDragActive);
      switch(DragMod){
      case 0:  //From a General List -> Complety Delete
        if(ObjetDrag.isGene()){ //A Gene
          Gene GeneDrag=ObjetDrag.getGene();
          if(confirm("Are you sure you want to delete  " + GeneDrag.Name+ " ?")) delGene(GeneDrag);
          ObjetDrag=null;
        } 
        else if(ObjetDrag.isDomain()){ // Domain
          Domain DomainDrag=ObjetDrag.getDomain();
          if(DomainDrag==GenericDomain)   alert("You can not delete this generic domain "+ DomainDrag.Name); 
          else{
            if(MenuDragActive==1) { // From the Domain List
              if(confirm("Are you sure you want to delete  " + DomainDrag.Name+ " ?")) delDomain(DomainDrag); 
            }
          }
          ObjetDrag=null;
        }
        else if(ObjetDrag.isRegion()){ //Region
              Region regDrag=ObjetDrag.getRegion();
              if(confirm("Are you sure you want to delete this region " + regDrag.Name+ " ?")) delRegion(regDrag); 
        }
        break;
      case 1: //From a definition (Gene / Domain ) 
          if(MenuDragActive==0) 
              GeneDef.delObject(numLogicDrag,ObjetDrag); //From Gene Definition
          break;
      case 2 : //Delete a logic
           if(MenuDragActive==0  && GeneDef!=null && ObjetDrag.isMetaLogic()) //&& DomainGeneDef==null
                GeneDef.del(ObjetDrag.getMetaLogic());
           
           //Delete an Element from the Domain TRee
           if(MenuDragActive==1 && DomainDef!=null && ObjetDrag.isRegion())
                 DomainDef.delTree(ObjetDrag.getRegion());
      break;
      }
      ObjetDrag=null;
  }
  
  
  
  DragPosition=-1;
  LogicDefActive=null;
  DragMod=-1;
  numLogicDrag=-1; 
  ObjetDrag=null;
  MenuDragActive=-1;
  MenuDrag=-1;
  MenuReSizeXY[0]=-1;MenuReSizeXY[1]=-1;
  TimeExplanation=30;Explanation="";GenesInvolve=null;
  scrollMenu=-1;
  
}


//When we drag an object
void mouseDragged(){
   MouseCursor=HAND; 
   
  //Drag a Menu
  if(MenuDrag>=0){
    OrginMenuXY[MenuDrag][0]=CoordMenuDrag[0]+mouseX()-CoordinateMouseXY[0];
    if(OrginMenuXY[MenuDrag][0]<0) OrginMenuXY[MenuDrag][0]=0;
    if(OrginMenuXY[MenuDrag][0]>width) OrginMenuXY[MenuDrag][0]=width;
     OrginMenuXY[MenuDrag][1]=CoordMenuDrag[1]+mouseY()-CoordinateMouseXY[1];
    if(OrginMenuXY[MenuDrag][1]<0) OrginMenuXY[MenuDrag][1]=0;
    if(OrginMenuXY[MenuDrag][1]>height) OrginMenuXY[MenuDrag][1]=height;
  }
  //When we resize a Menu in X
  if(MenuReSizeXY[0]>=0){  MouseCursor=CROSS;    SizeMenuXY[MenuReSizeXY[0]][0]=max(mouseX()-OrginMenuXY[MenuReSizeXY[0]][0],MiniMenuSizeXY[MenuReSizeXY[0]][0]);  }
  //When we resize a Menu in Y
  if(MenuReSizeXY[1]>=0){   MouseCursor=CROSS;  SizeMenuXY[MenuReSizeXY[1]][1]=max(mouseY()-OrginMenuXY[MenuReSizeXY[1]][1],MiniMenuSizeXY[MenuReSizeXY[1]][1]); scrollMenu=MenuReSizeXY[1];}
  
 
   if(scrollMenu>=0){
      float sizeBar=SizeMenuXY[scrollMenu][1]-40;
     switch(scrollMenu){
       case 0 :  //Menu Genes
           if(SizeMenuXY[scrollMenu][1]-50<Genes.size()*25){
               ShiftGenesY=oldScroll+round((mouseY-CoordinateMouseXY[1])*(sizeBar-Genes.size()*25)/(sizeBar-sizeBar/(Genes.size()*25/sizeBar)));
             if(ShiftGenesY>0)ShiftGenesY=0;
             else if(ShiftGenesY<SizeMenuXY[scrollMenu][1]-Genes.size()*25-50) ShiftGenesY=SizeMenuXY[scrollMenu][1]-Genes.size()*25-50; 
           }else ShiftGenesY=0;
       break;
       case 1 :  //Menu Domains
          if(SizeMenuXY[scrollMenu][1]-50<Domains.size()*25){
              ShiftDomainsY=oldScroll+round((mouseY-CoordinateMouseXY[1])*(sizeBar-Domains.size()*25)/(sizeBar-sizeBar/(Domains.size()*25/sizeBar)));
               if(ShiftDomainsY>0)ShiftDomainsY=0;
           else if(ShiftDomainsY<SizeMenuXY[scrollMenu][1]-Domains.size()*25-50) ShiftDomainsY=SizeMenuXY[scrollMenu][1]-Domains.size()*25-50; 
          }else ShiftDomainsY=0;
       break;
     }
       
     
     
   }
  
}


void mouseMoved(){
  TimeExplanation=30;Explanation="";GenesInvolve=null;
  StepExplanation=0;
}


void mousePressed(){
  CoordinateMouseXY[0]=mouseX(); CoordinateMouseXY[1]=mouseY();  
}

public boolean mouseDraggOn(float x,float y,float w,float h){
  if(ObjetDrag!=null && mouseOn(x,y,w,h)) return true;
  return false;
}
public boolean mouseOn(float x,float y,float w,float h){
  if (mouseOnMenu() && mouseX()<=x+w && mouseX()>=x &&mouseY()<=y+h && mouseY()>=y) return true;
  return false;

}
public boolean mousePressOn(float x,float y,float w,float h){
  if(mousePressed && AcceptEvent && mouseOn(x,y,w,h)) {
    AcceptEvent=false; 
    return true;
  }
  return false;

}
//Check if the Menu is not on a hidden place
public boolean mouseOnMenu(){
  if(offMenu) return false;
  for(int i=0;i<CurrentMenu;i++)
     if (mouseX()>=HideMouse[i][0] && mouseX()<=HideMouse[i][0]+HideMouse[i][2] && mouseY()>=HideMouse[i][1] && mouseY()<=HideMouse[i][1]+HideMouse[i][3]) return false; 
  if(ExternWindow!=null)  
     if (mouseX()>=ExternWindow[0] && mouseX()<=ExternWindow[0]+ExternWindow[2] && mouseY()>=ExternWindow[1] && mouseY()<=ExternWindow[1]+ExternWindow[3]) return false; 
  if(BlockMenuDefile!=null){
    if (mouseX()>=BlockMenuDefile[0] && mouseX()<=BlockMenuDefile[0]+BlockMenuDefile[2] && mouseY()>=BlockMenuDefile[1] && mouseY()<=BlockMenuDefile[1]+BlockMenuDefile[3]) return false; else{BlockMenuDefile=null;  }
  }
  
 return true;
}

   

//////////////////////// MOUSE WHEEL ////////////////////////////////////////////////////////////////////////
int not=5;
void initMouse(){
   addMouseWheelListener(new java.awt.event.MouseWheelListener() { 
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) { 
      int notches = evt.getWheelRotation(); 
      //Genes List 
      if(mouseX()>OrginMenuXY[0][0] && mouseX()<OrginMenuXY[0][0]+SizeDrawGene && mouseY()>OrginMenuXY[0][1]+50 && mouseY()<OrginMenuXY[0][1]+SizeMenuXY[0][1]){
        if(SizeMenuXY[0][1]-50<Genes.size()*25){
           if(notches>0) ShiftGenesY-=not; else ShiftGenesY+=not;
           if(ShiftGenesY>0) ShiftGenesY=0; 
           else if(ShiftGenesY<SizeMenuXY[0][1]-Genes.size()*25-50) ShiftGenesY=SizeMenuXY[0][1]-Genes.size()*25-50;
        }else ShiftGenesY=0;
      }
      //Domain List
      if(mouseX()>OrginMenuXY[1][0] && mouseX()<OrginMenuXY[1][0]+SizeDrawDomain && mouseY()>OrginMenuXY[1][1]+50 && mouseY()<OrginMenuXY[1][1]+SizeMenuXY[1][1]){
        if(SizeMenuXY[1][1]-50<Domains.size()*25){
          if(notches>0) ShiftDomainsY-=not; else ShiftDomainsY+=not;
          if(ShiftDomainsY>0) ShiftDomainsY=0; 
          else if(ShiftDomainsY<SizeMenuXY[1][1]-50-Domains.size()*25) ShiftDomainsY=SizeMenuXY[1][1]-50-Domains.size()*25;
        }else ShiftDomainsY=0;
      } 
    }
  }
  ); 
}

int mouseX(){if(Scale==1) return mouseX; return round(mouseX/Scale);}
int mouseY(){if(Scale==1) return mouseY; return round(mouseY/Scale);}


//Return true when it's a double click
public boolean doubleClick(){return mouseEvent.getClickCount()==2;}


public void bigMessage(String mes){bigMessage(mes,width/2,height/2);}
public void bigMessage(String mes,float x,float y){ 
  fill(Textcolor); textFont(myFont,36); text(mes,x,y+30);
  image(logo,x-45,y,40,40);textFont(myFont,12); 
}

///FUNCTION MESSAGE / ALERT
public void alert(String mes){ JOptionPane.showMessageDialog(null,mes);}
//FUNCTION ASK QUESTION CONFIRM YES OR NO
public boolean confirm(String mess){
  int Ans=JOptionPane.showConfirmDialog(null,mess,"GRN Boolean Model",JOptionPane.YES_NO_OPTION); //0 -> Yes, 1 No
  return Ans==0;  
}
//FUNCTION ASK STRIN INPUT
public String ask(String mess){
   String Name= JOptionPane.showInputDialog(mess); 
   if(Name!=null && !Name.equals("")) return Name;
   return null;
}
public String ask(String mess,String defaultMess){
  String Name= JOptionPane.showInputDialog(mess,defaultMess); 
   if(Name!=null && !Name.equals("")) return Name;
   return null;
}


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

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MenuDomains.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

import java.util.List;

public class DomainMenuManager {

  private GRNBoolModel p;
  
  DomainMenuManager(GRNBoolModel p) {
    this.p = p;   
  }

  final static int NUM_INCOMPLETE_DOMAINS_IN_DIALOG = 5;

  ///////////////////////////////////////////////  LIST  DOMAINS 
  void MenuDomains(int ligneX,int ligneY,int NumMenu){
    
    p.mm.MenuOnglet(ligneX,ligneY,NumMenu,"Domains");
    
    ligneX+=10; ligneY+=30;
  
    
   //Draw the list of domaines
    p.textFont(p.cm.myFont, 12); 
    for(int i=0;i<p.dm.nbDomain;i++){
       Domain dom=(Domain)p.dm.Domains.get(i);
       int coordY=ligneY+i*25+p.mm.ShiftDomainsY;
        if(coordY>=ligneY && coordY<ligneY+p.mm.SizeMenuXY[1][1]-50){
            dom.draw(ligneX+5,coordY,p.cm.colorDomain);
            if(p.eh.mousePressOn(ligneX+5,coordY,p.dm.SizeDrawDomain,20)) {
               if(p.doubleClick()){
                 //String Name=p.eh.ask("Give a new name to "+dom.Name);
                 //if(Name!=null) dom.Name=Name;
                 renameDomain(dom);
              }
              else{//Draw the domain
                  p.ObjetDrag=new Objet(p, dom); 
                  p.mm.MenuDragActive=1; p.mm.DragMod=0;
                  if(p.lm.MyModel!=null) p.lm.MyModel.ActiveDomain=p.lm.MyModel.getDomain(dom);
            }
            }
        }
    }
    
     if(p.mm.MenuActive[NumMenu]==1) {
        //Draw "R" Domain
        p.dm.GenericDomain.draw(ligneX+70,ligneY-30,p.cm.colorDomain,-1);
        if(p.eh.mousePressOn(ligneX+70,ligneY-30,15,20)) {  p.ObjetDrag=new Objet(p, p.dm.GenericDomain);  p.mm.MenuDragActive=1; p.mm.DragMod=0; }
        MenuDefinitionDomain(ligneX,ligneY-25); //Domain Definition
      }
      
      
     //Scroll Bar
     int sizeBar=p.mm.SizeMenuXY[NumMenu][1]-40;
    float ratio=p.dm.Domains.size()*25/(float)sizeBar;
    if(ratio>1){
       float sizeBarr=sizeBar/ratio;
       int barV=100;
       float yl=ligneY+p.mm.ShiftDomainsY*(sizeBar-sizeBarr)/(sizeBar-p.dm.Domains.size()*25);
       if(p.eh.mouseOn(ligneX-8,yl, 10,sizeBarr)){
           barV=250;
           if(p.mousePressed && p.eh.AcceptEvent){   p.eh.AcceptEvent=false; p.mm.scrollMenu=NumMenu;p.mm.oldScroll=p.mm.ShiftDomainsY; }
       }
       if(p.mm.scrollMenu==NumMenu)  barV=250;
       p.fill(p.cm.colorOnglet,barV);  p.rect(ligneX-8,yl, 10,sizeBarr); //Draw 
    }
    
  }

  /*
   * Displays a dialog for renaming a domain. Normalizes the new name
   * and checks that the name is not already in use.
   */
  private void renameDomain(Domain domain) {
    String newName = p.eh.ask("Give a new name to " + domain.Name);

    if (newName == null) {
      return;
    }

    if (newName.equals(domain.Name)) {
      p.mm.addMessage("Same name given for renaming domain \'" + domain.Name + "\'");
      return;
    }

    // Trim and truncate whitespace. This name will be used for the domain
    // if it is not in use already.
    newName = p.dm.trimDomainName(newName);

    String duplicate = p.dm.getDuplicateDomainName(newName);

    if (duplicate != null) {
      p.eh.alert("\'" + newName + "\' is already in use by domain \'" + duplicate + "\'");
      return;
    }

    p.mm.addMessage("Renaming domain \'" + domain.Name + "\' to \'" + newName + "\'");
    domain.Name = newName;
  }

  /*
   * Displays a dialog asking for a new domain name. If a domain does not already exist with
   * the given name, a new domain is added to the model.
   */
  public void addDomainFromMenu() {
    String name=p.eh.ask("Give a name ");

    if (name == null) {
      return;
    }

    // Trim and truncate whitespace. This name will be used for the domain
    // if it is not in use already.
    name = p.dm.trimDomainName(name);

    String duplicate = p.dm.getDuplicateDomainName(name);

    if (duplicate != null) {
      p.eh.alert("The domain \'"+ name + "\' already exist as domain \'" + duplicate + "\'");
      return;
    }

    Domain domain = new Domain(p, name);
    p.dm.addDomain(domain);
  }


  ///////////////////////////////////////////////   DOMAIN DEFINITION
  public int MenuDefinitionDomain(int ligneX,int ligneY){
   if(p.mm.mouseUnHide(ligneX+100,ligneY-2,15,5,"Add a new domain"))  {
     addDomainFromMenu();
    }
  
    ligneX+=170;
    
    //Draw the Definition Box
    p.mm.LigneDomDefBoxX=ligneX;  p.mm.LigneDomDefBoxY=ligneY;
  
   if(p.dm.DomainDef!=null){
     p.fill(p.cm.Textcolor);  p.textFont(p.cm.myFont, 24);  p.text(p.dm.DomainDef.Name,ligneX-10,ligneY+20);
     p.textFont(p.cm.myFont, 12); 
     ligneY+=70;
    //Tree Definition
   //  ligneX+=SizeDrawDomain+50; 
    int ix=0;
     if(p.rm.Regions.size()>0){
         p.fill(p.cm.Textcolor);  p.text("Domain Ancestors",ligneX,ligneY-15);
         int SizeTree=p.max(p.dm.DomainDef.getTreeSize(),50); 
         
         if(p.eh.mouseOn(ligneX-5,ligneY-5,SizeTree+10,30)) p.stroke(p.cm.colorButton); else p.stroke(p.cm.colorBoxBackground); 
         p.noFill(); p.rect(ligneX-5,ligneY-5,SizeTree+10,30);   
         for(int i=0;i<p.dm.DomainDef.Tree.size();i++){
            Region reg=p.dm.DomainDef.getTree(i);
            float tx=p.textWidth(reg.Name);
            p.fill(p.cm.colorBoxBackground,50);     p.noStroke();  p.rect(ligneX+ix+1,ligneY,tx+2,20);
            p.fill(p.cm.Textcolor);  p.text(reg.Name,ligneX+ix+2,ligneY+14);
            if(p.eh.mousePressOn(ligneX+ix+1,ligneY,tx+2,20)){   p.ObjetDrag=new Objet(p, reg);  p.mm.MenuDragActive=1;  p.mm.DragMod=2; } //Drag Obj
            ix+=tx+4;
         }
     }
     
     
       //Draw the domain Spatial Definition
      ligneY+=70;
      
      p.fill(p.cm.Textcolor);  p.text("Definition",ligneX,ligneY-15);
      
     int SizeDef=0;
     if(p.dm.DomainDef.DefObjets!=null)  {  
       SizeDef=p.dm.DomainDef.DefObjets.length;
       for(int i=0;i<SizeDef;i++){
          p.fill(p.cm.colorBoxBackground,50);    p.stroke(p.cm.colorBoxBackground);  p.rect(ligneX,ligneY-5+i*30,p.dm.SizeDrawDomain+p.pm.SizeDrawOperator+30+4*4,30);
          Objet[] objets=p.dm.DomainDef.DefObjets[i];
           ix=0;
          if(objets!=null)
            for(int j=0;j< objets.length;j++){
              int siz=p.dm.SizeDrawDomain;
              if(objets[j]!=null)  {
                    if(j==0) { 
                        siz=p.pm.SizeDrawOperator+30;
                        objets[j].draw(ligneX+ix+5+j*4,ligneY+i*30,siz);
                        Operator op=objets[j].getOperator();
                        int Middle=ligneX+ix+5+j*4+siz/2-p.round(p.textWidth(op.toRule())+4)/2+p.round(p.textWidth(op.getName())+4); //Begining of the word
                        if(p.eh.mouseOn(Middle-2,ligneY+i*30,p.textWidth(""+op.hmin)+4,20)) { 
                              if(p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; op.hmin++; if(op.hmin>30)op.hmin=0; }
                              if(p.keyPressed){
                                if(p.keyCode==p.UP)    op.hmin++;
                                if(p.keyCode==p.DOWN)  op.hmin--;
                              }
                              if(op.hmin<0)op.hmin=0;
                              
                         }
                        else if(p.eh.mouseOn(Middle+p.textWidth(""+op.hmin+"-")-2,ligneY+i*30,p.textWidth(""+op.hmax)+4,20)){ 
                             if(p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; op.hmax++; if(op.hmax>30)op.hmax=1;  }
                             if(p.keyPressed){
                                if(p.keyCode==p.UP)    op.hmax++;
                                if(p.keyCode==p.DOWN)  op.hmax--;
                              }
                             if(op.hmax<0)op.hmax=0;
                        }
                    }
                    else objets[j].draw(ligneX+ix+5+j*4,ligneY+i*30);
              }
             ix+=siz;
            }
        
       }
     }
     
    //Delete a Definition Domain
     ix=0;
     while(p.dm.DomainDef.DefObjets!=null && ix<p.dm.DomainDef.DefObjets.length){
          if(p.mm.mouseHide(ligneX-20,ligneY+ix*30+8,13,4,"Delete this definition"))  { p.dm.DomainDef.delDef(ix);  ix++; }
          ix++;
      } 
      if(p.mm.mouseUnHide(ligneX-22,ligneY+SizeDef*30,15,5,"Add a new definition"))   p.dm.DomainDef.addEmptyDef();
     
     
      p.mm.ResizeMenuXY(1,0,400);
      if(p.dm.DomainDef.DefObjets!=null) p.mm.ResizeMenuXY(1,1,200+p.dm.DomainDef.DefObjets.length*30);
     }

   return ligneY+20;
  }

  /*
   * Displays an error dialog which indicates that domains have incomplete definitions.
   * The incomplete domains are included as a list in the dialog. The list has at most
   * NUM_INCOMPLETE_DOMAINS_IN_DIALOG entries.
   *
   */
  void displayIncompleteDomainDefsErrorDialog() {
    List<Domain> incompleteDomains = p.dm.getIncompleteDomains();

    String message =
        "The model cannot be saved, because the following domains contain incomplete\n" +
        "domain definitions:\n\n";

    int limit = incompleteDomains.size();
    if (limit > NUM_INCOMPLETE_DOMAINS_IN_DIALOG) {
      limit = NUM_INCOMPLETE_DOMAINS_IN_DIALOG;
    }

    for (int i = 0; i < limit; i++) {
      Domain domain = incompleteDomains.get(i);
      message += "\'" + domain.Name + "\'\n";
    }

    p.eh.alert(message);
  }
}

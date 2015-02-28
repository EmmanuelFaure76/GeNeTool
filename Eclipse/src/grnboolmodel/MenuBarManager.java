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

import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Event;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MenuBar.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public class MenuBarManager {
  
  private GRNBoolModel p;
  
  MenuBarManager(GRNBoolModel p) {
    this.p = p;   
  }  
   
  Menu mF,mE,mW;
  MenuItem mOF,mSF,mSFA,mFD,mFNS; //FILE
  MenuItem mERT,mERP,mEMC,mEMP,mEDP,mECP; //EXPORT
  MenuItem mWC,mWB; //WINDOWS
  CheckboxMenuItem mWTr,mWCom,mWNa,mWDa,mWEx,mWOp,mWMo,mWDo,mWGe,mWCo,mWSe; //WINDOWS Check windows box
  
  void initMenuBar(){
    MenuBar mb = new MenuBar();  
    mb.add(mF = new Menu("File"));
    mF.add(mOF = new RedirectingMenuItem(p,"Open Model", new MenuShortcut( KeyEvent.VK_O )));  
    mF.add(mSF = new RedirectingMenuItem(p,"Save Model", new MenuShortcut( KeyEvent.VK_S )));  mSF.setEnabled(false);
    mF.add(mSFA = new RedirectingMenuItem(p,"Save Model as", null));  mSFA.setEnabled(false);
    mF.addSeparator();
    mF.add(mFD = new RedirectingMenuItem(p,"Load Data", null));  mFD.setEnabled(false);
    mF.add(mFNS = new RedirectingMenuItem(p,"Load NanoString", null));  mFNS.setEnabled(false);
  
    
    
     mb.add(mE = new Menu("Export"));
     mE.add(mERT = new RedirectingMenuItem(p,"Rules in TXT", null)); mERT.setEnabled(false);
     mE.add(mERP = new RedirectingMenuItem(p,"Rules in PDF", null)); mERP.setEnabled(false);
     mE.addSeparator();
     mE.add(mEMC = new RedirectingMenuItem(p,"Model in CSV", null)); mEMC.setEnabled(false);
     mE.add(mEMP = new RedirectingMenuItem(p,"Model in PDF", null)); mEMP.setEnabled(false);
     mE.addSeparator();
     mE.add(mEDP = new RedirectingMenuItem(p,"DATA in PDF", null)); mEDP.setEnabled(false);
     mE.add(mECP = new RedirectingMenuItem(p,"Compare in PDF", null)); mECP.setEnabled(false);
     
     mb.add(mW = new Menu("Windows"));
     mW.add(mWSe = new RedirectingCheckboxMenuItem(p,"Search...", false)); mWSe.setEnabled(false);
     mW.add(mWCo = new RedirectingCheckboxMenuItem(p,"Colors", false));
     mW.addSeparator();
     mW.add(mWC = new RedirectingMenuItem(p,"Cascade", null));
     mW.add(mWB = new RedirectingMenuItem(p,"Best", null));
     mW.addSeparator();
     mW.add(mWGe = new RedirectingCheckboxMenuItem(p,"Genes", p.mm.MenuActive[0]==1)); 
     mW.add(mWDo = new RedirectingCheckboxMenuItem(p,"Domains", p.mm.MenuActive[1]==1)); 
     mW.add(mWMo = new RedirectingCheckboxMenuItem(p,"Model", p.mm.MenuActive[2]==1)); mWMo.setEnabled(false);
     mW.add(mWOp = new RedirectingCheckboxMenuItem(p,"Operators", p.mm.MenuActive[3]==1)); mWOp.setEnabled(false);
     mW.add(mWEx = new RedirectingCheckboxMenuItem(p,"Expression", p.mm.MenuActive[4]==1)); mWEx.setEnabled(false);
     mW.add(mWDa = new RedirectingCheckboxMenuItem(p,"Data", p.mm.MenuActive[5]==1)); mWDa.setEnabled(false);
     mW.add(mWNa = new RedirectingCheckboxMenuItem(p,"NanoString", p.mm.MenuActive[6]==1));mWNa.setEnabled(false);
     mW.add(mWCom = new RedirectingCheckboxMenuItem(p,"Compare", p.mm.MenuActive[7]==1)); mWCom.setEnabled(false);
     mW.add(mWTr = new RedirectingCheckboxMenuItem(p,"Tree", p.mm.MenuActive[8]==1)); 
  
          
    p.frame.setMenuBar(mb);
  }
  
  public boolean checkEnablelMenu(){
	boolean modelInit = false;
    if(p.gm.nbGene>0 || p.dm.nbDomain>0) {  //START WITH GENES OR DOMAINS TO SAVE SOMETHING
        mWSe.setEnabled(true);
        mERT.setEnabled(true);
        mERP.setEnabled(true);
        mSF.setEnabled(true);
        mSFA.setEnabled(true);
        if(!p.mm.isActive("operator")) p.mm.active("operator",0); 
    }else{ //Nothing has been created (or delete)
      p.mm.desActive("operator");p.mm.desActive("expression");p.mm.desActive("data");p.mm.desActive("model"); p.mm.desActive("nanostring");p.mm.desActive("compare");
       mWSe.setEnabled(false);
       mERT.setEnabled(false);
       mERP.setEnabled(false);
       mSF.setEnabled(false);
       mSFA.setEnabled(false);
    }
    if(p.rm.Regions.size()==0 || p.rm.MaxTime==0){  p.mm.desActive("expression");p.mm.desActive("data"); p.mm.desActive("nanostring");  }
    else if(p.gm.nbGene>0 && p.rm.Regions.size()>0) {
        if(!p.mm.isActive("expression")) p.mm.inActive("expression");  
    } 
    if(p.dm.nbDomain>0 && p.gm.nbGene>0 ){
         if(!p.mm.isActive("model")) {
        	 p.lm.initializeModel();
        	 modelInit = true;
        	 p.mm.inActive("model");
         }  
         if(p.rm.MaxTime>0 && !p.mm.isActive("data")){ p.mm.inActive("data"); p.mm.inActive("compare");  mFNS.setEnabled(true);}
    }
    boolean is=p.mm.isActive("data");
    mEDP.setEnabled(is);
    mECP.setEnabled(is);
    mFD.setEnabled(is);
    mFNS.setEnabled(is);
    
    
    is=p.mm.isActive("model");
    mEMC.setEnabled(is);
    mEMP.setEnabled(is);
    return (modelInit);
  }
  
  public boolean handleAppletAction(Event me, Object arg) {
    ///////////////// FILE ///////////////////////////
    if (me.target == mOF) {  p.toDoMenu=1; return true;  }   //OPEN AN XML FILE
    if (me.target == mSF) {  p.toDoMenu=2; return true;  }  //SAVE CURRENT MODEL AN XML FILE
    if (me.target == mSFA){  p.toDoMenu=3; return true;  } //SAVE AS  CURRENT MODEL AN XML FILE
    if (me.target == mFD){  p.toDoMenu=4; return true;  } //LOAD DATA
    if (me.target == mFNS){  p.toDoMenu=5; return true;  } //LOAD NANOSTRING
    
    ///////////////// EXPORT ///////////////////////////
    if (me.target == mERT) {  p.toDoMenu=10; return true;  }
    if (me.target == mERP) {  p.toDoMenu=11; return true;  }
    if (me.target == mEMC) {  p.toDoMenu=12; return true;  }
    if (me.target == mEMP) {  p.toDoMenu=13; return true;  }
    if (me.target == mEDP) {  p.toDoMenu=14; return true;  }
    if (me.target == mECP) {  p.toDoMenu=15; return true;  }
    
     ///////////////// WIDNOWS ///////////////////////////
     if (me.target == mWSe) {  p.smm.changeMenuSearch(); return true;  } //Search Menu
     if (me.target == mWCo) {  p.cm.changeMenuColor(); return true;  } //COLORS
      
    if (me.target == mWC) { p.toDoMenu=20; return true;  } //OPEN ALL WINDOWS IN CASCADE
    if (me.target == mWB) {  p.mm.bestMenuOrder(); return true;  } //OPEN THE BES T CONFIGURATION IN CASCADE
  
    if (me.target == mWTr) {enableWindows(8); return true;  } //TREE
    if (me.target == mWCom) {enableWindows(7); return true;  } //COMPARE
    if (me.target == mWNa) {enableWindows(6); return true;  } //NANOSTRING
    if (me.target == mWDa) {enableWindows(5); return true;  } //DATA
    if (me.target == mWEx) {enableWindows(4); return true;  } //EXRESSION
    if (me.target == mWOp) {enableWindows(3); return true;  } //OPERATORS
    if (me.target == mWMo) {enableWindows(2); return true;  } //MODEL
    if (me.target == mWDo) {enableWindows(1); return true;  } //DOMAINS
    if (me.target == mWGe) {enableWindows(0); return true;  } //GENES
    
    return (false); 

  }
  
  
  public void activeItem(String name,int s){activeItem(p.mm.getNumMenu(name),s);}
  public void activeItem(int numMenu,int s){
    boolean state=s==1; boolean act=s>=0;
    switch(numMenu){
      case 0 : mWGe.setEnabled(act); mWGe.setState(state); break;
      case 1 : mWDo.setEnabled(act); mWDo.setState(state); break;
      case 2 : mWMo.setEnabled(act); mWMo.setState(state); break;
      case 3 : mWOp.setEnabled(act); mWOp.setState(state); break;
      case 4 : mWEx.setEnabled(act); mWEx.setState(state); break;
      case 5 : mWDa.setEnabled(act); mWDa.setState(state); break;
      case 6 : mWNa.setEnabled(act); mWNa.setState(state); break;
      case 7 : mWCom.setEnabled(act); mWCom.setState(state); break;
      case 8 : mWTr.setEnabled(act); mWTr.setState(state); break;
    }
  }
      
  public void enableWindows(int numMenu){
    if(p.mm.MenuActive[numMenu]==1) {
        p.mm.active(numMenu,0);
        p.mm.InvReOrderMenu(numMenu);
    }else{
      p.mm.active(numMenu,1);
      p.mm.ReOrderMenu(numMenu);
    }
  }
  
  
  public class RedirectingMenuItem extends MenuItem {
    private Component event_handler;
    public RedirectingMenuItem(Component event_handler, String label, MenuShortcut hotkey) {
      super(label,hotkey);    
      this.event_handler = event_handler;
    }
    public boolean postEvent(Event e) {
      if (event_handler.isValid()) return event_handler.postEvent(e);
      else return false;
    }
  }
  
  
  
  public class RedirectingCheckboxMenuItem extends CheckboxMenuItem {
    private Component event_handler;
    public RedirectingCheckboxMenuItem(Component event_handler, String label, boolean state) {
      super(label,state);    
      this.event_handler = event_handler;
    }
    public boolean postEvent(Event e) {
     // alert("ok"+event_handler.isValid()+":"+e);
      //if (event_handler.isValid()) 
      return event_handler.postEvent(e);
      //else return false;
    }
  }
  
  
}

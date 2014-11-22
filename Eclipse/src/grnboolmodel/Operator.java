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

import processing.core.PApplet;



public class Operator{
  String Name; //Name of the Operator
  boolean change=false; //In case of click to change it
  int comp=0;
  public int step=-1; //For Temporal Operator -> " How many Step after at .."
  public int hmin=-1; // For Minium Hours for CC & NCC
  public int hmax=-1; //For Maximum Hours CC & NCC
  GRNBoolModel p;
  
  Operator(GRNBoolModel p, String Name,int nb){  this.p = p; this.Name=Name; this.comp=nb; }
  public Operator(Operator o){  this.p = o.p; this.Name=o.Name;  this.comp=o.comp;  }
 
  public void invChange(){this.change=!this.change;}
  public String getName(){return this.Name;}
  public boolean is(String name){   
        if(comp==3){//NCC CC etc .. when there is hours 
             int sizeName=this.Name.length();
             if(name.length()>=sizeName) name=name.substring(0,sizeName);
             return p.uf.equal(this.Name,name);
        }
        return p.uf.equal(this.toRule(),name);   
 }
  public String toString(){     return this.getName();      }
  public String toRule(){   
       String NamePrint=Name; 
       if(comp==4 && step>=0)NamePrint=Name+"-"+step;  //For Temporal Operator 
       if(comp==5 && step>=0) NamePrint=Name+"-"+step;  //For PERM Operators
       if(comp==6 && step>=0) NamePrint=Name+step;  //For HPF Operators
       if(comp==3 && hmin>=0 && hmax>0) NamePrint=Name+"_"+hmin+"-"+hmax; //For Specifi CC NCC Operator
       return NamePrint;
  }  
  //For Double Logic
  boolean applyDOUBLE(boolean v1,boolean v2){
    if(Name.equals("AND")) {   if(v1 && v2) return true;   else return false; }
    if(Name.equals("OR")) {  if(v1) return true; if(v2) return true;  return false; } 
    if(Name.equals("NAND")) {  if(!v1 && !v2) return true;  else return false;  }
    if(Name.equals("XOR")) {  if( (v1 && !v2) || (v2 && !v1)) return true; else return false;  } 
    p.mm.addMessage("ERROR Apply Double "+Name);
    return false;
  } 
  
  //For Unit Logic
  boolean applySIMPLE(boolean v1){
    if(Name.equals("LINEAR")) return v1; 
    if(Name.equals("NOT"))  return !v1;
    p.mm.addMessage("ERROR Apply Single "+Name);
    return false;
  }
  
  
  
  //For IN Logic: IN R or IN CC R (apply to the current Gene)
  boolean applyIN(Gene gene,Logic l,ModelDomain md,int steps){
    if(Name.equals("IN")){     
      
      Domain dop=null; //TAR GET DOMAIN
      Logic Templ=l;
      while(p.pm.isLogicOne(Templ)) Templ=((LogicOne)Templ).l1;  //Possible Multiple  Single Logic include before
      if(p.pm.isLogicDomain(Templ)) dop=((LogicDomain)Templ).dom;  //IN R 
      if(dop==null) return false; //Found no Domain
      
      if(dop==p.dm.GenericDomain)  dop=md.dom; //Generic domain Substitute
      if(p.pm.isLogicOne(l)){ //Contains CC or NCC
          if(steps==0) return false; //First Case no check
          Operator top=((LogicOne)l).o;
          ArrayList ListDomains=top.getCorrespondDomain(dop); //Return the domains 
          if(ListDomains==null) { p.mm.addMessage("There is no Domain corresponding to this Operator "); return false; } 
          for(int i=0;i<ListDomains.size();i++){ //We check in all the CC or NCC domains if in the previous step we have a value at initValue
            Domain ndom=(Domain)ListDomains.get(i);
            if(ndom==md.dom){
                int [] Hours=top.getHoursCoorespond(dop,ndom);
                if(p.lm.MyModel.Correspons[steps]>=Hours[0] && p.lm.MyModel.Correspons[steps]<=Hours[1])  return true;
            }
          }
          return false;
      }
      //DIRECT IN R OR IN DOMAIN
      if(dop==md.dom) return true;
      return false;
    }
    p.mm.addMessage("ERROR Apply operator need to be IN instead of " +this.Name);
    return false;
  }
  
  //For IN Logic: X IN R or X IN CC R
  boolean applyXIN(Logic l1,Logic l2,Gene gene,ModelDomain md,int steps){
    if(Name.equals("IN")){
      Gene applyGene=null;
      Logic Templ=l1;
      boolean initValue=false; boolean inverseValue=false;
      int ATStep=0; //In Case we have a AT X IN CC R 
      int AFTERStep=0; //In Case we have a AFTER X IN CC R  
      while(p.pm.isLogicOne(Templ))  {  //Possible Multiple  Single Logic include before 
            Operator o=((LogicOne)Templ).o;
            if(o.getName().equals("NOT")) inverseValue=!inverseValue; //Inverse the initial Input
            if(o.getName().equals("AT")) ATStep=o.step; 
            if(o.getName().equals("AFTER")) AFTERStep=o.step; 
            Templ =((LogicOne)Templ).l1; 
      }
      if(p.pm.isLogicGene(Templ)){
            applyGene=((LogicGene)Templ).g;  // X 
            initValue=((LogicGene)Templ).v;  //Get the initiale Value of the logic
      }
      if(applyGene==null) return false;
      if(inverseValue) initValue=!initValue;
      
      int NexStep=steps-(ATStep+AFTERStep);
      if(NexStep<=0) return false;
      Domain dop=null; //TARGET DOMAIN
      Templ=l2;
      while(p.pm.isLogicOne(Templ)) Templ=((LogicOne)Templ).l1;  //Possible Multiple  Single Logic include before
      if(p.pm.isLogicDomain(Templ)) dop=((LogicDomain)Templ).dom;  //IN R 
      if(dop==null) return false;
      
      if(dop==p.dm.GenericDomain)  dop=md.dom; //Generic domain Substitute
      if(p.pm.isLogicOne(l2)){ //Contains CC or NCC
          if(steps==0) return false; //First Case no check
          Operator top=((LogicOne)l2).o;
          ArrayList ListCCRelation=top.getCorrespondDomain(dop);
          //We check in all the CC or NCC domains if in the previous step we have a value at initValue
          if(ListCCRelation==null) return false;// There is no Domain corresponding to this Operator 
          for(int i=0;i<ListCCRelation.size();i++){
            Objet[] defObj=(Objet[])ListCCRelation.get(i);
            if(defObj!=null && defObj[1]!=null && defObj[0]!=null){ 
              Domain ndom=defObj[1].getDomain();
              ModelDomain mddom=p.lm.MyModel.getDomain(ndom);
              Operator o=defObj[0].getOperator();
              int minHours=o.hmin; int maxHours=o.hmax;
              int numGene=p.gm.getNumGene(applyGene);
              if(numGene==-1) { p.mm.addMessage("ERROR in IN CC "+this.Name + " in "+md.getName() + " numGene=="+numGene); return false;}
              if(mddom.GenesStates[numGene][NexStep]==initValue) {//For Each Step chekc if we have a positive correspondance
                  if(p.lm.MyModel.Correspons[steps]>=minHours && p.lm.MyModel.Correspons[steps]<=maxHours+ATStep+AFTERStep)  return true;  //In case we have a AT OR AFTER logic, the validity is still on steps after CC
               }
            }
          }
          return false;
      }
      //DIRECT X IN R OR X IN DOMAIN
      return l1.is(gene,p.lm.MyModel.getDomain(dop),steps,false);
          
    }
    p.mm.addMessage("ERROR Apply operator need to be IN instead of " +this.Name);
    return false;
  }
  
   //Return the domains who are in rule with this operator (in this definition) with this dom
   ArrayList getCorrespondDomain(Domain dom){
    ArrayList ListCCRelation=new ArrayList();
    if(dom.DefObjets==null) return null;
     for(int i=0;i<dom.DefObjets.length;i++){
        Objet oj=dom.DefObjets[i][0];
        if(oj!=null){
            Operator o=oj.getOperator();
            if(o!=null && o.Name.equals(this.Name))  ListCCRelation.add(dom.DefObjets[i]);
        }
      }
    return ListCCRelation;
  }
  //Return the hours with the dom is in the domdest on the operators
  int [] getHoursCoorespond(Domain dom,Domain domdest){
    int []Hours=new int[2];Hours[0]=-1;Hours[1]=-1;
    if(dom.DefObjets==null) return Hours;
    for(int i=0;i<dom.DefObjets.length;i++){
        Operator o=(dom.DefObjets[i][0]).getOperator();
        Domain domD=(dom.DefObjets[i][1]).getDomain();
        if(o!=null && o.Name.equals(Name) && domD==domdest)  {
            Hours[0]=o.hmin; Hours[1]=o.hmax; 
            return Hours;
        }
      }
      return Hours;
  }
  
  
  //For Temporal Logic in hours
  boolean applyTEMP(Logic l,ModelDomain md, int steps,boolean involve){
    ArrayList Genes=l.getGenes();
    if(Genes.size()==1){
       if(p.pm.isLogicGene(l))  {
          LogicGene lg=(LogicGene)l;
          if(lg.logicName.equals("")) return applyTEMPSIMPLE(l,md,steps,involve);       
       }
       if(p.pm.isLogicOne(l))  return applyTEMPSIMPLE(l,md,steps,involve); 
    }
    return applyTEMPMUL(l,md,steps,involve);
  }
  
  
  
  
  
  //For Temporal Logic in hours Same as previous but only with simple gene (FASTER !)
  boolean applyTEMPSIMPLE(Logic l,ModelDomain md, int steps,boolean involve){
   ArrayList Genes=l.getGenes();
    Gene g=(Gene)Genes.get(0);
    boolean value=l.getValue(); 
    int numGene=p.gm.getNumGene(g);
    if(numGene==-1) { p.mm.addMessage("ERROR in  "+this.Name + " in "+md.getName()+ " with "+ l + " numGene=="+numGene + " at step + "+ steps); return false;}
    int lsteps=0; //Last Steps
    int h=p.lm.MyModel.Correspons[steps];

  // if(involve) addMessage("TRY SIMPLE in  "+this.Name + " in "+dom+ " with "+ l + " numGene=="+numGene + " at step + "+ steps); 
   
   if(Name.equals("AFTER")) {
         for(int j=0;j<=steps-this.step;j++){
               if(md.GenesStates[numGene][j]==value){
                     if(involve)p.gm.addGeneInvolve(numGene,j,true);               
                      return true;
               }
         }
         if(involve)p.gm.addGeneInvolve(numGene,steps-this.step,false);   
         return false;
   }
   if(Name.equals("AT")) {
       if(this.step==0 && md.GenesStates[numGene][steps]==value) {
         if(involve)p.gm.addGeneInvolve(numGene,steps,true);   
         return true;
       }
       h=h-this.step;
      for(int j=0;j<=steps;j++)
          if(p.lm.MyModel.Correspons[j]<=h && p.lm.MyModel.Correspons[j+1]>h) {
              lsteps=j;
              if(md.GenesStates[numGene][j]==value){
                  if(involve)p.gm.addGeneInvolve(numGene,j,true); 
                  return true;
               }
           }
        if(involve)p.gm.addGeneInvolve(numGene,lsteps,false); 
        return false;
   }
   
   if(Name.equals("ACCU")){
        if(md.GenesStates[numGene][steps]!=value) return false; //If the gene is not on, they cannot be any accumulation
        int acch=0;
        if(this.step==0 && md.GenesStates[numGene][steps]==value) return true; 
        for(int j=steps;j>=0;j--){
             if(md.GenesStates[numGene][j]==value){
               acch+=p.lm.MyModel.Correspons[j+1]-p.lm.MyModel.Correspons[j];
               if(acch>=this.step) {
                   if(involve)p.gm.addGeneInvolve(numGene,j,true);
                   return true;
               }
             } else return false;
        }
        if(involve)p.gm.addGeneInvolve(numGene,steps,false); 
        return false;
    }

   p.mm.addMessage("ERROR Apply Temporal Operator for "+ this.Name + " doesn't exist");
   return false;
  }
  
  boolean applyTEMPMUL(Logic l,ModelDomain md, int steps,boolean involve){
    ArrayList Genes=l.getGenes();
    Gene g=(Gene)Genes.get(0);
   
   int h=p.lm.MyModel.Correspons[steps];
   int numGene=p.gm.getNumGene(g);
   boolean value=l.getValue();
   int lsteps=0; //The Last steps
  //  if(involve) addMessage("TRY applyTEMPMUL in  "+this.Name + " in "+dom+ " with "+ l + " numGene=="+numGene + " at step + "+ steps); 
  
  
    if(Name.equals("AFTER")) {
         for(int j=0;j<=steps;j++){
                if(h-p.lm.MyModel.Correspons[j]>=this.step) {
                  lsteps=j;
                  if(md.GenesStates[numGene][j]==value && l.is(g,md,j,false)) {
                        if(involve)p.gm.addGeneInvolve(numGene,j,true);                
                        return true;
                  }
                }
         }
         if(involve)p.gm.addGeneInvolve(numGene,lsteps,false); 
         return false;
   }
   if(Name.equals("AT")) {
       if(this.step==0 && md.GenesStates[numGene][steps]==value && l.is(g,md,steps,false)) {
        if(involve)p.gm.addGeneInvolve(numGene,steps,true);
         return true;
       }
       h=h-this.step;
        
        for(int j=0;j<=steps;j++){
             //if(involve) addMessage("Test  h="+h+ " MyModel.Correspons[j]="+MyModel.Correspons[j] );
              if(p.lm.MyModel.Correspons[j]<=h && p.lm.MyModel.Correspons[j+1]>h) {
               //  if(involve)  addMessage("Logic="+l.toString()+" h="+h+ " MyModel.Correspons[j]="+MyModel.Correspons[j] +" MyModel.Correspons[j+1]="+MyModel.Correspons[j+1] + " l.is(g,dom,j)="+l.is(g,dom,j));
                lsteps=j;
                if(md.GenesStates[numGene][j]==value && l.is(g,md,j,false)) {
                    if(involve)p.gm.addGeneInvolve(numGene,j,true); 
                    return true;
                }
              }
        }
        if(involve)p.gm.addGeneInvolve(numGene,lsteps,false); 
        return false;
   }
   
   if(Name.equals("ACCU")){
        if(!l.is(g,md,steps,false)) return false; //If the gene is not on, they cannot be any accumulation
        if(this.step==0) return true; 
        int acch=0;
        for(int j=steps;j>=0;j--){
             if(md.GenesStates[numGene][j]==value && l.is(g,md,j,false)) {
               acch+=p.lm.MyModel.Correspons[j+1]-p.lm.MyModel.Correspons[j];
               if(acch>=this.step) {
                     if(involve)p.gm.addGeneInvolve(numGene,j,true); 
                     return true;
               }
             } else return false;
        }
        if(involve)p.gm.addGeneInvolve(numGene,steps,false); 
        return false;
    }

   p.mm.addMessage("ERROR Apply Temporal Operator for "+ this.Name + " doesn't exist");
   return false;
  }
  
  
  
  //Hpf Logic > or > 
  public boolean applyHPF(Gene gene, ModelDomain md, int steps){
      int h=p.lm.MyModel.Correspons[steps];
    //  if(involve) addMessage("Cooresponding Hours is " + h  + " at " + steps);
     if(this.Name.equals(">") &&  this.step<h) return true;  
     if(this.Name.equals("<") &&  this.step>h) return true;   
     return false;
  }

  public boolean applySTRONG(Gene gene, Logic l,ModelDomain md, int steps,boolean involve){
    if(Name.equals("PERM")) { //Permanent 
         ArrayList Genes=l.getGenes();
         if(Genes.size()!=1) {  //There is no Gene in this Logic, perahps it is a LogicLogic including a MetaLogic
               Logic Templ=l;
               boolean inverseValue=false;
               while(p.pm.isLogicOne(Templ))  {  //Possible Multiple  Single Logic include before 
                  Operator o=((LogicOne)Templ).o;
                  if(o.getName().equals("NOT")) inverseValue=!inverseValue; //Inverse the initial Input
                  Templ =((LogicOne)Templ).l1; 
               }
             
              if(p.pm.isLogicLogic(Templ))  {  //Get the LogicLogic
                  LogicLogic lo=(LogicLogic)Templ;
                  boolean initValue=lo.v; if(inverseValue) initValue=!initValue;
                  MetaLogic Mlo=(MetaLogic)lo.l1;
                  int exist=0; 
                  for(int s=0;s<=steps-this.step;s++){ //We need to verify in each Previous Step how as this Meta Logic !
                        boolean valueStep=Mlo.is(gene,md,s,false);
                        if(valueStep==!initValue) { if(exist==0) exist=1;}
                        else{ if(exist==1) exist=2;  }      
                    }
                    return !(exist==2);
              }
              else
                if(p.pm.isLogicTwo(Templ))  {// Permanent Co-Repression
                      for(int s=0;s<=steps-this.step;s++){ //We need to verify in each Previous Step how as this Meta Logic !
                            boolean valueStep=Templ.is(gene,md,s,false);
                            if(valueStep==true) return false;  
                      }
                      return true;
                }
                else{
                  p.mm.addMessage("ERROR in  PERM Definition "+this.Name + " in "+md.getName()+ " with "+ l + " there is no Genes or MetaLogic inside -> "+Templ.getClass().getName() ); 
                  return false;
                }
         }
         Gene g=(Gene)Genes.get(0);
         boolean value=l.getValue();
         int exist=0; 
         int numGene=p.gm.getNumGene(g);
         int represseSteps=steps-this.step;
        if(gene==g)  represseSteps--; //We cannot take in account the actual step for the autorepression  //Repress by it stelf
         
       if(numGene==-1) { p.mm.addMessage("ERROR in PERM "+this.Name + " in "+md.getName()+ " with "+ l + " numGene=="+numGene); return false;}
         for(int j=0;j<=represseSteps;j++){
              if(md.GenesStates[numGene][j]==!value) { if(exist==0) exist=1;}
              else{ 
                if(exist==1) {
                    exist=2;
                    //if(involve)addGeneInvolve(numGene,j,true);  //Repression ..
                 }
             }             
         }
        //if(!(exist==2) && involve)addGeneInvolve(numGene,steps,false); 
         return !(exist==2);
    }
    return false;
    
  }
  
  public int size(){ return p.round(p.textWidth(this.toRule())+4);  }
  public void draw(int x,int y,int c){ draw(x,y,c,p.pm.SizeDrawOperator);}
   public void draw(int x,int y,int c, int siz){
    /*String NamePrint=this.toRule();
    if(siz==-1)  siz=this.size();   //When size need to be the exact size of the test
    if(mouseOn(x,y,siz,20)){
          fill(c,ActiveColor);   
          if((this.comp==4 || this.comp==5 || this.comp==6) && keyPressed){  
            if(keyCode==UP)    this.step++;
            if(keyCode==DOWN)  this.step--;
            if(this.step<0) this.step=0;
          }
    } else fill(c,InactiveColor);
    stroke(c,255);noStroke();   rect(x,y,siz,20);
    fill(Textcolor);   text(NamePrint,x+siz/2-textWidth(NamePrint)/2,y+14);*/
     p.noStroke();  
     String NamePrint=this.toRule();
     if(siz==-1)  siz=this.size();
     p.fill(c,p.mm.InactiveColor); 
     if(p.eh.mouseOn(x,y,siz,20)) p.stroke(p.cm.colorButton);  p.rect(x,y,siz,20);
     x+=siz/2-p.textWidth(NamePrint)/2;
     y+=14;
     p.fill(p.cm.Textcolor); 
     p.text(this.Name,x,y);
     
     x+=p.textWidth(this.Name);
    //Only for active Operators
     if(step>=0 && (this.comp==4 || this.comp==5 || this.comp==6)){
             if(this.comp!=6){p.text("-",x,y);  x+=p.textWidth("-"); }
           float nsiz=p.textWidth(""+step);
           if(p.eh.mouseOn(x,y-14,nsiz,20)){ 
             p.fill(p.cm.colorButton);
             if(p.keyPressed){
                if(p.keyCode==p.UP)    this.step++;
                if(p.keyCode==p.DOWN)  this.step--;
                if(this.step<0) this.step=0;
              }
           }
           p.text(""+step,x,y);
           x+=nsiz;
     }
     if(this.comp==3 && hmin>=0 && hmax>0){
           p.text("_",x,y);  x+=p.textWidth("_"); 
           p.fill(p.cm.Textcolor); 
           float nsiz=p.textWidth(""+hmin);
           if(p.eh.mouseOn(x,y-14,nsiz,20)) p.fill(p.cm.colorButton);
           p.text(""+hmin,x,y);
           x+=nsiz;
           p.fill(p.cm.Textcolor); 
           p.text("-",x,y);  x+=p.textWidth("-"); 
           nsiz=p.textWidth(""+hmax);
           if(p.eh.mouseOn(x,y-14,nsiz,20)) p.fill(p.cm.colorButton);
           p.text(""+hmax,x,y);
           x+=nsiz;
     }
  }   
}


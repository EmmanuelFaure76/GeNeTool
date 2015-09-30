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
import java.util.Iterator;

import processing.core.PApplet;


//////////////////////////////////////////////////////////// CLASS GENE
public class Gene {
  String Name; //Name of the gene
  boolean isGene=true; //The gene is a gene and not a signaling interaction or what else
  boolean isUbiquitous=false; //When the gene is express everywhere at the same time
  boolean isUnknown=false; //When we don't know and ad had hoc information
  boolean isMaternal=false; //To specify if the gene is a maternal input
  Logic [] Logics; //List of imput and repressor/activator
  ArrayList [] Objects; //List of logics implemente in Objects
  int nbLogic;
  boolean init=false; //initilise activation
  public int [][]Expression;
  int []Nano=null; // NanoString xpression
  int MinNaNo=100000000; //Minimum Value of NanoString
  int MaxNaNo=-100000000; //Maximum Value of NanoString
  ArrayList NanoLines=new ArrayList(); //To draw line on the NanoString plot
  GRNBoolModel p;
  //Constructor
  
  public Gene(GRNBoolModel p, String Name){
    this.p = p;
    this.Name=Name;
    this.init();
    this.Expression=new int[p.rm.Regions.size()][p.rm.MaxTime+1]; //30 hours by 21 Regions
    for(int i=0;i<p.rm.Regions.size();i++) for(int j=0;j<=p.rm.MaxTime;j++)  this.Expression[i][j]=-1;
  } 

  
  public void init(){
    Logics=new Logic[p.maxStep];
    Objects=new ArrayList[p.maxStep]; //Maximum of 100 objets in 100 Rules
    for(int i=0;i<p.maxStep;i++) {Objects[i]=new ArrayList(); Logics[i]=null;}
    nbLogic=0;
  }
  


  public String toString(){ return this.Name;  }
  public String toRule(){ return p.uf.replaceString(this.Name," ",""+(char)35);}
  //////////////// Logic
  //Add a new Logic to the Gene
  public void addLogic(String id,String rule,String thens,String elses,boolean blueLogic,boolean dft){
    int NumLogic=nbLogic;nbLogic++;
    Objects[NumLogic]=p.om.DecodeObjects(this,rule);
    Logic CurrentLogic=p.rr.DecodeRule(this.getObjets(NumLogic));
    MetaLogic ml=new MetaLogic(p, CurrentLogic,id,thens,elses,blueLogic);
    ml.dft=dft;
    Logics[NumLogic]=ml;
  }

 //Return the position of the logic 
 int getNumLogic(String name){
   for(int i=0;i<nbLogic;i++){
        if(this.Logics[i].getClassName().equals(p.pkgName + "MetaLogic")){
            MetaLogic logi=(MetaLogic) this.Logics[i];
         if(p.uf.equal(logi.getName(),name)) return i;
        }
   }
   return -1;
 }
 
  //Set a new logic at a specific position
 public void setLogic(int l, Logic logi){
      MetaLogic logic=(MetaLogic)Logics[l];
      logic.logi=logi;
      if(logi==null) logic.valid=false; else logic.valid=true; 
 }
 
 //Set a Logic from a new objets
 boolean setLogic(ArrayList Objets){ 
    for(int i=0;i<nbLogic;i++)
       if(this.Objects[i]==Objets){ this.setLogic(i,p.rr.DecodeRule(Objets));    return true;}
     return false;
 }


 //Return the logic number l
 public Logic getLogic(int l){ return Logics[l];}
 //Return the logic correspond to the name
 Logic getLogic(String name){
   for(int i=0;i<nbLogic;i++)
     if(this.Logics[i]!=null && this.Logics[i].getClassName().equals(p.pkgName + "MetaLogic")){
         MetaLogic logi=(MetaLogic)this.Logics[i];
         if(p.uf.equal(logi.getName(),name)) return this.Logics[i];      
     } //else addMessage("ERROR  that this logic is "+this.Logics[i].getClass());
   return null;
 }
 
 
 //Return the correspond logic to this objects
 MetaLogic getMetaLogic(ArrayList Objets){
   for(int i=0;i<nbLogic;i++)
     if(Objects[i]==Objets) return (MetaLogic)this.Logics[i];
   return null;
 }
 
 //Return Default Logic
 public MetaLogic getDefaultLogic(){
    if(nbLogic==1) return (MetaLogic)this.Logics[0];
     for(int i=0;i<nbLogic;i++){
         MetaLogic logi=(MetaLogic)this.Logics[i];
         if(logi.dft)   return logi;      
     } 
   return null;
 }
 
 //Return Default Logic Name
 public String getDefaultLogicName(){
   if(nbLogic==1) return ((MetaLogic)this.Logics[0]).Name;
   return "";
 }
 
 //Delete a Logic (and the correspond object) from the List 
 public void del(MetaLogic lo){
    boolean found=false;
    for(int i=0;i<nbLogic;i++) {
      //addMessage("Logic at " + i + "="+this.Logics[i]);
      if(lo==this.Logics[i]){ 
          this.Logics[i]=null;found=true;// addMessage("Del " +lo + " at "+i); 
          this.Objects[i]=null;
        }
      else if(found){
        this.Logics[i-1]=this.Logics[i];
        this.Objects[i-1]=this.Objects[i];
      }  
    }
    if(found) { this.Logics[nbLogic-1]=null;this.Objects[nbLogic-1]=null; nbLogic--;}
   }
 


 //////////////// Objets
 //Move a logic from one indice to another
 public void swtich(int linit,int ldest){ 
   
     Logic [] TLogics=new Logic[this.Logics.length]; 
     ArrayList [] TObjects=new ArrayList[this.Logics.length];
     int shift=0;
     if(linit<ldest)ldest++;
     for(int i=0;i<this.Logics.length;i++){
     if(i==linit) shift--; //Logic to move
       else 
       if(i==ldest){
          TLogics[i+shift]=this.Logics[linit];
         TObjects[i+shift]=this.Objects[linit];
         shift++;
         TLogics[i+shift]=this.Logics[i];
         TObjects[i+shift]=this.Objects[i];
         
       }
       else{
         TLogics[i+shift]=this.Logics[i];
         TObjects[i+shift]=this.Objects[i];
       }
    }
    this.Logics=TLogics;
    this.Objects=TObjects;
 }
 
 //Add an objet when its dragg in the definition list
  public void addObjet(int l, int Position, Objet obj){
     ArrayList Objets=this.getObjets(l);
     if(Objets==null || Objets.size()==0){  Objets=new ArrayList();  Objets.add(obj); } 
     else  { if(Position<Objets.size()) Objets.add(Position,obj); else Objets.add(obj);}
     this.setObjets(l,Objets);
 }
 //Return the Default Logic of the Objetcs List
 public ArrayList getDefaultObjets(){
   ArrayList Objets=this.getObjets("");
   if(Objets==null) Objets=this.getObjets(0);
   return Objets;
 }

 
 public ArrayList getObjets(int l){  return this.Objects[l];  }
 public ArrayList getObjets(MetaLogic logis){
   for(int i=0;i<nbLogic;i++)  {
         MetaLogic logi=(MetaLogic)this.Logics[i];
         if(logi==logis || p.uf.equal(logi.getName(),logis.getName())) return this.getObjets(i);      
     }
   return null;
 }
 public ArrayList getObjets(String name){
   for(int i=0;i<nbLogic;i++) {
         MetaLogic logi=(MetaLogic)this.Logics[i];
         if(p.uf.equal(logi.getName(),name)) return this.getObjets(i);      
     }
   return null;
 }
 
  public void setObjets(int l ,ArrayList objets){
    Objects[l]=objets;
    this.setLogic(l,p.rr.DecodeRule(objets));       
  }
  

  //Switch two object dragg from the definition list in the same
  public void switchObject(int l,int Position, Objet obj){
    ArrayList Objets=this.getObjets(l);
    if(Objets!=null){
       int idx=Objets.lastIndexOf(obj);
       //Just simple Click on the  Element
       if(idx==Position){
           if(obj.isLogicGene()){  LogicGene dg=obj.getLogicGene(); dg.v=!dg.v;  }   //Swithc Value of Gene
           else if(obj.isLogicLogic()){  LogicLogic ll=obj.getLogicLogic(); ll.v=!ll.v;  }   //Swithc Value of Logic
           else if(obj.isOperator()){ 
               Operator op=obj.getOperator(); 
               if(op.comp==4 || op.comp==5 || op.comp==6){  op.step++; if(op.step>30) op.step=0;}//Add Step for Temporal Operator
           } 
       }
       else{ //Shift Element
         Objets.remove(idx);
         if(Position<Objets.size())  Objets.add(Position,obj);
         else Objets.add(obj);
       }
       this.setObjets(l,Objets);  
     } 
  }
  
 
  //Remove an objets dragged in the trash from the definition list
  public void  delObject(int l,Objet obj){
    ArrayList Objets=this.getObjets(l);
     if(Objets!=null){
       int idx=Objets.lastIndexOf(obj);
       if(idx>=0) Objets.remove(idx);
       this.setObjets(l,Objets);  
     } 
  }
  
  
  public int size(){return p.round(p.textWidth(Name)+4);}  
  //Return the background color of the gene
  public int colorGene(){
     int nbColor=0,r=0,b=0,g=0;
     if(!this.isGene)  { r+=p.red(p.cm.colorBoxNoGene);g+=p.green(p.cm.colorBoxNoGene);b+=p.blue(p.cm.colorBoxNoGene); nbColor++;}
     if(this.isUnknown)  {r+=p.red(p.cm.colorUnknownGene);g+=p.green(p.cm.colorUnknownGene);b+=p.blue(p.cm.colorUnknownGene);  nbColor++; }
     if(this.isMaternal)  { r+=p.red(p.cm.colorMaternal);g+=p.green(p.cm.colorMaternal);b+=p.blue(p.cm.colorMaternal); nbColor++; }
     if(this.isUbiquitous)  {r+=p.red(p.cm.colorUbiquitous);g+=p.green(p.cm.colorUbiquitous);b+=p.blue(p.cm.colorUbiquitous); nbColor++;}
     if(nbColor==0) return p.cm.colorBoxGene; //Dont recolor all the background
     return p.color(r / nbColor, b / nbColor, g / nbColor);
  }
 
  //Draw  the name of the gene and their background in different colors
  public void draw(int x,int y,boolean rot,int siz, boolean cent,boolean activ){ //SizeDrawGene
      if(siz==-1) siz=this.size(); //Define the size of the box to draw
      
      p.pushMatrix(); 
      p.translate(x,y);
      int c=this.colorGene();  //Color of the background 
      int cT=p.cm.Textcolor;  if(!this.isGene) cT=p.cm.TextNocolor; //Color of the texte
      p.fill(c); 
      if(activ) { if(p.eh.mouseOn(x,y,siz,20)) p.stroke(p.cm.colorButton); else p.noStroke(); }else p.noStroke();  //If mouse over 
      p.fill(c);  //Draw the background 
      int shif=0; 
      if(rot) { p.rotate(-p.PI/2); shif-=siz-2; p.rect(-siz,0,siz,20);  } //Rotate if necessary
      else p.rect(0,0,siz,20); 
      if(cent) shif+=(siz-p.textWidth(Name))/2;
      p.fill(cT);p.text(this.Name,shif, 14);
      p.popMatrix(); 
  }
  
  
 public int logicSize(String NameLo){ String N=this.Name; if(!NameLo.equals(""))N+=":"+NameLo; return p.round(p.textWidth(N)+4);}
  public void drawLogic(String NameLo,int x,int y,int c){  this.drawLogic(NameLo,x,y,c,p.gm.SizeDrawGene); }
  public void drawLogic(String NameLo,int x,int y,int c,int siz){
    String N=Name; if(!NameLo.equals(""))N+=":"+NameLo;
    if(siz==-1) siz=p.round(p.textWidth(N)+4);
    if(p.eh.mouseOn(x,y,siz,20)) p.fill(c,255);   else p.fill(c);  p.noStroke(); p.rect(x,y,siz,20);
    p.fill(p.cm.Textcolor); p.text(N, x - p.textWidth(N) / 2 + siz / 2, y + 14);
  }
  
  
  //Just return the comments
   public String getComments(ModelDomain md){
     String cmmt="<BR>"+this.Name+"\n";
    Logic logic=p.rr.DecodeRule(this.getDefaultObjets());
    if(logic==null) return cmmt;
     for(int l=0;l<this.nbLogic;l++){
       MetaLogic logi=(MetaLogic)this.getLogic(l);
       ArrayList Objets=this.getObjets(l);
       if(Objets!=null)  {
           if(logi.is(this,md,p.mm.StepExplanation,false))cmmt+="<BR>";
            if(!logi.getName().equals(""))  cmmt+=logi.getName()+":";
            cmmt+="if ";
            for(int i=0;i<Objets.size();i++)   {
                String rr=((Objet)Objets.get(i)).toRule();
                rr=p.uf.replaceString(rr,"G:","");rr=p.uf.replaceString(rr,"L:","");rr=p.uf.replaceString(rr,"D:","");rr=p.uf.replaceString(rr,";","");
                cmmt+=" "+rr;
            }
            cmmt+=" then="+logi.getThen()+ " else="+logi.getElse() +"\n";
      }
   }
   return cmmt;
  }
  //Return the list of genes involve in the default logic
   public void getGenesInvolve(ModelDomain md){
      p.mm.GenesInvolve=new ArrayList();p.gm.ModulInvovle=null;
      Logic logic=p.rr.DecodeRule(this.getDefaultObjets());
      if(logic!=null) p.gm.resultInvovle=logic.is(this,md,p.mm.StepExplanation,true);
  }
  
  //Test the default Logic
   public boolean is(ModelDomain md,int st){
    MetaLogic logic=this.getDefaultLogic();
    if(logic==null) { p.dm.CommentsRule="No Logic"; return md.getValue(this,st);}
    
    boolean v=false;
    if(this.isUbiquitous){
       for(int d=0;d<p.dm.nbDomain;d++){  
         ModelDomain mod=p.lm.MyModel.getDomain(d);
         if(mod.GenesStates[p.gm.getNumGene(this)][st]) v=true;
       }
       if(!v) v=logic.is(this,md,st,false); //Calcul the normal value
    }else   v=logic.is(this,md,st,false); //Not Ubiquitous Gene
    return v;
  }
  
  
  //Return true is the rule which statisfy the gene is blue
  public boolean isBlue(ModelDomain md,int st){
    if(nbLogic==1){ //We Just have one Logic and it's a blue one
       MetaLogic logi=(MetaLogic)this.Logics[0];
       if(logi.isBlue) return true;
    }
    
    for(int i=0;i<nbLogic;i++){
         Logic logic=p.rr.DecodeRule(this.getObjets(i));
         MetaLogic logi=(MetaLogic)this.Logics[i];
         if(logi.Name.equals("") && logi.isBlue) return true; //If the Default Logics is blue return true
         if(!logi.Name.equals("") && logi.isBlue){
             if(logic.is(this,md,st,false)) return true;
         }
    }
    return false;
  }
  
  public void setIsGene(boolean value) {
	  isGene = value;
  }
  
  public void setIsMaternal(boolean value) {
	  isMaternal = value;
  }
  
  public void setIsUbiquitous(boolean value) {
	  isUbiquitous = value;
  }
  
  public void setIsUnknown(boolean value) {
	  isUnknown = value;
  }

  public ArrayList<Gene> getAllGenesInVectorEquations() {
    ArrayList<Gene> geneList = new ArrayList<Gene>();

    for (int i = 0; i < nbLogic; i++) {
      Logic logic = getLogic(i);
      ArrayList logicGenes = logic.getAllGenes();
      for (Iterator<Object> geneIter = logicGenes.iterator(); geneIter.hasNext();) {
        Gene gene = (Gene)geneIter.next();
        geneList.add(gene);
      }
    }

    return geneList;
  }
}

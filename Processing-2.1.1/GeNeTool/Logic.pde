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

public interface  Logic{
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve);//Return the value of the logic at step s
  public Logic getLogic(int n); //Return the logic 0 for this, 1 for l1 2, for l2
  public Operator getOperator(); //Return the operator invole in this logic
  public ArrayList getGenes();  //Return the list of Genes involve in this logic
  public ArrayList getAllGenes();  //Return the list of Genes involve in this logic and all the sublogic
  public void switchGene(Gene g); //Go throw the logic tree and permut the value of the gene 
  public boolean isGene(Gene g);  //Return true if the tree logic contain this gene
  public String getClassName(); //Return the class of the logic
  public String toString(); //Print this logic  
  public String toRule(); //Return the rule of the logic 
  public boolean getValue(); //Return 1 the value of a Gene==1  or 0 of NOT GENE=1
}


//Logic with no elet
boolean isLogicEmpty(Logic l){ return l.getClass().getName().equals(NameApplication+"$"+"LogicEmpty");}
public class LogicEmpty implements Logic{

  LogicEmpty(){ super();  }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return null;}
  public Logic getLogic(int n){ if(n==0) return this;  return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){  return md.getValue(gene,s);  }
  public ArrayList getGenes(){ return new ArrayList();  }
  public ArrayList getAllGenes(){return new ArrayList();  }
  public void switchGene(Gene g){  }
  public boolean isGene(Gene g){  return true;  }
  public String toString(){  return ""; }
  public String toRule(){ return "";}
  public int gertSize(){return 0;}
  public boolean getValue(){ return true;}
}




//This Big Meta Logic encapsule the other
boolean isMetaLogic(Logic l){ return l.getClass().getName().equals(NameApplication+"$"+"MetaLogic");}
public class MetaLogic implements Logic{
  
   String Name="";
   String Else="";
   String Then="";
   Logic logi;
   boolean valid; //If is logic is avalid draw a good image
   boolean isBlue=false; //If the logic is a blue Logic
   boolean dft; //Is it the default logic ?
   
   MetaLogic(Logic logi,String Name,String Then,String Else,boolean blueLogic){
    this.Name=Name;
    this.Then=Then;
    this.Else=Else;
    this.logi=logi;
    this.isBlue=blueLogic;
    if(this.logi==null) valid=false; else valid=true;
    dft=false;
  }
  
  public void setName(String id){this.Name=id;}
  public void setThen(String thens){this.Then=thens;}
  public void invThen(){ if(this.Then.equals("1")) this.Then="0"; else this.Then="1"; }
  public void invElse(){ if(this.Else.equals("0")) this.Else="1"; else this.Else="0"; }
  public void setElse(String elses){this.Else=elses;}
  public String getThen(){return this.Then;}; 
  public String getElse(){return this.Else;}; 
  public String getName(){ return this.Name;}
  public boolean getValue(){ return logi.getValue();}
  
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return null;}
  public Logic getLogic(int n){ if(n==0) return this;  return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){  
    if(this.logi==null) return false;
    boolean v=this.logi.is(gene,md,s,involve);
    if(v) v=parseBool(Then);    else v=parseBool(Else);
    return v;
  }
  public ArrayList getGenes(){ return new ArrayList();  }
  public ArrayList getAllGenes(){ if(logi!=null) return logi.getAllGenes();return new ArrayList();  }
  public void switchGene(Gene g){  }
  public boolean isGene(Gene g){  return true;  }
  public String toString(){  return this.Name; }
  public String toRule(){
    if(this.logi==null) return ""; 
    if(isMetaLogic(logi)){ MetaLogic lo=(MetaLogic)logi;  return replaceString(lo.getName()," ",""+char(35));}
    return logi.toRule();
  }
  public int size(){return round(textWidth(Name)+4);}
  public void draw(int x,int y,color c, float siz){ 
    if(siz==-1) siz=textWidth(Name);
    if(mouseOn(x,y,siz,20)) fill(c,150);   else fill(c,50);
    stroke(c,150);   rect(x,y,siz,20);
    fill(Textcolor); text(Name,x-textWidth(Name)/2+siz/2,y+14);
  }
  public void draw(int x,int y,color c){  draw(x,y,c,40);}  
        
  
}



// HPF Operator > & <
boolean isLogicOperator(Logic l){ return l.getClass().getName().equals(NameApplication+"$"+"LogicOperator");}
public class LogicOperator implements Logic{
  Operator o=null;
  
  LogicOperator(Operator o){ this.o=o;  }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return this.o;}
  public Logic getLogic(int n){  return this;  }
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){ 
    boolean v=false;
    switch(o.comp){
      case 6:   v=o.applyHPF(gene,md,s); break;  //Unit Logic 
      default : addMessage("ERROR test LogicOperator " +this.toString() +" at Step "+ s + " : LogicOne -> Gene " +gene.Name + " in Domain " +md.getName()  + " with operator " + o.Name +" : " +o.comp); break;
    }    
    return v;
  }
  public boolean getValue(){ return false;}
  public ArrayList getGenes(){ return new ArrayList();  }
  public ArrayList getAllGenes(){return new ArrayList();  }
  public void switchGene(Gene g){  }
  public boolean isGene(Gene g){ return false;}
  public String toString(){  return  this.o.toString(); }  
  public String toRule(){   return  this.o.toRule();}

}


//Module Logic==1
boolean isLogicLogic(Logic l){ return l.getClass().getName().equals(NameApplication+"$"+"LogicLogic");}
public class LogicLogic implements Logic{
  Logic l1=null;
  boolean v=false;
  
  LogicLogic(Logic l1,boolean v){  this.v=v;  this.l1=l1;  }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return null;}
  public Logic getLogic(int n){ if(n==0) return this;  if(n==1) return this.l1;  return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){  
      boolean vv=l1.is(gene,md,s,involve)==this.v; 
      if(vv && involve && StepExplanation>0) ModulInvovle=l1;
      return vv;
  
 }
  public ArrayList getGenes(){  return l1.getGenes();  }
  public ArrayList getAllGenes(){ if(l1!=null) return l1.getAllGenes();return new ArrayList();  }
  public void switchGene(Gene g){  this.l1.switchGene(g); }
  public boolean isGene(Gene g){   return this.l1.isGene(g); }
  public String toString(){  return  this.l1+"="+this.v; }  
    public String toRule(){ 
   int vv=0; if(v)vv=1; 
  if(isMetaLogic(this.l1)){ MetaLogic lo=(MetaLogic)this.l1;  return replaceString(lo.getName()+"="+vv," ",""+char(35));}
  return  replaceString(this.l1+"="+vv," ",""+char(35));}
  public boolean getValue(){ return v;}
  
  public int size(){MetaLogic lo=(MetaLogic)this.l1;  String Name=lo.getName(); return round(textWidth(Name)+4);}
  public void draw(int x,int y, float siz){ 
    color c=colorGeneOff; if(this.v) c=colorGeneOn;
    MetaLogic lo=(MetaLogic)this.l1;  String Name=lo.getName();
    if(siz==-1) siz=textWidth(Name)+4;
    if(mouseOn(x,y,siz,20)) fill(c,255);   else fill(c);
    stroke(c,150);   rect(x,y,siz,20);
    fill(Textcolor); text(Name,x-textWidth(Name)/2+siz/2,y+14);
  }
  public void draw(int x,int y){  draw(x,y,40);}
  
}





//Logic Simple Gene=1 OR a specific  Gene:ModH=1
boolean isLogicGene(Logic l){ return l.getClass().getName().equals(NameApplication+"$"+"LogicGene");}
public class LogicGene implements Logic{
   Gene g=null;
   boolean v=false;
   String logicName;
   
  LogicGene(Gene g,boolean v,String logicName){   super();    this.g=g; this.v=v; this.logicName=logicName; }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return null;}
  public Logic getLogic(int n){ if(n==0) return this;  return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){ 
      CommentsRule+=" "+this.g.Name+"="; 
      boolean vv=false;
      if(logicName.equals("")) vv=md.getValue(this.g,s); //Test the default value
      else {
        if(md.Manual[getNumGene(g)][s]){  //If force manually
          vv=md.getValue(this.g,s);
        }
        else{
            MetaLogic Ml=(MetaLogic)g.getLogic(logicName); //Test a specific Modul on this logic
            vv=Ml.is(this.g,md,s,involve);
        }
      }
      if(vv) CommentsRule+=1; else CommentsRule+=0;
      return  vv==this.v;
  }
  public boolean getValue(){ return v; }
  public ArrayList getGenes(){  ArrayList ListGenes=new ArrayList();  ListGenes.add(this.g);  return ListGenes;  }
  public ArrayList getAllGenes(){  ArrayList ListGenes=new ArrayList();  ListGenes.add(this.g);  return ListGenes;  }
  public void switchGene(Gene g){ if(this.g==g) this.v=!this.v;  }
  public boolean isGene(Gene g){   return this.g==g;}
  public String toString(){ if(!logicName.equals("")) return ""+this.g+":"+logicName+"="+this.v;  return ""+this.g+"="+this.v; }
  public String toRule(){   int vv=0; if(v)vv=1;   if(!logicName.equals("")) return "G:"+this.g.toRule()+":"+logicName+"="+vv;  return "G:"+this.g.toRule()+"="+vv;  }
  public int getSize(){return 1;} 
  
  public int size(){return this.g.logicSize(logicName);}
  public void draw(int x,int y){ this.draw(x,y,SizeDrawGene);}
  public void draw(int x,int y,int siz){
    color c=colorGeneOn; if(!this.v) c=colorGeneOff;  
    this.g.drawLogic(logicName,x,y,c,siz);
    if(LogicDeroule==this) { LogicDerouleCoord[0]=x;LogicDerouleCoord[1]=y;}
  }
}


//Logic with simple elet
boolean isLogicDomain(Logic l){ return l.getClass().getName().equals(NameApplication+"$"+"LogicDomain");}
public class LogicDomain implements Logic{
   Domain dom=null;
 
  LogicDomain(Domain dom){  super();  this.dom=dom;  }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return null;}
  public Logic getLogic(int n){ if(n==0) return this;  return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){return true;}
  public ArrayList getGenes(){  return new ArrayList(); }
  public ArrayList getAllGenes(){ return new ArrayList(); }
  public void switchGene(Gene g){ }
  public boolean isGene(Gene g){ return false;}
  public String toString(){  return ""+this.dom; }
  public String toRule(){   return ""+this.dom.toRule(); }
  public int getSize(){return 1;}
  public boolean getValue(){ return true;}
}

//Logic with unit operator NOT X, IN R etc ..
boolean isLogicOne(Logic l){ return l.getClass().getName().equals(NameApplication+"$"+"LogicOne");}
public class LogicOne implements Logic{
  Logic l1=null;
  Operator o=null;
  
  LogicOne(Operator o,Logic l1){   this.o=o;    this.l1=l1;  }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return this.o;}
  public Logic getLogic(int n){ if(n==0) return this;  if(n==1) return this.l1;  return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){
    boolean v=false;
    //CommentsRule+=" "+this.o.Name; 
    switch(o.comp){
      case 1:   v=o.applySIMPLE(l1.is(gene,md,s,involve)); break;  //Unit Logic 
      case 3:   v=o.applyIN(gene,l1,md,s);  break;//Domain Logic IN DOMAIN OR IN CC DOMAIN 
      case 4:   v=o.applyTEMP(l1,md,s,involve); break;//Temporal Logic
      case 5:   v=o.applySTRONG(gene,l1,md,s,involve); break;//Temporal Logic
      default : addMessage("ERROR test LogicOne " +this.toString() +" at Step "+ s + " : LogicOne -> Gene " +gene.Name + " in Domain " +md.getName()  + " with operator " + o.Name +" : " +o.comp); break;
    }
    return v;
  }
  public boolean getValue(){ 
    boolean v=false;
    switch(o.comp){
      case 1:   v=o.applySIMPLE(l1.getValue()); break;  //Unit Logic 
      default : addMessage("ERROR getValue LogicOne " +this.toString()); break;
    }
    return v;
  }
  public ArrayList getGenes(){  return l1.getGenes();  }
  public ArrayList getAllGenes(){  return l1.getAllGenes(); }
  public void switchGene(Gene g){  this.l1.switchGene(g); }
  public boolean isGene(Gene g){   return this.l1.isGene(g); }
  public String toString(){  return  this.o+" "+this.l1; }  
  public String toRule(){ 
  if(isMetaLogic(this.l1)){ MetaLogic lo=(MetaLogic)this.l1;  return this.o+" "+lo.getName();}
  return  this.o+" "+this.l1.toRule(); }
}




//Logic with double operator // A OR B, A AND C..
boolean isLogicTwo(Logic l){ return l.getClass().getName().equals(NameApplication+"$"+"LogicTwo");}
public class LogicTwo implements Logic{
  Logic l1=null;
  Operator o=null;
  Logic l2=null;
 
  LogicTwo(Logic l1,Operator o,Logic l2){  this.l1=l1;  this.o=o;  this.l2=l2; }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return this.o;}
  public Logic getLogic(int n){ if(n==0) return this;  if(n==1) return this.l1; if(n==2) return this.l2; return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){
    if(o.Name.equals("OR")) {  //Just for OR  TO DO NOT TEST BOTH CONDITION IF THE FIRST IS GOOD
        if(l1.is(gene,md,s,involve)) return true;
       // if(GenesInvolve!=null) GenesInvolve.clear();
        if(l2.is(gene,md,s,involve))  return true;
        return false;
     } 
    //CommentsRule+=" 2 "+this.o.Name; 
    boolean v=false;
     switch(o.comp){
      case 2:   v= o.applyDOUBLE(l1.is(gene,md,s,involve),l2.is(gene,md,s,involve));   break;  //Double Log  OR AND NAND XOR
      case 3:   v= o.applyXIN(l1,l2,gene,md,s);  break;//Domain Logic X IN DOMAIN OR X IN CC DOMAIN 
      default : addMessage("ERROR test LogicTwo " +this.toString() +" at Step "+ s + " : LogicOne -> Gene " +gene.Name + " in Domain " +md.getName()  + " with operator " + o.Name +" : " +o.comp); break;
    }
   // if(StepExplanation>0) addMessage("GenesInvolve="+GenesInvolve.size() + " Step "+ MyModel.step + " : LogicTwo -> Gene " +gene.Name + " in Domain " +dom.Name + " with logic  "+l1.toString()+" " + o.Name +" "+l2.toString()+" -> RESULT = " + v);
    return v;
  }
  public boolean getValue(){
     boolean v=false;
     switch(o.comp){
      case 2:  v= o.applyDOUBLE(l1.getValue(),l2.getValue()); ; break;  //Double Logic 
      default : addMessage("ERROR getValue LogicTwo " +this.toString()); break;
    }
    return v;
  }
  public ArrayList getGenes(){  return combine(l1.getGenes(),l2.getGenes());  }
  public ArrayList getAllGenes(){  return combineGeneList(l1.getAllGenes(),l2.getAllGenes());  }
  public void switchGene(Gene g){  this.l1.switchGene(g);   this.l2.switchGene(g); }
  public boolean isGene(Gene g){   return this.l1.isGene(g) || this.l2.isGene(g); }
  public String toString(){  return this.l1+ " "+this.o+" "+l2; }  
   public String toRule(){ 
     String rule="";
     if(isMetaLogic(this.l1)){ MetaLogic lo=(MetaLogic)this.l1;  rule=lo.getName();}
     else rule=this.l1.toRule();
     rule=rule+ " "+this.o+" ";
     if(isMetaLogic(this.l2)){ MetaLogic lo=(MetaLogic)this.l2;  rule=rule+lo.getName();}
     else rule=rule+this.l2.toRule();
     return rule;
   }
}


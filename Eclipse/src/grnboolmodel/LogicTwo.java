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

public class LogicTwo implements Logic{
  Logic l1=null;
  Operator o=null;
  Logic l2=null;
  GRNBoolModel p;
 
  LogicTwo(GRNBoolModel p, Logic l1,Operator o,Logic l2){  this.p=p; this.l1=l1;  this.o=o;  this.l2=l2; }
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
      default : p.mm.addMessage("ERROR test LogicTwo " +this.toString() +" at Step "+ s + " : LogicOne -> Gene " +gene.Name + " in Domain " +md.getName()  + " with operator " + o.Name +" : " +o.comp); break;
    }
   // if(StepExplanation>0) addMessage("GenesInvolve="+GenesInvolve.size() + " Step "+ MyModel.step + " : LogicTwo -> Gene " +gene.Name + " in Domain " +dom.Name + " with logic  "+l1.toString()+" " + o.Name +" "+l2.toString()+" -> RESULT = " + v);
    return v;
  }
  public boolean getValue(){
     boolean v=false;
     switch(o.comp){
      case 2:  v= o.applyDOUBLE(l1.getValue(),l2.getValue()); ; break;  //Double Logic 
      default : p.mm.addMessage("ERROR getValue LogicTwo " +this.toString()); break;
    }
    return v;
  }
  public ArrayList getGenes(){  return p.uf.combine(l1.getGenes(),l2.getGenes());  }
  public ArrayList getAllGenes(){  return p.uf.combineGeneList(l1.getAllGenes(),l2.getAllGenes());  }
  public void switchGene(Gene g){  this.l1.switchGene(g);   this.l2.switchGene(g); }
  public boolean isGene(Gene g){   return this.l1.isGene(g) || this.l2.isGene(g); }
  public String toString(){  return this.l1+ " "+this.o+" "+l2; }  
   public String toRule(){ 
     String rule="";
     if(p.pm.isMetaLogic(this.l1)){ MetaLogic lo=(MetaLogic)this.l1;  rule=lo.getName();}
     else rule=this.l1.toRule();
     rule=rule+ " "+this.o+" ";
     if(p.pm.isMetaLogic(this.l2)){ MetaLogic lo=(MetaLogic)this.l2;  rule=rule+lo.getName();}
     else rule=rule+this.l2.toRule();
     return rule;
   }
}
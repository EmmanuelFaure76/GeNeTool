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

public class LogicOne implements Logic{
  Logic l1=null;
  Operator o=null;
  GRNBoolModel p;
  
  LogicOne(GRNBoolModel p, Operator o,Logic l1){   this.p=p; this.o=o;    this.l1=l1;  }
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
      default : p.mm.addMessage("ERROR test LogicOne " +this.toString() +" at Step "+ s + " : LogicOne -> Gene " +gene.Name + " in Domain " +md.getName()  + " with operator " + o.Name +" : " +o.comp); break;
    }
    return v;
  }
  public boolean getValue(){ 
    boolean v=false;
    switch(o.comp){
      case 1:   v=o.applySIMPLE(l1.getValue()); break;  //Unit Logic 
      default : p.mm.addMessage("ERROR getValue LogicOne " +this.toString()); break;
    }
    return v;
  }
  public ArrayList getGenes(){  return l1.getGenes();  }
  public ArrayList getAllGenes(){  return l1.getAllGenes(); }
  public void switchGene(Gene g){  this.l1.switchGene(g); }
  public boolean isGene(Gene g){   return this.l1.isGene(g); }
  public String toString(){  return  this.o+" "+this.l1; }  
  public String toRule(){ 
  if(p.pm.isMetaLogic(this.l1)){ MetaLogic lo=(MetaLogic)this.l1;  return this.o+" "+lo.getName();}
  return  this.o+" "+this.l1.toRule(); }
}

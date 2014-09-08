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

public class LogicOperator implements Logic{
  Operator o=null;
  GRNBoolModel p;
  
  LogicOperator(GRNBoolModel p, Operator o){ this.p=p; this.o=o;  }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return this.o;}
  public Logic getLogic(int n){  return this;  }
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){ 
    boolean v=false;
    switch(o.comp){
      case 6:   v=o.applyHPF(gene,md,s); break;  //Unit Logic 
      default : p.mm.addMessage("ERROR test LogicOperator " +this.toString() +" at Step "+ s + " : LogicOne -> Gene " +gene.Name + " in Domain " +md.getName()  + " with operator " + o.Name +" : " +o.comp); break;
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

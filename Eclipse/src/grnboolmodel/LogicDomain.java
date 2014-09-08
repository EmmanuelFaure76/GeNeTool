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

public class LogicDomain implements Logic{
   Domain dom=null;
   GRNBoolModel p;
 
  LogicDomain(GRNBoolModel p, Domain dom){  super();  this.p=p; this.dom=dom;  }
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
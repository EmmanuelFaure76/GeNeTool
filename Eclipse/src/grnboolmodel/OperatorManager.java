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
  // Operator.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Logic.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public class OperatorManager {

  private GRNBoolModel p;
  
  OperatorManager(GRNBoolModel p) {
    this.p = p;   
  } 
  
  int SizeDrawOperator=40;
  Operator [] LogicOperator; //List of All Operator
  int NbLogicOperator=12;
  Objet [] Bracket=new Objet[2];
  
 
  //Logic with no elet
  boolean isLogicEmpty(Logic l){ return l.getClass().getName().equals(p.pkgName + "LogicEmpty");}
  
  //This Big Meta Logic encapsule the other
  boolean isMetaLogic(Logic l){ return l.getClass().getName().equals(p.pkgName + "MetaLogic");}
  
  // HPF Operator > & <
  boolean isLogicOperator(Logic l){ return l.getClass().getName().equals(p.pkgName + "LogicOperator");}
  
  //Module Logic==1
  boolean isLogicLogic(Logic l){ return l.getClass().getName().equals(p.pkgName + "LogicLogic");}
  
  //Logic Simple Gene=1 OR a specific  Gene:ModH=1
  boolean isLogicGene(Logic l){ return l.getClass().getName().equals(p.pkgName + "LogicGene");}

  //Logic with simple elet
  boolean isLogicDomain(Logic l){ return l.getClass().getName().equals(p.pkgName + "LogicDomain");}
   
  //Logic with unit operator NOT X, IN R etc ..
  boolean isLogicOne(Logic l){ return l.getClass().getName().equals(p.pkgName + "LogicOne");} 

  //Logic with double operator // A OR B, A AND C..
  boolean isLogicTwo(Logic l){ return l.getClass().getName().equals(p.pkgName + "LogicTwo");}
  
  void initOperators(){
    LogicOperator=new Operator[NbLogicOperator];
     //Unit Operator
    //LogicOperator[0]=new Operator("LINEAR",1);
    LogicOperator[0]=new Operator(p, "NOT",1);
    //Double Operator
    LogicOperator[1]=new Operator(p, "AND",2);
    LogicOperator[2]=new Operator(p, "OR",2);
    //LogicOperator[4]=new Operator("NAND",2);
    //LogicOperator[5]=new Operator("XOR",2);
    //Domain Operator
    LogicOperator[3]=new Operator(p, "IN",3);
    LogicOperator[4]=new Operator(p, "CC",3);
    LogicOperator[5]=new Operator(p, "NCC_n",3);
    LogicOperator[6]=new Operator(p, "NCC_d",3);
    //Temporal Operator
    LogicOperator[7]=new Operator(p, "AFTER",4);
    LogicOperator[8]=new Operator(p, "AT",4);
    //LogicOperator[12]=new Operator("ACCU",4);
    //Specific
    LogicOperator[9]=new Operator(p, "PERM",5);
    //Hpf
    LogicOperator[10]=new Operator(p, "<",6);
    LogicOperator[11]=new Operator(p, ">",6);
    
    Bracket[0]=new Objet(p, "[");
    Bracket[1]=new Objet(p, "]");
  }

}

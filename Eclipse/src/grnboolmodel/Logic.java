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


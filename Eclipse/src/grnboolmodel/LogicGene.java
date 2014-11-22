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

public class LogicGene implements Logic{
   Gene g=null;
   boolean v=false;
   String logicName;
   GRNBoolModel p;
   
  public LogicGene(GRNBoolModel p, Gene g,boolean v,String logicName){   super();    this.p=p; this.g=g; this.v=v; this.logicName=logicName; }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return null;}
  public Logic getLogic(int n){ if(n==0) return this;  return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){ 
      p.dm.CommentsRule+=" "+this.g.Name+"="; 
      boolean vv=false;
      if(logicName.equals("")) vv=md.getValue(this.g,s); //Test the default value
      else {
        if(md.Manual[p.gm.getNumGene(g)][s]){  //If force manually
          vv=md.getValue(this.g,s);
        }
        else{
            MetaLogic Ml=(MetaLogic)g.getLogic(logicName); //Test a specific Modul on this logic
            vv=Ml.is(this.g,md,s,involve);
        }
      }
      if(vv) p.dm.CommentsRule+=1; else p.dm.CommentsRule+=0;
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
  public void draw(int x,int y){ this.draw(x,y,p.gm.SizeDrawGene);}
  public void draw(int x,int y,int siz){
    int c=p.cm.colorGeneOn; if(!this.v) c=p.cm.colorGeneOff;  
    this.g.drawLogic(logicName,x,y,c,siz);
    if(p.mm.LogicDeroule==this) { p.mm.LogicDerouleCoord[0]=x;p.mm.LogicDerouleCoord[1]=y;}
  }
}
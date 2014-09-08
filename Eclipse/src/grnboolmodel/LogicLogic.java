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

public class LogicLogic implements Logic{
  Logic l1=null;
  boolean v=false;
  GRNBoolModel p;
  
  LogicLogic(GRNBoolModel p, Logic l1,boolean v){  this.p = p; this.v=v;  this.l1=l1;  }
  public String getClassName(){ return this.getClass().getName();}
  public Operator getOperator(){return null;}
  public Logic getLogic(int n){ if(n==0) return this;  if(n==1) return this.l1;  return null;}
  public boolean is(Gene gene,ModelDomain md,int s,boolean involve){  
      boolean vv=l1.is(gene,md,s,involve)==this.v; 
      if(vv && involve && p.mm.StepExplanation>0) p.gm.ModulInvovle=l1;
      return vv;
  
 }
  public ArrayList getGenes(){  return l1.getGenes();  }
  public ArrayList getAllGenes(){ if(l1!=null) return l1.getAllGenes();return new ArrayList();  }
  public void switchGene(Gene g){  this.l1.switchGene(g); }
  public boolean isGene(Gene g){   return this.l1.isGene(g); }
  public String toString(){  return  this.l1+"="+this.v; }  
    public String toRule(){ 
   int vv=0; if(v)vv=1; 
  if(p.pm.isMetaLogic(this.l1)){ MetaLogic lo=(MetaLogic)this.l1;  return p.uf.replaceString(lo.getName()+"="+vv," ",""+(char)35);}
  return  p.uf.replaceString(this.l1+"="+vv," ",""+(char)35);}
  public boolean getValue(){ return v;}
  
  public int size(){MetaLogic lo=(MetaLogic)this.l1;  String Name=lo.getName(); return p.round(p.textWidth(Name)+4);}
  public void draw(int x,int y, float siz){ 
    int c=p.cm.colorGeneOff; if(this.v) c=p.cm.colorGeneOn;
    MetaLogic lo=(MetaLogic)this.l1;  String Name=lo.getName();
    if(siz==-1) siz=p.textWidth(Name)+4;
    if(p.eh.mouseOn(x,y,siz,20)) p.fill(c,255);   else p.fill(c);
    p.stroke(c,150);   p.rect(x,y,siz,20);
    p.fill(p.cm.Textcolor); p.text(Name,x-p.textWidth(Name)/2+siz/2,y+14);
  }
  public void draw(int x,int y){  draw(x,y,40);}
  
}
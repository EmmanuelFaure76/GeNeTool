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

import processing.core.PApplet;

public class MetaLogic implements Logic {
  
   String Name="";
   String Else="";
   String Then="";
   Logic logi;
   boolean valid; //If is logic is avalid draw a good image
   boolean isBlue=false; //If the logic is a blue Logic
   boolean dft; //Is it the default logic ?
   GRNBoolModel p;
   
   MetaLogic(GRNBoolModel p, Logic logi,String Name,String Then,String Else,boolean blueLogic){
    this.p = p;
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
    if(v) v=p.uf.parseBool(Then);    else v=p.uf.parseBool(Else);
    return v;
  }
  public ArrayList getGenes(){ return new ArrayList();  }
  public ArrayList getAllGenes(){ if(logi!=null) return logi.getAllGenes();return new ArrayList();  }
  public void switchGene(Gene g){  }
  public boolean isGene(Gene g){  return true;  }
  public String toString(){  return this.Name; }
  public String toRule(){
    if(this.logi==null) return ""; 
    if(p.pm.isMetaLogic(logi)){ MetaLogic lo=(MetaLogic)logi;  return p.uf.replaceString(lo.getName()," ",""+(char)35);}
    return logi.toRule();
  }
  public int size(){return p.round(p.textWidth(Name)+4);}
  public void draw(int x,int y,int c, float siz){ 
    if(siz==-1) siz=p.textWidth(Name);
    if(p.eh.mouseOn(x,y,siz,20)) p.fill(c,150);   else p.fill(c,50);
    p.stroke(c,150);   p.rect(x,y,siz,20);
    p.fill(p.cm.Textcolor); p.text(Name,x-p.textWidth(Name)/2+siz/2,y+14);
  }
  public void draw(int x,int y,int c){  draw(x,y,c,40);}  
}

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


public class Objet{
  Object obj;
  GRNBoolModel p;
  Objet(GRNBoolModel p, Object obj){this.p=p; this.obj=obj;}
  
  public boolean isDomain(){ return obj.getClass().getName().equals(p.pkgName + "Domain");}
  public Domain getDomain(){ return (Domain)obj;}

  public boolean isGene(){ return obj.getClass().getName().equals(p.pkgName + "Gene");}
  public Gene getGene(){ return (Gene)obj;}
 
  public boolean isOperator(){ return obj.getClass().getName().equals(p.pkgName + "Operator");}
  public Operator getOperator(){ return (Operator)obj;}
  
  public boolean isMetaLogic(){ return obj.getClass().getName().equals(p.pkgName + "MetaLogic");}
  public MetaLogic getMetaLogic(){ return (MetaLogic)obj;}
 
  public boolean isLogicTwo(){ return obj.getClass().getName().equals(p.pkgName + "LogicTwo");}
  public LogicTwo getLogicTwo(){ return (LogicTwo)obj;}
  
  public boolean isLogicOne(){ return obj.getClass().getName().equals(p.pkgName + "LogicOne");}
  public LogicOne getLogicOne(){ return (LogicOne)obj;}
  
  public boolean isLogicGene(){ return obj.getClass().getName().equals(p.pkgName + "LogicGene");}
  public LogicGene getLogicGene(){ return (LogicGene)obj;}
 
  public boolean isLogicLogic(){ return obj.getClass().getName().equals(p.pkgName + "LogicLogic");}
  public LogicLogic getLogicLogic(){ return (LogicLogic)obj;}
  
  public boolean isRegion(){ return obj.getClass().getName().equals(p.pkgName + "Region");}
  public Region getRegion(){ return (Region)obj;}

  public boolean isString(){ return obj.getClass().getName().equals("java.lang.String");}
  public String getString(){ return (String)obj;}
  
  public String getClassName(){return obj.getClass().getName();}
  
  
  public boolean equals(Objet objj){
      if(objj.isDomain() && this.isDomain()) return objj.getDomain()==((Domain)obj);
      return false;
  }
  
  public void draw(int x,int y){
    if(this.isGene())   ((Gene)obj).draw(x,y,false,p.gm.SizeDrawGene,true,false); 
    if(this.isDomain())   ((Domain)obj).draw(x,y,p.cm.colorDomain);
    if(this.isOperator())  ((Operator)obj).draw(x,y,p.cm.colorOperators); 
    if(this.isMetaLogic())  ((MetaLogic)obj).draw(x,y,p.cm.colorBoxBackground);
    if(this.isLogicLogic())  ((LogicLogic)obj).draw(x,y,p.cm.colorBoxBackground); 
    if(this.isLogicGene())  ((LogicGene)obj).draw(x,y); 
    if(this.isRegion()) ((Region)obj).draw(x,y);   
    if(this.isString()) p.uf.drawString(this.getString(),x,y,10,p.cm.colorBoxGene);
  }
 
  public void draw(int x,int y,int siz){
    if(this.isGene())   ((Gene)obj).draw(x,y,false,siz,true,false); 
    if(this.isDomain())   ((Domain)obj).draw(x,y,p.cm.colorDomain,siz);
    if(this.isOperator())  ((Operator)obj).draw(x,y,p.cm.colorOperators,siz); 
    if(this.isMetaLogic())  ((MetaLogic)obj).draw(x,y,p.cm.colorBoxBackground,siz); 
    if(this.isLogicLogic())  ((LogicLogic)obj).draw(x,y,siz); 
    if(this.isLogicGene())  ((LogicGene)obj).draw(x,y,siz); 
    if(this.isRegion()) ((Region)obj).draw(x,y,siz); 
     if(this.isString()) p.uf.drawString(this.getString(),x,y,siz,p.cm.colorBoxGene);
  }
  
   public int size(){
    int defaultV=40;
    if(this.isGene())  defaultV=((Gene)obj).size();
    if(this.isDomain())   defaultV=((Domain)obj).size();
    if(this.isOperator()) defaultV=((Operator)obj).size();
    if(this.isMetaLogic()) defaultV=((MetaLogic)obj).size();
    if(this.isLogicLogic()) defaultV=((LogicLogic)obj).size();
    if(this.isLogicGene()) defaultV=((LogicGene)obj).size();
    if(this.isRegion()) defaultV=((Region)obj).size();
    if(this.isString()) defaultV=p.round(p.textWidth(this.getString()))+4;
    return defaultV+6;
  }
  
  public String toString(){
    if(this.obj==null)  return "null";
   // addMessage("Class " + obj.getClass().getName());
    if(this.isGene()) return ((Gene)obj).toString();
    if(this.isDomain()) return ((Domain)obj).toString();
    if(this.isOperator()) return ((Operator)obj).toString();
    if(this.isMetaLogic()) return ((MetaLogic)obj).toString();
    if(this.isLogicLogic()) return ((LogicLogic)obj).toString();
    if(this.isLogicTwo()) return "( "+((LogicTwo)obj).toString() + " )";
    if(this.isLogicGene()) return ((LogicGene)obj).toString();
    if(this.isLogicOne()) return ((LogicOne)obj).toString();
    if(this.isRegion()) return ((Region)obj).toString();
    if(this.isString())  return this.getString();
    p.mm.addMessage("ERROR toString Don't no this Class " + obj.getClass().getName());
    return "";
  }
  
 
   public String toRule(){
    if(this.obj==null)  return "null";
    //addMessage("Class " + obj.getClass().getName());
    if(this.isGene()) return "G:"+((Gene)obj).toRule();
    if(this.isDomain()) return "D:"+((Domain)obj).toRule();
    if(this.isOperator()) return ((Operator)obj).toRule();
    if(this.isMetaLogic()) return "L:"+((MetaLogic)obj).getName();
    if(this.isLogicLogic()) return "L:"+((LogicLogic)obj).toRule();
    if(this.isLogicTwo()) return ((LogicTwo)obj).toRule();
    if(this.isLogicGene()) return ((LogicGene)obj).toRule();
    if(this.isLogicOne()) return ((LogicOne)obj).toRule();
    if(this.isRegion()) return ((Region)obj).toRule();
    if(this.isString())  return this.getString();
    p.mm.addMessage("ERROR toRule Don't no this Class " + obj.getClass().getName());
    return "";
  }
}

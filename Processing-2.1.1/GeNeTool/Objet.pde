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


Objet ObjetDrag=null;


public class Objet{
  Object obj;
  Objet(Object obj){this.obj=obj;}
  
  public boolean isDomain(){ return obj.getClass().getName().equals(NameApplication+"$"+"Domain");}
  public Domain getDomain(){ return (Domain)obj;}

  public boolean isGene(){ return obj.getClass().getName().equals(NameApplication+"$"+"Gene");}
  public Gene getGene(){ return (Gene)obj;}
 
  public boolean isOperator(){ return obj.getClass().getName().equals(NameApplication+"$"+"Operator");}
  public Operator getOperator(){ return (Operator)obj;}
  
  public boolean isMetaLogic(){ return obj.getClass().getName().equals(NameApplication+"$"+"MetaLogic");}
  public MetaLogic getMetaLogic(){ return (MetaLogic)obj;}
 
  public boolean isLogicTwo(){ return obj.getClass().getName().equals(NameApplication+"$"+"LogicTwo");}
  public LogicTwo getLogicTwo(){ return (LogicTwo)obj;}
  
  public boolean isLogicOne(){ return obj.getClass().getName().equals(NameApplication+"$"+"LogicOne");}
  public LogicOne getLogicOne(){ return (LogicOne)obj;}
  
  public boolean isLogicGene(){ return obj.getClass().getName().equals(NameApplication+"$"+"LogicGene");}
  public LogicGene getLogicGene(){ return (LogicGene)obj;}
 
  public boolean isLogicLogic(){ return obj.getClass().getName().equals(NameApplication+"$"+"LogicLogic");}
  public LogicLogic getLogicLogic(){ return (LogicLogic)obj;}
  
  public boolean isRegion(){ return obj.getClass().getName().equals(NameApplication+"$"+"Region");}
  public Region getRegion(){ return (Region)obj;}

  public boolean isString(){ return obj.getClass().getName().equals("java.lang.String");}
  public String getString(){ return (String)obj;}
  
  public String getClassName(){return obj.getClass().getName();}
  
  
  public boolean equals(Objet objj){
      if(objj.isDomain() && this.isDomain()) return objj.getDomain()==((Domain)obj);
      return false;
  }
  
  public void draw(int x,int y){
    if(this.isGene())   ((Gene)obj).draw(x,y,false,SizeDrawGene,true,false); 
    if(this.isDomain())   ((Domain)obj).draw(x,y,colorDomain);
    if(this.isOperator())  ((Operator)obj).draw(x,y,colorOperators); 
    if(this.isMetaLogic())  ((MetaLogic)obj).draw(x,y,colorBoxBackground);
    if(this.isLogicLogic())  ((LogicLogic)obj).draw(x,y,colorBoxBackground); 
    if(this.isLogicGene())  ((LogicGene)obj).draw(x,y); 
    if(this.isRegion()) ((Region)obj).draw(x,y);   
    if(this.isString()) drawString(this.getString(),x,y,10,colorBoxGene);
  }
 
  public void draw(int x,int y,int siz){
    if(this.isGene())   ((Gene)obj).draw(x,y,false,siz,true,false); 
    if(this.isDomain())   ((Domain)obj).draw(x,y,colorDomain,siz);
    if(this.isOperator())  ((Operator)obj).draw(x,y,colorOperators,siz); 
    if(this.isMetaLogic())  ((MetaLogic)obj).draw(x,y,colorBoxBackground,siz); 
    if(this.isLogicLogic())  ((LogicLogic)obj).draw(x,y,siz); 
    if(this.isLogicGene())  ((LogicGene)obj).draw(x,y,siz); 
    if(this.isRegion()) ((Region)obj).draw(x,y,siz); 
     if(this.isString()) drawString(this.getString(),x,y,siz,colorBoxGene);
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
    if(this.isString()) defaultV=round(textWidth(this.getString()))+4;
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
    addMessage("ERROR toString Don't no this Class " + obj.getClass().getName());
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
    addMessage("ERROR toRule Don't no this Class " + obj.getClass().getName());
    return "";
  }
  
}



//Decole a Logic Rule ad "Tcf=1 and Otx=1 and not HesC=1"
public ArrayList DecodeObjects(Gene gene,String rule){
  rule=trim(rule);
  if(rule.equals("")) return null;
  
  String []Word=split(rule,' ');  //Split all the word of the rule
  /*ArrayList Words=new ArrayList();
  String splitrule=rule;
  int space=splitrule.indexOf(" ");
  int dd=splitrule.indexOf("D:");
  while(space>0){
    if(space<dd){ //The space if before D:, we just remove it
      String w=splitrule.substring(0,space);
      Words.add(w);
      splitrule=splitrule.substring(space+1);
    }else{ //The space is after D:
      int pt=splitrule.indexOf(";");
      if(space>pt){//the space is after ; -> domain name with no space
          String w=splitrule.substring(0,space);
          Words.add(w);
          splitrule=splitrule.substring(space+1);
      }
      
      
    }
    space=splitrule.indexOf(" ");
    dd=splitrule.indexOf("D:");
  }*/
  
  
  ArrayList ListObjets=new ArrayList(); // Contains All the Elements;
  
  for(int i=0;i<Word.length;i++){ //First Read All the Element and put then in Objet
    String w=trimPlus(Word[i]); //Trim space comma ..
    boolean ObjFound=false;
    String [] What=split(w,":");
    //addMessage("Word is " +w + " -> " +What.length);
    switch(What.length){
     case 1://Operator or Bracket 
          for(int b=0;b<2;b++) 
                if(Bracket[b].getString().equals(w)) 
                    {ObjFound=true; ListObjets.add(new Objet(Bracket[b].getString())); } //Bracket 
          if(!ObjFound) 
              for(int l=0;l<NbLogicOperator;l++)  {
                 if(!ObjFound)
                 switch(LogicOperator[l].comp){
                   case 4: //AFTER AT 
                       String [] WhatOp=split(w,"-");
                      // addMessage("Try with this " +LogicOperator[l].Name + " WhatOp.length="+WhatOp.length + " w="+w + " WhatOp[0]="+WhatOp[0]+":");
                       if(WhatOp[0].equals(LogicOperator[l].Name)){
                             Operator op=new Operator(LogicOperator[l]);
                             if(WhatOp.length==2)   op.step=parseInt(WhatOp[1]); else op.step=0;
                             ObjFound=true; ListObjets.add(new Objet(op)); 
                        }
                        break;
                   case 5: //PERM
                       String [] PermOp=split(w,"-");
                       if( PermOp[0].equals(LogicOperator[l].Name)){
                             Operator op=new Operator(LogicOperator[l]);
                             if(PermOp.length==2)   op.step=parseInt(PermOp[1]); else op.step=0;
                             ObjFound=true; ListObjets.add(new Objet(op)); 
                        }
                        break;
                   case 6: //HPF
                      int le=LogicOperator[l].Name.length();
                       if(le<w.length() &&  LogicOperator[l].Name.equals(w.substring(0,le))) {
                          Operator op=new Operator(LogicOperator[l]); op.step=parseInt(w.substring(le));
                          ObjFound=true; ListObjets.add(new Objet(op)); 
                        }
                        break;
                  default :
                        if(LogicOperator[l].is(w))  {ObjFound=true; ListObjets.add(new Objet(new Operator(LogicOperator[l]))); }
                      break;
                 }
                 
              }
     break;
     case 2:  //Objet definie by  Gene G:, Domains D:, Logic L: 
         //addMessage("What ="+What[0] +" : "+What[1]); 
         if(What[0].equals("G")){ //Gene
               String G=replaceString(What[1],""+char(35)," ");
               boolean defaultValue=true;
               
               if(G.indexOf('=')>0) {  String []eq=split(G,'='); defaultValue=parseBool(trimPlus(eq[1]));  G=trimPlus(eq[0]);  }
               //addMessage("Found a gene " +G +" at "+defaultValue);
               Gene Input=getGene(G);
               if(Input==null) {  Input=new Gene(G);  Genes.add(Input);  } //Create a new Gene
               LogicGene dg =new LogicGene(Input,defaultValue,"");
               ListObjets.add(new Objet(dg));
         }
         else if(What[0].equals("D")){ //Domain
              //addMessage("Found a domain " + What[1]);
              String DD=replaceString(What[1],""+char(35)," ");
              Domain dom=getDomain(DD); //Found the domain
              if(equal(DD,"R")) dom=GenericDomain;   //If the domain is "R" it's a generic domain
              if(dom==null){   dom=new Domain(DD);   Domains.add(dom);   } //Create a new domain
              ListObjets.add(new Objet(dom));
         }
         else if(What[0].equals("L")){ //Logic
           //addMessage("Found a Logic " + What[1]);
           String G=replaceString(What[1],""+char(35)," ");
           boolean defaultValue=true;
           if(G.indexOf('=')>0) {  String []eq=split(G,'='); defaultValue=parseBool(trimPlus(eq[1]));  G=trimPlus(eq[0]);  }
           Logic lo=gene.getLogic(G); 
           if(lo==null) { 
                  lo=new MetaLogic(null,G,"1","0",false);
                  int NumLogic=gene.nbLogic;gene.nbLogic++;
                  gene.Objects[NumLogic]=null;
                  gene.Logics[NumLogic]=lo;
                  //addMessage("Create this Logics "+G+" does not exist ");
           }
           ListObjets.add(new Objet(new LogicLogic(lo,defaultValue)));
         
         }
     
     break;
     case 3: //Gene with a specific Logic 
          if(What[0].equals("G")){ //Gene
               String GName=replaceString(What[1],""+char(35)," ");
               String G=What[2];
               boolean defaultValue=true;
               if(G.indexOf('=')>0) {  String []eq=split(G,'='); defaultValue=parseBool(trimPlus(eq[1]));  G=trimPlus(eq[0]);  }
               
               Gene Input=getGene(GName);
               if(Input==null) {  Input=new Gene(GName);  Genes.add(Input);  } //Create a new Gene
               //Logic ML=(MetaLogic) Input.getLogic(G);
               Logic dg =new LogicGene(Input,defaultValue,G);
              // Logic dg =new LogicGene(Input,defaultValue,"");
               ListObjets.add(new Objet(dg));
               addMessage("Found a gene " +GName +" at "+defaultValue + " with Logic " + G);
         }
     
     
     break;
     default : addMessage("ERROR in DecodeObjects What.length=" + What.length);
    }
  }
    
  
 // print (" Found " +ListObjets.length + "objects :");  for(int i=0;i<ListObjets.length;i++) print(" "+ListObjets[i]); addMessage();
  //For All Objects cut by operator of category two
  return ListObjets;
}

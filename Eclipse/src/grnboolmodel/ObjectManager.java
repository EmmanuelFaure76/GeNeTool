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

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Objet.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public class ObjectManager {
  
  private GRNBoolModel p;
  
  ObjectManager(GRNBoolModel p) {
    this.p = p;   
  }
  
  //Decole a Logic Rule ad "Tcf=1 and Otx=1 and not HesC=1"
  public ArrayList DecodeObjects(Gene gene,String rule){
    rule=p.trim(rule);
    if(rule.equals("")) return null;
    
    String []Word=p.split(rule,' ');  //Split all the word of the rule
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
      String w=p.uf.trimPlus(Word[i]); //Trim space comma ..
      boolean ObjFound=false;
      String [] What=p.split(w,":");
      //addMessage("Word is " +w + " -> " +What.length);
      switch(What.length){
       case 1://Operator or Bracket 
            for(int b=0;b<2;b++) 
                  if(p.pm.Bracket[b].getString().equals(w)) 
                      {ObjFound=true; ListObjets.add(new Objet(p, p.pm.Bracket[b].getString())); } //Bracket 
            if(!ObjFound) 
                for(int l=0;l<p.pm.NbLogicOperator;l++)  {
                   if(!ObjFound)
                   switch(p.pm.LogicOperator[l].comp){
                     case 4: //AFTER AT 
                         String [] WhatOp=p.split(w,"-");
                        // addMessage("Try with this " +LogicOperator[l].Name + " WhatOp.length="+WhatOp.length + " w="+w + " WhatOp[0]="+WhatOp[0]+":");
                         if(WhatOp[0].equals(p.pm.LogicOperator[l].Name)){
                               Operator op=new Operator(p.pm.LogicOperator[l]);
                               if(WhatOp.length==2)   op.step=p.parseInt(WhatOp[1]); else op.step=0;
                               ObjFound=true; ListObjets.add(new Objet(p,op)); 
                          }
                          break;
                     case 5: //PERM
                         String [] PermOp=p.split(w,"-");
                         if( PermOp[0].equals(p.pm.LogicOperator[l].Name)){
                               Operator op=new Operator(p.pm.LogicOperator[l]);
                               if(PermOp.length==2)   op.step=p.parseInt(PermOp[1]); else op.step=0;
                               ObjFound=true; ListObjets.add(new Objet(p,op)); 
                          }
                          break;
                     case 6: //HPF
                        int le=p.pm.LogicOperator[l].Name.length();
                         if(le<w.length() &&  p.pm.LogicOperator[l].Name.equals(w.substring(0,le))) {
                            Operator op=new Operator(p.pm.LogicOperator[l]); op.step=p.parseInt(w.substring(le));
                            ObjFound=true; ListObjets.add(new Objet(p,op)); 
                          }
                          break;
                    default :
                          if(p.pm.LogicOperator[l].is(w))  {ObjFound=true; ListObjets.add(new Objet(p, new Operator(p.pm.LogicOperator[l]))); }
                        break;
                   }
                   
                }
       break;
       case 2:  //Objet definie by  Gene G:, Domains D:, Logic L: 
           //addMessage("What ="+What[0] +" : "+What[1]); 
           if(What[0].equals("G")){ //Gene
                 String G=p.uf.replaceString(What[1],""+(char)35," ");
                 boolean defaultValue=true;
                 
                 if(G.indexOf('=')>0) {  String []eq=p.split(G,'='); defaultValue=p.uf.parseBool(p.uf.trimPlus(eq[1]));  G=p.uf.trimPlus(eq[0]);  }
                 //addMessage("Found a gene " +G +" at "+defaultValue);
                 Gene Input=p.gm.getGene(G);
                 if(Input==null) {  Input=new Gene(p,G);  p.gm.Genes.add(Input);  } //Create a new Gene
                 LogicGene dg =new LogicGene(p, Input,defaultValue,"");
                 ListObjets.add(new Objet(p, dg));
           }
           else if(What[0].equals("D")){ //Domain
                //addMessage("Found a domain " + What[1]);
                String DD=p.uf.replaceString(What[1],""+(char)35," ");
                Domain dom=p.dm.getDomain(DD); //Found the domain
                if(p.uf.equal(DD,"R")) dom=p.dm.GenericDomain;   //If the domain is "R" it's a generic domain
                if(dom==null){   dom=new Domain(p, DD);   p.dm.Domains.add(dom);   } //Create a new domain
                ListObjets.add(new Objet(p, dom));
           }
           else if(What[0].equals("L")){ //Logic
             //addMessage("Found a Logic " + What[1]);
             String G=p.uf.replaceString(What[1],""+(char)35," ");
             boolean defaultValue=true;
             if(G.indexOf('=')>0) {  String []eq=p.split(G,'='); defaultValue=p.uf.parseBool(p.uf.trimPlus(eq[1]));  G=p.uf.trimPlus(eq[0]);  }
             Logic lo=gene.getLogic(G); 
             if(lo==null) { 
                    lo=new MetaLogic(p, null,G,"1","0",false);
                    int NumLogic=gene.nbLogic;gene.nbLogic++;
                    gene.Objects[NumLogic]=null;
                    gene.Logics[NumLogic]=lo;
                    //addMessage("Create this Logics "+G+" does not exist ");
             }
             ListObjets.add(new Objet(p, new LogicLogic(p, lo,defaultValue)));
           
           }
       
       break;
       case 3: //Gene with a specific Logic 
            if(What[0].equals("G")){ //Gene
                 String GName=p.uf.replaceString(What[1],""+(char)35," ");
                 String G=What[2];
                 boolean defaultValue=true;
                 if(G.indexOf('=')>0) {  String []eq=p.split(G,'='); defaultValue=p.uf.parseBool(p.uf.trimPlus(eq[1]));  G=p.uf.trimPlus(eq[0]);  }
                 
                 Gene Input=p.gm.getGene(GName);
                 if(Input==null) {  Input=new Gene(p, GName);  p.gm.Genes.add(Input);  } //Create a new Gene
                 //Logic ML=(MetaLogic) Input.getLogic(G);
                 Logic dg =new LogicGene(p, Input,defaultValue,G);
                // Logic dg =new LogicGene(Input,defaultValue,"");
                 ListObjets.add(new Objet(p, dg));
                 p.mm.addMessage("Found a gene " +GName +" at "+defaultValue + " with Logic " + G);
           }
       
       
       break;
       default : p.mm.addMessage("ERROR in DecodeObjects What.length=" + What.length);
      }
    }
      
    
   // print (" Found " +ListObjets.length + "objects :");  for(int i=0;i<ListObjets.length;i++) print(" "+ListObjets[i]); addMessage();
    //For All Objects cut by operator of category two
    return ListObjets;
  }
}

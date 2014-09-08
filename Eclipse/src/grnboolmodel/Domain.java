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


//
public class Domain{

  String Name;
  Objet[][] DefObjets; //List of the objects on the domain definition
  int [][]GenesData; //The real Data for each Genes
  ArrayList Tree; //The consecutif Domaine of Real Data
  GRNBoolModel p;
  
  Domain(GRNBoolModel p, String Name){
    this.p = p;
    Tree=new ArrayList();
    this.Name=Name;
    this.DefObjets=null;
    this.GenesData=new int[p.gm.nbGene][p.rm.MaxTime+1];
    for(int i=0;i<p.gm.nbGene;i++) for(int j=0;j<=p.rm.MaxTime;j++) this.GenesData[i][j]=-1;
    
  }
  
  public String getName(){return this.Name;}
   
  //When we add a new gene , we need to reallocate everything
  public void addGene(Gene g){
       int [][]TGenesData=new int[p.gm.nbGene][p.rm.MaxTime+1]; 
       int shiftGene=0;
       for(int i=0;i<p.gm.nbGene;i++){
             Gene gg=p.gm.getGene(i);
             if(gg==g){   //addMessage("Found " +gg.Name);
                shiftGene=1; 
                for(int j=0;j<=p.rm.MaxTime;j++)  TGenesData[i][j]=-1; 
             }
             else
               for(int j=0;j<=p.rm.MaxTime;j++)
                if(this.GenesData!=null) TGenesData[i][j]=this.GenesData[i-shiftGene][j];
       }
       this.GenesData=TGenesData;    
  }
  
   //When we add a new gene , we need to reallocate everything
  public void delGene(Gene g){
     int [][]TGenesData=new int[p.gm.nbGene-1][p.rm.MaxTime+1]; 
     int shiftGene=0;
     for(int i=0;i<p.gm.nbGene;i++){
           Gene gg=p.gm.getGene(i);
           if(gg==g)  shiftGene=1;    
           else
             for(int j=0;j<=p.rm.MaxTime;j++)  TGenesData[i-shiftGene][j]=this.GenesData[i][j];
     }
     this.GenesData=TGenesData;    
  }
  
  
  


 //Domaine Definition
 void delDef(int numDef){
    if(this.DefObjets!=null){
     if(this.DefObjets.length==1 && numDef==0)this.DefObjets=null;
     if(this.DefObjets!=null && this.DefObjets.length>1){
        Objet [][]NewObjetDeg=new Objet[this.DefObjets.length-1][2];
        int ix=0;
        for(int i=0;i<this.DefObjets.length;i++)
          if(i!=numDef) {
              for(int j=0;j<2;j++)   NewObjetDeg[ix][j]=this.DefObjets[i][j];
              ix++;
          }
      this.DefObjets=NewObjetDeg;       
     }
    }
 }
  
  void addEmptyDef(){
    if(this.DefObjets==null) this.DefObjets=new Objet[1][2]; 
    else{
      Objet [][]NewObjetDeg=new Objet[this.DefObjets.length+1][2];
      for(int i=0;i<this.DefObjets.length;i++)
        for(int j=0;j<2;j++) 
          NewObjetDeg[i][j]=this.DefObjets[i][j];
          
      for(int j=0;j<2;j++) NewObjetDeg[this.DefObjets.length][j]=null;
      this.DefObjets=NewObjetDeg;
    }
  }
  
  
  //Tree
  public int getTreeSize(){
    int si=0;
    for(int i=0;i<this.Tree.size();i++){
        Region reg=this.getTree(i);
        si+=p.textWidth(reg.Name)+4;
    }
    return si;
  }
  public boolean isInTree(Region regg){
    for(int i=0;i<this.Tree.size();i++){
        Region reg=this.getTree(i);
        if(reg==regg) return true;
    }
    return false;
  }
  public void addTree(String st){  Region reg=p.rm.getRegion(st); if(reg!=null) addTree(reg); this.computeData(); }
  public void addTree(Region reg){ if(!this.isInTree(reg)) Tree.add(reg); this.computeData();}
  
  public boolean delTree(Region regg){
    for(int i=0;i<this.Tree.size();i++){
      Region reg=this.getTree(i);
       if(reg==regg) { this.Tree.remove(i); this.computeData(); return true;}
    }this.computeData();
    return false;
  }
  public Region getTree(int i){ return (Region)this.Tree.get(i);}
  
  //Compute all the time hours step for all Gene in the Domain
  public void computeData(){
    //addMessage("Compute data for " + this.Name);
    this.GenesData=new int[p.gm.nbGene][p.rm.MaxTime+1]; //Max Time Hours
    for(int i=0;i<p.gm.nbGene;i++)  for(int t=0;t<=p.rm.MaxTime;t++)this.GenesData[i][t]=-1;     //init 
    for(int j=0;j<this.Tree.size();j++){     //First line is Region:hour
       Region reg=getTree(j);
       for(int i=0;i<p.gm.nbGene;i++){
           Gene g=p.gm.getGene(i);
           for(int t=reg.hours[0];t<=reg.hours[1];t++) this.GenesData[i][t]=g.Expression[reg.number][t];
       }
     }
  }
  //When the expression profile of genes has been changed...
  public void reComputeData(Gene g){ 
    p.mm.addMessage("Re-Compute data for " + this.Name + " for gene " + g.Name);
    int numGene=p.gm.getNumGene(g);
    for(int j=0;j<this.Tree.size();j++){     //First line is Region:hour
       Region reg=getTree(j);
       for(int t=reg.hours[0];t<=reg.hours[1];t++) this.GenesData[numGene][t]=g.Expression[reg.number][t];
     }
  }

  public String toString(){ return this.Name;}
  public String toRule(){return p.uf.replaceString(this.Name," ",""+(char)35);}

  public int size(){return p.round(p.textWidth(Name)+4);}
  public void draw(int x,int y,int c){ draw(x,y,c,p.dm.SizeDrawDomain);}
  public void draw(int x,int y,int c,int siz){
    if(siz==-1) siz=this.size(); p.fill(c);
    if(p.eh.mouseOn(x,y,siz,20))  p.stroke(p.cm.colorButton);   else  p.noStroke(); p.rect(x,y,siz,20);
    p.textFont(p.cm.myFont, 12);  p.fill(p.cm.Textcolor); p.text(Name,x+siz/2-p.textWidth(Name)/2,y+14);
  }
  public void drawName(int x,int y){
      p.textFont(p.cm.myFont, 24);  p.fill(p.cm.Textcolor); 
      p.text(this.Name,x,y); 
      p.textFont(p.cm.myFont, 12); 
  }
   

 
 
}

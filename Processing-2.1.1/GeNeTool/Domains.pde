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



ArrayList Domains=new ArrayList();
Domain DomainDef=null; //Domain current show in the definition
int nbDomain;

String CommentsRule="";
Domain GenericDomain=new Domain("R");
int SizeDrawDomain=100;


public void addDomain(Domain dom){
  addMessage("Create domain " +dom.Name);
  Domains.add(dom); nbDomain=Domains.size();
  if(nbDomain==1) checkEnablelMenu();
 
  DomainDef=dom;
  if(MyModels!=null) for(int i=0;i<MyModels.length;i++) MyModels[i].addDomain(dom);
}
public void delDomain(Domain dom){
  addMessage("Delete domain " +dom.Name);
  if(dom==DomainDef) DomainDef=null;
  if(MyModels!=null) for(int i=0;i<MyModels.length;i++) MyModels[i].delDomain(dom);
  ArrayList newDomains=new ArrayList();
  for(int d=0;d<nbDomain;d++){ Domain domm=getDomain(d);  if(domm!=dom) newDomains.add(domm);}
  Domains=newDomains;nbDomain=Domains.size();
  if(nbDomain==0)  checkEnablelMenu();
 
}
//Return a domain from the global list
public Domain getDomain(String name){  for(int d=0;d<nbDomain;d++){ Domain dom=getDomain(d);  if(equal(dom.Name,name)) return dom;}return null;}
public Domain getDomain(int i){ return (Domain)Domains.get(i);}
public void computeData(){ for(int d=0;d<nbDomain;d++) getDomain(d).computeData();}
public void reComputeData(Gene g){ for(int d=0;d<nbDomain;d++) getDomain(d).reComputeData(g);}

//
public class Domain{

  String Name;
  Objet[][] DefObjets; //List of the objects on the domain definition
  int [][]GenesData; //The real Data for each Genes
  ArrayList Tree; //The consecutif Domaine of Real Data
  Domain(String Name){
    Tree=new ArrayList();
    this.Name=Name;
    this.DefObjets=null;
    this.GenesData=new int[nbGene][MaxTime+1];
    for(int i=0;i<nbGene;i++) for(int j=0;j<=MaxTime;j++) this.GenesData[i][j]=-1;
    
  }
  
  public String getName(){return this.Name;}
   
  //When we add a new gene , we need to reallocate everything
  public void addGene(Gene g){
       int [][]TGenesData=new int[nbGene][MaxTime+1]; 
       int shiftGene=0;
       for(int i=0;i<nbGene;i++){
             Gene gg=getGene(i);
             if(gg==g){   //addMessage("Found " +gg.Name);
                shiftGene=1; 
                for(int j=0;j<=MaxTime;j++)  TGenesData[i][j]=-1; 
             }
             else
               for(int j=0;j<=MaxTime;j++)
                if(this.GenesData!=null) TGenesData[i][j]=this.GenesData[i-shiftGene][j];
       }
       this.GenesData=TGenesData;    
  }
  
   //When we add a new gene , we need to reallocate everything
  public void delGene(Gene g){
     int [][]TGenesData=new int[nbGene-1][MaxTime+1]; 
     int shiftGene=0;
     for(int i=0;i<nbGene;i++){
           Gene gg=getGene(i);
           if(gg==g)  shiftGene=1;    
           else
             for(int j=0;j<=MaxTime;j++)  TGenesData[i-shiftGene][j]=this.GenesData[i][j];
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
        si+=textWidth(reg.Name)+4;
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
  public void addTree(String st){  Region reg=getRegion(st); if(reg!=null) addTree(reg); this.computeData(); }
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
    this.GenesData=new int[nbGene][MaxTime+1]; //Max Time Hours
    for(int i=0;i<nbGene;i++)  for(int t=0;t<=MaxTime;t++)this.GenesData[i][t]=-1;     //init 
    for(int j=0;j<this.Tree.size();j++){     //First line is Region:hour
       Region reg=getTree(j);
       for(int i=0;i<nbGene;i++){
           Gene g=getGene(i);
           for(int t=reg.hours[0];t<=reg.hours[1];t++) this.GenesData[i][t]=g.Expression[reg.number][t];
       }
     }
  }
  //When the expression profile of genes has been changed...
  public void reComputeData(Gene g){ 
    addMessage("Re-Compute data for " + this.Name + " for gene " + g.Name);
    int numGene=getNumGene(g);
    for(int j=0;j<this.Tree.size();j++){     //First line is Region:hour
       Region reg=getTree(j);
       for(int t=reg.hours[0];t<=reg.hours[1];t++) this.GenesData[numGene][t]=g.Expression[reg.number][t];
     }
  }

  
  


 
  
  
  public String toString(){ return this.Name;}
  public String toRule(){return replaceString(this.Name," ",""+char(35));}

  public int size(){return round(textWidth(Name)+4);}
  public void draw(int x,int y,color c){ draw(x,y,c,SizeDrawDomain);}
  public void draw(int x,int y,color c,int siz){
    if(siz==-1) siz=this.size(); fill(c);
    if(mouseOn(x,y,siz,20))  stroke(colorButton);   else  noStroke(); rect(x,y,siz,20);
    textFont(myFont, 12);  fill(Textcolor); text(Name,x+siz/2-textWidth(Name)/2,y+14);
  }
  public void drawName(int x,int y){
      textFont(myFont, 24);  fill(Textcolor); 
      text(this.Name,x,y); 
      textFont(myFont, 12); 
  }
   

 
 
}


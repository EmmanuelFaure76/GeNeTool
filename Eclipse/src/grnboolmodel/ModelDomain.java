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


public class ModelDomain {
  
 
  Domain dom; //Correspondance with the real domain
  boolean [][]GenesStates; //When the domain is launch
  boolean [][]Manual; //When Someonclick on the box to change the value
  boolean HideSteps[]; //Hidding some steps 
  boolean []Steps; //the step where we have a gene changement (not a non gene)
  boolean [][]isBlueState; //When the result of the test is done by a blue modul
  GRNBoolModel p;
  
  //Contstructeur from an orignial domain
  ModelDomain(GRNBoolModel p, Domain dom){
    this.p = p;
    this.dom=dom;
    this.init();
  }
  public String getName(){return this.dom.Name;}
  
  //Duplication constructeur
  ModelDomain(ModelDomain md){
    this.p = md.p;
    this.dom=md.dom;
    this.init();
    for(int i=0;i<p.gm.nbGene;i++) for(int j=0;j<p.maxStep;j++) this.GenesStates[i][j]=md.GenesStates[i][j];
    for(int i=0;i<p.gm.nbGene;i++) for(int j=0;j<p.maxStep;j++) this.isBlueState[i][j]=md.isBlueState[i][j]; 
    for(int i=0;i<p.gm.nbGene;i++) for(int j=0;j<p.maxStep;j++) this.Manual[i][j]=md.Manual[i][j]; 
    for(int j=0;j<p.maxStep;j++) this.HideSteps[j]=md.HideSteps[j];
    for(int j=0;j<p.maxStep;j++) this.Steps[j]=md.Steps[j];
  }
  //Initisliation
  public void init(){
    this.GenesStates=new boolean [p.gm.nbGene][p.maxStep]; 
    this.isBlueState=new boolean [p.gm.nbGene][p.maxStep]; 
    this.Manual=new boolean [p.gm.nbGene][p.maxStep]; 
    for(int i=0;i<p.gm.nbGene;i++){
      for(int j=0;j<p.maxStep;j++)  this.GenesStates[i][j]=false; //Initialisation at false
      for(int j=0;j<p.maxStep;j++)  this.isBlueState[i][j]=false; 
      for(int j=0;j<p.maxStep;j++)  this.Manual[i][j]=false;
    }
    this.HideSteps=new boolean[p.maxStep];  for(int i=0;i<p.maxStep;i++) HideSteps[i]=true;//Hidden Step
    this.Steps=new boolean[p.maxStep];  for(int i=0;i<p.maxStep;i++) Steps[i]=true;//Hidden Step
  }
  
   //When we add a new gene , we need to reallocate everything
  public void addGene(Gene g){
    boolean [][]TGenesStates=new boolean[p.gm.nbGene][p.maxStep]; 
    boolean [][]TManual=new boolean[p.gm.nbGene][p.maxStep]; 
    boolean [][]TisBlueState=new boolean[p.gm.nbGene][p.maxStep]; 
       
    int shiftGene=0;
       for(int i=0;i<p.gm.nbGene;i++){
             Gene gg=p.gm.getGene(i);
             if(gg==g){   //addMessage("Found " +gg.Name);
                shiftGene=1; 
                for(int j=0;j<p.maxStep;j++){  TGenesStates[i][j]=false; TisBlueState[i][j]=false;  TManual[i][j]=false;  }
             }
             else
              for(int j=0;j<p.maxStep;j++){  TGenesStates[i][j]=this.GenesStates[i-shiftGene][j]; TisBlueState[i][j]=this.isBlueState[i-shiftGene][j];   TManual[i][j]=this.Manual[i-shiftGene][j];   }
       }
       this.GenesStates=TGenesStates;
       this.isBlueState=TisBlueState;
       this.Manual=TManual;
  }
  
     //When we add a new gene , we need to reallocate everything
  public void delGene(Gene g){
    boolean [][]TGenesStates=new boolean[p.gm.nbGene-1][p.maxStep]; 
    boolean [][]TManual=new boolean[p.gm.nbGene-1][p.maxStep]; 
    boolean [][]TisBlueState=new boolean[p.gm.nbGene-1][p.maxStep]; 
    int shiftGene=0;
    for(int i=0;i<p.gm.nbGene;i++){
         Gene gg=p.gm.getGene(i);
         if(gg==g) shiftGene=1;    
          else
              for(int j=0;j<p.maxStep;j++){ TGenesStates[i-shiftGene][j]=this.GenesStates[i][j]; TisBlueState[i-shiftGene][j]=this.isBlueState[i][j];   TManual[i-shiftGene][j]=this.Manual[i][j]; }
     }
     this.GenesStates=TGenesStates;
     this.isBlueState=TisBlueState;
     this.Manual=TManual;
  }
  
  
  //Compare a the model at step s, with the hours h, return the number of good correspondance
  public int compareData(int s, int h){
    int v=0;
    if(h>p.rm.MaxTime) return v;
    for(int i=0;i<p.gm.nbGene;i++)
      if(this.SameModelAsData(i,s,h)) v++;
    return v;
  }
  //Return true if the data and the model are similaire for this gene, at this step corresponding to this hour
  //0;No data;
  //1;No expression;
  //2;Weak expression;
  //3;Expressed;
  public boolean SameModelAsData(int i,int s,int h){
      if(h<0 || s<0 || i<0 || i>=this.dom.GenesData.length || h>=this.dom.GenesData[i].length || s>=this.GenesStates[i].length) return true;
      if(this.dom.GenesData[i][h]<=0)return true; //No data, no Comparaison
     
      if(this.dom.GenesData[i][h]>=2 && this.GenesStates[i][s]) return true;  //Both Express
      if(this.dom.GenesData[i][h]==1 && !this.GenesStates[i][s]) return true;  //Both non Express
      return false;
  }
  /*public boolean CompareModelAsData(int i,int h1,int h2){
      if(h1<0 || h2<0 || i<0 || i>=this.GenesData.length || h1>=this.GenesData[i].length || h2>=this.GenesStates[i].length) return true;
      if(this.GenesData[i][h1]<=0)return true; //No data, no Comparaison
      if(this.GenesData[i][h1]>=2 && this.GenesStates[i][s]) return true;  //Both Express
      if(this.GenesData[i][h2]==1 && !this.GenesStates[i][s]) return true;  //Both non Express
      return false;
  }*/
  
  //Return true if this step invovles a Gene a changement  (instedd of a non gene)
  public boolean isStep(int s){
    if(s==0) return false;
    for(int i=0;i<p.gm.nbGene;i++){
      Gene g=p.gm.getGene(i);
      if(g.isGene && this.GenesStates[i][s]!=this.GenesStates[i][s-1]) return true;
    }
    return false;
  }

  
  //Return the value of the specific gene at the step
  public boolean getValue(Gene g, int s){  return this.GenesStates[p.gm.getNumGene(g)][s]; }
  
}
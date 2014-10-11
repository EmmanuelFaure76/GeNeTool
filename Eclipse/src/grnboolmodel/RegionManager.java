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
  // Region.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

public class RegionManager {

  private GRNBoolModel p;
  
  RegionManager(GRNBoolModel p) {
    this.p = p;   
  }  
  boolean DataInModel=false; //Visualize the data in the model
  
  public int MaxTime=0;
  String timeUnit="hpf"; //String for unit
  int dtRegion=3;
  public ArrayList Regions=new ArrayList();
  
  //When we add or remove some hours (nb=1 for add, nb=-1 for delet
  public void changeMaxTime(int nb){
    if(MaxTime+nb>=0) {
        MaxTime+=nb;
        p.mm.addMessage("Change Max Time to " + MaxTime);
      //REGION
      for(int i=0;i<Regions.size();i++){
          Region reg=getRegion(i);
          for(int j=0;j<2;j++) if(reg.hours[j]>MaxTime) reg.hours[j]=MaxTime;
      }
      //GENES Changement
      for(int i=0;i<p.gm.nbGene;i++){
          Gene gene=p.gm.getGene(i);
          int [][]Expression=new int[Regions.size()+1][MaxTime+1]; //30 hours by 21 Regions
          for(int k=0;k<Regions.size();k++) 
            for(int j=0;j<=MaxTime;j++) 
                if(gene.Expression[k].length>j) Expression[k][j]=gene.Expression[k][j];
                else Expression[k][j]=-1;
           gene.Expression=Expression;
      }
      //DOMAINE
      for(int i=0;i<p.dm.nbDomain;i++){
           Domain dom=p.dm.getDomain(i);
           int [][]GenesData=new int[p.gm.nbGene][MaxTime+1];
            for(int k=0;k<p.gm.nbGene;k++) 
                  for(int j=0;j<=MaxTime;j++)
                     if(dom.GenesData[k].length>j) GenesData[k][j]=dom.GenesData[k][j];
                     else  GenesData[k][j]=-1;
           dom.GenesData=GenesData;
      }
      p.dm.computeData();
      if(MaxTime==0) p.mbm.checkEnablelMenu();
    }
    
    
  }
  
  public void addRegion(Region reg){
      Regions.add(reg);
      if(Regions.size()==1) p.mbm.checkEnablelMenu();
      //GENES CHANGEMENT
      for(int i=0;i<p.gm.nbGene;i++){
          Gene gene=p.gm.getGene(i);
          int [][]Expression=new int[Regions.size()+1][MaxTime+1]; //30 hours by 21 Regions
          int nbK=0;
          for(int k=0;k<Regions.size();k++){
             Region region=getRegion(k);
             if(region==reg) { for(int j=0;j<=MaxTime;j++) Expression[k][j]=-1;} //This is the region add Empty Infos
             else{
                for(int j=0;j<=MaxTime;j++) Expression[k][j]=gene.Expression[nbK][j];
                nbK++;
             }
          }
          gene.Expression=Expression;
      }
      
      if(Regions.size()==1){ //FIRST REGION CREATED ADD MENUS
           if(p.gm.nbGene>0 && !p.mm.isActive(4))  p.mm.active(4,0); //ACTIVE EXPRESSION
       }
  }
  
  //If we found the Name of the region before initialize them
  public Region addRegion(String Name){
     Region region=getRegion(Name);
     if(region!=null) return region; //The region with this name already exist
     p.mm.addMessage("Create TEMPORAL Region " + Name  + " at number -1 !!");
     region=new Region(p, Name,-1,-1,-1);
     addRegion(region);
     return region;
  }
  
  public Region addRegion(String Name,int number,int Start,int End){
    Region region=getRegion(Name);
    if(region!=null){  region.number=number;region.hours[0]=Start;region.hours[1]=End; } //The region was already added , just update some informations
    else{
        p.mm.addMessage("Create Region " + Name  + " at number " +number);
        region=new Region(p, Name,number,Start,End);
        addRegion(region);
    }
    return region;
  }
  
  
  
  public void delRegion(Region regg){
    ArrayList nRegions=new ArrayList();
    boolean swit=false;
     for(int i=0;i<Regions.size();i++){
      Region reg=getRegion(i);
      if(swit) reg.number--;
      if(reg!=regg) nRegions.add(reg);
      else swit=true;
     }
     Regions=nRegions;
     
     for(int d=0;d<p.dm.Domains.size();d++) p.dm.getDomain(d).delTree(regg);
     p.dm.computeData();
     if(Regions.size()==0) p.mbm.checkEnablelMenu();
  }
  
  public Region getRegion(int i){ return (Region)Regions.get(i);}
  public Region getRegion(String name){
    if(name.equals("")) return null;
    name=name.toLowerCase();
    for(int i=0;i<Regions.size();i++){
      Region reg=getRegion(i);
      if(reg.Name.toLowerCase().equals(name)) return reg;
    }
    return null;
  }
  
  
  int getRegionNumber(String name) {
    name=name.toLowerCase();
    for(int i=0;i<Regions.size();i++){
      Region reg=getRegion(i);
      if(reg.Name.toLowerCase().equals(name)) return i;
    }
    return -1;
  }
  
  
  //TEMPORARIE
  
  public void initRegion(){
    //Temporal initialisation
    for(int i=0;i<=19;i++){
         int [] hh=getRegionHours(i);
         addRegion(getRegionName(i),i,hh[0],hh[1]);
    }
    
    
  }
  
  String getRegionName(int num) {
    String Name="";
    switch(num) {
    case 0:  Name="Egg";   break; 
    case 1:   Name="Small Microm";   break; 
    case 2:   Name="Skel Microm";   break; 
    case 3:   Name="Macrom";  break; 
    case 4:   Name="V2";   break; 
    case 5:  Name="V2 Meso A";   break; 
    case 6:  Name="V2 Meso O"; break; 
    case 7:  Name="V2 Endo";   break; 
    case 8:   Name="V1"; break; 
    case 9:   Name="V1 Endo A";   break; 
    case 10:   Name="V1 Endo O";   break; 
    case 11:   Name="V1 Ecto A"; break; 
    case 12:   Name="V1 Ecto O";   break; 
    case 13:  Name="Mesom";  break; 
    case 14:  Name="Ecto A";   break; 
    case 15:  Name="Ecto O"; break; 
    case 16:    Name="Stomodeum";   break; 
    case 17:  Name="Oral Face";  break; 
    case 18:  Name="Ciliated Band";   break; 
    case 19:  Name="Apical Plate";   break;
    }
    return Name;
  }
  
  
  int [] getRegionHours(int num){
    int []Hours=new int[2];Hours[0]=-1;Hours[1]=-1;
    switch(num) {
    case 0:  Hours[0]=0;Hours[1]=5;   break; 
    case 1:  Hours[0]=6;Hours[1]=30;     break; 
    case 2:  Hours[0]=6;Hours[1]=30;   break; 
    case 3:  Hours[0]=6;Hours[1]=8;  break; 
    case 4:  Hours[0]=9;Hours[1]=17;   break; 
    case 5:  Hours[0]=18;Hours[1]=30;   break; 
    case 6:  Hours[0]=18;Hours[1]=30;  break; 
    case 7:  Hours[0]=18;Hours[1]=30;    break; 
    case 8:  Hours[0]=9;Hours[1]=20;  break; 
    case 9:   Hours[0]=21;Hours[1]=30;   break; 
    case 10:  Hours[0]=21;Hours[1]=30;   break; 
    case 11:  Hours[0]=21;Hours[1]=30;break; 
    case 12:  Hours[0]=21;Hours[1]=30; break; 
    case 13:  Hours[0]=6;Hours[1]=8; break; 
    case 14:  Hours[0]=9;Hours[1]=30;   break; 
    case 15:  Hours[0]=9;Hours[1]=17; break; 
    case 16:  Hours[0]=18;Hours[1]=30;  break; 
    case 17:  Hours[0]=18;Hours[1]=30;  break; 
    case 18:  Hours[0]=18;Hours[1]=30;   break; 
    case 19:  Hours[0]=9;Hours[1]=30;    break;
    }
    return Hours;
  }
}

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

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MenuCompare.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public class CompareMenuManager {

  private GRNBoolModel p;
  
  CompareMenuManager(GRNBoolModel p) {
    this.p = p;   
  } 

  /////////////////////////////// Compare Specific Step with Data and Model
  //String activeCompareRows="Select";  //Rows
  //String activeCompareLigne="Select"; //Ligne
  String activeCompareRows="Genes";  //Rows
  String activeCompareLigne="Domains"; //Ligne
  String activeCompareThird="Select"; //Active Third Dimension
  int timeCompareBegin=0; int timeCompareEnd=30; //If the Third Dimension is time
  int spaceCompareLine=25;
  
  boolean []hideCompareRows=new boolean[100]; //To Hide some colum
  boolean []hideCompareLigne=new boolean[100];//To hide some ligne
  
  int modeCompareBox=0; //List the different Mode of comparaison, 0-> Heat; 1->Full
  int modeCompareData=0; //List of the different Mode to compare with data
  int compareDataVersion=-2; //List to compare with the models version or the data
  String [] compareOthersDomains=null; //List of mapping to comparae with oothers domains
  String [] OrderCompareOthersDomains=null;//List of mapping to comparae with oothers domains ORDERING
  
  void MenuCompare(int ligneX,int ligneY,int NumMenu){
    
    p.mm.MenuOnglet(ligneX,ligneY,NumMenu,"Compare");
    
    ligneX+=5;
    ligneY+=50;
    
     int idStep=16;
     if(p.mm.MenuActive[NumMenu]==1 && p.lm.MyModel!=null){ 
         
         String [] DimensionsCompare = {"Genes","Domains",p.rm.timeUnit};
         //Rows
         int nbRows=0;
         if(p.toDo==0 && !activeCompareRows.equals("Select"))
          if(p.mm.mouseUnHide(ligneX+105,ligneY-43,11,3,"un-hide all rows"))
              for(int i=0;i<100;i++) hideCompareRows[i]=true;    //UnHide all Rows
            
         if(activeCompareRows.equals("Genes")){
             for(int g=0;g<p.gm.nbGene;g++)  
             if(hideCompareRows[g]){
                   if(p.toDo==0 && p.mm.mouseHide(ligneX+p.dm.SizeDrawDomain+20*(nbRows+1)+3,ligneY-5,9,3,"hide this row"))hideCompareRows[g]=false;
                   p.gm.getGene(g).draw(ligneX+p.dm.SizeDrawDomain+20*(nbRows+1),ligneY,true,p.gm.SizeDrawGene,false,false);
                   p.noFill(); p.stroke(p.cm.colorBoxGene);  ;   p.rect(ligneX+p.dm.SizeDrawDomain+20*(nbRows+1),ligneY,20,p.gm.SizeDrawGene);
                   nbRows++;
             }
         }
         if(activeCompareRows.equals("Domains")){
              for(int d=0;d<p.dm.nbDomain;d++) 
                if(hideCompareRows[d]){
                   if(p.toDo==0 && p.mm.mouseHide(ligneX+p.dm.SizeDrawDomain+20*(nbRows+1)+3,ligneY-5,9,3,"hide this row"))hideCompareRows[d]=false;
                   p.pushMatrix();    p.translate(ligneX+p.dm.SizeDrawDomain+20*(nbRows+1),ligneY-4);
                   p.fill(p.cm.colorBoxGene,100); p.stroke(p.cm.colorBoxGene);  ;   p.rect(0,5,20,p.dm.SizeDrawDomain);
                   p.rotate(-p.PI/2);  p.fill(p.cm.Textcolor);  p.text(p.lm.MyModel.getDomain(d).getName(),-p.dm.SizeDrawDomain+2, 14);
                    p.popMatrix(); 
                   nbRows++;
                }
           }
         if(activeCompareRows.equals(p.rm.timeUnit)){
            for(int s=0;s<=p.lm.MyModel.step;s++) 
                 if(hideCompareRows[s]){
                   if(p.toDo==0 && p.mm.mouseHide(ligneX+p.dm.SizeDrawDomain+20*(nbRows+1)+2,ligneY-17,9,3,"hide this row"))hideCompareRows[s]=false;
                   p.pushMatrix();    p.translate(ligneX+p.dm.SizeDrawDomain+20*(nbRows+1),ligneY-4);
                   p.fill(p.cm.colorBoxGene,100); p.stroke(p.cm.colorBoxGene);  ;   p.rect(0,5,20,p.dm.SizeDrawDomain);
                   p.rotate(-p.PI/2); p.fill(p.cm.Textcolor); String hpf=""+p.lm.MyModel.Correspons[s]; p.text(hpf,-p.dm.SizeDrawDomain+2, 14);
                   p.popMatrix(); 
                   nbRows++;
               }
          }
  
         
         //Ligne
          int nbLignes=0;
          if(p.toDo==0 && !activeCompareLigne.equals("Select"))
          if(p.mm.mouseUnHide(ligneX+10,ligneY+72,11,3,"un-hide all lignes")) 
            for(int i=0;i<100;i++) hideCompareLigne[i]=true;  //UnHide all lignes
         
         if(activeCompareLigne.equals("Domains")){
                 if(compareDataVersion==-1){ //Add the possibilitie to Compare with an other Domainss
                      int nbMaxLignes=0;  for(int d=p.dm.nbDomain-1;d>=0;d--)   if(hideCompareLigne[d])nbMaxLignes++; //Count the number of lignes
                      nbMaxLignes--;
                      for(int d=p.dm.nbDomain-1;d>=0;d--)  //Draw it in the opposite Direction 
                         if(hideCompareLigne[d]){
                             if(p.toDo==0 && p.mm.mouseHide(ligneX+5,ligneY+p.dm.SizeDrawDomain+nbMaxLignes*spaceCompareLine+8,9,3,"hide this ligne"))hideCompareLigne[d]=false;
                             p.textFont(p.cm.myFont,10);
                             p.pushMatrix(); p.translate(ligneX+20,ligneY+p.dm.SizeDrawDomain+nbMaxLignes*spaceCompareLine+2);
                             float  f=0.70F;   int colorBoxGeneE=p.color(p.red(p.cm.colorBoxGene)/f,p.green(p.cm.colorBoxGene)/f,p.blue(p.cm.colorBoxGene)/f);
                             p.fill(colorBoxGeneE); p.stroke(p.cm.colorBoxGene);  p.rect(0,0,p.dm.SizeDrawDomain,20);
                             p.fill(p.cm.Textcolor); p.noStroke(); p.text(p.lm.MyModel.getDomain(d).getName(),0,8);
                             p.popMatrix(); 
                      
                             int OtherDomainsComp=p.mm.MenuDefile(p.round(ligneX+18+p.dm.SizeDrawDomain-p.textWidth(compareOthersDomains[d])),ligneY+p.dm.SizeDrawDomain+nbMaxLignes*spaceCompareLine+10,p.round(p.textWidth(compareOthersDomains[d]))+2,12,colorBoxGeneE,compareOthersDomains[d],OrderCompareOthersDomains); //Menu Select Third Domains Dimension
                             if(OtherDomainsComp>0) compareOthersDomains[d]=OrderCompareOthersDomains[OtherDomainsComp-1];
                             p.textFont(p.cm.myFont,12);
                            nbMaxLignes--;
                       }
                 }
                 else{
                          for(int d=0;d<p.dm.nbDomain;d++)   //Normal Presentation of the list of domains
                            if(hideCompareLigne[d]){
                                  if(p.toDo==0 && p.mm.mouseHide(ligneX+5,ligneY+p.dm.SizeDrawDomain+nbLignes*spaceCompareLine+8,9,3,"hide this ligne"))hideCompareLigne[d]=false;
                                  ModelDomain md=p.lm.MyModel.getDomain(d);
                                  p.pushMatrix(); p.translate(ligneX+20,ligneY+p.dm.SizeDrawDomain+nbLignes*spaceCompareLine+2);
                                  p.fill(p.cm.colorBoxGene,100); p.stroke(p.cm.colorBoxGene);  p.rect(0,0,p.dm.SizeDrawDomain,20);
                                  p.fill(p.cm.Textcolor); p.noStroke(); p.text(md.getName(),(p.dm.SizeDrawDomain-p.textWidth(md.getName()))/2,12);
                                  p.popMatrix(); 
                                  nbLignes++;
                          }
                   }
           }
           
           
         if(activeCompareLigne.equals("Genes")){
             for(int g=0;g<p.gm.nbGene;g++)  
             if(hideCompareLigne[g]){
                   if(p.toDo==0 && p.mm.mouseHide(ligneX+5,ligneY+p.dm.SizeDrawDomain+nbLignes*spaceCompareLine+8,9,3,"hide this ligne"))hideCompareLigne[g]=false;
                   p.gm.getGene(g).draw(ligneX+20,ligneY+p.dm.SizeDrawDomain+nbLignes*spaceCompareLine+2,false,p.gm.SizeDrawGene,true,false); 
                   nbLignes++;
             }
         }
         
          if(activeCompareLigne.equals(p.rm.timeUnit)){
            for(int s=0;s<=p.lm.MyModel.step;s++) 
                 if(hideCompareLigne[s]){
                    if(p.toDo==0 && p.mm.mouseHide(ligneX,ligneY+p.dm.SizeDrawDomain+nbLignes*spaceCompareLine+2,9,3,"hide this ligne"))hideCompareLigne[s]=false;
                    p.pushMatrix();   p.translate(ligneX+20,ligneY+p.dm.SizeDrawDomain+nbLignes*spaceCompareLine+2);
                    p.fill(p.cm.colorBoxGene,100); p.stroke(p.cm.colorBoxGene);  ;   p.rect(0,0,p.dm.SizeDrawDomain,20);
                    p.fill(p.cm.Textcolor); String hpf=""+p.lm.MyModel.Correspons[s]; p.text(hpf,p.dm.SizeDrawDomain-p.textWidth(hpf)-2,12);
                    p.popMatrix(); 
                    nbLignes++;
                 }
          }
           
        p.stroke(p.cm.Textcolor,150);  
      
         //Mode of Box Visualisation
         if(p.toDo==0){
           int cHeat=p.color((int)(p.red(p.cm.colorGeneOff)*0.5 + p.red(p.cm.colorGeneOn)*0.5), (int)(p.green(p.cm.colorGeneOff)*0.5+ p.green(p.cm.colorGeneOn)*0.5), (int)(p.blue(p.cm.colorGeneOff)*0.5 + p.blue(p.cm.colorGeneOn)*0.5), (int)(p.alpha(p.cm.colorGeneOff)*0.5 + p.alpha(p.cm.colorGeneOn)*0.5));  
         p.fill(cHeat);  p.rect(ligneX,ligneY-10,18,18);
         if(p.eh.mousePressOn(ligneX,ligneY-10,18,18))modeCompareBox=0;  //HEAT FORMAT
         
         p.fill(p.cm.colorGeneOn);   p.rect(ligneX+20,ligneY-10,18,18);
         if(p.eh.mousePressOn(ligneX+20,ligneY-10,18,18))modeCompareBox=1;  //ON OR OFF FORMAT
        
         //Size of ligne Separation
         p.noFill();  p.rect(ligneX+40,ligneY-10,18,9);  p.rect(ligneX+40,ligneY-1,18,9);  
         if(p.eh.mousePressOn(ligneX+40,ligneY-10,18,18))spaceCompareLine--;; //ECART OF LINE
         
          p.noFill();  p.rect(ligneX+60,ligneY-10,18,7);  p.rect(ligneX+60,ligneY+1,18,7);  
         if(p.eh.mousePressOn(ligneX+60,ligneY-10,18,18))spaceCompareLine++; //ECART OF LINE
         
         
         
         //Mode of Comparaison with Data
         p.noFill();  p.rect(ligneX,ligneY+10,18,18);
         if(p.eh.mousePressOn(ligneX,ligneY+10,18,18)) modeCompareData=0;  //NO DATA COMPARAISON
         
         p.noStroke(); p.fill(p.cm.colorGeneOff);    p.beginShape(); p.vertex(ligneX+38,ligneY+28); p.vertex(ligneX+38,ligneY+10); p.vertex(ligneX+20,ligneY+28); p.endShape(p.CLOSE);
         p.fill(p.cm.colorGeneOn);   p.beginShape(); p.vertex(ligneX+20,ligneY+10); p.vertex(ligneX+38,ligneY+10); p.vertex(ligneX+20,ligneY+28); p.endShape(p.CLOSE);
         p.noFill(); p.stroke(p.cm.Textcolor,150); p.rect(ligneX+20,ligneY+10,18,18);
         if(p.eh.mousePressOn(ligneX+20,ligneY+10,18,18))modeCompareData=1;  //COMPARAISON WITH HALF TRIANGLE
         
         p.noStroke(); p.fill(p.cm.colorGeneOff);    p.beginShape(); p.vertex(ligneX+58,ligneY+28); p.vertex(ligneX+58,ligneY+10); p.vertex(ligneX+40,ligneY+28); p.endShape(p.CLOSE);
         p.fill(cHeat);   p.beginShape(); p.vertex(ligneX+40,ligneY+10); p.vertex(ligneX+58,ligneY+10); p.vertex(ligneX+40,ligneY+28); p.endShape(p.CLOSE);
         p.noFill(); p.stroke(p.cm.Textcolor,150); p.rect(ligneX+40,ligneY+10,18,18);
         if(p.eh.mousePressOn(ligneX+40,ligneY+10,18,18))modeCompareData=2;  //COMPARAISON WITH HALF TRIANGLE HEAT
         
         
         p.fill(p.cm.colorGeneOn);  p.rect(ligneX+60,ligneY+10,18,18); p.fill(p.cm.Textcolor); p.rect(ligneX+74,ligneY+10,4,18);
         if(p.eh.mousePressOn(ligneX+60,ligneY+10,18,18))modeCompareData=3; //COMPARAISON WITH LITTLE BALCK BAR
         
         p.noStroke();  p.fill(p.cm.colorGeneOn);  p.rect(ligneX+80,ligneY+10,18,8);  p.fill(p.cm.colorGeneOff);  p.rect(ligneX+80,ligneY+20,18,8);  p.stroke(p.cm.Textcolor,150);  p.noFill(); p.rect(ligneX+80,ligneY+10,18,18); 
         if(p.eh.mousePressOn(ligneX+80,ligneY+10,18,18))modeCompareData=4; //COMPARAISON WITH 2 BOX WITH NO STROKE
         
        
         }
         
         if(!activeCompareRows.equals("Select") && !activeCompareLigne.equals("Select")){ 
           
            int nbR=0;
            if(activeCompareRows.equals("Genes"))  nbR=p.gm.nbGene;
            if(activeCompareRows.equals("Domains"))  nbR=p.dm.nbDomain;
            if(activeCompareRows.equals(p.rm.timeUnit))  nbR=p.lm.MyModel.step+1;
            
            int nbL=0;
            if(activeCompareLigne.equals("Genes"))  nbL=p.gm.nbGene;
            if(activeCompareLigne.equals("Domains"))  nbL=p.dm.nbDomain;
            if(activeCompareLigne.equals(p.rm.timeUnit))  nbL=p.lm.MyModel.step+1;
            
            nbRows=0;
            ModelDomain md=null; int g=-1;  Gene gg=null;
            int s=-1; 
            ModelDomain mdCompare=null; 
           
            if((activeCompareRows.equals("Genes") && activeCompareLigne.equals(p.rm.timeUnit)) || (activeCompareRows.equals(p.rm.timeUnit) && activeCompareLigne.equals("Genes"))) {
                md=p.lm.MyModel.getDomain(activeCompareThird);
                if(compareDataVersion>=0) mdCompare=p.lm.MyModels[compareDataVersion].getDomain(activeCompareThird);
            }
            // FIXME
            //FIXME BUG Array OOB when user selects hpf-> activeCompareThird equals 'Select', -1 on the gene number
            if((activeCompareRows.equals("Domains") && activeCompareLigne.equals(p.rm.timeUnit)) || (activeCompareRows.equals(p.rm.timeUnit) && activeCompareLigne.equals("Domains"))){
                  g=p.gm.getNumGene(activeCompareThird);  gg=p.gm.getGene(g);
            }
            
           
            for(int i=0;i<nbR;i++)
               if(hideCompareRows[i]){
                   if(activeCompareRows.equals("Domains"))  {
                     md=p.lm.MyModel.getDomain(i);
                     if(compareDataVersion>=0) mdCompare=p.lm.MyModels[compareDataVersion].getDomain(i);
                     if(compareDataVersion==-1)  mdCompare=p.lm.MyModel.getDomain(compareOthersDomains[i]); 
                   }
                   else if(activeCompareRows.equals("Genes"))  { g=i; gg=p.gm.getGene(g);}
                   else if(activeCompareRows.equals(p.rm.timeUnit)) s=i;
                  
                   nbLignes=0;
                   for(int j=0;j<nbL;j++)
                         if(hideCompareLigne[j]){
                             if(activeCompareLigne.equals("Domains"))  {
                                 md=p.lm.MyModel.getDomain(j);
                                  if(compareDataVersion>=0) mdCompare=p.lm.MyModels[compareDataVersion].getDomain(j);
                                  if(compareDataVersion==-1) mdCompare=p.lm.MyModel.getDomain(compareOthersDomains[j]); 
                             }
                             else if(activeCompareLigne.equals("Genes"))  { g=j; gg=p.gm.getGene(g);}
                             else if(activeCompareLigne.equals(p.rm.timeUnit))    s=j;
                             
                             if(md!=null && g>=0){
                             int c=p.cm.colorBoxBackground;
                             float XX=ligneX+p.dm.SizeDrawDomain+20*(nbRows+1); float YY=ligneY+p.gm.SizeDrawGene+nbLignes*spaceCompareLine+2;
                             //Heat Color Bar
                             boolean sameAsData=true;
                             int nbExpress=0; int nbNotExpress=0;
                             int nbDataExpress=0; int nbDataNotExpress=0; int nbDataDiff=0;
                             boolean same=false;
                             int TtimeCompareBegin=timeCompareBegin; int TtimeCompareEnd=timeCompareEnd;
                             if(activeCompareLigne.equals(p.rm.timeUnit) || activeCompareRows.equals(p.rm.timeUnit)){   TtimeCompareBegin=p.lm.MyModel.Correspons[s]; TtimeCompareEnd=p.lm.MyModel.Correspons[s];} //COMPARE ONLY AT ONE TIME
  
                              boolean isManual=false;
                              for(s=0;s<=p.lm.MyModel.step;s++) 
                                 if(p.lm.MyModel.Correspons[s]>=TtimeCompareBegin && p.lm.MyModel.Correspons[s]<=TtimeCompareEnd){
                                      same=true;
                                      if(md.Manual[g][s] || (compareDataVersion>=-1 && mdCompare.Manual[g][s])) isManual=true;
                                      if(compareDataVersion>=-1){    if(md.GenesStates[g][s]!=mdCompare.GenesStates[g][s]) same=false;  }  //Compare  with an other Domains or other version
                                      else if( md.dom.GenesData[g][s]!=4 && !md.SameModelAsData(g,s,p.lm.MyModel.Correspons[s]))same=false;  //Compare with data
                                   
                                      if(md.GenesStates[g][s]){
                                         nbExpress++; 
                                         if(!same) {nbDataDiff++; sameAsData=false;  nbDataNotExpress++; }else nbDataExpress++;
                                        }
                                        else{
                                          nbNotExpress++; 
                                          if(!same) {nbDataDiff++; sameAsData=false;  nbDataExpress++; }else nbDataNotExpress++;
                                       }
                                }
               
                              //Draw the Box
                              if(modeCompareBox==0) c=p.color(p.red(p.cm.colorGeneOff)*nbNotExpress/(nbNotExpress+nbExpress) + p.red(p.cm.colorGeneOn)*nbExpress/(nbNotExpress+nbExpress), p.green(p.cm.colorGeneOff)*nbNotExpress/(nbNotExpress+nbExpress) + p.green(p.cm.colorGeneOn)*nbExpress/(nbNotExpress+nbExpress) , p.blue(p.cm.colorGeneOff)*nbNotExpress/(nbNotExpress+nbExpress) + p.blue(p.cm.colorGeneOn)*nbExpress/(nbNotExpress+nbExpress), p.alpha(p.cm.colorGeneOff)*nbNotExpress/(nbNotExpress+nbExpress) + p.alpha(p.cm.colorGeneOn)*nbExpress/(nbNotExpress+nbExpress) );  
                              if(modeCompareBox==1) {if(nbExpress>0) c=p.cm.colorGeneOn; else c=p.cm.colorGeneOff;}
                              
                              switch(modeCompareData){
                                case 0 :  p.fill(c);p.stroke(p.cm.Textcolor,150); p.rect(XX,YY,20,20); //NO DATA COMPARAISON
                                      break;
                                 case 1 :  //TRIANGLE
                                  if(sameAsData) {p.fill(c);p.stroke(p.cm.Textcolor,150);  p.rect(XX,YY,20,20);} //STROKE ONLY  
                                  else{
                                     int datacolor=p.cm.colorBoxBackground;
                                     if(modeCompareBox==0) datacolor=p.color(p.red(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.red(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress), p.green(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.green(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , p.blue(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.blue(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , p.alpha(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.alpha(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) );  
                                     else if(modeCompareBox==1) {if(nbDataExpress>0) datacolor=p.cm.colorGeneOn; else datacolor=p.cm.colorGeneOff;}
                                     
                                     if(modeCompareBox==1 && ( ( nbExpress>0 && nbDataExpress>0) ||   ( nbExpress==0 && nbDataExpress==0)))
                                       {p.fill(c);  p.stroke(p.cm.Textcolor,150); p.rect(XX,YY,20,20);}
                                    else{
                                       p.noStroke();
                                       p.fill(c);    p.beginShape(); p.vertex(XX,YY); p.vertex(XX+20,YY); p.vertex(XX,YY+20); p.endShape(p.CLOSE);
                                       p.fill(datacolor);   p.beginShape();p.vertex(XX+20,YY+20); p.vertex(XX+20,YY); p.vertex(XX,YY+20);p.endShape(p.CLOSE);
                                       p.noFill();  p.stroke(p.cm.Textcolor,150); p.rect(XX,YY,20,20); //STROKE ONLY
                                    }
                                  }
                                break;
                                case 2 :
                                  if(sameAsData){p.fill(c);p.stroke(p.cm.Textcolor,150); p.rect(XX,YY,20,20);} //STROKE ONLY  
                                  else{
                                     if(modeCompareBox==1 && ( ( nbExpress>0 && nbDataExpress>0) ||   ( nbExpress==0 && nbDataExpress==0)))
                                        {p.fill(c);p.stroke(p.cm.Textcolor,150); p.rect(XX,YY,20,20);} 
                                    else{
                                        int datacolor=p.color(p.red(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.red(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress), p.green(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.green(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , p.blue(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.blue(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , p.alpha(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.alpha(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) );  
                                     
                                       p.noStroke();
                                       p.fill(c);    p.beginShape(); p.vertex(XX,YY); p.vertex(XX+20,YY); p.vertex(XX,YY+20); p.endShape(p.CLOSE);
                                       p.fill(datacolor);   p.beginShape();p.vertex(XX+20,YY+20); p.vertex(XX+20,YY); p.vertex(XX,YY+20);p.endShape(p.CLOSE);
                                       p.noFill();  p.stroke(p.cm.Textcolor,150); p.rect(XX,YY,20,20); //STROKE ONLY
                                    }
                                  }
                                break;
                                case 3 :   //SMALL BLACK BAR
                                    p.fill(c); p.stroke(p.cm.Textcolor,150); p.rect(XX,YY,20,20);
                                   if(!sameAsData) {
                                      p.noStroke(); 
                                      if(modeCompareBox==0) {if(nbNotExpress+nbExpress==1) {p.fill(p.cm.Textcolor); p.rect(XX,YY,20,20); } else{p.fill(p.cm.Textcolor,nbDataDiff*255/(nbNotExpress+nbExpress)); p.rect(XX+15,YY,5,20); }}
                                      if(modeCompareBox==1 && ( (nbExpress>0 && nbDataExpress==0) ||   ( nbExpress==0 && nbDataExpress>0))){p.fill(p.cm.Textcolor); p.rect(XX+15,YY,5,20); }
                                   }
                                break;
                                 case 4 :  //DIVIDE IN 2 SUB BOX WITHOUT STROKE
                                     p.noFill(); p.stroke(p.cm.Textcolor,150); p.rect(XX,YY,20,20); 
                                     p.fill(c);p.noStroke(); p.rect(XX,YY,20,10);
                                     int datacolor=p.cm.colorBoxBackground;
                                     if(modeCompareBox==0) datacolor=p.color(p.red(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.red(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress), p.green(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.green(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , p.blue(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.blue(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress), p.alpha(p.cm.colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + p.alpha(p.cm.colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) );  
                                     else if(modeCompareBox==1) {if(nbDataExpress>0) datacolor=p.cm.colorGeneOn; else datacolor=p.cm.colorGeneOff;}
                                     p.fill(datacolor); p.rect(XX,YY+10,20,10); 
                                 break;
                              }
                              
                              if(gg.isUnknown || isManual)  {p.noFill();p.stroke(p.cm.colorUnknownGene); p.rect(XX,YY,20,20); }
                             }
                              nbLignes++;
                         }
                      nbRows++;
               }
               
         }
         
         if(p.toDo==0){
            //ROWS MENU
            int RowsComp=p.mm.MenuDefile(ligneX+120,ligneY-45,60,20,p.cm.colorBackground,activeCompareRows,DimensionsCompare); //Menu Select Rows Dimension
            if(RowsComp>0) {activeCompareRows=DimensionsCompare[RowsComp-1];activeCompareThird="Select";}
            //LIGNE MENU
            int LigneComp=p.mm.MenuDefile(ligneX+30,ligneY+70,60,20,p.cm.colorBackground,activeCompareLigne,DimensionsCompare);
            if(LigneComp>0){ activeCompareLigne=DimensionsCompare[LigneComp-1];activeCompareThird="Select";}
         }
         
         //MENU Third Dimension
        
         //Time
         if((activeCompareRows.equals("Genes") && activeCompareLigne.equals("Domains")) || (activeCompareRows.equals("Domains") && activeCompareLigne.equals("Genes"))){ 
           p.fill(p.cm.Textcolor); p.text("From " + timeCompareBegin + " to "+ timeCompareEnd +" "+p.rm.timeUnit,ligneX,ligneY+50);
           if(p.eh.mouseOn(ligneX+p.textWidth("From"),ligneY+36,p.textWidth(" "+timeCompareBegin+" "),20) && p.keyPressed){
              if(p.keyCode==p.UP)  timeCompareBegin++; if(p.keyCode==p.DOWN)  timeCompareBegin--;           
           }
           if(p.eh.mouseOn(ligneX+p.textWidth("From " + timeCompareBegin + " to "),ligneY+36,p.textWidth(" "+timeCompareEnd+" "),20) && p.keyPressed){
              if(p.keyCode==p.UP)  timeCompareEnd++; if(p.keyCode==p.DOWN)  timeCompareEnd--;
           }
         }
         //Domains
         if((activeCompareRows.equals("Genes") && activeCompareLigne.equals(p.rm.timeUnit)) || (activeCompareRows.equals(p.rm.timeUnit) && activeCompareLigne.equals("Genes"))){ 
           String [] ThirdDomains = new String[p.dm.nbDomain]; for(int i=0;i<p.dm.nbDomain;i++) ThirdDomains[i]=p.lm.MyModel.getDomain(i).getName();
           int ThirdComp=p.mm.MenuDefile(ligneX,ligneY+40,p.dm.SizeDrawDomain,20,p.cm.colorBackground,activeCompareThird,ThirdDomains); //Menu Select Third Domains Dimension
           if(ThirdComp>0) activeCompareThird=ThirdDomains[ThirdComp-1];
         }
         //Genes
         if((activeCompareRows.equals("Domains") && activeCompareLigne.equals(p.rm.timeUnit)) || (activeCompareRows.equals(p.rm.timeUnit) && activeCompareLigne.equals("Domains"))){ 
           String [] ThirdGenes = new String[p.gm.nbGene]; for(int i=0;i<p.gm.nbGene;i++) ThirdGenes[i]=p.gm.getGene(i).Name;
           int ThirdComp=p.mm.MenuDefile(ligneX,ligneY+40,p.dm.SizeDrawDomain,20,p.cm.colorBackground,activeCompareThird,ThirdGenes); //Menu Select Third Domains Dimension
           if(ThirdComp>0) activeCompareThird=ThirdGenes[ThirdComp-1];
         }
         //Compare with Data or others Domains or Version ?
       
                String [] modelVersionList=new String[p.lm.MyModels.length];
                for(int i=0;i<p.lm.MyModels.length;i++) modelVersionList[i]=p.lm.MyModels[i].Name;
                int VersionComp=p.mm.MenuDefile(ligneX+370,ligneY-42,150,20,p.cm.colorBackground,p.lm.MyModel.Name,modelVersionList);
                if(VersionComp>0) {
                   p.lm.MyModel=p.lm.MyModels[VersionComp-1];
                   p.lm.MyModel.ActiveDomain=p.lm.MyModel.getDomain(0);
                 }
                
                p.fill(p.cm.Textcolor);  p.text("/",ligneX+500,ligneY-30); 
                modelVersionList=new String[p.lm.MyModels.length+2];modelVersionList[0]="data";modelVersionList[1]="Domains";
                for(int i=0;i<p.lm.MyModels.length;i++) modelVersionList[i+2]=p.lm.MyModels[i].Name;
                VersionComp=p.mm.MenuDefile(ligneX+520,ligneY-42,60,20,p.cm.colorBackground,modelVersionList[compareDataVersion+2],modelVersionList);
                if(VersionComp>0){ 
                    compareDataVersion=VersionComp-3;
                    if(compareDataVersion==-1){ //First Initialisation of Compare with Others Domains
                          if(compareOthersDomains==null) {
                             compareOthersDomains=new String[p.dm.nbDomain]; 
                             OrderCompareOthersDomains=new String[p.dm.nbDomain]; 
                             for(int d=0;d<p.dm.nbDomain;d++){  ModelDomain md=p.lm.MyModel.getDomain(d);compareOthersDomains[d]=md.getName();OrderCompareOthersDomains[d]=md.getName();}
                          }      
                    }
                }
           
      
         p.mm.ResizeMenuXY(NumMenu,0,p.dm.SizeDrawDomain+20*(nbRows+1)+30);  //Resize Menu X
         p.mm.ResizeMenuXY(NumMenu,1,p.gm.SizeDrawGene+(nbLignes+1)*spaceCompareLine+60);  //Resize Menu Y
     }
  }  
}

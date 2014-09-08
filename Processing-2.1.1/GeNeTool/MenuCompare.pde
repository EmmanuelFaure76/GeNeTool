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
  
  MenuOnglet(ligneX,ligneY,NumMenu,"Compare");
  
  ligneX+=5;
  ligneY+=50;
  
   int idStep=16;
   if(MenuActive[NumMenu]==1 && MyModel!=null){ 
       
       String [] DimensionsCompare = {"Genes","Domains",timeUnit};
       //Rows
       int nbRows=0;
       if(toDo==0 && !activeCompareRows.equals("Select"))
        if(mouseUnHide(ligneX+105,ligneY-43,11,3,"un-hide all rows"))
            for(int i=0;i<100;i++) hideCompareRows[i]=true;    //UnHide all Rows
          
       if(activeCompareRows.equals("Genes")){
           for(int g=0;g<nbGene;g++)  
           if(hideCompareRows[g]){
                 if(toDo==0 && mouseHide(ligneX+SizeDrawDomain+20*(nbRows+1)+3,ligneY-5,9,3,"hide this row"))hideCompareRows[g]=false;
                 getGene(g).draw(ligneX+SizeDrawDomain+20*(nbRows+1),ligneY,true,SizeDrawGene,false,false);
                 noFill(); stroke(colorBoxGene);  ;   rect(ligneX+SizeDrawDomain+20*(nbRows+1),ligneY,20,SizeDrawGene);
                 nbRows++;
           }
       }
       if(activeCompareRows.equals("Domains")){
            for(int d=0;d<nbDomain;d++) 
              if(hideCompareRows[d]){
                 if(toDo==0 && mouseHide(ligneX+SizeDrawDomain+20*(nbRows+1)+3,ligneY-5,9,3,"hide this row"))hideCompareRows[d]=false;
                 pushMatrix();    translate(ligneX+SizeDrawDomain+20*(nbRows+1),ligneY-4);
                 fill(colorBoxGene,100); stroke(colorBoxGene);  ;   rect(0,5,20,SizeDrawDomain);
                 rotate(-PI/2);  fill(Textcolor);  text(MyModel.getDomain(d).getName(),-SizeDrawDomain+2, 14);
                  popMatrix(); 
                 nbRows++;
              }
         }
       if(activeCompareRows.equals(timeUnit)){
          for(int s=0;s<=MyModel.step;s++) 
               if(hideCompareRows[s]){
                 if(toDo==0 && mouseHide(ligneX+SizeDrawDomain+20*(nbRows+1)+2,ligneY-17,9,3,"hide this row"))hideCompareRows[s]=false;
                 pushMatrix();    translate(ligneX+SizeDrawDomain+20*(nbRows+1),ligneY-4);
                 fill(colorBoxGene,100); stroke(colorBoxGene);  ;   rect(0,5,20,SizeDrawDomain);
                 rotate(-PI/2); fill(Textcolor); String hpf=""+MyModel.Correspons[s]; text(hpf,-SizeDrawDomain+2, 14);
                 popMatrix(); 
                 nbRows++;
             }
        }

       
       //Ligne
        int nbLignes=0;
        if(toDo==0 && !activeCompareLigne.equals("Select"))
        if(mouseUnHide(ligneX+10,ligneY+72,11,3,"un-hide all lignes")) 
          for(int i=0;i<100;i++) hideCompareLigne[i]=true;  //UnHide all lignes
       
       if(activeCompareLigne.equals("Domains")){
               if(compareDataVersion==-1){ //Add the possibilitie to Compare with an other Domainss
                    int nbMaxLignes=0;  for(int d=nbDomain-1;d>=0;d--)   if(hideCompareLigne[d])nbMaxLignes++; //Count the number of lignes
                    nbMaxLignes--;
                    for(int d=nbDomain-1;d>=0;d--)  //Draw it in the opposite Direction 
                       if(hideCompareLigne[d]){
                           if(toDo==0 && mouseHide(ligneX+5,ligneY+SizeDrawDomain+nbMaxLignes*spaceCompareLine+8,9,3,"hide this ligne"))hideCompareLigne[d]=false;
                           textFont(myFont,10);
                           pushMatrix(); translate(ligneX+20,ligneY+SizeDrawDomain+nbMaxLignes*spaceCompareLine+2);
                           float  f=0.70;   color colorBoxGeneE=color(red(colorBoxGene)/f,green(colorBoxGene)/f,blue(colorBoxGene)/f);
                           fill(colorBoxGeneE); stroke(colorBoxGene);  rect(0,0,SizeDrawDomain,20);
                           fill(Textcolor); noStroke(); text(MyModel.getDomain(d).getName(),0,8);
                           popMatrix(); 
                    
                           int OtherDomainsComp=MenuDefile(round(ligneX+18+SizeDrawDomain-textWidth(compareOthersDomains[d])),ligneY+SizeDrawDomain+nbMaxLignes*spaceCompareLine+10,round(textWidth(compareOthersDomains[d]))+2,12,colorBoxGeneE,compareOthersDomains[d],OrderCompareOthersDomains); //Menu Select Third Domains Dimension
                           if(OtherDomainsComp>0) compareOthersDomains[d]=OrderCompareOthersDomains[OtherDomainsComp-1];
                           textFont(myFont,12);
                          nbMaxLignes--;
                     }
               }
               else{
                        for(int d=0;d<nbDomain;d++)   //Normal Presentation of the list of domains
                          if(hideCompareLigne[d]){
                                if(toDo==0 && mouseHide(ligneX+5,ligneY+SizeDrawDomain+nbLignes*spaceCompareLine+8,9,3,"hide this ligne"))hideCompareLigne[d]=false;
                                ModelDomain md=MyModel.getDomain(d);
                                pushMatrix(); translate(ligneX+20,ligneY+SizeDrawDomain+nbLignes*spaceCompareLine+2);
                                fill(colorBoxGene,100); stroke(colorBoxGene);  rect(0,0,SizeDrawDomain,20);
                                fill(Textcolor); noStroke(); text(md.getName(),(SizeDrawDomain-textWidth(md.getName()))/2,12);
                                popMatrix(); 
                                nbLignes++;
                        }
                 }
         }
         
         
       if(activeCompareLigne.equals("Genes")){
           for(int g=0;g<nbGene;g++)  
           if(hideCompareLigne[g]){
                 if(toDo==0 && mouseHide(ligneX+5,ligneY+SizeDrawDomain+nbLignes*spaceCompareLine+8,9,3,"hide this ligne"))hideCompareLigne[g]=false;
                 getGene(g).draw(ligneX+20,ligneY+SizeDrawDomain+nbLignes*spaceCompareLine+2,false,SizeDrawGene,true,false); 
                 nbLignes++;
           }
       }
       
        if(activeCompareLigne.equals(timeUnit)){
          for(int s=0;s<=MyModel.step;s++) 
               if(hideCompareLigne[s]){
                  if(toDo==0 && mouseHide(ligneX,ligneY+SizeDrawDomain+nbLignes*spaceCompareLine+2,9,3,"hide this ligne"))hideCompareLigne[s]=false;
                  pushMatrix();   translate(ligneX+20,ligneY+SizeDrawDomain+nbLignes*spaceCompareLine+2);
                  fill(colorBoxGene,100); stroke(colorBoxGene);  ;   rect(0,0,SizeDrawDomain,20);
                  fill(Textcolor); String hpf=""+MyModel.Correspons[s]; text(hpf,SizeDrawDomain-textWidth(hpf)-2,12);
                  popMatrix(); 
                  nbLignes++;
               }
        }
         
      stroke(Textcolor,150);  
    
       //Mode of Box Visualisation
       if(toDo==0){
         color cHeat=color(red(colorGeneOff)*0.5 + red(colorGeneOn)*0.5, green(colorGeneOff)*0.5+ green(colorGeneOn)*0.5, blue(colorGeneOff)*0.5 + blue(colorGeneOn)*0.5, alpha(colorGeneOff)*0.5 + alpha(colorGeneOn)*0.5);  
       fill(cHeat);  rect(ligneX,ligneY-10,18,18);
       if(mousePressOn(ligneX,ligneY-10,18,18))modeCompareBox=0;  //HEAT FORMAT
       
       fill(colorGeneOn);   rect(ligneX+20,ligneY-10,18,18);
       if(mousePressOn(ligneX+20,ligneY-10,18,18))modeCompareBox=1;  //ON OR OFF FORMAT
      
       //Size of ligne Separation
       noFill();  rect(ligneX+40,ligneY-10,18,9);  rect(ligneX+40,ligneY-1,18,9);  
       if(mousePressOn(ligneX+40,ligneY-10,18,18))spaceCompareLine--;; //ECART OF LINE
       
        noFill();  rect(ligneX+60,ligneY-10,18,7);  rect(ligneX+60,ligneY+1,18,7);  
       if(mousePressOn(ligneX+60,ligneY-10,18,18))spaceCompareLine++; //ECART OF LINE
       
       
       
       //Mode of Comparaison with Data
       noFill();  rect(ligneX,ligneY+10,18,18);
       if(mousePressOn(ligneX,ligneY+10,18,18)) modeCompareData=0;  //NO DATA COMPARAISON
       
       noStroke(); fill(colorGeneOff);    beginShape(); vertex(ligneX+38,ligneY+28); vertex(ligneX+38,ligneY+10); vertex(ligneX+20,ligneY+28); endShape(CLOSE);
       fill(colorGeneOn);   beginShape(); vertex(ligneX+20,ligneY+10); vertex(ligneX+38,ligneY+10); vertex(ligneX+20,ligneY+28); endShape(CLOSE);
       noFill(); stroke(Textcolor,150); rect(ligneX+20,ligneY+10,18,18);
       if(mousePressOn(ligneX+20,ligneY+10,18,18))modeCompareData=1;  //COMPARAISON WITH HALF TRIANGLE
       
       noStroke(); fill(colorGeneOff);    beginShape(); vertex(ligneX+58,ligneY+28); vertex(ligneX+58,ligneY+10); vertex(ligneX+40,ligneY+28); endShape(CLOSE);
       fill(cHeat);   beginShape(); vertex(ligneX+40,ligneY+10); vertex(ligneX+58,ligneY+10); vertex(ligneX+40,ligneY+28); endShape(CLOSE);
       noFill(); stroke(Textcolor,150); rect(ligneX+40,ligneY+10,18,18);
       if(mousePressOn(ligneX+40,ligneY+10,18,18))modeCompareData=2;  //COMPARAISON WITH HALF TRIANGLE HEAT
       
       
       fill(colorGeneOn);  rect(ligneX+60,ligneY+10,18,18); fill(Textcolor); rect(ligneX+74,ligneY+10,4,18);
       if(mousePressOn(ligneX+60,ligneY+10,18,18))modeCompareData=3; //COMPARAISON WITH LITTLE BALCK BAR
       
       noStroke();  fill(colorGeneOn);  rect(ligneX+80,ligneY+10,18,8);  fill(colorGeneOff);  rect(ligneX+80,ligneY+20,18,8);  stroke(Textcolor,150);  noFill(); rect(ligneX+80,ligneY+10,18,18); 
       if(mousePressOn(ligneX+80,ligneY+10,18,18))modeCompareData=4; //COMPARAISON WITH 2 BOX WITH NO STROKE
       
      
       }
       
       if(!activeCompareRows.equals("Select") && !activeCompareLigne.equals("Select")){ 
         
          int nbR=0;
          if(activeCompareRows.equals("Genes"))  nbR=nbGene;
          if(activeCompareRows.equals("Domains"))  nbR=nbDomain;
          if(activeCompareRows.equals(timeUnit))  nbR=MyModel.step+1;
          
          int nbL=0;
          if(activeCompareLigne.equals("Genes"))  nbL=nbGene;
          if(activeCompareLigne.equals("Domains"))  nbL=nbDomain;
          if(activeCompareLigne.equals(timeUnit))  nbL=MyModel.step+1;
          
          nbRows=0;
          ModelDomain md=null; int g=-1;  Gene gg=null;
          int s=-1; 
          ModelDomain mdCompare=null; 
         
          if((activeCompareRows.equals("Genes") && activeCompareLigne.equals(timeUnit)) || (activeCompareRows.equals(timeUnit) && activeCompareLigne.equals("Genes"))) {
              md=MyModel.getDomain(activeCompareThird);
              if(compareDataVersion>=0) mdCompare=MyModels[compareDataVersion].getDomain(activeCompareThird);
          }
          if((activeCompareRows.equals("Domains") && activeCompareLigne.equals(timeUnit)) || (activeCompareRows.equals(timeUnit) && activeCompareLigne.equals("Domains"))){
                g=getNumGene(activeCompareThird);  gg=getGene(g);
          }
          
         
          for(int i=0;i<nbR;i++)
             if(hideCompareRows[i]){
                 if(activeCompareRows.equals("Domains"))  {
                   md=MyModel.getDomain(i);
                   if(compareDataVersion>=0) mdCompare=MyModels[compareDataVersion].getDomain(i);
                   if(compareDataVersion==-1)  mdCompare=MyModel.getDomain(compareOthersDomains[i]); 
                 }
                 else if(activeCompareRows.equals("Genes"))  { g=i; gg=getGene(g);}
                 else if(activeCompareRows.equals(timeUnit)) s=i;
                
                 nbLignes=0;
                 for(int j=0;j<nbL;j++)
                       if(hideCompareLigne[j]){
                           if(activeCompareLigne.equals("Domains"))  {
                               md=MyModel.getDomain(j);
                                if(compareDataVersion>=0) mdCompare=MyModels[compareDataVersion].getDomain(j);
                                if(compareDataVersion==-1) mdCompare=MyModel.getDomain(compareOthersDomains[j]); 
                           }
                           else if(activeCompareLigne.equals("Genes"))  { g=j; gg=getGene(g);}
                           else if(activeCompareLigne.equals(timeUnit))    s=j;
                           
                           if(md!=null && g>=0){
                           color c=colorBoxBackground;
                           float XX=ligneX+SizeDrawDomain+20*(nbRows+1); float YY=ligneY+SizeDrawGene+nbLignes*spaceCompareLine+2;
                           //Heat Color Bar
                           boolean sameAsData=true;
                           int nbExpress=0; int nbNotExpress=0;
                           int nbDataExpress=0; int nbDataNotExpress=0; int nbDataDiff=0;
                           boolean same=false;
                           int TtimeCompareBegin=timeCompareBegin; int TtimeCompareEnd=timeCompareEnd;
                           if(activeCompareLigne.equals(timeUnit) || activeCompareRows.equals(timeUnit)){   TtimeCompareBegin=MyModel.Correspons[s]; TtimeCompareEnd=MyModel.Correspons[s];} //COMPARE ONLY AT ONE TIME

                            boolean isManual=false;
                            for(s=0;s<=MyModel.step;s++) 
                               if(MyModel.Correspons[s]>=TtimeCompareBegin && MyModel.Correspons[s]<=TtimeCompareEnd){
                                    same=true;
                                    if(md.Manual[g][s] || (compareDataVersion>=-1 && mdCompare.Manual[g][s])) isManual=true;
                                    if(compareDataVersion>=-1){    if(md.GenesStates[g][s]!=mdCompare.GenesStates[g][s]) same=false;  }  //Compare  with an other Domains or other version
                                    else if( md.dom.GenesData[g][s]!=4 && !md.SameModelAsData(g,s,MyModel.Correspons[s]))same=false;  //Compare with data
                                 
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
                            if(modeCompareBox==0) c=color(red(colorGeneOff)*nbNotExpress/(nbNotExpress+nbExpress) + red(colorGeneOn)*nbExpress/(nbNotExpress+nbExpress), green(colorGeneOff)*nbNotExpress/(nbNotExpress+nbExpress) + green(colorGeneOn)*nbExpress/(nbNotExpress+nbExpress) , blue(colorGeneOff)*nbNotExpress/(nbNotExpress+nbExpress) + blue(colorGeneOn)*nbExpress/(nbNotExpress+nbExpress), alpha(colorGeneOff)*nbNotExpress/(nbNotExpress+nbExpress) + alpha(colorGeneOn)*nbExpress/(nbNotExpress+nbExpress) );  
                            if(modeCompareBox==1) {if(nbExpress>0) c=colorGeneOn; else c=colorGeneOff;}
                            
                            switch(modeCompareData){
                              case 0 :  fill(c);stroke(Textcolor,150); rect(XX,YY,20,20); //NO DATA COMPARAISON
                                    break;
                               case 1 :  //TRIANGLE
                                if(sameAsData) {fill(c);stroke(Textcolor,150);  rect(XX,YY,20,20);} //STROKE ONLY  
                                else{
                                   color datacolor=colorBoxBackground;
                                   if(modeCompareBox==0) datacolor=color(red(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + red(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress), green(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + green(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , blue(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + blue(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , alpha(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + alpha(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) );  
                                   else if(modeCompareBox==1) {if(nbDataExpress>0) datacolor=colorGeneOn; else datacolor=colorGeneOff;}
                                   
                                   if(modeCompareBox==1 && ( ( nbExpress>0 && nbDataExpress>0) ||   ( nbExpress==0 && nbDataExpress==0)))
                                     {fill(c);  stroke(Textcolor,150); rect(XX,YY,20,20);}
                                  else{
                                     noStroke();
                                     fill(c);    beginShape(); vertex(XX,YY); vertex(XX+20,YY); vertex(XX,YY+20); endShape(CLOSE);
                                     fill(datacolor);   beginShape();vertex(XX+20,YY+20); vertex(XX+20,YY); vertex(XX,YY+20);endShape(CLOSE);
                                     noFill();  stroke(Textcolor,150); rect(XX,YY,20,20); //STROKE ONLY
                                  }
                                }
                              break;
                              case 2 :
                                if(sameAsData){fill(c);stroke(Textcolor,150); rect(XX,YY,20,20);} //STROKE ONLY  
                                else{
                                   if(modeCompareBox==1 && ( ( nbExpress>0 && nbDataExpress>0) ||   ( nbExpress==0 && nbDataExpress==0)))
                                      {fill(c);stroke(Textcolor,150); rect(XX,YY,20,20);} 
                                  else{
                                      color datacolor=color(red(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + red(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress), green(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + green(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , blue(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + blue(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , alpha(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + alpha(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) );  
                                   
                                     noStroke();
                                     fill(c);    beginShape(); vertex(XX,YY); vertex(XX+20,YY); vertex(XX,YY+20); endShape(CLOSE);
                                     fill(datacolor);   beginShape();vertex(XX+20,YY+20); vertex(XX+20,YY); vertex(XX,YY+20);endShape(CLOSE);
                                     noFill();  stroke(Textcolor,150); rect(XX,YY,20,20); //STROKE ONLY
                                  }
                                }
                              break;
                              case 3 :   //SMALL BLACK BAR
                                  fill(c); stroke(Textcolor,150); rect(XX,YY,20,20);
                                 if(!sameAsData) {
                                    noStroke(); 
                                    if(modeCompareBox==0) {if(nbNotExpress+nbExpress==1) {fill(Textcolor); rect(XX,YY,20,20); } else{fill(Textcolor,nbDataDiff*255/(nbNotExpress+nbExpress)); rect(XX+15,YY,5,20); }}
                                    if(modeCompareBox==1 && ( (nbExpress>0 && nbDataExpress==0) ||   ( nbExpress==0 && nbDataExpress>0))){fill(Textcolor); rect(XX+15,YY,5,20); }
                                 }
                              break;
                               case 4 :  //DIVIDE IN 2 SUB BOX WITHOUT STROKE
                                   noFill(); stroke(Textcolor,150); rect(XX,YY,20,20); 
                                   fill(c);noStroke(); rect(XX,YY,20,10);
                                   color datacolor=colorBoxBackground;
                                   if(modeCompareBox==0) datacolor=color(red(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + red(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress), green(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + green(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) , blue(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + blue(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress), alpha(colorGeneOff)*nbDataNotExpress/(nbDataNotExpress+nbDataExpress) + alpha(colorGeneOn)*nbDataExpress/(nbDataNotExpress+nbDataExpress) );  
                                   else if(modeCompareBox==1) {if(nbDataExpress>0) datacolor=colorGeneOn; else datacolor=colorGeneOff;}
                                   fill(datacolor); rect(XX,YY+10,20,10); 
                               break;
                            }
                            
                            if(gg.isUnknown || isManual)  {noFill();stroke(colorUnknownGene); rect(XX,YY,20,20); }
                           }
                            nbLignes++;
                       }
                    nbRows++;
             }
             
       }
       
       if(toDo==0){
          //ROWS MENU
          int RowsComp=MenuDefile(ligneX+120,ligneY-45,60,20,colorBackground,activeCompareRows,DimensionsCompare); //Menu Select Rows Dimension
          if(RowsComp>0) {activeCompareRows=DimensionsCompare[RowsComp-1];activeCompareThird="Select";}
          //LIGNE MENU
          int LigneComp=MenuDefile(ligneX+30,ligneY+70,60,20,colorBackground,activeCompareLigne,DimensionsCompare);
          if(LigneComp>0){ activeCompareLigne=DimensionsCompare[LigneComp-1];activeCompareThird="Select";}
       }
       
       //MENU Third Dimension
      
       //Time
       if((activeCompareRows.equals("Genes") && activeCompareLigne.equals("Domains")) || (activeCompareRows.equals("Domains") && activeCompareLigne.equals("Genes"))){ 
         fill(Textcolor); text("From " + timeCompareBegin + " to "+ timeCompareEnd +" "+timeUnit,ligneX,ligneY+50);
         if(mouseOn(ligneX+textWidth("From"),ligneY+36,textWidth(" "+timeCompareBegin+" "),20) && keyPressed){
            if(keyCode==UP)  timeCompareBegin++; if(keyCode==DOWN)  timeCompareBegin--;           
         }
         if(mouseOn(ligneX+textWidth("From " + timeCompareBegin + " to "),ligneY+36,textWidth(" "+timeCompareEnd+" "),20) && keyPressed){
            if(keyCode==UP)  timeCompareEnd++; if(keyCode==DOWN)  timeCompareEnd--;
         }
       }
       //Domains
       if((activeCompareRows.equals("Genes") && activeCompareLigne.equals(timeUnit)) || (activeCompareRows.equals(timeUnit) && activeCompareLigne.equals("Genes"))){ 
         String [] ThirdDomains = new String[nbDomain]; for(int i=0;i<nbDomain;i++) ThirdDomains[i]=MyModel.getDomain(i).getName();
         int ThirdComp=MenuDefile(ligneX,ligneY+40,SizeDrawDomain,20,colorBackground,activeCompareThird,ThirdDomains); //Menu Select Third Domains Dimension
         if(ThirdComp>0) activeCompareThird=ThirdDomains[ThirdComp-1];
       }
       //Genes
       if((activeCompareRows.equals("Domains") && activeCompareLigne.equals(timeUnit)) || (activeCompareRows.equals(timeUnit) && activeCompareLigne.equals("Domains"))){ 
         String [] ThirdGenes = new String[nbGene]; for(int i=0;i<nbGene;i++) ThirdGenes[i]=getGene(i).Name;
         int ThirdComp=MenuDefile(ligneX,ligneY+40,SizeDrawDomain,20,colorBackground,activeCompareThird,ThirdGenes); //Menu Select Third Domains Dimension
         if(ThirdComp>0) activeCompareThird=ThirdGenes[ThirdComp-1];
       }
       //Compare with Data or others Domains or Version ?
     
              String [] modelVersionList=new String[MyModels.length];
              for(int i=0;i<MyModels.length;i++) modelVersionList[i]=MyModels[i].Name;
              int VersionComp=MenuDefile(ligneX+370,ligneY-42,150,20,colorBackground,MyModel.Name,modelVersionList);
              if(VersionComp>0) {
                 MyModel=MyModels[VersionComp-1];
                 MyModel.ActiveDomain=MyModel.getDomain(0);
               }
              
              fill(Textcolor);  text("/",ligneX+500,ligneY-30); 
              modelVersionList=new String[MyModels.length+2];modelVersionList[0]="data";modelVersionList[1]="Domains";
              for(int i=0;i<MyModels.length;i++) modelVersionList[i+2]=MyModels[i].Name;
              VersionComp=MenuDefile(ligneX+520,ligneY-42,60,20,colorBackground,modelVersionList[compareDataVersion+2],modelVersionList);
              if(VersionComp>0){ 
                  compareDataVersion=VersionComp-3;
                  if(compareDataVersion==-1){ //First Initialisation of Compare with Others Domains
                        if(compareOthersDomains==null) {
                           compareOthersDomains=new String[nbDomain]; 
                           OrderCompareOthersDomains=new String[nbDomain]; 
                           for(int d=0;d<nbDomain;d++){  ModelDomain md=MyModel.getDomain(d);compareOthersDomains[d]=md.getName();OrderCompareOthersDomains[d]=md.getName();}
                        }      
                  }
              }
         
    
       ResizeMenuXY(NumMenu,0,SizeDrawDomain+20*(nbRows+1)+30);  //Resize Menu X
       ResizeMenuXY(NumMenu,1,SizeDrawGene+(nbLignes+1)*spaceCompareLine+60);  //Resize Menu Y
   }
  
  
}
  

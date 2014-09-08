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


//First Creation of model
void initializeModel(){
  MyModel=new Model(0,now());
   for(int d=0;d<Domains.size();d++)
       MyModel.addDomain(getDomain(d));
       MyModel.reset(); 
       if(MyModel.modelDomains.size()>0) MyModel.ActiveDomain=MyModel.getDomain(0);
       MyModels=new Model[1];
       MyModels[0]=MyModel;
  
  
}
///////////////////////////////////////////////  MODEL 
void MenuModel(int ligneX,int ligneY,int NumMenu){
  
  MenuOnglet(ligneX,ligneY,NumMenu,"Model");
  
  ligneY+=50;
 
  int idStep=0;
  if(MenuActive[NumMenu]==1 && MyModel!=null){
       if(toDo==0){ //Capture Mode
       
       //Button to compute the model
       if(!MyModel.lock){
             if(mouseUnHide(ligneX+10,ligneY-20,20,4,"Compute the next line"))  {MyModel.ActualStep++;  MyModel.Step();   }
             if(mouseHide(ligneX+40,ligneY-13,20,5,"Delete the last step"))  {if(MyModel.step>0) MyModel.step--;MyModel.ActualStep--; }
             if(ImageClicked(ligneX+10,ligneY+10,20,20,Redo,"Reset the Model"))  {MyModel.reset();}
             if(ImageClicked(ligneX+38, ligneY+5,22,22,Reset,"Recompute the Model until step "+MyModel.step)) {MyModel.step=0; toDo=4;}
             if(ImageClicked(ligneX+90,ligneY+2,28,28,Unlock,"Lock this domain")) MyModel.lock=true;
         }
         else  if(ImageClicked(ligneX+93,ligneY+2,28,28,Lock,"Un-Lock this domain")) MyModel.lock=false;
         
         color dc=colorGeneOff; if(DataInModel)dc=colorGeneOn;
         if(boutonClicked(ligneX+97,ligneY-18,18,18,dc,"","Visualize the Data in the Model",DataInModel)) DataInModel=!DataInModel;
         fill(Textcolor); rect(ligneX+109,ligneY-18,5,18);
         
       }
     ligneY-=20;
   
     int sX=ligneX+SizeDrawDomain+20; int sY=ligneY+SizeDrawGene;    
  //For the Active Domain
  if(MyModel.ActiveDomain!=null){
        MyModel.ActiveDomain.dom.drawName(sX,ligneY-5);  //Draw the name of the domain
        // textFont(myFont, 24);  fill(Textcolor); text(dom.Name,ligneX+500,ligneY-5);     textFont(myFont, 12);
       
        //Draw the next step of the computational model
       for(int i=0;i<nbGene;i++) getGene(i).draw(ligneX+SizeDrawDomain+20*(i+1),ligneY,true,SizeDrawGene,false,false); //Draw the genes name
       
       //Count nb Lines active in the model 
       int nbActiveLigne=0;
       for(int s=0;s<=MyModel.ActualStep;s++) if(MyModel.ActiveDomain.HideSteps[s] || toDo==5) nbActiveLigne++;

       //Draw a Off background (most commun) for until last step
        noStroke();fill(colorGeneOff); rect(sX,sY,nbGene*20,nbActiveLigne*20);
        
       for(int i=0;i<nbGene;i++){
         Gene gene=getGene(i);
        idStep=0;
         for(int s=0;s<=MyModel.ActualStep;s++){
              if(MyModel.ActiveDomain.HideSteps[s] || toDo==5){ //When Export Model 
                float XX=ligneX+SizeDrawDomain+20*(i+1);float YY=ligneY+SizeDrawGene+idStep*20;
                if(MyModel.ActiveDomain.GenesStates[i][s]) {fill(colorGeneOn);noStroke(); rect(XX,YY,20,20);}
                if(MyModel.ActiveDomain.Manual[i][s] || MyModel.ActiveDomain.isBlueState[i][s])  {strokeWeight(2);noFill();stroke(colorUnknownGene); rect(XX+1,YY+1,18,18);strokeWeight(1);}
                
                if(mouseOn(ligneX+SizeDrawDomain+20*(i+1),ligneY+SizeDrawGene+idStep*20,20,20)){ //When the mouse is on a box
                 if(TimeExplanation<0){
                  // if(dom.Manual[i][s])Explanation="Manual"; else Explanation=""+gene.Name+"\n"+gene.getComments();
                   if(StepExplanation!=s){
                        StepExplanation=s;
                        gene.getGenesInvolve(MyModel.ActiveDomain);    //Calcul Genes invovle
                        Explanation=gene.getComments(MyModel.ActiveDomain); //Take the Comments
                        CoordExplanation[0]=ligneX+SizeDrawDomain+20*(i+1)+10; CoordExplanation[1]=ligneY+SizeDrawGene+idStep*20+10; 
                   }
                  }
                  if(!MyModel.lock && mousePressOn(ligneX+SizeDrawDomain+20*(i+1),ligneY+SizeDrawGene+idStep*20,20,20)) { //When click on a box to Manual
                       MyModel.ActiveDomain.GenesStates[i][s]=!MyModel.ActiveDomain.GenesStates[i][s];
                       MyModel.ActiveDomain.Manual[i][s]=!MyModel.ActiveDomain.Manual[i][s]; 
                       //MyModel.step=max(0,s); while(MyModel.step<MyModel.ActualStep) MyModel.Step(); //AUTOMATIC RECOMPUTING
                 }
              }
              idStep++;
           }
         }
       }
       //DRAW LINE
          stroke(colorBoxGene);
         line(sX,ligneY,sX+20*nbGene,ligneY); //First ligne before Genes 
         for(int i=0;i<=nbGene;i++) line(sX+20*i,ligneY,sX+20*i,sY+20*nbActiveLigne);  //Draw ligne |
         for(int i=0;i<=nbActiveLigne;i++) line(sX,sY+20*i,sX+20*nbGene,sY+20*i);  //Draw Colone -

      //IF WE RECOMPUTE DRAW THE LINE FROM THE STEP AND THE ACTUAL STEP
      if(MyModel.step!=MyModel.ActualStep) {fill(colorBackground,25);stroke(colorBackground); rect(sX,sY+20*MyModel.step,20*nbGene,20);}

        
 
       //Colorbos the Genes invovle in the logic Explanation
       if(GenesInvolve!=null && StepExplanation>0 && toDo==0){
          noFill(); 
          strokeWeight(2);
          //addMessage("GenesInvolve="+GenesInvolve.size());
          for(int g=0;g<GenesInvolve.size();g++){
             int []TimeXY=(int [])GenesInvolve.get(g); 
             float XX=ligneX+SizeDrawDomain+20*(TimeXY[0]+1)+1; float YY=ligneY+SizeDrawGene+(TimeXY[1])*20+1;
             if(TimeXY[2]==1) stroke(Textcolor,255); else {if(!resultInvovle) stroke(255,0,0,155); else stroke(0,0,0,0);}
             bezier(XX+10,YY+10,XX+10,CoordExplanation[1],CoordExplanation[0],YY+10,CoordExplanation[0],CoordExplanation[1]);
             rect(XX,YY,18,18);
            } 
         rect(CoordExplanation[0]-9,CoordExplanation[1]-9,18,18);
          strokeWeight(1);
       }  
       
        //DRAW THE HOURS ON THE LEFT
        fill(Textcolor);   textFont(myFont, 14);
        text(timeUnit,ligneX+90,ligneY+SizeDrawGene-7);
        if(toDo==0 && !MyModel.lock && mouseUnHide(ligneX+78,ligneY+82,11,3,"un-hide all steps"))if(MyModel.ActiveDomain!=null) for(int i=0;i<=MyModel.step;i++) MyModel.ActiveDomain.HideSteps[i]=true;
           
        idStep=0;
        for(int s=0;s<=MyModel.ActualStep;s++) {
         if(MyModel.ActiveDomain.HideSteps[s] || toDo==5){
               fill(Textcolor);
               String hpf=""+MyModel.Correspons[s];
               text(hpf,ligneX+100,ligneY+SizeDrawGene+idStep*20+17);
               if(!MyModel.lock){
                if(toDo==0 && mouseHide(ligneX+90,ligneY+SizeDrawGene+idStep*20+10,9,3,"hide this step"))  MyModel.ActiveDomain.HideSteps[s]=!MyModel.ActiveDomain.HideSteps[s];
                if(mousePressOn(ligneX+100-textWidth(hpf),ligneY+SizeDrawGene+idStep*20,40,20)){ MyModel.Correspons[s]++; if(MyModel.Correspons[s]>30) MyModel.Correspons[s]=0; }     
                 if(mouseOn(ligneX+100-textWidth(hpf),ligneY+SizeDrawGene+idStep*20,40,20) && keyPressed){
                   if(keyCode==UP)    MyModel.Correspons[s]++;
                   if(keyCode==DOWN)  MyModel.Correspons[s]--;
                 }
               }
               //Not a real step
             // if(!MyModel.ActiveDomain.Steps[s]){ fill(blueGene,30);noStroke();rect(ligneX+SizeDrawDomain+20,ligneY+SizeDrawGene+idStep*20+2,nbGenes*20,20);   }
               idStep++;
           }
        }
         textFont(myFont, 12);
             
       //Visualise the Tree in the left Part
       stroke(Textcolor); textFont(myFont,18);  int ligneXTree=ligneX+50;
       for(int t=0;t<MyModel.ActiveDomain.dom.Tree.size();t++){
             Region reg=MyModel.ActiveDomain.dom.getTree(t);
             int [] CoordHours=new int[2];CoordHours[0]=-1;CoordHours[1]=-1;
             idStep=0;
             for(int s=0;s<=MyModel.ActualStep;s++)  
               if(MyModel.ActiveDomain.HideSteps[s] || toDo==5){
                   if(MyModel.Correspons[s]<=reg.hours[0]) CoordHours[0]=idStep;
                   if(MyModel.Correspons[s]<=reg.hours[1]) CoordHours[1]=idStep;
                   idStep++;
               }
             if(CoordHours[0]!=CoordHours[1]){
                
                 noFill();
                 beginShape();
                 vertex(ligneXTree+30,ligneY+SizeDrawGene+CoordHours[0]*20+12);
                 vertex(ligneXTree+20,ligneY+SizeDrawGene+CoordHours[0]*20+12);
                 vertex(ligneXTree+20,ligneY+SizeDrawGene+CoordHours[1]*20+12);
                 vertex(ligneXTree+30,ligneY+SizeDrawGene+CoordHours[1]*20+12);
                 endShape();
                 pushMatrix(); 
                   translate(ligneXTree,(ligneY+SizeDrawGene+CoordHours[0]*20+12+ligneY+SizeDrawGene+CoordHours[1]*20+12)/2);
                   rotate(-PI/2); fill(Textcolor); text(reg.Name,-textWidth(reg.Name)/2,14);
                 popMatrix();
             }
            
       }
       textFont(myFont,12);
       

       
       
       int Shift=1;
         //Wisualize the NaNoString in the Model
       if(NanoInModel){
           noStroke(); 
           for(int i=0;i<nbGene;i++){
               Gene gene=getGene(i);
              idStep=0;
              if(gene.Nano!=null)
                for(int s=0;s<=MyModel.step;s++) {
                  if(MyModel.ActiveDomain.HideSteps[s] || toDo==5){
                    if(gene.Nano[MyModel.Correspons[s]]>300){
                        fill(colorNanoString(gene.Nano[MyModel.Correspons[s]]));
                        rect(ligneX+SizeDrawDomain+20*(i+1)+20-Shift*5,ligneY+SizeDrawGene+idStep*20+1,5,18);
                    }
                    idStep++;
                  }
                } 
            }
            Shift++;
       }
        //Wisualize the Data in the Model
        if(DataInModel){
           noStroke(); fill(0,0,0,200);
           for(int i=0;i<nbGene;i++){
              Gene gene=getGene(i);
              idStep=0;
              
              for(int s=0;s<=MyModel.ActualStep;s++) 
                if(MyModel.ActiveDomain.HideSteps[s] || toDo==5){
                if(!MyModel.ActiveDomain.SameModelAsData(i,s,MyModel.Correspons[s])) {
                    //Test if there is a Correspond Gene Data (Low) where it's ok !
                   boolean NotCorrect=true;
                   int  nbG=-1;
                   if(NotCorrect){
                       int nb=255;
                       //If it's on 3 hours before or After , it's just a problem of step 
                       boolean oneFound=false; 
                      /* boolean FromBegin=false;
                       if((nbG>=0 && !dom.GenesStates[nbG][0] && dom.GenesData[nbG][0]>=2) || (!dom.GenesStates[i][0] && dom.GenesData[i][0]>=2)) {
                             //Maternal Problème
                             FromBegin=true;
                              for(int ss=0;ss<=s;ss++)   
                                    if( (nbG>=0 && dom.GenesStates[nbG][ss]) || (dom.GenesStates[i][ss])) 
                                          FromBegin=false;
                       }
                       if(!FromBegin)*/
                       for(int ss=0;ss<=MyModel.ActualStep;ss++) {
                           int hh=MyModel.Correspons[ss];
                           //if((nbG>=0 && dom.GenesStates[nbG][ss]) || dom.GenesStates[i][ss]) FromBegin=false;
                           if(hh>=MyModel.Correspons[s]-3 && hh<=MyModel.Correspons[s]+3) {
                               if(MyModel.ActiveDomain.SameModelAsData(i,s,hh) || (nbG>=0 && MyModel.ActiveDomain.SameModelAsData(nbG,s,hh) )) nb=150; //Compare GenesData[i][hh] && GenesStates[i][s]
                               if( ( !MyModel.ActiveDomain.GenesStates[i][s] && MyModel.ActiveDomain.SameModelAsData(i,hh,s) ) || (nbG>=0 && !MyModel.ActiveDomain.GenesStates[nbG][s] && MyModel.ActiveDomain.SameModelAsData(nbG,hh,s) ) ) oneFound=true;  //Compare GenesData[i][s] && GenesStates[i][hh]
                           }
                                 
                       }
                        
                           
                       if(!oneFound && (!MyModel.ActiveDomain.GenesStates[i][s] || (nbG>=0 && !MyModel.ActiveDomain.GenesStates[nbG][s]) ) ) nb=255;
                       //if(FromBegin){ noFill(); stroke(0,200);}
                        
                       // else { fill(0,0,0,nb); if(nb<255) noStroke(); else stroke(0,0,0,nb);}
                       //  stroke(0);  fillExpressionColor(dom.GenesData[i][MyModel.Correspons[s]]);
                        if((nbG>=0 && !MyModel.ActiveDomain.GenesStates[nbG][s] && MyModel.ActiveDomain.dom.GenesData[nbG][s]==4) || (!MyModel.ActiveDomain.GenesStates[i][s] && MyModel.ActiveDomain.dom.GenesData[i][s]==4)) {
                         stroke(colorMaternal); noFill(); 
                        }
                       else{  if(nb==255) fill(0,0,0); else noFill();
                         stroke(0,0,0);
                       }
                        rect(ligneX+SizeDrawDomain+20*(i+1)+20-Shift*5,ligneY+SizeDrawGene+idStep*20+3,5,18);
                   }
                }  
                idStep++;
                }
            }
          Shift++;
       }
       
        //HighLight
      if(toDo==0){
        HighLithY=-1;HighLithX=-1;
       if(mouseOn(ligneX,ligneY,SizeDrawDomain+20*(nbGene+1),SizeDrawGene+MyModel.step*20)){
         //Highlight the line
         strokeWeight(2); stroke(colorBackground); fill(colorBackground,25);
         idStep=0;
         for(int s=0;s<=MyModel.step;s++)  
           if(MyModel.ActiveDomain.HideSteps[s]){
           if(mouseOn(ligneX,ligneY+SizeDrawGene+idStep*20,nbGene*20+SizeDrawDomain+20,20))  {
                   HighLithY=MyModel.Correspons[s];
                   rect(ligneX+SizeDrawDomain+20,ligneY+SizeDrawGene+idStep*20,nbGene*20,20);      
              }
              idStep++;
           }
         //Highlight the Colone
         for(int i=0;i<nbGene;i++)
           if(mouseOn(ligneX+SizeDrawDomain+20*(i+1),ligneY,20,(idStep)*20+SizeDrawGene)) {
                    HighLithX=i;
                    rect(ligneX+SizeDrawDomain+20*(i+1),ligneY,20,(idStep)*20+SizeDrawGene);
           }
        
        strokeWeight(1);
       }
      }
      
    
    
    //VERSION MENU
         //Duplicate
      //fill(Textcolor); text("Version",ligneX+130,ligneY-10); 
        if(toDo==0)
           if(ImageClicked(sX+280,ligneY-25,20,20,Duplicate,"Create a new version")){
                String Name= ask("Give a name to this new vesion "); 
               if(Name!=null) addModel(new Model(MyModel,MyModels.length,Name,now()));
           }
         //TO DO CANNOT DELETE TE VERSION IF THERE IS ONLY ONE
        if(MyModels.length>1){
          if(toDo==0) if(!MyModel.lock && ImageClicked(sX+305,ligneY-25,20,20,Wrong,"Delete this version"))  delModel(MyModel);
         
          textFont(myFont, 16);
          String [] modelVersionList=new String[MyModels.length];
          for(int i=0;i<MyModels.length;i++) modelVersionList[i]=MyModels[i].Name;
          int VersionComp=MenuDefile(sX+325,ligneY-22,150,20,colorBackground,MyModel.Name,modelVersionList);
          if(VersionComp>0) {
                String ActDom=""; if(MyModel.ActiveDomain!=null) ActDom=MyModel.ActiveDomain.getName();
                MyModel=MyModels[VersionComp-1];
                MyModel.ActiveDomain=MyModel.getDomain(ActDom);
           }
           textFont(myFont, 12);
                 
      }
  }
    
   }
  
  
  
   ResizeMenuXY(NumMenu,0,20*nbGene+30+SizeDrawDomain);  //Resize Menu X
   ResizeMenuXY(NumMenu,1,100+(20*(1+idStep)+30));  //Resize Menu Y
}



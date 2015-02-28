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
  // MenuModel.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public class ModelMenuManager {

  private GRNBoolModel p;
  
  ModelMenuManager(GRNBoolModel p) {
    this.p = p;   
  }  
  
  ///////////////////////////////////////////////  MODEL 
  void MenuModel(int ligneX,int ligneY,int NumMenu){
    
    p.mm.MenuOnglet(ligneX,ligneY,NumMenu,"Model");
    
    ligneY+=50;
   
    int idStep=0;
    if(p.mm.MenuActive[NumMenu]==1 && p.lm.MyModel!=null){
         if(p.toDo==0){ //Capture Mode
         
         //Button to compute the model
         if(!p.lm.MyModel.lock){
               if(p.mm.mouseUnHide(ligneX+10,ligneY-20,20,4,"Compute the next line"))  {p.lm.MyModel.ActualStep++;  p.lm.MyModel.Step();   }
               if(p.mm.mouseHide(ligneX+40,ligneY-13,20,5,"Delete the last step"))  {if(p.lm.MyModel.step>0) p.lm.MyModel.step--;p.lm.MyModel.ActualStep--; }
               if(p.mm.ImageClicked(ligneX+10,ligneY+10,20,20,p.Redo,"Reset the Model"))  {p.lm.MyModel.reset();}
               if(p.mm.ImageClicked(ligneX+38, ligneY+5,22,22,p.Reset,"Recompute the Model until step "+p.lm.MyModel.step)) {p.lm.MyModel.step=0; p.toDo=4;}
               if(p.mm.ImageClicked(ligneX+90,ligneY+2,28,28,p.Unlock,"Lock this domain")) p.lm.MyModel.lock=true;
           }
           else  if(p.mm.ImageClicked(ligneX+93,ligneY+2,28,28,p.Lock,"Un-Lock this domain"))p.lm.MyModel.lock=false;
           
           int dc=p.cm.colorGeneOff; if(p.rm.DataInModel)dc=p.cm.colorGeneOn;
           if(p.mm.boutonClicked(ligneX+97,ligneY-18,18,18,dc,"","Visualize the Data in the Model",p.rm.DataInModel)) p.rm.DataInModel=!p.rm.DataInModel;
           p.fill(p.cm.Textcolor); p.rect(ligneX+109,ligneY-18,5,18);
           
         }
       ligneY-=20;
     
       int sX=ligneX+p.dm.SizeDrawDomain+20; int sY=ligneY+p.gm.SizeDrawGene;    
    //For the Active Domain
    if(p.lm.MyModel.ActiveDomain!=null){
          p.lm.MyModel.ActiveDomain.dom.drawName(sX,ligneY-5);  //Draw the name of the domain
          // textFont(myFont, 24);  fill(Textcolor); text(dom.Name,ligneX+500,ligneY-5);     textFont(myFont, 12);
         
          //Draw the next step of the computational model
         for(int i=0;i<p.gm.nbGene;i++) p.gm.getGene(i).draw(ligneX+p.dm.SizeDrawDomain+20*(i+1),ligneY,true,p.gm.SizeDrawGene,false,false); //Draw the genes name
         
         //Count nb Lines active in the model 
         int nbActiveLigne=0;
         for(int s=0;s<=p.lm.MyModel.ActualStep;s++) if(p.lm.MyModel.ActiveDomain.HideSteps[s] || p.toDo==5) nbActiveLigne++;
  
         //Draw a Off background (most commun) for until last step
          p.noStroke();p.fill(p.cm.colorGeneOff); p.rect(sX,sY,p.gm.nbGene*20,nbActiveLigne*20);
          
         for(int i=0;i<p.gm.nbGene;i++){
           Gene gene=p.gm.getGene(i);
          idStep=0;
           for(int s=0;s<=p.lm.MyModel.ActualStep;s++){
                if(p.lm.MyModel.ActiveDomain.HideSteps[s] || p.toDo==5){ //When Export Model 
                  float XX=ligneX+p.dm.SizeDrawDomain+20*(i+1);float YY=ligneY+p.gm.SizeDrawGene+idStep*20;
                  if(p.lm.MyModel.ActiveDomain.GenesStates[i][s]) {p.fill(p.cm.colorGeneOn);p.noStroke(); p.rect(XX,YY,20,20);}
                  if(p.lm.MyModel.ActiveDomain.Manual[i][s] || p.lm.MyModel.ActiveDomain.isBlueState[i][s])  {p.strokeWeight(2);p.noFill();p.stroke(p.cm.colorUnknownGene); p.rect(XX+1,YY+1,18,18);p.strokeWeight(1);}
                  
                  if(p.eh.mouseOn(ligneX+p.dm.SizeDrawDomain+20*(i+1),ligneY+p.gm.SizeDrawGene+idStep*20,20,20)){ //When the mouse is on a box
                   if(p.mm.TimeExplanation<0){
                    // if(dom.Manual[i][s])Explanation="Manual"; else Explanation=""+gene.Name+"\n"+gene.getComments();
                     if(p.mm.StepExplanation!=s){
                          p.mm.StepExplanation=s;
                          gene.getGenesInvolve(p.lm.MyModel.ActiveDomain);    //Calcul Genes invovle
                          p.mm.Explanation=gene.getComments(p.lm.MyModel.ActiveDomain); //Take the Comments
                          p.mm.CoordExplanation[0]=ligneX+p.dm.SizeDrawDomain+20*(i+1)+10; p.mm.CoordExplanation[1]=ligneY+p.gm.SizeDrawGene+idStep*20+10; 
                     }
                    }
                    if(!p.lm.MyModel.lock && p.eh.mousePressOn(ligneX+p.dm.SizeDrawDomain+20*(i+1),ligneY+p.gm.SizeDrawGene+idStep*20,20,20)) { //When click on a box to Manual
                         p.lm.MyModel.ActiveDomain.GenesStates[i][s]=!p.lm.MyModel.ActiveDomain.GenesStates[i][s];
                         p.lm.MyModel.ActiveDomain.Manual[i][s]=!p.lm.MyModel.ActiveDomain.Manual[i][s]; 
                         //p.lm.MyModel.step=max(0,s); while(p.lm.MyModel.step<p.lm.MyModel.ActualStep) p.lm.MyModel.Step(); //AUTOMATIC RECOMPUTING
                   }
                }
                idStep++;
             }
           }
         }
         //DRAW LINE
            p.stroke(p.cm.colorBoxGene);
           p.line(sX,ligneY,sX+20*p.gm.nbGene,ligneY); //First ligne before Genes 
           for(int i=0;i<=p.gm.nbGene;i++) p.line(sX+20*i,ligneY,sX+20*i,sY+20*nbActiveLigne);  //Draw ligne |
           for(int i=0;i<=nbActiveLigne;i++) p.line(sX,sY+20*i,sX+20*p.gm.nbGene,sY+20*i);  //Draw Colone -
  
        //IF WE RECOMPUTE DRAW THE LINE FROM THE STEP AND THE ACTUAL STEP
        if(p.lm.MyModel.step!=p.lm.MyModel.ActualStep) {p.fill(p.cm.colorBackground,25);p.stroke(p.cm.colorBackground); p.rect(sX,sY+20*p.lm.MyModel.step,20*p.gm.nbGene,20);}
  
          
   
         //Colorbos the Genes invovle in the logic Explanation
         if(p.mm.GenesInvolve!=null && p.mm.StepExplanation>0 && p.toDo==0){
            p.noFill(); 
            p.strokeWeight(2);
            //addMessage("GenesInvolve="+GenesInvolve.size());
            for(int g=0;g<p.mm.GenesInvolve.size();g++){
               int []TimeXY=(int [])p.mm.GenesInvolve.get(g); 
               float XX=ligneX+p.dm.SizeDrawDomain+20*(TimeXY[0]+1)+1; float YY=ligneY+p.gm.SizeDrawGene+(TimeXY[1])*20+1;
               if(TimeXY[2]==1) p.stroke(p.cm.Textcolor,255); else {if(!p.gm.resultInvovle) p.stroke(255,0,0,155); else p.stroke(0,0,0,0);}
               p.bezier(XX+10,YY+10,XX+10,p.mm.CoordExplanation[1],p.mm.CoordExplanation[0],YY+10,p.mm.CoordExplanation[0],p.mm.CoordExplanation[1]);
               p.rect(XX,YY,18,18);
              } 
           p.rect(p.mm.CoordExplanation[0]-9,p.mm.CoordExplanation[1]-9,18,18);
            p.strokeWeight(1);
         }  
         
          //DRAW THE HOURS ON THE LEFT
          p.fill(p.cm.Textcolor);   p.textFont(p.cm.myFont, 14);
          p.text(p.rm.timeUnit,ligneX+90,ligneY+p.gm.SizeDrawGene-7);
          if(p.toDo==0 && !p.lm.MyModel.lock && p.mm.mouseUnHide(ligneX+78,ligneY+82,11,3,"un-hide all steps"))if(p.lm.MyModel.ActiveDomain!=null) for(int i=0;i<=p.lm.MyModel.step;i++) p.lm.MyModel.ActiveDomain.HideSteps[i]=true;
             
          idStep=0;
          for(int s=0;s<=p.lm.MyModel.ActualStep;s++) {
           if(p.lm.MyModel.ActiveDomain.HideSteps[s] || p.toDo==5){
                 p.fill(p.cm.Textcolor);
                 String hpf=""+p.lm.MyModel.Correspons[s];
                 p.text(hpf,ligneX+100,ligneY+p.gm.SizeDrawGene+idStep*20+17);
                 if(!p.lm.MyModel.lock){
                  if(p.toDo==0 && p.mm.mouseHide(ligneX+90,ligneY+p.gm.SizeDrawGene+idStep*20+10,9,3,"hide this step"))  p.lm.MyModel.ActiveDomain.HideSteps[s]=!p.lm.MyModel.ActiveDomain.HideSteps[s];
                  if(p.eh.mousePressOn(ligneX+100-p.textWidth(hpf),ligneY+p.gm.SizeDrawGene+idStep*20,40,20)){ p.lm.MyModel.Correspons[s]++; if(p.lm.MyModel.Correspons[s]>30) p.lm.MyModel.Correspons[s]=0; }     
                   if(p.eh.mouseOn(ligneX+100-p.textWidth(hpf),ligneY+p.gm.SizeDrawGene+idStep*20,40,20) && p.keyPressed){
                     if(p.keyCode==p.UP)    p.lm.MyModel.Correspons[s]++;
                     if(p.keyCode==p.DOWN)  p.lm.MyModel.Correspons[s]--;
                   }
                 }
                 //Not a real step
               // if(!p.lm.MyModel.ActiveDomain.Steps[s]){ fill(blueGene,30);noStroke();rect(ligneX+p.SizeDrawDomain+20,ligneY+p.gm.SizeDrawGene+idStep*20+2,p.gm.nbGenes*20,20);   }
                 idStep++;
             }
          }
           p.textFont(p.cm.myFont, 12);
               
         //Visualise the Tree in the left Part
         p.stroke(p.cm.Textcolor); p.textFont(p.cm.myFont,18);  int ligneXTree=ligneX+50;
         for(int t=0;t<p.lm.MyModel.ActiveDomain.dom.Tree.size();t++){
               Region reg=p.lm.MyModel.ActiveDomain.dom.getTree(t);
               int [] CoordHours=new int[2];CoordHours[0]=-1;CoordHours[1]=-1;
               idStep=0;
               for(int s=0;s<=p.lm.MyModel.ActualStep;s++)  
                 if(p.lm.MyModel.ActiveDomain.HideSteps[s] || p.toDo==5){
                     if(p.lm.MyModel.Correspons[s]<=reg.hours[0]) CoordHours[0]=idStep;
                     if(p.lm.MyModel.Correspons[s]<=reg.hours[1]) CoordHours[1]=idStep;
                     idStep++;
                 }
               if(CoordHours[0]!=CoordHours[1]){
                  
                   p.noFill();
                   p.beginShape();
                   p.vertex(ligneXTree+30,ligneY+p.gm.SizeDrawGene+CoordHours[0]*20+12);
                   p.vertex(ligneXTree+20,ligneY+p.gm.SizeDrawGene+CoordHours[0]*20+12);
                   p.vertex(ligneXTree+20,ligneY+p.gm.SizeDrawGene+CoordHours[1]*20+12);
                   p.vertex(ligneXTree+30,ligneY+p.gm.SizeDrawGene+CoordHours[1]*20+12);
                   p.endShape();
                   p.pushMatrix(); 
                     p.translate(ligneXTree,(ligneY+p.gm.SizeDrawGene+CoordHours[0]*20+12+ligneY+p.gm.SizeDrawGene+CoordHours[1]*20+12)/2);
                     p.rotate(-p.PI/2); p.fill(p.cm.Textcolor); p.text(reg.Name,-p.textWidth(reg.Name)/2,14);
                   p.popMatrix();
               }
              
         }
         p.textFont(p.cm.myFont,12);
         
  
         
         
         int Shift=1;
           //Wisualize the NaNoString in the Model
         if(p.nmm.NanoInModel){
             p.noStroke(); 
             for(int i=0;i<p.gm.nbGene;i++){
                 Gene gene=p.gm.getGene(i);
                idStep=0;
                if(gene.Nano!=null)
                  for(int s=0;s<=p.lm.MyModel.step;s++) {
                    if(p.lm.MyModel.ActiveDomain.HideSteps[s] || p.toDo==5){
                      if(gene.Nano[p.lm.MyModel.Correspons[s]]>300){
                          p.fill(p.nmm.colorNanoString(gene.Nano[p.lm.MyModel.Correspons[s]]));
                          p.rect(ligneX+p.dm.SizeDrawDomain+20*(i+1)+20-Shift*5,ligneY+p.gm.SizeDrawGene+idStep*20+1,5,18);
                      }
                      idStep++;
                    }
                  } 
              }
              Shift++;
         }
          //Wisualize the Data in the Model
          if(p.rm.DataInModel){
             p.noStroke(); p.fill(0,0,0,200);
             for(int i=0;i<p.gm.nbGene;i++){
                Gene gene=p.gm.getGene(i);
                idStep=0;
                
                for(int s=0;s<=p.lm.MyModel.ActualStep;s++) 
                  if(p.lm.MyModel.ActiveDomain.HideSteps[s] || p.toDo==5){
                  if(!p.lm.MyModel.ActiveDomain.SameModelAsData(i,s,p.lm.MyModel.Correspons[s])) {
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
                         for(int ss=0;ss<=p.lm.MyModel.ActualStep;ss++) {
                             int hh=p.lm.MyModel.Correspons[ss];
                             //if((nbG>=0 && dom.GenesStates[nbG][ss]) || dom.GenesStates[i][ss]) FromBegin=false;
                             if(hh>=p.lm.MyModel.Correspons[s]-3 && hh<=p.lm.MyModel.Correspons[s]+3) {
                                 if(p.lm.MyModel.ActiveDomain.SameModelAsData(i,s,hh) || (nbG>=0 && p.lm.MyModel.ActiveDomain.SameModelAsData(nbG,s,hh) )) nb=150; //Compare GenesData[i][hh] && GenesStates[i][s]
                                 if( ( !p.lm.MyModel.ActiveDomain.GenesStates[i][s] && p.lm.MyModel.ActiveDomain.SameModelAsData(i,hh,s) ) || (nbG>=0 && !p.lm.MyModel.ActiveDomain.GenesStates[nbG][s] && p.lm.MyModel.ActiveDomain.SameModelAsData(nbG,hh,s) ) ) oneFound=true;  //Compare GenesData[i][s] && GenesStates[i][hh]
                             }
                                   
                         }
                          
                             
                         if(!oneFound && (!p.lm.MyModel.ActiveDomain.GenesStates[i][s] || (nbG>=0 && !p.lm.MyModel.ActiveDomain.GenesStates[nbG][s]) ) ) nb=255;
                         //if(FromBegin){ noFill(); stroke(0,200);}
                          
                         // else { fill(0,0,0,nb); if(nb<255) noStroke(); else stroke(0,0,0,nb);}
                         //  stroke(0);  fillExpressionColor(dom.GenesData[i][p.lm.MyModel.Correspons[s]]);
                          if((nbG>=0 && !p.lm.MyModel.ActiveDomain.GenesStates[nbG][s] && p.lm.MyModel.ActiveDomain.dom.GenesData[nbG][s]==4) || (!p.lm.MyModel.ActiveDomain.GenesStates[i][s] && p.lm.MyModel.ActiveDomain.dom.GenesData[i][s]==4)) {
                           p.stroke(p.cm.colorMaternal); p.noFill(); 
                          }
                         else{  if(nb==255) p.fill(0,0,0); else p.noFill();
                           p.stroke(0,0,0);
                         }
                          p.rect(ligneX+p.dm.SizeDrawDomain+20*(i+1)+20-Shift*5,ligneY+p.gm.SizeDrawGene+idStep*20+3,5,18);
                     }
                  }  
                  idStep++;
                  }
              }
            Shift++;
         }
         
          //HighLight
        if(p.toDo==0){
          p.mm.HighLithY=-1;p.mm.HighLithX=-1;
         if(p.eh.mouseOn(ligneX,ligneY,p.dm.SizeDrawDomain+20*(p.gm.nbGene+1),p.gm.SizeDrawGene+p.lm.MyModel.step*20)){
           //Highlight the line
           p.strokeWeight(2); p.stroke(p.cm.colorBackground); p.fill(p.cm.colorBackground,25);
           idStep=0;
           for(int s=0;s<=p.lm.MyModel.step;s++)  
             if(p.lm.MyModel.ActiveDomain.HideSteps[s]){
             if(p.eh.mouseOn(ligneX,ligneY+p.gm.SizeDrawGene+idStep*20,p.gm.nbGene*20+p.dm.SizeDrawDomain+20,20))  {
                     p.mm.HighLithY=p.lm.MyModel.Correspons[s];
                     p.rect(ligneX+p.dm.SizeDrawDomain+20,ligneY+p.gm.SizeDrawGene+idStep*20,p.gm.nbGene*20,20);      
                }
                idStep++;
             }
           //Highlight the Colone
           for(int i=0;i<p.gm.nbGene;i++)
             if(p.eh.mouseOn(ligneX+p.dm.SizeDrawDomain+20*(i+1),ligneY,20,(idStep)*20+p.gm.SizeDrawGene)) {
                      p.mm.HighLithX=i;
                      p.rect(ligneX+p.dm.SizeDrawDomain+20*(i+1),ligneY,20,(idStep)*20+p.gm.SizeDrawGene);
             }
          
          p.strokeWeight(1);
         }
        }
        
      
      
      //VERSION MENU
           //Duplicate
        //fill(Textcolor); text("Version",ligneX+130,ligneY-10); 
          if(p.toDo==0)
             if(p.mm.ImageClicked(sX+280,ligneY-25,20,20,p.Duplicate,"Create a new version")){
                  String Name= p.eh.ask("Give a name to this new vesion "); 
                 if(Name!=null) p.lm.addModel(new Model(p.lm.MyModel,p.lm.MyModels.length,Name,p.uf.now()));
             }
           //TO DO CANNOT DELETE TE VERSION IF THERE IS ONLY ONE
          if(p.lm.MyModels.length>1){
            if(p.toDo==0) if(!p.lm.MyModel.lock && p.mm.ImageClicked(sX+305,ligneY-25,20,20,p.Wrong,"Delete this version"))  p.lm.delModel(p.lm.MyModel);
           
            p.textFont(p.cm.myFont, 16);
            String [] modelVersionList=new String[p.lm.MyModels.length];
            for(int i=0;i<p.lm.MyModels.length;i++) modelVersionList[i]=p.lm.MyModels[i].Name;
            int VersionComp=p.mm.MenuDefile(sX+325,ligneY-22,150,20,p.cm.colorBackground,p.lm.MyModel.Name,modelVersionList);
            if(VersionComp>0) {
                  String ActDom=""; if(p.lm.MyModel.ActiveDomain!=null) ActDom=p.lm.MyModel.ActiveDomain.getName();
                  p.lm.MyModel=p.lm.MyModels[VersionComp-1];
                  p.lm.MyModel.ActiveDomain=p.lm.MyModel.getDomain(ActDom);
             }
             p.textFont(p.cm.myFont, 12);
                   
        }
    }
      
     }
    
    
    
     p.mm.ResizeMenuXY(NumMenu,0,20*p.gm.nbGene+30+p.dm.SizeDrawDomain);  //Resize Menu X
     p.mm.ResizeMenuXY(NumMenu,1,100+(20*(1+idStep)+30));  //Resize Menu Y
  }


  
  
}

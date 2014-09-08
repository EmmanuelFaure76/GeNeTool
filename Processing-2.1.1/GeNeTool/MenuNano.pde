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



boolean NanoInModel=false;
int NaNoThreSold=20; //% of valid NanoThreSold


public void ReadNanoStringFile(File selection){
  if (selection != null) {
    String Name=selection.getAbsolutePath();
    try{ ReadNanoString(Name);} catch (Exception e){addMessage("This is not a NanoString file");}
  } 
}
public void ReadNanoString(String file){
  addMessage(" Try to Read " + file);
 String [] fileline=loadStrings(file); 
  
  //First line is hpf
  //First colone is Gene Name
  
  
  //Read the different gpf
  String [] HpfS=split(fileline[0],';');
  int [] hpf=new int[HpfS.length-1];
  for(int r=1;r<HpfS.length;r++) hpf[r-1]=parseInt(HpfS[r]);
  
  for(int l=1;l<fileline.length;l++){
    String [] nono=split(fileline[l],';');
    Gene g=getGene(CorrespondName(nono[0]));
    if(g!=null){
      g.Nano=new int[hpf.length];
      for(int r=1;r<nono.length;r++) {
          g.Nano[r-1]=parseInt(nono[r]);
          g.MaxNaNo=max(g.Nano[hpf[r-1]],g.MaxNaNo);
          g.MinNaNo=min(g.Nano[hpf[r-1]],g.MinNaNo);
      } 
      
      
    }//else addMessage(nono[0] + " not Found"); 
    
    
  }
  
  active("nanostring",1);
}

//When read the data Table change some name ...
public String CorrespondName(String name) {
  if (equal(name, "b-catenin/TCF")) return "N_b-cat";
  if (equal(name, "Dach1")) return "Dac";
  if (equal(name, "Ets1/2")) return "Ets1";
  if (equal(name, "Runt1")) return "Runx";
  if (equal(name, "Otxa")) return "Otx-alpha";
  if (equal(name, "Otxb1/2")) return "Otx-beta";
  if (equal(name, "b1/2otx")) return "Otx-beta";
  if (equal(name, "aotx")) return "Otx-alpha";
  if (equal(name, "z13")) return "z13/Krl";
  if (equal(name, "N")) return "Notch";
  return name;
}




/////////////////////////////// NANOSTRING
void MenuNanoString(int ligneX,int ligneY,int NumMenu){
  
  MenuOnglet(ligneX,ligneY,NumMenu,"NanoString");
  
  if(MenuActive[NumMenu]==1 && MyModel!=null && MyModel.ActiveDomain!=null){
       Domain dom=MyModel.ActiveDomain.dom;
       int sx=ligneX+SizeDrawDomain+20; 
       //if(boutonClicked(ligneX+100,ligneY-49,70,18,ButtonColor,"-> Model","Visualize the NanoString in the Model",NanoInModel))  NanoInModel=!NanoInModel;
       stroke(colorBoxBackground,150);
       fill(ExpressionColor(1)); rect(sx+320,ligneY+5,15,15);
       fill(ExpressionColor(2)); rect(sx+420,ligneY+5,15,15);   
       fill(ExpressionColor(3)); rect(sx+520,ligneY+5,15,15); 
       fill(Textcolor);  text("<100",sx+340,ligneY+17); text("<300",sx+440,ligneY+17); text(">=300",sx+540,ligneY+17);
     
     
     dom.drawName(sx,ligneY+20); 
     ligneY+=25;

      //Draw the step text
       for(int i=0;i<nbGene;i++){
         Gene gene=getGene(i);
         gene.draw(sx+20*i,ligneY,true,SizeDrawGene,false,false);    //Draw the Gene
         if(mousePressOn(sx+20*i,ligneY,20,SizeDrawGene)) geneNano=gene;
          
         //Draw the next step of the computational model
         for(int h=0;h<=MaxTime;h++) if(gene.Nano!=null){ fill(colorNanoString(gene.Nano[h]));  rect(sx+20*i,ligneY+SizeDrawGene+h*20,20,20); }  
      }
      
       //DRAW LINE
       stroke(colorBoxGene);
       line(sx,ligneY,sx+20*nbGene,ligneY); //First ligne before Genes 
       for(int i=0;i<=nbGene;i++) line(sx+20*i,ligneY,sx+20*i,ligneY+SizeDrawGene+20*MaxTime);  //Draw ligne |
       for(int i=0;i<=MaxTime;i++) line(sx,ligneY+SizeDrawGene+20*i,sx+20*nbGene,ligneY+SizeDrawGene+20*i);  //Draw Colone -
       
        drawMenuHours(ligneX+10+SizeDrawGene,ligneY+SizeDrawGene); //Draw the hours on the left
      
      //Plot the detail of the curve
      if(geneNano!=null && geneNano.Nano!=null){
         pushMatrix(); 
         float MouseShitX=ligneX+SizeDrawDomain+70;float MouseShitY=ligneY+SizeDrawGene+20;
         translate(MouseShitX,MouseShitY);
         int XX=geneNano.Nano.length*10;
          //White Rectangle
          fill(255,255,255); rect(-50,-20,XX+70,500+40);
          //Gene
          fill(0,0,0); textFont(myFont, 24);  text(geneNano.Name,300,50); textFont(myFont, 12); 
          //XAxes
          stroke(0,0,0); line(0,500,XX,500); line(geneNano.Nano.length*10,500,XX-5,500-5);line(XX,500,XX-5,500+5);
          fill(0,0,0); text("Time ("+timeUnit+")",XX/2-15,515);
          for(int i=10;i<geneNano.Nano.length;i+=10){  line(i*10,500,i*10,492);  text(""+i,i*10-8,512); }
          //YAxes 
          line(0,500,0,0); line(0,0,-5,5); line(0,0,5,5); 
          pushMatrix();  translate(-5,240); rotate(-PI/2); text("Transcripts",0,0); popMatrix();
          int ic=round((float)geneNano.MaxNaNo/5); int oic=ic; int nbic=0; while(ic>0){oic=ic; ic=round((float)ic/10);nbic++;}ic=oic; for(int k=1;k<nbic;k++) ic*=10;
          for(int i=1;i<=5;i++) { int yc=500-round((ic*i)*500.0/geneNano.MaxNaNo); if(yc>50){line(0,yc,8,yc);  text(""+(i*ic),-textWidth(""+(i*ic))-5,yc+5 ); } }
          //Plot Curve
          for(int i=1;i<geneNano.Nano.length;i++){
                //stroke(colorNanoString(geneNano.Nano[i]));
                stroke(Textcolor);
                float NanoX=i*10; float NanoY=500-geneNano.Nano[i]*500.0/geneNano.MaxNaNo;
                line((i-1)*10,500-geneNano.Nano[i-1]*500.0/geneNano.MaxNaNo,NanoX,NanoY);
                strokeWeight(3); point(NanoX,NanoY); strokeWeight(1);
                
                if(mouseOn(MouseShitX+NanoX-3,MouseShitY+NanoY-3,6,6)){ //Display some details of the curve
                  stroke(0,0,0); line(NanoX,500,NanoX,NanoY); line(0,NanoY,NanoX,NanoY);
                  text("("+i+","+geneNano.Nano[i]+")",NanoX,NanoY);
                }
          }
          
         
          //Draw the lines
          if(CoordinateLineNano!=null) {
            color col=colorLine(geneNano.NanoLines.size());
            fill(col); stroke(col);
            rect(CoordinateLineNano[0]-2,CoordinateLineNano[1]-2,4,4);
            line(CoordinateLineNano[0],CoordinateLineNano[1],mouseX()-MouseShitX,mouseY()-MouseShitY);
          }
          
          int LineDel=-1;
          for(int i=0;i<geneNano.NanoLines.size();i++){
            color col=colorLine(i);
            float [] Lines=(float []) geneNano.NanoLines.get(i);
            fill(col); stroke(col);
            rect(Lines[0]-2,Lines[1]-2,4,4);rect(Lines[2]-2,Lines[3]-2,4,4);
            line(Lines[0],Lines[1],Lines[2],Lines[3]);
            //Plot la droite 
            float a=1;  if(Lines[0]!=Lines[2]) a=(Lines[1]-Lines[3])/(Lines[0]-Lines[2]);
            float b=Lines[1]-a*Lines[0];
            float x1=0;float y1=coordDroiteY(x1,a,b);
            float y2=0;float x2=coordDroiteX(y2,a,b);
            if(a>=0){y2=500;x2=coordDroiteX(y2,a,b);}
            if(y1>500){y1=500; x1=(y1-b)/a;}
            if(y1<0)  {y1=0; x1=coordDroiteX(y1,a,b);}
            if(x2>500) {x2=500; y2=coordDroiteY(x2,a,b);}
            if(Lines[0]==Lines[2]){x1=Lines[0];x2=Lines[0];y1=500;y2=0;}
            if(Lines[1]==Lines[3]){x1=0;x2=500;y1=Lines[1];y2=Lines[1];}
             line(x1,y1,x2,y2); 
             rect(x1-2,y1-2,4,4);rect(x2-2,y2-2,4,4);
             //Splot Curve
             String NameCoef=""+roundAt(((Lines[3]-Lines[1])*geneNano.MaxNaNo/500)/(Lines[0]/10-Lines[2]/10),2)+"/h";   
             fill(250,250,250,180); noStroke(); rect((x1+x2)/2-2-textWidth(NameCoef)/2,(y1+y2)/2-14,textWidth(NameCoef)+4,20); 
             fill(col); stroke(col); text(NameCoef,(x1+x2)/2-textWidth(NameCoef)/2,(y1+y2)/2);
                   
             if(mouseOn(MouseShitX-50,MouseShitY-20,XX+70,540)) //To plot the detail of each point
              for(int j=0;j<4;j++){
               float xx=0;float yy=0;
               switch(j){
                 case 0:xx=x1;yy=y1;break;
                 case 1:xx=x2;yy=y2;break;
                 case 2:xx=Lines[0];yy=Lines[1];break;
                 case 3:xx=Lines[2];yy=Lines[3];break;
               }
              if(mouseOn(MouseShitX+xx-3,MouseShitY+yy-3,6,6)) { 
                   int x=round(xx/10); int y=round((-yy+500)*geneNano.MaxNaNo/500);
                   String NameCoord="("+x+","+y+")"; 
                   fill(250,250,250,180); noStroke(); rect(xx-2,yy-14,textWidth(NameCoord)+4,20); 
                   fill(col); stroke(col); text(NameCoord,xx,yy);
                   if(mousePressed && AcceptEvent){AcceptEvent=false; geneNano.NanoLines.remove(i); } //LineDel=i;  //Delete this line
               }
             }
          } 
             
             //Click to determine a Point
         if(mousePressOn(MouseShitX-50,MouseShitY-20,XX+70,540)){
            if(CoordinateLineNano==null) { // First Point
                CoordinateLineNano=new float[2]; CoordinateLineNano[0]=mouseX()-MouseShitX;CoordinateLineNano[1]=mouseY()-MouseShitY;
            }else{ //Second Point
              float []Lines=new float[4];Lines[0]=CoordinateLineNano[0];Lines[1]=CoordinateLineNano[1];Lines[2]=mouseX()-MouseShitX;Lines[3]=mouseY()-MouseShitY;
              geneNano.NanoLines.add(Lines); CoordinateLineNano=null;
            }
          }
          
           //Close Button
          tint(colorButton); image(Wrong,0,-10);
          if(mousePressOn(MouseShitX-80,MouseShitY-10,20,20)){geneNano=null; CoordinateLineNano=null;}
         
          popMatrix(); 
      }
        
     }
     ResizeMenuXY(NumMenu,0,20*nbGene+30+SizeDrawDomain);  //Resize Menu X
     ResizeMenuXY(NumMenu,1,50+SizeDrawDomain+20*(MaxTime+1)+10);  //Resize Menu Y
}




public color colorNanoString(int v){
  if(v<100) return ExpressionColor(1);
  if(v<300) return ExpressionColor(2);
  return ExpressionColor(3); 
}





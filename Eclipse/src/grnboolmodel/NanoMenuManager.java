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
  // MenuNano.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

public class NanoMenuManager {

  private GRNBoolModel p;
  
  NanoMenuManager(GRNBoolModel p) {
    this.p = p;   
  }  

  boolean NanoInModel=false;
  int NaNoThreSold=20; //% of valid NanoThreSold
  
  public void ReadNanoString(String file){
    p.mm.addMessage(" Try to Read " + file);
   String [] fileline=p.loadStrings(file); 
    
    //First line is hpf
    //First colone is Gene Name
    
    
    //Read the different gpf
    String [] HpfS=p.split(fileline[0],';');
    int [] hpf=new int[HpfS.length-1];
    for(int r=1;r<HpfS.length;r++) hpf[r-1]=p.parseInt(HpfS[r]);
    
    for(int l=1;l<fileline.length;l++){
      String [] nono=p.split(fileline[l],';');
      Gene g=p.gm.getGene(CorrespondName(nono[0]));
      if(g!=null){
        g.Nano=new int[hpf.length];
        for(int r=1;r<nono.length;r++) {
            g.Nano[r-1]=p.parseInt(nono[r]);
            g.MaxNaNo=p.max(g.Nano[hpf[r-1]],g.MaxNaNo);
            g.MinNaNo=p.min(g.Nano[hpf[r-1]],g.MinNaNo);
        } 
        
        
      }//else addMessage(nono[0] + " not Found"); 
      
      
    }
    
    p.mm.active("nanostring",1);
  }
  
  //When read the data Table change some name ...
  public String CorrespondName(String name) {
    if (p.uf.equal(name, "b-catenin/TCF")) return "N_b-cat";
    if (p.uf.equal(name, "Dach1")) return "Dac";
    if (p.uf.equal(name, "Ets1/2")) return "Ets1";
    if (p.uf.equal(name, "Runt1")) return "Runx";
    if (p.uf.equal(name, "Otxa")) return "Otx-alpha";
    if (p.uf.equal(name, "Otxb1/2")) return "Otx-beta";
    if (p.uf.equal(name, "b1/2otx")) return "Otx-beta";
    if (p.uf.equal(name, "aotx")) return "Otx-alpha";
    if (p.uf.equal(name, "z13")) return "z13/Krl";
    if (p.uf.equal(name, "N")) return "Notch";
    return name;
  }
  
  
  
  
  /////////////////////////////// NANOSTRING
  void MenuNanoString(int ligneX,int ligneY,int NumMenu){
    
   p.mm.MenuOnglet(ligneX,ligneY,NumMenu,"NanoString");
    
    if(p.mm.MenuActive[NumMenu]==1 && p.lm.MyModel!=null && p.lm.MyModel.ActiveDomain!=null){
         Domain dom=p.lm.MyModel.ActiveDomain.dom;
         int sx=ligneX+p.dm.SizeDrawDomain+20; 
         //if(boutonClicked(ligneX+100,ligneY-49,70,18,ButtonColor,"-> Model","Visualize the NanoString in the Model",NanoInModel))  NanoInModel=!NanoInModel;
         p.stroke(p.cm.colorBoxBackground,150);
         p.fill(p.cm.ExpressionColor(1)); p.rect(sx+320,ligneY+5,15,15);
         p.fill(p.cm.ExpressionColor(2)); p.rect(sx+420,ligneY+5,15,15);   
         p.fill(p.cm.ExpressionColor(3)); p.rect(sx+520,ligneY+5,15,15); 
         p.fill(p.cm.Textcolor);  p.text("<100",sx+340,ligneY+17); p.text("<300",sx+440,ligneY+17); p.text(">=300",sx+540,ligneY+17);
       
       
       dom.drawName(sx,ligneY+20); 
       ligneY+=25;
  
        //Draw the step text
         for(int i=0;i<p.gm.nbGene;i++){
           Gene gene=p.gm.getGene(i);
           gene.draw(sx+20*i,ligneY,true,p.gm.SizeDrawGene,false,false);    //Draw the Gene
           if(p.eh.mousePressOn(sx+20*i,ligneY,20,p.gm.SizeDrawGene)) p.mm.geneNano=gene;
            
           //Draw the next step of the computational model
           for(int h=0;h<=p.rm.MaxTime;h++) if(gene.Nano!=null){ p.fill(colorNanoString(gene.Nano[h]));  p.rect(sx+20*i,ligneY+p.gm.SizeDrawGene+h*20,20,20); }  
        }
        
         //DRAW LINE
         p.stroke(p.cm.colorBoxGene);
         p.line(sx,ligneY,sx+20*p.gm.nbGene,ligneY); //First ligne before Genes 
         for(int i=0;i<=p.gm.nbGene;i++) p.line(sx+20*i,ligneY,sx+20*i,ligneY+p.gm.SizeDrawGene+20*p.rm.MaxTime);  //Draw ligne |
         for(int i=0;i<=p.rm.MaxTime;i++) p.line(sx,ligneY+p.gm.SizeDrawGene+20*i,sx+20*p.gm.nbGene,ligneY+p.gm.SizeDrawGene+20*i);  //Draw Colone -
         
          p.tmm.drawMenuHours(ligneX+10+p.gm.SizeDrawGene,ligneY+p.gm.SizeDrawGene); //Draw the hours on the left
        
        //Plot the detail of the curve
        if(p.mm.geneNano!=null && p.mm.geneNano.Nano!=null){
           p.pushMatrix(); 
           float MouseShitX=ligneX+p.dm.SizeDrawDomain+70;float MouseShitY=ligneY+p.gm.SizeDrawGene+20;
           p.translate(MouseShitX,MouseShitY);
           int XX=p.mm.geneNano.Nano.length*10;
            //White Rectangle
            p.fill(255,255,255); p.rect(-50,-20,XX+70,500+40);
            //Gene
            p.fill(0,0,0); p.textFont(p.cm.myFont, 24);  p.text(p.mm.geneNano.Name,300,50); p.textFont(p.cm.myFont, 12); 
            //XAxes
            p.stroke(0,0,0); p.line(0,500,XX,500); p.line(p.mm.geneNano.Nano.length*10,500,XX-5,500-5);p.line(XX,500,XX-5,500+5);
            p.fill(0,0,0); p.text("Time ("+p.rm.timeUnit+")",XX/2-15,515);
            for(int i=10;i<p.mm.geneNano.Nano.length;i+=10){  p.line(i*10,500,i*10,492);  p.text(""+i,i*10-8,512); }
            //YAxes 
            p.line(0,500,0,0); p.line(0,0,-5,5); p.line(0,0,5,5); 
            p.pushMatrix();  p.translate(-5,240); p.rotate(-p.PI/2); p.text("Transcripts",0,0); p.popMatrix();
            int ic=p.round((float)p.mm.geneNano.MaxNaNo/5); int oic=ic; int nbic=0; while(ic>0){oic=ic; ic=p.round((float)ic/10);nbic++;}ic=oic; for(int k=1;k<nbic;k++) ic*=10;
            for(int i=1;i<=5;i++) { int yc=500-p.round((float)((ic*i)*500.0/p.mm.geneNano.MaxNaNo)); if(yc>50){p.line(0,yc,8,yc);  p.text(""+(i*ic),-p.textWidth(""+(i*ic))-5,yc+5 ); } }
            //Plot Curve
            for(int i=1;i<p.mm.geneNano.Nano.length;i++){
                  //stroke(colorNanoString(geneNano.Nano[i]));
                  p.stroke(p.cm.Textcolor);
                  float NanoX=i*10; float NanoY=(float)(500-p.mm.geneNano.Nano[i]*500.0/p.mm.geneNano.MaxNaNo);
                  p.line( (i-1)*10, (float)(500-p.mm.geneNano.Nano[i-1]*500.0/p.mm.geneNano.MaxNaNo), NanoX, NanoY);
                  p.strokeWeight(3); p.point(NanoX,NanoY); p.strokeWeight(1);
                  
                  if(p.eh.mouseOn(MouseShitX+NanoX-3,MouseShitY+NanoY-3,6,6)){ //Display some details of the curve
                    p.stroke(0,0,0); p.line(NanoX,500,NanoX,NanoY); p.line(0,NanoY,NanoX,NanoY);
                    p.text("("+i+","+p.mm.geneNano.Nano[i]+")",NanoX,NanoY);
                  }
            }
            
           
            //Draw the lines
            if(p.mm.CoordinateLineNano!=null) {
              int col=p.cm.colorLine(p.mm.geneNano.NanoLines.size());
              p.fill(col); p.stroke(col);
              p.rect(p.mm.CoordinateLineNano[0]-2,p.mm.CoordinateLineNano[1]-2,4,4);
              p.line(p.mm.CoordinateLineNano[0],p.mm.CoordinateLineNano[1],p.eh.mouseX()-MouseShitX,p.eh.mouseY()-MouseShitY);
            }
            
            int LineDel=-1;
            for(int i=0;i<p.mm.geneNano.NanoLines.size();i++){
              int col=p.cm.colorLine(i);
              float [] Lines=(float []) p.mm.geneNano.NanoLines.get(i);
              p.fill(col); p.stroke(col);
              p.rect(Lines[0]-2,Lines[1]-2,4,4);p.rect(Lines[2]-2,Lines[3]-2,4,4);
              p.line(Lines[0],Lines[1],Lines[2],Lines[3]);
              //Plot la droite 
              float a=1;  if(Lines[0]!=Lines[2]) a=(Lines[1]-Lines[3])/(Lines[0]-Lines[2]);
              float b=Lines[1]-a*Lines[0];
              float x1=0;float y1=p.uf.coordDroiteY(x1,a,b);
              float y2=0;float x2=p.uf.coordDroiteX(y2,a,b);
              if(a>=0){y2=500;x2=p.uf.coordDroiteX(y2,a,b);}
              if(y1>500){y1=500; x1=(y1-b)/a;}
              if(y1<0)  {y1=0; x1=p.uf.coordDroiteX(y1,a,b);}
              if(x2>500) {x2=500; y2=p.uf.coordDroiteY(x2,a,b);}
              if(Lines[0]==Lines[2]){x1=Lines[0];x2=Lines[0];y1=500;y2=0;}
              if(Lines[1]==Lines[3]){x1=0;x2=500;y1=Lines[1];y2=Lines[1];}
               p.line(x1,y1,x2,y2); 
               p.rect(x1-2,y1-2,4,4);p.rect(x2-2,y2-2,4,4);
               //Splot Curve
               String NameCoef=""+p.uf.roundAt(((Lines[3]-Lines[1])*p.mm.geneNano.MaxNaNo/500)/(Lines[0]/10-Lines[2]/10),2)+"/h";   
               p.fill(250,250,250,180); p.noStroke(); p.rect((x1+x2)/2-2-p.textWidth(NameCoef)/2,(y1+y2)/2-14,p.textWidth(NameCoef)+4,20); 
               p.fill(col); p.stroke(col); p.text(NameCoef,(x1+x2)/2-p.textWidth(NameCoef)/2,(y1+y2)/2);
                     
               if(p.eh.mouseOn(MouseShitX-50,MouseShitY-20,XX+70,540)) //To plot the detail of each point
                for(int j=0;j<4;j++){
                 float xx=0;float yy=0;
                 switch(j){
                   case 0:xx=x1;yy=y1;break;
                   case 1:xx=x2;yy=y2;break;
                   case 2:xx=Lines[0];yy=Lines[1];break;
                   case 3:xx=Lines[2];yy=Lines[3];break;
                 }
                if(p.eh.mouseOn(MouseShitX+xx-3,MouseShitY+yy-3,6,6)) { 
                     int x=p.round(xx/10); int y=p.round((-yy+500)*p.mm.geneNano.MaxNaNo/500);
                     String NameCoord="("+x+","+y+")"; 
                     p.fill(250,250,250,180); p.noStroke(); p.rect(xx-2,yy-14,p.textWidth(NameCoord)+4,20); 
                     p.fill(col); p.stroke(col); p.text(NameCoord,xx,yy);
                     if(p.mousePressed && p.eh.AcceptEvent){p.eh.AcceptEvent=false; p.mm.geneNano.NanoLines.remove(i); } //LineDel=i;  //Delete this line
                 }
               }
            } 
               
               //Click to determine a Point
           if(p.eh.mousePressOn(MouseShitX-50,MouseShitY-20,XX+70,540)){
              if(p.mm.CoordinateLineNano==null) { // First Point
                  p.mm.CoordinateLineNano=new float[2]; p.mm.CoordinateLineNano[0]=p.eh.mouseX()-MouseShitX;p.mm.CoordinateLineNano[1]=p.eh.mouseY()-MouseShitY;
              }else{ //Second Point
                float []Lines=new float[4];Lines[0]=p.mm.CoordinateLineNano[0];Lines[1]=p.mm.CoordinateLineNano[1];Lines[2]=p.eh.mouseX()-MouseShitX;Lines[3]=p.eh.mouseY()-MouseShitY;
                p.mm.geneNano.NanoLines.add(Lines); p.mm.CoordinateLineNano=null;
              }
            }
            
             //Close Button
            p.tint(p.cm.colorButton); p.image(p.Wrong,0,-10);
            if(p.eh.mousePressOn(MouseShitX-80,MouseShitY-10,20,20)){p.mm.geneNano=null; p.mm.CoordinateLineNano=null;}
           
            p.popMatrix(); 
        }
          
       }
       p.mm.ResizeMenuXY(NumMenu,0,20*p.gm.nbGene+30+p.dm.SizeDrawDomain);  //Resize Menu X
       p.mm.ResizeMenuXY(NumMenu,1,50+p.dm.SizeDrawDomain+20*(p.rm.MaxTime+1)+10);  //Resize Menu Y
  }

  public int colorNanoString(int v){
    if(v<100) return p.cm.ExpressionColor(1);
    if(v<300) return p.cm.ExpressionColor(2);
    return p.cm.ExpressionColor(3); 
  }
 
}

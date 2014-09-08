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
  // MenuData.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public class DataMenuManager {
  
  private GRNBoolModel p;
  
  DataMenuManager(GRNBoolModel p) {
    this.p = p;   
  } 

  ////////////////////////////////////////////// TREE
  public void MenuTree(int ligneX,int ligneY,int NumMenu){
    
    p.mm.MenuOnglet(ligneX,ligneY,NumMenu,"Progenitors");
    
    ligneX+=30; ligneY+=40;
    if(p.mm.MenuActive[NumMenu]==1){ 
          //ADD A NEW REGION
          if(p.rm.MaxTime>0 &&  p.mm.mouseUnHide(ligneX+50,ligneY-38,15,5,"Add a new definition")) {
               String Name= p.eh.ask("Give a name to this new Region"); 
               if(Name!=null)   p.rm.addRegion(Name,p.rm.Regions.size(),0,p.rm.MaxTime);
         }
         
         //MAX TIME
         float tx=p.textWidth("Max "+p.rm.MaxTime);
         p.fill(p.cm.Textcolor); 
         if(p.eh.mouseOn(ligneX+135-tx-2,ligneY-40,tx+5,20)){
             p.fill(p.cm.colorButton);
             if(p.eh.mousePressOn(ligneX+135-tx-2,ligneY-40,tx+5,20) && p.doubleClick()){String NewMaxTime=p.eh.ask("Change time ? ",""+p.rm.MaxTime); if(NewMaxTime!=null) p.rm.changeMaxTime(p.parseInt(NewMaxTime)-p.rm.MaxTime);}
             else if(p.keyPressed){ if(p.keyCode==p.UP)   p.rm.changeMaxTime(1);  if(p.keyCode==p.DOWN) p.rm.changeMaxTime(-1);  }
          }
          p.text("Max "+p.rm.MaxTime,ligneX+135-tx,ligneY-26);
          
          //TIME UNIT
          p.fill(p.cm.Textcolor); 
          if(p.eh.mouseOn(ligneX+135,ligneY-40,100,20)){
              p.fill(p.cm.colorButton);
              if(p.eh.mousePressOn(ligneX+135,ligneY-40,100,20)){
                String Name= p.eh.ask("Give a time unit ",p.rm.timeUnit); 
                if(Name!=null) p.rm.timeUnit=Name;
              }
            }
           p.text(p.rm.timeUnit,ligneX+140,ligneY-26);
        
        if(p.rm.MaxTime>0) //LIST OF ALL REGIONS
        for(int r=0;r<p.rm.Regions.size();r++){
          Region reg=p.rm.getRegion(r);
          //NAME OF REGION
          p.fill(p.cm.Textcolor);  
          if(p.eh.mouseOn(ligneX-5,ligneY+20*r-14,100,20)){
              p.fill(p.cm.colorButton);
              if(p.eh.mousePressOn(ligneX-5,ligneY+20*r-14,100,20)){ //PRESS N
                  if(p.doubleClick()) { 
                    String Name=p.eh.ask("Give a name to this Region ",reg.Name); 
                    if(Name!=null) reg.Name=Name;
                  }else{ //DRAG REGION TO SOMEWHERE ELSE
                    p.ObjetDrag=new Objet(p, reg); 
                    p.mm.MenuDragActive=NumMenu;
                    p.mm.DragMod=0;
                  }
             }
          }
          p.text(reg.Name,ligneX,ligneY+20*r);
          
          //FROM HOURS
           p.fill(p.cm.Textcolor);  p.text("from",ligneX+100,ligneY+20*r);
           if(p.eh.mouseOn(ligneX+130,ligneY+20*r-14,30,20)){
               p.fill(p.cm.colorButton);
                if(p.keyPressed){ if(p.keyCode==p.UP && reg.hours[0]<p.rm.MaxTime)    reg.hours[0]++;  if(p.keyCode==p.DOWN && reg.hours[0]>0)  reg.hours[0]--;}
            }
            p.text(""+reg.hours[0],ligneX+130,ligneY+20*r);
            //TO HOURS
            p.fill(p.cm.Textcolor);  p.text("to",ligneX+160,ligneY+20*r);  
            if(p.eh.mouseOn(ligneX+180,ligneY+20*r-14,30,20)){
                p.fill(p.cm.colorButton);
               if(p.keyPressed){ if(p.keyCode==p.UP && reg.hours[1]<p.rm.MaxTime)    reg.hours[1]++;  if(p.keyCode==p.DOWN && reg.hours[1]>0)  reg.hours[1]--; }
            }
            p.text(""+reg.hours[1],ligneX+180,ligneY+20*r);
        }
         
         
        p.mm.ResizeMenuXY(NumMenu,0,250);  //Resize Menu X
        p.mm.ResizeMenuXY(NumMenu,1,20*p.rm.Regions.size()+50);  //Resize Menu Y
    }
    
  }
    
  ///////////////////////////////////////////////  Expression Profile   
  void MenuExpression(int ligneX,int ligneY,int NumMenu){
    
    p.mm.MenuOnglet(ligneX,ligneY,NumMenu,"Expression");
    
    ligneX+=10; ligneY+=20;
    
    if(p.mm.MenuActive[NumMenu]==1){ 
         drawLegend(ligneX+150,ligneY-20);
         
         p.fill(p.cm.Textcolor,150);  p.text(p.rm.timeUnit,ligneX+102,ligneY-6);
          for(int h=0;h<=p.rm.MaxTime;h++){ //DRAW THE HOURS
            p.fill(p.cm.colorBoxBackground,50); p.stroke(p.cm.colorBoxBackground,150); p.rect(ligneX+100+h*20,ligneY,20,20); 
            p.fill(p.cm.Textcolor,150);  p.text(""+h,ligneX+110+h*20-p.textWidth(""+h)/2,ligneY+14);
          }
          for(int r=0;r<p.rm.Regions.size();r++){ //DRAW THE REGION
             Region reg=p.rm.getRegion(r);
             p.stroke(p.cm.colorBoxBackground,150); p.fill(p.cm.colorBoxBackground,p.mm.InactiveColor); p.rect(ligneX,ligneY+(1+r)*20,100,20); 
             p.fill(p.cm.Textcolor,150);  p.text(reg.Name,ligneX+50-p.textWidth(reg.Name)/2,ligneY+(1+r)*20+14);
          }
         
      
      if(p.eh.GeneDef!=null){
         p.fill(p.cm.Textcolor,250);  p.text(p.eh.GeneDef.Name,ligneX+50+-p.textWidth(p.eh.GeneDef.Name)/2,ligneY+14);
         p.stroke(p.color(p.cm.colorBoxBackground,150));
         for(int r=0;r<p.rm.Regions.size();r++){ //On Each Region
              Region reg=p.rm.getRegion(r);
              if(p.eh.GeneDef.Expression!=null){
                for(int h=reg.hours[0];h<=reg.hours[1];h++){
                      p.fill(p.cm.ExpressionColor(p.eh.GeneDef.Expression[r][h]));p.rect(ligneX+100+h*20,ligneY+(1+r)*20,20,20);
                      if(p.eh.mousePressOn(ligneX+100+h*20,ligneY+(1+r)*20,20,20)){
                          if(p.eh.GeneDef.Expression[r][h]==-1)p.eh.GeneDef.Expression[r][h]=0; 
                          p.eh.GeneDef.Expression[r][h]++; 
                          if(p.eh.GeneDef.Expression[r][h]>4)p.eh.GeneDef.Expression[r][h]=0;
                          p.dm.reComputeData(p.eh.GeneDef); //Recompute all the data
                      }
                }
             }
        }
      }
      
       p.mm.ResizeMenuXY(NumMenu,0,150+p.rm.MaxTime*20);  //Resize Menu X
       p.mm.ResizeMenuXY(NumMenu,1,20*p.rm.Regions.size()+50);  //Resize Menu Y
    }
  }
  
  
  ///////////////////// LEGEND
  
  public void drawLegend(int ligneX,int ligneY){
    p.stroke(p.cm.Textcolor,50);
    int idx=0;
    for(int i=0;i<=4;i++){
      p.fill(p.cm.ExpressionColor(i)); p.rect(ligneX+idx,ligneY+2,15,15);
      String TxtLegen="";
      switch(i){
        case 0: TxtLegen="no data"; break;
        case 1: TxtLegen="no expression"; break;
        case 2: TxtLegen="weak expression"; break;
        case 3: TxtLegen="expression"; break;
        case 4: TxtLegen="maternal"; break;
      }
      p.fill(p.cm.Textcolor);  p.text(TxtLegen,ligneX+idx+20,ligneY+14);
      idx+=40+p.textWidth(TxtLegen);
    }
  }
  
  
  ///////////////////////////////////////////////  DATA to the Model    
  public void MenuData(int ligneX,int ligneY,int NumMenu){
    
    p.mm.MenuOnglet(ligneX,ligneY,NumMenu,"Data");
    
    if(p.mm.MenuActive[NumMenu]==1 && p.lm.MyModel!=null){
      if(p.lm.MyModel.ActiveDomain!=null){
        int sx=ligneX+p.dm.SizeDrawDomain+20; int sy=ligneY;
        p.lm.MyModel.ActiveDomain.dom.drawName(sx,sy+20);  //Draw the name of the domain
        drawLegend(sx+300,sy+5); //Draw the Legend
        sy+=25;
        //Draw the next step of the computational model
        p.noStroke();
        for(int i=0;i<p.gm.nbGene;i++){
           p.gm.getGene(i).draw(sx+20*i,sy,true,p.gm.SizeDrawGene,false,false);     //Draw the Gene
           for(int h=0;h<=p.rm.MaxTime;h++){   
               p.fill(p.cm.ExpressionColor(p.lm.MyModel.ActiveDomain.dom.GenesData[i][h]));   p.rect(sx+20*i,sy+p.gm.SizeDrawGene+h*20,20,20);  
               //fill(Textcolor); text(""+MyModel.ActiveDomain.dom.GenesData[i][h],sx+20*i,sy+SizeDrawGene+h*20+12);
            } //Draw the rectangle value
        }
        
        //DRAW LINE
        p.stroke(p.cm.colorBoxGene);
        p.line(sx,sy,sx+20*p.gm.nbGene,sy); //First ligne before Genes 
        for(int i=0;i<=p.gm.nbGene;i++) p.line(sx+20*i,sy,sx+20*i,sy+p.gm.SizeDrawGene+20*(1+p.rm.MaxTime));  //Draw ligne |
        for(int i=0;i<=p.rm.MaxTime+1;i++) p.line(sx,sy+p.gm.SizeDrawGene+20*i,sx+20*p.gm.nbGene,sy+p.gm.SizeDrawGene+20*i);  //Draw Colone -
         
        drawMenuHours(ligneX+10+p.gm.SizeDrawGene,sy+p.gm.SizeDrawGene);
        
       /*  //Highlight the Colone
       if(HighLithX>=0){  stroke(Textcolor,255); fill(Textcolor,25);   rect(ligneX+SizeDrawDomain+20*(HighLithX+1),ligneY,20,SizeDrawGene+idx*20+4);  }
        
        
         //Highlight the line
       if(HighLithY>=0)  {
              int HighLithYHPF=0;  idx=0; for(int h=0;h<=MaxTime;h+=3)   if(h!=3) if(h<=HighLithY){ HighLithYHPF=h; idx++;}  idx--;if(idx<0) idx=0;
              stroke(Textcolor,255); fill(Textcolor,25);rect(ligneX+SizeDrawDomain+20,ligneY+SizeDrawGene+idx*20+2,nbGenes*20,20); 
        }*/
      
       
      }
       p.mm.ResizeMenuXY(NumMenu,0,20*p.gm.nbGene+30+p.dm.SizeDrawDomain);  //Resize Menu X
       p.mm.ResizeMenuXY(NumMenu,1,50+p.dm.SizeDrawDomain+21*p.rm.MaxTime);  //Resize Menu Y
    } 
    
    
  }
  
       //Draw the  Hours
   public void drawMenuHours(int ligneX,int ligneY){
       p.textFont(p.cm.myFont, 14); p.fill(p.cm.Textcolor); p.stroke(p.cm.Textcolor);
       p.text(p.rm.timeUnit,ligneX-20,ligneY-5);
       
       for(int h=0;h<=p.rm.MaxTime;h++) p.text(""+h,ligneX-10,ligneY+h*20+16);
       
      //Visualise the Tree in the left Part
      p.textFont(p.cm.myFont,18); 
      int shif=20;
      for(int t=0;t<p.lm.MyModel.ActiveDomain.dom.Tree.size();t++){
               Region reg=p.lm.MyModel.ActiveDomain.dom.getTree(t);
               p.noFill();
               p.beginShape();
               p.vertex(ligneX-shif,ligneY+reg.hours[0]*20+12);
               p.vertex(ligneX-shif-10,ligneY+reg.hours[0]*20+12);
               p.vertex(ligneX-shif-10,ligneY+reg.hours[1]*20+12);
               p.vertex(ligneX-shif,ligneY+reg.hours[1]*20+12);
               p.endShape();
               p.pushMatrix(); 
               p.translate(ligneX-shif-30,(ligneY+reg.hours[0]*20+12+ligneY+reg.hours[1]*20+12)/2);
               p.rotate(-p.PI/2); p.fill(p.cm.Textcolor); p.text(reg.Name,-p.textWidth(reg.Name)/2,14);
               p.popMatrix();
           }
         p.textFont(p.cm.myFont,12);
   }

}

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




////////////////////////////////////////////// TREE
public void MenuTree(int ligneX,int ligneY,int NumMenu){
  
  MenuOnglet(ligneX,ligneY,NumMenu,"Progenitors");
  
  ligneX+=30; ligneY+=40;
  if(MenuActive[NumMenu]==1){ 
        //ADD A NEW REGION
        if(MaxTime>0 &&  mouseUnHide(ligneX+50,ligneY-38,15,5,"Add a new definition")) {
             String Name= ask("Give a name to this new Region"); 
             if(Name!=null)   addRegion(Name,Regions.size(),0,MaxTime);
       }
       
       //MAX TIME
       float tx=textWidth("Max "+MaxTime);
       fill(Textcolor); 
       if(mouseOn(ligneX+135-tx-2,ligneY-40,tx+5,20)){
           fill(colorButton);
           if(mousePressOn(ligneX+135-tx-2,ligneY-40,tx+5,20) && doubleClick()){String NewMaxTime=ask("Change time ? ",""+MaxTime); if(NewMaxTime!=null) changeMaxTime(parseInt(NewMaxTime)-MaxTime);}
           else if(keyPressed){ if(keyCode==UP)   changeMaxTime(1);  if(keyCode==DOWN) changeMaxTime(-1);  }
        }
        text("Max "+MaxTime,ligneX+135-tx,ligneY-26);
        
        //TIME UNIT
        fill(Textcolor); 
        if(mouseOn(ligneX+135,ligneY-40,100,20)){
            fill(colorButton);
            if(mousePressOn(ligneX+135,ligneY-40,100,20)){
              String Name= ask("Give a time unit ",timeUnit); 
              if(Name!=null) timeUnit=Name;
            }
          }
         text(timeUnit,ligneX+140,ligneY-26);
      
      if(MaxTime>0) //LIST OF ALL REGIONS
      for(int r=0;r<Regions.size();r++){
        Region reg=getRegion(r);
        //NAME OF REGION
        fill(Textcolor);  
        if(mouseOn(ligneX-5,ligneY+20*r-14,100,20)){
            fill(colorButton);
            if(mousePressOn(ligneX-5,ligneY+20*r-14,100,20)){ //PRESS N
                if(doubleClick()) { 
                  String Name=ask("Give a name to this Region ",reg.Name); 
                  if(Name!=null) reg.Name=Name;
                }else{ //DRAG REGION TO SOMEWHERE ELSE
                  ObjetDrag=new Objet(reg); 
                  MenuDragActive=NumMenu;
                  DragMod=0;
                }
           }
        }
        text(reg.Name,ligneX,ligneY+20*r);
        
        //FROM HOURS
         fill(Textcolor);  text("from",ligneX+100,ligneY+20*r);
         if(mouseOn(ligneX+130,ligneY+20*r-14,30,20)){
             fill(colorButton);
              if(keyPressed){ if(keyCode==UP && reg.hours[0]<MaxTime)    reg.hours[0]++;  if(keyCode==DOWN && reg.hours[0]>0)  reg.hours[0]--;}
          }
          text(""+reg.hours[0],ligneX+130,ligneY+20*r);
          //TO HOURS
          fill(Textcolor);  text("to",ligneX+160,ligneY+20*r);  
          if(mouseOn(ligneX+180,ligneY+20*r-14,30,20)){
              fill(colorButton);
             if(keyPressed){ if(keyCode==UP && reg.hours[1]<MaxTime)    reg.hours[1]++;  if(keyCode==DOWN && reg.hours[1]>0)  reg.hours[1]--; }
          }
          text(""+reg.hours[1],ligneX+180,ligneY+20*r);
      }
       
       
      ResizeMenuXY(NumMenu,0,250);  //Resize Menu X
      ResizeMenuXY(NumMenu,1,20*Regions.size()+50);  //Resize Menu Y
  }
  
}
  
  
  



///////////////////////////////////////////////  Expression Profile   
void MenuExpression(int ligneX,int ligneY,int NumMenu){
  
  MenuOnglet(ligneX,ligneY,NumMenu,"Expression");
  
  ligneX+=10; ligneY+=20;
  
  if(MenuActive[NumMenu]==1){ 
       drawLegend(ligneX+150,ligneY-20);
       
       fill(Textcolor,150);  text(timeUnit,ligneX+102,ligneY-6);
        for(int h=0;h<=MaxTime;h++){ //DRAW THE HOURS
          fill(colorBoxBackground,50); stroke(colorBoxBackground,150); rect(ligneX+100+h*20,ligneY,20,20); 
          fill(Textcolor,150);  text(""+h,ligneX+110+h*20-textWidth(""+h)/2,ligneY+14);
        }
        for(int r=0;r<Regions.size();r++){ //DRAW THE REGION
           Region reg=getRegion(r);
           stroke(colorBoxBackground,150); fill(colorBoxBackground,InactiveColor); rect(ligneX,ligneY+(1+r)*20,100,20); 
           fill(Textcolor,150);  text(reg.Name,ligneX+50-textWidth(reg.Name)/2,ligneY+(1+r)*20+14);
        }
       
    
    if(GeneDef!=null){
       fill(Textcolor,250);  text(GeneDef.Name,ligneX+50+-textWidth(GeneDef.Name)/2,ligneY+14);
       stroke(color(colorBoxBackground,150));
       for(int r=0;r<Regions.size();r++){ //On Each Region
            Region reg=getRegion(r);
            if(GeneDef.Expression!=null){
              for(int h=reg.hours[0];h<=reg.hours[1];h++){
                    fill(ExpressionColor(GeneDef.Expression[r][h]));rect(ligneX+100+h*20,ligneY+(1+r)*20,20,20);
                    if(mousePressOn(ligneX+100+h*20,ligneY+(1+r)*20,20,20)){
                        if(GeneDef.Expression[r][h]==-1)GeneDef.Expression[r][h]=0; 
                        GeneDef.Expression[r][h]++; 
                        if(GeneDef.Expression[r][h]>4)GeneDef.Expression[r][h]=0;
                        reComputeData(GeneDef); //Recompute all the data
                    }
              }
           }
      }
    }
    
     ResizeMenuXY(NumMenu,0,150+MaxTime*20);  //Resize Menu X
     ResizeMenuXY(NumMenu,1,20*Regions.size()+50);  //Resize Menu Y
  }
}


///////////////////// LEGEND

public void drawLegend(int ligneX,int ligneY){
  stroke(Textcolor,50);
  int idx=0;
  for(int i=0;i<=4;i++){
    fill(ExpressionColor(i)); rect(ligneX+idx,ligneY+2,15,15);
    String TxtLegen="";
    switch(i){
      case 0: TxtLegen="no data"; break;
      case 1: TxtLegen="no expression"; break;
      case 2: TxtLegen="weak expression"; break;
      case 3: TxtLegen="expression"; break;
      case 4: TxtLegen="maternal"; break;
    }
    fill(Textcolor);  text(TxtLegen,ligneX+idx+20,ligneY+14);
    idx+=40+textWidth(TxtLegen);
  }
}


///////////////////////////////////////////////  DATA to the Model    
public void MenuData(int ligneX,int ligneY,int NumMenu){
  
  MenuOnglet(ligneX,ligneY,NumMenu,"Data");
  
  if(MenuActive[NumMenu]==1 && MyModel!=null){
    if(MyModel.ActiveDomain!=null){
      int sx=ligneX+SizeDrawDomain+20; int sy=ligneY;
      MyModel.ActiveDomain.dom.drawName(sx,sy+20);  //Draw the name of the domain
      drawLegend(sx+300,sy+5); //Draw the Legend
      sy+=25;
      //Draw the next step of the computational model
      noStroke();
      for(int i=0;i<nbGene;i++){
         getGene(i).draw(sx+20*i,sy,true,SizeDrawGene,false,false);     //Draw the Gene
         for(int h=0;h<=MaxTime;h++){   
             fill(ExpressionColor(MyModel.ActiveDomain.dom.GenesData[i][h]));   rect(sx+20*i,sy+SizeDrawGene+h*20,20,20);  
             //fill(Textcolor); text(""+MyModel.ActiveDomain.dom.GenesData[i][h],sx+20*i,sy+SizeDrawGene+h*20+12);
          } //Draw the rectangle value
      }
      
      //DRAW LINE
      stroke(colorBoxGene);
      line(sx,sy,sx+20*nbGene,sy); //First ligne before Genes 
      for(int i=0;i<=nbGene;i++) line(sx+20*i,sy,sx+20*i,sy+SizeDrawGene+20*(1+MaxTime));  //Draw ligne |
      for(int i=0;i<=MaxTime+1;i++) line(sx,sy+SizeDrawGene+20*i,sx+20*nbGene,sy+SizeDrawGene+20*i);  //Draw Colone -
       
      drawMenuHours(ligneX+10+SizeDrawGene,sy+SizeDrawGene);
      
     /*  //Highlight the Colone
     if(HighLithX>=0){  stroke(Textcolor,255); fill(Textcolor,25);   rect(ligneX+SizeDrawDomain+20*(HighLithX+1),ligneY,20,SizeDrawGene+idx*20+4);  }
      
      
       //Highlight the line
     if(HighLithY>=0)  {
            int HighLithYHPF=0;  idx=0; for(int h=0;h<=MaxTime;h+=3)   if(h!=3) if(h<=HighLithY){ HighLithYHPF=h; idx++;}  idx--;if(idx<0) idx=0;
            stroke(Textcolor,255); fill(Textcolor,25);rect(ligneX+SizeDrawDomain+20,ligneY+SizeDrawGene+idx*20+2,nbGenes*20,20); 
      }*/
    
     
    }
     ResizeMenuXY(NumMenu,0,20*nbGene+30+SizeDrawDomain);  //Resize Menu X
     ResizeMenuXY(NumMenu,1,50+SizeDrawDomain+21*MaxTime);  //Resize Menu Y
  } 
  
  
}

     //Draw the  Hours
 public void drawMenuHours(int ligneX,int ligneY){
     textFont(myFont, 14); fill(Textcolor); stroke(Textcolor);
     text(timeUnit,ligneX-20,ligneY-5);
     
     for(int h=0;h<=MaxTime;h++) text(""+h,ligneX-10,ligneY+h*20+16);
     
    //Visualise the Tree in the left Part
    textFont(myFont,18); 
    int shif=20;
    for(int t=0;t<MyModel.ActiveDomain.dom.Tree.size();t++){
             Region reg=MyModel.ActiveDomain.dom.getTree(t);
             noFill();
             beginShape();
             vertex(ligneX-shif,ligneY+reg.hours[0]*20+12);
             vertex(ligneX-shif-10,ligneY+reg.hours[0]*20+12);
             vertex(ligneX-shif-10,ligneY+reg.hours[1]*20+12);
             vertex(ligneX-shif,ligneY+reg.hours[1]*20+12);
             endShape();
             pushMatrix(); 
             translate(ligneX-shif-30,(ligneY+reg.hours[0]*20+12+ligneY+reg.hours[1]*20+12)/2);
             rotate(-PI/2); fill(Textcolor); text(reg.Name,-textWidth(reg.Name)/2,14);
             popMatrix();
         }
       textFont(myFont,12);
 }





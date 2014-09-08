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


public class Region{
  String Name;
  int number;
  int []hours;
  GRNBoolModel p;
  
  Region(GRNBoolModel p, String Name,int number,int start,int end){
    this.p = p;
    this.Name=Name;
    this.number=number;
    hours=new int[2];
    hours[0]=start;hours[1]=end;
  }

  public int size(){  return p.round(p.textWidth(this.Name))+4; }
  public String toString(){return this.Name;}
  public String toRule(){return this.Name;}
  public void draw(int x,int y){this.draw(x,y,-1);}
  public void draw(int x,int y,float s){
    int c=p.cm.colorBoxGene;
    if(s==-1) s=p.textWidth(Name)+4;
    if(p.eh.mouseOn(x,y,s,20)) p.fill(c,p.mm.ActiveColor);   else p.fill(c,p.mm.InactiveColor);
    p.stroke(c,255);  p.rect(x,y,s,20);
    p.fill(p.cm.Textcolor);   p.text(Name,x+s/2-p.textWidth(Name)/2,y+14);
  }  
}


  


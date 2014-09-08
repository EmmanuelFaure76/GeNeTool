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



//Draw a light color Menu on the rest
boolean searchMenu=false;
int sizeHeightSearch=200; 
String textSearchInput="";//List of text Input
ArrayList textSearch=new ArrayList();//result of the search Function
int blinkTest=0;

public void changeMenuSearch(){
  searchMenu=!searchMenu;
  if(searchMenu) ReadCurrentSearch();
  mWSe.setState(searchMenu);
  if(searchMenu && colorMenu) changeMenuColor(); //Remove color Menu if it was here
   offMenu=searchMenu;
}

public void MenuSearch(){
  fill(colorBackground,175); noStroke(); rect(0,0,width,height); //Draw a white cover on everything
  
  int sizeWidthSearch=800;
  int ligneX=round(width-sizeWidthSearch)/2; int ligneY=100;
  
  rect(ligneX-5,ligneY+20,sizeWidthSearch,sizeHeightSearch-20,colorBackground,colorOngletBackground); 
  if(mousePressed && AcceptEvent && !mouseOnMenuOff(ligneX-5,ligneY,sizeWidthSearch,sizeHeightSearch)) {   AcceptEvent=false;  changeMenuSearch();  } //If mouse pressOut out the color Menu come back
  
   //Search Function
   int sizeTextInput=round(textWidth(textSearchInput));
   int maxSeachInput=max(150,sizeTextInput+5);
    fill(Textcolor); text("search",ligneX,ligneY+40);
    fill(Textcolor,10);stroke(Textcolor); rect(ligneX,ligneY+50,maxSeachInput,20);
    fill(Textcolor); text(textSearchInput,ligneX+2,ligneY+64);
    if(blinkTest>0)  text("_",ligneX+2+sizeTextInput,ligneY+64);   blinkTest++; if(blinkTest==10)blinkTest=-10; //Blink Cursor
    //Display the result
    if(textSearchInput.length()>0){
      int MaxLength=sizeWidthSearch-100; //Size Maximum for the text in width
      int nbLine=1; //Count the number of line
      for(int i=0;i<textSearch.size();i++){
         String sr=(String)textSearch.get(i);
         //Subdivide in several lines if the width size of the txt is too big
         ArrayList stringDraw=new ArrayList();
         if(textWidth(sr)<MaxLength) stringDraw.add(sr); //The line is less longer than the max widht, that's fine
         else{
           int l=0;
           String[] listSplit = split(sr,' ');
           String cutsr="";
           while(l<listSplit.length){
              if(textWidth(cutsr)>MaxLength-textWidth(listSplit[l])){ //New Cut
                stringDraw.add(new String(cutsr));
                cutsr="       ";
              }
              cutsr+=listSplit[l]+" ";
              l++;
            }
            stringDraw.add(cutsr);
         }
         fill(Textcolor,255); text("*",ligneX,ligneY+100+nbLine*20);
         for(int l=0;l<stringDraw.size();l++){
            // text((String)stringDraw.get(l),ligneX+20,ligneY+100+nbLine*20);  //Normal  without specification
            sr=(String)stringDraw.get(l);
            int j=sr.toLowerCase().indexOf(textSearchInput.toLowerCase());
            int lx=0;
             while(j>0){
             //addMessage("j="+j + "  sr="+sr +  "   st len="+sr.length() + " textInput[0]="+textInput[0]+ " textInput[0].length()="+textInput[0].length());
               fill(Textcolor,150); text(sr.substring(0,j),ligneX+10+lx,ligneY+100+nbLine*20); //First Part of the Sentence
               lx+=textWidth(sr.substring(0,j));
               fill(Textcolor,255); text(sr.substring(j,j+textSearchInput.length()),ligneX+10+lx,ligneY+100+nbLine*20); //the word
               lx+=textWidth(textSearchInput);
               sr=sr.substring(j+textSearchInput.length());
               j=sr.toLowerCase().indexOf(textSearchInput.toLowerCase());
             }
              fill(Textcolor,150); text(sr,ligneX+10+lx,ligneY+100+nbLine*20); //Last part of the word*/
            nbLine++;
           
         }
      }
      sizeHeightSearch=max(200,100+nbLine*20); 
      
    }
}


////////// READ SEARCH
public void ReadCurrentSearch(){
  ArrayList Temp=new ArrayList();
  for(int g=0;g<nbGene;g++){
    Gene gene=getGene(g);
    if(gene.Logics!=null) {
      for(int l=0;l<gene.nbLogic;l++){
          ArrayList Objets=gene.getObjets(l);
          if(Objets!=null)  {
              MetaLogic logi=(MetaLogic)gene.getLogic(l);
              String logiS=gene.Name+" : if ";
              for(int i=0;i<Objets.size();i++)   {
                 String objS=((Objet)Objets.get(i)).toRule();
                 objS=replaceAllString(objS,"G:",""); objS=replaceAllString(objS,"L:",""); objS=replaceAllString(objS,"D:","");objS=replaceString(objS,"#"," ");
                 logiS+=" "+objS;
              }
             
             logiS+=" then="+logi.getThen()+" else="+logi.getElse();
             Temp.add(logiS);
         }
      }
    }
  }
   ModelLoad=new String[Temp.size()];
  for(int i=0;i<Temp.size();i++) ModelLoad[i]=(String)Temp.get(i);
  
}


//Read all the rules and convert them for the search functions
public void ReadSearch(String Name){
  String [] TempModelLoad=loadStrings(Name);
  if(TempModelLoad!=null) ReadSearch(TempModelLoad);
}

public void ReadSearch(String [] TempModelLoad){
  ArrayList Temp=new ArrayList();
  boolean ok=false;
  for(int i=2;i<TempModelLoad.length;i++){
      String sr=TempModelLoad[i];
      if(sr.indexOf("<Rules")!=-1) ok=true;
      if(sr.indexOf("</Rules>")!=-1) ok=false;
      if(ok){
        sr=replaceString(sr,"/>",""); 
        sr=replaceAllString(sr,"G:",""); sr=replaceAllString(sr,"L:",""); sr=replaceAllString(sr,"D:","");sr=replaceString(sr,"#"," ");
        sr=replaceAllString(sr,"\"","");
        sr=replaceAllString(sr,"&gt;",">"); 
        sr=replaceAllString(sr,"&lt;","<");
        sr=replaceString(sr,"isBlue=true","is blue "); 
        sr=replaceString(sr,"isBlueModul=true","as blue modul"); 
        sr=replaceString(sr,"<Rule gene=","");
        sr=replaceString(sr," if=",": if=");
        Temp.add(sr);
    }
  }
  ModelLoad=new String[Temp.size()];
  for(int i=0;i<Temp.size();i++) ModelLoad[i]=(String)Temp.get(i);
}

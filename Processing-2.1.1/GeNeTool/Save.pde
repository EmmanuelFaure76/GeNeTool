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

public void saveXMLFile(File selection){
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    String filename=selection.getAbsolutePath();
    if(!getFileExtension(filename).equals("xml")) filename=filename+".xml";
    lastModelLoaded=filename; saveXML(filename);
  } 
}

public void saveXML(String filename){
  if(!getFileExtension(filename).equals("xml")) filename=filename+".xml";
  XML Modelxml = loadXML("temp.xml");
  Modelxml.setName("Model");
  Modelxml.setString("date",""+String.valueOf(day())+":"+String.valueOf(month())+":"+String.valueOf(year())+"-"+String.valueOf(hour())+":"+String.valueOf(minute())+":"+String.valueOf(second()));
  Modelxml.setString("MaxTime",""+MaxTime);
 
  if(Regions.size()>0){
    XML Regionxml = Modelxml.addChild("Regions"); 
    for(int d=0;d<Regions.size();d++){
       Region reg=(Region)Regions.get(d);
       XML RegionEltxml = Regionxml.addChild("Tree");
       RegionEltxml.setString("Name",reg.Name);
       RegionEltxml.setString("Number",""+reg.number);
       RegionEltxml.setString("Start",""+reg.hours[0]);
       RegionEltxml.setString("End",""+reg.hours[1]);
    };
  }
  
  if(Domains.size()>0){
    XML Domainsxml = Modelxml.addChild("Domains"); 
    for(int d=0;d<Domains.size();d++)
      saveDomain(Domainsxml,(Domain)Domains.get(d));
  }
  
  
  XML dataxml=Modelxml.addChild("Expression");
  dataxml.setString("timeUnit",timeUnit);
  for(int g=0;g<Genes.size();g++){
     saveExpression(dataxml,(Gene)Genes.get(g));
  }
  
 
 
  XML Rulesxml =  Modelxml.addChild("Rules");
  Rulesxml.setString("type","Vector Equations");
  for(int g=0;g<Genes.size();g++){
    Gene gene=(Gene)Genes.get(g);
    if(gene.Logics!=null) {
      for(int l=0;l<gene.nbLogic;l++)
            saveRule(Rulesxml,gene,(MetaLogic)gene.getLogic(l),gene.getObjets(l));
    } else  saveEmptyRule(Rulesxml,gene);
  }
 
  
  
  if(isActive("Compare")){
   XML Comparexml =  Modelxml.addChild("Compare");
  Comparexml.setString("activeCompareRows",activeCompareRows);
  Comparexml.setString("activeCompareLigne",activeCompareLigne);
  Comparexml.setString("activeCompareThird",activeCompareThird);
  Comparexml.setString("timeCompareBegin",""+timeCompareBegin);
  Comparexml.setString("timeCompareEnd",""+timeCompareEnd);
  Comparexml.setString("spaceCompareLine",""+spaceCompareLine);
  Comparexml.setString("modeCompareBox",""+modeCompareBox);
  Comparexml.setString("modeCompareData",""+modeCompareData);
  Comparexml.setString("compareDataVersion",""+compareDataVersion);
  if(compareDataVersion==-1){
      XML DomainsComparexml=Comparexml.addChild("compareOthersDomains");
      for(int i=0;i<compareOthersDomains.length;i++){
        DomainsComparexml.setString("Domain",""+compareOthersDomains[i]);
        DomainsComparexml.setString("i",""+i);
        
      }
  }
 
  }
  

  if(isActive("model")){
 if(MyModels!=null) 
     for(int m=0;m<MyModels.length;m++){
        Model model=MyModels[m];
        XML Versionxml=Modelxml.addChild("Version");
        Versionxml.setString("number",""+model.number);
        Versionxml.setString("date",model.date);
        Versionxml.setString("Name",model.Name);
        Versionxml.setString("lock",""+model.lock);
        Versionxml.setString("step",""+model.step);
        
 
        for(int d=0;d<nbDomain;d++){ 
             ModelDomain md=model.getDomain(d);
             XML DomainsXML = Versionxml.addChild("Domains");
             DomainsXML.setString("Name",""+md.getName());
     
             for(int s=0;s<=model.step;s++){
                     XML StepsXML =  DomainsXML.addChild("Step");
                     StepsXML.setString("Number",""+s);
                     StepsXML.setString("hpf",""+model.Correspons[s]);
                     //boolean ongene=false;
                     for(int i=0;i<nbGene;i++){ 
                           Gene gene=getGene(i);
                           if(md.Manual[i][s] || md.isBlueState[i][s] || md.GenesStates[i][s]){ //We save only the information relevant (not automatic false)
                             //ongene=true;
                             XML GenesXML = StepsXML.addChild("Gene");
                             GenesXML.setString("Name",gene.Name);
                             String value="";
                             if(md.Manual[i][s]) GenesXML.setString("Manual",""+md.GenesStates[i][s]);
                             if(md.isBlueState[i][s]) GenesXML.setString("BlueState","true");
                              
                           }
                     }
                     //if(ongene)  DomainsXML.addChild(StepsXML); 
             }
             
        }
        
     }
  }
  
  saveConfig(Modelxml);
  writeXML(Modelxml, filename);
}



//Save Data Expression for each Gene
public void saveExpression(XML ParentXML, Gene gene){
  if(gene.Expression!=null) {
      XML Dataxml = ParentXML.addChild("Gene");
      Dataxml.setString("name",gene.Name);
      boolean expresomewhere=false;
      for(int s=0;s<Regions.size();s++){
          Region reg=getRegion(s);
          boolean expre=false; for(int i=reg.hours[0];i<=reg.hours[1];i++) if(gene.Expression[s][i]>=0)expre=true;
          if(expre){ //Save only if we have at least one expression
            expresomewhere=true;
            XML RegionXML =Dataxml.addChild("Region");
            RegionXML.setString("name",reg.Name);
            String value="";
            for(int i=reg.hours[0];i<=reg.hours[1];i++) if(gene.Expression[s][i]>=0) value=value+i+":"+gene.Expression[s][i]+";";
            RegionXML.setString("value",value); 
            
          }
      }
  }
}
  
  
//<Domain name="E1X" >
public void saveDomain(XML ParentXML,Domain dom){
  XML Domainxml =  ParentXML.addChild("Domain");
  Domainxml.setString("name",dom.Name);
  String Tree="";
  for(int i=0;i<dom.Tree.size();i++){
      Region reg=dom.getTree(i);
      Tree+=reg.Name+";";
  }
  if(!Tree.equals("")) Domainxml.setString("tree",Tree);
  
  if(dom.DefObjets!=null) {
      String def="";
      for(int d=0;d<dom.DefObjets.length;d++){
          if(d>0) def=def+", " ;
          String DefObjets="";
          for(int i=0;i<dom.DefObjets[d].length;i++)
                DefObjets=DefObjets+dom.DefObjets[d][i].toRule()+" ";
          def+=DefObjets;
      }
      if(!def.equals("")) Domainxml.setString("def",def);
  }
}

public void saveEmptyRule(XML ParentXML,Gene gene){
   XML Rulexml =  ParentXML.addChild("Rule");
   Rulexml.setString("gene",gene.Name);
   Rulexml.setString("then","1");
   Rulexml.setString("else","0");
}
//<Rule gene="Pmar1" if="Tcf=1 and Otx=1 and not HesC=1" then="1" else="0"/>
public void saveRule(XML ParentXML,Gene gene, MetaLogic logi,ArrayList Objets){   saveRule(ParentXML,gene,logi.getThen(),logi.getElse(),logi.getName(),Objets,logi.isBlue,logi.dft);}


public void saveRule(XML ParentXML,Gene gene, String Then,String Else,String id,ArrayList Objets,boolean blueLogic,boolean dft){
  XML Rulexml =  ParentXML.addChild("Rule");
   Rulexml.setString("gene",gene.Name);
   if(!gene.isGene) Rulexml.setString("isGene",""+gene.isGene);
   if(gene.isUbiquitous) Rulexml.setString("isUbiquitous",""+gene.isUbiquitous);
   if(gene.isUnknown) Rulexml.setString("isUnknown",""+gene.isUnknown);
   if(gene.isMaternal) Rulexml.setString("isMaternal",""+gene.isMaternal);
   if(blueLogic) Rulexml.setString("isBlueModul",""+blueLogic);
   if(dft) Rulexml.setString("isDefault",""+dft);
   
   
   String logiS="";
   if(Objets!=null)   for(int i=0;i<Objets.size();i++)   {
         logiS+=" "+((Objet)Objets.get(i)).toRule();
        //addMessage(""+((Objet)Objets.get(i)).getClassName()+" at " +i + " -> " + ((Objet)Objets.get(i)).toRule());
   }
   if(verbose>=3)   addMessage("Save Gene " + gene.Name+ " with Objetcs " + logiS);
   Rulexml.setString("if",logiS);
   Rulexml.setString("then",Then);
   Rulexml.setString("else",Else);
   if(!trim(id).equals(""))  Rulexml.setString("id",id);
  
}


void writeXML(XML element,String filename){
  addMessage("Write "+filename);
  saveXML(element,filename);
  /*PrintWriter xmlfile = createWriter(filename);
  XMLWriter writer = new XMLWriter(xmlfile);
  try {
      writer.write(element);
      xmlfile.flush();
      xmlfile.close();
  } catch (IOException e) {
      e.printStackTrace();
  }*/
 
   //This prog writes it without any enter
  String [] xmlS=loadStrings(filename);
  
  int nb=0;
  ArrayList newStr=new ArrayList();
  for(int i=0;i<xmlS.length;i++){
    String stri=xmlS[i];
    int j=stri.indexOf("><");
    while(j>0){
      String fi=stri.substring(0, j+1);
      String olfi=fi;
      if(olfi.charAt(1)=='/')nb--;
      for(int k=0;k<nb;k++)  fi=char(9)+fi;
      newStr.add(fi);
      if(olfi.charAt(1)!='/' && olfi.charAt(olfi.length()-2)!='/')nb++;
     
      stri=stri.substring(j+1);
      j=stri.indexOf("><");
    }
    newStr.add(stri);
  }
  String []strr=new String[newStr.size()];
  for(int i=0;i<newStr.size();i++)strr[i]=(String)newStr.get(i);
  saveStrings(filename,strr);
  addMessage("End Write " +filename);
}



///////////////////////////////////////////////////////// Export Rule
void ExportRulesTXTFile(File selection){  if (selection != null)  ExportRulesTXT(selection.getAbsolutePath());}
void ExportRulesTXT(String Name){
   if(!getFileExtension(Name).equals("txt")) Name=Name+".txt";
           
  //First Calcul the Maxmimum Length of all Logic
  int MaxLength=0;
  for(int g=0;g<Genes.size();g++){
    Gene gene=(Gene)Genes.get(g);
     String GeneS=gene.Name;   while(GeneS.length()<15) GeneS+=" "; 
    for(int l=0;l<gene.nbLogic;l++){
     MetaLogic logi=(MetaLogic)gene.getLogic(l);
      ArrayList Objets=gene.getObjets(l);
      if(Objets!=null)  {
           if(!logi.getName().equals("")) GeneS+=logi.getName();
            while(GeneS.length()<25) GeneS+=" ";
            GeneS+="if ";
             for(int i=0;i<Objets.size();i++)   {
                String rr=((Objet)Objets.get(i)).toRule();
                rr=replaceString(rr,"G:","");   rr=replaceString(rr,"L:","");rr=replaceString(rr,"D:","");rr=replaceString(rr,"#"," "); 
                GeneS+=" "+rr;
             }
            GeneS+=" then="+logi.getThen()+ " else="+logi.getElse();
            if(GeneS.length()>MaxLength)MaxLength=GeneS.length();
      }
      GeneS="";  while(GeneS.length()<15) GeneS+=" ";
    }
   }
  
  String LinesBetwen="";
  for(int i=0;i<MaxLength;i++)LinesBetwen+="-";

  ArrayList StringGenes=new ArrayList();
  
  
  for(int g=0;g<Genes.size();g++){
    Gene gene=(Gene)Genes.get(g);
    String GeneS=gene.Name;   while(GeneS.length()<15) GeneS+=" "; 
    for(int l=0;l<gene.nbLogic;l++){
     MetaLogic logi=(MetaLogic)gene.getLogic(l);
      ArrayList Objets=gene.getObjets(l);
      if(Objets!=null)  {
            if(!logi.getName().equals("")) GeneS+=logi.getName();
            while(GeneS.length()<25) GeneS+=" ";
            GeneS+="if ";
             for(int i=0;i<Objets.size();i++)   {
                String rr=((Objet)Objets.get(i)).toRule();
                rr=replaceString(rr,"G:","");   rr=replaceString(rr,"L:","");rr=replaceString(rr,"D:","");rr=replaceString(rr,"#"," "); 
                GeneS+=" "+rr;
             }
            GeneS+=" then="+logi.getThen()+ " else="+logi.getElse();
      }
     // GeneS+="\n";
      
      StringGenes.add(GeneS);
      GeneS="";  while(GeneS.length()<15) GeneS+=" ";
    }
    StringGenes.add(LinesBetwen);
  }
  
  String []StringGeneS=new String[StringGenes.size()];
  for(int i=0;i<StringGenes.size();i++)   StringGeneS[i]=(String)StringGenes.get(i);
  addMessage("Export Rules to " +Name);
  saveStrings(Name,StringGeneS);
}

void ExportRulesPDFFile(File selection){  if (selection != null)  ExportRulesPDF(selection.getAbsolutePath());}
void ExportRulesPDF(String Name){
  if(!getFileExtension(Name).equals("pdf")) Name=Name+".pdf";
  float ratioA4=28;//28.3446712018141;
  int WidthPDF=round(21*ratioA4); //Max width of the pdf page 
  int HeightPDF=round(29.7*ratioA4); //Max height of the pdf page
  int WidthGene=0;  //Max size of the Gene Colum
  int WidthLogic=0; //Max size of the name of the logic 
  int WidthLogicDef=0; //Max Size of the logic definition
  
  PFont PDFFont = createFont("TimesNewRomanPSMT", 10);
  PFont PDFFontBold = createFont("TimesNewRomanPS-BoldMT", 10);
  textFont(PDFFont, 10);
  
  
  //First calcul the Marge (NOP TO BIG)
  int incGene=0;
  for(int g=0;g<Genes.size();g++){
    Gene gene=(Gene)Genes.get(g);
    if(floor(textWidth(gene.Name))+1>WidthGene)WidthGene=floor(textWidth(gene.Name))+1;
    
    for(int l=0;l<gene.nbLogic;l++){
      MetaLogic logi=(MetaLogic)gene.getLogic(l);
      ArrayList Objets=gene.getObjets(l);
      if(Objets!=null)  {
            if(!logi.getName().equals(""))if(floor(textWidth(logi.getName()))+1>WidthLogic)WidthLogic=floor(textWidth(logi.getName()))+1; 
            String GeneS= "if ";
             for(int i=0;i<Objets.size();i++)   {
                String rr=((Objet)Objets.get(i)).toRule();
                rr=replaceString(rr,"G:","");rr=replaceString(rr,"L:","");rr=replaceString(rr,"D:","");rr=replaceString(rr,"#"," "); 
                GeneS+=" "+rr;
            }
            GeneS+=" then="+logi.getThen()+ " else="+logi.getElse();
            //if(floor(textWidth(GeneS))+1>WidthPDF)WidthPDF=floor(textWidth(GeneS))+1; 
      }
      incGene++;
    }
  } 
  WidthGene+=15; 
  WidthLogic+=WidthGene+5;
  WidthLogicDef=WidthPDF+30; //Size Colum Logic
  
  
  //HeightPDF=(incGene+1)*20+25;
  textFont(myFont, 12);
  
  PGraphics pdf = createGraphics(WidthPDF, HeightPDF, PDF, Name);
  PGraphicsPDF pdfg = (PGraphicsPDF) pdf;  // Get the renderer
  pdf.beginDraw();
  pdf.background(255,255,255);
 // pdf.textFont(PDFFontBold, 12);
  pdf.textFont(PDFFont, 12);
  pdf.fill(0,0,0);
  incGene=0;
  pdf.text("Gene",10,incGene*20+20);
  //pdf.text("Logic",WidthGene+5,incGene*20+20);
  pdf.text("Vector Equation",WidthGene+5,incGene*20+20);
  incGene++;
  pdf.textFont(PDFFont, 10);
  for(int g=0;g<Genes.size();g++){
     Gene gene=(Gene)Genes.get(g);
     pdf.fill(0,0,0);
     //if(gene.isUnknown)  pdf.fill(colorUnknownGene);
     //if(!gene.isGene) pdf.fill(colorBoxNoGene);
     if(incGene>38){ //New Page 
         pdfg.nextPage();  // Tell it to go to the next page
         incGene=0;
     }
     //pdf.textFont(PDFFontBold, 10);
     pdf.textFont(PDFFontBold, 10);
     pdf.text(gene.Name,10,incGene*20+20); //Draw the Gene Name
     int indStart=incGene;
     
     pdf.textFont(PDFFont, 10); pdf.fill(0,0,0);
     for(int l=0;l<gene.nbLogic;l++){ //For Each Logic
         MetaLogic logi=(MetaLogic)gene.getLogic(l);
         ArrayList Objets=gene.getObjets(l);
      
         if(Objets!=null)  {
           pdf.fill(50,50,50);
           if(!logi.getName().equals("")) pdf.text(logi.getName(),WidthGene+5,incGene*20+20); 
           pdf.fill(0,0,0);
           String GeneS= "if ";
            for(int i=0;i<Objets.size();i++)   {
                String rr=((Objet)Objets.get(i)).toRule();
                rr=replaceString(rr,"G:","");rr=replaceString(rr,"L:","");rr=replaceString(rr,"D:","");rr=replaceString(rr,"#"," "); 
                GeneS+=" "+rr;
            }
            GeneS+=" then="+logi.getThen()+ " else="+logi.getElse(); 
            while(textWidth(GeneS)>WidthLogicDef){ //To Much Caracteres in the width of the page
              int pos=GeneS.length();
              while(textWidth(GeneS.substring(0,pos))>WidthLogicDef) pos--; //Move back until size fitt
              while(GeneS.charAt(pos)!=' ') pos--; //Move Back until find the last space characters
              pdf.text(GeneS.substring(0,pos),WidthLogic,incGene*20+20); incGene++; //Draw the first part
              GeneS="    "+GeneS.substring(pos+1); //Little shift on the right
            }
            pdf.text(GeneS,WidthLogic,incGene*20+20); //Draw the Logic
          }
      incGene++;
      
    }
    pdf.fill(100,100,100,10);   pdf.stroke(0,0,0,150); 
    pdf.rect(5,indStart*20+10,WidthPDF-10,(incGene-1-indStart)*20+15);
    pdf.line(WidthGene+3,indStart*20+11,WidthGene+3,(incGene-1)*20+25);
  }
  

  //Domains Page
  pdfg.nextPage();  // Tell it to go to the next page
  WidthLogic=100;
  pdf.background(250,250,250);
  //pdf.textFont(PDFFontBold, 12);
  pdf.textFont(PDFFont, 12);
  pdf.fill(0,0,0);
  incGene=0;
  pdf.text("Domain",10,incGene*20+20);
  pdf.text("Definition",WidthLogic+5,incGene*20+20);
  incGene++;
  pdf.textFont(PDFFont, 10);
  int nbPages=0;
  for(int m=0;m<Domains.size();m++){
     Domain dom=(Domain)Domains.get(m);
     if((incGene+dom.DefObjets.length)*20+20>HeightPDF){ 
         pdfg.nextPage();  // Tell it to go to the next page
        incGene=0;nbPages++;
      }
      
      
      pdf.fill(0,0,0);
      //pdf.textFont(PDFFontBold, 10);
      pdf.textFont(PDFFont, 10);
      pdf.text(replaceString(dom.Name,"#"," "),10,incGene*20+20);
      pdf.textFont(PDFFont, 10);
     
      int indStart=incGene;
     
      String Tree="Lineage Tree : ";
      for(int i=0;i<dom.Tree.size();i++){ 
         Region reg=dom.getTree(i);
         Tree+=reg.Name+", "; 
       }
      if(!Tree.equals("Lineage Tree : ")) { pdf.text(Tree,WidthLogic+5,incGene*20+20); incGene++;}
  
      if(dom.DefObjets!=null) {
            for(int d=0;d<dom.DefObjets.length;d++){
                  String DefObjets=dom.DefObjets[d][0].toRule();
                  pdf.text(DefObjets,WidthLogic+5,incGene*20+20);
                  DefObjets=replaceString(dom.DefObjets[d][1].toRule(),"D:","");DefObjets=replaceString(DefObjets,"#"," ");
                  pdf.text(DefObjets,WidthLogic+105,incGene*20+20);
                  incGene++;
            }
       }
       pdf.fill(100,100,100,10); 
       pdf.rect(5,indStart*20+10,WidthPDF-10,(incGene-1-indStart)*20+15);
       pdf.line(WidthLogic,indStart*20+10,WidthLogic,(incGene-1)*20+25);
  }
  addMessage("Export Rules to " +Name);          
  pdf.dispose();
  pdf.endDraw();  
}






//////////////////////////////////////////////////////////////////////////////////////////   SAVE AND LOAD CONFIGURATION FILE


void saveConfig(XML ParentXML){
  XML Configxml=ParentXML.addChild("Config");
  Configxml.setString("Version",Version);
   for(int Comp=1;Comp<=MenuColorList.length;Comp++){
         color colorchoose=colorMenu(Comp);
          XML MenuXML = Configxml.addChild("Color");
           MenuXML.setString("Name",MenuColorList[Comp-1]);
           MenuXML.setString("red",""+red(colorchoose));
           MenuXML.setString("green",""+green(colorchoose));
           MenuXML.setString("blue",""+blue(colorchoose));
           MenuXML.setString("alpha",""+alpha(colorchoose));
           
   }
     
  for(int i=0;i<nbMenu;i++){
    //Save a ligne for Each Menu
     XML MenuXML =  Configxml.addChild("Menu");
     MenuXML.setString("Number",""+i);
     MenuXML.setString("OriginX",""+OrginMenuXY[i][0]);
     MenuXML.setString("OriginY",""+OrginMenuXY[i][1]);
     MenuXML.setString("SizeX",""+SizeMenuXY[i][0]);
     MenuXML.setString("SizeY",""+SizeMenuXY[i][1]);
     MenuXML.setString("Active",""+MenuActive[i]);
  }
  
   XML FontXML =Configxml.addChild("Font");
   FontXML.setString("Number",""+font);
   FontXML.setString("Name",""+(String)allFonts.get(font));
   
   
   
    XML ScreenXML = Configxml.addChild("Screen");
    ScreenXML.setString("ScreenX",""+ScreenX);
    ScreenXML.setString("ScreenY",""+ScreenY);
    ScreenXML.setString("frameRate",""+frameR);
     
     
}



//Read the Config XML element
public void ReadConfig(XML element){
  if(element.hasAttribute("Version"))  { Version=element.getString("Version"); addMessage("Load GeNeTool Version "+Version);}
  
  XML[]children=element.getChildren();
  for(int j=0;j<children.length;j++){
    if(children[j].getName().equals("Menu")) readMenu(children[j]);
    if(children[j].getName().equals("Color")) readColor(children[j]);
    if(children[j].getName().equals("Font")) readFont(children[j]);
    if(children[j].getName().equals("Screen")) readScreen(children[j]);
  }
}

public void readScreen(XML element){
    if(element.hasAttribute("ScreenX"))  ScreenX=parseInt(element.getString("ScreenX"));
    if(element.hasAttribute("ScreenY"))  ScreenY=parseInt(element.getString("ScreenY"));
    if(element.hasAttribute("frameRate"))  frameR=parseInt(element.getString("frameRate"));
}


public void readFont(XML element){
   if(element.hasAttribute("Number")) {
     font=parseInt(element.getString("Number"));
     myFont = (PFont)allmyFonts.get(font);
   }
   else if(element.hasAttribute("Name")) {
       String Name=element.getString("Name");
       for(int i=0;i<allFonts.size();i++)
         if(Name.toLowerCase().equals(((String)allFonts.get(i)).toLowerCase())){
           font=i;
           myFont = (PFont)allmyFonts.get(font);
         }
   }
}


public void readColor(XML element){
  String Name=""; color colorchoose=color(0,0,0);
   if(element.hasAttribute("Name")) Name=element.getString("Name");
   if(element.hasAttribute("red")) colorchoose=color(parseFloat(element.getString("red")),green(colorchoose),blue(colorchoose),alpha(colorchoose));
   if(element.hasAttribute("green")) colorchoose=color(red(colorchoose),parseFloat(element.getString("green")),blue(colorchoose),alpha(colorchoose));
   if(element.hasAttribute("blue")) colorchoose=color(red(colorchoose),green(colorchoose),parseFloat(element.getString("blue")),alpha(colorchoose));
   if(element.hasAttribute("alpha")) colorchoose=color(red(colorchoose),green(colorchoose),blue(colorchoose),parseFloat(element.getString("alpha")));
   for(int Comp=1;Comp<=MenuColorList.length;Comp++){
     if(Name.equals(MenuColorList[Comp-1]))
      assignColorMenu(Comp,colorchoose);
   }
}

//Read each type of Menu
public void readMenu(XML element){
  if(element.hasAttribute("Number")) {
     int num=element.getInt("Number");
     if(element.hasAttribute("OriginX")) OrginMenuXY[num][0]=element.getInt("OriginX");
     if(element.hasAttribute("OriginY")) OrginMenuXY[num][1]=element.getInt("OriginY");
     
     if(element.hasAttribute("SizeX")) SizeMenuXY[num][0]=element.getInt("SizeX");
     if(element.hasAttribute("SizeY")) SizeMenuXY[num][1]=element.getInt("SizeY");
     
     if(element.hasAttribute("Active")) MenuActive[num]=parseInt(element.getString("Active"));
     
     
  }
}



///////////////////////////////////////////////////////// Export CSV
void saveCSVFile(File selection){  if (selection != null)  saveCSV(selection.getAbsolutePath());}
void saveCSV(String Name){
   if(MyModels!=null) {
     if(!getFileExtension(Name).equals("csv")) Name=Name+".csv";
     ArrayList ModelsString=new ArrayList();
     String whiteLine=";";   for(int i=0;i<nbGene;i++) whiteLine+=";"; 
     for(int m=0;m<MyModels.length;m++){
        Model model=MyModels[m];
        if(MyModels.length>1) {
            String versionsS=model.Name+";";
            for(int i=0;i<nbGene;i++) versionsS+=";";
            ModelsString.add(versionsS);
        }
       for(int d=0;d<nbDomain;d++){ 
             ModelDomain md=model.getDomain(d);
             String domS=md.getName()+";";
              for(int i=0;i<nbGene;i++){ Gene gene=getGene(i);domS+=gene.Name+";"; }
              ModelsString.add(domS);
             
              for(int s=0;s<=model.step;s++){
                 String stepS=""+model.Correspons[s]+" "+timeUnit+";";
                 for(int i=0;i<nbGene;i++) {
                        if(md.Manual[i][s] || md.isBlueState[i][s])  {stepS+="M"; //else stepS+="A";
                         int v=0; if(md.GenesStates[i][s]) v=1;  stepS+=""+v;
                        // if(!md.SameModelAsData(i,s,md.Correspons[s]))  StepS+="/D"+(1-v);
                        }
                        else{ if(md.GenesStates[i][s]) stepS+="1"; }
                         stepS+=";";
                  }
                  ModelsString.add(stepS);
              }
              ModelsString.add(whiteLine); //Jsut add a white line
        }
         ModelsString.add(whiteLine);  ModelsString.add(whiteLine);    
     }
     String []lines=new String[ModelsString.size()];
     for(int i=0;i<ModelsString.size();i++)lines[i]=(String)ModelsString.get(i);
     saveStrings(Name, lines);
     addMessage("End save in CSV " + Name);
   }
}


///////////////////////////////////////////////////////// Export Model as PDF
void ExportModelasPDF(File selection){  
    if (selection != null)   {
            String Name=selection.getAbsolutePath();
            if(!getFileExtension(Name).equals("pdf")) Name=Name+".pdf";
             MenuCapture=getNumMenu("model"); 
             saveMenu();
             ResizeMenuXY(MenuCapture,0,20*nbGene+30+SizeDrawDomain);  //Resize Menu X
             ResizeMenuXY(MenuCapture,1,100+(20*(1+MyModel.ActualStep)+30));  //Resize Menu Y
             pdfCapture = createGraphics(SizeMenuXY[MenuCapture][0]+1,SizeMenuXY[MenuCapture][1]+1, PDF, Name);
             pdfCapture.beginDraw();
             ReOrderMenu(MenuCapture); //Put the Model Menu in front 
             OrginMenuXY[MenuCapture][0]=1;OrginMenuXY[MenuCapture][1]=1; //Change the Coordinate to the top
             for(int i=0;i<nbMenu;i++) MenuActive[i]=-1;  MenuActive[MenuCapture]=1 ; //Hide and unhide the menu
             CaptureNb=-1;
             toDo=5;
       }
}
void ExportDataasPDF(File selection){  
    if (selection != null)   {
            String Name=selection.getAbsolutePath();
            if(!getFileExtension(Name).equals("pdf")) Name=Name+".pdf";
           MenuCapture=getNumMenu("data"); 
           saveMenu();
           ResizeMenuXY(MenuCapture,0,20*nbGene+30+SizeDrawDomain);  //Resize Menu X
           ResizeMenuXY(MenuCapture,1,50+SizeDrawDomain+21*MaxTime);  //Resize Menu Y
           pdfCapture = createGraphics(SizeMenuXY[MenuCapture][0]+1,SizeMenuXY[MenuCapture][1]+1, PDF, Name);
           pdfCapture.beginDraw();
           ReOrderMenu(MenuCapture); //Put the Model Menu in front 
           OrginMenuXY[MenuCapture][0]=1;OrginMenuXY[MenuCapture][1]=1; //Change the Coordinate to the top
           for(int i=0;i<nbMenu;i++)  MenuActive[i]=-1 ;  MenuActive[MenuCapture]=1 ; //Hide and unhide the menu
           CaptureNb=-1;
           toDo=5;
    }
 }
void ExportCompareasPDF(File selection){  
    if (selection != null)   {
              String Name=selection.getAbsolutePath();
              if(!getFileExtension(Name).equals("pdf")) Name=Name+".pdf";
               pdfCapture = createGraphics(SizeMenuXY[MenuCapture][0]+1,SizeMenuXY[MenuCapture][1]+1, PDF, Name);
               pdfCapture.beginDraw();
               toDo=5;
               MenuCompare(1,1,MenuCapture);
               toDo=0;
               PImage im=new PImage(width,height);
               loadPixels();
               for(int i=0;i<width*height;i++)  im.pixels[i]=pixels[i];
               im.updatePixels();
               PImage imcrop=im.get(0,0,SizeMenuXY[MenuCapture][0]+1,SizeMenuXY[MenuCapture][1]+1);
               pdfCapture.image(imcrop,0,0); 
               pdfCapture.dispose();
               pdfCapture.endDraw(); 
       }
}


void SnapShot(File selection){  
    if (selection != null)   {
             String Name=selection.getAbsolutePath();
             if(!getFileExtension(Name).equals("tif")) Name=Name+".tif";
             PImage im=new PImage(width,height);
              loadPixels();
              for(int i=0;i<width*height;i++)  im.pixels[i]=pixels[i];
               im.updatePixels();
               im.save(Name);
               addMessage("Save "+Name); 
    }
}

     
 



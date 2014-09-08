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

import java.io.File;
import java.util.ArrayList;

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Functions.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

public class UtilityFuncs {

  private GRNBoolModel p;
  
  UtilityFuncs(GRNBoolModel p) {
    this.p = p;   
  } 

  public void drawString(String Name,int x,int y,float s,int c){
    if(s==-1) s=p.textWidth(Name)+4;
    if(p.eh.mouseOn(x,y,s,20)) p.fill(c,p.mm.ActiveColor);   else p.fill(c,p.mm.InactiveColor);
    p.stroke(c,255);   p.rect(x,y,s,20);
    p.fill(p.cm.Textcolor);   p.text(Name,x+s/2-p.textWidth(Name)/2,y+14); 
  }

  //Convert a String to a boolean
  boolean parseBool(String fbool) {
    if (fbool.equals("true")) return true;
    if (fbool.equals("True")) return true;
    if (fbool.equals("1")) return true;
    return false;
  }
  
  
  //Return the Y coodinate with Y=aX+b;
  float coordDroiteY(float X, float a, float b) { 
    return a*X+b;
  }
  //Return the X coodinate with Y=aX+b;
  float coordDroiteX(float Y, float a, float b) { 
    return (Y-b)/a;
  }
  //Round a float with i decimal after virgule
  float roundAt(float x, int i) {
    if (i==0) return p.round(x);
    x=p.round(x*p.pow(10, i));
    return x/p.pow(10, i);
  }
  
  public ArrayList combineGeneList(ArrayList L1, ArrayList L2) {
    ArrayList L3=new ArrayList();
    for (int i=0;i<L1.size();i++) {
      Gene g1=(Gene)L1.get(i);
      boolean inL2=false;
      for (int j=0;j<L2.size();j++) {
        Gene g2=(Gene)L2.get(j);
        if (g2==g1 || g1.Name.equals(g2.Name))  inL2=true;
      }
      if (!inL2) L3.add(g1);
    }
  
    for (int j=0;j<L2.size();j++) L3.add(L2.get(j));
    return L3;
  }
  
  //Compare 2 string aussi en lower case
  boolean equal(String Name1, String Name2) {
    if (Name1.equals(Name2)) return true;
    if (Name1.compareToIgnoreCase(Name2)==0) return true;
    return false;
  }
  
  //Return the number of occurence in a string
  int countString(String str, String name) {
    int nb=0;
    int p=str.indexOf(name);
    while (p>=0) {  
      nb++; 
      str=str.substring(p+name.length()); 
      p=str.indexOf(name);
    };
    return nb;
  }
  //Delete the same occurent in the String
  String delSameString(String name) {
    String []splitname=p.split(p.trim(name), ' ');
    String NewName=splitname[0];
    for (int i=1;i<splitname.length;i++) {
      boolean is=false;
      for (int j=0;j<i;j++)
        if (splitname[i].equals(splitname[j])) is=true;
      if (!is)NewName+=", "+splitname[i];
    }
    return NewName;
  }
  
  //Like trim with space but also with , and ;
  public String trimPlus(String truc) {
    String tructruc="";
    boolean start=false;
    //Forward
    for (int i=0;i<truc.length();i++) {
      if (!start) {
        if (truc.charAt(i)==' ' || truc.charAt(i)==',' || truc.charAt(i)==';') {
        }
        else start=true;
      }
      if (start)  tructruc=tructruc+truc.charAt(i);
    }
    truc="";
    //Backward
    start=false;
    for (int i=tructruc.length()-1;i>=0;i--) {
      if (!start) {
        if (tructruc.charAt(i)==' ' || tructruc.charAt(i)==',' || tructruc.charAt(i)==';') {
        }
        else start=true;
      }
      if (start)  truc=tructruc.charAt(i)+truc;
    }
  
    return truc;
  }
  
  //Replace a string a by b in in
  String replaceString(String in, String a, String b) {
    if(in.indexOf(a)>=0) {
      in=in.substring(0, in.indexOf(a)) + b + in.substring(in.indexOf(a)+a.length(), in.length()) ;
    }
    return in;
  }
  
  //Replace a string a by b in in
  String replaceAllString(String in, String a, String b) {
    while(in.indexOf(a)>=0) {
      in=in.substring(0, in.indexOf(a)) + b + in.substring(in.indexOf(a)+a.length(), in.length()) ;
    }
    return in;
  }
  
  public ArrayList combine(ArrayList l1, ArrayList l2) {
    ArrayList l3=new ArrayList();
    for (int i=0;i<l1.size();i++) l3.add(l1.get(i));
    for (int i=0;i<l2.size();i++) l3.add(l2.get(i)); 
    return l3;
  }
  
  
  public String getFileExtension(String filename) {
    String ext = "";
    int i = filename.lastIndexOf('.');
    if (i > 0 &&  i < filename.length() - 1) {
      ext = filename.substring(i+1).toLowerCase();
    }
    return ext;
  }
  
  public String getFileName(String filename){
    String name = "";
    int i = filename.lastIndexOf('/');
    if (i > 0 &&  i < filename.length() - 1) {
      name = filename.substring(i+1);
    }
    return name;
  }
  
  public String now(){
   return ""+String.valueOf(p.day())+":"+String.valueOf(p.month())+":"+String.valueOf(p.year())+"-"+String.valueOf(p.hour())+":"+String.valueOf(p.minute())+":"+String.valueOf(p.second()); 
  }
  
  public boolean delete(String filename){
    String fileName = p.dataPath(filename);
    File f = new File(fileName);
    if (f.exists()) {
      f.delete();
      return true;
    }
    return false;
  }
  
  
  //Reimplemente selectOutput which change in Processing > 2
  
  //
  // WJRL New implementation for save and load:
  // Pretty sure the actual callback functions have to live in the PApplet?
  //
  
  File Gselection;
  int pendingToDo;
  
  void selectOutput(String ask, int todo) {
    pendingToDo = todo;
    p.selectOutput(ask, p.getFileCallbackFunctionName(todo));
  }
  
  void selectInput(String ask, int todo){
    pendingToDo = todo;
    p.selectInput(ask, p.getFileCallbackFunctionName(todo)); 
  }
  
  
  /*public String selectOutputPro(String prompt, String filestring) { 
    //return selectFileImplPro(prompt, filestring, FileDialog.SAVE);
    //return selectInputPro(prompt,filestring);
    return selectMyOutput(prompt,filestring);
  }
  
  
  //boolean FirstSelectInput=true;
  public String selectInput(String prompt, String filestring) { 
    //if(FirstSelectInput){FirstSelectInput=false; return selectInput(prompt);}
    //return selectInputPro(prompt,filestring);
    //return selectFileImpl(prompt,FileDialog.LOAD,lastDirectory);
    return selectMyInputPro(prompt,filestring);
  }
  public String selectInputPro(String prompt, String filestring) { 
   // System.setProperty("apple.awt.fileDialogForDirectories", "true");
    addMessage("okok");
    JFileChooser chooser = new JFileChooser();
    addMessage("okok");
    //FileNameExtensionFilter filter = new FileNameExtensionFilter( filestring + " files", filestring);
    //chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(this);
   addMessage("okok");
   if (returnVal == JFileChooser.APPROVE_OPTION)   return chooser.getSelectedFile().getAbsoluteFile().toString();
    return null;
  }
  
  */
  /*
  //public String selectInputPro(String prompt, String filestring) { return selectFileImplPro(prompt, filestring, FileDialog.LOAD);}
  protected String selectFileImplPro(final String prompt, final String filestring, final int mode) {
    this.checkParentFrame();
  
    try {
  
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          FileDialog fileDialog = new FileDialog(parentFrame, prompt, mode);
          fileDialog.setFile(filestring);
          fileDialog.setVisible(true);
          String directory = fileDialog.getDirectory();
          String filename = fileDialog.getFile();
          selectedFile = (filename == null) ? null : new File(directory, filename);
        }
      } 
      );
      return (selectedFile == null) ? null : selectedFile.getAbsolutePath();
    } 
    catch (Exception e) {
      addMessage("ERROR in select");  
      e.printStackTrace();  
      return null;
    }
  } */
  /*
  public String selectMyInputPro(String prompt, String filestring) { 
         JFileChooser chooser = new JFileChooser();//creation dun nouveau filechosser
         chooser.setApproveButtonText("Open"); //intitule du bouton
         int returnVal =  chooser.showOpenDialog(null); //affiche la boite de dialogue
        if (returnVal == JFileChooser.APPROVE_OPTION)   return chooser.getSelectedFile().getAbsoluteFile().toString();
         return null;
  }*/
}

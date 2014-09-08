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
  // Model.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

public class ModelManager {
 
  private GRNBoolModel p;
  
  ModelManager(GRNBoolModel p) {
    this.p = p;   
  } 

  Model []MyModels=null; //List of all versions
  Model MyModel=null; //Current Model-> version Loaded
  
  
  String lastModelLoaded=""; //Save the name of the  last Model / Rules Loaded
  String [] ModelLoad; //To have the Model in String format for Search Function

  //Add a new model in the list
  public void addModel(Model model){
    Model[] TempMyModels=new Model[MyModels.length+1];
    for(int i=0;i<MyModels.length;i++) TempMyModels[i]=MyModels[i];
    TempMyModels[MyModels.length]=model;
    MyModels=TempMyModels;
    MyModel=model; //Current Model
  }
  //Delete a model from the list
  public void delModel(Model model){
     Model []TempMyModels=new Model[MyModels.length-1];
     int nb=0;
     for(int i=0;i<MyModels.length;i++)  if(MyModels[i]!=model)  TempMyModels[nb++]=MyModels[i];
     MyModels=TempMyModels;
     MyModel=MyModels[0];
     MyModel.ActiveDomain=MyModel.getDomain(0);
  }
}

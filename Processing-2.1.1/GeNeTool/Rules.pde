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


 //First we Delet all the Old things
 void deleteModel(){
  Genes=new ArrayList();
  Domains=new ArrayList();
  MyModels=null;
  MyModel=null;
  Regions=new ArrayList();
  lastModelLoaded="";
  MaxTime=0;
}


public void SelectAModelFile(File selection){
  if (selection != null) { 
     String Name=selection.getAbsolutePath();
     try { ReadRules(Name); } catch (Exception e){addMessage("There is not valid "+Name+" files in the application"); deleteModel();}
  } 
  
  
}
//Read the Big XML element
public void ReadRules(String Name){
  println("Read " + Name);
  deleteModel();
  lastModelLoaded=Name;
 
  XML element=loadXML(Name);
  
  if(element.hasAttribute("MaxTime")) MaxTime=parseInt(element.getString("MaxTime"));
  
  XML[]version=element.getChildren();
  int nbVersion=0;  
  for(int j=0;j<version.length;j++)   if(version[j].getName().equals("Regions")) ReadRegion(version[j]);  
  for(int j=0;j<version.length;j++)   if(version[j].getName().equals("Domains")) ReadDomains(version[j]);
  for(int j=0;j<version.length;j++)   if(version[j].getName().equals("Expression")) ReadExpression(version[j]);
  for(int j=0;j<version.length;j++)   if(version[j].getName().equals("Rules")) {  ReadRule(version[j]);  Genes=sortGene(Genes); }
  for(int j=0;j<version.length;j++)   if(version[j].getName().equals("Compare")) ReadCompare(version[j]);
  for(int j=0;j<version.length;j++)   if(version[j].getName().equals("Config")) ReadConfig(version[j]);
  for(int j=0;j<version.length;j++)   if(version[j].getName().equals("Version")) nbVersion++;
 
  if(nbVersion>0){
     active(2,1); 
     addMessage("Found " + nbVersion  +" versions");
     MyModels=new Model[nbVersion]; // the number of version
     int numVersion=0;
     for(int i=0;i<version.length;i++)
           if(version[i].getName().equals("Version")){  //For Each Version
                int number=i; if(version[i].hasAttribute("number"))  number=parseInt(version[i].getString("number"));
                String date=now();  if(version[i].hasAttribute("date"))  date=version[i].getString("date");
                Model model=new Model(number,date);
                if(version[i].hasAttribute("Name"))  model.Name=version[i].getString("Name");
                if(version[i].hasAttribute("lock"))  model.lock=parseBool(version[i].getString("lock"));
                if(version[i].hasAttribute("step"))  { model.step=parseInt(version[i].getString("step"));model.reset();};
          
                XML[]children=version[i].getChildren();
                for(int j=0;j<children.length;j++) //Second we load the data we had the domains
                      if(children[j].getName().equals("Domains")) model.addDomain(readModelDomain(model,children[j])); 
              
                MyModels[numVersion]=model;
                
                if(numVersion==nbVersion-1){ //Take the last Version
                    MyModel=model; 
                    MyModel.ActiveDomain=MyModel.getDomain(0);
                }
                numVersion++;
          }
   }
   computeData();
   GeneDef=null;
   
}


public void ReadCompare(XML element){
  if(element.hasAttribute("activeCompareRows")) activeCompareRows=element.getString("activeCompareRows");
  if(element.hasAttribute("activeCompareLigne")) activeCompareLigne=element.getString("activeCompareLigne");
  if(element.hasAttribute("activeCompareThird")) activeCompareThird=element.getString("activeCompareThird");
  if(element.hasAttribute("timeCompareBegin")) timeCompareBegin=parseInt(element.getString("timeCompareBegin"));
  if(element.hasAttribute("timeCompareEnd")) timeCompareEnd=parseInt(element.getString("timeCompareEnd"));
  if(element.hasAttribute("spaceCompareLine")) spaceCompareLine=parseInt(element.getString("spaceCompareLine"));
  if(element.hasAttribute("modeCompareBox")) modeCompareBox=parseInt(element.getString("modeCompareBox"));
  if(element.hasAttribute("modeCompareData")) modeCompareData=parseInt(element.getString("modeCompareData"));
  if(element.hasAttribute("compareDataVersion")) compareDataVersion=parseInt(element.getString("compareDataVersion"));
  
     
  XML[]children=element.getChildren();
  if(children.length>1 && compareOthersDomains==null) {
    compareOthersDomains=new String[Domains.size()]; 
    OrderCompareOthersDomains=new String[Domains.size()]; 
    for(int d=0;d<Domains.size();d++){  Domain dom=getDomain(d);compareOthersDomains[d]=dom.Name;OrderCompareOthersDomains[d]=dom.Name;}
 }  
 
  for(int j=0;j<children.length;j++){
    String d="";int i=-1;
    if(children[j].getName().equals("compareOthersDomains")) {
         if(children[j].hasAttribute("Domain")) d=children[j].getString("Domain");
         if(children[j].hasAttribute("i")) i=parseInt(children[j].getString("i"));
    }
    if(i>=0 && !d.equals("")) compareOthersDomains[i]=d;
  }
}



public void ReadRegion(XML element){
  XML[]children=element.getChildren();
  for(int j=0;j<children.length;j++){
    if(children[j].getName().equals("Tree")) ReadTree(children[j]);
  }
}

public void ReadTree(XML element){
   if(element.hasAttribute("Name") && element.hasAttribute("Number") && element.hasAttribute("Start") && element.hasAttribute("End") )  
       addRegion(element.getString("Name"),parseInt(element.getString("Number")),parseInt(element.getString("Start")),parseInt(element.getString("End")));
}
  

//Read Data Expression 
public void ReadExpression(XML element){
  if(element.hasAttribute("timeUnit")) timeUnit=element.getString("timeUnit");
  
   XML[]children=element.getChildren();
  for(int j=0;j<children.length;j++){
    if(children[j].getName().equals("Gene")) this.ReadDataGene(children[j]);
  }
}
//Read Data Expression for each Gene
public void ReadDataGene(XML element){
  if(element.hasAttribute("name"))  {
    Gene g=null;
    String name=element.getString("name");
    g=getGene(name);
    if(g==null) { g=new Gene(name); addGene(g);  }  //Create a new Gene
    XML[]children=element.getChildren();
    for(int j=0;j<children.length;j++)
        if(children[j].getName().equals("Region"))
            this.ReadDataGeneRegion(g,children[j]);
  }
}

//Read Data Expression for each Gene and each Region
public void ReadDataGeneRegion(Gene g,XML element){
  if(element.hasAttribute("name"))  {
      String name=element.getString("name");
      Region region=getRegion(name);
      if(region==null) region=addRegion(name);
      
      if(element.hasAttribute("value"))  {
        String value=element.getString("value");
        String[] values = split(value,';');
        for(int j=0;j<values.length;j++){
            String []tv=split(values[j],':');
            if(tv.length==2) g.Expression[region.number][parseInt(tv[0])]=parseInt(tv[1]);
        }
      } 
  }
  
}


    
//Read each type of Rule
public void ReadRule(XML element){
  if(element.hasAttribute("type"))  addMessage("Read " +element.getString("type"));

  XML[]children=element.getChildren();
  for(int j=0;j<children.length;j++){
    if(children[j].getName().equals("Rule")) this.ReadGene(children[j]);
  }
}
//Read each Gene definition
public void ReadGene(XML element){
  Gene g=null;
  if(element.hasAttribute("gene"))  {
    String name=element.getString("gene");
    g=getGene(name);
    if(g==null) { g=new Gene(name); addGene(g); }  //Create a new Gene
  }
  boolean blueLogic=false; boolean dft=false;
  if(element.hasAttribute("isGene")) g.isGene=parseBool(element.getString("isGene"));
  if(element.hasAttribute("isUbiquitous")) g.isUbiquitous=parseBool(element.getString("isUbiquitous"));
  if(element.hasAttribute("isBlue")) g.isUnknown=parseBool(element.getString("isBlue")); //TO DELETE AFTER SAVE ALL
  if(element.hasAttribute("isUnknown")) g.isUnknown=parseBool(element.getString("isUnknown"));
  if(element.hasAttribute("isMaternal")) g.isMaternal=parseBool(element.getString("isMaternal"));
  if(element.hasAttribute("isBlueModul")) blueLogic=parseBool(element.getString("isBlueModul"));
  if(element.hasAttribute("isDefault")) dft=parseBool(element.getString("isDefault"));
 
       
   
  String rule="";
  String id="";
  String thens="";
  String elses="";
  if(element.hasAttribute("id"))   id=element.getString("id");
  if(element.hasAttribute("if"))    rule=element.getString("if");
  if(element.hasAttribute("then"))  thens=element.getString("then");
  if(element.hasAttribute("else"))  elses=element.getString("else");
  
  addMessage("Gene " + g.Name + "   -> Add Logic " + id + " := "+ rule);
  
  Logic lo=g.getLogic(id); 
  if(lo!=null){ // If the Logic is already previously create and empty
     int num=g.getNumLogic(id);
     g.Objects[num]=DecodeObjects(g,rule);
     MetaLogic Mlogi=(MetaLogic)g.Logics[num];
     Mlogi.isBlue=blueLogic;
     Mlogi.dft=dft;
     Mlogi.logi=DecodeRule(g.getObjets(num));
     Mlogi.Then=thens;Mlogi.Else=elses;
     if(Mlogi.logi==null) Mlogi.valid=false; else Mlogi.valid=true;
     addMessage("This Logic is already crate " + id  + " at " + num);
  }  else g.addLogic(id,rule,thens,elses,blueLogic,dft);
    
 

}
//Decode a Rule
public Logic DecodeRule(ArrayList ListObjets){ 
    if(ListObjets==null) return new LogicEmpty();
    Logic lo=DecodeBracket(ListObjets);
    return lo;
}

//Decode A bracket
public Logic DecodeBracket(ArrayList ListObjets){
  if(ListObjets==null) return new LogicEmpty();
   //print("Decode :"); for(int j=0;j<ListObjets.size();j++) print("."+(Objet)ListObjets.get(j)); addMessage("");
  int numberBracketOpen=0;
  ArrayList ObjetInBracket=new ArrayList();
  ArrayList Elements=new ArrayList();
  //Separte the Two Logic
  for(int i=0;i<ListObjets.size();i++){
    Objet obj=(Objet)ListObjets.get(i);
    if(obj==null) return null;
    Boolean AddObjet=true;
    if(obj.isString()){
       String Brack=obj.getString();
       if(Brack.equals("[")) {
           if(numberBracketOpen==0) AddObjet=false;  
           numberBracketOpen++; 
           // addMessage("Found a Bracket "+Brack + " (number open="+numberBracketOpen+")");
        }
       if(Brack.equals("]")) {  
           numberBracketOpen--; if(numberBracketOpen<0) {addMessage("ERROR More ]  then ["); return null; }//More Close than open
           // addMessage("Found a Bracket "+Brack + " (number open="+numberBracketOpen+")");
            if(numberBracketOpen==0 && ObjetInBracket.size()>0) {
                Logic logi=DecodeBracket(ObjetInBracket);
                if(logi!=null)   Elements.add(new Objet(logi));
                 else { addMessage("ERROR  Logic null in DecodeBracket "); return null;}
                 ObjetInBracket=new ArrayList();
                 AddObjet=false;
           }
           
       }
      
     } 
     if(AddObjet){
       if(numberBracketOpen==0){
        // addMessage("Found an Element "+obj);
             Elements.add(obj);      
        }   else    {
            //  addMessage("Add " + obj+ " in Bracket" );
              ObjetInBracket.add(obj);
        }
     }
 }
  
 if(numberBracketOpen>0) { addMessage("ERROR More  [ then ]"); return null; }//More Open than Close
  
  //Decode the elements
  if(Elements.size()==0) return new LogicEmpty();
  //print("Return Decode "+Elements.size()+" Elemets :");   for(int j=0;j<Elements.size();j++) print("."+(Objet)Elements.get(j)); addMessage("");
  return DecodeOperator(Elements);
}

//Decode A logic Between Bracket
public Logic DecodeOperator(ArrayList ListObjets){
  if(ListObjets==null) return new LogicEmpty();
    for(int j=0;j<ListObjets.size();j++)  if((Objet)ListObjets.get(j)==null) return null;
  //print("Start Decode Operator "+ListObjets.size()+" Elemets :");   for(int j=0;j<ListObjets.size();j++) print("."+(Objet)ListObjets.get(j)); addMessage("");
  
  Logic logi=null;
  ArrayList TempLogics=new ArrayList();
  ArrayList TempOperatos=new ArrayList();
  ArrayList TempObjets=new ArrayList();
  
  int numberBracketOpen=0;
  //Separte the Two Logic
  for(int i=0;i<ListObjets.size();i++){
    Objet obj=(Objet)ListObjets.get(i);
    if(obj==null) return null;
    if(numberBracketOpen==0 && obj.isOperator()){
         Operator op=obj.getOperator();
         if(op.comp==2) { //Operator in Category 2
             //addMessage("Found an Operator binarie");
             Logic TempLogi=DecodeLogic(TempObjets);
             if(TempLogi==null){ addMessage("ERROR Decode Objetcs TempLogi==null at 1"); return null;}
             TempLogics.add(TempLogi);
             TempOperatos.add(op);
             TempObjets=new ArrayList();
         }
         else TempObjets.add(obj);
    } else TempObjets.add(obj);
  }
  Logic TempLogi=DecodeLogic(TempObjets);
  if(TempLogi==null) { addMessage("ERROR Decode Objetcs TempLogi==null at 2 ");  return null;}
  TempLogics.add(TempLogi);
  
  //Recombine Combine the Two Logic
  logi=(Logic)TempLogics.get(0); //First Logic
  for(int i=0;i<TempOperatos.size();i++)
    logi=new LogicTwo(logi,(Operator)TempOperatos.get(i),(Logic)TempLogics.get(i+1));
  //addMessage("Return Logi " + logi);
  return logi;
}

//Decode a simple logic
public Logic DecodeLogic(ArrayList Objets){
  if(Objets.size()==0) return null;
  int nbOj=Objets.size()-1;
 // addMessage("Try to find an IN");
  ArrayList FirstObjets=new  ArrayList();
  ArrayList SecondObjets=new  ArrayList();
  Operator IN=null;
  for(int i=0;i<Objets.size();i++){
     Objet obj=(Objet)Objets.get(i);
     if(IN!=null) SecondObjets.add(obj);
     else{
      if(obj.isOperator()){
         Operator op=obj.getOperator();
         if(op.getName().equals("IN")) IN=op;
          else FirstObjets.add(obj);    
      } else FirstObjets.add(obj);
    }
  }
  if(IN!=null) {
    //addMessage("Found an IN");
    if(FirstObjets.size()==0){
        //addMessage("With nothing Before");
        Logic TempLogi=DecodeINLogic(SecondObjets);
        if(TempLogi==null) return null;
        return new LogicOne(IN,TempLogi);
    }
    else {
        //addMessage("With things Before");
        Logic TempLogiFirst=DecodeINLogic(FirstObjets);
        if(TempLogiFirst==null) return null;
        Logic TempLogiSecond= DecodeINLogic(SecondObjets);
        if(TempLogiSecond==null) return null;
      return new LogicTwo(TempLogiFirst,IN,TempLogiSecond);
    }
  }
  return DecodeINLogic(FirstObjets);
}


//Return a Logic between IN
public Logic DecodeINLogic(ArrayList Objets){
  Logic logi=null;
  if(Objets==null || Objets.size()==0) return null;
  Objet obj=(Objet)Objets.get(Objets.size()-1);
 // addMessage("Read this Object " + obj);
  if(obj.isDomain()) {  Domain dom=obj.getDomain(); logi=new LogicDomain(dom); }//DOMAIN
  else if(obj.isMetaLogic())  logi=obj.getMetaLogic(); //Meta Logic 
  else if(obj.isLogicLogic())  logi=obj.getLogicLogic(); //Logic Logic 
  else if(obj.isLogicGene()) logi=obj.getLogicGene(); //Logic Gene
  else if(obj.isLogicTwo()) logi=obj.getLogicTwo(); //Logic Two
  else if(obj.isLogicOne()) logi=obj.getLogicOne(); //Logic One
  else if(obj.isOperator()){ Operator op=obj.getOperator(); if(op.comp==6)  logi=new LogicOperator(op); } //Logic One
  if(logi!=null){
      //print("Found the logic " + logi);
      int nbOj=Objets.size()-2;
      while(nbOj>=0){ 
         obj=(Objet)Objets.get(nbOj);
         if(!obj.isOperator())  return null;
         logi=new LogicOne(obj.getOperator(),logi);
         nbOj--;
      }
      //addMessage("-> is " + logi);
  }
  else addMessage("ERROR this Object "+obj + " is not a logic :"+obj.getClass().getName());
  return logi;
}
    
 

//Read the Model for each domains
public ModelDomain readModelDomain(Model model,XML element){
    ModelDomain md=null; 
    if(element.hasAttribute("Name"))  {
        String Name=element.getString("Name");
        md=model.getDomain(Name);
        if(md==null){
            Domain dom=getDomain(Name); //The Domain was created before the model even exist !
            if(dom!=null)  md=new ModelDomain(dom);
        }
    }
    if(md!=null){
       XML[]children=element.getChildren();
       for(int j=0;j<children.length;j++)
          if(children[j].getName().equals("Step")) readModelStep(model,children[j],md);
   } else addMessage("We don't have this domain " + element.getString("Name"));
   return md;
}

//compute the step for each domain
public void readModelStep(Model model, XML element,ModelDomain md){
  int s=-1; if(element.hasAttribute("Number"))  s=element.getInt("Number");
  int hpf=-1; if(element.hasAttribute("hpf"))  hpf=element.getInt("hpf");
  int d=-1; if(element.hasAttribute("Draw"))  d=element.getInt("Draw");
  
  if(s>=0){
      model.step=max(model.step,s);
      model.ActualStep=model.step;
      model.Correspons[s]=hpf;
      XML[]children=element.getChildren();
      for(int j=0;j<children.length;j++)
        if(children[j].getName().equals("Gene")) 
              readModelGene(children[j],md,s);
  } else addMessage("We didn't fint step number " );
   
}

 
                  
public void readModelGene(XML element,ModelDomain md,int s){
  Gene g=null;
  if(element.hasAttribute("Name"))  g=getGene(element.getString("Name"));
  if(g!=null){
      boolean v=true; boolean blueState=false; //It's true by default
      boolean Manual=false; 
      if(element.hasAttribute("Manual")){Manual=true;  v=parseBool(element.getString("Manual"));}
      if(element.hasAttribute("BlueState"))blueState=parseBool(element.getString("BlueState"));
      int i=getNumGene(g);
      md.GenesStates[i][s]=v;
      md.Manual[i][s]=Manual;
      md.isBlueState[i][s]=blueState;
     // addMessage("Add Genes "+element.getString("Name") + " to domain " +dom.Name);
  } else addMessage("This gene doesn't exist " + element.getString("Name"));
   
}













//Read each type of Domain
public void ReadDomains(XML element){
  XML[]children=element.getChildren();
  for(int j=0;j<children.length;j++){
    if(children[j].getName().equals("Domain")) ReadDomain(children[j]);
  }
}


//Read each single Domain
public void ReadDomain(XML element){
  String Name="";
  if(element.hasAttribute("name"))  Name=element.getString("name");
  Domain dom=getDomain(Name);
  if(dom==null) {
      dom=new Domain(Name);addDomain(dom);
      if(verbose>=2)  addMessage("Create Domain " +Name);
  }  else if(verbose>=2)  addMessage("Found Domain " +dom.Name);
  
  if(element.hasAttribute("tree")) {
       String Tree=element.getString("tree");
       String [] EletTree=split(Tree,";");
       for(int i=0;i<EletTree.length;i++)
           dom.addTree(trim(EletTree[i]));
  }


   if(element.hasAttribute("def"))  {
      String rule=trim(element.getString("def"));
      String []Eq=split(rule,',');  //Split all the different rule
      dom.DefObjets=new Objet[Eq.length][2];
      for(int i=0;i<Eq.length;i++)
          dom.DefObjets[i]=DecodeObjetsDomain(Eq[i]);

   }
   
}


//X CC Y, X NCC 1-X-Y
public Objet[] DecodeObjetsDomain(String rule){
    
    
    Objet[] ListObjets=new Objet[2]; for(int i=0;i<2;i++) ListObjets[i]=null;
    
    rule=trimPlus(rule);
    int space=rule.indexOf(" "); //Loof fo the first space which is the separatore
    String []word=new String[2];   word[0]=trim(rule.substring(0,space)); word[1]=trim(rule.substring(space+1));
    //First Operator
    Operator op=null; 
    for(int l=0;l<NbLogicOperator;l++)
      if(LogicOperator[l].is(word[0]) && (LogicOperator[l].Name.equals("CC") || LogicOperator[l].Name.equals("NCC_n") || LogicOperator[l].Name.equals("NCC_d"))) {
            String [] hhh=split(word[0].substring(LogicOperator[l].Name.length()+1),'-');  //get the Hours 
            op=new Operator(LogicOperator[l]);
            op.hmin=parseInt(hhh[0]);
            op.hmax=parseInt(hhh[1]);
      }
    if(op==null) { addMessage("ERROR need an Operator (CC || NCC_n || NCC_d) in First " + word[0]); return null;}
    ListObjets[0]=new Objet(op);
    
    //Second Domain
    String [] Elt=split(word[1],':');
    if(Elt[0].equals("G"))  { addMessage("ERROR not need an Gene in Third " + word[1]); return null;}
    if(Elt.length!=2 || !Elt[0].equals("D"))  { addMessage("ERROR need an Domaine in Third " + word[1]); return null;}
    Elt[1]=replaceString(Elt[1],""+char(35)," ");
    Domain domLast=getDomain(Elt[1]); //Found the last domain
    if(equal(Elt[1],"R")) domLast=GenericDomain;   //If the domain is "R" it's a generic domain
    if(domLast==null){domLast=new Domain(Elt[1]);   addDomain(domLast); println("Create Domain " + domLast.Name);  } //Create a new domain
    ListObjets[1]=new Objet(domLast);

    return ListObjets;
}




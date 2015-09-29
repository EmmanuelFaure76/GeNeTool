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

import java.io.IOException;
import java.util.ArrayList;

import processing.data.XML;

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // ReadRules.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

public class RuleReader {

  private GRNBoolModel p;
  
  RuleReader(GRNBoolModel p) {
    this.p = p;   
  }
 
  //First we Delet all the Old things
   void deleteModel(){
    p.gm.Genes=new ArrayList();
    p.gm.nbGene = 0;
    p.dm.Domains=new ArrayList();
    p.dm.nbDomain = 0;
    p.lm.MyModels=null;
    p.lm.MyModel=null;
    p.rm.Regions=new ArrayList();
    p.lm.lastModelLoaded="";
    p.rm.MaxTime=0;
  }
  
  
  //Read the Big XML element
  public void ReadRules(String Name){
    deleteModel();
    p.lm.lastModelLoaded=Name;
   
    XML element=p.loadXML(Name);
    
    if(element.hasAttribute("MaxTime")) p.rm.MaxTime=p.parseInt(element.getString("MaxTime"));
    
    XML[]version=element.getChildren();
    int nbVersion=0;

    try {
      for (int j = 0; j < version.length; j++) if (version[j].getName().equals("Regions")) ReadRegion(version[j]);
      for (int j = 0; j < version.length; j++) if (version[j].getName().equals("Domains")) ReadDomains(version[j]);
      for (int j = 0; j < version.length; j++) {
        if (version[j].getName().equals("Expression")) ReadExpression(version[j]);
      }
      for (int j = 0; j < version.length; j++) {
        if (version[j].getName().equals("Rules")) {
          ReadRule(version[j]);
          p.gm.Genes = p.gm.sortGene(p.gm.Genes);
        }
      }
      for (int j = 0; j < version.length; j++) if (version[j].getName().equals("Compare")) ReadCompare(version[j]);
      for (int j = 0; j < version.length; j++) if (version[j].getName().equals("Config")) p.sm.ReadConfig(version[j]);
      for (int j = 0; j < version.length; j++) if (version[j].getName().equals("Version")) nbVersion++;
    }
    catch (ModelLoadingException mle) {
      p.eh.alert("Error while loading the model XML:\n" + mle.getMessage());
      mle.printStackTrace();

      // Empty the partially loaded model
      deleteModel();
      return;
    }

    if(nbVersion>0){
       p.mm.active(2,1); 
       p.mm.addMessage("Found " + nbVersion  +" versions");
       p.lm.MyModels=new Model[nbVersion]; // the number of version
       int numVersion=0;
       for(int i=0;i<version.length;i++)
             if(version[i].getName().equals("Version")){  //For Each Version
                  int number=i; if(version[i].hasAttribute("number"))  number=p.parseInt(version[i].getString("number"));
                  String date=p.uf.now();  if(version[i].hasAttribute("date"))  date=version[i].getString("date");
                  Model model=new Model(p, number,date);
                  if(version[i].hasAttribute("Name"))  model.Name=version[i].getString("Name");
                  if(version[i].hasAttribute("lock"))  model.lock=p.uf.parseBool(version[i].getString("lock"));
                  if(version[i].hasAttribute("step"))  { model.step=p.parseInt(version[i].getString("step"));model.reset();};
            
                  XML[]children=version[i].getChildren();
                  for(int j=0;j<children.length;j++) //Second we load the data we had the domains
                        if(children[j].getName().equals("Domains")) model.addDomain(readModelDomain(model,children[j])); 
                
                  p.lm.MyModels[numVersion]=model;
                  
                  if(numVersion==nbVersion-1){ //Take the last Version
                      p.lm.MyModel=model; 
                      p.lm.MyModel.ActiveDomain=p.lm.MyModel.getDomain(0);
                  }
                  numVersion++;
            }
     }
     p.dm.computeData();
     p.eh.GeneDef=null;
     
  }
  
  
  public void ReadCompare(XML element){
    if(element.hasAttribute("activeCompareRows")) p.cmm.activeCompareRows=element.getString("activeCompareRows");
    if(element.hasAttribute("activeCompareLigne")) p.cmm.activeCompareLigne=element.getString("activeCompareLigne");
    if(element.hasAttribute("activeCompareThird")) p.cmm.activeCompareThird=element.getString("activeCompareThird");
    if(element.hasAttribute("timeCompareBegin")) p.cmm.timeCompareBegin=p.parseInt(element.getString("timeCompareBegin"));
    if(element.hasAttribute("timeCompareEnd")) p.cmm.timeCompareEnd=p.parseInt(element.getString("timeCompareEnd"));
    if(element.hasAttribute("spaceCompareLine")) p.cmm.spaceCompareLine=p.parseInt(element.getString("spaceCompareLine"));
    if(element.hasAttribute("modeCompareBox")) p.cmm.modeCompareBox=p.parseInt(element.getString("modeCompareBox"));
    if(element.hasAttribute("modeCompareData")) p.cmm.modeCompareData=p.parseInt(element.getString("modeCompareData"));
    if(element.hasAttribute("compareDataVersion")) p.cmm.compareDataVersion=p.parseInt(element.getString("compareDataVersion"));
    
       
    XML[]children=element.getChildren();
    if(children.length>1 && p.cmm.compareOthersDomains==null) {
      p.cmm.compareOthersDomains=new String[p.dm.Domains.size()]; 
      p.cmm.OrderCompareOthersDomains=new String[p.dm.Domains.size()]; 
      for(int d=0;d<p.dm.Domains.size();d++){  Domain dom=p.dm.getDomain(d);p.cmm.compareOthersDomains[d]=dom.Name;p.cmm.OrderCompareOthersDomains[d]=dom.Name;}
   }  
   
    for(int j=0;j<children.length;j++){
      String d="";int i=-1;
      if(children[j].getName().equals("compareOthersDomains")) {
           if(children[j].hasAttribute("Domain")) d=children[j].getString("Domain");
           if(children[j].hasAttribute("i")) i=p.parseInt(children[j].getString("i"));
      }
      if(i>=0 && !d.equals("")) p.cmm.compareOthersDomains[i]=d;
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
         p.rm.addRegion(element.getString("Name"),p.parseInt(element.getString("Number")),p.parseInt(element.getString("Start")),p.parseInt(element.getString("End")));
  }
    
  
  //Read Data Expression 
  public void ReadExpression(XML element) throws ModelLoadingException {
    if(element.hasAttribute("timeUnit")) p.rm.timeUnit=element.getString("timeUnit");
    
     XML[]children=element.getChildren();
    for(int j=0;j<children.length;j++){
      if(children[j].getName().equals("Gene")) this.ReadDataGene(children[j]);
    }
  }

  //Read Data Expression for each Gene
  public void ReadDataGene(XML element) throws ModelLoadingException {
    String name = null;

    if (element.hasAttribute("name")) {
      name = element.getString("name");
    }
    else {
      String message = "Gene-element with missing \'name\' tag";
      throw new ModelLoadingException(message);
    }

    String duplicate = p.gm.getDuplicateGeneName(name);

    if (duplicate != null) {
      String message = "The gene \'" + name + "\' already exist as gene \'" + duplicate + "\'";
      throw new ModelLoadingException(message);
    }

    Gene g = new Gene(p, name);
    p.gm.addGene(g);   //Create a new Gene

    XML[] children = element.getChildren();
    for (int j = 0; j < children.length; j++) {
      if (children[j].getName().equals("Region")) {
        this.ReadDataGeneRegion(g, children[j]);
      }
    }
  }
  
  //Read Data Expression for each Gene and each Region
  public void ReadDataGeneRegion(Gene g,XML element){
    if(element.hasAttribute("name"))  {
        String name=element.getString("name");
        Region region=p.rm.getRegion(name);
        if(region==null) region=p.rm.addRegion(name);
        
        if(element.hasAttribute("value"))  {
          String value=element.getString("value");
          String[] values = p.split(value,';');
          for(int j=0;j<values.length;j++){
              String []tv=p.split(values[j],':');
              if(tv.length==2) g.Expression[region.number][p.parseInt(tv[0])]=p.parseInt(tv[1]);
          }
        } 
    }
  }
  
  
      
  //Read each type of Rule
  public void ReadRule(XML element) throws ModelLoadingException {
    if(element.hasAttribute("type"))  p.mm.addMessage("Read " +element.getString("type"));
  
    XML[]children=element.getChildren();
    for(int j=0;j<children.length;j++){
      if(children[j].getName().equals("Rule")) this.ReadGene(children[j]);
    }
  }

  //Read each Gene definition
  public void ReadGene(XML element) throws ModelLoadingException {
    String geneName = element.getString("gene");
    Gene g = p.gm.getGene(geneName);

    if (g == null) {
      String message = "Gene \'" + geneName + "\' does not exist.";
      throw new ModelLoadingException(message);
    }

    boolean blueLogic=false; boolean dft=false;
    if(element.hasAttribute("isGene")) g.isGene=p.uf.parseBool(element.getString("isGene"));
    if(element.hasAttribute("isUbiquitous")) g.isUbiquitous=p.uf.parseBool(element.getString("isUbiquitous"));
    if(element.hasAttribute("isBlue")) g.isUnknown=p.uf.parseBool(element.getString("isBlue")); //TO DELETE AFTER SAVE ALL
    if(element.hasAttribute("isUnknown")) g.isUnknown=p.uf.parseBool(element.getString("isUnknown"));
    if(element.hasAttribute("isMaternal")) g.isMaternal=p.uf.parseBool(element.getString("isMaternal"));
    if(element.hasAttribute("isBlueModul")) blueLogic=p.uf.parseBool(element.getString("isBlueModul"));
    if(element.hasAttribute("isDefault")) dft=p.uf.parseBool(element.getString("isDefault"));

     
    String rule="";
    String id="";
    String thens="";
    String elses="";
    if(element.hasAttribute("id"))   id=element.getString("id");
    if(element.hasAttribute("if"))    rule=element.getString("if");
    if(element.hasAttribute("then"))  thens=element.getString("then");
    if(element.hasAttribute("else"))  elses=element.getString("else");
    
    p.mm.addMessage("Gene " + g.Name + "   -> Add Logic " + id + " := "+ rule);
    
    Logic lo=g.getLogic(id); 
    if(lo!=null){ // If the Logic is already previously create and empty
       int num=g.getNumLogic(id);
       g.Objects[num]=p.om.DecodeObjects(g,rule);
       MetaLogic Mlogi=(MetaLogic)g.Logics[num];
       Mlogi.isBlue=blueLogic;
       Mlogi.dft=dft;
       Mlogi.logi=DecodeRule(g.getObjets(num));
       Mlogi.Then=thens;Mlogi.Else=elses;
       if(Mlogi.logi==null)
    	   Mlogi.valid=false;
       else
    	   Mlogi.valid=true;
       p.mm.addMessage("This Logic is already crate " + id  + " at " + num);
    } 
    else
    	g.addLogic(id,rule,thens,elses,blueLogic,dft);
      
   
  
  }
  //Decode a Rule
  public Logic DecodeRule(ArrayList ListObjets){ 
      if(ListObjets==null) return new LogicEmpty(p);
      Logic lo=DecodeBracket(ListObjets);
      return lo;
  }
  
  //Decode A bracket
  public Logic DecodeBracket(ArrayList ListObjets){
    if(ListObjets==null) return new LogicEmpty(p);
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
             numberBracketOpen--; if(numberBracketOpen<0) {p.mm.addMessage("ERROR More ]  then ["); return null; }//More Close than open
             // addMessage("Found a Bracket "+Brack + " (number open="+numberBracketOpen+")");
              if(numberBracketOpen==0 && ObjetInBracket.size()>0) {
                  Logic logi=DecodeBracket(ObjetInBracket);
                  if(logi!=null)   Elements.add(new Objet(p, logi));
                   else { p.mm.addMessage("ERROR  Logic null in DecodeBracket "); return null;}
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
    
   if(numberBracketOpen>0) { p.mm.addMessage("ERROR More  [ then ]"); return null; }//More Open than Close
    
    //Decode the elements
    if(Elements.size()==0) return new LogicEmpty(p);
    //print("Return Decode "+Elements.size()+" Elemets :");   for(int j=0;j<Elements.size();j++) print("."+(Objet)Elements.get(j)); addMessage("");
    return DecodeOperator(Elements);
  }
  
  //Decode A logic Between Bracket
  public Logic DecodeOperator(ArrayList ListObjets){
    if(ListObjets==null) return new LogicEmpty(p);
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
               if(TempLogi==null){ p.mm.addMessage("ERROR Decode Objetcs TempLogi==null at 1"); return null;}
               TempLogics.add(TempLogi);
               TempOperatos.add(op);
               TempObjets=new ArrayList();
           }
           else TempObjets.add(obj);
      } else TempObjets.add(obj);
    }
    Logic TempLogi=DecodeLogic(TempObjets);
    if(TempLogi==null) { p.mm.addMessage("ERROR Decode Objetcs TempLogi==null at 2 ");  return null;}
    TempLogics.add(TempLogi);
    
    //Recombine Combine the Two Logic
    logi=(Logic)TempLogics.get(0); //First Logic
    for(int i=0;i<TempOperatos.size();i++)
      logi=new LogicTwo(p, logi,(Operator)TempOperatos.get(i),(Logic)TempLogics.get(i+1));
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
          return new LogicOne(p, IN,TempLogi);
      }
      else {
          //addMessage("With things Before");
          Logic TempLogiFirst=DecodeINLogic(FirstObjets);
          if(TempLogiFirst==null) return null;
          Logic TempLogiSecond= DecodeINLogic(SecondObjets);
          if(TempLogiSecond==null) return null;
        return new LogicTwo(p, TempLogiFirst,IN,TempLogiSecond);
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
    if(obj.isDomain()) {  Domain dom=obj.getDomain(); logi=new LogicDomain(p, dom); }//DOMAIN
    else if(obj.isMetaLogic())  logi=obj.getMetaLogic(); //Meta Logic 
    else if(obj.isLogicLogic())  logi=obj.getLogicLogic(); //Logic Logic 
    else if(obj.isLogicGene()) logi=obj.getLogicGene(); //Logic Gene
    else if(obj.isLogicTwo()) logi=obj.getLogicTwo(); //Logic Two
    else if(obj.isLogicOne()) logi=obj.getLogicOne(); //Logic One
    else if(obj.isOperator()){ Operator op=obj.getOperator(); if(op.comp==6)  logi=new LogicOperator(p, op); } //Logic One
    if(logi!=null){
        //print("Found the logic " + logi);
        int nbOj=Objets.size()-2;
        while(nbOj>=0){ 
           obj=(Objet)Objets.get(nbOj);
           if(!obj.isOperator())  return null;
           logi=new LogicOne(p, obj.getOperator(),logi);
           nbOj--;
        }
        //addMessage("-> is " + logi);
    }
    else p.mm.addMessage("ERROR this Object "+obj + " is not a logic :"+obj.getClass().getName());
    return logi;
  }
      
   
  
  //Read the Model for each domains
  public ModelDomain readModelDomain(Model model,XML element){
      ModelDomain md=null; 
      if(element.hasAttribute("Name"))  {
          String Name=element.getString("Name");
          md=model.getDomain(Name);
          if(md==null){
              Domain dom=p.dm.getDomain(Name); //The Domain was created before the model even exist !
              if(dom!=null)  md=new ModelDomain(p, dom);
          }
      }
      if(md!=null){
         XML[]children=element.getChildren();
         for(int j=0;j<children.length;j++)
            if(children[j].getName().equals("Step")) readModelStep(model, children[j], md);
     } else p.mm.addMessage("We don't have this domain " + element.getString("Name"));
     return md;
  }
  
  //compute the step for each domain
  public void readModelStep(Model model, XML element,ModelDomain md){
    int s=-1; if(element.hasAttribute("Number"))  s=element.getInt("Number");
    int hpf=-1; if(element.hasAttribute("hpf"))  hpf=element.getInt("hpf");
    int d=-1; if(element.hasAttribute("Draw"))  d=element.getInt("Draw");
    
    if(s>=0){
        model.step=p.max(model.step,s);
        model.ActualStep=model.step;
        model.Correspons[s]=hpf;
        XML[]children=element.getChildren();
        for(int j=0;j<children.length;j++)
          if(children[j].getName().equals("Gene")) 
                readModelGene(children[j], md, s);
    } else p.mm.addMessage("We didn't fint step number " );
     
  }
  
   
                    
  public void readModelGene(XML element,ModelDomain md,int s){
    Gene g=null;
    if(element.hasAttribute("Name"))  g=p.gm.getGene(element.getString("Name"));
    if(g!=null){
        boolean v=true; boolean blueState=false; //It's true by default
        boolean Manual=false; 
        if(element.hasAttribute("Manual")){Manual=true;  v=p.uf.parseBool(element.getString("Manual"));}
        if(element.hasAttribute("BlueState"))blueState=p.uf.parseBool(element.getString("BlueState"));
        int i=p.gm.getNumGene(g);
        md.GenesStates[i][s]=v;
        md.Manual[i][s]=Manual;
        md.isBlueState[i][s]=blueState;
       // addMessage("Add Genes "+element.getString("Name") + " to domain " +dom.Name);
    } else p.mm.addMessage("This gene doesn't exist " + element.getString("Name"));
     
  }
  
  //Read each type of Domain
  public void ReadDomains(XML element){
      String domains_format_field = "format";
      int domains_format = element.getInt(domains_format_field, 0);

      XML[]children=element.getChildren();
    for(int j=0;j<children.length;j++){
      if(children[j].getName().equals("Domain")) ReadDomain(children[j]);
    }
  }

    // Read a domain. Delegates to V1 or V2 implementation.
    public void ReadDomain(XML element) {
        int version = element.getInt("version", 1);
        if (version == 1) {
            ReadDomainV1(element);
        }
        else if (version == 2) {
            ReadDomainV2(element);
        }
        else {
            p.mm.addMessage("Skipping domain - found version \'" + version + "\'");
        }
    }

  //Read each single Domain
  public void ReadDomainV1(XML element){
    String Name="";
    if(element.hasAttribute("name"))  Name=element.getString("name");
    Domain dom=p.dm.getDomain(Name);
    if(dom==null) {
        dom=new Domain(p, Name);p.dm.addDomain(dom);
        if(p.verbose>=2)  p.mm.addMessage("Create Domain " +Name);
    }  else if(p.verbose>=2)  p.mm.addMessage("Found Domain " +dom.Name);
    
    if(element.hasAttribute("tree")) {
         String Tree=element.getString("tree");
         String [] EletTree=p.split(Tree,";");
         for(int i=0;i<EletTree.length;i++)
             dom.addTree(p.trim(EletTree[i]));
    }
  
  
     if(element.hasAttribute("def"))  {
        String rule=p.trim(element.getString("def"));
        String []Eq=p.split(rule,',');  //Split all the different rule
        dom.DefObjets=new Objet[Eq.length][2];
        for(int i=0;i<Eq.length;i++)
            dom.DefObjets[i]=DecodeObjetsDomain(Eq[i]);
     }
     
  }

  //Read each single Domain
  public void ReadDomainV2(XML element) {
    String name = element.getString("name");

    // Create the domain object
    Domain domain = findOrCreateDomain(name);

    // Read ancestors
    for (XML ancestor : element.getChild("ancestors").getChildren("ancestor")) {
      String ancestorName = ancestor.getString("name");
      domain.addTree(ancestorName);
    }

    // Read spatial relations
    XML definition = element.getChild("definition");
    ArrayList<Objet[]> relations = new ArrayList<Objet[]>();

    for (XML spatial : definition.getChildren("spatial")) {
      String type = spatial.getString("type");
      int min = spatial.getInt("min");
      int max = spatial.getInt("max");
      String targetDomain = spatial.getString("domain");
      relations.add(createSpatialRelation(type, min, max, targetDomain));
    }

    Objet[][] result = new Objet[relations.size()][2];
    for (int i = 0; i < relations.size(); i++) {
      result[i] = relations.get(i);
    }
    domain.DefObjets = result;
  }

  public Domain findOrCreateDomain(String name) {
    Domain dom = p.dm.getDomain(name);
    if (dom == null) {
      dom = new Domain(p, name);
      p.dm.addDomain(dom);
    }

    return dom;
  }

  public Objet[] createSpatialRelation(String type, int min, int max, String targetName) {
    // Refer to OperatorManager.java for logic operator indices in p.pm.LogicOperator[]
    Operator opCC = p.pm.LogicOperator[4];
    Operator opNCC_N = p.pm.LogicOperator[5];
    Operator opNCC_D = p.pm.LogicOperator[6];

    Operator op = null;
    if (type.equals("CC")) {
      op = opCC;
    } else if (type.equals("NCC_n")) {
      op = opNCC_N;
    }
    if (type.equals("NCC_d")) {
      op = opNCC_D;
    }

    op.hmin = min;
    op.hmax = max;

    Domain target = null;

    // Handle generic domain "R" special case
    if (targetName == "R") {
      target = p.dm.GenericDomain;
    } else {
      target = findOrCreateDomain(targetName);
    }

    Objet[] result = new Objet[2];
    result[0] = new Objet(p, op);
    result[1] = new Objet(p, target);

    return result;
  }

  //X CC Y, X NCC 1-X-Y
  public Objet[] DecodeObjetsDomain(String rule){
      
      
      Objet[] ListObjets=new Objet[2]; for(int i=0;i<2;i++) ListObjets[i]=null;
      
      rule=p.uf.trimPlus(rule);
      int space=rule.indexOf(" "); //Loof fo the first space which is the separatore
      String []word=new String[2];   word[0]=p.trim(rule.substring(0,space)); word[1]=p.trim(rule.substring(space+1));
      //First Operator
      Operator op=null; 
      for(int l=0;l<p.pm.NbLogicOperator;l++)
        if(p.pm.LogicOperator[l].is(word[0]) && (p.pm.LogicOperator[l].Name.equals("CC") || p.pm.LogicOperator[l].Name.equals("NCC_n") || p.pm.LogicOperator[l].Name.equals("NCC_d"))) {
              String [] hhh=p.split(word[0].substring(p.pm.LogicOperator[l].Name.length()+1),'-');  //get the Hours 
              op=new Operator(p.pm.LogicOperator[l]);
              op.hmin=p.parseInt(hhh[0]);
              op.hmax=p.parseInt(hhh[1]);
        }
      if(op==null) { p.mm.addMessage("ERROR need an Operator (CC || NCC_n || NCC_d) in First " + word[0]); return null;}
      ListObjets[0]=new Objet(p, op);
      
      //Second Domain
      String [] Elt=p.split(word[1],':');
      if(Elt[0].equals("G"))  { p.mm.addMessage("ERROR not need an Gene in Third " + word[1]); return null;}
      if(Elt.length!=2 || !Elt[0].equals("D"))  { p.mm.addMessage("ERROR need an Domaine in Third " + word[1]); return null;}
      Elt[1]=p.uf.replaceString(Elt[1],""+(char)35," ");
      Domain domLast=p.dm.getDomain(Elt[1]); //Found the last domain
      if(p.uf.equal(Elt[1],"R")) domLast=p.dm.GenericDomain;   //If the domain is "R" it's a generic domain
      if(domLast==null){domLast=new Domain(p, Elt[1]);   p.dm.addDomain(domLast); p.println("Create Domain " + domLast.Name);  } //Create a new domain
      ListObjets[1]=new Objet(p, domLast);
  
      return ListObjets;
  }

}

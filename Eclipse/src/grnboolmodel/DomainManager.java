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

import java.util.ArrayList;
import java.util.List;

/////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Domain.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////

public class DomainManager {
  
  private GRNBoolModel p;
  
  public ArrayList Domains=new ArrayList();
  Domain DomainDef=null; //Domain current show in the definition
  public int nbDomain;
  
  String CommentsRule="";
  Domain GenericDomain;
  int SizeDrawDomain=100;
  
  
  DomainManager(GRNBoolModel p) {
    this.p = p;
    GenericDomain=new Domain(p, "R");
  }  

  public void addDomain(Domain dom){
    p.mm.addMessage("Create domain " +dom.Name);
    Domains.add(dom); nbDomain=Domains.size();
    // GITHUB issue #2. checkEnableMenu() MAY create the first model AND add all existing domains
    // to it at that time. If this happens, do not add again. This is not a clean solution, but I cannot
    // fully anticipate at this time the effect of moving the model operation out of menu code.
    boolean modelInit = false;
    if(nbDomain==1) {
    	modelInit = p.mbm.checkEnablelMenu();
    }
   
    DomainDef=dom;
    if (p.lm.MyModels!=null) {
    	for(int i=0;i<p.lm.MyModels.length;i++) {
    		if (!modelInit || (i > 0)) { // think second test is belt and suspenders...
    		   p.lm.MyModels[i].addDomain(dom);
    		}
    	}
    }
  }

  /*
   * Trims leading and trailing whitespace. Whitespace blocks inside the string
   * are truncated to one character.
   */
  public String trimDomainName(String name) {
    return p.gm.trimGeneName(name);
  }

  /*
   * Checks if a domain with given name already exists in the model. The given
   * candidate domain name is checked against all existing gene names such that
   * all comparisons are done using normalized names.
   *
   * Example scenario:
   *
   * 1. Domain "A 1" is added to the model.
   * 2. getDuplicateDomainName is called with "A1" as the parameter.
   * 3. Since "A 1" (normalized to "A1") already exists, getDuplicateDomainName
   *    returns "A 1".
   *
   * Returns: null if no duplicate domain is found.
   *          name of duplicate domain if one exists.
   *
   */
  public String getDuplicateDomainName(String candidateName) {
    String normalizedName = UtilityFuncs.normKey(candidateName);

    for(int i = 0; i < Domains.size(); i++) {
      Domain domain = getDomain(i);
      String normalizedDomain = UtilityFuncs.normKey(domain.Name);
      if (normalizedName.equals(normalizedDomain)) {
        return domain.Name;
      }
    }

    return null;
  }

  public void delDomain(Domain dom){
    p.mm.addMessage("Delete domain " +dom.Name);
    if(dom==DomainDef) DomainDef=null;
    if(p.lm.MyModels!=null) for(int i=0;i<p.lm.MyModels.length;i++) p.lm.MyModels[i].delDomain(dom);
    ArrayList newDomains=new ArrayList();
    for(int d=0;d<nbDomain;d++){ Domain domm=getDomain(d);  if(domm!=dom) newDomains.add(domm);}
    Domains=newDomains;nbDomain=Domains.size();
    if(nbDomain==0)  p.mbm.checkEnablelMenu();
   
  }
  //Return a domain from the global list
  public Domain getDomain(String name){  for(int d=0;d<nbDomain;d++){ Domain dom=getDomain(d);  if(p.uf.equal(dom.Name,name)) return dom;}return null;}
  public Domain getDomain(int i){ return (Domain)Domains.get(i);}
  public void computeData(){ for(int d=0;d<nbDomain;d++) getDomain(d).computeData();}
  public void reComputeData(Gene g){ for(int d=0;d<nbDomain;d++) getDomain(d).reComputeData(g);}

  public List<Domain> getIncompleteDomains() {
    ArrayList<Domain> result = new ArrayList<Domain>();

    for (int i = 0; i < nbDomain; i++) {
      Domain domain = (Domain)Domains.get(i);

      if (domain.getIncompleteDefinitionIndices().size() > 0) {
        result.add(domain);
      }
    }
    return result;
  }
}

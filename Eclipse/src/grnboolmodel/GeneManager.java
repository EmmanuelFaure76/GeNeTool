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
import java.util.Iterator;

  /////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Gene.pde
  //
  /////////////////////////////////////////////////////////////////////////////////////////////


public class GeneManager {

  private GRNBoolModel p;
  
  GeneManager(GRNBoolModel p) {
    this.p = p;   
  } 
  

  public ArrayList Genes=new ArrayList();
  public int nbGene;
  
  int SizeDrawGene=100;

  // TODO needs review
  public void addGeneForUnitTest(Gene g) {
    p.mm.addMessage("Create Gene " + g.Name);
    Genes.add(g); nbGene=Genes.size();
    p.eh.GeneDef=g;
    for(int i=0;i<p.dm.nbDomain;i++) p.dm.getDomain(i).addGene(g);  //ADD TO DOMAIN
    if(p.lm.MyModels!=null) for(int i=0;i<p.lm.MyModels.length;i++)p.lm.MyModels[i].addGene(g); //ADD TO MODEL
  }

  public void addGene(Gene g){
    p.mm.addMessage("Create Gene " + g.Name);
    Genes.add(g); nbGene=Genes.size();
    if(nbGene==1)  p.mbm.checkEnablelMenu();
    p.eh.GeneDef=g;
    for(int i=0;i<p.dm.nbDomain;i++) p.dm.getDomain(i).addGene(g);  //ADD TO DOMAIN
    if(p.lm.MyModels!=null) for(int i=0;i<p.lm.MyModels.length;i++)p.lm.MyModels[i].addGene(g); //ADD TO MODEL
  }

  /*
   * Trims leading and trailing whitespace. Whitespace blocks inside the string
   * are truncated to one character.
   */
  public String trimGeneName(String name) {
    name = name.trim().replaceAll(" +", " ");
    return name;
  }

  /*
   * Checks if a gene with given name already exists in the model. The given
   * candidate gene name is checked against all existing gene names such that
   * all comparisons are done using normalized names.
   *
   * Example scenario:
   *
   * 1. Gene "A 1" is added to the model.
   * 2. getDuplicateGeneName is called with "A1" as the parameter.
   * 3. Since "A 1" (normalized to "A1") already exists, getDuplicateGeneName
   *    returns "A 1".
   *
   * Returns: null if no duplicate gene is found.
   *          name of duplicate gene if one exists.
   *
   */
  public String getDuplicateGeneName(String candidateName) {
    String normalizedName = UtilityFuncs.normKey(candidateName);

    for(int i = 0; i < Genes.size(); i++) {
      Gene gene = getGene(i);
      String normalizedGene = UtilityFuncs.normKey(gene.Name);
      if (normalizedName.equals(normalizedGene)) {
        return gene.Name;
      }
    }

    return null;
  }
              
  public void delGene(Gene g){
    p.mm.addMessage("Delete Gene " + g.Name); 
    if(p.eh.GeneDef==g)p.eh.GeneDef=null;
    for(int i=0;i<p.dm.nbDomain;i++) {
      p.dm.getDomain(i).delGene(g); //DEL TO DOMAIN
    }
    if(p.lm.MyModels!=null) {
      for(int i=0;i<p.lm.MyModels.length;i++) {
        p.lm.MyModels[i].delGene(g); //DEL TO MODEL
      }
    }
    ArrayList newListGenes=new ArrayList();
    for(int i=0;i<Genes.size();i++) {
       Gene gene=getGene(i);
       if(gene!=g) newListGenes.add(gene);
    }
    Genes=newListGenes; nbGene=Genes.size();
    if(nbGene==0)  p.mbm.checkEnablelMenu();
    
  }
  
  
  //To draw the gene invovle in the result
  public void addGeneInvolve(int a,int b,boolean v){
    int [] Inv=new int[3];Inv[0]=a;Inv[1]=b;   if(v)Inv[2]=1; else Inv[2]=0;
    p.mm.GenesInvolve.add(Inv);       
  }
  Logic ModulInvovle=null;
  boolean resultInvovle=false;
  
  public int getNumGene(String Name){  for(int i=0;i<nbGene;i++)  if(p.uf.equal(Name,getGene(i).Name)) return i;  return -1;}
  public int getNumGene(Gene gg){ for(int i=0;i<nbGene;i++)  if(getGene(i)==gg) return i;  return -1;}
  public Gene getGene(int n){ return (Gene)Genes.get(n);}
  public Gene getGene(String Name){  for(int i=0;i<nbGene;i++){  Gene g=getGene(i);   if(p.uf.equal(Name,g.Name)) return g; }return null;}
  
  //Order the Gene by Name;
  public ArrayList sortGene(ArrayList ListGenes){
    String [] GeneName=new String[ListGenes.size()];
    for(int i=0;i<ListGenes.size();i++){
      Gene g=(Gene)ListGenes.get(i);
      GeneName[i]=g.Name.toLowerCase();
    }
    GeneName=p.sort(GeneName);
    
    ArrayList NewListGenes=new ArrayList();
     for(int i=0;i<GeneName.length;i++){
       for(int j=0;j<ListGenes.size();j++){
          Gene g=(Gene)ListGenes.get(j);
          if(p.uf.equal(GeneName[i],g.Name.toLowerCase())) NewListGenes.add(g);
        }
     }
    return NewListGenes;
  }

  ArrayList<Gene> getReferencingGenes(Gene targetGene) {
    ArrayList<Gene> result = new ArrayList<Gene>();

    for (Iterator<Object> geneIter = Genes.iterator(); geneIter.hasNext();) {
      Gene gene = (Gene)geneIter.next();
      ArrayList<Gene> logicGenes = gene.getAllGenesInVectorEquations();

      if (logicGenes.contains(targetGene)) {
        result.add(gene);
      }
    }

    return result;
  }
}

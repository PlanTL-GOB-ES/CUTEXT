/*********************************************************************************************
This resource was developed by the Centro Nacional de Investigaciones Oncológicas (CNIO) 
in the framework of the "Plan de Impulso de las Tecnologías del Lenguaje” driven by the 
Secretaría de Estado para la Sociedad de la Información y Agenda Digital.

Copyright (C) 2017 Secretaría de Estado para la Sociedad de la Información y la Agenda Digital (SESIAD)
 
This program is free software; you can redistribute it and/or
modify it under the terms of the MIT License see LICENSE.txt file.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*********************************************************************************************/


/**
 *
 * @author Jesús Santamaría
 */
 


package cutext.filter.sta;


import cutext.prepro.DatosEntrada;

import java.io.*;
import java.util.*;
import java.util.regex.*;


public class FastCvalue implements Serializable
{
	//private static final long serialVersionUID = 344249317623161704L;
	private static final long serialVersionUID = -7149755349268484907L;


	private DatosEntrada datos;


	public FastCvalue(DatosEntrada datos)
	{
		this.datos = datos;
	}


	public void exec()
	{
		//get files from input folder
	//	if(DatosEntrada.dison)
	//		System.out.println("\ngetting all path files into folder " + this.datos.getRouteHashTerms() + " ...\n");
		List<String> inputPathFiles = cutext.util.Estaticos.listFilePerFolder(new File(this.datos.getRouteHashTerms()));

		//each table is crossed twice
		//first for candidate terms with length == maxLength
		for(int i = 0; i < inputPathFiles.size(); i++)
		{
			String pathFile = inputPathFiles.get(i);
			calculateCvalueForMaxLength(pathFile);
		}
		//second for candidate terms with length < maxLength
		for(int i = 0; i < inputPathFiles.size(); i++)
		{
			String pathFile = inputPathFiles.get(i);
			calculateCvalueForLessThanMaxLength(pathFile);
		}
	}
	
	/****************************************
		FOR TERMS WITH LENGTH == MAX LENGTH
	*****************************************/
	public void calculateCvalueForMaxLength(String pathFile)
	{
		cutext.prepro.SerSimpliHashTerms s = 
				new cutext.prepro.SerSimpliHashTerms("Recuperar", pathFile);
		cutext.prepro.SimpliHashTerms sht = s.recuperar();
		
	//	if(DatosEntrada.dison)
	//		System.out.println("calculate for max length: " + pathFile + " ...");
		calculateCvalueForMaxLength(sht);
	//	if(DatosEntrada.dison)
	//		System.out.println("calculated for max length: " + pathFile);
		
		//save ht in the same folder
		s = new cutext.prepro.SerSimpliHashTerms("Guardar", pathFile);
		s.guardar(sht);
	}
	
	public void calculateCvalueForMaxLength(cutext.prepro.SimpliHashTerms sht)
	{
		Enumeration<String> ks = sht.keys(); //terms for sht
		while(ks.hasMoreElements())
		{
			String k = ks.nextElement(); //term
			cutext.prepro.SimpliFrase value = sht.get(k);
			
			if(value.tamanyo() != datos.getPalabrasAprocesar()) //next term
				continue;
			double cvalue = cutext.util.Estaticos.log2(value.tamanyo()) * (double)value.getFrecuencia(); //English
			if(!this.datos.getIdioma().equals(DatosEntrada.ENG)) //other languages
				cvalue = 	(
							cutext.util.Estaticos.CTE_I + cutext.util.Estaticos.log2(value.tamanyo())
							) * 
							(double)value.getFrecuencia();
			value.setCvalue(cvalue);
			if(Double.compare(cvalue, datos.getUmbralCvalue()) >= 0)
			{
				reviseTC(k, value.getFrecuencia());
			}
		}
	}
	
	/****************************************
		FOR TERMS WITH LENGTH < MAX LENGTH
	*****************************************/
	public void calculateCvalueForLessThanMaxLength(String pathFile)
	{
		cutext.prepro.SerSimpliHashTerms s = new cutext.prepro.SerSimpliHashTerms("Recuperar",  pathFile);
		cutext.prepro.SimpliHashTerms sht = s.recuperar();
		
	//	if(DatosEntrada.dison)
	//		System.out.println("calculate for others: " + pathFile + " ...");
		calculateCvalueForLessThanMaxLength(sht);
	//	if(DatosEntrada.dison)
	//		System.out.println("calculated for others: " + pathFile);
		
		//save ht in the same folder
		s = new cutext.prepro.SerSimpliHashTerms("Guardar", pathFile);
		s.guardar(sht);
	}
	
	public void calculateCvalueForLessThanMaxLength(cutext.prepro.SimpliHashTerms sht)
	{
		Enumeration<String> ks = sht.keys(); //terms for sht
		while(ks.hasMoreElements())
		{
			String k = ks.nextElement(); //term
			cutext.prepro.SimpliFrase value = sht.get(k);
			
			if(value.tamanyo() >= datos.getPalabrasAprocesar()) //next term
				continue;
			if(value.esPrimeraVez())
			{
				double cvalue = cutext.util.Estaticos.log2(value.tamanyo()) * (double)value.getFrecuencia(); //English
				if(!this.datos.getIdioma().equals(DatosEntrada.ENG)) //other languages
				cvalue = 	(
							cutext.util.Estaticos.CTE_I + cutext.util.Estaticos.log2(value.tamanyo())
							) * 
							(double)value.getFrecuencia();				
				value.setCvalue(cvalue);
			}
			else
			{
				double cvalue = cutext.util.Estaticos.log2(value.tamanyo()) * 
					(double)(((double)value.getFrecuencia()) - (double)((1/(double)value.getC()) * ((double)value.getT()))); //English
				if(!this.datos.getIdioma().equals(DatosEntrada.ENG)) //other languages
					cvalue = 	(
								cutext.util.Estaticos.CTE_I + cutext.util.Estaticos.log2(value.tamanyo())
								) * 
								(double)(((double)value.getFrecuencia()) - (double)((1/(double)value.getC()) * ((double)value.getT())));
				value.setCvalue(cvalue);
				if(Double.compare(cvalue, datos.getUmbralCvalue()) >= 0)
				{
					reviseTC(k, value.getFrecuencia());
				}
			}
		}
	}
		
	
	//a: term cadidate; fa: a frequency
	public void reviseTC(String a, int fa)
	{
		cutext.prepro.SerSimpliHashTerms s = 
					new cutext.prepro.SerSimpliHashTerms("Recuperar",  datos.getRouteHashTerms() + "hsimpli");
		cutext.prepro.SimpliHashTerms sht = s.recuperar();
		//
		List<String> subterms = getSubterms(a);
		//modify t and c for each subterm
		for(String subterm : subterms)
		{
			cutext.prepro.SimpliFrase v = sht.get(subterm);
			if(v != null) //exist
			{
				v.setT(v.getT() + fa);
				v.setC(v.getC() + 1);
			}
		}
		//save sht
		s = new cutext.prepro.SerSimpliHashTerms("Guardar",  datos.getRouteHashTerms() + "hsimpli");
		s.guardar(sht);
	}
	
	public static List<String> getSubterms(String term)
	{
		List<String> subterms = new ArrayList<String>();
		String termwords[] = term.split("\\s+");
		for(int i = 1; i < termwords.length; i++)
		{
			List<String> partialsubterms = getSubterms(termwords, i);
			//System.out.println("\n\t" + i + ". subterms with size " + i + "\n" + subterms.toString());
			subterms.addAll(partialsubterms);
		}
		return subterms;
	}
	
	public static List<String> getSubterms(String termwords[], int sizeSubterms)
	{
		List<String> subterms = new ArrayList<String>();
		int endIndex = termwords.length - sizeSubterms + 1;
		for(int i = 0; i < endIndex; i++)
		{
			String subterm = getSubterm(termwords, i, (sizeSubterms + i - 1));
			subterms.add(subterm);
		}
		return subterms;
	}
	
	public static String getSubterm(String termwords[], int beginIndex, int endIndex)
	{
		String subterm = "";
		for(int i = beginIndex; i <= endIndex; i++)
		{
			subterm += termwords[i] + " ";
		}
		return (subterm.trim());
	}
	

}







































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
 


/*
*	Create output files from hashTerms
*/




package cutext.util;




import cutext.prepro.SimpliFrase;

import cutext.prepro.SerSimpliHashTerms;
import cutext.prepro.SimpliHashTerms;

import cutext.util.StringToFile;

import java.io.*;
import java.util.*;
import java.nio.charset.*;
import java.nio.file.*;



public class CreateOutFiles
{
	
	
	private static final long serialVersionUID = -7149755349268484907L;

	
	public static final String HASH = "HASH";
	public static final String RAW = "RAW TEXT SORTED BY CVALUE";
	public static final String BIOC = "BIOC";
	public static final String JSON = "JSON";
		
	public static int totalTerms = 0;
	
	
	//return one file per hashTerms
	public static void create(String routeHashTerms, String routeOut, String type)
	{
		if(type.equals(BIOC))
			createHeaderBioC(routeOut);
		else if(type.equals(JSON))
			createHeaderJson(routeOut);
		else if(type.equals(RAW))
			createHeaderRaw(routeOut);
		
		List<String> inputPathHashes = 
			cutext.util.Estaticos.listFilePerFolder(new File(routeHashTerms));
			
		if(type.equals(RAW))
				createBodyRaw(inputPathHashes, routeOut);

		for(int i = 0; i < inputPathHashes.size(); i++)
		{
			String pathHash = inputPathHashes.get(i);
			if(type.equals(HASH))
				createHashFile(pathHash, routeOut);
			else if(type.equals(BIOC))
				createBodyBioC(pathHash, routeOut);
			else if(type.equals(JSON))
				createBodyJson(pathHash, routeOut);
		}
		
		if(type.equals(BIOC))
			createFooterBioC(routeOut);
		else if(type.equals(JSON))
			createFooterJson(routeOut);
		else if(type.equals(RAW))
			createFooterRaw(routeOut);
		
		//set totalTerms 
		CreateOutFiles.totalTerms = 0;
	}
	
	
	/***********************************
	*	HASH: one file per hash
	***********************************/
	public static void createHashFile(String pathHash, String route)
	{
		SerSimpliHashTerms s = new SerSimpliHashTerms("Recuperar", pathHash);
		SimpliHashTerms sht = s.recuperar();
		
		//term | lemma | freq | cvalue | tamanyo | t | c
		String strht = sht.toString();
		String nameht = (new File(pathHash).getName());
		
		StringToFile.stringToFile(strht, route + nameht + ".txt");
	}
	
	
	/*****************
	*	BIOC
	******************/
	public static void createHeaderBioC(String routeOut)
	{
		//date
		Calendar fecha = new GregorianCalendar();

		String y = String.valueOf(fecha.get(Calendar.YEAR));
	    String m = String.valueOf(fecha.get(Calendar.MONTH) + 1);
        String d = String.valueOf(fecha.get(Calendar.DAY_OF_MONTH));
		if(m.length() == 1)
			m = "0" + m;
		if(d.length() == 1)
			d = "0" + d;

		String header = "<?xml version='1.0' encoding='UTF-8'?>" + "\n" + 
						"<!DOCTYPE collection SYSTEM \"BioC.dtd\">" + "\n" + 
						"<collection>" + "\n" + 
						"<source>Unknown</source>" + "\n" + 
						"<date>" + y+m+d+ "</date>" + "\n" + 
						"<key>nothing.key</key>" + "\n" + 
						"<document>" + "\n" + 
						"<id>1</id>" + "\n" + 
						"<passage>" + "\n" + 
						"<infon key=\"type\">paragraph</infon>" + "\n";
		
		StringToFile.stringToFileAppend(header, routeOut);
	}
	
	public static void createBodyBioC(String pathHash, String routeOut)
	{
		String textbioc = "";
		SerSimpliHashTerms s = new SerSimpliHashTerms("Recuperar", pathHash);
		SimpliHashTerms sht = s.recuperar();
		
		//terms
		Enumeration<String> ks = sht.keys();
		while(ks.hasMoreElements())
		{
			String k = ks.nextElement();
			SimpliFrase termino = sht.get(k);
			String termText = termino.getWords();
			int termLength = termino.getWords().length();
			termLength += termino.tamanyo() - 1; //sumar los espacios entre las palabras

			textbioc += "<annotation id = \"0\">" + "\n";
			textbioc += "<infon key = \"type\">Term</infon>" + "\n";
			textbioc += "<infon key = \"cvalue\">" + termino.getCvalue() + "</infon>" + "\n";
			textbioc += "<infon key = \"frecuency\">" + termino.getFrecuencia() + "</infon>" + "\n";
			textbioc += "<length = \"" + String.valueOf(termLength) + "\" />" + "\n";
			textbioc += "<text>" + termText + "</text>" + "\n";
			textbioc += "</annotation>" + "\n";
			//write to file
			StringToFile.stringToFileAppend(textbioc, routeOut);
			textbioc = "";
		}
	}
	
	public static void createFooterBioC(String routeOut)
	{
		String textbioc = "</passage>" + "\n";
		textbioc += "</document>" + "\n";
		textbioc += "</collection>";
		//write to file
		StringToFile.stringToFileAppend(textbioc, routeOut);
	}

	
	
	
	/*****************
	*	JSON
	******************/
	public static void createHeaderJson(String routeOut)
	{
		String fileName = (new File(routeOut)).getName();
		int extFileName = fileName.indexOf(".");
		if(extFileName != -1) //remove extension
			fileName = fileName.substring(0, extFileName);
		String textjson = "{\n" + "\t\"" + fileName + "\"" + ":\n\t[\n";
		
		StringToFile.stringToFileAppend(textjson, routeOut);
	}
	
	public static void createBodyJson(String pathHash, String routeOut)
	{
		String textjson = "";
		SerSimpliHashTerms s = new SerSimpliHashTerms("Recuperar", pathHash);
		SimpliHashTerms sht = s.recuperar();
		
		//terms
		Enumeration<String> ks = sht.keys();
		int i = 0;
		while(ks.hasMoreElements())
		{
			i++;
			String k = ks.nextElement();
			SimpliFrase termino = sht.get(k);
			
			String term = termino.getWords();
			term = term.trim();

			textjson += "\t\t{\n";
			textjson += "\t\t\t\"term" + "\":" + " \"";
			textjson += term + "\",\n";
			textjson += "\t\t\t\"frecuency" + "\":" + " \"";
			textjson += termino.getFrecuencia() + "\",\n";
			textjson += "\t\t\t\"c-value" + "\":" + " \"";
			textjson += termino.getCvalue() + "\"\n";
			textjson += "\t\t}\n";
			//write to file
			StringToFile.stringToFileAppend(textjson, routeOut);
			textjson = "";
		}
	}
	
	public static void createFooterJson(String routeOut)
	{
		String textjson = "\t]\n}";
		//write to file
		StringToFile.stringToFileAppend(textjson, routeOut);
	}
	

	
	
	/*****************
	*	RAW
	******************/
	public static void createHeaderRaw(String routeOut)
	{
		//date
		Calendar fecha = new GregorianCalendar();

		String y = String.valueOf(fecha.get(Calendar.YEAR));
	    String m = String.valueOf(fecha.get(Calendar.MONTH) + 1);
        String d = String.valueOf(fecha.get(Calendar.DAY_OF_MONTH));
		if(m.length() == 1)
			m = "0" + m;
		if(d.length() == 1)
			d = "0" + d;

		String textraw = "\n\n\n\t\t\tFile Created on " + y + "-" + m + "-" + d + "\n\n\n";
		textraw += "\n\n\n===== TERMS (sorted by c-value) =====\n\n\n";
		
		StringToFile.stringToFileAppend(textraw, routeOut);
	}
	
	public static void createBodyRaw(List<String> inputPathHashes, String routeOut)
	{
		String textraw = "";
		
		//Hashtable<String, Frase> hall = getAllHashTerms(inputPathHashes);
		SimpliHashTerms sht = null;
		for(int i = 0; i < inputPathHashes.size(); i++)
		{
			String pathHash = inputPathHashes.get(i);
			SerSimpliHashTerms s = new SerSimpliHashTerms("Recuperar", pathHash);
			sht = s.recuperar();
		}
		
		CreateOutFiles.totalTerms += sht.size();
		
		//sort
		ArrayList<Map.Entry<String, SimpliFrase>> listSort = sortValue(convert(sht));
		
		for(int i = 0; i < listSort.size(); i++)
        {
            Map.Entry<String, SimpliFrase> me = listSort.get(i);
            String k = me.getKey();
			SimpliFrase termino = me.getValue();
			//

			String termText = termino.getWords();
			int termLength = termino.getWords().length();
			termText = termText.trim();
			termLength += termino.tamanyo() - 1; //sumar los espacios entre las palabras

			textraw += "Term: " + termText + "\n";
			//textraw += "Term: " + termino.wordsToString() + "\n";
			textraw += "Lema: " + k + "\n";
			textraw += "Cvalue: " + termino.getCvalue() + "\n";
			textraw += "Frecuency: " + termino.getFrecuencia() + "\n";
			textraw += "Length: " + String.valueOf(termLength) + "\n";
			textraw += "\n\n\n";
			//write to file
			StringToFile.stringToFileAppend(textraw, routeOut);
			textraw = "";
        }
		//write to file
		//StringToFile.stringToFileAppend(textraw, routeOut);
	}
	
	public static void createFooterRaw(String routeOut)
	{
		String textraw = "\n\n\n===== TOTAL TERMS: " + CreateOutFiles.totalTerms + " =====\n\n\n";
		//write to file
		StringToFile.stringToFileAppend(textraw, routeOut);
	}

	
	
	
	/*
	*	Methods to sort simpli hash terms
	*/
	
	public static Hashtable<String,SimpliFrase> convert(SimpliHashTerms ht)
	{
		Hashtable<String,SimpliFrase> htable = new Hashtable<String,SimpliFrase>();
		Enumeration<String> ks = ht.keys();
		while(ks.hasMoreElements())
		{
			String k = ks.nextElement();
			cutext.prepro.SimpliFrase v = ht.get(k);
			htable.put(k,v);
		}
		return htable;
	}
	
	public static ArrayList<Map.Entry<String, SimpliFrase>> sortValue(Hashtable<String,SimpliFrase> t)
    {
       //Transfer as List and sort it
       ArrayList<Map.Entry<String, SimpliFrase>> l = new ArrayList(t.entrySet());
       Collections.sort(l, new Comparator<Map.Entry<String,SimpliFrase>>(){

         public int compare(Map.Entry<String,SimpliFrase> o1, Map.Entry<String,SimpliFrase> o2) {
            //return o1.getValue().compareTo(o2.getValue());
            return Double.compare(o2.getValue().getCvalue(), o1.getValue().getCvalue());
        }});

       return l;
    }
	
	
	
	

	
	

	

	public static void main(String[] args)
	{
/*
		String routeH = "../../cutext/entrada/hashTermsTemporal/";
		String routeO = "../../cutext/out/terms-raw.txt";
		routeH = routeH.replace("/", cutext.util.Estaticos.FILE_SEP);
		routeO = routeO.replace("/", cutext.util.Estaticos.FILE_SEP);
		
		long initialtime = System.nanoTime();
		System.out.println("\n\tCreating raw file");
		
		CreateOutFiles.create(routeH, routeO, CreateOutFiles.RAW);
		
		long ftime = System.nanoTime() - initialtime;
		es.cnio.bionlp.recommender.util.Time t = new es.cnio.bionlp.recommender.util.Time();
		t.conversion(ftime);
		System.out.println("\n\tRaw file created. Time: " + t.toString());
*/
	}


}







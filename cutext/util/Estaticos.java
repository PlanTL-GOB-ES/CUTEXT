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
 


/**************************
* Atributos estaticos
***************************/



package cutext.util;

import java.util.*;
import java.io.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class Estaticos
{
	
	
	private static final long serialVersionUID = -7149755349268484907L;

	public Estaticos()
	{
	}



	public static final String FILE_SEP = System.getProperty("file.separator"); //the file separator depends on OS: Linux="/"; Windows="\"


	//public static final int UMBRAL_FRECUENCIA = 0; //en el paper 3

	public static final int LONGITUD_MAXIMA_STRINGS = 5; //en el paper 5

	//public static final int UMBRAL_CVALUE = 0; //en el paper 0

	public static final double FACTOR_CVALUE = 0.8; //en el paper 0.8

	public static final double FACTOR_WEIGHT_CVALUE = 0.2; //en el paper 0.2

	public static final double CTE_I = 1.0; //en el paper 1.0



	public static Hashtable<String,String> STOPWORDS_LIST = new Hashtable<String,String>();

	public static Hashtable<String,String> LISTA_PUNCT = new Hashtable<String,String>();

	public static Hashtable<String,String> FRONT_PUNCT_MARKS = new Hashtable<String,String>();





	//Sometimes Treetagger returns <unknown> for a lemma
	public static final String U = "<unknown>";

	//taggers
	public static final String TT = "TreeTagger";
	public static final String GT = "GeniaTagger";
	public static final String FT = "FreelingTagger";



	//Logaritmo en base 2 de num = Math.log(num) / Math.log(2);
	public static double log2(double a)
	{
		return (Math.log(a) / Math.log(2));
	}

/*
	//Determina si el string esta en la lista
	public static boolean esta(String elemento, String lista[])
	{
		for(int i = 0; i < lista.length; i++)
		{
			if(elemento.equals(lista[i]))
				return true;
		}
		return false;
	}
*/

	//Determina si el string esta en la tabla
	public static boolean esta(String elemento, Hashtable tabla)
	{
		String v = (String)tabla.get((String)elemento);
		return (v != null);
	}

	//Para ejecucion, hasta intro
	public static void parada()
	{
		Scanner entradaEscaner = new Scanner(System.in);
		String entradaTeclado = entradaEscaner.nextLine();
	}


	public void rellenarLista(Hashtable ht, String lista[])
	{
		for(int i = 0; i < lista.length; i++)
		{
			ht.put(lista[i], lista[i]);
		}
	}

	public static void setConfigFiles(Hashtable ht, List<String> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			String element = (String)list.get(i);
			ht.put(element, element);
		}
	}



	//only one space between words
	public static String setOnlyOneSpaceBetweenWords(String sentence)
	{
		return join(deleteEmpties(sentence.split("\\s")), " ");
	}

	public static ArrayList<String> deleteEmpties(String str[])
	{
		ArrayList<String> l = new ArrayList<String>();
		for(int i = 0; i < str.length; i++)
		{
			if(!str[i].equals(""))
				l.add((String)str[i]);
		}
		return l;
	}

	static public String join(ArrayList<String> list, String conjunction)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(String item : list)
		{
			if (first)
				first = false;
			else
				sb.append(conjunction);
			sb.append(item);
		}
		return sb.toString();
	}



	static public List<String> takeOutGaps(List<String> list)
	{
		if((list == null) || list.size() == 0)
			return list;
		List<String> newList = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++)
		{
			String str = list.get(i);
			if(!str.equals(""))
				newList.add(str);
		}
		return newList;
	}
	
	
	
	
	
	
	
	
	//extracts subsequences (n-grams) from term
	public static List<String> getSubterms(String termwords[], int sizeSubterms)
	{
		List<String> subterms = new ArrayList<String>();
		//String termwords[] = term.split("\\s+");
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


	

	
	public static List<String> listFilePerFolder(File folder)
	{
		List<String> files = new ArrayList<String>();
		for(File infile : folder.listFiles())
		{
			if(infile.isDirectory())
			{
				files.addAll(listFilePerFolder(infile));
			}
			else
			{
				files.add(infile.getPath());
				//files.add(infile.getName());
			}
		}
		return files;
	}

	
	
	
	

	/*
	* Load parameters from properties file
	* 
	*/
	public static void loadCutextProperties(cutext.prepro.DatosEntrada datos, String routeProperties, boolean readInOut)
	{
		Properties p = new Properties();
		try
		{
			p.load(new FileReader(routeProperties));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		Enumeration<Object> keys = p.keys();
		
		while (keys.hasMoreElements())
		{
			String key = (String)keys.nextElement();
			String value = (String)p.get(key);
			
			//check and replace each parameter
			if(key.equals("-displayon"))
				datos.dison = Boolean.parseBoolean(value.toLowerCase());
			else if(key.equals("-withoutcvalue"))
				datos.setExecWithoutCvalue(Boolean.parseBoolean(value.toLowerCase()));
			else if(key.equals("-generateTextFile"))
				datos.setGenerateTextFile(Boolean.parseBoolean(value.toLowerCase()));
			else if(key.equals("-postagger"))
					datos.setPosTagger(value);
			else if(key.equals("-frecT"))
				datos.setUmbralFrecuencia(Integer.parseInt(value));
			else if(key.equals("-cvalueT"))
				datos.setUmbralCvalue(Double.parseDouble(value));
			else if(key.equals("-bioc"))
				datos.setOutBioC(Boolean.parseBoolean(value.toLowerCase()));
			else if(key.equals("-json"))
				datos.setOutJson(Boolean.parseBoolean(value.toLowerCase()));
			else if(key.equals("-convert"))
				datos.setConvertFileToLowerCase(Boolean.parseBoolean(value.toLowerCase()));
			//=====
			//files to delete: these parameters are not in DatosEntrada, only at the properties file
			//=====
			//routes
			else if(key.equals("-routeHashTerms"))
			{
				value = value.replace("/", Estaticos.FILE_SEP);
				value = value.replace("\\", Estaticos.FILE_SEP);
				datos.setRouteHashTerms(value);
			}
			else if(key.equals("-routeTextFileHashTerms"))
			{
				value = value.replace("/", Estaticos.FILE_SEP);
				value = value.replace("\\", Estaticos.FILE_SEP);
				datos.setRouteTextFileHashTerms(value);
			}
			else if(key.equals("-inputFile"))
			{
				if(readInOut)
				{
					String inputFile = value;
					inputFile = inputFile.replace("/", Estaticos.FILE_SEP);
					inputFile = inputFile.replace("\\", Estaticos.FILE_SEP);
					String onlyDir = inputFile.substring(0, inputFile.lastIndexOf(Estaticos.FILE_SEP));
					String onlyFile = inputFile.substring(inputFile.lastIndexOf(Estaticos.FILE_SEP) + 1, inputFile.length());
					datos.setDirectorioEntrada(onlyDir);
					ArrayList al = new ArrayList();
					al.add((String)onlyFile);
					datos.setListaFicheros(al);
				}
			}
			else if(key.equals("-outputFile"))
			{
				if(readInOut)
				{
					value = value.replace("/", Estaticos.FILE_SEP);
					value = value.replace("\\", Estaticos.FILE_SEP);
					datos.setDirectorioInformacionSalida(value);
				}
			}
			else if(key.equals("-routeconfigfiles"))
			{
				String rcf = value.replace("/", Estaticos.FILE_SEP);
				rcf = rcf.replace("\\", Estaticos.FILE_SEP);
				datos.fileLangSpa = rcf + "tags-spa.txt";
				datos.fileLangEng = rcf + "tags-eng.txt";
				datos.fileLangGal = rcf + "tags-gal.txt";
				datos.fileLangCat = rcf + "tags-cat.txt";

				datos.filePunctuation = rcf + "punctuation.txt";
				datos.fileFrontiersPunctuation = rcf + "frontiers-punctuation.txt";

				datos.fileSWspa = rcf + "stop-words-spa.txt";
				datos.fileSWeng = rcf + "stop-words-eng.txt";
				datos.fileSWgal = rcf + "stop-words-gal.txt";
				datos.fileSWcat = rcf + "stop-words-cat.txt";
			}
			else if(key.equals("-routeinterntt"))
			{
				String ritt = value.replace("/", Estaticos.FILE_SEP);
				ritt = ritt.replace("\\", Estaticos.FILE_SEP);
				datos.setDirectorioEntrada(ritt + "in");
				datos.setDirectorioIntermedio(ritt + "x");
				datos.setDirectorioSalida(ritt + "out");
			}
			else if(key.equals("-routeProperties"))
			{
				value = value.replace("/", Estaticos.FILE_SEP);
				value = value.replace("\\", Estaticos.FILE_SEP);
				routeProperties = value;
				datos.setRouteProperties(routeProperties);
			}
			//this parameter change another DatosEntrada parameters 
			else if(key.equals("-language"))
			{
				datos.setIdioma(value.toUpperCase());
				//this.lan = true;
			}
		}
	}
	
	
	
	
	
	public static void deleteFiles(String dir)
	{
		File route = new File(dir);
		File listFiles[] = route.listFiles();
		if(listFiles == null)
		{
			//The input directory does not exist
			System.err.println("input directory: #" + dir + "# non-existent");
			System.exit(0);
		}
		for(int i = 0; i < listFiles.length; i++)
		{
			File infile = listFiles[i];
			if(!infile.isDirectory()) //skip directories
			{
				boolean infiledelete = infile.delete();
				if(!infiledelete)
					System.out.println("\n\tTHE FILE " + infile.getName() + " COULD NOT BE DELETED");
			}
		}
	}
	
	
	
	
	
	//Copy and Paste fileName at inputPath to outputPath 
	public static void copyAndPaste(String inputPath, String outputPath, String fileName)
	{
		try
		{           
			File fileToCopy = new File(inputPath, fileName);
			File destFile = new File(outputPath, fileName);
			if(fileToCopy.exists())
			{
				Files.copy(Paths.get(fileToCopy.getAbsolutePath()), Paths.get(destFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
			}
			else
			{
				System.out.println("The file " + fileName + " non-existent at the directory " + inputPath);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	


}




























































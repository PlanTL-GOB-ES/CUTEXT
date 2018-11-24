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
 


/**************************************************************************************************
* Almacena los datos introducidos por el usuario. 
***************************************************************************************************/





package cutext.prepro;



import java.util.*;
import java.io.*;


import java.nio.charset.*;
import java.nio.file.*;



import cutext.util.*;
//import cutext.filter.lin.*;




public class DatosEntrada implements Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	

	public static final String N = "." + Estaticos.FILE_SEP + "intern" + Estaticos.FILE_SEP + "datosEntrada" + Estaticos.FILE_SEP + "datosEntrada"; //para ARDatosEntrada

	public static final String SPA = "SPANISH";
	public static final String ENG = "ENGLISH";
	public static final String CAT = "CATALAN";
	public static final String GAL = "GALICIAN";

	String idioma = SPA;

	String directorioEntrada = ".." + Estaticos.FILE_SEP + "intern" + Estaticos.FILE_SEP + "TT" + Estaticos.FILE_SEP + "in";
	String directorioIntermedio = ".." + Estaticos.FILE_SEP + "intern" + Estaticos.FILE_SEP + "TT" + Estaticos.FILE_SEP + "x";
	String directorioSalida = ".." + Estaticos.FILE_SEP + "intern" + Estaticos.FILE_SEP + "TT" + Estaticos.FILE_SEP + "out";

	String directorioModelo = ".." + Estaticos.FILE_SEP + "modelo" + Estaticos.FILE_SEP + "Model";

	String directorioInformacionSalida = ".." + Estaticos.FILE_SEP + "out" + Estaticos.FILE_SEP;

	String directorioInfo = ".." + Estaticos.FILE_SEP + "info" + Estaticos.FILE_SEP + "model";

	//WINDOWS
	String relacionTaggerIdioma[][] = {
				{"ENGLISH", "tag-english.bat"},
				{"FRENCH", "tag-french.bat"},
				{"GERMAN", "tag-german.bat"},
				{"GREEK", "tag-greek.bat"},
				{"ITALIAN", "tag-italian.bat"},
				{"SPANISH", "tag-spanish.bat"}
				};
	
	//LINUX
	String relacionTaggerLinuxIdioma[][] = {
				{"ENGLISH", "tree-tagger-english"},
				{"FRENCH", "tree-tagger-french"},
				{"GERMAN", "tree-tagger-german"},
				{"GREEK", "tree-tagger-greek"},
				{"ITALIAN", "tree-tagger-italian"},
//
				{"SPANISH", "tree-tagger-spanish"},
				{"GALICIAN", "tree-tagger-galician"},
				{"CATALAN", "tree-tagger-catalan"}
				};



	//tags files
	//spa
	public static String relativeFileLangSpa = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "tags-spa.txt";
	public static File flanspa = new File(relativeFileLangSpa);
	public static String fileLangSpa;// = flanspa.getAbsolutePath();
	//eng
	public static String relativeFileLangEng = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "tags-eng.txt";
	public static File flaneng = new File(relativeFileLangEng);
	public static String fileLangEng;// = flaneng.getAbsolutePath();
	//gal
	public static String relativeFileLangGal = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "tags-gal.txt";
	public static File flangal = new File(relativeFileLangGal);
	public static String fileLangGal;// = flangal.getAbsolutePath();
	//cat
	public static String relativeFileLangCat = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "tags-cat.txt";
	public static File flancat = new File(relativeFileLangCat);
	public static String fileLangCat;// = flancat.getAbsolutePath();


	//punctuation file
	private static String relativeFilePunct = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "punctuation.txt";
	public static File fp = new File(relativeFilePunct);
	public static String filePunctuation;// = fp.getAbsolutePath();

	//frontiers punctuation file
	private static String relativeFileFrontPunct = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "frontiers-punctuation.txt";
	public static File ffp = new File(relativeFileFrontPunct);
	public static String fileFrontiersPunctuation;// = ffp.getAbsolutePath();


	//stopwords files
	//spa
	public static String relativeFileSW1 = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "stop-words-spa.txt";
	public static File fswspa = new File(relativeFileSW1);
	public static String fileSWspa;// = fswspa.getAbsolutePath();
	//eng
	public static String relativeFileSW2 = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "stop-words-eng.txt";
	public static File fsweng = new File(relativeFileSW2);
	public static String fileSWeng;// = fsweng.getAbsolutePath();
	//gal
	public static String relativeFileSW3 = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "stop-words-gal.txt";
	public static File fswgal = new File(relativeFileSW3);
	public static String fileSWgal;// = fswgal.getAbsolutePath();
	//cat
	public static String relativeFileSW4 = ".." + Estaticos.FILE_SEP + "config_files" + Estaticos.FILE_SEP + "stop-words-cat.txt";
	public static File fswcat = new File(relativeFileSW4);
	public static String fileSWcat;// = fswcat.getAbsolutePath();






	String etiquetador = "tag-english.bat";
	String etiquetadorLinux = "tree-tagger-spanish";
	//String etiquetadorLinux = "tree-tagger-english";

	String ficheroTagger = "tagger";
	public static String TAGGER_FILE;
	//String ficheroTagger = "taggerOut.txt";

	boolean recuperarModelo = false;

	int palabrasAprocesar = 10;

	boolean modeloPredefinido = false;
	String modeloPredefinidoSeleccionado = "WSJ";

	ArrayList listaFicheros = new ArrayList();


	//POS tagger
	String posTagger = "TreeTagger";

	//umbrales
	int umbralFrecuencia = -0; //en el paper 3
	double umbralCvalue = -0.0; //en el paper 0.0

	//para filtro ling.
	String nombres[];
	String adjetivos[];
	String preposiciones[];
	String nombresC[];
	String nombresP[];
	String foreigns[];
	//para ncvalue
	String verbos[];

	//lista stop_words
	//String listaStopWords[];
	Hashtable<String,String> listaStopWords;

	//nombre en salida: cambiarlo para cada ejec.
	public static final String out = "INCREMENTAL";

	//indices para acelerar las busquedas en Filtro ling.
	Hashtable<Integer,Integer> indicesNombres = new Hashtable<Integer,Integer>();
	Hashtable<Integer,Integer> indicesAdjetivos = new Hashtable<Integer,Integer>();
	Hashtable<Integer,Integer> indicesPreposiciones = new Hashtable<Integer,Integer>();
	Hashtable<Integer,Integer> indicesNombresC = new Hashtable<Integer,Integer>();
	Hashtable<Integer,Integer> indicesNombresP = new Hashtable<Integer,Integer>();
	Hashtable<Integer,Integer> indicesAcronimos = new Hashtable<Integer,Integer>();


	//para los offset: salida bioc
	String fileToString = "";

	boolean outBioC = false;

	boolean convertFileToLowerCase = true;

	boolean execWithoutCvalue = false;

	boolean incremental = false;

	boolean outJson = false;
	
	
	
	String routeHashTerms = 
				".." + 
				Estaticos.FILE_SEP + 
				"out" + 
				Estaticos.FILE_SEP + 
				"serHashTerms" + 
				Estaticos.FILE_SEP;
				
	String routeTextFileHashTerms = 
				".." + 
				Estaticos.FILE_SEP + 
				"out" + 
				Estaticos.FILE_SEP + 
				"fileTextHashTerms" + 
				Estaticos.FILE_SEP;
	
	public static boolean dison;
	
	boolean generateTextFile = false;
	
	
	//../properties/cutext.properties
	String routeProperties = 
				".." + 
				Estaticos.FILE_SEP + 
				"properties" + 
				Estaticos.FILE_SEP + 
				"cutext.properties";
	

	public DatosEntrada()
	{
		//this.out = getFechaHora();
		DatosEntrada.TAGGER_FILE = this.ficheroTagger;
	}

	public DatosEntrada(String idioma, String directorioEntrada, String directorioIntermedio, String directorioSalida, boolean recuperarModelo, 
			String directorioInformacionSalida, int palabrasAprocesar, int umbralFrecuencia, double umbralCvalue)
	{
		//this.out = getFechaHora();
		this.idioma = idioma.toUpperCase();
		this.directorioEntrada = directorioEntrada;
		this.directorioIntermedio = directorioIntermedio;
		this.directorioSalida = directorioSalida;
		this.recuperarModelo = recuperarModelo;
		etiquetador = getEtiquetador(this.idioma);
		etiquetadorLinux = getEtiquetadorLinux(this.idioma);
		this.directorioInformacionSalida = directorioInformacionSalida;
		this.palabrasAprocesar = palabrasAprocesar;
		this.umbralFrecuencia = umbralFrecuencia;
		this.umbralCvalue = umbralCvalue;
	}


	/****************************************
		GET
	****************************************/

	public String getIdioma()
	{
		return idioma;
	}

	public String getDirectorioEntrada()
	{
		return directorioEntrada;
	}

	public String getDirectorioIntermedio()
	{
		return directorioIntermedio;
	}

	public String getDirectorioSalida()
	{
		return directorioSalida;
	}

	public String getDirectorioModelo()
	{
		return directorioModelo;
	}

	public String getEtiquetador()
	{
		return etiquetador;
	}

	public String getEtiquetadorLinux()
	{
		return etiquetadorLinux;
	}

	public String getFicheroTagger()
	{
		return ficheroTagger;
	}

	public boolean isRecuperarModelo()
	{
		return recuperarModelo;
	}

	public String getDirectorioInformacionSalida()
	{
		return directorioInformacionSalida;
	}

	public int getPalabrasAprocesar()
	{
		return palabrasAprocesar;
	}

	public String getDirectorioInfo()
	{
		return directorioInfo;
	}

	public boolean isModeloPredefinido()
	{
		return modeloPredefinido;
	}

	public String getModeloPredefinidoSeleccionado()
	{
		return modeloPredefinidoSeleccionado;
	}

	public ArrayList getListaFicheros()
	{
		return listaFicheros;
	}

	public int getUmbralFrecuencia()
	{
		return umbralFrecuencia;
	}

	public double getUmbralCvalue()
	{
		return umbralCvalue;
	}

	public String[] getNombres()
	{
		return nombres;
	}

	public String[] getAdjetivos()
	{
		return adjetivos;
	}

	public String[] getPreposiciones()
	{
		return preposiciones;
	}

	public String[] getVerbos()
	{
		return verbos;
	}

	public String[] getNombresC()
	{
		return nombresC;
	}

	public String[] getNombresP()
	{
		return nombresP;
	}

	public String[] getForeigns()
	{
		return foreigns;
	}

	public String getPosTagger()
	{
		return posTagger;
	}

/*
	public String[] getListaStopWords()
	{
		return listaStopWords;
	}
*/
	public Hashtable getListaStopWords()
	{
		return listaStopWords;
	}


	public String getFileToString()
	{
		return fileToString;
	}

	public boolean isOutBioC()
	{
		return outBioC;
	}
	
	public boolean isOutJson()
	{
		return outJson;
	}

	public boolean isConvertFileToLowerCase()
	{
		return convertFileToLowerCase;
	}

	public boolean isExecWithoutCvalue()
	{
		return execWithoutCvalue;
	}

	public boolean isIncremental()
	{
		return incremental;
	}
	
	public String getRouteProperties()
	{
		return this.routeProperties;
	}


	//------------------------------------   indices
	public int getIndicesNombres(int i)
	{
		if((i < 0) || (i >= indicesNombres.size()))
			return -1;
		return (int)indicesNombres.get(i);
	}

	public int getIndicesAdjetivos(int i)
	{
		if((i < 0) || (i >= indicesAdjetivos.size()))
			return -1;
		return (int)indicesAdjetivos.get(i);
	}

	public int getIndicesPreposiciones(int i)
	{
		if((i < 0) || (i >= indicesPreposiciones.size()))
			return -1;
		return (int)indicesPreposiciones.get(i);
	}

	public int getIndicesNombresC(int i)
	{
		if((i < 0) || (i >= indicesNombresC.size()))
			return -1;
		return (int)indicesNombresC.get(i);
	}

	public int getIndicesNombresP(int i)
	{
		if((i < 0) || (i >= indicesNombresP.size()))
			return -1;
		return (int)indicesNombresP.get(i);
	}

	public int getIndicesAcronimos(int i)
	{
		if((i < 0) || (i >= indicesAcronimos.size()))
			return -1;
		return (int)indicesAcronimos.get(i);
	}


	public int tamIndicesNombres()
	{
		return indicesNombres.size();
	}

	public int tamIndicesAdjetivos()
	{
		return indicesAdjetivos.size();
	}

	public int tamIndicesPreposiciones()
	{
		return indicesPreposiciones.size();
	}

	public int tamIndicesNombresC()
	{
		return indicesNombresC.size();
	}

	public int tamIndicesNombresP()
	{
		return indicesNombresP.size();
	}

	public int tamIndicesAcronimos()
	{
		return indicesAcronimos.size();
	}


	public boolean containsValueIndicesNombres(int v)
	{
		return indicesNombres.containsValue((Integer)v);
	}

	public boolean containsValueIndicesAdjetivos(int v)
	{
		return indicesAdjetivos.containsValue((Integer)v);
	}

	public boolean containsValueIndicesPreposiciones(int v)
	{
		return indicesPreposiciones.containsValue((Integer)v);
	}

	public boolean containsValueIndicesNombresC(int v)
	{
		return indicesNombresC.containsValue((Integer)v);
	}

	public boolean containsValueIndicesNombresP(int v)
	{
		return indicesNombresP.containsValue((Integer)v);
	}

	public boolean containsValueIndicesAcronimos(int v)
	{
		return indicesAcronimos.containsValue((Integer)v);
	}
	
	public String getRouteHashTerms()
	{
		return this.routeHashTerms;
	}
	
	public String getRouteTextFileHashTerms()
	{
		return this.routeTextFileHashTerms;
	}
	
	public boolean isGenerateTextFile()
	{
		return this.generateTextFile;
	}



	/****************************************
		SET
	****************************************/

	//Cuando se fija el idioma se fija todo lo referente a el
	public void setIdioma(String idioma)
	{
		this.idioma = idioma;
		//Fijar el resto
		//etiquetador
		this.setEtiquetador(this.getEtiquetador(this.getIdioma()));
		this.setEtiquetadorLinux(this.getEtiquetadorLinux(this.getIdioma()));
		//nombres, adjetivos, preposiciones, verbos
		this.fijarNAPV();
		//lista stop_words (internamente carga los config files de punct. y frontiers punct)
		this.fijarListaStopWords();
	}

	public void setDirectorioEntrada(String directorioEntrada)
	{
		this.directorioEntrada = directorioEntrada;
	}

	public void setDirectorioIntermedio(String directorioIntermedio)
	{
		this.directorioIntermedio = directorioIntermedio;
	}

	public void setDirectorioSalida(String directorioSalida)
	{
		this.directorioSalida = directorioSalida;
	}

	public void setDirectorioModelo(String directorioModelo)
	{
		this.directorioModelo = directorioModelo;
	}

	public void setEtiquetador(String et)
	{
		etiquetador = et;
	}

	public void setEtiquetadorLinux(String et)
	{
		etiquetadorLinux = et;
	}

	public void setFicheroTagger(String f)
	{
		ficheroTagger = f;
	}

	public void setRecuperarModelo(boolean r)
	{
		recuperarModelo = r;
	}

	public void setDirectorioInformacionSalida(String directorioInformacionSalida)
	{
		this.directorioInformacionSalida = directorioInformacionSalida;
	}

	public void setPalabrasAprocesar(int palabrasAprocesar)
	{
		this.palabrasAprocesar = palabrasAprocesar;
	}

	public void setDirectorioInfo(String directorioInfo)
	{
		this.directorioInfo = directorioInfo;
	}

	public void setModeloPredefinido(boolean mp)
	{
		this.modeloPredefinido = mp;
	}

	public void setModeloPredefinidoSeleccionado(String mp)
	{
		this.modeloPredefinidoSeleccionado = mp;
	}

	public void setListaFicheros(ArrayList listaFicheros)
	{
		this.listaFicheros = listaFicheros;
	}

	public void setUmbralFrecuencia(int umbralFrecuencia)
	{
		this.umbralFrecuencia = umbralFrecuencia;
	}

	public void setUmbralCvalue(double umbralCvalue)
	{
		this.umbralCvalue = umbralCvalue;
	}

	public void setNombres(String nombres[])
	{
		this.nombres = nombres;
	}

	public void setAdjetivos(String adjetivos[])
	{
		this.adjetivos = adjetivos;
	}

	public void setPreposiciones(String preposiciones[])
	{
		this.preposiciones = preposiciones;
	}

	public void setVerbos(String verbos[])
	{
		this.verbos = verbos;
	}

	public void setNombresC(String nombresC[])
	{
		this.nombresC = nombresC;
	}

	public void setNombresP(String nombresP[])
	{
		this.nombresP = nombresP;
	}

	public void setForeigns(String foreigns[])
	{
		this.foreigns = foreigns;
	}


	public void setPosTagger(String posTagger)
	{
		this.posTagger = posTagger;
	}

/*
	public void setListaStopWords(String lista[])
	{
		this.listaStopWords = lista;
	}
*/
	public void setListaStopWords(Hashtable lista)
	{
		this.listaStopWords = lista;
	}



	public void setFileToString(String str)
	{
		this.fileToString = str;
	}

	public void setOutBioC(boolean outBioC)
	{
		this.outBioC = outBioC;
	}
	
	public void setOutJson(boolean outJson)
	{
		this.outJson = outJson;
	}

	public void setConvertFileToLowerCase(boolean convertFileToLowerCase)
	{
		this.convertFileToLowerCase = convertFileToLowerCase;
	}

	public void setExecWithoutCvalue(boolean execWithoutCvalue)
	{
		this.execWithoutCvalue = execWithoutCvalue;
	}

	public void setIncremental(boolean incremental)
	{
		this.incremental = incremental;
	}
	
	public void setRouteProperties(String routeProperties)
	{
		this.routeProperties = routeProperties;
	}


	//-----------------------------------------------   indices: K=orden,V=pos
	public void setIndicesNombres(int indice)
	{
		indicesNombres.put(indicesNombres.size(), indice);
	}

	public void setIndicesAdjetivos(int indice)
	{
		indicesAdjetivos.put(indicesAdjetivos.size(), indice);
	}

	public void setIndicesPreposiciones(int indice)
	{
		indicesPreposiciones.put(indicesPreposiciones.size(), indice);
	}

	public void setIndicesNombresC(int indice)
	{
		indicesNombresC.put(indicesNombresC.size(), indice);
	}

	public void setIndicesNombresP(int indice)
	{
		indicesNombresP.put(indicesNombresP.size(), indice);
	}

	public void setIndicesAcronimos(int indice)
	{
		indicesAcronimos.put(indicesAcronimos.size(), indice);
	}
	
	public void setRouteHashTerms(String routeHashTerms)
	{
		this.routeHashTerms = routeHashTerms;
	}
	
	public void setRouteTextFileHashTerms(String routeTextFileHashTerms)
	{
		this.routeTextFileHashTerms = routeTextFileHashTerms;
	}
	
	public void setGenerateTextFile(boolean generateTextFile)
	{
		this.generateTextFile = generateTextFile;
	}


	//reboot indexes
	public void rebootIndexes()
	{
		this.indicesNombres = new Hashtable<Integer,Integer>();
		this.indicesAdjetivos = new Hashtable<Integer,Integer>();
		this.indicesPreposiciones = new Hashtable<Integer,Integer>();
		this.indicesNombresC = new Hashtable<Integer,Integer>();
		this.indicesNombresP = new Hashtable<Integer,Integer>();
		this.indicesAcronimos = new Hashtable<Integer,Integer>();
	}





	public void setPalabrasAprocesar(String palabrasAprocesar)
	{
		try
		{
			Integer i = Integer.valueOf(palabrasAprocesar);
			setPalabrasAprocesar(i.intValue());
		}
		catch(NumberFormatException e)
		{
			//setPalabrasAprocesar(Oracion.TAM_FRASE);
			System.err.println("--- DatosEntrada.setPalabrasAprocesar --- " + e);
		}
	}

	public String getEtiquetador(String idioma)
	{
		if(this.getPosTagger().equals("GeniaTagger"))
			return "geniatagger";
		for(int i = 0; i < relacionTaggerIdioma.length; i++)
		{
			if(relacionTaggerIdioma[i][0].equals(idioma))
				return relacionTaggerIdioma[i][1];
		}
		return null; //no hay coincidencias
	}

	public String getEtiquetadorLinux(String idioma)
	{
		if(this.getPosTagger().equals("GeniaTagger"))
			return "geniatagger";
		for(int i = 0; i < relacionTaggerLinuxIdioma.length; i++)
		{
			if(relacionTaggerLinuxIdioma[i][0].equals(idioma))
				return relacionTaggerLinuxIdioma[i][1];
		}
		return null; //no hay coincidencias
	}

	public String[] getIdiomas()
	{
		String idiomas[] = new String[relacionTaggerLinuxIdioma.length];
		for(int i = 0; i < relacionTaggerLinuxIdioma.length; i++)
			idiomas[i] = relacionTaggerLinuxIdioma[i][0];

		return idiomas;
	}







	//fijar los atributos nombres, adjetivos, preposiciones, y verbos una vez elegido el idioma: en GUI
	public void fijarNAPV()
	{
		setStaticParameters();
		List<String> list = null;
		if(this.getIdioma().equals(DatosEntrada.SPA))
		{
			list = FileToString.asStringList(DatosEntrada.fileLangSpa, StandardCharsets.UTF_8);
		}
		else if(this.getIdioma().equals(DatosEntrada.GAL)) //comienzan por
		{
			list = FileToString.asStringList(DatosEntrada.fileLangGal, StandardCharsets.UTF_8);
		}
		else if(this.getIdioma().equals(DatosEntrada.CAT)) //comienzan por
		{
			list = FileToString.asStringList(DatosEntrada.fileLangCat, StandardCharsets.UTF_8);
		}
		else if(this.getIdioma().equals(DatosEntrada.ENG)) //comienzan por
		{
			list = FileToString.asStringList(DatosEntrada.fileLangEng, StandardCharsets.UTF_8);
		}

		if(list == null)
		{
			System.err.println("the POS tags could not be loaded");
			System.exit(0);
		}

		//7 rows: nouns, common nouns, proper nouns, adjectives, prepositions, verbs, foreign words
		//nouns
		String s = list.get(0);
		String strNouns[] = s.split("\\|");
		this.nombres = strNouns;
		//common nouns
		s = list.get(1);
		String strCommonNouns[] = s.split("\\|");
		this.nombresC = strCommonNouns;
		//proper nouns
		s = list.get(2);
		String strProperNouns[] = s.split("\\|");
		this.nombresP = strProperNouns;
		//adjectives
		s = list.get(3);
		String strAdj[] = s.split("\\|");
		this.adjetivos = strAdj;
		//prepositions
		s = list.get(4);
		String strPrep[] = s.split("\\|");
		this.preposiciones = strPrep;
		//verbs
		s = list.get(5);
		String strVerbs[] = s.split("\\|");
		this.verbos = strVerbs;
		//foreign words
		s = list.get(6);
		String strForeigns[] = s.split("\\|");
		this.foreigns = strForeigns;
	}



	//fijar la lista de stop_words
	public void fijarListaStopWords()
	{
		if(this.getIdioma().equals(DatosEntrada.SPA))
		{
			List<String> list = FileToString.asStringList(DatosEntrada.fileSWspa, StandardCharsets.UTF_8);
			Estaticos.setConfigFiles(Estaticos.STOPWORDS_LIST, list);
		}
		else if(this.getIdioma().equals(DatosEntrada.GAL))
		{
			List<String> list = FileToString.asStringList(DatosEntrada.fileSWgal, StandardCharsets.UTF_8);
			Estaticos.setConfigFiles(Estaticos.STOPWORDS_LIST, list);
		}
		else if(this.getIdioma().equals(DatosEntrada.CAT))
		{
			List<String> list = FileToString.asStringList(DatosEntrada.fileSWcat, StandardCharsets.UTF_8);
			Estaticos.setConfigFiles(Estaticos.STOPWORDS_LIST, list);
		}
		else if(this.getIdioma().equals(DatosEntrada.ENG))
		{
			List<String> list = FileToString.asStringList(DatosEntrada.fileSWeng, StandardCharsets.UTF_8);
			Estaticos.setConfigFiles(Estaticos.STOPWORDS_LIST, list);
		}

		this.setListaStopWords(Estaticos.STOPWORDS_LIST);

		loadPunctConfigFiles();
	}



	//punctuation marks, frontiers punctuation marks
	public void loadPunctConfigFiles()
	{
		//punctuation marks
		List<String> list = FileToString.asStringList(DatosEntrada.filePunctuation, StandardCharsets.UTF_8);
		Estaticos.setConfigFiles(Estaticos.LISTA_PUNCT, list);

		//frontiers punctuation marks
		list = FileToString.asStringList(DatosEntrada.fileFrontiersPunctuation, StandardCharsets.UTF_8);
		Estaticos.setConfigFiles(Estaticos.FRONT_PUNCT_MARKS, list);
	}

	public void setStaticParameters()
	{
		//file language
		if(DatosEntrada.fileLangSpa == null)
			DatosEntrada.fileLangSpa = DatosEntrada.flanspa.getAbsolutePath();

		if(DatosEntrada.fileLangEng == null)
			DatosEntrada.fileLangEng = DatosEntrada.flaneng.getAbsolutePath();

		if(DatosEntrada.fileLangGal == null)
			DatosEntrada.fileLangGal = DatosEntrada.flangal.getAbsolutePath();

		if(DatosEntrada.fileLangCat == null)
			DatosEntrada.fileLangCat = DatosEntrada.flancat.getAbsolutePath();

		//punctuation
		if(DatosEntrada.filePunctuation == null)
			DatosEntrada.filePunctuation = DatosEntrada.fp.getAbsolutePath();

		if(DatosEntrada.fileFrontiersPunctuation == null)
			DatosEntrada.fileFrontiersPunctuation = DatosEntrada.ffp.getAbsolutePath();

		//stopwords files
		if(DatosEntrada.fileSWspa == null)
			DatosEntrada.fileSWspa = DatosEntrada.fswspa.getAbsolutePath();

		if(DatosEntrada.fileSWeng == null)
			DatosEntrada.fileSWeng = DatosEntrada.fsweng.getAbsolutePath();

		if(DatosEntrada.fileSWgal == null)
			DatosEntrada.fileSWgal = DatosEntrada.fswgal.getAbsolutePath();

		if(DatosEntrada.fileSWcat == null)
			DatosEntrada.fileSWcat = DatosEntrada.fswcat.getAbsolutePath();
	}

}





































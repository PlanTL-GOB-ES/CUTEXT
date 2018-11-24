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
 

/************************************************************************************ 
* Execute CUTEXT - Cvalue Used To EXtract Terms
************************************************************************************/


package cutext.main;

import cutext.util.*;


import java.util.*;
import java.io.*;


import java.nio.charset.*;
import java.nio.file.*;


import cutext.filter.lin.*;
import cutext.filter.sta.*;
import cutext.gui.*;
import cutext.prepro.*;
import cutext.util.*;


public class Cutext// extends Thread
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	DialogoEjecucion marco;
	DatosEntrada datos;
	Frases frases;

	String stringSalida;
	long tiempoPrimero;

	boolean hasElementWithSizePalabrasAprocesar = false;
	int maxSizeElement = 0;
	
	cutext.prepro.SimpliHashTerms simpliHashTerms = null;

	public Cutext(DialogoEjecucion marco, DatosEntrada datos, Frases frases, String stringSalida, long tiempoPrimero)
	{
		this.marco = marco;
		this.datos = datos;
		this.frases = frases;
		this.stringSalida = stringSalida;
		this.tiempoPrimero = tiempoPrimero;
		//
		this.simpliHashTerms = cutext.prepro.StaticHashTerms.get();
		
		run();
	}

	
	public void run()
	{
		if(this.datos.isIncremental())
			execInIncrementalMode(); //in this mode this.frases == null
		else
			execInBatchMode();
		
		//========================
		//save all HashTerms
		//StaticHashTerms.saveHashTerms(this.datos.getRouteHashTerms());
		//
		
		
		if(this.datos.isGenerateTextFile())
		{
			//one file per hashTerm
			CreateOutFiles.create(this.datos.getRouteHashTerms(), this.datos.getRouteTextFileHashTerms(), CreateOutFiles.HASH);
			if(this.datos.dison)
				System.out.println("\n\n\n....... text files (one per hashTerm) at: " + this.datos.getRouteTextFileHashTerms());
			//raw text sorted by cvalue
			CreateOutFiles.create(this.datos.getRouteHashTerms(), this.datos.getRouteTextFileHashTerms() + "terms_raw.txt", CreateOutFiles.RAW);
			if(this.datos.dison)
				System.out.println("\n\n\n....... raw text file at: " + this.datos.getRouteTextFileHashTerms() + "terms_raw.txt");
		}
			
		
		if(this.datos.isOutBioC())
		{
			CreateOutFiles.create(this.datos.getRouteHashTerms(), this.datos.getRouteTextFileHashTerms() + "terms_bioc.xml", CreateOutFiles.BIOC);
			if(this.datos.dison)
				System.out.println("\n\n\n....... text file in BioC format at: " + this.datos.getRouteTextFileHashTerms() + "terms_bioc.xml");
		}
			
		
		if(this.datos.isOutJson())
		{
			CreateOutFiles.create(this.datos.getRouteHashTerms(), this.datos.getRouteTextFileHashTerms() + "terms_json.json", CreateOutFiles.JSON);
			if(this.datos.dison)
				System.out.println("\n\n\n....... text file in JSON format at: " + this.datos.getRouteTextFileHashTerms() + "terms_json.json");
		}
			
	}
	
	
	public void execInIncrementalMode()
	{
		this.stringSalida = "";
		String rute = this.datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + this.datos.getFicheroTagger();
		//List<String> listLines = FileToString.asStringList(rute, StandardCharsets.UTF_8);
		List<String> listLines = FileToString.asStringList(rute, StandardCharsets.ISO_8859_1);
		listLines = Estaticos.takeOutGaps(listLines);
		String onlyDir = datos.getDirectorioIntermedio();
		String onlyFile = "temp.txt";
		this.datos.setFicheroTagger(onlyFile);
		this.datos.setDirectorioEntrada(datos.getDirectorioIntermedio());
		int itemp = 0;
		int ifile = 1;
		int itempthre = 30000;
		//
		long timeF1 = System.nanoTime();
		Tiempo t = new Tiempo();
		if(DatosEntrada.dison)
			System.out.println("Obtaining candidate terms (first filter)...");
		//
		for(int i = 0; i < listLines.size(); i++)
		{
			itemp++;
			if(DatosEntrada.dison)
				System.out.print("\tprocessing " + (i+1) + " corpus (of " + listLines.size() + ")...\r");
			String scorpus = listLines.get(i);
			this.stringSalida += "\n====================\n" + scorpus + "\n";
			//if(DatosEntrada.dison)
			//	System.out.println(scorpus);
			//create a temporal file with scorpus
			createTempFile(scorpus, onlyDir + Estaticos.FILE_SEP + onlyFile);
			ArrayList al = new ArrayList();
			al.add((String)onlyFile);
			datos.setListaFicheros(al);
			Preprocesar p = new Preprocesar(datos.getListaFicheros(), this.datos);
			//System.out.println("preprocessing...");
			this.frases = p.execTagger(onlyDir + Estaticos.FILE_SEP + onlyFile);
			//System.out.println("finished preprocess");
//			if(this.datos.isExecWithoutCvalue())
//			{
				execWithoutCvalue(ifile);
//			}
//			else
//			{
//				execWithoutCvalue(ifile);
//				ejecutar(ifile); //execute c-value
//			}
			this.datos.setFicheroTagger(onlyFile);
			//System.out.println("corpus " + (i+1) + " processed");
			reboot();
			if(itemp >= itempthre)
			{
				itemp = 0;
				ifile++;
				this.stringSalida = "";
				//saveHash();
			}
		}
		//here we have all hashTerms with frequencies
        //===============================
		//
		//OLD---> StaticHashTerms.saveHashTerms(this.datos.getRouteHashTerms());
		StaticHashTerms.saveSimpliHashTerms(this.datos.getRouteHashTerms());
		//
		t.conversion(System.nanoTime() - timeF1);
		if(DatosEntrada.dison)
			System.out.println("\n... candidate terms (first filter) obtained. Time:" + t.aString());
		//
		long timeF2 = System.nanoTime();
		if(DatosEntrada.dison)
			System.out.println("\nObtaining cvalue (second filter)...");
		//
		if(!this.datos.isExecWithoutCvalue())
		{
			//fastCvalue modified simpliHashTerms
			ejecutar(ifile); //execute c-value
		}
		//
		t.conversion(System.nanoTime() - timeF2);
		if(DatosEntrada.dison)
			System.out.println("...cvalue obtained (second filter). Time:" + t.aString());
		//
		//saveHash();
		//createHashesFiles(this.datos.getRouteTextFileHashTerms());
		reboot();
	}

	public void createTempFile(String scorpus, String sname)
	{
		StringToFile.oneLiner(scorpus.toLowerCase(), sname);
	}

	public void reboot()
	{
		this.frases = null;
		deleteFiles(this.datos.getDirectorioEntrada());
		deleteFiles(this.datos.getDirectorioIntermedio());
		deleteFiles(this.datos.getDirectorioSalida());
		this.datos.rebootIndexes();
	}

	public void deleteFiles(String dir)
	{
		File ruta = new File(dir);
		File listaArchivos[] = ruta.listFiles();
		if(listaArchivos == null)
		{
			//The input directory does not exist
			System.err.println("input directory: #" + dir + "# non-existent");
			System.exit(0);
		}
		for(int i = 0; i < listaArchivos.length; i++)
			listaArchivos[i].delete();
	}

	public void execInBatchMode()
	{
		execWithoutCvalue(0);
        //here we have all hashTerms with frequencies
        //===============================
		//
		//OLD---> StaticHashTerms.saveHashTerms(this.datos.getRouteHashTerms());
		StaticHashTerms.saveSimpliHashTerms(this.datos.getRouteHashTerms());
		//
		if(!this.datos.isExecWithoutCvalue())
		{
			ejecutar(0); //execute c-value
		}
		//
		reboot();
	}


	/************************************************
		execute the complete algorithm
	*************************************************/

	public void ejecutar(int ifile)
	{
		long tiempoInicial = System.nanoTime(); //Coger el tiempo inicial

		String tempstr = "";
/*
//=============================================================================================
		//fusionar todas las frases en una sola: corpus
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("merging...");			
		}

		Frase corpus = frases.fusion();
		long tiempoFusion = System.nanoTime() - tiempoInicial;
		Tiempo t = new Tiempo();
		t.conversion(tiempoFusion);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("merged. Tokens: " + corpus.tamanyo() + ". Time:" + t.aString());
		}
//System.out.println("===== CORPUS (size:" + corpus.tamanyo() + ") =====\n");
//System.out.println(corpus.aString());
//System.out.println("===== fin CORPUS =====\n");
//Estaticos.parada();


		//guardar el corpus
		//guardar(corpus);

		//filtrar
		long tiempoFILTROinicial = System.nanoTime();
		ArrayFrases corpusFiltrado = filtrar(corpus);
		//long tiempoFiltro = System.nanoTime() - tiempoInicial;
		long tiempoFiltro = System.nanoTime() - tiempoFILTROinicial;
		t = new Tiempo();
		t.conversion(tiempoFiltro);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.print("... time:" + t.aString());
		}
		//stringSalida += "\nFilter Time: " + t.aString();
		tempstr += "\nFilter Time: " + t.aString();
//System.out.println("===== CORPUS FILTRADO #" + corpusFiltrado.tamanyo() + " =====\n");
//System.out.println(corpusFiltrado.aString());
//System.out.println("===== fin CORPUS FILTRADO =====\n");
//Estaticos.parada();

		//stop list
		long tiempoSWinicial = System.nanoTime();
		StopWords sw = new StopWords(this.datos);
		ArrayFrases corpusFsw = stopList(sw, corpusFiltrado);
		long tiempoStopWords = System.nanoTime() - tiempoSWinicial;
		t = new Tiempo();
		t.conversion(tiempoStopWords);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.print("... time:" + t.aString());
		}
		//stringSalida += "\nStop List Time: " + t.aString();
		tempstr += "\nStop List Time: " + t.aString();
//System.out.println("===== STOP LIST #" + corpusFsw.tamanyo() + " =====\n");
//System.out.println(corpusFsw.aString());
//System.out.println("\n= Stop Words List =\n" + this.datos.getListaStopWords().toString());
//System.out.println("===== fin STOP LIST =====\n");
//Estaticos.parada();





		ArrayFrases terminos = corpusFsw;

		Frases fContr = terminos.contraer();

		loadIntoHash(fContr);
*/
//=============================================================================================

		
		
		
		//fast c-value
		long tiempoCVALUEinicial = System.nanoTime();
		cvalue();
		long tiempoCvalue = System.nanoTime() - tiempoCVALUEinicial;
		Tiempo t = new Tiempo();
		t.conversion(tiempoCvalue);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.print("... time:" + t.aString());
		}
		tempstr += "\nC-Value Time: " + t.aString();
		
		
/*		
		//nc-value
		long tiempoNCVALUEinicial = System.nanoTime();
		ArrayFrases terminos = ncvalue(corpus, corpusFsw, listaCvalue, sw);
		long tiempoNcvalue = System.nanoTime() - tiempoNCVALUEinicial;
		t = new Tiempo();
		t.conversion(tiempoNcvalue);
		if(DatosEntrada.dison)
			System.out.print("... time:" + t.aString());
		//stringSalida += "\nNC-Value Time: " + t.aString();
		tempstr += "\nNC-Value Time: " + t.aString();
//System.out.println("===== NC-VALUE  #" + terminos.tamanyo() + " =====\n");
//System.out.println(terminos.aString());
//System.out.println("===== fin NC-VALUE =====\n");
//Estaticos.parada();
*/

		//guardar el resultado final: terminos
		//guardar(terminos);



//		long tiempoEstimado = System.nanoTime() - tiempoPrimero;
//		t = new Tiempo();
//		t.conversion(tiempoEstimado);
		//stringSalida += "\nTotal Time: " + t.aString() + "\n-------------------";
//		tempstr += "\nTotal Time: " + t.aString() + "\n-------------------";


		//Frases fContr = terminos.contraer();
		//fContr = fContr.ordenarCvalue(fContr, 0, fContr.tamanyo());

		//gui
		if(marco != null)
		{
			marco.finalizar();
			//marco.insertar("\n\n\n\t=== " + String.valueOf(terminos.contraer().tamanyo()) + " TERMS (sorted by c-value) ===\n\n" + terminos.contraer().aStringLineal() + "\n" + t.aString());
		}

		//Frases fContr = terminos.contraer();
		//fContr = fContr.ordenarCvalue(fContr, 0, fContr.tamanyo());


		if(!this.datos.isIncremental())
		{
			this.stringSalida += tempstr;
			//crearFicheroSalida(fContr.aStringLineal(), fContr.tamanyo(), ifile);
		}
		//else
			//crearFicheroSalida(fContr.aString(), fContr.tamanyo(), ifile);

		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.print("... total time:" + t.aString() + "\n");
		}

		//marco.setTexto("\n\n\n=============== END ================");
		//marco.setTexto("\nTems\n" + ncv.getTerminos().aString());

//		Frases listaNCvalue = ncv.getLista();
//System.out.println("listaNCvalue\n" + listaNCvalue.aString());

		//crear json
		//crearJSON(fContr);

		//crear bioc
//		if(datos.isOutBioC())
//			crearBioC(fContr, corpus);

		//set the tagger file name at the beginning (for concurrent executions - GUI)
		datos.setFicheroTagger(datos.TAGGER_FILE);
		if(marco != null) //gui
			reboot();
		
		//initialize
		reboot();
	}



	/******************************
		without CValue
	*******************************/

	public void execWithoutCvalue(int ifile)
	{
		long tiempoInicial = System.nanoTime(); //Coger el tiempo inicial

		String tempstr = "";

		//fusionar todas las frases en una sola: corpus
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("merging...");
		}
		Frase corpus = frases.fusion();
		long tiempoFusion = System.nanoTime() - tiempoInicial;
		Tiempo t = new Tiempo();
		t.conversion(tiempoFusion);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("merged. Tokens: " + corpus.tamanyo() + ". Time:" + t.aString());
		}
		
//System.out.println("\nmerged: " + corpus.tamanyo() + ". Time:" + t.aString());
//System.out.println("\ncorpus\n" + corpus.aString() + "\n");

		//guardar el corpus
		//guardar(corpus);

		//filtrar
		long tiempoFILTROinicial = System.nanoTime();
		ArrayFrases corpusFiltrado = filtrar(corpus);
		long tiempoFiltro = System.nanoTime() - tiempoFILTROinicial;
		t = new Tiempo();
		t.conversion(tiempoFiltro);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.print("... time:" + t.aString());
		}
		tempstr += "\nFilter Time: " + t.aString();
		
//System.out.println("\nfiltered: " + corpusFiltrado.tamanyo() + ". Time:" + t.aString());
//System.out.println("\ncorpusFiltrado\n" + corpusFiltrado.aString() + "\n");

		//stop list: set the frequencies
		long tiempoSWinicial = System.nanoTime();
		StopWords sw = new StopWords(this.datos);
		ArrayFrases corpusFsw = stopList(sw, corpusFiltrado);
		long tiempoStopWords = System.nanoTime() - tiempoSWinicial;
		t = new Tiempo();
		t.conversion(tiempoStopWords);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.print("... time:" + t.aString());
		}
		tempstr += "\nStop List Time: " + t.aString();

		ArrayFrases terminos = corpusFsw;
		
//System.out.println("\nstop list: " + terminos.tamanyo() + ". Time:" + t.aString());
//System.out.println("\nterminos\n" + terminos.aString() + "\n");


		long tiempoEstimado = System.nanoTime() - tiempoPrimero;
		t = new Tiempo();
		t.conversion(tiempoEstimado);
		tempstr += "\nTotal Time: " + t.aString() + "\n-------------------";


		//gui
		if(marco != null)
		{
			marco.finalizar();
			//marco.insertar("\n\n\n\t=== " + String.valueOf(terminos.contraer().tamanyo()) + " TERMS (without c-value) ===\n\n" + terminos.contraer().aStringLineal() + "\n" + t.aString());
		}


		Frases fContr = terminos.contraer();
		
//System.out.println("\ncontraer: " + fContr.tamanyo() + ". Time:" + t.aString());
//System.out.println("\nfContr\n" + fContr.aString() + "\n");


		if(!this.datos.isIncremental())
		{
			this.stringSalida += tempstr;
			//crearFicheroSalida(fContr.aStringLineal(), fContr.tamanyo(), ifile);
		}

		loadIntoHash(fContr);


		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.print("... total time:" + t.aString() + "\n");
		}	

		//marco.setTexto("\n\n\n=============== END ================");
		//marco.setTexto("\nTems\n" + ncv.getTerminos().aString());

//		Frases listaNCvalue = ncv.getLista();
//System.out.println("listaNCvalue\n" + listaNCvalue.aString());

		//crear json
		//crearJSON(fContr);

		

		//crear bioc
		//if(datos.isOutBioC())
		//	crearBioC(fContr, corpus);

		//set the tagger file name at the beginning (for concurrent executions - GUI)
		datos.setFicheroTagger(datos.TAGGER_FILE);
		if(marco != null) //gui
			reboot();
			


	}





	/******************************
		Methods
	*******************************/


	public void guardar(Frase corpus)
	{
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("\nsaving corpus...");
		}
		//ARCorpus ar = new ARCorpus("Guardar");
		ARCorpus ar = new ARCorpus("Guardar", "corpus_");
		ar.guardar(corpus);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("corpus saved");
		}
	}

	public ArrayFrases filtrar(Frase corpus)
	{
		Filtro f = new Filtro(corpus, this.datos);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("\nfiltering...");
		}
		if(this.marco != null)
			this.marco.setTexto("\n\nfiltering...");
		ArrayFrases corpusFiltrado = f.filtrar();
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("finished filter");
		}
		if(this.marco != null)
			this.marco.insertar("\nfinished filter");

		return corpusFiltrado;
	}

	public ArrayFrases stopList(StopWords sw, ArrayFrases corpusFiltrado)
	{
		//asignar frecuencias al corpusFiltrado
		corpusFiltrado.setFrecuencias();
		//extraer los que satisfagan el filtro y el umbral de frecuencias
		ArrayFrases corpusFiltradoUmbral = corpusFiltrado.getUmbral(this.datos);
		//filtrar el corpus con las stop_words
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("\nstop list...");
		}
		ArrayFrases corpusFsw = sw.stopList(corpusFiltradoUmbral);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("finished stop list");
		}
//System.out.println("corpusFsw\n" + corpusFsw.aString());
		//eliminar posibles candidatos duplicados al eliminar alguna stop-word
		ArrayFrases af = corpusFsw.eliminarDuplicados();
//System.out.println("corpusFsw.eliminarDuplicados === arrayfrases\n" + af.aStringLineal());

		//return corpusFsw;
		return af;
	}
	
	public void loadIntoHash(Frases terms)
	{
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("\n\tCreating hash terms from " + terms.tamanyo() + " terms");
		}
		for(int j = 0; j < terms.tamanyo(); j++)
		{
			Frase term = terms.getFrase(j);
			//OLD---> add(term);
			add(term.toSimpliFrase());
		}
	}
	
	//add term to h and save h
	public void add(Frase term)
	{
//System.out.println("\ninside add (loadIntoHash)\nterm: " + term.aString() + "\n");
		String words = term.wordsToString();
		String k = term.lemasAstring();
		if((this.datos.getIdioma().equals(DatosEntrada.SPA)) || (this.datos.getIdioma().equals(DatosEntrada.ENG)))
		{
			cutext.stemmer.Stemmer stemmer = new cutext.stemmer.Stemmer(this.datos.getIdioma());
			k = stemmer.exec(words);
		}
		//String firstLetter = k.substring(0, 1);
		char firstLetter = k.toLowerCase().charAt(0);
		//==============================
		//cutext.prepro.HashTerms ht = this.get(firstLetter);
		cutext.prepro.HashTerms ht = cutext.prepro.StaticHashTerms.get(String.valueOf(firstLetter));
		//
		Frase v = ht.get(k);
		if(v == null) //add
		{
//System.out.println("\nk: " + k + ":::v == null\n");
			v = (Frase)term.clone();
			if(this.datos.isIncremental())
			{
				if(v.getFrecuencia() <= 1)
					v.setFrecuencia(1);
				else
					v.setFrecuencia(v.getFrecuencia());
			}
			ht.put(k, v);
		}
		else
		{
//System.out.println("\nk: " + k + ":::v NOT null\n");
			if(this.datos.isIncremental())
				v.setFrecuencia(v.getFrecuencia() + term.getFrecuencia());
				//v.setFrecuencia(v.getFrecuencia() + 1);
			ht.put(k, v);
		}
//System.out.println("\nht\n" + ht.toString() + "\n");
		//here v is not null
		if(v.tamanyo() > this.maxSizeElement)
		{
			this.maxSizeElement = v.tamanyo();
		}
		if(v.tamanyo() == this.datos.getPalabrasAprocesar())
		{
			this.hasElementWithSizePalabrasAprocesar = true;
		}
		//==============================
		//this.saveHash(ht, firstLetter);
		//
	}
	
	//add term to simpliHashTerms
	public void add(SimpliFrase term)
	{
//System.out.println("\ninside add (loadIntoHash)\nterm: " + term.aString() + "\n");
		String words = term.getWords();
		String k = term.getLemma();
		if((this.datos.getIdioma().equals(DatosEntrada.SPA)) || (this.datos.getIdioma().equals(DatosEntrada.ENG)))
		{
			cutext.stemmer.Stemmer stemmer = new cutext.stemmer.Stemmer(this.datos.getIdioma());
			k = stemmer.exec(words);
		}
		SimpliFrase v = this.simpliHashTerms.get(k);
		if(v == null) //add
		{
//System.out.println("\nk: " + k + ":::v == null\n");
			v = (SimpliFrase)term.clone();
			if(this.datos.isIncremental())
			{
				if(v.getFrecuencia() <= 1)
					v.setFrecuencia(1);
				else
					v.setFrecuencia(v.getFrecuencia());
			}
			this.simpliHashTerms.put(k, v);
		}
		else
		{
//System.out.println("\nk: " + k + ":::v NOT null\n");
			if(this.datos.isIncremental())
				v.setFrecuencia(v.getFrecuencia() + term.getFrecuencia());
				//v.setFrecuencia(v.getFrecuencia() + 1);
			this.simpliHashTerms.put(k, v);
		}
//System.out.println("\nht\n" + ht.toString() + "\n");
		//here v is not null
		if(v.tamanyo() > this.maxSizeElement)
		{
			this.maxSizeElement = v.tamanyo();
		}
		if(v.tamanyo() == this.datos.getPalabrasAprocesar())
		{
			this.hasElementWithSizePalabrasAprocesar = true;
		}
		//==============================
		//this.saveHash(ht, firstLetter);
		//
	}
	
	public cutext.prepro.HashTerms get(char fl)
	{
		if(Character.isLetter(fl)) //h a to z
		{
			cutext.prepro.SerHashTerms s = 
				new cutext.prepro.SerHashTerms("Recuperar",  this.datos.getRouteHashTerms() + "h" + String.valueOf(fl));
			return (cutext.prepro.HashTerms)s.recuperar();
		}
		//hother
		cutext.prepro.SerHashTerms s = 
			new cutext.prepro.SerHashTerms("Recuperar",  this.datos.getRouteHashTerms() + "hother");
		return (cutext.prepro.HashTerms)s.recuperar();
	}
	
	public void cvalue()
	{
		//set palabrasAprocesar := element with max size
		if(!hasElementWithSizePalabrasAprocesar)
		{
			this.datos.setPalabrasAprocesar(this.maxSizeElement);
		}
		//fast c-value
		FastCvalue fcv = new FastCvalue(this.datos);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("\nc-value...");
		}
		fcv.exec();
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("finished c-value");
		}
	}
	
	

	public Frases cvalue(ArrayFrases corpusFsw)
	{
		//contraer ArrayFrases: convertirlo en Frases (1 por cada grupo)
		Frases corpusComp = corpusFsw.contraer();

		//c-value
		Cvalue cv = new Cvalue(corpusComp);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("\nc-value...");
		}
		//cv.setCvalue();
		cv.setCvalue(this.datos);
		Frases listaCvalue = cv.getLista();
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("finished c-value");
		}
//System.out.println("listaCvalue\n" + listaCvalue.aString());

		return listaCvalue;
	}

	public ArrayFrases ncvalue(Frase corpus, ArrayFrases corpusFsw, Frases listaCvalue, StopWords sw)
	{
		//expandir ArrayFrases: asignar mismo cvalue a cada miembro del mismo grupo
		ArrayFrases corpusExp = corpusFsw.expandir(listaCvalue);
//System.out.println("corpusExp - Cvalue\n" + corpusExp.aString());


		//ncvalue: utiliza el corpus sin filtrar
		NCvalue ncv = new NCvalue(this.datos, corpus, corpusExp, sw);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("\nnc-value...");
		}
		ncv.setNCvalue();
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("finished nc-value");
		}

		return ncv.getTerminos();
	}

	public void guardar(ArrayFrases resultado)
	{
		//cutext.eval.ARNCvalue ar = new cutext.eval.ARNCvalue("Guardar");
		//cutext.eval.ARNCvalue ar = new cutext.eval.ARNCvalue("Guardar", ".." + Estaticos.FILE_SEP + "out" + Estaticos.FILE_SEP + "resul" + Estaticos.FILE_SEP "terms_background2_terminesp.ser");
		//cutext.eval.ARNCvalue ar = new cutext.eval.ARNCvalue("Guardar", ".." + Estaticos.FILE_SEP + "out" + Estaticos.FILE_SEP + "resul" + Estaticos.FILE_SEP + "terms_terminesp_" + DatosEntrada.out + ".ser");
		//cutext.eval.ARNCvalue ar = new cutext.eval.ARNCvalue("Guardar", "terms_terminesp_" + DatosEntrada.out + ".ser");
		
		//cutext.eval.ARNCvalue ar = new cutext.eval.ARNCvalue("Guardar", "terms_cutext_");
		//ar.guardar(resultado);
	}
	
	
	
	//this method is not used, it is maintained just in case
	public void saveHash(cutext.prepro.HashTerms ht, char firstLetter)
	{
		String fl = String.valueOf(firstLetter);
		if(!Character.isLetter(firstLetter))
			fl = "other";
		
		//cutext.prepro.SerHashTerms s = 
			//new cutext.prepro.SerHashTerms("Guardar", this.datos.getRouteHashTerms() + "h" + String.valueOf(firstLetter));
		cutext.prepro.SerHashTerms s = 
			new cutext.prepro.SerHashTerms("Guardar", this.datos.getRouteHashTerms() + "h" + fl);
		s.guardar((cutext.prepro.HashTerms)ht);
	}
	

	
	
	
	
	
	
	
	//generate out files
	/******************************** 

	public void crearFicheroSalida(String stringTerminos, int numT, int ifile)
	{
		//ARDatosEntrada arde = new ARDatosEntrada("Recuperar", DatosEntrada.N);
		//DatosEntrada de = arde.recuperar();

		DatosEntrada de = this.datos;

		if(!datos.isExecWithoutCvalue())
			this.stringSalida += "\n\n\n===== " + String.valueOf(numT) + " TERMS (sorted by c-value) =====\n\n\n" + stringTerminos;
		else
			this.stringSalida += "\n\n\n===== " + String.valueOf(numT) + " TERMS (without c-value) =====\n\n\n" + stringTerminos;

		String directorio = de.getDirectorioInformacionSalida();
		String fichero = "Cutext_" + de.getIdioma() + "_" + ifile + "_" + DatosEntrada.out;
		String extension = ".dat";
		Fichero f = new Fichero(directorio, fichero + extension);
		f.escribir(this.stringSalida);
		f.cerrar();
		if(DatosEntrada.dison)
			System.out.println("\n\n\n....... result in: " + directorio + Estaticos.FILE_SEP + fichero + extension);
	}

	public void crearFicheroSalida(Frases terms)
	{
		DatosEntrada de = this.datos;
		//String rute = de.getDirectorioIntermedio() + Estaticos.FILE_SEP + de.getFicheroTagger();
		//List<String> listLines = FileToString.asStringList(rute, StandardCharsets.UTF_8);

		//this.stringSalida += listLines.get(0);


		if(!datos.isExecWithoutCvalue())
			this.stringSalida += "\n\n\n===== " + String.valueOf(terms.tamanyo()) + " TERMS (sorted by c-value) =====\n\n\n" + terms.aString();
		else
			this.stringSalida += "\n\n\n===== " + String.valueOf(terms.tamanyo()) + " TERMS (without c-value) =====\n\n\n" + terms.aString();


		String directorio = de.getDirectorioInformacionSalida();
		String fichero = "Cutext_" + de.getIdioma() + "_" + DatosEntrada.out;
		String extension = ".dat";
		Fichero f = new Fichero(directorio, fichero + extension);
		f.escribir(this.stringSalida);
		f.cerrar();
		if(DatosEntrada.dison)
			System.out.println("\n\n\n....... result in: " + directorio + Estaticos.FILE_SEP + fichero + extension);
	}

	public void crearFicheroSalida(ArrayFrases terminos)
	{
		//ARDatosEntrada arde = new ARDatosEntrada("Recuperar", DatosEntrada.N);
		//DatosEntrada de = arde.recuperar();

		DatosEntrada de = this.datos;

		String stringTerminos = terminos.contraer().aStringLineal();

		this.stringSalida += "\n\n\n=====TERMS=====\n\n\n" + stringTerminos;

		String directorio = de.getDirectorioInformacionSalida();
		String fichero = "Cutext_" + de.getIdioma() + "_" + DatosEntrada.out;
		String extension = ".dat";
		Fichero f = new Fichero(directorio, fichero + extension);
		f.escribir(this.stringSalida);
		f.cerrar();
	}

	public void crearJSON(Frases terminos)
	{
		String directorio = this.datos.getDirectorioInformacionSalida();
		String fichero = "terms_" + this.datos.getIdioma() + "_" + DatosEntrada.out;
		String extension = ".json";
		Fichero f = new Fichero(directorio, fichero + extension);

		f.escribir("{\n" + "\t\"" + fichero + "\"" + ":\n\t[\n");

		for(int i = 0; i < terminos.tamanyo(); i++)
		{
			Frase termino = terminos.getFrase(i);
			String term = "";
			for(int j = 0; j < termino.tamanyo(); j++)
			{
				Token token = termino.getToken(j);
				term += token.getPalabra() + " ";
			}
			term = term.trim();

			f.escribir("\t\t{\n");
			f.escribir("\t\t\t\"term" + "\":" + " \"");
			f.escribir(term + "\",\n");
			f.escribir("\t\t\t\"frecuency" + "\":" + " \"");
			f.escribir(termino.getFrecuencia() + "\",\n");
			f.escribir("\t\t\t\"c-value" + "\":" + " \"");
			f.escribir(termino.getCvalue() + "\"\n");
			if(i == terminos.tamanyo() - 1)
				f.escribir("\t\t}\n");
			else
				f.escribir("\t\t},\n");
		}

		f.escribir("\t]\n}");

		f.cerrar();

		if(DatosEntrada.dison)
			System.out.println("\n\n\n....... result in json format in: " + directorio + Estaticos.FILE_SEP + fichero + extension);
	}

	public void crearBioC(Frases terminos, Frase corpus)
	{
		String directorio = this.datos.getDirectorioInformacionSalida();
		String fichero = "terms_" + this.datos.getIdioma() + "_" + DatosEntrada.out;
		String extension = ".xml";
		Fichero f = new Fichero(directorio, fichero + extension);

		//date
		Calendar fecha = new GregorianCalendar();

		String y = String.valueOf(fecha.get(Calendar.YEAR));
	        String m = String.valueOf(fecha.get(Calendar.MONTH) + 1);
        	String d = String.valueOf(fecha.get(Calendar.DAY_OF_MONTH));
		if(m.length() == 1)
			m = "0" + m;
		if(d.length() == 1)
			d = "0" + d;

		f.escribir("<?xml version='1.0' encoding='UTF-8'?>");
		f.escribir("<!DOCTYPE collection SYSTEM \"BioC.dtd\">");
		f.escribir("<collection>");
		f.escribir("<source>Unknown</source>");
		f.escribir("<date>" + y+m+d+ "</date>");
		f.escribir("<key>nothing.key</key>");
		f.escribir("<document>");
		f.escribir("<id>1</id>");
		f.escribir("<passage>");
		f.escribir("<infon key=\"type\">paragraph</infon>");
		f.escribir("<offset>0</offset>");
		f.escribir("<text>");
		//f.escribir(corpus.wordsToString());
		f.escribir(this.datos.getFileToString());
		f.escribir("</text>");
		//terms
		for(int i = 0; i < terminos.tamanyo(); i++)
		{
			Frase termino = terminos.getFrase(i);
			String termText = "";
			int termLength = 0;
			for(int j = 0; j < termino.tamanyo(); j++)
			{
				Token token = termino.getToken(j);
				termText += token.getPalabra() + " ";
				termLength += token.getPalabra().length();
			}
			termText = termText.trim();
			termLength += termino.tamanyo() - 1; //sumar los espacios entre las palabras

			f.escribir("<annotation id = \"0\">");
			f.escribir("<infon key = \"type\">Term</infon>");
			f.escribir("<infon key = \"cvalue\">" + termino.getCvalue() + "</infon>");
			f.escribir("<infon key = \"frecuency\">" + termino.getFrecuencia() + "</infon>");
			f.escribir("<location offset = \"" + termino.getToken(0).getOffset() + "\" length = \"" + String.valueOf(termLength) + "\" />");
			f.escribir("<text>" + termText + "</text>");
			f.escribir("</annotation>");
		}
		f.escribir("</passage>");
		f.escribir("</document>");
		f.escribir("</collection>");

		f.cerrar();

		if(DatosEntrada.dison)
			System.out.println("\n\n\n....... result in BioC format in: " + directorio + Estaticos.FILE_SEP + fichero + extension);
	}
	
	*****************************************************************/


}



































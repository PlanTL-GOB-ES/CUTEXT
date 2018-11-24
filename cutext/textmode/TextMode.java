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
 


/****************************************************************************
 Interfaz de Texto
****************************************************************************/






package cutext.textmode;




import java.io.*;
import java.util.*;


import cutext.prepro.*;
import cutext.main.*;
import cutext.util.*;



public class TextMode
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	private DatosEntrada datos = new DatosEntrada();

	private static final String HELP = "-help";
	private static final String DISON = "-displayon";
	private static final String POS_TAGGER = "-postagger";
	private static final String LANGUAGE = "-language";
	private static final String FREC_T = "-frecT";
	private static final String CVALUE_T = "-cvalueT";
	private static final String BIOC = "-bioc";
	private static final String JSON = "-json";
	private static final String CONV = "-convert";
	private static final String WCV = "-withoutcvalue";
	private static final String INC = "-incremental";
	private static final String GTF = "-generateTextFile";
	//routes hashTerms
	private static final String RHT = "-routeHashTerms";
	private static final String RTFHT = "-routeTextFileHashTerms";
	//route input
	private static final String INFILE = "-inputFile";
	//route output
	private static final String OUTFILE = "-outputFile";
	//config files
	private static final String RCF = "-routeconfigfiles";
	private static final String RITT = "-routeinterntt";
	
	//if this parameter is changed then the properties file will be read and the parameters will be loaded through it
	private static final String RPROP = "-routeProperties";
	
	private static final String DELFILES = "-deleteFiles";
	//the files contained in these folders will be deleted at the beginning
	//postagger
	private static final String DELPS = "-deletePosSer";
	private static final String DELPT = "-deletePosText";
	private static final String DELPIO = "-deletePosInternOut";
	private static final String DELPII = "-deletePosInternIn";
	private static final String DELPIX = "-deletePosInternX";
	private static final String DELPO = "-deletePosOutput";
	//out intern
	private static final String DELOT = "-deleteOutSer";
	private static final String DELOS = "-deleteOutText";
	private static final String DELIO = "-deleteInternOut";
	private static final String DELII = "-deleteInternIn";
	private static final String DELIX = "-deleteInternX";
	


	String routeProperties = "../properties/cutext.properties";
	boolean propertiesChanged = false;
	boolean lan = false;
	//
	boolean deleteFiles = true;
	String deletePosSer = "../postagger/serHashTerms/";
	String deletePosText = "../postagger/fileTextHashTerms/";
	String deletePosInternOut = "../postagger/intern/TT/out/";
	String deletePosInternIn = "../postagger/intern/TT/in/";
	String deletePosInternX = "../postagger/intern/TT/x/";
	String deletePosOutput = "../postagger/output/";
	//out,intern
	String deleteOutSer = "../out/serHashTerms/";
	String deleteOutText = "../out/fileTextHashTerms/";
	String deleteInternOut = "../intern/TT/out/";
	String deleteInternIn = "../intern/TT/in/";
	String deleteInternX = "../intern/TT/x/";
	
	
	public TextMode(String args[])
	{
		startTextMode(args);
	}


	public TextMode(String args[], DatosEntrada datos)
	{
		this.routeProperties = this.routeProperties.replace("/", Estaticos.FILE_SEP);
		this.routeProperties = this.routeProperties.replace("\\", Estaticos.FILE_SEP);
		//
		// intern postagger
/*
		this.deletePosSer = this.deletePosSer.replace("/", Estaticos.FILE_SEP);
		this.deletePosSer = this.deletePosSer.replace("\\", Estaticos.FILE_SEP);
		this.deletePosText = this.deletePosText.replace("/", Estaticos.FILE_SEP);
		this.deletePosText = this.deletePosText.replace("\\", Estaticos.FILE_SEP);
		this.deletePosInternOut = this.deletePosInternOut.replace("/", Estaticos.FILE_SEP);
		this.deletePosInternOut = this.deletePosInternOut.replace("\\", Estaticos.FILE_SEP);
		this.deletePosInternIn = this.deletePosInternIn.replace("/", Estaticos.FILE_SEP);
		this.deletePosInternIn = this.deletePosInternIn.replace("\\", Estaticos.FILE_SEP);
		this.deletePosInternX = this.deletePosInternX.replace("/", Estaticos.FILE_SEP);
		this.deletePosInternX = this.deletePosInternX.replace("\\", Estaticos.FILE_SEP);
		this.deletePosOutput = this.deletePosOutput.replace("/", Estaticos.FILE_SEP);
		this.deletePosOutput = this.deletePosOutput.replace("\\", Estaticos.FILE_SEP);
*/
		// out
		this.deleteOutSer = this.deleteOutSer.replace("/", Estaticos.FILE_SEP);
		this.deleteOutSer = this.deleteOutSer.replace("\\", Estaticos.FILE_SEP);
		this.deleteOutText = this.deleteOutText.replace("/", Estaticos.FILE_SEP);
		this.deleteOutText = this.deleteOutText.replace("\\", Estaticos.FILE_SEP);
		this.deleteInternOut = this.deleteInternOut.replace("/", Estaticos.FILE_SEP);
		this.deleteInternOut = this.deleteInternOut.replace("\\", Estaticos.FILE_SEP);
		this.deleteInternIn = this.deleteInternIn.replace("/", Estaticos.FILE_SEP);
		this.deleteInternIn = this.deleteInternIn.replace("\\", Estaticos.FILE_SEP);
		this.deleteInternX = this.deleteInternX.replace("/", Estaticos.FILE_SEP);
		this.deleteInternX = this.deleteInternX.replace("\\", Estaticos.FILE_SEP);
		//
		this.datos = datos;
		startTextMode(args);
	}

	/**
	* Infamous main method.
	*
	* @param args Command line arguments, should be one and a reference to a file.
	*/
	public static void main(String args[])
	{
		TextMode tm = new TextMode(args);
	}


	/**
	* Starts the text mode.
	*  
	* @param args the commandline arguments.
	*/
	public void startTextMode(String[] args)
	{
		
		boolean dison = false;
		String inputFile = null;
		String outputFile = null;
		

		for(int i = 1; i < args.length; i++) //begins in 1 because args[0] == "-TM"
		{
			if(args[i].equals(HELP))
			{
				usage();
			}
			//
			//properties file changed. Read and load parameters from cutext.properties 
			else if(args[i].equals(RPROP))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String rp = args[i].replace("/", Estaticos.FILE_SEP);
				rp = rp.replace("\\", Estaticos.FILE_SEP);
				this.routeProperties = rp;
				this.datos.setRouteProperties(this.routeProperties);
				this.propertiesChanged = true;
			}
			//
			else if(args[i].equals(DISON))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				DatosEntrada.dison = Boolean.parseBoolean(args[i].toLowerCase());
				dison = true;
			}
			//
			else if(args[i].equals(POS_TAGGER))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setPosTagger(args[i]);
			}
			else if(args[i].equals(LANGUAGE))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setIdioma(args[i]);
				this.lan = true;
			}
			else if(args[i].equals(FREC_T))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setUmbralFrecuencia(Integer.parseInt(args[i]));
			}
			else if(args[i].equals(CVALUE_T))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setUmbralCvalue(Double.parseDouble(args[i]));
			}
			else if(args[i].equals(BIOC))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setOutBioC(Boolean.parseBoolean(args[i].toLowerCase()));
			}
			else if(args[i].equals(JSON))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setOutJson(Boolean.parseBoolean(args[i].toLowerCase()));
			}
			else if(args[i].equals(CONV))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setConvertFileToLowerCase(Boolean.parseBoolean(args[i].toLowerCase()));
			}
			else if(args[i].equals(WCV))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setExecWithoutCvalue(Boolean.parseBoolean(args[i].toLowerCase()));
			}
			else if(args[i].equals(INC))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setIncremental(Boolean.parseBoolean(args[i].toLowerCase()));
			}
			else if(args[i].equals(GTF))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setGenerateTextFile(Boolean.parseBoolean(args[i].toLowerCase()));
			}
			//routes
			else if(args[i].equals(RHT))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setRouteHashTerms(args[i]);
			}
			else if(args[i].equals(RTFHT))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				datos.setRouteTextFileHashTerms(args[i]);
			}
			//delete files
			//flag
			else if(args[i].equals(DELFILES))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.deleteFiles = Boolean.parseBoolean(args[i].toLowerCase());
			}
			//files to delete
			else if(args[i].equals(DELPS))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deletePosSer = d;
			}
			else if(args[i].equals(DELPT))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deletePosText = d;
			}
			else if(args[i].equals(DELPIO))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deletePosInternOut = d;
			}
			else if(args[i].equals(DELPII))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deletePosInternIn = d;
			}
			else if(args[i].equals(DELPIX))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deletePosInternX = d;
			}
			else if(args[i].equals(DELPO))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deletePosOutput = d;
			}
			else if(args[i].equals(DELOT))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deleteOutSer = d;
			}
			else if(args[i].equals(DELOS))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deleteOutText = d;
			}
			else if(args[i].equals(DELIO))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deleteInternOut = d;
			}
			else if(args[i].equals(DELII))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deleteInternIn = d;
			}
			else if(args[i].equals(DELIX))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String d = args[i].replace("/", Estaticos.FILE_SEP);
				d = d.replace("\\", Estaticos.FILE_SEP);
				this.deleteInternX = d;
			}
			//config files
			else if(args[i].equals(RCF))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String rcf = args[i].replace("/", Estaticos.FILE_SEP);
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
			else if(args[i].equals(RITT))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				String ritt = args[i].replace("/", Estaticos.FILE_SEP);
				ritt = ritt.replace("\\", Estaticos.FILE_SEP);
				datos.setDirectorioEntrada(ritt + "in");
				datos.setDirectorioIntermedio(ritt + "x");
				datos.setDirectorioSalida(ritt + "out");
			}
			//
			else if(args[i].equals(INFILE))
			{
				//if(inputFile == null)
				//{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				inputFile = args[i];
				String onlyDir = inputFile.substring(0, inputFile.lastIndexOf(Estaticos.FILE_SEP));
				String onlyFile = inputFile.substring(inputFile.lastIndexOf(Estaticos.FILE_SEP) + 1, inputFile.length());
				datos.setDirectorioEntrada(onlyDir);
				ArrayList al = new ArrayList();
				al.add((String)onlyFile);
				datos.setListaFicheros(al);
				//}
				//else if(outputFile == null)
				//{
				//	outputFile = args[i];
				//	datos.setDirectorioInformacionSalida(outputFile);
				//}
			}
			else if(args[i].equals(OUTFILE))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				outputFile = args[i];
				datos.setDirectorioInformacionSalida(outputFile);
			}
			//
			else //other parameter
			{
				System.err.println("The parameter " + args[i] + " is not among the possibilities offered");
				System.exit(0);
			}
		}
		if(this.propertiesChanged)
		{
			//loadCutextProperties(); //load parameters via properties file
			Estaticos.loadCutextProperties(this.datos, this.routeProperties, true);
			preprocesar();
		}
		else if(inputFile == null)
		{
			usage();
		}
		else
		{
			if(!dison)
			{
				DatosEntrada.dison = true;
			}
			if(!lan)
			{
				datos.setIdioma(datos.getIdioma());
			}
			//save DatosEntrada in memory
			//ARDatosEntrada arde = new ARDatosEntrada("Guardar", DatosEntrada.N);
			//arde.guardar(datos);
			
			//if(this.propertiesChanged) //load parameters via properties file
			//{
			//	loadCutextProperties();
			//}

			preprocesar();
		}
	}

	public void preprocesar()
	{
		if(this.deleteFiles)
		{
			if(DatosEntrada.dison)
				System.out.println("deleting files from previous executions ...");
			this.deleteFiles();
			
			StaticHashTerms.initializeHashTerms();
		}
		
		
		
		String stringSalida = "\n\n\n\t===== CUTEXT =====\n\n\n" + 
			"------- Input -------" + 
			"\n  From: File" +
			"\n  Language: " + datos.getIdioma() +
			"\n  POS tagger: " + datos.getPosTagger() +
			"\n  Frequency Threshold: " + datos.getUmbralFrecuencia() +
			"\n  C-Value Threshold: " + datos.getUmbralCvalue() +
			"\n  BioC output: " + datos.isOutBioC() +  
			"\n  JSON output: " + datos.isOutJson() +  
			"\n-------------------" + 
			"\n\n\n------- Time -------" + 
			"\nPOS Tagging Time: ";

		long initialTpos = System.nanoTime();


		Preprocesar p = new Preprocesar(datos.getListaFicheros(), this.datos);
		p.preprocesar();
		Frases frases = null;
		if(!datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("preprocessing...");
			frases = p.execTagger();
			if(DatosEntrada.dison)
				System.out.println("finished preprocess");
		}
		else
		{
			if(DatosEntrada.dison)
				System.out.println("\n\t===== CUTEXT will run in incremental mode =====\n");
		}

		long finalTpos = System.nanoTime() - initialTpos;
		Tiempo t = new Tiempo();
		t.conversion(finalTpos);

		stringSalida += t.aString();

		//ejecutar Cutext
		Principal principal = new Principal(null, datos, frases);
		principal.cutext(stringSalida, initialTpos);
	}
	
	
	public void deleteFiles()
	{
		Estaticos.deleteFiles(this.deletePosSer);
		Estaticos.deleteFiles(this.deletePosText);
		Estaticos.deleteFiles(this.deletePosInternOut);
		Estaticos.deleteFiles(this.deletePosInternIn);
		Estaticos.deleteFiles(this.deletePosInternX);
		Estaticos.deleteFiles(this.deletePosOutput);
		Estaticos.deleteFiles(this.deleteOutSer);
		Estaticos.deleteFiles(this.deleteOutText);
		Estaticos.deleteFiles(this.deleteInternOut);
		Estaticos.deleteFiles(this.deleteInternIn);
		Estaticos.deleteFiles(this.deleteInternX);
	}
	
	
	
	
	
	
	
	

	//This will print the usage requirements and exit.
	private static void usage()
	{
		String message = "Usage: java cutext.main.ExecCutext -TM [options] <inputfile> [output-text-file]\n"
				+ "\nOptions:\n"
				+ "  -help	: Show this message\n"
				//
				+ "  -displayon	<boolean>	: Show the messages at the standard output. Default TRUE (show)\n"
				//
				+ "  -postagger	<postagger>		: POS tagger to tagger the input file. TreeTagger (default) or GeniaTagger.\n"
				+ "  -language	<language>		: SPANISH (default) or ENGLISH, CATALAN, GALICIAN.\n"
				+ "  -frecT	<integer>		: Frecuency Threshold. Default 0.\n"
				+ "  -cvalueT	<double>		: C-Value Threshold. Default 0.0.\n"
				+ "  -bioc	<boolean>		: Create a BioC output. Default false.\n"
				+ "  -json	<boolean>		: Create a JSON output. Default false.\n"
				+ "  -convert	<boolean>		: If true then convert the input file into lower case. Default true.\n"
				+ "  -withoutcvalue	<boolean>		: If true then execute only the linguistic filter. Default false.\n"
				+ "  -incremental	<boolean>		: If true then execute one line of the file as a entire corpus. Default false.\n"
				+ "  -generateTextFile	<boolean>		: If true then create one file per hashTerms, from 'a' to 'z'. Default false.\n"
				//
				+ "  -routeHashTerms	<string>		: folder where you store the hash terms.\n"
				+ "  -routeTextFileHashTerms	<string>		: folder where you store the text file hash terms.\n"
				//
				+ "  -routeconfigfiles	<string>		: folder where store config files.\n"
				+ "  -routeinterntt	<string>		: temporary folder (tt).\n"
				//
				+ "  -inputFile	<string>		: The document to use\n"
				+ "  -outputFile	<string>		: The file to write the result to\n\n"
				//
				+ "  ======= the following files will be delete at the beginning, if flag \'deleteFiles\' is true =======\n\n"
				+ "  -deleteFiles	<boolean>		: If true delete the following files at the beginning. Default: TRUE\n"
				+ "  -deletePosSer	<string>		: Route folder hashTerms at postagger folder. Default: ../postagger/serHashTerms/\n"
				+ "  -deletePosText	<string>		: Route folder text at postagger folder. Default: ../postagger/fileTextHashTerms/\n"
				+ "  -deletePosInternOut	<string>		: Route folder intern/out at postagger folder. Default: ../postagger/intern/TT/out/\n"
				+ "  -deletePosInternIn	<string>		: Route folder intern/in at postagger folder. Default: ../postagger/intern/TT/in/\n"
				+ "  -deletePosInternX	<string>		: Route folder intern/x at postagger folder. Default: ../postagger/intern/TT/x/\n"
				+ "  -deletePosOutput	<string>		: Route folder output at postagger folder. Default: ../postagger/output/\n"
				//out, intern
				+ "  -deleteOutSer	<string>		: Route folder hashTerms at cutext folder. Default: ../out/serHashTerms/\n"
				+ "  -deleteOutText	<string>		: Route folder text at cutext folder. Default: ../out/fileTextHashTerms/\n"
				+ "  -deleteInternOut	<string>		: Route folder intern/out at cutext folder. Default: ../intern/TT/out/\n"
				+ "  -deleteInternIn	<string>		: Route folder intern/in at cutext folder. Default: ../intern/TT/in/\n"
				+ "  -deleteInternX	<string>		: Route folder intern/x at cutext folder. Default: ../intern/TT/x/";
		
		System.err.println(message);
		System.exit(1);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	


}

















































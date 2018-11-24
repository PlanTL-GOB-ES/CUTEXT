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
 


/***********************************************************************************************************************************************************
* Se encarga de: concatenar los ficheros de entrada, pasar el POS tagger a los ficheros, y crear el corpus (Frases) con las frecuencias iniciales
************************************************************************************************************************************************************/



package cutext.prepro;



import java.util.*;
import java.io.*;

import java.nio.charset.*;
import java.nio.file.*;



import cutext.util.*;



public class Preprocesar implements Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	//DatosEntrada datos = new DatosEntrada();
	DatosEntrada datos;
	ArrayList seleccionLista;

	public Preprocesar(ArrayList seleccionLista, DatosEntrada de)
	{
		//recuperar DatosEntrada y asignarlo
		//ARDatosEntrada arde = new ARDatosEntrada("Recuperar", DatosEntrada.N);
		//DatosEntrada de = arde.recuperar();
		this.datos = de;

		this.seleccionLista = seleccionLista;
	}




	public void preprocesar()
	{
		//fijar el nombre del fichero para el etiquetador: datos.getFicheroTagger() + DatosEntrada.out
		datos.setFicheroTagger(datos.getFicheroTagger() + "-" + DatosEntrada.out);


		//Preprocesar los ficheros de entrada
		ConcatenarFicheros cf = new ConcatenarFicheros(seleccionLista, datos.getDirectorioEntrada(), datos.getDirectorioIntermedio(), datos.getFicheroTagger());
		cf.concatenar();


		//Pasar el POS tagger
		//ejecutarEtiquetadorLexico();
/*
File f = new File(datos.getEtiquetadorLinux());
System.out.println("ruta etiquetador: " + f.getAbsolutePath());
//etiq = f.getAbsolutePath();
f = new File(datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger());
System.out.println("ruta fichero: " + f.getAbsolutePath() + "\n");
*/

		if(datos.isConvertFileToLowerCase())
		{
			// !!! convert file into lowerCase(): because Treetagger has problems with words in upper case
			try
			{
				String fstr = FileToString.oneLiner(datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger(), Charset.forName(StandardCharsets.UTF_8.name()));
				StringToFile.oneLiner(fstr.toLowerCase(), datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger());
			}
			catch(IOException e)
			{
				System.out.println("Error: " + e);
				e.printStackTrace();
			}
		}

	}

	public Frases execTagger()
	{
		//averiguar el sistema operativo
		String so = System.getProperty("os.name").toUpperCase();

		if(so.startsWith("WINDOWS"))
		{
			//create temp file
			String tempFile = "tempTaggerFile";
			try
			{
				String fstr = FileToString.oneLiner(datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger(), Charset.forName(StandardCharsets.UTF_8.name()));
				StringToFile.oneLiner(fstr, datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + tempFile);
			}
			catch(IOException e)
			{
				System.out.println("Error: " + e);
				e.printStackTrace();
			}
			cutext.postagger.Etiquetador.ejecutar(
							datos.getEtiquetador(), 
							datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + tempFile, 
							datos.getDirectorioSalida(), 
							datos.getFicheroTagger()
			);
		}
		else //Linux
		{
			cutext.postagger.Etiquetador.ejecutar(
								datos.getEtiquetadorLinux(), 
								datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger(), 
								datos.getDirectorioSalida(), 
								datos.getFicheroTagger()
			);
		}




		//crear el corpus de Frases
		//EntradaSalida es = new EntradaSalida(datos.getDirectorioSalida());
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("getting corpus...");
		}
		long tiempoESin = System.nanoTime();
		EntradaSalida es = new EntradaSalida(datos);
		Frases corpus = es.getCorpus(); //procesa el corpus, creando las Frases (sin frecuencias) de manera interna
		long tiempoES = System.nanoTime() - tiempoESin;
		Tiempo t = new Tiempo();
		t.conversion(tiempoES);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("...corpus obtained. Time:" + t.aString());
		}
//System.out.println(corpus.aString());

		//determinar las frecuencias y asignarlas
		//corpus = getFrecuencias(corpus);
//System.out.println(corpus.aString());

		return corpus;
	}



	public Frases execTagger(String rute)
	{
		//averiguar el sistema operativo
		String so = System.getProperty("os.name").toUpperCase();

		if(so.startsWith("WINDOWS"))
		{
			//rute contains the temp file
			cutext.postagger.Etiquetador.ejecutar(
							datos.getEtiquetador(), 
							rute, 
							datos.getDirectorioSalida(), 
							datos.getFicheroTagger()
			);
		}
		else //Linux
		{
			cutext.postagger.Etiquetador.ejecutar(
								datos.getEtiquetadorLinux(), 
								rute, 
								datos.getDirectorioSalida(), 
								datos.getFicheroTagger()
			);
		}



		//crear el corpus de Frases
		//EntradaSalida es = new EntradaSalida(datos.getDirectorioSalida());
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("getting corpus...");
		}
		long tiempoESin = System.nanoTime();
		EntradaSalida es = new EntradaSalida(datos);
		Frases corpus = es.getCorpus(); //procesa el corpus, creando las Frases (sin frecuencias) de manera interna
		long tiempoES = System.nanoTime() - tiempoESin;
		Tiempo t = new Tiempo();
		t.conversion(tiempoES);
		if(!this.datos.isIncremental())
		{
			if(DatosEntrada.dison)
				System.out.println("...corpus obtained. Time:" + t.aString());
		}
//System.out.println(corpus.aString());

		//determinar las frecuencias y asignarlas
		//corpus = getFrecuencias(corpus);
//System.out.println(corpus.aString());

		return corpus;
	}




	public synchronized void ejecutarEtiquetadorLexico()
	{
		Runtime aplicacion = Runtime.getRuntime();
	 	try
		{
			//averiguar el sistema operativo
			String so = System.getProperty("os.name").toUpperCase();
			
			if(so.startsWith("WINDOWS"))
			{
				Process tt = aplicacion.exec("cmd.exe /C " + datos.getEtiquetador() + " " + 
							datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger() + " " + 
							datos.getDirectorioSalida() + Estaticos.FILE_SEP + datos.getFicheroTagger());

				tt.waitFor(); //esperar hasta que acabe el etiquetador
			}
			else //suponer Linux
			{
				String arg[] = new String[2];
				arg[0] = datos.getEtiquetadorLinux();
				arg[1] = datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger();
				//tree-tagger-english ./entrada/TT/x/tagger.txt 
				Process tt = aplicacion.exec(arg);
				//Process tt = aplicacion.exec(arg);
				//Process tt = aplicacion.exec("tree-tagger-spanish" + " " + 
				//		datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger() + " > " + 
				//		datos.getDirectorioSalida() + Estaticos.FILE_SEP + datos.getFicheroTagger());

				BufferedReader stdInput = new BufferedReader(new 
					                 InputStreamReader(tt.getInputStream()));

				BufferedReader stdError = new BufferedReader(new 
					                 InputStreamReader(tt.getErrorStream()));

				Fichero ficheroSalida = new Fichero(datos.getDirectorioSalida(), datos.getFicheroTagger());
				String s = null;
				//read the output from the command
				while((s = stdInput.readLine()) != null)
				{
			        //System.out.println(s);
					ficheroSalida.escribir(s + "\n");
			    }
				ficheroSalida.cerrar();
            
				//read any errors from the attempted command
				//System.out.println("Here is the standard error of the command (if any):\n");
				//while ((s = stdError.readLine()) != null)
				//{
					//System.out.println(s);
				//}


				tt.waitFor(); //esperar hasta que acabe el etiquetador

			}
		}
		catch(Exception e)
		{
			System.err.println("ERROR to execute the POS Tagger");
			e.printStackTrace();
			System.exit(0);
		}
	}

	//Determina las frecuencias de los lemas del corpus y las devuelve
	public Frases getFrecuencias(Frases corpus)
	{
		//Frase palabrasProcesadas = new Frase();
		Frase lemasProcesados = new Frase();
		for(int i = 0; i < corpus.tamanyo(); i++)
		{
			Frase frase = corpus.getFrase(i);
			//calcular frecuencia para cada frase
			for(int j = 0; j < frase.tamanyo(); j++)
			{
				Token token = frase.getToken(j);
				//int indice = palabrasProcesadas.indicePalabra(token.getPalabra());
				int indice = lemasProcesados.indiceLema(token.getLema());
				if(indice == -1) //no esta
				{
					token.setFrecuencia(1);
					lemasProcesados.anyadir(token);
				}
				else
				{
					Token tokenProcesado = lemasProcesados.getToken(indice);
					tokenProcesado.setFrecuencia(tokenProcesado.getFrecuencia() + 1);
				}
			}
		}
		corpus.igualarFrecuencias(lemasProcesados);
		return corpus;
	}

	public boolean esta(String palabra, ArrayList palabrasProcesadas)
	{
		for(int i = 0; i < palabrasProcesadas.size(); i++)
		{
			String palabraProcesada = (String)palabrasProcesadas.get(i);
			if(palabraProcesada.equals(palabra))
				return true;
		}
		return false;
	}

	public void setFrecuencias(Frases frases, Frase corpus)
	{
		int indice = 0;
		for(int i = 0; i < frases.tamanyo(); i++)
		{
			Frase frase = frases.getFrase(i);
			//asignar la frecuencia por las palabras
			frase.setFrecuencia(corpus);
		}
	}










	public static void main(String args[])
	{
		Preprocesar p = new Preprocesar(null, null);

		System.out.println("preprocessing...");
		p.preprocesar();
		System.out.println("finished preprocess");
	}


}

























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
 



/******************************************************************************************
* Es la clase que manipula el corpus, es decir se encarga de la E/S. 
* Rellena el ArrayTerminos con objetos Termino.
******************************************************************************************/


package cutext.prepro;


import java.util.*;
import java.io.*;

import java.nio.charset.*;
import java.nio.file.*;


import cutext.util.*;
import cutext.filter.lin.Filtro;


public class EntradaSalida implements Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	Frases oraciones = new Frases();
	String directorioCorpus;
	transient File listaArchivos[];
	long puntero;
	transient RandomAccessFile raf;

	transient int indiceListaArchivos = 0;
	transient boolean fin = false;

	DatosEntrada datos;

	Frase todas = new Frase();// para asignar frecuencias

	int codigo = 0;

	//para los offsets
	String sArchivo = "";








	//public EntradaSalida(String directorioCorpus)
	public EntradaSalida(DatosEntrada datos)
	{
		this.datos = datos;
		this.sArchivo = fileToString(datos.getDirectorioIntermedio() + Estaticos.FILE_SEP + datos.getFicheroTagger());
		this.datos.setFileToString(sArchivo);
		puntero = 0;
		this.directorioCorpus = datos.getDirectorioSalida();
		File ruta = new File(this.directorioCorpus);
		File archivos[] = ruta.listFiles();
		int cont = 0;
		for(int i = 0; i < archivos.length; i++)
		{
			String path = archivos[i].getPath();
			if(!path.substring(path.length() - 1).equals("~"))
				cont++;
		}
		listaArchivos = new File[cont];
		int indice = 0;
		for(int i = 0; i < archivos.length; i++)
		{
			String path = archivos[i].getPath();
			if(!path.substring(path.length() - 1).equals("~"))
				listaArchivos[indice++] = archivos[i];
		}
	}




	public static String fileToString(String archivo)
	{
		String sArchivo = "";
		String linea = "";
		try
		{
			FileReader f = new FileReader(archivo);
			BufferedReader b = new BufferedReader(f);
			while((linea = b.readLine()) != null)
			{
				sArchivo += linea;
			}
		}
		catch(FileNotFoundException e)
		{
			System.err.println("File not found: " + e);
		}
		catch(IOException ioe)
		{
			System.err.println("IO Error: " + ioe);
		}
		return sArchivo;
	}







	public Frases getCorpus()
	{
		oraciones.limpiar(); //eliminar todas las Oraciones que hubiese anteriormente
		for(int i = 0; i < listaArchivos.length; i++)
		{ 
			//crearListadoOraciones(listaArchivos[i]);
			getFrases(listaArchivos[i]);
		}
		//guardar DatosEntrada con las listas de los indices, machacando el anterior
		//ARDatosEntrada arde = new ARDatosEntrada("Guardar", DatosEntrada.N);
		//arde.guardar(this.datos);


		return oraciones;
	}


	public void getFrases(File archivo)// throws IOException
	{
		Frase oracion = new Frase();
		String linea = "";
		String punto = ".";
		int indice = 0;
		int offset = 0;
		String rute = "";
		try
		{
			rute = archivo.getCanonicalPath();
		}
		catch(IOException e)
		{
			System.err.println("IO Error: " + e);
		}

		//List<String> listLines = FileToString.asStringList(rute, StandardCharsets.UTF_8);
		//treetagger generates files that depend on the operating system (linux = utf8, windows = ascii)
		//"juniversalchardet" is a library that detects the file charset, but it is a bit complex
		List<String> listLines = FileToString.asStringList(rute, Charset.defaultCharset());
		for(int i = 0; i < listLines.size(); i++)
		{
			linea = listLines.get(i);
			String[] result = linea.split("\\s");
			if(result.length < 3)// linea rara
				continue;
			String palabra = result[0];
			//--- treetagger
			String etiqueta = result[1];
			String lema = result[2];
			//--- geniatagger
			if(datos.getPosTagger().equals(Estaticos.GT))
			{
				etiqueta = result[2];
				lema = result[1];
			}
			if(lema.equals(Estaticos.U)) //coger la palabra
				lema = palabra;

			//offset
			offset = sArchivo.indexOf(palabra, offset);
			Token token = new Token(palabra, etiqueta, lema, indice, -1, 0.0, offset); //-1 = sin frecuencia, sin peso = 0.0
			//asignar offset
			//offset += palabra.length() + 1;

			//asignar frecuencia
			setFrecuencia(token);
			oracion.anyadir(indice, token);

			indice++;

			//asignar indices para filtro: una por delante
			asignarIndices(codigo, etiqueta);
			codigo++;

			if(palabra.equals(punto))//fin frase, anyadir a Frases
			{
				oracion.setLongitud(oracion.tamanyo());
				this.oraciones.anyadir((Frase)oracion.clone());
				oracion = new Frase();
				indice = 0;
			}
		}
		if(indice > 0)// fichero no acaba en .
		{
			oracion.setLongitud(oracion.tamanyo());
			this.oraciones.anyadir((Frase)oracion.clone());
		}
	}



	public void getFrasesAntiguo(File archivo)// throws FileNotFoundException, IOException
	{
		Frase oracion = new Frase();
		String linea = "";
		String punto = ".";
		int indice = 0;
		int offset = 0;


		try
		{
			FileReader f = new FileReader(archivo);
			BufferedReader b = new BufferedReader(f);
			while((linea = b.readLine()) != null)
			{
				String[] result = linea.split("\\s");
				if(result.length < 3)// linea rara
					continue;
				String palabra = result[0];
				//--- treetagger
				String etiqueta = result[1];
				String lema = result[2];
				//--- geniatagger
				if(datos.getPosTagger().equals("GeniaTagger"))
				{
					etiqueta = result[2];
					lema = result[1];
				}
				if(lema.equals(Estaticos.U)) //coger la palabra
					lema = palabra;

				//offset
				offset = sArchivo.indexOf(palabra, offset);
				Token token = new Token(palabra, etiqueta, lema, indice, -1, 0.0, offset); //-1 = sin frecuencia, sin peso = 0.0
				//asignar offset
				//offset += palabra.length() + 1;

				//asignar frecuencia
				setFrecuencia(token);
				oracion.anyadir(indice, token);

				indice++;

				//asignar indices para filtro: una por delante
				asignarIndices(codigo, etiqueta);
				codigo++;


				if(palabra.equals(punto))//fin frase, anyadir a Frases
				{
					oracion.setLongitud(oracion.tamanyo());
					this.oraciones.anyadir((Frase)oracion.clone());
					oracion = new Frase();
					indice = 0;
				}
			}
			if(indice > 0)// fichero no acaba en .
			{
				oracion.setLongitud(oracion.tamanyo());
				this.oraciones.anyadir((Frase)oracion.clone());
			}
		}
		catch(FileNotFoundException e)
		{
			System.err.println("File not found: " + e);
		}
		catch(IOException ioe)
		{
			System.err.println("IO Error: " + ioe);
		}
	}

	public void setFrecuencia(Token token)
	{
		int indice = -1;
		for(int i = 0; i < this.todas.tamanyo(); i++)
		{
			indice = this.todas.indiceLema(token.getLema(), indice+1);
			if(indice == -1) //no esta
			{
				token.setFrecuencia(1);
				this.todas.anyadir(token);
				break;
			}
			else
			{
				Token tokenProcesado = this.todas.getToken(indice);
				tokenProcesado.setFrecuencia(tokenProcesado.getFrecuencia() + 1);
			}
		}
	}

	public void asignarIndices(int codigo, String etiqueta)
	{
		//nombres, adjetivos, preposiciones, acronimos
		if(Filtro.esta(etiqueta, this.datos.getNombres()))
		{
			this.datos.setIndicesNombres(codigo);
		}
		if(Filtro.esta(etiqueta, this.datos.getNombresC()))
		{
			this.datos.setIndicesNombresC(codigo);
		}
		if(Filtro.esta(etiqueta, this.datos.getNombresP()))
		{
			this.datos.setIndicesNombresP(codigo);
		}
		else if(Filtro.esta(etiqueta, this.datos.getAdjetivos()))
		{
			this.datos.setIndicesAdjetivos(codigo);
		}
		else if(Filtro.esta(etiqueta, this.datos.getPreposiciones()))
		{
			this.datos.setIndicesPreposiciones(codigo);
		}
		else if(etiqueta.equals("ACRNM"))// acron. esp.
		{
			this.datos.setIndicesAcronimos(codigo);
		}
	}

/*

	//Anyade todas las oraciones que contiene un fichero al objeto ArrayList "oraciones"
	public void crearListadoOraciones(File nombreF)
	{
		FicheroLectura fl = new FicheroLectura(nombreF);
		do
		{
			Frase oracion = obtenerOracion(fl);
			if(oracion != null) //insertar la Oracion en el ArrayList
			{
				oraciones.anyadir(oracion);
			}

		}while(!fl.esFin());	
	}

*/



/*
	//Devuelve un objeto Oracion
	public Frase obtenerOracion(FicheroLectura fl)
	{
		Frase oracion = new Frase();
		File nombreF = fl.getFile();
		boolean fin = false;
		String linea = "";
		String punto = ".";
		int indice = 0;
		
		try
		{
			raf = new RandomAccessFile(nombreF, "r");	//solo lectura
			raf.seek(puntero);

			while(!fin) //salir cuando sea fin de frase
			{
				linea = raf.readLine();
//System.out.println("linea POS: " + linea);

				//comprobar si es fin de fichero
				if(linea == null)
				{
					fl.setFin(true);
					break;
				}

				//crear la palabra y la etiqueta, y guardarla en la Oracion
				String[] result = linea.split("\\s");
				if(result.length < 3)// linea rara
					continue;
				String palabra = result[0];
				String etiqueta = result[1];
				String lema = result[2];
				if(lema.equals(Estaticos.U)) //coger la palabra
					lema = palabra;
				//si idioma SPANISH coger el lemma
				//if(this.datos.getIdioma().equals("SPANISH"))
				//	palabra = result[2];

				
				Token token = new Token(palabra, etiqueta, lema, indice, -1, 0.0); //-1 = sin frecuencia, y sin peso = 0.0
				oracion.anyadir(indice, token);
				indice++;

				if(palabra.equals(punto))
					fin = true;
			}
			puntero = raf.getFilePointer();

			//si es fin fichero, situar el puntero al inicio
			if(linea == null)
				puntero = 0;

			raf.close();
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Fichero no encontrado: " + e);
		}
		catch(IOException ioe)
		{
			System.err.println("Error de entrada/salida: " + ioe);
		}

		if(indice > 0)
		{
			oracion.setLongitud(oracion.tamanyo());
			return oracion;
		}
		return null;
	}
*/





	public String getDirectorioCorpus()
	{
		return directorioCorpus;
	}

	public boolean fin()
	{
		return fin;
	}


}
















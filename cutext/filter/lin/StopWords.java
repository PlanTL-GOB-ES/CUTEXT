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
 



/*****************************************************************************************
Filtra a partir de la lista de las stop words
******************************************************************************************/




package cutext.filter.lin;





import java.util.*;
import java.io.*;

import cutext.prepro.*;
import cutext.util.*;





public class StopWords implements Cloneable, Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;

	StopInterface stop = null;

	DatosEntrada datos;

	public StopWords clone()
	{
		StopWords s = null;
		try
		{
			s = (StopWords)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}

		return s;
	}

	public StopWords(DatosEntrada datos)
	{
		this.datos = datos;

		//select stop by language
		if(this.datos.getIdioma().equals(DatosEntrada.SPA))
			this.stop = new StopWordsSpa(datos);
		else if(this.datos.getIdioma().equals(DatosEntrada.ENG))
			this.stop = new StopWordsEng(datos);
		else if(this.datos.getIdioma().equals(DatosEntrada.CAT))
			this.stop = new StopWordsCat(datos);
		else //if(this.datos.getIdioma().equals(DatosEntrada.GAL))
			this.stop = new StopWordsGal(datos);
	}




	//------------- Stop List --------------------


	public ArrayFrases stopList(ArrayFrases terminos)
	{
		ArrayFrases ss = new ArrayFrases();
		for(int i = 0; i < terminos.tamanyo(); i++)
		{
			Frases t = terminos.getFrases(i);
			Frases tss = stopList(t);
			if(tss != null)
			{
				ss.anyadir(tss);
			}
		}
		return ss;
	}

	public Frases stopList(Frases terminos)
	{
		Frases ss = new Frases();
		ss.setFrecuencia(terminos.getFrecuencia());
		
		for(int i = 0; i < terminos.tamanyo(); i++)
		{
			Frase t = terminos.getFrase(i);
			Frase tss = this.stop.stopList(t);
			if(tss != null)
			{
				ss.anyadir(tss);
			}
		}
		if(ss.vacias())
			return null;
		return ss;
	}








	//Extraer los términos (en minúsculas) de 1/10 del corpus que aparecen frecuentemente
	//public String[] extraerLista(String corpus)
	public Hashtable extraerLista(String corpus)
	{
		return this.datos.getListaStopWords(); //--- ADD CODE HERE
	}

	
	public static boolean esta(String token, Hashtable listaStopWords)
	{
		String v = (String)listaStopWords.get((String)token.toLowerCase());
		return (v != null);
	}




}























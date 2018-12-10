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
 




package cutext.filter.lin;



import java.util.*;
import java.io.*;

import cutext.prepro.*;



public class FilterEng implements FilterInterface, Cloneable, Serializable
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	Frase corpus;

	DatosEntrada datos;

	ArrayFrases af;
	Hashtable<String,Integer> entradasArrayFrases;// k=lemaPOS, v=pos. en ArrayFrases
	String sep = "---";



	public FilterEng clone()
	{
		FilterEng f = null;
		try
		{
			f = (FilterEng)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		f.corpus = (Frase)f.corpus.clone();

		return f;
	}

	public FilterEng()
	{
	}


	public FilterEng(Frase corpus, DatosEntrada datos)
	{
		this.datos = datos;
		inicializar();
		this.corpus = corpus;
	}

	public void inicializar()
	{
		corpus = null;
		this.af = new ArrayFrases();
		this.entradasArrayFrases = new Hashtable<String,Integer>();
	}






	public ArrayFrases filtrar()
	{
		Hashtable<String,Frase> fraseFiltrada = new Hashtable<String,Frase>();// k=Frase.aString(), v=Frase

		filtrarEN1(fraseFiltrada);
		filtrarEN2(fraseFiltrada);
		Frases corpusFiltro3 = filtrarEN3();
		anyadir(fraseFiltrada, corpusFiltro3);

		Frases fs = getFrases(fraseFiltrada);
		return this.af;
	}




	/*=========================
		FILTERS
	===========================*/




	//--------------------- con Hashtables
	//N+N
	public void filtrarEN1(Hashtable filtro1)
	{
		int inicio = -1;
		int fin = -1;

		for(int i = 0; i < corpus.tamanyo(); i++)
		{
			//Token token = corpus.getToken(i);
			if((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres())) && (i+1 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(i+1).getPos(), this.datos.getNombres())))
			{
				inicio = corpus.getToken(i).getCodigo(); //inicio = i;
				fin = corpus.getToken(i+1).getCodigo(); //fin = i + 1;
				while((i < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres()))) //marcar el fin y seguir
				{
					fin = corpus.getToken(i).getCodigo(); //fin = i;
					i++;
				}
				//guardar el inicio y el fin
				Frase temp = corpus.subFrase(inicio, fin);
				//una por delante con el AF
				adelantarAF(temp, filtro1);
				filtro1.put(temp.aString(), temp);
			}
		}
	}

	//(ADJ|N)+N
	public void filtrarEN2(Hashtable filtro2)
	{
		int inicio = -1;
		int fin = -1;
		for(int i = 0; i < corpus.tamanyo(); i++)
		{
			if((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres())) || (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getAdjetivos())))
			{
				int posibleInicio = corpus.getToken(i).getCodigo(); //int posibleInicio = i;
				while((i < corpus.tamanyo()) && ((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres())) || (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getAdjetivos()))))
				{
					i++;
				}
				//acaba en Nombre
				if(Filtro.esta(corpus.getToken(i-1).getPos(), this.datos.getNombres()) && (posibleInicio < i-1))
				{
					inicio = posibleInicio;
					fin = corpus.getToken(i-1).getCodigo(); //fin = i - 1;
					//guardar el inicio y el fin
					Frase temp = corpus.subFrase(inicio, fin);
					//una por delante con el AF
					adelantarAF(temp, filtro2);
					filtro2.put(temp.aString(), temp);
				}
			}
		}
	}


	//filtrarEN3() con Frases
	//( (ADJ|N)+  ((ADJ|N)* (N IN)?)  (ADJ|N)* )N
	//evalua la expresion regular desde el final hasta el principio
	public Frases filtrarEN3()
	{
		Frases filtro3 = new Frases();
		int inicio = -1;
		int fin = -1;
		for(int i = 0; i < corpus.tamanyo(); i++)
		{
			if((i < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres()))) //N
			{
				while((i < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres()))) //N
				{
					i++;
				}
				if(i >= corpus.tamanyo()) //fin corpus
				{
					filtro3 = eliminarSubFrases(filtro3);
					return filtro3;
				}
				i--;
				int posibleFin = corpus.getToken(i).getCodigo();
				while((i > 0) && ((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres())) || (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getAdjetivos())))) //(ADJ|N)*
				{
					i--;
				}
				if(i <= 0) //inicio corpus
				{
					i = posibleFin;
					continue;
				}
				if((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getPreposiciones())) && (i-1 > 0) && (Filtro.esta(corpus.getToken(i-1).getPos(), this.datos.getNombres()))) //(N IN)?
				{
					i -= 2; //posicionarse en la anterior a N
				}
				else
				{
					i = posibleFin;
					continue;
				}
				if((i >= 0) && ((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getAdjetivos())) || (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres())))) //(ADJ|N)
				{
					while((i >= 0) && ((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getAdjetivos())) || (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres())))) //(ADJ|N)+
					{
						i--;
					}
					i++;
					inicio = corpus.getToken(i).getCodigo(); //inicio = i;
					fin = posibleFin;
					//guardar el inicio y el fin
					Frase temp = corpus.subFrase(inicio, fin);
					filtro3.anyadir(temp);
				}
				i = posibleFin;
			}
		}
		filtro3 = eliminarSubFrases(filtro3);
		return filtro3;
	}

	public Frases eliminarSubFrases(Frases frases)
	{
		Frases nuevas = new Frases();
		boolean incluir = true;
		for(int i = 0; i < frases.tamanyo(); i++)
		{
			Frase subfrase = frases.getFrase(i);
			for(int j = 0; j < frases.tamanyo(); j++)
			{
				if(i == j) //mismo elemento
					continue;
				Frase frase = frases.getFrase(j);
				if(subfrase.esSubFrase(frase)) //no incluir
				{
					incluir = false;
					break;
				}
			}
			if(incluir)
				nuevas.anyadir((Frase)subfrase.clone());
			incluir = true;
		}
		return nuevas;
	}

	public void anyadir(Hashtable h, Frases fs)
	{
		for(int i = 0; i < fs.tamanyo(); i++)
		{
			Frase f = fs.getFrase(i);
			//una por delante con el AF
			adelantarAF(f, h);
			h.put(f.aString(), f);
		}
	}







	public void adelantarAF(Frase temp, Hashtable filtro)
	{
		Frase ff = (Frase)filtro.get((String)temp.aString());
		if(ff == null)
		{
			String lp = temp.lemasAstring() + sep + temp.posAstring();
			Integer p = this.entradasArrayFrases.get(lp);
			if(p == null)
			{
				Frases elem = new Frases();
				elem.anyadir(temp);
				this.af.anyadir(this.entradasArrayFrases.size(), elem);
				this.entradasArrayFrases.put(lp, this.entradasArrayFrases.size());
			}
			else
			{
				Frases elem = this.af.getFrases(p.intValue());
				elem.anyadir(temp);
			}
		}
	}






	public Frases getFrases(Hashtable h)
	{
		Frases frases = new Frases();
		Enumeration<Frase> enumeration = h.elements();
		while (enumeration.hasMoreElements())
		{
			frases.anyadir((Frase)enumeration.nextElement());
		}
		return frases;
	}







	public static void main(String args[])
	{

	}






}



































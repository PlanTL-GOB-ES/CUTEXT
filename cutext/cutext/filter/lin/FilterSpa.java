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



public class FilterSpa implements FilterInterface, Cloneable, Serializable
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	Frase corpus;

	DatosEntrada datos;

	ArrayFrases af;
	Hashtable<String,Integer> entradasArrayFrases;// k=lemaPOS, v=pos. en ArrayFrases
	String sep = "---";



	public FilterSpa clone()
	{
		FilterSpa f = null;
		try
		{
			f = (FilterSpa)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		f.corpus = (Frase)f.corpus.clone();

		return f;
	}

	public FilterSpa()
	{
	}


	public FilterSpa(Frase corpus, DatosEntrada datos)
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

		closeFilter(fraseFiltrada);
		openFilter(fraseFiltrada);

		Frases fs = getFrases(fraseFiltrada);
		return this.af;
	}




	/*=========================
		FILTERS
	===========================*/




	//------------------------------------------- close filter


	//--------------------- con Hashtable
	public void closeFilter(Hashtable filtro)
	{
		close1(filtro);
		close2(filtro);
		close3(filtro);
	}

	//N ADJ
	public void close1(Hashtable filtro)
	{
		for(int i = 0; i < this.datos.tamIndicesNombresC(); i++)
		{
			int iN = this.datos.getIndicesNombresC(i);
			if(iN == -1)// no hay nombres
				return;
			if((iN+1 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+1).getPos(), this.datos.getAdjetivos())))
			{
				Frase temp = corpus.subFrase(iN, (iN+1));
				//una por delante con el AF
				adelantarAF(temp, filtro);
				filtro.put(temp.aString(), temp);
			}
		}
	}

	//N de ADJ
	public void close2(Hashtable filtro)
	{
		for(int i = 0; i < this.datos.tamIndicesNombresC(); i++)
		{
			int iN = this.datos.getIndicesNombresC(i);
			if(iN == -1)// no hay nombres
				return;
			boolean bP = ((iN+1 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+1).getPos(), this.datos.getPreposiciones())) && (corpus.getToken(iN+1).getPalabra().equals("de")));
			boolean bA = ((iN+2 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+2).getPos(), this.datos.getAdjetivos())));
			if((bP) && (bA))
			{
				Frase temp = corpus.subFrase(iN, (iN+2));
				//una por delante con el AF
				adelantarAF(temp, filtro);
				filtro.put(temp.aString(), temp);
			}
		}
	}

	//N
	public void close3(Hashtable filtro)
	{
		for(int i = 0; i < this.datos.tamIndicesNombresC(); i++)
		{
			int iN = this.datos.getIndicesNombresC(i);
			if(iN == -1)// no hay nombres
				return;
			Frase temp = corpus.subFrase(iN, iN);
			//una por delante con el AF
			adelantarAF(temp, filtro);
			filtro.put(temp.aString(), temp);
		}
	}





	//------------------------------------------- open filter
	public void openFilter(Hashtable filtro)
	{
		open1(filtro);

		open2(filtro);

		open3(filtro);

		open4(filtro);

		open5(filtro);
	}

	//(N | ProperN | ForeignWord)+
	public void open1(Hashtable filtro)
	{
		int inicio = -1;
		int fin = -1;
		for(int i = 0; i < corpus.tamanyo(); i++)
		{
			if((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres())) || (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getForeigns())))
			{
				inicio = corpus.getToken(i).getCodigo(); //inicio = i;
				fin = inicio; //fin = i;
				while((i < corpus.tamanyo()) && ((Filtro.esta(corpus.getToken(i).getPos(), this.datos.getNombres())) || (Filtro.esta(corpus.getToken(i).getPos(), this.datos.getForeigns())))) //marcar el fin y seguir
				{
					fin = corpus.getToken(i).getCodigo(); //fin = i;
					i++;
				}
				//guardar el inicio y el fin
				Frase temp = corpus.subFrase(inicio, fin);
				//una por delante con el AF
				adelantarAF(temp, filtro);
				filtro.put(temp.aString(), temp);
			}
		}
	}

	//(N ADJ)(de (N | ProperN))*
	public void open2(Hashtable filtro)
	{
		int inicio = -1;
		int fin = -1;
		int iN = -1;
		for(int i = 0; i < this.datos.tamIndicesNombresC(); i++)
		{
			int iNc = this.datos.getIndicesNombresC(i);
			if(iNc == -1)
				return;
			if(iNc <= iN)// dentro de la expr. reg. procesada
				continue;
			iN = iNc;
			boolean bA = ((iN+1 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+1).getPos(), this.datos.getAdjetivos())));
			if(bA)
			{
				inicio = iN;
				fin = iN + 1;
				iN += 2; //buscar siguiente
				while((iN < corpus.tamanyo()) && 
					((Filtro.esta(corpus.getToken(iN).getPos(), this.datos.getPreposiciones())) && ("de".equals(corpus.getToken(iN).getPalabra()))) && 
					(iN+1 < corpus.tamanyo()) && 
					(Filtro.esta(corpus.getToken(iN+1).getPos(), this.datos.getNombres()))) //(de (N | ProperN))*
				{
					fin = corpus.getToken(iN+1).getCodigo(); //fin = iN+1;
					iN += 2;
				}
				//guardar el inicio y el fin
				Frase temp = corpus.subFrase(inicio, fin);
				//una por delante con el AF
				adelantarAF(temp, filtro);
				filtro.put(temp.aString(), temp);
			}
		}
	}

	//N de (N | ProperN)
	public void open3(Hashtable filtro)
	{
		for(int i = 0; i < this.datos.tamIndicesNombresC(); i++)
		{
			int iN = this.datos.getIndicesNombresC(i);
			if(iN == -1)
				return;
			boolean bP = ((iN+1 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+1).getPos(), this.datos.getPreposiciones())) && (corpus.getToken(iN+1).getPalabra().equals("de"))); //de
			boolean bN = ((iN+2 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+2).getPos(), this.datos.getNombres()))); //(N | ProperN)
			if((bP) && (bN))
			{
				Frase temp = corpus.subFrase(iN, (iN+2));
				//una por delante con el AF
				adelantarAF(temp, filtro);
				filtro.put(temp.aString(), temp);
			}
		}
	}

	//N? Acron
	public void open4(Hashtable filtro)
	{
		for(int i = 0; i < this.datos.tamIndicesAcronimos(); i++)
		{
			int iAcron = this.datos.getIndicesAcronimos(i);
			if(iAcron == -1)
				return;
			boolean bN = ((iAcron-1 >= 0) && (Filtro.esta(corpus.getToken(iAcron-1).getPos(), this.datos.getNombresC()))); //N?
			if(bN)
			{
				Frase temp = corpus.subFrase((iAcron-1), iAcron);
				//una por delante con el AF
				adelantarAF(temp, filtro);
				filtro.put(temp.aString(), temp);
			}
			else
			{
				Frase temp = corpus.subFrase(iAcron, iAcron);
				//una por delante con el AF
				adelantarAF(temp, filtro);
				filtro.put(temp.aString(), temp);
			}
		}
	}

	//N de ((N ADJ) | (ADJ N))
	public void open5(Hashtable filtro)
	{
		for(int i = 0; i < this.datos.tamIndicesNombresC(); i++)
		{
			int iN = this.datos.getIndicesNombresC(i);
			if(iN == -1)
				return;
			boolean bP = ((iN+1 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+1).getPos(), this.datos.getPreposiciones())) && (corpus.getToken(iN+1).getPalabra().equals("de"))); //de
			boolean bN1 = ((iN+2 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+2).getPos(), this.datos.getNombresC()))); //(N )
			boolean bA1 = ((iN+3 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+3).getPos(), this.datos.getAdjetivos()))); //( ADJ)
			boolean bA2 = ((iN+2 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+2).getPos(), this.datos.getAdjetivos()))); //(ADJ )
			boolean bN2 = ((iN+3 < corpus.tamanyo()) && (Filtro.esta(corpus.getToken(iN+3).getPos(), this.datos.getNombresC()))); //( N)
			if((bP) && (((bN1) && (bA1)) || ((bA2) && (bN2))))//seguidos
			{
				Frase temp = corpus.subFrase(iN, (iN+3));
				//una por delante con el AF
				adelantarAF(temp, filtro);
				filtro.put(temp.aString(), temp);
			}
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



































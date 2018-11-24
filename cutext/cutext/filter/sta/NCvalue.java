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
Aplica NC-Value: 3 fases de la p.11 del paper
******************************************************************************************/




package cutext.filter.sta;



import java.util.*;
import java.io.*;

import cutext.prepro.*;
import cutext.filter.lin.*;
import cutext.util.*;



public class NCvalue implements Cloneable, Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	//w: nombres, adjetivos, verbos

	Frase corpus; //corpus sin filtrar
	ArrayFrases terminos; //los dados por Cvalue
	StopWords sw;
	Frases lista;

	DatosEntrada datos;

	NCvalueInterface ncvalue = null;


	public NCvalue clone()
	{
		NCvalue nc = null;
		try
		{
			nc = (NCvalue)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:
		nc.corpus = (Frase)nc.corpus.clone();
		nc.terminos = (ArrayFrases)nc.terminos.clone();
		nc.sw = (StopWords)nc.sw.clone();
		nc.lista = (Frases)nc.lista.clone();

		return nc;
	}

	public NCvalue(DatosEntrada datos, Frase corpus, ArrayFrases terminos, StopWords sw)
	{
		this.datos = datos;
		inicializar();
		this.corpus = corpus;
		this.terminos = terminos;
		this.sw = sw;

		//select ncvalue by language
		if(this.datos.getIdioma().equals(DatosEntrada.SPA))
			this.ncvalue = new NCvalueSpa(this.corpus);
		else if(this.datos.getIdioma().equals(DatosEntrada.ENG))
			this.ncvalue = new NCvalueEng();
		else if(this.datos.getIdioma().equals(DatosEntrada.CAT))
			this.ncvalue = new NCvalueCat();
		else //if(this.datos.getIdioma().equals(DatosEntrada.GAL))
			this.ncvalue = new NCvalueGal();
	}

	public void inicializar()
	{
		corpus = null;
		terminos = null;
		sw = null;
		lista = new Frases();
	}






	//------------- SET --------------------


	public void setCorpus(Frase corpus)
	{
		this.corpus = corpus;
	}

	public void setTerminos(ArrayFrases terminos)
	{
		this.terminos = terminos;
	}

	public void setSw(StopWords sw)
	{
		this.sw = sw;
	}

	public void setLista(Frases lista)
	{
		this.lista = lista;
	}



	//------------- GET --------------------


	public Frase getCorpus()
	{
		return corpus;
	}

	public ArrayFrases getTerminos()
	{
		return terminos;
	}

	public StopWords getSw()
	{
		return sw;
	}

	public Frases getLista()
	{
		return lista;
	}






	//------------- Otros: Fases p.11 del paper --------------------


	//1 ordenar terminos por su cvalue
	//2 extraccion y pesado
	//3 extraccion multiwords y modificar cvalue con ncvalue
	public void setNCvalue()
	{
		//1. ordenar de mayor a menor cvalue (???: viene en el paper pero no se para que)
		//---terminos = terminos.ordenarCvalue(terminos, 0, terminos.tamanyo()-1);

		//para cada termino: creacion de un conjunto de los w y sus pesos, y reajustar cvalue:::parece que solo se reajusta el c-value a los top candidate terms ???cuantos es top
		for(int i = 0; i < terminos.tamanyo(); i++)
		{
			//2. extraccion de w y sus pesos
			Frases a = terminos.getFrases(i); 
			ConjuntoW ca = extraerConjuntoW(a);
//System.out.println("ca\n" + ca.aString());
			//3. extraccion terminos multipalabra (?) y reajuste de cvalue
			getNCvalue(ca, a);
		}
//System.out.println("terminos con cvalue\n" + terminos.aString());


/*
		for(int i = 0; i < tord.tamanyo(); i++)
		{
			Frase t = tord.getFrase();
			Frase w = t.extraerConjunto(corpus); //extrae el conjunto de los w de a
		}
*/
	}

	public void getNCvalue(ConjuntoW ca, Frases termino)
	{
		double sumatorio = 0.0;
		//sumatorio: frecuencia * peso
		for(int i = 0; i < ca.tamanyo(); i++)
		{
			Token w = ca.getW(i);
			sumatorio += (double)((double)w.getFrecuencia() * w.getPeso());
		}
		double ncvalue = (double)((double)Estaticos.FACTOR_CVALUE * (double)termino.getCvalue()) + (double)((double)Estaticos.FACTOR_WEIGHT_CVALUE * (double)sumatorio);

		//asignar nuevo cvalue a cada termino
		termino.setCvalue(ncvalue);
		for(int i = 0; i < termino.tamanyo(); i++)
		{
			Frase frase = termino.getFrase(i);
			frase.setCvalue(ncvalue);
		}
	}

	public ConjuntoW extraerConjuntoW(Frases termino)
	{
		//extraer los w asociados al termino
		ConjuntoW cw1 = extraerW(termino);
		//asignar peso a cada w
		ConjuntoW cw = asignarPeso(cw1, termino);

		return cw;
	}

	public ConjuntoW extraerW(Frases termino)
	{
		ConjuntoW cw = new ConjuntoW(termino.getFrase(0)); //el primer representante del grupo
		for(int i = 0; i < termino.tamanyo(); i++)
		{
			Frase t = termino.getFrase(i);
			//izquierdo
			Frase fWizq = obtenerWizq(t);
			for(int j = 0; j < fWizq.tamanyo(); j++)
			{
				Token posibleWizq = fWizq.getToken(j);
				if((Filtro.esta(posibleWizq.getPos(), this.datos.getNombres())) || 
					(Filtro.esta(posibleWizq.getPos(), this.datos.getAdjetivos())) || 
					(Filtro.esta(posibleWizq.getPos(), this.datos.getVerbos())))
				{
					cw.anyadir((Token)posibleWizq.clone());
				}
			}
			//derecho
			Frase fWder = obtenerWder(t);
			for(int j = 0; j < fWder.tamanyo(); j++)
			{
				Token posibleWder = fWder.getToken(j);
				if((Filtro.esta(posibleWder.getPos(), this.datos.getNombres())) || 
					(Filtro.esta(posibleWder.getPos(), this.datos.getAdjetivos())) || 
					(Filtro.esta(posibleWder.getPos(), this.datos.getVerbos())))
				{
					cw.anyadir((Token)posibleWder.clone());
				}
			}
		}
		return cw;
	}

	public ConjuntoW asignarPeso(ConjuntoW cw, Frases term)
	{
		ConjuntoW conjunto = new ConjuntoW(term.getFrase(0));

		//adjetivos
		int t = numWaparece(this.datos.getAdjetivos(), cw);
		double peso = (double)((double)t / (double)term.tamanyo());
		if(Double.compare(peso, 0.0) > 0)
			//Token(String palabra, String pos, String lema, int codigo, int frecuencia, double peso, int offset)
			conjunto.anyadir(new Token("ADJETIVOS", this.datos.getAdjetivos()[0], "ADJETIVO", 1, t, peso, term.getFrase(0).getToken(0).getOffset()));

		//nombres
		t = numWaparece(this.datos.getNombres(), cw);
		peso = (double)((double)t / (double)term.tamanyo());
		if(Double.compare(peso, 0.0) > 0)
			conjunto.anyadir(new Token("NOMBRES", this.datos.getNombres()[0], "NOMBRE", 1, t, peso, term.getFrase(0).getToken(0).getOffset()));

		//verbos
		t = numWaparece(this.datos.getVerbos(), cw);
		peso = (double)((double)t / (double)term.tamanyo());
		if(Double.compare(peso, 0.0) > 0)
			conjunto.anyadir(new Token("VERBOS", this.datos.getVerbos()[0], "VERBO", 1, t, peso, term.getFrase(0).getToken(0).getOffset()));

		return conjunto;
	}

	public int numWaparece(String lista[], ConjuntoW cw)
	{
		int veces = 0;
		for(int i = 0; i < cw.tamanyo(); i++)
		{
			Token w = cw.getW(i);
			if(Filtro.esta(w.getPos(), lista))
				veces++;
		}
		return veces;
	}

	public Frase obtenerWizq(Frase termino)
	{
//System.out.println("========= obtenerWizq ==========\n------------- termino:\n" + termino.aString());
		Token tokenizq = termino.getToken(0);
		int codigoizq = tokenizq.getCodigo();
		if(codigoizq <= 0) //no hay nada a su izquierda
			return new Frase();
		int indiceDerecho = codigoizq - 1;
		int tope = this.ncvalue.setTopeIzquierdo(termino);
		//int indiceIzquierdo = codigoizq - Estaticos.LONGITUD_MAXIMA_STRINGS;
		int indiceIzquierdo = codigoizq - tope;
		Frase fIzquierda = this.corpus.subFrase(indiceIzquierdo, indiceDerecho);
		int i = 1;
		while(fIzquierda == null)
		{
			//indiceIzquierdo = Math.abs(codigoizq - (Estaticos.LONGITUD_MAXIMA_STRINGS + i));
			indiceIzquierdo = Math.abs(codigoizq - (tope + i));
			i++;
			fIzquierda = this.corpus.subFrase(indiceIzquierdo, indiceDerecho);
		}
//System.out.println("========= FIN obtenerWizq ==========\n------------- fIzquierda:\n" + fIzquierda.aString());
		return fIzquierda;
	}

	public Frase obtenerWder(Frase termino)
	{
//System.out.println("========= obtenerWder ==========\n------------- termino:\n" + termino.aString());
		Token tokender = termino.getToken(termino.tamanyo()-1);
		int codigoder = tokender.getCodigo();
		if(codigoder >= this.corpus.tamanyo()) //no hay nada a su derecha
			return new Frase();
		int indiceIzquierdo = codigoder + 1;
		int tope = this.ncvalue.setTopeDerecho(termino);
		//int indiceDerecho = codigoder + Estaticos.LONGITUD_MAXIMA_STRINGS;
		int indiceDerecho = codigoder + tope;
		Frase fDerecha = this.corpus.subFrase(indiceIzquierdo, indiceDerecho);
		int i = 1;
		while(fDerecha == null)
		{
			//indiceDerecho = codigoder + (Estaticos.LONGITUD_MAXIMA_STRINGS - i);
			indiceDerecho = codigoder + (tope - i);
			i++;
			fDerecha = this.corpus.subFrase(indiceIzquierdo, indiceDerecho);
		}
//System.out.println("========= FIN obtenerWder ==========\n------------- fDerecha:\n" + fDerecha.aString());
		return fDerecha;
	}


}











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
 





package cutext.filter.sta;



import java.util.*;
import java.io.*;

import cutext.prepro.*;
import cutext.util.*;



public class NCvalueSpa implements NCvalueInterface, Cloneable, Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;

	Frase corpus; //corpus sin filtrar

	public NCvalueSpa clone()
	{
		NCvalueSpa n = null;
		try
		{
			n = (NCvalueSpa)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		n.corpus = (Frase)n.corpus.clone();

		return n;
	}

	public NCvalueSpa(Frase corpus)
	{
		this.corpus = corpus;
	}





	public int setTopeIzquierdo(Frase termino)
	{
		Token token = termino.getToken(0);
		int tope = token.getCodigo() - 1;
		int indice = tope;
		while(indice >= 0)
		{
			if(indice-1 < 0)
				break;
			indice--;
			token = this.corpus.getToken(indice);
			if(Estaticos.esta(token.getPalabra(), Estaticos.FRONT_PUNCT_MARKS))
			{
				tope = token.getCodigo();
				break; //encontrado
			}
		}
		int codigoTermino = termino.getToken(0).getCodigo();
		tope = codigoTermino - tope;

		return tope;
	}

	public int setTopeDerecho(Frase termino)
	{
		Token token = termino.getToken(termino.tamanyo()-1);
		int tope = token.getCodigo() + 1;
		int indice = tope;
		while(indice < this.corpus.tamanyo())
		{
			if(indice+1 >= this.corpus.tamanyo())
				break;
			indice++;
			token = this.corpus.getToken(indice);
			if(Estaticos.esta(token.getPalabra(), Estaticos.FRONT_PUNCT_MARKS))
			{
				tope = token.getCodigo();
				break; //encontrado
			}
		}
		int codigoTermino = termino.getToken(termino.tamanyo()-1).getCodigo();
		tope = tope - codigoTermino;

		return tope;
	}





}









































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



public class StopWordsSpa implements StopInterface, Cloneable, Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;

	DatosEntrada datos;


	public StopWordsSpa clone()
	{
		StopWordsSpa s = null;
		try
		{
			s = (StopWordsSpa)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		return s;
	}

	public StopWordsSpa(DatosEntrada datos)
	{
		this.datos = datos;
	}





	public Frase stopList(Frase termino)
	{
		return selectiveStopWordsDeletion(termino);
	}

	//p.132: Algorithm 2. Figure 2.
	public Frase selectiveStopWordsDeletion(Frase candidate)
	{
		Frase delete = new Frase(); //D

		for(int i = 0; i < candidate.tamanyo(); i++)
		{
			Token token = candidate.getToken(i);
			if((Filtro.esta(token.getPos(), this.datos.getAdjetivos())) && (StopWords.esta(token.getPalabra(), this.datos.getListaStopWords())))
			{
				delete.anyadir((Token)token.clone());
			}
			else if((Filtro.esta(token.getPos(), this.datos.getNombresC())) && (StopWords.esta(token.getPalabra(), this.datos.getListaStopWords())))
			{
				delete.anyadir((Token)token.clone());
				if((i-1 >= 0) && (Filtro.esta(candidate.getToken(i-1).getPos(), this.datos.getPreposiciones())))
				{
					delete.anyadir((Token)candidate.getToken(i-1).clone());
				}
				if((i+1 < candidate.tamanyo()) && 
					((Filtro.esta(candidate.getToken(i+1).getPos(), this.datos.getPreposiciones())) || 
					(Filtro.esta(candidate.getToken(i+1).getPos(), this.datos.getAdjetivos()))))
				{
					delete.anyadir((Token)candidate.getToken(i+1).clone());
					if((i+2 < candidate.tamanyo()) && 
						((Filtro.esta(candidate.getToken(i+2).getPos(), this.datos.getPreposiciones())) || 
						(Filtro.esta(candidate.getToken(i+2).getPos(), this.datos.getAdjetivos()))))
					{
						delete.anyadir((Token)candidate.getToken(i+2).clone());
					}
				}
			}
		}
		return selectiveStopWordsDeletion(delete, candidate);
	}

	private Frase selectiveStopWordsDeletion(Frase delete, Frase candidate)
	{
		Frase select = new Frase(); //s
		select.setFrecuencia(candidate.getFrecuencia());

		for(int i = 0; i < candidate.tamanyo(); i++)
		{
			Token token = candidate.getToken(i);
			if(!delete.esta(token))
			{
				select.anyadir((Token)token.clone());
			}
		}
		if(select.vacia()) //se han eliminado todos sus tokens
			return null;
		return select;
	}








}
















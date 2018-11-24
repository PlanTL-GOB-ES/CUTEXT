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



public class StopWordsGal implements StopInterface, Cloneable, Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;

	DatosEntrada datos;


	public StopWordsGal clone()
	{
		StopWordsGal s = null;
		try
		{
			s = (StopWordsGal)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		return s;
	}

	public StopWordsGal(DatosEntrada datos)
	{
		this.datos = datos;
	}





	public Frase stopList(Frase termino)
	{
		Frase tss = new Frase();
		tss.setFrecuencia(termino.getFrecuencia());

		for(int i = 0; i < termino.tamanyo(); i++)
		{
			Token token = termino.getToken(i);
			if(StopWords.esta(token.getPalabra(), this.datos.getListaStopWords())) //no incluir el termino
			//if(esta(token.getLema(), this.lista)) //no incluir el termino
			{
				return null;
			}
			else
			{
				tss.anyadir((Token)token.clone());
			}
		}
		return tss;
	}









}
















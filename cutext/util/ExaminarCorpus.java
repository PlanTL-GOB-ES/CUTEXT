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
* Se encarga de mirar el corpus guardado
************************************************************************************************************************************************************/



package cutext.util;



import java.util.*;
import java.io.*;

import cutext.prepro.*;



public class ExaminarCorpus
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	Frase corpus;

	public ExaminarCorpus()
	{
		ARCorpus ar = new ARCorpus("Recuperar", "corpus_");
		Frase frases = ar.recuperar();
		this.corpus = frases;
	}


	public Frase extraer(int tope)
	{
		Frase f = new Frase();
		for(int i = 0; i < this.corpus.tamanyo(); i++)
		{
			Token t = this.corpus.getToken(i);
			if(t.getFrecuencia() > tope)
				f.anyadir((Token)t.clone());
		}
		return f;
	}


	public Frase extraerMayores()
	{
		Frase frase = new Frase();
		int mayor = -3;
		for(int i = 0; i < this.corpus.tamanyo(); i++)
		{
			Token t = this.corpus.getToken(i);
			if(mayor < t.getFrecuencia())
			{
				mayor = t.getFrecuencia();
				frase = new Frase();
			}
			if(mayor == t.getFrecuencia())
			{
				frase.anyadir((Token)t.clone());
			}
		}
		return frase;
	}







	public static void main(String args[])
	{
		ExaminarCorpus e = new ExaminarCorpus();

		//Frase fEx = e.extraer(300);
		//System.out.println("=====\n" + fEx.aString());

		//Frase fmayores = e.extraerMayores();
		//System.out.println("===== MAYORES =====\n" + fmayores.aString());
		System.out.println("size corpus = " + e.corpus.tamanyo());
	}


}

























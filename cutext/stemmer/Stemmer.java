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
 


package cutext.stemmer;


import java.io.*;


public class Stemmer
{
	
	private static final long serialVersionUID = -7149755349268484907L;

	StemmerInterface stemmer = null;


	public Stemmer(String lat)
	{
		if(lat.equals(cutext.prepro.DatosEntrada.SPA))
			stemmer = new StemmerS();
		else
			stemmer = new StemmerE();
	}

	public String exec(String str)
	{
		String stemmerstr = "";
		String words[] = str.split("\\s+");
		for(int i = 0; i < words.length; i++)
		{
			String word = words[i];
			//String stem = this.stemmer.stemm(word.toLowerCase());
			String stem = stemmIter(word.toLowerCase());
			stemmerstr += stem + " ";
		}
		return stemmerstr.trim();
	}

	//gets the lemma iteratively until it does not change
	public String stemmIter(String word)
	{
		String stem = this.stemmer.stemm(word.toLowerCase());
		String stem2 = stem;
		while(true)
		{
			stem = this.stemmer.stemm(stem2);
			if(!stem.equals(stem2))
			{
				stem2 = stem;
			}
			else
				break;
		}
		return stem;
	}





	public static void main(String args[])
	{
		Stemmer s = new Stemmer(cutext.prepro.DatosEntrada.SPA);

		//spanish test
		s.stemmer = new StemmerS();
		System.out.println("\t===== SPANISH TEST =====");
		//plural
		String words = "unidades de cuidados intensivos";
		String wordsStemm = s.exec(words);
		System.out.println("original term: " + words + "\tlemma term: " + wordsStemm);
		//singular
		words = "unidad de cuidado intensivo";
		wordsStemm = s.exec(words);
		System.out.println("original term: " + words + "\tlemma term: " + wordsStemm);

		//english test
		s = new Stemmer(cutext.prepro.DatosEntrada.ENG);
		s.stemmer = new StemmerE();
		System.out.println("\t===== ENGLISH TEST =====");
		//plural
		words = "intensive care units";
		wordsStemm = s.exec(words);
		System.out.println("original term: " + words + "\tlemma term: " + wordsStemm);
		//singular
		words = "intensive care unit";
		wordsStemm = s.exec(words);
		System.out.println("original term: " + words + "\tlemma term: " + wordsStemm);
	}



}



















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
 






package cutext.prepro;











import java.util.*;
import java.io.*;





public class HashTerms implements Serializable
{

	private static final long serialVersionUID = -7149755349268484907L;
	//private static final long serialVersionUID = -8708239248180800881L;

	Hashtable<String,cutext.prepro.Frase> hashTerms; //<lemma><Frase>


	public HashTerms()
	{
		this.hashTerms = new Hashtable<String,cutext.prepro.Frase>();
	}



	public Enumeration<cutext.prepro.Frase> elements()
	{
		return this.hashTerms.elements();
	}

	public Enumeration<String> keys()
	{
		return this.hashTerms.keys();
	}

	public Set<String> keySet()
	{
		return this.hashTerms.keySet();
	}


	public cutext.prepro.Frase get(String key)
	{
		return this.hashTerms.get(key);
	}

	public cutext.prepro.Frase put(String key, cutext.prepro.Frase value)
	{
		return this.hashTerms.put(key, value);
	}


	public boolean isEmpty()
	{
		return this.hashTerms.isEmpty();
	}

	public cutext.prepro.Frase remove(String key)
	{
		return this.hashTerms.remove(key);
	}

	public int size()
	{
		return this.hashTerms.size();
	}

	public Collection<cutext.prepro.Frase> values()
	{
		return this.hashTerms.values();
	}

	public String toString()
	{
		String str = "";
		Enumeration<String> ks = this.keys();
		while(ks.hasMoreElements())
		{
			String k = ks.nextElement();
			cutext.prepro.Frase v = this.get(k);
			str += v.wordsToString() + "|" + k + "|" + v.getFrecuencia() + "|" + v.getCvalue() + "\n"; //term | lemma | frequency | cvalue
		}
		return str;
	}

	



}































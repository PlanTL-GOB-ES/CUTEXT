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





public class SimpliHashTerms implements Serializable
{

	private static final long serialVersionUID = -7149755349268484907L;
	//private static final long serialVersionUID = -8708239248180800881L;

	Hashtable<String,cutext.prepro.SimpliFrase> simpliHashTerms; //<lemma><SimpliFrase>


	public SimpliHashTerms()
	{
		this.simpliHashTerms = new Hashtable<String,cutext.prepro.SimpliFrase>();
	}



	public Enumeration<cutext.prepro.SimpliFrase> elements()
	{
		return this.simpliHashTerms.elements();
	}

	public Enumeration<String> keys()
	{
		return this.simpliHashTerms.keys();
	}

	public Set<String> keySet()
	{
		return this.simpliHashTerms.keySet();
	}


	public cutext.prepro.SimpliFrase get(String key)
	{
		return this.simpliHashTerms.get(key);
	}

	public cutext.prepro.SimpliFrase put(String key, cutext.prepro.SimpliFrase value)
	{
		return this.simpliHashTerms.put(key, value);
	}


	public boolean isEmpty()
	{
		return this.simpliHashTerms.isEmpty();
	}

	public cutext.prepro.SimpliFrase remove(String key)
	{
		return this.simpliHashTerms.remove(key);
	}

	public int size()
	{
		return this.simpliHashTerms.size();
	}

	public Collection<cutext.prepro.SimpliFrase> values()
	{
		return this.simpliHashTerms.values();
	}

	//term | lemma | freq | cvalue | tamanyo | t | c
	public String toString()
	{
		String str = "";
		Enumeration<String> ks = this.keys();
		while(ks.hasMoreElements())
		{
			String k = ks.nextElement();
			cutext.prepro.SimpliFrase v = this.get(k);
			str += v.getWords() + "|" + 
					k + "|" + 
					v.getFrecuencia() + "|" + 
					v.getCvalue() + "|" + 
					v.tamanyo() + "|" + 
					//v.getT() + "|" + 
					//v.getC() + 
					"\n";
		}
		return str;
	}

	



}































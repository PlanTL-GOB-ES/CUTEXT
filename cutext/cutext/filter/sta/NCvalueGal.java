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



public class NCvalueGal implements NCvalueInterface, Cloneable, Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;

	public NCvalueGal clone()
	{
		NCvalueGal n = null;
		try
		{
			n = (NCvalueGal)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}

		return n;
	}

	public NCvalueGal()
	{
	}





	public int setTopeIzquierdo(Frase termino)
	{
		int tope = Estaticos.LONGITUD_MAXIMA_STRINGS;
		return tope;
	}

	public int setTopeDerecho(Frase termino)
	{
		int tope = Estaticos.LONGITUD_MAXIMA_STRINGS;
		return tope;
	}








}









































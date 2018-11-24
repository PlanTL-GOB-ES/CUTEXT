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
 

/********************************************************************************************
* Son datos temporales, para determinar cuanto tarda en encontrar la solucion.
********************************************************************************************/



package cutext.util;





import java.util.*;
import java.io.*;






public class Tiempo
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	private long horas;
	private long minutos;
	private long segundos;
	private long centesimas;

	public Tiempo()
	{
	}

	public void setCentesimas(long c)
	{
		centesimas = c;
	}

	public void setSegundos(long s)
	{
		segundos = s;
	}

	public void setMinutos(long m)
	{
		minutos = m;
	}

	public void setHoras(long h)
	{
		horas = h;
	}

	public long getCentesimas()
	{
		return centesimas;
	}

	public long getSegundos()
	{
		return segundos;
	}

	public long getMinutos()
	{
		return minutos;
	}

	public long getHoras()
	{
		return horas;
	}

	public Tiempo conversion(long tEstNano)
	{
		//conversion a horas
		double horas = (double)tEstNano * (1.0/1000000000.0) * (1.0/60.0) * (1.0/60.0);
		setHoras((long)horas); //quedarse con la parte entera

		//conversion a minutos
		double minutos = Math.abs(horas - (double)getHoras()) * 60.0;
		setMinutos((long)minutos); //quedarse con la parte entera

		//conversion a segundos
		double segundos = Math.abs(minutos - (double)getMinutos()) * 60.0;
		setSegundos((long)segundos); //quedarse con la parte entera

		//conversion a centesimas
		double centesimas = Math.abs(segundos - (double)getSegundos()) * 100.0;
		setCentesimas((long)centesimas); //quedarse con la parte entera

		return this;
	}

	public String aString()
	{
		String time = //"\n\n\nEstimated Time:       " + 
				getHoras() + ":" +
				getMinutos() + ":" +
				getSegundos() + "." +
				getCentesimas();
		return time;
	}

}




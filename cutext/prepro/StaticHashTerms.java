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



import cutext.prepro.HashTerms;
import cutext.prepro.SerHashTerms;

import java.io.*;


public class StaticHashTerms implements Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;


	public static HashTerms hA = new HashTerms();
	public static HashTerms hB = new HashTerms();
	public static HashTerms hC = new HashTerms();
	public static HashTerms hD = new HashTerms();
	public static HashTerms hE = new HashTerms();
	public static HashTerms hF = new HashTerms();
	public static HashTerms hG = new HashTerms();
	public static HashTerms hH = new HashTerms();
	public static HashTerms hI = new HashTerms();
	public static HashTerms hJ = new HashTerms();
	public static HashTerms hK = new HashTerms();
	public static HashTerms hL = new HashTerms();
	public static HashTerms hM = new HashTerms();
	public static HashTerms hN = new HashTerms();
	public static HashTerms hO = new HashTerms();
	public static HashTerms hP = new HashTerms();
	public static HashTerms hQ = new HashTerms();
	public static HashTerms hR = new HashTerms();
	public static HashTerms hS = new HashTerms();
	public static HashTerms hT = new HashTerms();
	public static HashTerms hU = new HashTerms();
	public static HashTerms hV = new HashTerms();
	public static HashTerms hW = new HashTerms();
	public static HashTerms hX = new HashTerms();
	public static HashTerms hY = new HashTerms();
	public static HashTerms hZ = new HashTerms();
	public static HashTerms hOTHER = new HashTerms();
	
	//simplification hash terms
	public static SimpliHashTerms hsimpli = new SimpliHashTerms();
	
	
	
	
	public static void saveHashTerms(String routeHash)
	{
		if(StaticHashTerms.hA.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "ha");
			s.guardar(StaticHashTerms.hA);
		}
		if(StaticHashTerms.hB.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hb");
			s.guardar(StaticHashTerms.hB);
		}
		if(StaticHashTerms.hC.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hc");
			s.guardar(StaticHashTerms.hC);
		}
		if(StaticHashTerms.hD.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hd");
			s.guardar(StaticHashTerms.hD);
		}
		if(StaticHashTerms.hE.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "he");
			s.guardar(StaticHashTerms.hE);
		}
		if(StaticHashTerms.hF.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hf");
			s.guardar(StaticHashTerms.hF);
		}
		if(StaticHashTerms.hG.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hg");
			s.guardar(StaticHashTerms.hG);
		}
		if(StaticHashTerms.hH.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hh");
			s.guardar(StaticHashTerms.hH);
		}
		if(StaticHashTerms.hI.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hi");
			s.guardar(StaticHashTerms.hI);
		}
		if(StaticHashTerms.hJ.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hj");
			s.guardar(StaticHashTerms.hJ);
		}
		if(StaticHashTerms.hK.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hk");
			s.guardar(StaticHashTerms.hK);
		}
		if(StaticHashTerms.hL.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hl");
			s.guardar(StaticHashTerms.hL);
		}
		if(StaticHashTerms.hM.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hm");
			s.guardar(StaticHashTerms.hM);
		}
		if(StaticHashTerms.hN.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hn");
			s.guardar(StaticHashTerms.hN);
		}
		if(StaticHashTerms.hO.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "ho");
			s.guardar(StaticHashTerms.hO);
		}
		if(StaticHashTerms.hP.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hp");
			s.guardar(StaticHashTerms.hP);
		}
		if(StaticHashTerms.hQ.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hq");
			s.guardar(StaticHashTerms.hQ);
		}
		if(StaticHashTerms.hR.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hr");
			s.guardar(StaticHashTerms.hR);
		}
		if(StaticHashTerms.hS.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hs");
			s.guardar(StaticHashTerms.hS);
		}
		if(StaticHashTerms.hT.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "ht");
			s.guardar(StaticHashTerms.hT);
		}
		if(StaticHashTerms.hU.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hu");
			s.guardar(StaticHashTerms.hU);
		}
		if(StaticHashTerms.hV.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hv");
			s.guardar(StaticHashTerms.hV);
		}
		if(StaticHashTerms.hW.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hw");
			s.guardar(StaticHashTerms.hW);
		}
		if(StaticHashTerms.hX.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hx");
			s.guardar(StaticHashTerms.hX);
		}
		if(StaticHashTerms.hY.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hy");
			s.guardar(StaticHashTerms.hY);
		}
		if(StaticHashTerms.hZ.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hz");
			s.guardar(StaticHashTerms.hZ);
		}
		if(StaticHashTerms.hOTHER.size() > 0)
		{
			SerHashTerms s = new SerHashTerms("Guardar", routeHash + "hother");
			s.guardar(StaticHashTerms.hOTHER);
		}
	}
	
	
	public static HashTerms get(String fl)
	{
		switch (fl.toLowerCase())
		{
			case "a":
				return StaticHashTerms.hA;
			case "b":
				return StaticHashTerms.hB;
			case "c":
				return StaticHashTerms.hC;
			case "d":
				return StaticHashTerms.hD;
			case "e":
				return StaticHashTerms.hE;
			case "f":
				return StaticHashTerms.hF;
			case "g":
				return StaticHashTerms.hG;
			case "h":
				return StaticHashTerms.hH;
			case "i":
				return StaticHashTerms.hI;
			case "j":
				return StaticHashTerms.hJ;
			case "k":
				return StaticHashTerms.hK;
			case "l":
				return StaticHashTerms.hL;
			case "m":
				return StaticHashTerms.hM;
			case "n":
				return StaticHashTerms.hN;
			case "o":
				return StaticHashTerms.hO;
			case "p":
				return StaticHashTerms.hP;
			case "q":
				return StaticHashTerms.hQ;
			case "r":
				return StaticHashTerms.hR;
			case "s":
				return StaticHashTerms.hS;
			case "t":
				return StaticHashTerms.hT;
			case "u":
				return StaticHashTerms.hU;
			case "v":
				return StaticHashTerms.hV;
			case "w":
				return StaticHashTerms.hW;
			case "x":
				return StaticHashTerms.hX;
			case "y":
				return StaticHashTerms.hY;
			case "z":
				return StaticHashTerms.hZ;
			default:
				return StaticHashTerms.hOTHER;
		}
	}
	
	
	public static void initializeHashTerms()
	{
		StaticHashTerms.hA = new HashTerms();
		StaticHashTerms.hB = new HashTerms();
		StaticHashTerms.hC = new HashTerms();
		StaticHashTerms.hD = new HashTerms();
		StaticHashTerms.hE = new HashTerms();
		StaticHashTerms.hF = new HashTerms();
		StaticHashTerms.hG = new HashTerms();
		StaticHashTerms.hH = new HashTerms();
		StaticHashTerms.hI = new HashTerms();
		StaticHashTerms.hJ = new HashTerms();
		StaticHashTerms.hK = new HashTerms();
		StaticHashTerms.hL = new HashTerms();
		StaticHashTerms.hM = new HashTerms();
		StaticHashTerms.hN = new HashTerms();
		StaticHashTerms.hO = new HashTerms();
		StaticHashTerms.hP = new HashTerms();
		StaticHashTerms.hQ = new HashTerms();
		StaticHashTerms.hR = new HashTerms();
		StaticHashTerms.hS = new HashTerms();
		StaticHashTerms.hT = new HashTerms();
		StaticHashTerms.hU = new HashTerms();
		StaticHashTerms.hV = new HashTerms();
		StaticHashTerms.hW = new HashTerms();
		StaticHashTerms.hX = new HashTerms();
		StaticHashTerms.hY = new HashTerms();
		StaticHashTerms.hZ = new HashTerms();
		StaticHashTerms.hOTHER = new HashTerms();
	}
	
	
	
	
	/*******************************************
		SAME METHODS FOR "SIMPLI HASH TERMS"
	********************************************/
	public static void saveSimpliHashTerms(String routeHash)
	{
		if(StaticHashTerms.hsimpli.size() > 0)
		{
			SerSimpliHashTerms s = new SerSimpliHashTerms("Guardar", routeHash + "hsimpli");
			s.guardar(StaticHashTerms.hsimpli);
		}
	}
	
	public static SimpliHashTerms get()
	{
		return StaticHashTerms.hsimpli;
	}
	
	public static void initializeSimpliHashTerms()
	{
		StaticHashTerms.hsimpli = new SimpliHashTerms();
	}
	
}






















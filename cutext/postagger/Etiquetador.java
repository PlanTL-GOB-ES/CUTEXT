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
 


/*
Treetagger, Geniatagger
*/


package cutext.postagger;

import cutext.util.*;


import java.util.*;
import java.io.*;







public class Etiquetador
{
	
	
	private static final long serialVersionUID = -7149755349268484907L;


	public Etiquetador()
	{
	}


	//public static synchronized void ejecutar(String etiq, String fich, String dirout, String fichtagger)
	public static void ejecutar(String etiq, String fich, String dirout, String fichtagger, String rutaScriptFreeling)
	{
		Runtime aplicacion = Runtime.getRuntime();
	 	try
		{

			//averiguar el sistema operativo
			String so = System.getProperty("os.name").toUpperCase();

			String arg[] = new String[2];
			arg[0] = etiq;
			arg[1] = fich;
			
			
			

/*
File f = new File(etiq);
System.out.println("ruta etiquetador: " + f.getAbsolutePath());
//etiq = f.getAbsolutePath();
f = new File(fich);
System.out.println("ruta fichero: " + f.getAbsolutePath());
*/

			Process tt;

			if(so.startsWith("WINDOWS"))
			{
				//=====
				if(etiq.equals("freelingtagger"))
				{
					System.err.println("\n\t===== Freeling only works under Linux =====\n");
					System.exit(0);
				}
				//=====
				
				
//System.out.println("\tEN WINDOWS\netiq=" + etiq + "\nfich=" + fich + "\nfichtagger=" + fichtagger + "\ndirout=" + dirout);
				tt = aplicacion.exec("cmd.exe /C " + etiq + " " + 
							fich  + " " + //+ Estaticos.FILE_SEP + fichtagger + " " + 
							dirout + Estaticos.FILE_SEP + fichtagger);

				tt.waitFor(); //esperar hasta que acabe el etiquetador

				return;
			}

			
			// Linux
			
			if(etiq.equals("freelingtagger"))
			{
				//exec script := script param1 param2 ... paramN
				//tt = aplicacion.exec("/bin/sh " + ".." + Estaticos.FILE_SEP + "postagger" + Estaticos.FILE_SEP + "scriptFreeling.sh");
				tt = aplicacion.exec("/bin/sh " + rutaScriptFreeling + " " + fich + " " + dirout + Estaticos.FILE_SEP + fichtagger);
			}
			else
			{
				tt = aplicacion.exec(arg);
			}

			BufferedReader stdInput = new BufferedReader(new 
				                 InputStreamReader(tt.getInputStream()));
			BufferedReader stdError = new BufferedReader(new 
				                 InputStreamReader(tt.getErrorStream()));

			cutext.util.Fichero ficheroSalida = new cutext.util.Fichero(dirout, fichtagger);
			String s = null;
			//read the output from the command
			while((s = stdInput.readLine()) != null)
			{
				ficheroSalida.escribir(s + "\n");
				//System.out.println(s);
		    }
			ficheroSalida.cerrar();
            
			//read any errors from the attempted command
			//System.out.println("Here is the standard error of the command (if any):\n");
			//while ((s = stdError.readLine()) != null)
			//{
				//System.out.println(s);
			//}

			tt.waitFor(); //esperar hasta que acabe el etiquetador



		}
		catch(Exception e)
		{
			System.err.println("TAGGER ERROR");
			e.printStackTrace();
			System.exit(0);
		}
	}











}







































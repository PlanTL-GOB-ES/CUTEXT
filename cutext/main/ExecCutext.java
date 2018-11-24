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
* Es el que ejecuta el Algoritmo propiamente dicho.
********************************************************************************************/

package cutext.main;




public class ExecCutext
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	public static void main(String args[])
	{
		int i = 0;
		if((args.length < 1) || (args[i].equals("GUI")))
		{
			i++;
			if(i >= args.length) //user do not changed the routeProperties parameter
			{
				new cutext.gui.NewJFrame(new cutext.prepro.DatosEntrada()).setVisible(true);
			}
			else if(args[i].equals("-routeProperties")) //user changed the routeProperties parameter
			{
				String rp = args[i].replace("/", cutext.util.Estaticos.FILE_SEP);
				rp = rp.replace("\\", cutext.util.Estaticos.FILE_SEP);
				cutext.prepro.DatosEntrada datos = new cutext.prepro.DatosEntrada();
				datos.setRouteProperties(rp);
				new cutext.gui.NewJFrame(datos).setVisible(true);
			}
			else //other parameter has been introduced by user
			{
				System.err.println("The parameter " + args[i] + " is not among the possibilities offered in GUI mode");
				System.exit(0);
			}
		}
		else
		{
			new cutext.textmode.TextMode(args); //modo texto
		}
	}

}





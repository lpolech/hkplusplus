package test;

import data.Parameters;
import utils.CmdLineParser;
import utils.Utils;

public class Test {

	public static void main (String [] args)
	{
		System.out.println( Parameters.getEpsilon());
		System.out.println(Utils.createCsvFileIfNotExists("testcsv.csv"));
		Utils.startTimer();
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Utils.stopTimer();
		System.out.println(Utils.getTimerSeconds());
		System.out.println(Utils.getTimerInMinutes());
		System.out.println(Utils.getTimerInHours());
		
		Jama.Matrix mat= new Jama.Matrix (2,2,1);
		Jama.Matrix mat2= Utils.getDiagonalMatrix(mat);
		mat.print(0, 0);
		mat2.print(0, 0);
		
		System.out.println(mat.equals(mat2));
		//System.out.println(mat.print(arg0, arg1););
		
	
	}
}

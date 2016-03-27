package utils;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import data.Parameters;

public class Utils {
	private static int id = -1;
	private static long timerStart = 0;
	private static long timerStop = 0;
	
	public static boolean isDoubleExtremelyNearZero(double number)
	{
		return Math.abs(number) <= Parameters.getEpsilon();
	}
	
	public static int roundToUpper(double number)
	{
		return (int)(number + 0.5f);
	}

	public static String getStringOfColor(Color colorOnImage) {
		if(colorOnImage == null)
		{
			return "No Color";
		}
		
		int red = colorOnImage.getRed();
		int green = colorOnImage.getGreen();
		int blue = colorOnImage.getBlue();
		
		return red + Constans.delimiter + green + Constans.delimiter + blue ;
	}
	
	public static int getNextId()
	{
		return ++id;
	}
	
	public static Path createCsvFileIfNotExists(String csvFilePath) {
		Path csvFile = Paths.get(csvFilePath);
		if(!Files.exists(csvFile, LinkOption.NOFOLLOW_LINKS))
		{
			try {
				Files.createFile(csvFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Creating csv file: " + csvFile.toString());
		}
		return csvFile;
	}
	
	public static void startTimer()
	{
		timerStart = System.nanoTime();
	}
	
	public static long stopTimer()
	{
		timerStop =  System.nanoTime() - timerStart;
		return timerStart;
	}
	
	public static String getTimerReport()
	{
		String report  = ("Elapsed time: \t" + Double.toString(getTimerSeconds()) + "s.\n"
				+ "\t\t" + Double.toString(getTimerInMinutes()) + "min.\n"
				+ "\t\t" + Double.toString(getTimerInHours()) + "h.");
		return report;
	}
	
	public static double getTimerSeconds()
	{
		return (double)(timerStop/1_000_000_000);
	}
	
	public static double getTimerInMinutes()
	{
		return (double)(timerStop/1_000_000_000)/60;
	}
	
	public static double getTimerInHours()
	{
		return (double)(timerStop/1_000_000_000)/3600;
	}
}

package rna;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CompareStructure {
	// ~ Fields
	static String currDir = System.getProperty("user.dir")
			+ "/";
			//private String currDir = "/home/moose60/Desktop/";
	//String currDir = UniRunner.currDir;
	private String orgMFEStruct;
	private String seqID;
	private int numIdenNa; 
	private int numTotal;
	private int orgHd;
	private float result;

	public CompareStructure(String inputPath, int hamD)
	{
		//parse input
		Input in = new Input(hamD);
		in.parse(inputPath);

		// read parsed input
		orgMFEStruct = in.getStruct();
		orgHd = in.getHd();
		seqID = in.getID();
	}

	/**
	 * Compare originalMFE with mutantMFE
	 */
	public void compareWith()
	{
		// make scanner local
		Scanner scanner = null;
		int index = 0;
		int count = 0;
		File struct = new File(
				currDir + "structures.txt");
		try {
			scanner = new Scanner(struct);
		}
		catch (FileNotFoundException e) {
			System.out.printf("Error: file not found:"
					+ " structures.txt", e);
		}
 
		while (scanner.hasNext())
		{
			index++;
			String curr = scanner.next();
			if (curr.equals(orgMFEStruct))
			{
				count++;
			}
		}
		scanner.close();
		// number of identical natural seq
		numIdenNa = count;
		numTotal = index;
		System.out.println("Number of identical structures: ");
		System.out.println(count);	
	}

	/**
	 * Compare originalMFE with mutantMFE
	 * Overloaded input version
	 */
	public void compareWith(String structPath)
	{
		// make scanner local
		Scanner scanner = null;
		int index = 0;
		int count = 0;
		File struct = new File(structPath);
		try 
		{
			scanner = new Scanner(struct);
		}
		catch (FileNotFoundException e) 
		{
			System.out.printf("Error: file not found:"
					+ " structure file", e);
		}

		while (scanner.hasNext())
		{
			index++;
			String curr = scanner.next();
			if (curr.equals(orgMFEStruct))
			{
				count++;
			}
		}
		scanner.close();
		numIdenNa = count;
		numTotal = index;
		System.out.println(count);	
	}

	/**
	 * Computes the inverse folding rate
	 * for the random pair
	 */
	public void compIFR()
	{
		System.out.println("Mean IFR for native pairs: ");
		result = (float)numIdenNa / numTotal;
		System.out.println(Float.toString(result));
		
		
		// create an input file for RNAinverse
		File f = new File(currDir + "inv.in");
		PrintWriter printer = null;
		
		try {
			printer = new PrintWriter(f);
			printer.println("> struct1");
			printer.println(orgMFEStruct);

		} catch (FileNotFoundException e) {
			System.out.printf("Error: file not found:"
					+ " structure file", e);
		}
		printer.close();
	}
	
	/**
	 * Output the native IFR into a file
	 * File format:
	 * 1st digit: native IFR
	 * rest: derived
	 * 
	 */
	public void outputIFR()
	{
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(currDir + seqID + "_IFRdata.csv", true);
			//print the result for native pair
			fWriter.write(String.valueOf(orgHd)
					+ "," + Float.toString(result) + ",");
			fWriter.close();
		} catch (IOException e) {
			System.out.print("Cannot create file");
			e.printStackTrace();
		}
	}
}

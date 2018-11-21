package rnaInv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

import rna.FileConverter;
import rna.Input;
import rna.UniSampleSeq;

/**
 * Precondition:
 * Another program takes 
 * @author Mia
 *
 */
public class DerivedPair {

	// ~ Fields
	private String currDir = "/Users/Mia/Desktop/";
		//private String currDir = "/home/moose60/Desktop/";
	
	// Number of derived pair
	private int numSeq;

	// Number of samples for each derived pair
	private int numSam;

	// array of sampler
	private UniSampleSeq[] uni;
	private int[] countId;
	private float[] ifr;

	private String originalMFE;
	private int hammingD;
	private LinkedList<String> sequence;

	// object arrays for uniSamplers

	/**
	 * Read the file containing the
	 * inverse folded sequences
	 * @param numberOfDerSeq 
	 * 		Number of derived sequences
	 */
	public DerivedPair(String originalInput,
			int numberOfDerSeq, int numberOfSamSeq)
	{
		Input in = new Input();
		sequence = new LinkedList<String>();
		in.parse(originalInput);
		// header of the new file
		originalMFE = in.getStruct();
		hammingD = in.getHd();
		// number of derived sequence
		numSeq = numberOfDerSeq;
		numSam = numberOfSamSeq;
		uni = new UniSampleSeq[numSeq + 1];
		countId = new int[numSeq + 1];
		ifr = new float[numSeq + 1];

	}

	/**
	 * Clean up the output file from inverse fold
	 * 1. reformat the outputFile as inputFile
	 * 2. eliminate invalid outputs
	 * 3. add sequences to linked list
	 * default file name: invf.txt
	 */
	public void cleanUpInvfOutput(
			String inputPath, String outputPath)
	{
		// Test counter
		int count = 0;

		File invSeq = new File(inputPath);
		File derivedIn = new File(outputPath);
		PrintWriter invWrite = null;
		Scanner invScan = null;
		try
		{
			invScan = new Scanner(invSeq);
			invWrite = new PrintWriter(derivedIn);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		//// File header CONSTANT
		// Hamming distance 1 by default
		invWrite.println(hammingD);
		// prints the structure
		invWrite.println(originalMFE);

		// Test
		int countTotal = 0;
		int rejected = 0;

		// print sequences
		while (invScan.hasNextLine())
		{
			String curr = invScan.nextLine();
			if (!curr.startsWith(">"))
			{
				countTotal++;
				String[] temp = curr.split("\\s+");
				//System.out.println(temp.length);
				if (temp.length == 2)
				{
					// test
					count++;

					invWrite.println(temp[0]);

					// add sequence
					sequence.add(temp[0]);
				}
				else
				{
					rejected++;
				}
			}
		}
		// test
		invWrite.print(count);

		System.out.println("Clean up result for hamming distance"
				+ " " + hammingD);
		System.out.println("Total: " + countTotal);
		System.out.println("Rejected: " + rejected);

		invScan.close();
		invWrite.close();
	}

	/**
	 * Uniform sample sequences for derived pairs
	 * @param numSamples Number of samples generated
	 * for each derived pair
	 */
	public void uniSamDerived(String outputPath)
	{	
		PrintWriter output = null;
		File outputFile = new File(outputPath);
		try {
			output = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			System.out.printf("Error: cannot output data. "
					+ "Class DerivedPair, method unisamDerived", e);
		}
		char[] struct = originalMFE.toCharArray();
		for (int i = 0; i < numSeq; i++)
		{
			//System.out.println("Unisampler: " + i);
			String temp = sequence.remove();
			char[] seq = temp.toCharArray();

			uni[i] = new UniSampleSeq(
					hammingD, seq, struct);
			uni[i].readInput();
			uni[i].createMatrix();
			for (int j = 0; j < numSam; j++)
			{
				//System.out.println("sample number" + j);
				output.println(">");
				uni[i].sampleSeq();
				output.println(uni[i].getResult());
			}

		}
		output.close();
	}


	/**
	 * A modified compare method
	 * compares: each group of structures with 
	 * the original structure
	 * 
	 */
	public void compareStructure()
	{
		FileConverter conv = new FileConverter();
		{
			conv.convertFoldToTxtWMFE(
					currDir + "RNAfold_output.fold",
					currDir + "structGroup.txt");
			Scanner scanner = null;
			File idMFE = new File(currDir + "idMFE.txt");
			PrintWriter print = null;
			File struct = new File(
					currDir + "structGroup.txt");
			try {
				scanner = new Scanner(struct);
				print = new PrintWriter(idMFE);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			for (int i = 0 ; i < numSeq; i++)
			{
				int posCounter = 0;
				for (int j = 0; j < numSam; j++)
				{
					if (scanner.hasNextLine())
					{
						String curr = scanner.nextLine();
						String[] temp = curr.split("\\s");
						if (temp[0].equals(originalMFE))
						{
							posCounter++;
							print.println("[" + i + " " + j + "]");
						}
						else
						{
							// MFE analysis on sequences
							// that do not fold into struct
						}
					}

				}
				countId[i] = posCounter;
			}
			scanner.close();
			print.close();
		}
	}

	/**
	 * Output an array of IFR
	 * 
	 * Write the array into the IFR data file
	 */
	public void outputIFR()
	{
		for (int i = 0; i < numSeq; i++)
		{
			ifr[i] = (float)countId[i] / numSam;
		}

		// print array
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < numSeq; i++)
		{
			if (str.length() > 1)
			{
				str.append(", ");
			}
			str.append(ifr[i]);
		}
		System.out.println("IFR array");
		String ifrArr = str.toString();
		System.out.println(ifrArr);
		
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(currDir + "IFRdata.csv", true);
			fWriter.write(ifrArr + "\n");
			fWriter.close();
		} catch (IOException e) {
			System.out.print("Cannot open file");
			e.printStackTrace();
		}
	}

	/**
	 * Precondition: RNAfold has folded the output
	 * from the previous step
	 * 
	 * Compute IFR for each pair
	 *  -> Store into numerical array
	 *  -> Compute the mean for the array
	 */
	public void meanIFR()
	{
		float total = 0;
		for (int i = 0; i < numSeq; i++)
		{
			total = total + ifr[i];
		}
		float result = total / numSeq;
		System.out.println("Mean IFR for derived pairs: ");
		System.out.println(result);
	}

	/**
	 * For testing purposes
	 * Prints the arrays
	 */
	public void printString()
	{
		StringBuilder str = new StringBuilder();
		str.append("[");
		for (int i = 0; i < numSeq; i++)
		{
			if (str.length() > 1)
			{
				str.append(", ");
			}
			str.append(countId[i]);
		}
		str.append("]");
		System.out.println("Identical counts array");
		System.out.println(str.toString());
	}

	/**
	 * For analysis purposes
	 * convert output file with indices
	 */
	public void convertWIndex()
	{
		File input = new File(currDir + "structGroup.txt");
		File output = new File(currDir + "structGroupAnalysis.csv");
		PrintWriter writer = null;
		Scanner scanner = null;
		try
		{
			writer = new PrintWriter(output);
			scanner = new Scanner(input);
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error: cannot output data. convertWIndex", e);
		}
		String currLine = "";
		boolean start = false;
		for (int i = 0 ; i < numSeq; i++)
		{
			if (start == false)
			{
				writer.print("Index,");
				writer.print("Structure,");
				writer.println("MFE");
				start = true;
			}
			for (int j = 0; j < numSam; j++)
			{
				if (scanner.hasNextLine())
				{
					currLine = scanner.nextLine();
					String[] temp = currLine.split("\\s+");
					writer.print("[" + i
							+ " " + j + "]");
					writer.print(",");
					writer.print(temp[0]);
					writer.print(",");
					writer.println(temp[1]);
				}
			}
		}
		scanner.close();
		writer.close();
	}

	/**
	 * output another file for free energy analysis
	 */
	public void printMFEWIndex()
	{
		File input = new File(currDir + "seq1.fold");
		File output = new File(currDir + "MFEGroupAnalysis.csv");
		PrintWriter writer = null;
		Scanner scanner = null;
		try
		{
			writer = new PrintWriter(output);
			scanner = new Scanner(input);
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error: cannot output data. printMFEWIndex", e);
		}
		String currLine = "";
		int idx = 0;
		boolean start = false;
		while (scanner.hasNext())
		{
			if (start == false)
			{
				writer.print("Index,");
				writer.print("Sequence,");
				writer.print("Structure,");
				writer.print("MFE");
				start = true;
			}
			else
			{
				currLine = scanner.nextLine();
				if (currLine.startsWith(">"))
				{
					idx++;
					writer.println();
				}
				else
				{
					// MFE contains space
					if (currLine.contains(" "))
					{

						String[] temp = currLine.split("\\s+");

						writer.print(temp[0]);
						writer.print(",");
						if (temp[1].equals("("))
						{
							temp[2] = temp[2].
									substring(0, temp[2].length()-1);
							writer.print(temp[2]);
						}
						else //MFE does not contain space
						{
							temp[1] = temp[1].replaceAll("[\\(\\)]", "");
							writer.print(temp[1]);
						}
					}
					//seq
					else
					{
						writer.print(idx);
						writer.print(",");
						writer.print(currLine);
						writer.print(",");
					}
				}
			}
		}
		scanner.close();
		writer.close();
	}
	
	/**
	 * Extract the MfE of sequences that fold into
	 * the same structure
	 */
	public void ExtractIdMFE()
	{
		File inputIndex = new File(currDir + "idMFE.txt");
		File inputData = new File(currDir + "structGroupAnalysis.csv");
		
		File output = new File(currDir + "idMFEGroupAnalysis.csv");
		PrintWriter writer = null;
		Scanner scannerI = null;
		Scanner scannerD = null;
		try
		{
			writer = new PrintWriter(output);
			scannerI = new Scanner(inputIndex);
			scannerD = new Scanner(inputData);
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error: cannot output data. printMFEWIndex", e);
		}
		//String currIndex = "";
		//String currLine = "";
		//LinkedList<String> data = new LinkedList<String>();
		
		boolean start = false;
		while (scannerI.hasNextLine())
		{
			//currIndex = scannerI.nextLine();
			// create a string array
			
		}
		if (start == false)
		{
			writer.print("Index,");
			writer.print("Structure,");
			writer.println("MFE");
			start = true;
		}
		scannerI.close();
		scannerD.close();
		writer.close();
	}
}

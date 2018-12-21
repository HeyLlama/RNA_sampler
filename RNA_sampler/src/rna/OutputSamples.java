package rna;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Output results into files
 * @author Mia
 * @version 2018.5.30
 *
 */
public class OutputSamples {

	// ~ Fields
	private Input input;
	private UniSampleSeq unisam;
	private File outputFile;
	private PrintWriter output;
	
	/**
	 * Initialize field
	 * path is the file path
	 */
	public OutputSamples(String inputPath, String outputPath,
			String outputPathInvf, int hamD)
	{
		input = new Input(hamD);
		input.parse(inputPath, outputPathInvf);
		outputFile = new File(outputPath);
		try {
			output = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			System.out.printf("Error: cannot output data."
					+ " File not found", e);
		}
	}
	
	/**
	 * Process all the inputs
	 */
	public void process(int numSamples)
	{
		int inputSize = input.getSize();
		for (int i = 0; i < inputSize; i++)
		{
			char[] seq = input.getSeq(
					).toCharArray();
			char[] struct = input.getStruct(
					).toCharArray();
			int hamd = input.getHd();
			
			unisam = new UniSampleSeq(
					hamd, seq, struct);
			unisam.readInput();
			unisam.createMatrix();
			
			//unisam.printMatrix();

			for (int j = 0; j < numSamples; j++)
			{
				unisam.sampleSeq();
				output.println(unisam.getResult());
			}
			
			// output divider
			output.println("//");
			// reduce the number 
			//  of inputs to be processed
			input.reduce();
		}
		output.close();
	}
	
	/**
	 * Process all the inputs
	 * Test
	 * Check partition matrix
	 */
	public void printPartitionMatrix()
	{
		int inputSize = input.getSize();
		for (int i = 0; i < inputSize; i++)
		{
			char[] seq = input.getSeq(
					).toCharArray();
			char[] struct = input.getStruct(
					).toCharArray();
			int hamd = input.getHd();
			
			unisam = new UniSampleSeq(
					hamd, seq, struct);
			unisam.readInput();
			unisam.createMatrix();
			// prints the partition matrix
			unisam.printMatrix();
			
			// output divider
			output.println();
			// reduce the number 
			//  of inputs to be processed
			input.reduce();
		}
		output.close();
	}
}

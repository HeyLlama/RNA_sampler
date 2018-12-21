package rna;

import org.junit.jupiter.api.Test;

public class OutputSamplesTest {
	//String currDir = UniRunner.currDir;
	private String currDir = "/Users/Mia/Desktop/";
		//private String currDir = "/home/moose60/Desktop/";
	private String inputPath = 
			currDir + "Test_input.txt";
	private String outputPath = 
			currDir + "Test_uniform.txt";
	private String outputForInvf =
			currDir + "inv.in";
	/**
	 * Prints the matrix
	 */
	@Test
	public void testPrintMatrix()
	{
		OutputSamples out = new OutputSamples(
				inputPath, outputPath, outputForInvf, 1);
		out.printPartitionMatrix();	
	}
	
	/**
	 * Prints the output
	 */
	@Test
	public void testOutput()
	{
		OutputSamples out = new OutputSamples(
				inputPath, outputPath, outputForInvf, 1);
		out.process(100);
	}
	
	/**
	 * Generate a sequence file for testing
	 */
	@Test
	public void testFile()
	{
		OutputSamples out = new OutputSamples(
				inputPath, outputPath, outputForInvf, 1);
		out.process(1000);
	}

}

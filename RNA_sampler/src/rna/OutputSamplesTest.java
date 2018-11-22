package rna;

import org.junit.jupiter.api.Test;

public class OutputSamplesTest {
	String currDir = UniRunner.currDir;
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
				inputPath, outputPath, outputForInvf);
		out.printPartitionMatrix();	
	}
	
	/**
	 * Prints the output
	 */
	@Test
	public void testOutput()
	{
		OutputSamples out = new OutputSamples(
				inputPath, outputPath, outputForInvf);
		out.process(10);
	}
	
	/**
	 * Generate a sequence file for testing
	 */
	@Test
	public void testFile()
	{
		OutputSamples out = new OutputSamples(
				inputPath, outputPath, outputForInvf);
		out.process(1000);
	}

}

package rna;

import org.junit.jupiter.api.Test;


public class UniSampleSeqTest{
	private String currDir = "/Users/Mia/Desktop/";
			//private String currDir = "/home/moose60/Desktop/";
	//String currDir = UniRunner.currDir;
	private final String inputPath = 
			currDir + "Test_input.txt";
	private final String outputPath = 
			currDir + "Test_uniform.txt";
	private String outputForInvf =
			currDir + "inv.in";
	
	/**
	 * Test sampling output
	 */
	@Test
	public void testOutputSamples()
	{
		OutputSamples out = new OutputSamples(
				inputPath, outputPath, outputForInvf, 1);
		int numSamples = 10;
		out.process(numSamples);
	}
}

package rnaInv;

import org.junit.jupiter.api.Test;

public class DerivedPairTest {
	private String currDir = "/Users/Mia/Desktop/";
		//private String currDir = "/home/moose60/Desktop/";
	private DerivedPair der;
	
	//@Test
	public void testClean()
	{
		der = new DerivedPair(currDir +
				"Test_input.txt",
				100, 100, 1);
		//
		der.cleanUpInvfOutput(currDir + "invf.txt",
				currDir + "Test_Derived.txt");
		//der.uniSamDerived(85, 
		//		"/Users/Mia/Desktop/Test_DerivedSamples.txt");
	}
	
	@Test
	public void testDerivedProcedure()
	{
		// compute the inverse fold first
		// terminal produce invf output
					// !!! Separate this in main
		
		// Produce derived sequences
		der = new DerivedPair(
				currDir + "Test_input.txt",
				100, 100, 1);
		
		der.cleanUpInvfOutput(currDir + "invf.txt",
				currDir + "Test_Derived.txt");
		
		// generate n sequences for each pair
		der.uniSamDerived(
				currDir + "Test_DerivedSamples.fasta");
		
		// !!! fold the sequences
			// done in terminal
		
		//default path /Users/Mia/Desktop/RNAfold_output.fold
		// compare structures
		der.compareStructure();
		der.printString();
		der.outputIFR();
		der.meanIFR();
		
		// Further analysis
		//der.convertWIndex();
		//der.printMFEWIndex();
		//comp.compareWith("/Users/Mia/Desktop/structures.txt");
		//System.out.println("Comparison completed!");
		
	}
	
	@Test
	public void testSeparationP1()
	{
		DerivedPair der = new DerivedPair(
				currDir + "Test_input.txt",
				100, 100, 1);
		
		der.cleanUpInvfOutput(currDir + "invf.txt",
				currDir + "Test_Derived.txt");
		
		// generate n sequences for each pair
		der.uniSamDerived(
				currDir + "Test_DerivedSamples.fasta");
	}
	
	@Test
	public void testSeparationP2()
	{
		DerivedPair der = new DerivedPair(
				currDir + "Test_input.txt",
				100, 100, 1);
		
		der.cleanUpInvfOutput(currDir + "invf.txt",
				currDir + "Test_Derived.txt");
		
		// generate n sequences for each pair
		der.uniSamDerived(
				currDir + "Test_DerivedSamples.fasta");
	}
}

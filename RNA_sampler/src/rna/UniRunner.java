package rna;

import java.io.File;

import rnaInv.DerivedPair;

public class UniRunner {
	private static String currDir = "/Users/Mia/Desktop/";
		//private static String currDir = "/home/moose60/Desktop/";
	/**
	 * The input form the terminal will
	 * be the paths for the input and 
	 * output files path
	 * @param args 1.input 2.output
	 * 	3.number of samples for each input
	 */
	public static void main(String[] args)
	{
		// if user given arguments
		if (args.length == 4)
		{
			// takes the first path
			File newFile = new File(args[0]);
			if (newFile.isDirectory())
			{
				OutputSamples out = new OutputSamples(
						args[0],args[1], args[2]);
				int numSamples = 
						Integer.parseInt(args[3]);
				out.process(numSamples);
			}
		}
		// First parameter function name
		else if (args.length == 1)
		{
			// Folds the original sequence
			// write to a input file
			if (args[0].equals("WriteInput"))
			{
				FileConverter converter = new FileConverter();
				converter.convertFASTAtoInput(
						currDir + "InputSequence.fasta",
						currDir + "Test_input.txt");
			}
			// assuming .fold file exist
			else if (args[0].equals("Convert"))
			{
				FileConverter converter = new FileConverter();
				converter.convertFoldToTxt(currDir + "seq1.fold",
						currDir + "structures.txt");
			}


			/*
			// convert structure text to stdin file
			else if (args[0].equals("ConvertStruct"))
			{
				FileConverter converter = new FileConverter();
				converter.convertTxtToFASTAstruct(
						"/Users/Mia/Desktop/structures.txt",
						"/Users/Mia/Desktop/invf.in");
				System.out.println("Conversion for structures completed!");
			}
			 */

			// compare structures
			// gives the IFR for native pair
			else if (args[0].equals("CompareStruct"))
			{
				CompareStructure comp = new CompareStructure(
						currDir + "Test_input.txt");
				comp.compareWith(currDir + "structures.txt");
				comp.compIFR();
				comp.outputIFR();
			}

			// compare inverseFold sequence with original sequence
			else if (args[0].equals("CompareInvf"))
			{
				CompareInverse inv = new CompareInverse(
						currDir + "Test_input.txt");
				inv.compareWith(currDir + "invf.txt");
				System.out.println("Inverse fold sequence"
						+ " comparison completed!");
			}
			


		}
		
		// Parameters
		// 1. Function name
		// 2. Number of desired samples
		else if (args.length == 2)
		{
			if (args[0].equals("Process"))
			{
				OutputSamples out = new OutputSamples(
						currDir + "Test_input.txt",
						currDir + "Test_uniform.txt",
						currDir + "inv.in");
				out.process(Integer.valueOf(args[1]));
				FileConverter converter = new FileConverter();

				converter.convertTxtToFASTAseq(
						currDir + "Test_uniform.txt",
						currDir + "Test_uniform_out.fasta");
				System.out.println("Sample completed!");
			}
		}
		// Parameters
		// 1: function name
		// 2: number of derived sequences
		// 3: number of desired samples
 
		else if (args.length == 3)
		{
			// Computes the derived sequences
			// computes the IFR for the derived pair
			if (args[0].equals("CleanAndDerivedSeq"))
			{
				// Produce derived sequences
				DerivedPair der = new DerivedPair(
						currDir + "Test_input.txt",
						Integer.valueOf(args[1]),
						Integer.valueOf(args[2]));

				der.cleanUpInvfOutput(currDir + "invf.txt",
						currDir + "Test_Derived.txt");

				// generate n sequences for each pair
				der.uniSamDerived(currDir + "Test_DerivedSamples.fasta");
			}

			// This begins after the sequences are folded
			// This completes the operation by comparing 
			// the derived structures with the original one
			else if (args[0].equals("CompareInvFold"))
			{
				DerivedPair der = new DerivedPair(
						currDir + "Test_input.txt",
						Integer.valueOf(args[1]),
						Integer.valueOf(args[2]));
				der.compareStructure();
				der.printString();
				der.outputIFR();
				der.meanIFR();
				// Test
				//der.convertWIndex();
				//der.printMFEWIndex();
			}
		}
	}
}

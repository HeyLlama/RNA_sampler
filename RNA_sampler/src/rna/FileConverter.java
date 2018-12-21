package rna;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Reads a txt file
 * 
 */
public class FileConverter {

	private PrintWriter writer;
	private Scanner scanner;
	private File input;
	private File output;
	
	private PrintWriter structW;
	private File structOut;
	/**
	 * Constructor
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	public FileConverter()
	{
		// this constructor is intentionally left empty
	}
	
	/**
	 * Convert FASTA sequence file to input text file
	 */
	public void convertFASTAtoInput(String inputPath,
			String outputPath)
	{
		input = new File(inputPath);
		output = new File(outputPath);
		try
		{
			writer = new PrintWriter(output);
			scanner = new Scanner(input);
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error: cannot output data.", e);
		}
		String seq = "";
		int idx = 1;
		while (scanner.hasNextLine())
		{
			seq = scanner.nextLine();
			writer.println(idx);
			writer.println(seq);
		}
		scanner.close();
		writer.close();
	}
	/**
	 * Convert txt file to FASTA file
	 * Converter for sequences
	 * @param inputPath 
	 * @param outputPath 
	 */
	public void convertTxtToFASTAseq(String inputPath,
			String outputPath)
	{
		input = new File(inputPath);
		output = new File(outputPath);
		try
		{
			writer = new PrintWriter(output);
			scanner = new Scanner(input);
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error: cannot output data. Conversion", e);
		}
		String currLine = "";
		int seqIdx = 1;
		while (scanner.hasNext())
		{
			currLine = scanner.next();
			if (!currLine.equals("//"))
			{
				writer.println("> seq" + seqIdx);
				writer.println(currLine);
			}
			else
			{
				seqIdx++;
			}
		}
		scanner.close();
		writer.close();
	}
	
	/**
	 * Convert txt file to FASTA file
	 * Converter for sequences
	 * @param inputPath 
	 * @param outputPath 
	 */
	public void convertTxtToFASTAstruct(String inputPath,
			String outputPath)
	{
		input = new File(inputPath);
		output = new File(outputPath);
		try
		{
			scanner = new Scanner(input);
			writer = new PrintWriter(output);
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error: cannot output data.", e);
		}
		String currLine = "";
		int seqIdx = 1;
		while (scanner.hasNext())
		{
			currLine = scanner.next();
			writer.println("> struct" + seqIdx);
			writer.println(currLine);
			writer.println();
		}
		scanner.close();
		writer.close();
	}
	
	/**
	 * Convert fold file to csv file
	 * @param inputPath 
	 * @param outputPath 
	 */
	public void convertFoldToCSV(String inputPath,String outputPath)
	{
		input = new File(inputPath);
		output = new File(outputPath);
		try
		{
			writer = new PrintWriter(output);
			scanner = new Scanner(input);
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error: cannot output data.", e);
		}
		Boolean start = false;
		String currLine = "";
		while (scanner.hasNext())
		{
			if (start == false)
			{
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
					writer.println();
				}
				else
				{
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
					else
					{
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
	 * Convert fold file to txt file
	 * @param inputPath 
	 * @param outputPath 
	 */
	public void convertFoldToTxt(String inputPath,
			String outputPath)
	{
		structOut = new File(outputPath);
		try {
			structW = new PrintWriter(structOut);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input = new File(inputPath);
		try
		{
			scanner = new Scanner(input);
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Error: cannot output data.", e);
		}
		String currLine = "";
		while (scanner.hasNext())
		{
			currLine = scanner.nextLine();
			if (currLine.startsWith(">"))
			{
				//
			}
			else
			{
				if (currLine.contains(" "))
				{
					String[] temp = currLine.split("\\s+");
					structW.println(temp[0]);
				}
				else
				{
					// tba
				}			
			}
		}
		scanner.close();
		structW.close();
	}
	
	/**
	 * Convert fold file to txt file
	 * @param inputPath 
	 * @param outputPath 
	 */
	public void convertFoldToTxtWMFE(String inputPath,
			String outputPath)
	{
		structOut = new File(outputPath);
		input = new File(inputPath);
		try {
			scanner = new Scanner(input);
			structW = new PrintWriter(structOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String currLine = "";
		while (scanner.hasNext())
		{
			currLine = scanner.nextLine();
			if (currLine.startsWith(">"))
			{
				//
			}
			else
			{
				if (currLine.contains(" "))
				{
					String[] temp = currLine.split("\\s+");
					structW.print(temp[0]);
					structW.print(" ");
					if (temp[1].equals("("))
					{
						temp[2] = temp[2].
								substring(0, temp[2].length()-1);
						structW.println(temp[2]);
					}
					else //MFE does not contain space
					{
						temp[1] = temp[1].replaceAll("[\\(\\)]", "");
						structW.println(temp[1]);
					}
				}
				else
				{
					// tba
				}			
			}
		}
		scanner.close();
		structW.close();
	}
}

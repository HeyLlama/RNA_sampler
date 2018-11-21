package rna;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class CompareInverse {

	// ~ Fields
	private String originalStruct;
	private int numIdenRa;
	private int numTotal;
	
	public CompareInverse(String inputPath)
	{
		Input in = new Input();
		in.parse(inputPath);
		
		// read the original structure
		originalStruct = in.getStruct();
	}
	
	/**
	 * Compare original sequence with inverse fold
	 */
	public void compareWith(String path)
	{
		Scanner scanner = null;
		// counts the number of sequence
		int index = 0;
		// count the identical sequences
		int count = 0;
		// the location of the invf result
		File invSeq = new File(path);
		try
		{
			scanner = new Scanner(invSeq);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		while (scanner.hasNextLine())
		{
			String curr = scanner.nextLine();
			if (!curr.startsWith(">"))
			{
				String[] temp = curr.split("\\s+");
				index++;
				//System.out.println(temp.length);
				if (temp.length == 2)
				{
					if (temp[0].equals(originalStruct))
					{
						count++;
						//System.out.println("hey");
					}
					
				}
			}
		}
		scanner.close();
		numIdenRa = count;
		numTotal = index;
		System.out.println(count);
	}
	
	/**
	 * Computes the inverse fold rate
	 * for the native pair
	 */
	public void compIFR()
	{
		System.out.println("The IFR for derived pair is:");
		float result = numIdenRa / numTotal;
		System.out.println(Float.toString(result));
	}
}

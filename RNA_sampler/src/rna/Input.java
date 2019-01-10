package rna;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
//import java.io.IOException;
//import java.io.PrintWriter;
import java.util.Scanner;
/**
 * This input class will read the
 * input file and create files for
 * other programs in this package
 * @author Mia
 *
 */
public class Input {
	private LinkedList<String> seqID;
	private int size; 
	// index for counting inputs
	//supports multiple input
	private int hd;
	private LinkedList<String> seq;
	private LinkedList<String> struct;

	public Input(int hamD)
	{
		seqID = new LinkedList<String>();
		size = 0;
		hd = hamD;
		seq = new LinkedList<String>();
		struct = new LinkedList<String>();
	}


	public int getSize()
	{
		return size;
	}

	public void reduce()
	{
		size--;
	}

	public int getHd()
	{
		return hd;
	}

	/**
	 * Returns the sequence
	 */
	public String getSeq()
	{
		return seq.remove();
	}

	/**
	 * Returns the structure
	 */
	public String getStruct()
	{
		return struct.remove();
	}

	/**
	 * Read from sample input
	 * This method creates a new file for inverse fold
	 */
	public void parse(String path, String outputFilePath)
	{
		try
		{
			File file = new File(path);
			Scanner input = new Scanner(file);
			// create new file for inverse fold program
			File structFile = new File(outputFilePath);
			PrintWriter output = new PrintWriter(structFile);
			while (input.hasNextLine())
			{
				if (!input.hasNext())
				{
					break;
				}
				seqID.add(input.next());
				seq.add(input.next());
				
					String temp = input.next();
				struct.add(temp);
					// Prints the structure as input for inv. fold
					output.println(temp);
			}
			input.close();
			output.close();
			size = seq.size();		
		}
		catch (FileNotFoundException exc)
		{
			System.out.printf("Error: file not found.", exc);
		}
	}
	
	/**
	 * A parsing method that reads the sequence data
	 * @param path Input file path
	 */
	public void parse(String path)
	{
		try
		{
			File file = new File(path);
			Scanner input = new Scanner(file);
			while (input.hasNextLine())
			{
				if (!input.hasNext())
				{
					break;
				}
				seqID.add(input.next());
				seq.add(input.next());
				struct.add(input.next());
			}
			input.close();
			size = seq.size();		
		}
		catch (FileNotFoundException exc)
		{
			System.out.printf("Error: file not found:"
					+ " input file", exc);
		}
	}

	public String getID()
	{
		return seqID.remove();
	}

	public boolean writeOutput(String path)
	{
		try
		{
			File file = new File(path);
			PrintWriter output = new PrintWriter(file);

			output.println(toStringStr(seq));
			output.println(toStringStr(struct));
			output.println(hd);
			output.close();


		}
		catch (IOException exc)
		{
			System.out.printf("Error: file not found:"
					+ " input file", exc);
		}
		return true;
	}

	public String toStringStr(LinkedList<String> arg)
	{
		StringBuilder str = new StringBuilder();
		str.append("[");
		for (String s : arg)
		{
			if (str.length() > 1)
			{
				str.append(", ");
			}
			str.append(s);
		}
		str.append("]");
		return str.toString();
	}

	public String toStringInt(LinkedList<Integer> arg)
	{
		StringBuilder str = new StringBuilder();
		str.append("[");
		for (Integer i: arg)
		{
			if (str.length() > 1)
			{
				str.append(", ");
			}
			str.append(i);
		}
		str.append("]");
		return str.toString();
	}




}

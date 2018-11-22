package rna;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class CheckCompatibility
{
	private LinkedList<Integer> hd;
	private LinkedList<String> seq;
	private LinkedList<String> outputSeq;
	private LinkedList<String> struct;
	private int currSeqLen;
	private int counter;
	private static final String[] pool = { "AU", "UA", "GC", "CG", "GU", "UG" };

	public CheckCompatibility()
	{
		hd = new LinkedList<Integer>();
		seq = new LinkedList<String>();
		outputSeq = new LinkedList<String>();
		struct = new LinkedList<String>();
	}






	public void compare()
	{
		String currentSeq = (String)seq.remove();
		char[] localStruct = 
				((String)struct.remove()).toCharArray();
		currSeqLen = currentSeq.length();

		while (outputSeq.size() != 0)
		{
			String currentOut = (String)outputSeq.remove();
			if (currentOut.equals("//"))
			{
				if (seq.size() == 0) {
					break;
				}

				currentSeq = (String)seq.remove();
				localStruct = 
						((String)struct.remove()).toCharArray();
			}
			else
			{
				boolean result = 
						compareWith(localStruct, currentOut);
				counter += 1;

				if (!result)
				{
					System.out.println("Error!");
				}
			}
		}
	}

	public int getCount()
	{
		return counter;
	}


	public boolean compareWith(char[] struct, String seq)
	{
		boolean res = false;
		char[] localOut = seq.toCharArray();
		Stack<Integer> deck = new Stack<Integer>();
		int[] temp = new int[2];
		char[] pairTemp = new char[2];

		for (int scan = 0; scan < currSeqLen; scan++)
		{
			if (struct[scan] == '(')
			{
				deck.push(Integer.valueOf(scan));
			}
			else if (struct[scan] == ')')
			{
				temp[0] = ((Integer)deck.pop()).intValue();
				temp[1] = scan;
				pairTemp[0] = localOut[temp[0]];
				pairTemp[1] = localOut[temp[1]];
				String pair = String.valueOf(
						pairTemp);
				res = isPair(pair);
				if (!res)
				{
					return false;
				}
			}
		}
		return true;
	}


	public boolean isPair(String pair)
	{
		for (int i = 0; i < pool.length; i++)
		{
			if (pair.equals(pool[i]))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Reads input file as template
	 * @param pathIn
	 * @param pathOut
	 */
	public void read(String pathIn, String pathOut)
	{
		try
		{
			File file = new File(pathIn);
			Scanner input = new Scanner(file);
			while (input.hasNextLine())
			{
				input.next();
				seq.add(input.next());
				struct.add(input.next());
				hd.add(Integer.valueOf(Integer.parseInt(input.next())));
			}
			input.close();

		}
		catch (FileNotFoundException exc)
		{
			System.out.printf("Error: file not found.", exc);
		}

		try
		{
			File file = new File(pathOut);
			Scanner in = new Scanner(file);
			while (in.hasNextLine())
			{
				outputSeq.add(in.nextLine());
			}

			in.close();

		}
		catch (FileNotFoundException exc)
		{
			System.out.printf("Error: file not found.", exc);
		}
	}

	/**
	 * Convert linkedList to string
	 * @param arg List to be converted
	 * @return A string
	 */
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

	/** 
	 * Checks the Hamming distance
	 */
	public void checkHam()
	{
		int hammd = 0;
		int currHd = ((Integer)hd.remove()).intValue();
		char[] currentSeq = ((String)seq.remove())
				.toCharArray();
		while (outputSeq.size() != 0)
		{
			String currentOut = (String)outputSeq.remove();
			char[] outArray = currentOut.toCharArray();
			if (currentOut.equals("//"))
			{
				if (seq.size() == 0) {
					break;
				}

				currentSeq = 
						((String)seq.remove()).toCharArray();
				currHd = ((Integer)hd.remove()).intValue();
			}
			else
			{
				hammd = 0;
				for (int i = 0; i < outArray.length; i++)
				{
					if (currentSeq[i] != outArray[i])
					{
						hammd++;
					}
				}
				System.out.println("Count:" + hammd);
				if (hammd != currHd)
				{
					System.out.println("Error!");
				}
			}
		}
	}
}

package rna;

import java.util.Stack;
//import java.util.concurrent.ThreadLocalRandom;
import java.math.BigInteger;
import java.util.Random;
/**
 * The sampling methods
 * @author Mia
 * @version 2018.5.30
 *
 */
public class UniSampleSeq {
	
	// ~ Fields
	private int a;
	private int b;
	private int c;
	private int seqlen;
	private int hd;
	private char[] seq;
	private char[] struct;
	private BigInteger[][][][] parmat;
	
	// temporary fields for sampling
	private int single;
	private int wc;
	private int wob;
	private int ham;
	// a box of pairs
	private static final String[] box = 
		{"AU", "UA", "GC", "CG", "GU", "UG"};
	// a box of unpaired
	private static final char[] unpairBox =
		{'A', 'G', 'C', 'U'};
	
	// a pool for available mutations
	private String[] pool;
	private int poolLen;
	
	private char[] singPool;
	// a uniform sampled compatible sequence
	private char[] comSeq;

	/*
	 * Documentation:
	 * 
	 * Sample input
	 * Neu : UGCCAAUCCG
	 * .() : ((.))()(.)
	 * 
	 * Given the number of:
	 * 1.unpaired, denoted a
	 * 2.paired (Watson-Crick or Wobble), denoted b or c
	 * 3.Hamming distance, denoted hd
	 * Output a compatible sequence with given Hamming distance
	 * 
	 * Recursive rules:
	 * 1.a is the unpaired;
	 * 2.b is the Watson-Crick;
	 * 3.c is the Wobble
	 * 
	 * 1.PM(a,b,c,Hd) = PM(a-1,b,c,Hd) + 3PM(a-1,b,c,Hd-1)
	 * 2.PM(a,b,c,Hd) = PM(a,b-1,c,Hd) + PM(a,b-1,c,Hd-1) + 4PM(a,b-1,c,Hd-2)
	 * 3.PM(a,b,c,Hd) = PM(a,b,c-1,Hd) + 2PM(a,b,c-1,Hd-1) + 3PM(a,b,c-1,Hd-2)
	 * 
	 * Base case:
	 * base case 1: Hd = 0, DNE
	 * base case 2: a = 0, b = 0, c = 0, DNE
	 * base case 3: a = 0, Hd = 1, DNE
	 */
	
	/**
	 * The constructor
	 */
	public UniSampleSeq(int ihd, char[] iseq, char[] istruct)
	{
		hd = ihd;
		seq = iseq;
		struct = istruct;
		seqlen = seq.length;
		//comSeq = new char[seqlen];
	}
	
	
	public UniSampleSeq(String path, int hamD)
	{
		Input in = new Input(hamD);
		in.parse(path);	
		hd = in.getHd();
		seq = in.getSeq().toCharArray();
		struct = in.getStruct().toCharArray();
		seqlen = seq.length;
	 
		comSeq = new char[seqlen];
	}
	
	/**
	 * Read nucleotide and structure
	 */
	public void readInput()
	{
		Stack<Integer> deck = new Stack<Integer>();
		int[] temp = new int[2];
		char[] pairTemp = new char[2];
		// scan seq with struct
		for (int scan = 0; scan < seqlen; scan++)
		{
			if (struct[scan] == '(')
			{
				deck.push(scan);
			}
			else if (struct[scan] == ')')
			{
				temp[0] = deck.pop();
				temp[1] = scan;
				pairTemp[0] = seq[temp[0]];
				pairTemp[1] = seq[temp[1]];
				// convert to string for comparison
				String unknownPair = String.valueOf(pairTemp);
				// Watson Crick
				if (unknownPair.equals("AU") || unknownPair.equals("UA"))
				{
					b++;
				}
				else if (unknownPair.equals("GC") || unknownPair.equals("CG"))
				{
					b++;
				}
				// Wobble Pair
				else if (unknownPair.equals("GU") || unknownPair.equals("UG"))
				{
					c++;
				}
			}
			// unpaired
			else
			{
				a++;
			}
				
		}
	}
	
	/**
	 * Creates a matrix
	 */
	public void createMatrix()
	{
		createMatrix(a, b, c, hd);
	}
	
	/**
	 * Fill the partition matrix
	 */
	private void createMatrix(int sing, int wcc, int wobb, int hamm)
	{
		parmat = new BigInteger[sing+1][wcc+1][wobb+1][hamm+1];
		// base cases
		
		// 1. hd = 0, DNE
		for (int k = 0; k <= c; k++)
		{
			for (int j = 0; j <= b; j++)
			{
				for (int i = 0; i <= a; i++)
				{
					parmat[i][j][k][0] = BigInteger.valueOf(1);
				}
			}
		}
		
		// 2. a = 0; b = 0; c = 0; DNE
		parmat[0][0][0][0] = BigInteger.valueOf(1);
		for (int l = 1; l <= hd; l++)
		{
			parmat[0][0][0][l] = BigInteger.valueOf(0);
		}
		
		// 3. a = 0; hd = 1; DNE
		for (int k = 0; k <= c; k++)
		{
			for (int j = 0; j <= b; j++)
			{
				parmat[0][j][k][1] = BigInteger.valueOf(j + 2*k);
			}
		}
		
		// recursive fill
		for (int l = 0; l <= hd; l++)
		{
			for (int i = 0; i <= a; i++)
			{
				for (int j = 0; j <= b; j++)
				{
					for (int k = 0; k <= c; k++)
					{
						if (i > 0 && l > 0)
						{
							parmat[i][j][k][l] = parmat[i-1][j][k][l].add(
									parmat[i-1][j][k][l-1]
											.multiply(BigInteger.valueOf(3)));
						}
						else if (j > 0 && l > 1)
						{
							parmat[i][j][k][l] = (parmat[i][j-1][k][l]
									.add(parmat[i][j-1][k][l-1]))
											.add(parmat[i][j-1][k][l-2]
													.multiply(
															BigInteger.valueOf(4)));
						}
						else if (k > 0 && l > 1)
						{
							parmat[i][j][k][l] = (parmat[i][j][k-1][l]
									.add(parmat[i][j][k-1][l-1].multiply(
											BigInteger.valueOf(2))))
											.add(parmat[i][j][k-1][l-2]
													.multiply(
													BigInteger.valueOf(3)));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Uniform sample using the partition matrix
	 */
	public void sampleSeq()
	{
		
		// pivots, initialize fields
		single  = a;
		wc = b;
		wob = c;
		ham = hd;
		
		// initialize
		Stack<Integer> duck = new Stack<Integer>();
		int[] tempMuteIdx = new int[2];
		char[] tempPair = new char[2];
		String tbdPair = "";
		comSeq = new char[seqlen];
		
		
		
		for (int scan2 = 0; scan2 < seqlen; scan2++)
		{
			if (struct[scan2] == '(')
			{
				duck.push(scan2);
			}
			else if (struct[scan2] == ')')
			{
				tempMuteIdx[0] = duck.pop();
				tempMuteIdx[1] = scan2;
				tempPair[0] = seq[tempMuteIdx[0]];
				tempPair[1] = seq[tempMuteIdx[1]];
				tbdPair = String.valueOf(tempPair);
				//System.out.println(tbdPair);
				//determine pair type; (boolean)
				
				// Watson Crick
				boolean wc1 = tbdPair.equals("AU") || tbdPair.equals("UA");
				boolean wc2 = tbdPair.equals("GC") || tbdPair.equals("CG");
				if (wc1 || wc2)
				{
					ruleTwo(tempPair, tempMuteIdx);
				}
				
				// Wobble Pair
				else if (tbdPair.equals("GU") || tbdPair.equals("UG"))
				{
					ruleThree(tempPair, tempMuteIdx);
				}
			}
			// unpaired
			else if (struct[scan2] == '.')
			{
				char tempSingle = seq[scan2];
				int tempSingleIdx = scan2;
				ruleOne(tempSingle, tempSingleIdx);
			}
		}
	}
	
	

	/**
	 * Generate a pool for choice of mutation
	 */
	private void createPool(char[] tempPair, int target)
	{
		pool = new String[4];
		poolLen = 0;
		int idx = 0;
		for (int i = 0; i < 6; i++)
		{
			//choose from box
			char[] tp = box[i].toCharArray();
			//storage index
			int numMute = 0;
			
			// compare 
			if (tempPair[0] != tp[0])
			{
				numMute++;
			}
			if (tempPair[1] != tp[1])
			{
				numMute++;
			}
			// if the pair in box satisfies the score
			// put it into the pool
			if (numMute == target)
			{
				pool[idx] = box[i];
				idx++;
				poolLen++;
			}
		}
	}
	
	/**
	 * Watson-Crick
	 * Helper method for rule 2 mutation
	 */
	private void ruleTwo(char[] tempPair, int[] tempMuteIndex)
	{
		BigInteger currentScore = parmat[single][wc][wob][ham];
		// generate a random number of range 1 to score from matrix
		//Random randi = new Random();
		//long prob_WC = ThreadLocalRandom.current().nextLong(currentScore) + 1;
		//int prob_WC = randi.nextLong(currentScore) + 1;
		
		// big Integer random generator
		Random randi = new Random();
		
		BigInteger prob_WC = BigInteger.valueOf(0);
		do {
			prob_WC = new BigInteger(currentScore.bitLength(), randi);
			prob_WC = prob_WC.add(BigInteger.valueOf(1));
		}
		while (prob_WC.compareTo(currentScore) > 0);
		
		BigInteger mu1_WC = BigInteger.valueOf(0);
		BigInteger mu2_WC = BigInteger.valueOf(0);
		
		if (ham > 0)
		{
			mu1_WC = parmat[single][wc-1][wob][ham-1];
		}
		if (ham > 1)
		{
			mu2_WC = parmat[single][wc-1][wob][ham-2]
						.multiply(BigInteger.valueOf(4));
		}
		BigInteger noMute = parmat[single][wc-1][wob][ham];
		
		// No mutation
		//if (prob_WC <= noMute)
		if (prob_WC.compareTo(noMute) <= 0)
		{
			comSeq[tempMuteIndex[0]] = tempPair[0];
			comSeq[tempMuteIndex[1]] = tempPair[1];
			wc--;
		}
		
		// mutate 1
		//else if ((prob_WC <= mu1_WC + noMute) && (prob_WC > noMute))
		else if ((prob_WC.compareTo(mu1_WC.add(noMute))  <= 0)
				&& (prob_WC.compareTo(noMute) > 0))
		{
			createPool(tempPair, 1);
			
				//assert poolLen == 1;
			
			// only one option
			String result = pool[0];
			char[] resultChar = result.toCharArray();
			comSeq[tempMuteIndex[0]] = resultChar[0];
			comSeq[tempMuteIndex[1]] = resultChar[1];
			// reduce the parameter
			wc--;
			ham--;
		}
		
		// mutate 2
		//else if ((prob_WC <= mu1_WC + noMute + mu2_WC) && (prob_WC > mu1_WC + noMute))
		else if ((prob_WC.compareTo(mu1_WC.add(noMute).add(mu2_WC)) <= 0)
				&& (prob_WC.compareTo(mu1_WC.add(noMute)) > 0))
		{
			createPool(tempPair, 2);
			int randInt = randi.nextInt(4);
			
				//assert poolLen == 4;
				
			String result = pool[randInt];
			char[] resultChar = result.toCharArray();
			comSeq[tempMuteIndex[0]] = resultChar[0];
			comSeq[tempMuteIndex[1]] = resultChar[1];
			// reduce the parameter
			wc--;
			ham-=2;
		}
	}
	
	/**
	 * Wobble pair
	 * Helper method for rule 3
	 */
	private void ruleThree(char[] tempPair, int[] tempMuteIndex)
	{
		// mutate
		BigInteger currentScore = parmat[single][wc][wob][ham];
		// generate a random number of range 1 to score from matrix
		//Random randi = new Random();
		//long prob_Wob = ThreadLocalRandom.current().nextLong(currentScore) + 1;
		//int prob_Wob = randi.nextInt(currentScore) + 1;
		
		Random randi = new Random();
		
		
		BigInteger prob_Wob = BigInteger.valueOf(0);
		do {
			prob_Wob = new BigInteger(currentScore.bitLength(), randi);
			prob_Wob = prob_Wob.add(BigInteger.valueOf(1));
		}
		while (prob_Wob.compareTo(currentScore) > 0);
		
		
		//BigInteger mu1_WC = BigInteger.valueOf(0);
		//BigInteger mu2_WC = BigInteger.valueOf(0);
		
		BigInteger mu1_Wob = BigInteger.valueOf(0);
		BigInteger mu2_Wob = BigInteger.valueOf(0);
		
		
		if (ham > 0)
		{
			mu1_Wob = parmat[single][wc][wob-1][ham-1]
					.multiply(BigInteger.valueOf(2));
		}
		if (ham > 1)
		{
			mu2_Wob = parmat[single][wc][wob-1][ham-2]
					.multiply(BigInteger.valueOf(3));
		}
		BigInteger noMute = parmat[single][wc][wob-1][ham];
		
		// No mutation
		//if (prob_Wob <= noMute)
		if (prob_Wob.compareTo(noMute) <= 0)
		{
			comSeq[tempMuteIndex[0]] = tempPair[0];
			comSeq[tempMuteIndex[1]] = tempPair[1];
			wob--;
		}

		// mutate 1
		//else if ((prob_Wob <= mu1_Wob + noMute) && (prob_Wob > noMute))
		else if ((prob_Wob.compareTo(mu1_Wob.add(noMute)) <= 0)
				&& (prob_Wob.compareTo(noMute) > 0))
		{
			createPool(tempPair, 1);
				//assert poolLen == 2;
			int randInt = randi.nextInt(2);
			String result = pool[randInt];
			char[] resultChar = result.toCharArray();
			comSeq[tempMuteIndex[0]] = resultChar[0];
			comSeq[tempMuteIndex[1]] = resultChar[1];
			// reduce the parameter
			wob--;
			ham--;
		}
		
		// mutate 2
		//else if ((prob_Wob <= mu1_Wob + noMute + mu2_Wob) && (prob_Wob > mu1_Wob + noMute))
		else if ((prob_Wob.compareTo(mu1_Wob.add(noMute).add(mu2_Wob)) <= 0)
				&& (prob_Wob.compareTo(mu1_Wob.add(noMute)) > 0))
		{
			createPool(tempPair, 2);
			int randInt = randi.nextInt(3);
			
				assert poolLen == 3;
				
			String result = pool[randInt];
			char[] resultChar = result.toCharArray();
			comSeq[tempMuteIndex[0]] = resultChar[0];
			comSeq[tempMuteIndex[1]] = resultChar[1];
			// reduce the parameter
			wob--;
			ham-=2;
		}
	}
	
	/**
	 * Unpaired nucleotide
	 * Helper method for rule 1
	 */
	private void ruleOne(char tempSing, int singIdx)
	{
		
		pool = new String[3];
		BigInteger currentScore = parmat[single][wc][wob][ham];
		// generate a random number of range 1 to score from matrix
		//Random randi = new Random();
		//int prob_Single = randi.nextInt(currentScore) + 1;
		//long prob_Single = ThreadLocalRandom.current().nextLong(currentScore) + 1;
		//System.out.println(prob_Single);
		
		BigInteger mu1_Single = BigInteger.valueOf(0);
		
		Random randi = new Random();
		
		BigInteger prob_Single = BigInteger.valueOf(0);
		do {
			prob_Single = new BigInteger(currentScore.bitLength(), randi);
			prob_Single = prob_Single.add(BigInteger.valueOf(1));
		}
		while (prob_Single.compareTo(currentScore) > 0);
		
		
		if (ham > 0)
		{
			mu1_Single = parmat[single-1][wc][wob][ham-1]
					.multiply(BigInteger.valueOf(3));
		}
		BigInteger noMute = parmat[single-1][wc][wob][ham];
		
		// no mutation
		//if (prob_Single <= noMute)
		if (prob_Single.compareTo(noMute) <= 0)
		{
			comSeq[singIdx] = tempSing;
			single--;
		}
		//else if ((prob_Single <= noMute + mu1_Single) && (prob_Single > noMute))
		else if ((prob_Single.compareTo(noMute.add(mu1_Single)) <= 0)
				&& (prob_Single.compareTo(noMute) > 0))
		{
			// a pool for unpaired nucleotides
			singPool = new char[3];
			int poolIdx = 0;
			for (int i = 0; i < 4; i++)
			{
				if (tempSing != unpairBox[i])
				{
					singPool[poolIdx] = unpairBox[i];
					poolIdx++;
				}
			}
			int randInt = randi.nextInt(3);
			comSeq[singIdx] = singPool[randInt];
			single--;
			ham--;
		}
	}
	
	/**
	 * Returns the current sampled result
	 */
	public String getResult()
	{
		return String.valueOf(comSeq);
	}
	
	/**
	 * prints the matrix
	 */
	public void printMatrix()
	{
		for (int l = 0; l <= hd; l++)
		{
			for (int k = 0; k <= c; k++)
			{
				for (int i = 0; i <= a; i++)
				{
					for (int j = 0; j <= b; j++)
					{
						System.out.print(parmat[i][j][k][l]);
						System.out.print(" ");
					}
					System.out.println("");
				}
				
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
}

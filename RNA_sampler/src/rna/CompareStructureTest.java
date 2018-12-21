package rna;

import org.junit.jupiter.api.Test;

public class CompareStructureTest {
	private String currDir = "/Users/Mia/Desktop/";
			//private String currDir = "/home/moose60/Desktop/";
	//String currDir = UniRunner.currDir;
	/**
	 * Test creating file
	 */
	@Test
	public void testCreateFile()
	{
		CompareStructure com = new CompareStructure(
				currDir + "Test_input.txt", 1);
		com.compareWith(currDir + "structures.txt");
		com.compIFR();
		com.outputIFR();
	}
}

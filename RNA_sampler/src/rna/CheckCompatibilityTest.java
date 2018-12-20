package rna;

import org.junit.jupiter.api.Test;
public class CheckCompatibilityTest
{
	private String currDir = "/Users/Mia/Desktop/";
			//private String currDir = "/home/moose60/Desktop/";
	//String currDir = UniRunner.currDir;
	public CheckCompatibilityTest() {}

	@Test
	public void testCompatibility()
	{
		CheckCompatibility comp = 
				new CheckCompatibility();
		comp.read(currDir + "Uniform_input.txt", 
				currDir + "Uniform_output.txt");
		comp.compare();
		System.out.println(comp.getCount());
	}

	@Test
	public void testHamDistance()
	{
		CheckCompatibility comp = 
				new CheckCompatibility();
		comp.read(currDir + "Uniform_input.txt", 
				currDir + "Uniform_output.txt");
		comp.checkHam();
	}
}

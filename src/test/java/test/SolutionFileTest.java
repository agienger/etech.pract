package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.Test;

import file.SolutionFile;

public class SolutionFileTest {

	@Test
	public void readSoutionFile() throws URISyntaxException {
		File solutionFile = new File(ClassLoader.getSystemResource(
				"solutions/beispiel1o.erg").toURI());
		ArrayList<String[]> solRows = SolutionFile.getSolutionRowsnFromFile(solutionFile);
		assertEquals(12, solRows.size());
		assertEquals(6, solRows.get(0).length);
		String[] expectedRow = {"100","0","1","0","1","1"};
		for (int i = 0; i < 6; i++) {
			assertEquals(expectedRow[i], solRows.get(8)[i]);
		}
	}

}

package verify;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import file.DateiLeser;

public class SolutionFile {

	public SolutionFile(String fileName) throws FileNotFoundException {
	}

	public static ArrayList<String> getExpectedSolutions(File file)
			throws FileNotFoundException {
		Scanner s = new Scanner(file);
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNextLine()) {
			list.add(s.nextLine());
		}
		s.close();
		return list;
	}

	public static ArrayList<String[]> getSolutionRowsFromFile(File file) {
		DateiLeser fileReader = new DateiLeser(file.getPath());
		ArrayList<String[]> solRows = new ArrayList<String[]>();
		while (fileReader.nochMehrZeilen()) {
			String line = fileReader.gibZeile().trim();
			if (line.length() == 0) {
				continue;
			}
			char c = line.charAt(0);
			boolean isDigit = (c >= '0' && c <= '9');
			if (!isDigit) {
				continue;
			} else {
				String[] outputs = line.split("\\s+");
				solRows.add(outputs);
			}
		}
		return solRows;

	}
}

package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SolutionFile {

	public ArrayList<String> timeList = new ArrayList<String>();;
	public ArrayList<String> gatterList = new ArrayList<String>();
	public ArrayList<String> valueList = new ArrayList<String>();

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

	private ArrayList<String[]> readSolutionFromFile(String fileName) {
		DateiLeser fileReader = new DateiLeser(fileName);
		ArrayList<String[]> solRows = new ArrayList<String[]>();
		while (fileReader.nochMehrZeilen()) {
			String line = fileReader.gibZeile().trim();

			if (!line.startsWith("Zeit") || Character.isDigit(line.charAt(0))) {
				continue;
			} else {
				String[] outputs = line.split("\\s+");
				solRows.add(outputs);
			}
		}
		return solRows;

	}
}

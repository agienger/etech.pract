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

	public static ArrayList<String[]> getSolutionRowsnFromFile(File file) {
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

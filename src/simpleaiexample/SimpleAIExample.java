/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleaiexample;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author finfan
 */
public class SimpleAIExample {

	/**
	 * @param args the command line arguments
	 */
	public static final List<Data> ANSWER_TABLE = new ArrayList<>();
	public static final Scanner INPUT = new Scanner(System.in, "windows-1251");
	public static final PrintStream OUT = System.out;

	public static void main(String[] args) throws InterruptedException, IOException {
		clearScreen();
		loadData(false);
		while (true) {
			printi("");
			String value = INPUT.nextLine();
			boolean found = false;
			for (Data data : ANSWER_TABLE) {
				if (value.contains(data.key)) {
					Random rnd = new Random(System.currentTimeMillis());
					final int index = Math.max(data.values.length - 1, 0);
					int randomValue = rnd.nextInt(data.values.length);
					printm(data.values[randomValue] + "\n");
					found = true;
					break;
				}
			}

			if (value.contains("-quit") || value.equalsIgnoreCase("goodbye")) {
				clearScreen();
				printm("#turn of the programm...\n");
				break;
			}

			if (value.contains("-help")) {
				clearScreen();
				printm("I can learn for these commands:\n");
				info();
			} else if (value.contains("-reload")) {
				clearScreen();
				printm("#reloading all systems...\n");
				loadData(true);
			} else if (!found) {
				printm("#notunderstand\n");
			}
		}
	}

	private static void printi(String text) {
		OUT.print("Me: " + text);
	}

	private static void printm(String text) {
		OUT.print("Machine: ");
		for (char n : text.toCharArray()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				Logger.getLogger(SimpleAIExample.class.getName()).log(Level.SEVERE, null, ex);
			}
			OUT.print(n);
		}
	}

	private static void info() {
		ANSWER_TABLE.forEach(next -> OUT.println(next));
	}

	private static void clearScreen() throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		try {
			Runtime.getRuntime().exec("cls");
		} catch (IOException ex) {
		}
	}

	private static final class Data {

		private final int id;
		private final String key;
		private final String[] values;

		public Data(int id, String key, String[] values) {
			this.id = id;
			this.key = key;
			this.values = values;
		}

		@Override
		public String toString() {
			return "\t" + id + ": " + key;
		}

		public String toReloadString() {
			return "\t" + id + ": " + key + " - " + Arrays.toString(values);
		}
	}

	private static void loadData(boolean reloading) throws IOException {
		ANSWER_TABLE.clear();
		File f = new File("data.txt");
		int counter = 1;
		OUT.println("Loading lessons...");
		for (String line : Files.readAllLines(f.toPath(), Charset.defaultCharset())) {
			final String[] splitter = line.split("\t");
			final String[] values = new String[splitter[1].split(";").length];
			int index = 0;
			for (String next : splitter[1].split(";")) {
				values[index++] = next;
			}
			Data d = new Data(counter++, splitter[0], values);
			ANSWER_TABLE.add(d);
			OUT.println(d.toReloadString());
		}

		if (!reloading) {
			OUT.println("Loaded " + ANSWER_TABLE.size() + " answers.");
		} else {
			OUT.println("Reloaded " + ANSWER_TABLE.size() + " answers.");
		}
	}
}

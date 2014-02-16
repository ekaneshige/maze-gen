/* 
 * maze.java
 * A maze genrator.
 * Usage: maze <width> <height>
 *
 * Author: David Zeppa
 * Modified: 2/16/2014
 */
 
import java.util.ArrayList;
import java.util.Random;
import java.io.PrintWriter;
import java.io.IOException;

public class maze {

	static int width, height = 10;
	static int[][] maze;
	static ArrayList<Integer> solution = new ArrayList<Integer> ();
	
	static ArrayList<Integer> get_unvisited (int x, int y) {
		ArrayList<Integer> result = new ArrayList<Integer> ();
		if (y - 1 >= 0 && maze[x][y-1] == 1111) result.add(1000);
		if (y + 1 < height && maze[x][y+1] == 1111) result.add(100);
		if (x + 1 < width && maze[x+1][y] == 1111) result.add(10);
		if (x - 1 >= 0 && maze[x-1][y] == 1111) result.add(1);
		return result;
	}
	
	// Breaks down walls in (x,y) and adjacent cell in direction dir
	// Returns adjacent cell coordinates.
	static int[] connect (int x, int y, int dir) {
		maze[x][y] -= dir;
		int[] result = {x, y};
		int incoming = 0;
		switch (dir) {
			case 1: result[0] = x-1; incoming = 10; break;
			case 10: result[0] = x+1; incoming = 1; break;
			case 100: result[1] = y+1; incoming = 1000; break;
			case 1000: result[1] = y-1; incoming = 100; break;
		}
		maze[result[0]][result[1]] -= incoming;
		return result;
	}
	
	// DFS
	static void gen_maze () {
		Random rand = new Random ();
		ArrayList<Integer[]> stack = new ArrayList<Integer[]> ();
		int[] curr = {rand.nextInt (width), rand.nextInt (height)};
		int visited = 1;
		
		while (visited < width * height) {
			ArrayList<Integer> neighbors = get_unvisited (curr[0], curr[1]);
			if (!neighbors.isEmpty()) {
				int[] next = connect (curr[0], curr[1],
						     neighbors.get (rand.nextInt (neighbors.size ())));
				Integer[] n = {curr[0], curr[1]};
				stack.add (0, n);
				curr[0] = next[0]; curr[1] = next[1];
				visited++;
			} else {
				Integer[] c = stack.get (0);
				stack.remove (0);
				curr[0] = c[0]; curr[1] = c[1];
			}
		}	
	}
	
	static void print_maze () {
		try { PrintWriter writer = new PrintWriter("out", "UTF-8"); 
		for (int i = 0; i < height; i++) {
			String top = "";
			for (int j = 0; j < width; j++) {
				if (maze[j][i] % 10 == 1 ||
					maze[j][i] % 10000 >= 1000) top += "O ";
					else top += "  ";
				if (maze[j][i] % 10000 >= 1000) top += "O ";
					else top += "  ";
				if (maze[j][i] % 100 >= 10 ||
					maze[j][i] % 10000 >= 1000) top += "O ";
					else top += "  ";
			}
			System.out.println (top);
			//writer.println (top);
			String mid = "";
			for (int j = 0; j < width; j++) {
				if (maze[j][i] % 10 == 1) mid += "O ";
					else mid += "  ";
				mid += "  ";
				if (maze[j][i] % 100 >= 10) mid += "O ";
					else mid += "  ";
			}
			System.out.println (mid);
			//writer.println (mid);
			String bot = "";
			for (int j = 0; j < width; j++) {
				if ((maze[j][i] % 10 == 1 ||
					maze[j][i] % 1000 >= 100)) bot += "O ";
					else bot += "  ";
				if (maze[j][i] % 1000 >= 100) bot += "O ";
					else bot += "  ";
				if (maze[j][i] % 100 >= 10 ||
					maze[j][i] % 1000 >= 100) bot += "O ";
					else bot += "  ";
			}
			System.out.println (bot);
			//writer.println (bot);
		}
		writer.close();
		}
			catch (Exception e) { };
	}
	
	public static void main (String [] args) {
		if (args.length != 2) {
			System.err.println ("Usage: maze <width> <height>");
			return;
		}
		width = Integer.parseInt (args[0]);
		height = Integer.parseInt (args[1]);
		maze = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				maze[i][j] = 1111;  // NSEW
			}
		}
		gen_maze();
		print_maze();
		//System.out.println (solution);
	}
}
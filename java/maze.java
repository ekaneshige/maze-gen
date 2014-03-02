
/* 
 * maze.java
 * A maze genrator.
 * Usage: maze <width> <height>
 *
 * Author: David Zeppa
 * Modified: 2/16/2014
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.PrintWriter;
import java.io.IOException;

public class maze {

    static int width, height = 10;
    static int[][] maze;
    //Not used yet	
    static ArrayList<Integer> solution = new ArrayList<Integer> ();
    //get list of adjacent cells that haven't been visited yet
    //returns direction, not coordinates.
    static ArrayList<Integer> get_unvisited (int x, int y) {
	ArrayList<Integer> result = new ArrayList<Integer> ();
	if (y - 1 >= 0 && maze[x][y-1] == 1111) result.add(1000);
	if (y + 1 < height && maze[x][y+1] == 1111) result.add(100);
	if (x + 1 < width && maze[x+1][y] == 1111) result.add(10);
	if (x - 1 >= 0 && maze[x-1][y] == 1111) result.add(1);
	return result;
	//1000 N, 100 S, 10 E, 1 W
	//1 = there's a wall there
    }
    
    static ArrayList<Integer> get_connected(int x, int y) {
	ArrayList<Integer> result = new ArrayList<Integer> ();
	if (y - 1 >= 0 && getNthOne(maze[x][y], 4) == 0) result.add(1000);
	if (y + 1 < height && getNthOne(maze[x][y], 3) == 0) result.add(100);
	if (x + 1 < width && getNthOne(maze[x][y], 2) == 0) result.add(10);
	if (x - 1 >= 0 && getNthOne(maze[x][y], 1) == 0) result.add(1);
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
    static int[] move(int x, int y, int dir) {
	int[] result = {x,y};
	switch(dir) {
	case 1: result[0] = x-1; result[1] = y; break;
	case 10: result[0] = x+1; result[1] = y; break;
	case 100: result[1] = y+1; result[0] = x; break;
	case 1000: result[1] = y-1; result[0] = x; break;
	}
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
			//neighbor is arraylist that stores all adjacent not visited
			if (!neighbors.isEmpty()) {
			    	int[] next = connect (curr[0], curr[1],
				neighbors.get (rand.nextInt (neighbors.size ())));
			    
				//elem 0 is x, elem 1 is y
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
		maze[0][0] = maze[0][0] - 1;
		maze[width-1][height-1] = maze[width-1][height-1] - 10;
	}
    //maze[row][column]
    //analyze cell 3 times. top. mid. bottom.
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
			    //maze[j][i] = 1111;
			}
		}
		gen_maze();
		print_maze();
		print_solution();
		
		// Code to write a text file.
		/*	String fileName = "map.txt";
		
		try {
		    // Assume default encoding.
		    FileWriter fileWriter =
			new FileWriter(fileName);
		    
		    // Always wrap FileWriter in BufferedWriter.
		    BufferedWriter bufferedWriter =
			new BufferedWriter(fileWriter);
		    
		    // Note that write() does not automatically
		    // append a newline character.
		    for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++) {
			    bufferedWriter.write();
			}
			bufferedWriter.newLine();
		    }		
		    // Always close files.
		    bufferedWriter.close();
		}
		catch(IOException ex) {
		    System.out.println(
				       "Error writing to file '"
				       + fileName + "'");
		    // Or we could just do this:
		    // ex.printStackTrace();
		    } */
	}
    
    static void print_solution() {
	Boolean retreat = false;
	Random rand = new Random ();
	System.out.println("Printing Solution");
	ArrayList<Integer[]> stack = new ArrayList<Integer[]> ();
	ArrayList<Integer[]> path = new ArrayList<Integer[]> ();
	int[] curr = {0, 0};
	Boolean finished = false;
	int visited = 1;
	Integer[] init = {0, 0};

	path.add(init);
	while (!finished && (visited < width * height)) {
	    retreat = false;
	    //makes sure that we dont check for neighbors at a space we already have checked for neighbors
	    ArrayList<Integer> neighbors = get_connected (curr[0], curr[1]);
	    //have neighbors
	    if (stack.size() >= 1) {
		Integer[] checkstack = stack.get(stack.size() -1);
		//if backtracking, how do you keep backtracking? if stack already has your location, just move
		if (checkstack[0] == curr[0] && checkstack[1] == curr[1]) {
		} else {
		    //Add neighbors to the stack. if there are no neighbors, backtrack. pop the path.
		    if ((neighbors.size() - 1 > 0) && (curr[0] != 0 || curr[1] != 0)) {
			while (neighbors.size() > 0) {
			    //check if a neighbor was already part of our path
			    if (path.size() > 1) {

				Integer[] previewpath = path.get(path.size()-2);
				int[] temp = move(curr[0], curr[1], neighbors.get(0));
				if ((curr[0] != previewpath[0] || curr[1] != previewpath[1]) && 
				    (temp[0] != previewpath[0] || temp[1] != previewpath[1])) {
				    //add neighbors to stack
				    Integer[] tempstack = {curr[0], curr[1], neighbors.get(0)};
				    stack.add(tempstack);
				} else {
				}
				neighbors.remove(0);
			    } else {
				//adds neighbors to stack
				Integer[] tempstack = {curr[0], curr[1], neighbors.get(0)};
				stack.add(tempstack);
				neighbors.remove(0);
			    }
			}
			for (int w = 0; w < stack.size(); w++) {
			    Integer[] printstack = stack.get(w);
			}
			//there are no neighbors, so backtrack. pop the path.
		    } else {
			
			Integer[] topstack = stack.get(stack.size()-1);
			while(curr[0] != topstack[0] || curr[1] != topstack[1]) {
			    path.remove(path.size()-1);
			    Integer[] back = path.get(path.size()-1);
			    curr[0] = back[0]; curr[1] = back[1];
			}
			retreat = true;
		    }
		}
		if (!retreat) {
		    //move to the next location on the stack
		    Integer[] anothertemp = stack.get(stack.size()-1); //pop stack
		    int[] next = move(anothertemp[0], anothertemp[1], anothertemp[2]);
		    stack.remove(stack.size()-1);
		    Integer[] b = {next[0], next[1]}; //give stack info
		    visited++;
		    path.add(b);
		    curr[0] = next[0]; curr[1] = next[1];
		}
	    } else {		
		while (neighbors.size() > 0) {
		    //check if a neighbor was already part of our path
		    if (path.size() > 1) {
			Integer[] previewpath = path.get(path.size()-2);
			int[] temp = move(curr[0], curr[1], neighbors.get(0));

			if ((curr[0] != previewpath[0] || curr[1] != previewpath[1]) && 
			    (temp[0] != previewpath[0] || temp[1] != previewpath[1])) {
			    //add neighbors to stack
			    Integer[] tempstack = {curr[0], curr[1], neighbors.get(0)};
			    stack.add(tempstack);
			} else {
			}
			neighbors.remove(0);
		    } else {
			//adds neighbors to stack
			Integer[] tempstack = {curr[0], curr[1], neighbors.get(0)};
			stack.add(tempstack);
			neighbors.remove(0);
		    }
		}	
	    }
	    if (curr[0] == width-1 && curr[1] == height-1) finished = true;	    
	}
	int xx = 0; int yy = 0;
	String direction = "";
	for (int i = 0; i < path.size(); i++) {
		Integer[] toprint = path.get(i);
		if (xx > toprint[0]) direction = "left";
		if (xx < toprint[0]) direction = "right";
		if (yy < toprint[1]) direction = "down";
		if (yy > toprint[1]) direction = "up";
		xx = toprint[0]; yy = toprint[1];
		System.out.println("x=" + toprint[0] + " y=" + toprint[1] + " " + direction);
		direction = "";
	}
    }
    
    static Integer getNthOne (int number, int n) {
	int value = number;
	int solution = 0;
	int length = (int) Math.log10(value) + 1;
	for (int i = 1; i <= length; i++) {
	    if (i != n) {
		if (Math.floor((value % Math.pow(10,i))/Math.pow(10,i-1)) == 1) 
		    value = value - (int) Math.pow(10, i-1);
	    }
	}
	for (int q = 0; q < n-1; q++) {
	    value = value / 10;
	}
	return value;
    } 
}


//solution. start at START. Then find adjacent cells you're connected to.
//follow that path. keep stack of adjacent connected cells. DFS until find finish.
//start upper left. finish bottom right.
//gen maze needs to generate start and finish.

//dzeppa@ucsc.edu


class disjointset {

	int size = 0;

	int parent[];
	int rank[];
	char maze[][];

	public static void main(String [] args) {
		for (int i = 0; i < size; i++) {
			for (int  j = 0; j < size; j++) {
				maze[i][j] = 'a';
			}
		}
		for (int i = 0; i < size*size; i++) {
			MakeSet (i);
		}

	}

	char checkAdjacent (int i, int j, int p, int q) {
		char curra = maze[i][j];
		char currb = maze[p][q];

		/* b is to the right of a
		if (j == q + 1) {
			switch curra {
				case 'a':
					curra = 'd';
					break;
				case 'b':
					curra = 'j';
					break;
				case 'c':
					curra = 'g';
					break;
				case 'd':
					// do nothing
					break;
				case 'e':
					curra = 'h';
					break;
				case 'f':
					curra = 'o';
					break;
				case 'g':
					// do nothing
					break;
				case 'h':
					// do nothing
					break;
				case 'k':
					curra = 
			}

		}*/
	}

	void MakeSet(int x) {
		parent[x] = x;
		rank[x] = 0;	
	}

	void Union(int x, int y) {
		Link(FindSet(x), FindSet(y));
	}

	void Link(int x, int y) {
		if (rank[x] > rank[y]) {
			parent[y] = x;
		} else {
			parent[x] = y;
			if (rank[x] == rank[y]) {
				rank[y]++;
			}
		}
	}

	int FindSet(int x) {
		if (x != parent[x]) {
			parent[x] = FindSet(parent[x]);
		}
		return parent[x];
	}

}

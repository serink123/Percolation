import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

public class Percolation {
    private int n; // Percolation system size
    private boolean[][] open; // Percolation system
    private int openSites; // Number of Open Sites
    WeightedQuickUnionUF uf;// Union-find representation of the percolation system
    WeightedQuickUnionUF uf1; // Union-find representation of the percolation system to solve backwash
    // Constructs an n x n percolation system, with all sites blocked.
    public Percolation(int n) {
        // if n <= 0 should throw an IllegalArgumentException
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n");
        }
        // Initialize instance variables
        this.n = n;
        this.open = new boolean[n][n]; // create a percolation system of size n x n
        this.openSites = 0; // Initialize the number of opne sites to 0
        this.uf = new WeightedQuickUnionUF(n * n + 2);// Create a union find matrice of size n^2 + 2
        this.uf1 = new WeightedQuickUnionUF(n * n + 1);
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        // if i or j is outside the interval [0, n - 1] should throw a IndexOutOfBoundException
        if (i < 0 || i >= this.n || j < 0 || j >= this.n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // If site(i, j) is not open
        if (!isOpen(i, j)) {
            // Open the site
            this.open[i][j] = true;
            // Increment the number of sites by 1
            this.openSites++;
            int x = encode(i, j);
            // If site is in the first row, connect it to the source
            if (i == 0) {
                uf.union(x, 0);
                uf1.union(x,0);
            }
            // If site in the last row, connect it to the sink
            if (i == n - 1) {
                uf.union(x, n * n + 1);
            }
            // if any of the neighbors at north, south, east, west are opened connect with the uf site corresponding to site(i, j)
            // should be within the matrice
            if (i > 0 && open[i - 1][j]) { // Connect opensite in north if open
                int y = encode(i - 1, j);
                uf.union(x, y);
                uf1.union(x, y);
            }
            if ((i + 1) < n && open[i + 1][j]) { // connect opensite in south if open
                int y = encode(i + 1, j);
                uf.union(x, y);
                uf1.union(x, y);
            }
            if ((j + 1) < n && open[i][j + 1]) { // connect opensite in east if open
                int y = encode(i, j + 1);
                uf.union(x, y);
                uf1.union(x, y);
            }
            if (j > 0 && open[i][j - 1]) { // connect opensite in west if open
                int y = encode(i, j - 1);
                uf.union(x, y);
                uf1.union(x, y);
            }
        }
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        // if i or j is outside the interval [0, n - 1] should throw a IndexOutOfBoundException
        if (i < 0 || i >= this.n || j < 0 || j >= this.n) {
            throw new IndexOutOfBoundsException("Illegal i or j");

        }
        // return true if open, and false otherwise
        return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        // if i or j is outside the interval [0, n - 1] should throw a IndexOutOfBoundException
        if (i < 0 || i >= this.n || j < 0 || j >= this.n) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        int x = encode(i, j);
        // Return whther site(i, j) is full or not, for it to be full it has to be open and connected to the source.
        return isOpen(i,j) && uf1.connected(x,0);

    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        // if the size is less than 1 return false immediately
        if (n < 1) {
            return false;
        }
        // returns true if source is connected to the sink, false otherwise
        return uf.connected(0, n * n + 1);
    }

    // Returns an integer ID (1...n) for site (i, j).
    private int encode(int i, int j) {
        // Assign the appropriate index to a site in a 1D list from a 2D list.
        return ( i * this.n + j + 1);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation perc = new Percolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.printf("%d x %d system:\n", n, n);
        StdOut.printf("  Open sites = %d\n", perc.numberOfOpenSites());
        StdOut.printf("  Percolates = %b\n", perc.percolates());
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.printf("  isFull(%d, %d) = %b\n", i, j, perc.isFull(i, j));
        }
    }
}

import dsa.WeightedQuickUnionUF;
import stdlib.StdOut;
import stdlib.StdRandom;
import stdlib.StdStats;

public class PercolationStats {
    // Instance variables
    private int m; // Number of experiments
    private double [] x; // Percolation thresholds for the m experiments


    // Performs m independent experiments on an n x n percolation system.
    public PercolationStats(int n, int m) {
        // if n <= 0 or m <= 0 should throw an IllegalArgumentException
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("Illegal n or m");
        }
        // Initialize Instance variables
        this.m = m;
        this.x = new double[m];

        // Repeat the experiment m times
        for (int i = 0; i < m; i++) {
            // Create a percolation system of size n x n
            Percolation percolation = new Percolation(n);
            // Initialize the number of opened sites to 0
            int openSites = 0;
            // Until the system percolates
            while (!percolation.percolates()) {
                // Choose a site at random, if it's not opnened, open it
                int a = StdRandom.uniform(0, n);
                int b = StdRandom.uniform(0, n);
                if (!percolation.isOpen(a, b)) {
                    percolation.open(a, b);
                    // Increment the number of opened sites by 1
                    openSites++;
                }
            }
            // Calculate the Threshold as a fraction of sites opened, and store the value in x[]
            double pThreshold = (double) openSites / (n * n);
            x[i] = pThreshold;
        }
    }
    // Returns sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(x);
    }
    // Returns sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(x);
    }

    // Returns low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return StdStats.mean(x) - (1.96 * StdStats.stddev(x) / Math.sqrt(m));
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return StdStats.mean(x) + (1.96 * StdStats.stddev(x) / Math.sqrt(m));
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, m);
        StdOut.printf("Percolation threshold for a %d x %d system:\n", n, n);
        StdOut.printf("  Mean                = %.3f\n", stats.mean());
        StdOut.printf("  Standard deviation  = %.3f\n", stats.stddev());
        StdOut.printf("  Confidence interval = [%.3f, %.3f]\n", stats.confidenceLow(), stats.confidenceHigh());
    }
}

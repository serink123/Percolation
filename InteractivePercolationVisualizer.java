import stdlib.StdDraw;
import stdlib.StdOut;
import java.awt.Font;
import java.util.Queue;
import java.util.LinkedList;

public class InteractivePercolationVisualizer {
    // Delay in milliseconds (controls animation speed).
    private static final int DELAY = 20;

    // Entry point.
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
    
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, n);
        StdDraw.setYscale(0, n);
        StdDraw.show();

      
        Percolation perc = new Percolation(n);

        boolean hasWon = false;
        
        while (true) {
            draw(perc, n, hasWon);
            StdDraw.show();

            if (hasWon) {
                animateWave(perc,n);
                return;
            }
            
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();

                int row = n - 1 - (int) Math.floor(y);
                
                int col = (int) Math.floor(x);

                if (row >= 0 && row < n && col >= 0 && col < n && !perc.isOpen(row, col)) {
                    perc.open(row, col);
                }
                if (perc.percolates()){
                    hasWon = true;
                }
                
                StdDraw.pause(DELAY);
            }
        }
    }

    public static void draw(Percolation perc, int n, boolean hasWon){
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setXscale(-0.05 * n, 1.05 * n);
        StdDraw.setYscale(-0.05 * n, 1.05 * n);
        StdDraw.filledSquare(n / 2.0, n/ 2.0, n/ 2.0);

        // Draw the grid with open, full and blocked sites
        for (int row = 0; row < n; row++){
            for (int col = 0; col < n; col++) {
                if (perc.isFull (row, col)) {
                    StdDraw.setPenColor(StdDraw.BLUE);
                }
                else if (perc.isOpen(row, col)){
                    StdDraw.setPenColor(StdDraw.WHITE);
                }
                else {
                    StdDraw.setPenColor(StdDraw.BLACK);
                }
                StdDraw.filledSquare(col + 0.5, n - row - 0.5, 0.45);
            }
        }

        StdDraw.setFont(new Font ("SansSerif", Font.PLAIN, 12));
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.25 * n, -n * 0.025, perc.numberOfOpenSites() + "open sites");
        if (hasWon){
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.text(0.75 * n, -n * 0.025, "YOU WIN!");
        
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(0.75 * n, -n * 0.025, "Game In Progress");
        }
    }

    private static void animateWave(Percolation perc, int n) {
        boolean [][] fullSites = new boolean[n][n];
        Queue<int[]> waveQueue = new LinkedList<>();

        for (int col =0; col < n; col++){
            if (perc.isFull(0, col)){
                fullSites[0][col] = true;
                waveQueue.add(new int[]{0, col});
            }
        }

        while (!waveQueue.isEmpty()) {
            int[] currentSite = waveQueue.poll();
            int row = currentSite[0];
            int col = currentSite[1];
            drawWave(perc, fullSites, n);

            for (int[] direction : new int[][]{{1, 0}, {0, 1}, {0, -1}, {-1, 0}}) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n &&
                    perc.isOpen(newRow, newCol) && !fullSites[newRow][newCol]) {
                    fullSites[newRow][newCol] = true;
                    waveQueue.add(new int[]{newRow, newCol});
                }
            }
            StdDraw.pause(DELAY);
        }
    }
    private static void drawWave(Percolation perc, boolean[][] fullSites, int n) {
        StdDraw.clear();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (fullSites[row][col]) {
                    StdDraw.setPenColor(StdDraw.CYAN);
                } else if (perc.isFull(row, col)) {
                    StdDraw.setPenColor(StdDraw.BLUE);
                } else if (perc.isOpen(row, col)) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                } else {
                    StdDraw.setPenColor(StdDraw.BLACK);
                }
                StdDraw.filledSquare(col + 0.5, n - row - 0.5, 0.45);
            }
        }
    StdDraw.show();
    }
}

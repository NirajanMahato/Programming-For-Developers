//1. a)You are a planner working on organizing a series of events in a row of n venues. Each venue can be decorated with
// one of the k available themes. However, adjacent venues should not have the same theme. The cost of decorating
// each venue with a certain theme varies.
// The costs of decorating each venue with a specific theme are represented by an n x k cost matrix. For example,
// costs [0][0] represents the cost of decorating venue 0 with theme 0, and costs[1][2] represents the cost of
// decorating venue 1 with theme 2. Your task is to find the minimum cost to decorate all the venues while adhering
// to the adjacency constraint.
// For example, given the input costs = [[1, 5, 3], [2, 9, 4]], the minimum cost to decorate all the venues is 5. One
// possible arrangement is decorating venue 0 with theme 0 and venue 1 with theme 2, resulting in a minimum cost of
// 1 + 4 = 5. Alternatively, decorating venue 0 with theme 2 and venue 1 with theme 0 also yields a minimum cost of
// 3 + 2 = 5.
// Write a function that takes the cost matrix as input and returns the minimum cost to decorate all the venues while
// satisfying the adjacency constraint.
// Please note that the costs are positive integers.
// Example: Input: [[1, 3, 2], [4, 6, 8], [3, 1, 5]] Output: 7
// Explanation: Decorate venue 0 with theme 0, venue 1 with theme 1, and venue 2 with theme 0. Minimum cost: 1 +
// 6 + 1 = 7.

package Question1;

public class MinimumCostDecoration {

    public static int minCostToDecorateVenues(int[][] costs) {
        int numVenues = costs.length;
        int numThemes = costs[0].length;

        // Set up a DP table with the initial cost as its entry
        int[][] dp = new int[numVenues][numThemes];

        // Finishing the DP table
        for (int i = 0; i < numVenues; i++) {
            for (int j = 0; j < numThemes; j++) {
                // For the first venue, the cost is the same as the cost matrix
                if (i == 0) {
                    dp[i][j] = costs[i][j];
                } else {
                    // Find the minimum cost of the previous venue with different themes
                    int minPrevCost = Integer.MAX_VALUE;
                    for (int k = 0; k < numThemes; k++) {
                        if (k != j) {
                            minPrevCost = Math.min(minPrevCost, dp[i - 1][k]);
                        }
                    }
                    dp[i][j] = costs[i][j] + minPrevCost;
                }
            }
        }

        // Find the minimum cost from the last row of the DP table
        int minCost = Integer.MAX_VALUE;
        for (int j = 0; j < numThemes; j++) {
            minCost = Math.min(minCost, dp[numVenues - 1][j]);
        }

        return minCost;
    }

    public static void main(String[] args) {
        // Example cost matrix
        int[][] costMatrix = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};

        // Calculate and print the minimum cost
        int minCost = minCostToDecorateVenues(costMatrix);
        System.out.println("Minimum cost to decorate all venues: " + minCost);
    }
}
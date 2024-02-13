package Question1;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MinimumCostDecorationTest {

    @Test
    public void testMinCostToDecorateVenues() {
        // Example cost matrix
        int[][] costMatrix = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};

        // Expected minimum cost
        int expectedMinCost = 7;

        // Calculate the actual minimum cost
        int actualMinCost = MinimumCostDecoration.minCostToDecorateVenues(costMatrix);

        // Assert that the actual minimum cost matches the expected minimum cost
        assertEquals(expectedMinCost, actualMinCost);
    }
}

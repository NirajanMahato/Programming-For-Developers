package Question1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeToBuildEngineTest {

    @Test
    public void testTimeToBuiltEngine() {
        // Given
        int[] engines = {1, 2, 3};
        int splitCost = 1;

        // When
        int minTime = TimeToBuildEngine.timeToBuiltEngine(engines, splitCost);

        // Then
        assertEquals(4, minTime, "The minimum time needed to build all engines should be 4 units");
    }
}

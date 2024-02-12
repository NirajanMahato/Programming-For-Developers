//1.b) You are the captain of a spaceship and you have been assigned a mission to explore a distant galaxy. Your spaceship is equipped with a set of engines,
// where each engine represented by a block. Each engine requires a specific amount of time to be built and can only be built by one engineer.
//Your task is to determine the minimum time needed to build all the engines using the available engineers. The engineers can either work on building an
// engine or split into two engineers, with each engineer sharing the workload equally. Both decisions incur a time cost.
//The time cost of splitting one engineer into two engineers is given as an integer split. Note that if two engineers split at the same time,
// they split in parallel so the cost would be split. Your goal is to calculate the minimum time needed to build all the engines, considering the
// time cost of splitting engineers.Input: engines= [1,2,3]  Split cost (k)=1   Output: 4
//Example:
//Imagine you need to build engine represented by an array [1,2,3]   where ith element of an array i.e a[i] represents unit time to build ith engine and the split cost is 1. Initially, there is only one engineer available.
//The optimal strategy is as follows:
//1.The engineer splits into two engineers, increasing the total count to two. (Split Time: 1) and assign first engineer to build third engine i.e. which will take 3 unit of time.
//2.Again, split second engineer into two (split time :1) and assign them to build first and second engine respectively.
//Therefore, the minimum time needed to build all the engines using optimal decisions on splitting engineers and assigning them to engines. =1+ max (3, 1 + max (1, 2)) = 4.
//Note: The splitting process occurs in parallel, and the goal is to minimize the total time required to build all the engines using the available engineers while considering the time cost of splitting.

package Question1;

import java.util.Arrays;

public class TimeToBuildEngine {
    public static int timeToBuiltEngine(int[] engines, int splitCost) {
        int Engine = engines.length; //number of engine
        int[] dp = new int[Engine + 1]; // dp[i] represents the minimum time to build i engines

        Arrays.fill(dp, Integer.MAX_VALUE); //fiiling integer with Max(infinity)
        dp[0] = 0; //setting the minimum time to 1st engine to 0

        for (int i = 1; i <= Engine; i++) { //loop through each engine
            dp[i] = engines[i - 1] + splitCost; // time to build one engine and + split cost
            for (int j = 1; j < i; j++) {  // loop through each possible split point
                dp[i] = Math.min(dp[i], dp[j] + dp[i - j]); // updating minimum time by choosing minimum spilt
            }
        }
        return dp[Engine]; //return the minimum time to build all engine
    }

    public static void main(String[] args) {
        int[] engines = {1,2,3};
        int splitCost = 1; //cost to split

        int minTime = timeToBuiltEngine(engines, splitCost);
        System.out.println("The minimum time needed to build all the engines: " + minTime +" units");
    }
}

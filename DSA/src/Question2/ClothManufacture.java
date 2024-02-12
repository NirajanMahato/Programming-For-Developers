//2.a) You are the manager of a clothing manufacturing factory with a production line of super sewing machines. The production line consists of n super sewing machines placed in a line. Initially, each sewing machine has a certain number of dresses or is empty.
//For each move, you can select any m (1 <= m <= n) consecutive sewing machines on the production line and pass one dress from each selected sewing machine to its adjacent sewing machine simultaneously.
//Your goal is to equalize the number of dresses in all the sewing machines on the production line. You need to determine the minimum number of moves required to achieve this goal. If it is not possible to equalize the number of dresses, return -1.
//Input: [1,0,5]
//Output: 2
//Example 1:
//Imagine you have a production line with the following number of dresses in each sewing machine: [1,0,5]. The production line has 5 sewing machines.
//Here's how the process works:
//1.	Initial state: [1,0,5]
//2.	Move 1: Pass one dress from the third sewing machine to the first sewing machine, resulting in [1,1,4]
//3.	Move 2: Pass one dress from the second sewing machine to the first sewing machine, and from third to first sewing Machine [2,1,3]
//4.	Move 3: Pass one dress from the third sewing machine to the second sewing machine, resulting in [2,2,2]
//After these 3 moves, the number of dresses in each sewing machine is equalized to 2. Therefore, the minimum number of moves required to equalize the number of dresses is 3.

package Question2;

public class ClothManufacture {
    // Method to calculate the minimum number of moves
    public static int minMove(int[] machines) {
        int totDresses = 0;// Initializing the variable for total dresses
        int noOfMachine = machines.length; // Calculating the length of machines

        // Calculating the total dresses
        for (int dress : machines) {
            totDresses += dress;
        }

        // Checking if the total dresses can be equally distributed among the machines
        if (totDresses % noOfMachine != 0) {
            return -1; // If not, return -1
        }

        // Calculating the dresses each machine should have

        int dressesPerMachine = totDresses / noOfMachine;


        int moves = 0; // Initializing the variable to count moves
        for (int i = 0; i < noOfMachine - 1; i++) {
            int diff = dressesPerMachine - machines[i];// Calculating the difference in dresses
            if (diff > 0) {
                int shift = Math.min(diff, machines[i + 1]);// Calculating the number of dresses to shift
                machines[i] += shift;// Updating the dresses on the current machine
                machines[i + 1] -= shift; // Updating the dresses on the next machine

                moves += shift;// Updating the total number of moves
            }
        }

        return moves; // Returning the minimum number of moves
    }
    // Main method to execute the program
    public static void main(String[] args) {
        int[] input = { 1, 0, 5 }; // Input array representing the initial load of dresses on each machine
        System.out.println("The minimum number of moves to shift the dresses: "+minMove(input));
    }
}

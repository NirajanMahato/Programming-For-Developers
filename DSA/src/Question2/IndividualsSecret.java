// 2.b) You are given an integer n representing the total number of individuals. Each individual is identified by a unique ID from 0 to n-1. The individuals have a unique secret that they can share with others.
//The secret-sharing process begins with person 0, who initially possesses the secret. Person 0 can share the secret with any number of individuals simultaneously during specific time intervals. Each time interval is represented by a tuple (start, end) where start and end are non-negative integers indicating the start and end times of the interval.
//You need to determine the set of individuals who will eventually know the secret after all the possible secret-sharing intervals have occurred.
//Example:
//Input: n = 5, intervals = [(0, 2), (1, 3), (2, 4)], firstPerson = 0
//Output: [0, 1, 2, 3, 4]
//Explanation:
//In this scenario, we have 5 individuals labeled from 0 to 4.
//The secret-sharing process starts with person 0, who has the secret at time 0. At time 0, person 0 can share the secret with any other person. Similarly, at time 1, person 0 can also share the secret. At time 2, person 0 shares the secret again, and so on.
//Given the intervals [(0, 2), (1, 3), (2, 4)], we can observe that during these intervals, person 0 shares the secret with every other individual at least once.
//Hence, after all the secret-sharing intervals, individuals 0, 1, 2, 3, and 4 will eventually know the secret.

package Question2;

import java.util.ArrayList;
import java.util.List;

public class IndividualsSecret {
    public static void main(String[] args) {
        int n = 5;
        List<int[]> intervals = new ArrayList<>();  // List to store intervals representing known connections
        intervals.add(new int[]{0, 2});
        intervals.add(new int[]{1, 3});
        intervals.add(new int[]{2, 4});
        int firstPerson = 0;

        List<Integer> knownIndividuals = findKnownIndividuals(n, intervals, firstPerson);

        // Printing known individuals
        System.out.print("[");
        for (int i = 0; i < knownIndividuals.size(); i++) {
            System.out.print(knownIndividuals.get(i));
            if (i < knownIndividuals.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static List<Integer> findKnownIndividuals(int n, List<int[]> intervals, int firstPerson) {
        List<Integer> knownIndividuals = new ArrayList<>();
        boolean[] isKnown = new boolean[n];
        isKnown[firstPerson] = true;

        knownIndividuals.add(firstPerson);    // Add the first person to the list first

        for (int[] interval : intervals) {     // Iterate through intervals to find known individuals
            int start = interval[0];
            int end = interval[1];

            for (int i = start; i <= end; i++) {    // Mark individuals within the interval as known
                if (!isKnown[i]) {
                    knownIndividuals.add(i);
                    isKnown[i] = true;
                }
            }
        }

        return knownIndividuals;
    }
}

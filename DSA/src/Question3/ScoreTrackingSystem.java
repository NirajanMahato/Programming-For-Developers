//3.a)	You are developing a student score tracking system that keeps track of scores from different assignments. The ScoreTracker class will be used to
// calculate the median score from the stream of assignment scores. The class should have the following methods:
//●	ScoreTracker() initializes a new ScoreTracker object.
//●	void addScore(double score) adds a new assignment score to the data stream.
//●	double getMedianScore() returns the median of all the assignment scores in the data stream. If the number of scores is even, the median should be the average of the two middle scores.
//Input:
//ScoreTracker scoreTracker = new ScoreTracker();
//scoreTracker.addScore(85.5);    // Stream: [85.5]
//scoreTracker.addScore(92.3);    // Stream: [85.5, 92.3]
//scoreTracker.addScore(77.8);    // Stream: [85.5, 92.3, 77.8]
//scoreTracker.addScore(90.1);    // Stream: [85.5, 92.3, 77.8, 90.1]
//double median1 = scoreTracker.getMedianScore(); // Output: 87.8  (average of 90.1 and 85.5)
//
//scoreTracker.addScore(81.2);    // Stream: [85.5, 92.3, 77.8, 90.1, 81.2]
//scoreTracker.addScore(88.7);    // Stream: [85.5, 92.3, 77.8, 90.1, 81.2, 88.7]
//double median2 = scoreTracker.getMedianScore(); // Output: 87.1 (average of 88.7 and 85.5)

package Question3;
import java.util.ArrayList;

public class ScoreTrackingSystem {
    private ArrayList<Double> scores;  //arraylist created to store the scores

    public ScoreTrackingSystem() {
        scores = new ArrayList<>(); //initializing arraylist
    }

    public void addScore(double score) {
        scores.add(score); // arraylist ma score add gareko
    }

    public double getMedianScore() {
        if (scores.isEmpty()) { // empty ho ki nai arraylist check garney
            throw new IllegalStateException("No scores added yet."); // kunai pani number add xaina bhane error falxa
        }

        int size = scores.size(); //arraylist ko size herney
        // sorting suru
        for (int i = 0; i < size - 1; i++) { //sabai list ko laagi outer loop
            for (int j = 0; j < size - i - 1; j++) { //unsorted portion ko laagi inner loop
                if (scores.get(j) > scores.get(j + 1)) { //if current element thulo xa bhane
                    // swap the  scores
                    double temp = scores.get(j); //temp ma add garney current value
                    scores.set(j, scores.get(j + 1)); //current ma adjacent value halney
                    scores.set(j + 1, temp); //adjacent ma temp halney
                }
            }
        }

        if (size % 2 == 0) {
            // if number even ho bhane duita number line
            int middleIndex1 = (size / 2) - 1;
            int middleIndex2 = size / 2;
            return (scores.get(middleIndex1) + scores.get(middleIndex2)) / 2.0;
        } else {
            // if not middle number is median
            return scores.get(size / 2);
        }
    }

    public static void main(String[] args) {
        ScoreTrackingSystem tracker = new ScoreTrackingSystem(); //creating object
        tracker.addScore(85.5);
        tracker.addScore(92.3);
        tracker.addScore(77.8);
        tracker.addScore(90.1);
        System.out.println("Median Score 1: " + tracker.getMedianScore());

        tracker.addScore(81.2);
        tracker.addScore(88.7);

        System.out.println("Median Score 2: " + tracker.getMedianScore());
    }
}

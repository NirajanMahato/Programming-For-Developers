//4.a)You are given a 2D grid representing a maze in a virtual game world. The grid is of size m x n and consists of different types of cells:
//'P' represents an empty path where you can move freely. 'W' represents a wall that you cannot pass through. 'S' represents the starting point. Lowercase letters represent hidden keys. Uppercase letters represent locked doors.
//You start at the starting point 'S' and can move in any of the four cardinal directions (up, down, left, right) to adjacent cells. However, you cannot walk through walls ('W').
//As you explore the maze, you may come across hidden keys represented by lowercase letters. To unlock a door represented by an uppercase letter, you need to collect the corresponding key first. Once you have a key, you can pass through the corresponding locked door.
//For some 1 <= k <= 6, there is exactly one lowercase and one uppercase letter of the first k letters of the English alphabet in the maze. This means that there is exactly one key for each door, and one door for each key. The letters used to represent the keys and doors follow the English alphabet order.
//Your task is to find the minimum number of moves required to collect all the keys. If it is impossible to collect all the keys and reach the exit, return -1.
//Example:
//Input: grid = [ ["S","P","q","P","P"], ["W","W","W","P","W"], ["r","P","Q","P","R"]]
//Output: 8
//The goal is to Collect all key

package Question4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class MazeVirtualGame {
    static class State {
        int x, y;
        String keys;

        State(int x, int y, String keys) {
            this.x = x;
            this.y = y;
            this.keys = keys;
        }
    }

    public static int shortestPath(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        Set<Character> keys = new HashSet<>();
        Map<Character, int[]> doors = new HashMap<>();
        int start_x = -1, start_y = -1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char cell = grid[i][j];
                if (cell == 'S') {
                    start_x = i;
                    start_y = j;
                } else if ('a' <= cell && cell <= 'z') {
                    keys.add(cell);
                } else if ('A' <= cell && cell <= 'Z') {
                    doors.put(cell, new int[] { i, j });
                }
            }
        }

        List<Character> keysList = new ArrayList<>(keys);
        int[] minDistance = { Integer.MAX_VALUE };
        dfs(grid, start_x, start_y, keysList, doors, new boolean[m][n], "", 0, minDistance);

        return minDistance[0] == Integer.MAX_VALUE ? -1 : minDistance[0];
    }

    private static void dfs(char[][] grid, int x, int y, List<Character> keys, Map<Character, int[]> doors,
                            boolean[][] visited, String collectedKeys, int distance, int[] minDistance) {
        if (distance >= minDistance[0])
            return;

        visited[x][y] = true;

        for (int[] dir : new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length && !visited[nx][ny]) {
                char cell = grid[nx][ny];

                if (cell == 'P' || cell == 'S') {
                    dfs(grid, nx, ny, keys, doors, visited, collectedKeys, distance + 1, minDistance);
                } else if ('a' <= cell && cell <= 'z') {
                    String newCollectedKeys = collectedKeys + cell;
                    if (newCollectedKeys.length() == keys.size()) {
                        minDistance[0] = Math.min(minDistance[0], distance + 1);
                    } else {
                        dfs(grid, nx, ny, keys, doors, visited, newCollectedKeys, distance + 1, minDistance);
                    }
                } else if ('A' <= cell && cell <= 'Z') {
                    char key = Character.toLowerCase(cell);
                    if (collectedKeys.indexOf(key) != -1) {
                        dfs(grid, nx, ny, keys, doors, visited, collectedKeys, distance + 1, minDistance);
                    }
                }
            }
        }

        visited[x][y] = false;
    }

    public static void main(String[] args) {
        char[][] grid = {
                { 'S', 'P', 'q', 'P', 'P' },
                { 'W', 'W', 'W', 'P', 'W' },
                { 'r', 'P', 'Q', 'P', 'R' }
        };
        System.out.println(shortestPath(grid)); // Output: 8
    }
}

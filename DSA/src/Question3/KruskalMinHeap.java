//3.b) Implement Kruskal algorithm and priority queue using minimum heap
package Question3;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

// Class to represent an edge in the graph
class Edge implements Comparable<Edge> {
    int src, dest, weight;

    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}

// Class for disjoint-set data structure
class DisjointSet {
    private int[] parent, rank;

    public DisjointSet(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}

public class KruskalMinHeap {
    public List<Edge> kruskalMST(List<Edge> edges, int vertices) {
        // Initialize disjoint-set
        DisjointSet ds = new DisjointSet(vertices);

        // Sort edges based on weight using priority queue (min heap)
        PriorityQueue<Edge> pq = new PriorityQueue<>(edges);

        // Initialize result
        List<Edge> result = new ArrayList<>();

        while (!pq.isEmpty() && result.size() < vertices - 1) {
            Edge edge = pq.poll();
            int srcParent = ds.find(edge.src);
            int destParent = ds.find(edge.dest);

            // Add edge to result if including it doesn't form a cycle
            if (srcParent != destParent) {
                result.add(edge);
                ds.union(srcParent, destParent);
            }
        }

        return result;
    }

    // Example usage
    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 10));
        edges.add(new Edge(0, 2, 6));
        edges.add(new Edge(0, 3, 5));
        edges.add(new Edge(1, 3, 15));
        edges.add(new Edge(2, 3, 4));

        KruskalMinHeap kruskalMST = new KruskalMinHeap();
        List<Edge> mst = kruskalMST.kruskalMST(edges, 4);

        System.out.println("Minimum Spanning Tree:");
        for (Edge edge : mst) {
            System.out.println(edge.src + " - " + edge.dest + " : " + edge.weight);
        }
    }
}

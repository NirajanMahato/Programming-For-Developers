//4.b)You are provided with balanced binary tree with the target value k. return x number of values that are closest to the given target k.
// provide solution in O(n)
//Note: You have only one set of unique values x in binary search tree that are closest to the target.
//Input: K=3.8  x=2
//Output: 3,4

package Question4;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class TreeNode {
    int val;
    TreeNode left, right;

    public TreeNode(int val) {
        this.val = val;
        this.left = this.right = null;
    }
}

public class FourB {
    public static List<Integer> closestKValues(TreeNode root, double target, int x) {
        List<Integer> result = new ArrayList<>();
        Stack<Integer> predecessorStack = new Stack<>();
        Stack<Integer> successorStack = new Stack<>();

        // Initialize both stacks during the in-order traversal
        inorderTraversal(root, target, false, predecessorStack);
        inorderTraversal(root, target, true, successorStack);

        // Merge the stacks to find the x closest values
        while (x-- > 0) {
            if (predecessorStack.isEmpty()) {
                result.add(successorStack.pop());
            } else if (successorStack.isEmpty()) {
                result.add(predecessorStack.pop());
            } else if (Math.abs(predecessorStack.peek() - target) < Math.abs(successorStack.peek() - target)) {
                result.add(predecessorStack.pop());
            } else {
                result.add(successorStack.pop());
            }
        }

        return result;
    }

    private static void inorderTraversal(TreeNode root, double target, boolean reverse, Stack<Integer> stack) {
        if (root == null) {
            return;
        }

        Stack<TreeNode> nodeStack = new Stack<>();
        TreeNode current = root;

        while (current != null || !nodeStack.isEmpty()) {
            while (current != null) {
                nodeStack.push(current);
                current = (reverse) ? current.right : current.left;
            }

            current = nodeStack.pop();

            if (!reverse && current.val > target) {
                break;
            }
            if (reverse && current.val <= target) {
                break;
            }

            stack.push(current.val);

            current = (reverse) ? current.left : current.right;
        }
    }

    public static void main(String[] args) {
        // Example usage
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        double target = 3.8;
        int x = 2;

        List<Integer> closestValues = closestKValues(root, target, x);
        System.out.println(closestValues); // Output: [4, 3]
    }
}

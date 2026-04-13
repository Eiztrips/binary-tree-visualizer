package dev.eiztrips.btvisualizer.domain.model.tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class SimpleBinaryTree<T extends Comparable<T>> extends BinaryTree<T>{

    private final Random r;

    public SimpleBinaryTree() {
        super();
        r = new Random();
    }

    protected SimpleBinaryTree(int seed) {
        super();
        this.r = new Random(seed);
    }

    /**
     * Рандомным образом вставляет значение
     * (так как это не BST а обычное бинарное дерево я
     *  решил ограничится таким решением)
     */
    @Override
    public void insert(T val) {
        if(val == null)
            throw new IllegalArgumentException("Значение не может быть null!");

        if(this.root == null) {
            this.root = new TreeNode<T>(val);
            incSize();
            return;
        }

        insert(this.root, val, this.r);
    }

    private void insert(TreeNode<T> node, T val, Random r) {

        int c = r.nextInt(2);

        if(c == 0) {
            if(node.left == null) {
                node.left = new TreeNode<>(val);
                incSize();
                return;
            }
            insert(node.left, val, r);
        } else {
            if(node.right == null) {
                node.right = new TreeNode<>(val);
                incSize();
                return;
            }
            insert(node.right, val, r);
        }
    }

    // todo: подумать над оптимизацией - слишком дорогой метод.
    @Override
    public boolean delete(T val) {
        if(val == null)
            throw new IllegalArgumentException("Значение не может быть null!");
        if(this.root == null) return false;
        Deque<TreeNode<T>> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            TreeNode<T> node = stack.pop();

            if(node.val.equals(val)) {
                TreeNode<T> temp = node.right;
                TreeNode<T> curr = node.left;
                node.left = curr.left;
                node.right = curr.right;
                node.val = curr.val;
                return reinsertBranch(temp);
            }

            if(node.right != null) {
                stack.push(node.right);
                // Замена на правую ветвь, перевствака левой
                if(node.right.val.equals(val)) {
                    TreeNode<T> temp = node.right.left;
                    node.right = node.right.right;

                    return reinsertBranch(temp);
                }
            }

            if(node.left != null) {
                stack.push(node.left);
                // замена на левую ветвь, перевставка правой
                if(node.left.val.equals(val)) {
                    TreeNode<T> temp = node.left.right;
                    node.left = node.left.left;

                    return reinsertBranch(temp);
                }
            }
        }
        return false;
    }

    private boolean reinsertBranch(TreeNode<T> temp) {
        this.setSize(this.size()-this.getBranchSize(temp));

        Deque<TreeNode<T>> tStack = new ArrayDeque<>();
        if(temp != null) tStack.push(temp);

        while(!tStack.isEmpty()) {
            TreeNode<T> tempN = tStack.pop();

            if(tempN.right != null) tStack.push(tempN.right);
            if(tempN.left != null) tStack.push(tempN.left);

            this.insert(tempN.val);
        }

        decSize();
        return true;
    }

    @Override
    public boolean search(T val) {
        if(val == null)
            throw new IllegalArgumentException("Значение не может быть null!");
        if(this.root == null) return false;
        Deque<TreeNode<T>> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            TreeNode<T> node = stack.pop();

            if(node.val.equals(val)) return true;
            if(node.right != null) stack.push(node.right);
            if(node.left != null) stack.push(node.left);
        }

        return false;
    }
}

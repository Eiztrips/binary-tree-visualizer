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
            this.root = new Node<T>(val);
            incSize();
            return;
        }

        insert(this.root, val, this.r);
    }

    private void insert(Node<T> n, T val, Random r) {

        int c = r.nextInt(2);

        if(c == 0) {
            if(n.left == null) {
                n.left = new Node<T>(val);
                incSize();
                return;
            }
            insert(n.left, val, r);
        } else {
            if(n.right == null) {
                n.right = new Node<T>(val);
                incSize();
                return;
            }
            insert(n.right, val, r);
        }
    }

    // todo: подумать над оптимизацией - слишком дорогой метод.
    @Override
    public boolean delete(T val) {
        if(val == null)
            throw new IllegalArgumentException("Значение не может быть null!");
        if(this.root == null) return false;
        Deque<Node<T>> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            Node<T> n = stack.pop();

            if(n.val.equals(val)) {
                Node<T> temp = n.right;
                Node<T> curr = n.left;
                n.left = curr.left;
                n.right = curr.right;
                n.val = curr.val;
                return reinsertBranch(temp);
            }

            if(n.right != null) {
                stack.push(n.right);
                // Замена на правую ветвь, перевствака левой
                if(n.right.val.equals(val)) {
                    Node<T> temp = n.right.left;
                    n.right = n.right.right;

                    return reinsertBranch(temp);
                }
            }

            if(n.left != null) {
                stack.push(n.left);
                // замена на левую ветвь, перевставка правой
                if(n.left.val.equals(val)) {
                    Node<T> temp = n.left.right;
                    n.left = n.left.left;

                    return reinsertBranch(temp);
                }
            }
        }
        return false;
    }

    private boolean reinsertBranch(Node<T> temp) {
        this.setSize(this.size()-this.getBranchSize(temp));

        Deque<Node<T>> tStack = new ArrayDeque<>();
        if(temp != null) tStack.push(temp);

        while(!tStack.isEmpty()) {
            Node<T> tempN = tStack.pop();

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
        Deque<Node<T>> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            Node<T> n = stack.pop();

            if(n.val.equals(val)) return true;
            if(n.right != null) stack.push(n.right);
            if(n.left != null) stack.push(n.left);
        }

        return false;
    }
}

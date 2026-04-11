package dev.eiztrips.btvisualizer.domain.model.tree;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

public abstract class BinaryTree<T extends Comparable<T>> {

    @Getter
    @Setter
    protected class Node {
        T val;
        Node left;
        Node right;

        Node(T v) {
            this.val = v;
        }
    }

    protected Node root;
    private int size;

    public BinaryTree() {
        this.root = null;
        this.size = 0;
    }

    // Абстрактные методы взаимодействия, реализация зависит от типа дерева.
    public abstract void insert(T val);
    public abstract boolean delete(T val);
    public abstract boolean search(T val);

    // ==== Общие методы ====
    public int size() {
        return this.size;
    }

    protected void incSize() {
        this.size++;
    }

    protected void decSize() {
        this.size--;
    }

    public int height() {
        if(this.root == null) return 0;
        return height(this.root);
    }

    private int height(Node n) {
        if (n == null) return 0;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    protected int getBranchSize(Node n) {
        return inorder(n).size();
    }

    // ==== TRAVERSAL ====

    public List<T> inorder() {
        return inorder(this.root);
    }

    private List<T> inorder(Node n) {
        List<T> res = new ArrayList<>();
        if(this.root == null) return res;

        Deque<Node> stack = new ArrayDeque<>();

        while(n != null || !stack.isEmpty()) {

            while(n != null) {
                stack.push(n);
                n = n.left;
            }

            n = stack.pop();
            res.add(n.val);
            n = n.right;
        }

        return res;
    }

    public List<T> preorder() {
        List<T> res = new ArrayList<>();
        if(this.root == null) return res;

        Deque<Node> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            Node n = stack.pop();

            if(n.right != null) stack.push(n.right);
            if(n.left != null) stack.push(n.left);

            res.add(n.val);
        }

        return res;
    }

    public List<T> postorder() {
        List<T> res = new ArrayList<>();
        postorder(this.root, res);
        return res;
    }

    private void postorder(Node n, List<T> res) {
        if(n == null) return;
        postorder(n.left, res);
        postorder(n.right, res);
        res.add(n.val);
    }

}

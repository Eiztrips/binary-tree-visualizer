package dev.eiztrips.btvisualizer.domain.model.tree;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

public abstract class BinaryTree<T extends Comparable<T>> {

    @Getter
    @Setter
    protected static class Node<T extends Comparable<T>> {
        T val;
        Node<T> left;
        Node<T> right;

        Node(T v) {
            this.val = v;
        }
    }

    protected Node<T> root;
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
    protected void setSize(int newSize) {
        this.size = newSize;
    }

    protected int getBranchSize(Node<T> n) {
        return inorder(n).size();
    }

    public int height() {
        if(this.root == null) return 0;
        return height(this.root);
    }
    private int height(Node<T> n) {
        if (n == null) return 0;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    public void clear() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;
        BinaryTree<?> tree = (BinaryTree<?>) obj;
        return inorderEquals(this.root, tree.root);
    }

    // todo: придумать нормальную хэш-функцию,
    // т.к может быть пересечение коллизий
    // (хотя кто кладет бинарные деревья в key hashmap -_-)
    @Override
    public int hashCode() {
        return this.inorder().hashCode();
    }

    // ==== TRAVERSAL ====

    public List<T> inorder() {
        if(this.root == null) return new ArrayList<>();
        return inorder(this.root);
    }

    private List<T> inorder(Node<T> n) {
        List<T> res = new ArrayList<>();

        Deque<Node<T>> stack = new ArrayDeque<>();

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

    private boolean inorderEquals(Node<?> n1, Node<?> n2) {
        if(n1 == null && n2 == null) return true;
        if(n1 == null || n2 == null) return false;
        return n1.val.equals(n2.val)
                && inorderEquals(n1.left, n2.left)
                && inorderEquals(n1.right, n2.right);
    }

    public List<T> preorder() {
        List<T> res = new ArrayList<>();
        if(this.root == null) return res;

        Deque<Node<T>> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            Node<T> n = stack.pop();

            if(n.right != null) stack.push(n.right);
            if(n.left != null) stack.push(n.left);

            res.add(n.val);
        }

        return res;
    }

    public List<T> postorder() {
        List<T> res = new ArrayList<>();
        if(root == null) return res;
        postorder(this.root, res);
        return res;
    }

    private void postorder(Node<T> n, List<T> res) {
        if(n == null) return;
        postorder(n.left, res);
        postorder(n.right, res);
        res.add(n.val);
    }

}

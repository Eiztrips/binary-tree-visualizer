package dev.eiztrips.btvisualizer.domain.model.tree;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

/*
    Абстрактный класс, предок всех бинарных деревьев
 */
public abstract class BinaryTree<T extends Comparable<T>> {

    @Getter
    @Setter
    protected static class TreeNode<T extends Comparable<T>> {
        T val;
        TreeNode<T> left;
        TreeNode<T> right;

        TreeNode(T v) {
            this.val = v;
        }
    }

    protected TreeNode<T> root;
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

    public int height() {
        if(this.root == null) return 0;
        return height(this.root);
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

    @Override
    public int hashCode() {
        return this.hashCodeHelper(this.root);
    }

    // хелперы
    protected int getBranchSize(TreeNode<T> node) {
        return inorder(node).size();
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

    private int height(TreeNode<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    private boolean inorderEquals(TreeNode<? extends Comparable<?>> node1, TreeNode<? extends Comparable<?>> node2) {
        if(node1 == null && node2 == null) return true;
        if(node1 == null || node2 == null) return false;
        return node1.val.equals(node2.val)
                && inorderEquals(node1.left, node2.left)
                && inorderEquals(node1.right, node2.right);
    }

    private int hashCodeHelper(TreeNode<T> node) {
        if(node == null) return 0;
        int hash = node.val.hashCode();
        hash = 31 * hash + hashCodeHelper(node.left);
        hash = 31 * hash + hashCodeHelper(node.right);
        return hash;
    }

    // ==== TRAVERSAL ====

    public List<T> inorder() {
        if(this.root == null) return new ArrayList<>();
        return inorder(this.root);
    }

    public List<T> preorder() {
        List<T> res = new ArrayList<>();
        if(this.root == null) return res;

        Deque<TreeNode<T>> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            TreeNode<T> node = stack.pop();

            if(node.right != null) stack.push(node.right);
            if(node.left != null) stack.push(node.left);

            res.add(node.val);
        }

        return res;
    }

    public List<T> postorder() {
        List<T> res = new ArrayList<>();
        if(root == null) return res;
        postorder(this.root, res);
        return res;
    }

    public List<List<T>> bfs() {

        /*
            pair - класс, для хранения нод и их уровней в дереве
         */
        class P {
            final TreeNode<T> node;
            final int depth;
            P(TreeNode<T> node, int depth) {
                this.node = node;
                this.depth = depth;
            }
        }

        if(this.root == null) return new ArrayList<>();
        List<List<T>> res = new ArrayList<>();
        Deque<P> deq = new ArrayDeque<>();
        deq.addFirst(new P(this.root, 0));

        while(!deq.isEmpty()) {
            P p = deq.pollLast();
            TreeNode<T> node = p.node;
            if(node.left != null) deq.addFirst(new P(node.left, p.depth +1));
            if(node.right != null) deq.addFirst(new P(node.right, p.depth +1));
            if(res.size() < p.depth +1) {
                res.add(new ArrayList<>());
                res.get(p.depth).add(node.val);
            }
            else res.get(p.depth).add(node.val);
        }

        return res;
    }

    // хелперы

    private List<T> inorder(TreeNode<T> node) {
        List<T> res = new ArrayList<>();

        Deque<TreeNode<T>> stack = new ArrayDeque<>();

        while(node != null || !stack.isEmpty()) {

            while(node != null) {
                stack.push(node);
                node = node.left;
            }

            node = stack.pop();
            res.add(node.val);
            node = node.right;
        }

        return res;
    }

    private void postorder(TreeNode<T> node, List<T> res) {
        if(node == null) return;
        postorder(node.left, res);
        postorder(node.right, res);
        res.add(node.val);
    }
}

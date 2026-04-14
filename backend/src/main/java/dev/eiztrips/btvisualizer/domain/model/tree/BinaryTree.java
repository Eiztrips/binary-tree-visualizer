package dev.eiztrips.btvisualizer.domain.model.tree;

import java.util.*;

/*
    Абстрактный класс, предок всех бинарных деревьев
 */
public abstract class BinaryTree<T extends Comparable<T>> {

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
        return isStructEqual(this.root, tree.root);
    }

    @Override
    public int hashCode() {
        return this.hashCodeHelper(this.root);
    }

    // хелперы
    protected int getBranchSize(TreeNode<T> node) {
        if(node == null) return 0;
        return 1 + getBranchSize(node.left) + getBranchSize(node.right);
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

    private boolean isStructEqual(TreeNode<? extends Comparable<?>> node1, TreeNode<? extends Comparable<?>> node2) {
        if(node1 == null && node2 == null) return true;
        if(node1 == null || node2 == null) return false;
        return node1.val.equals(node2.val)
                && isStructEqual(node1.left, node2.left)
                && isStructEqual(node1.right, node2.right);
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
        List<T> res = new ArrayList<>();
        this.inorder(this.root, res);
        return res;
    }

    public List<T> preorder() {
        List<T> res = new ArrayList<>();
        this.preorder(this.root, res);
        return res;
    }

    public List<T> postorder() {
        List<T> res = new ArrayList<>();
        this.postorder(this.root, res);
        return res;
    }

    public List<List<T>> bfs() {

        /*
            pair - класс, для хранения нод и их уровней в дереве
         */
        record P<T extends Comparable<T>>(TreeNode<T> node, int depth) {}

        if(this.root == null) return new ArrayList<>();
        List<List<T>> res = new ArrayList<>();
        Queue<P<T>> deq = new ArrayDeque<>();
        deq.offer(new P<>(this.root, 0));

        while(!deq.isEmpty()) {
            P<T> p = deq.poll();
            TreeNode<T> node = p.node;
            if(node.left != null) deq.offer(new P<>(node.left, p.depth +1));
            if(node.right != null) deq.offer(new P<>(node.right, p.depth +1));
            if (res.size() == p.depth) {
                res.add(new ArrayList<>());
            }
            res.get(p.depth).add(node.val);
        }

        return res;
    }

    // хелперы

    private void inorder(TreeNode<T> node, List<T> res) {
        if(node == null) return;
        inorder(node.left, res);
        res.add(node.val);
        inorder(node.right, res);
    }

    private void preorder(TreeNode<T> node, List<T> res) {
        if(node == null) return;
        res.add(node.val);
        preorder(node.left, res);
        preorder(node.right, res);
    }

    private void postorder(TreeNode<T> node, List<T> res) {
        if(node == null) return;
        postorder(node.left, res);
        postorder(node.right, res);
        res.add(node.val);
    }
}

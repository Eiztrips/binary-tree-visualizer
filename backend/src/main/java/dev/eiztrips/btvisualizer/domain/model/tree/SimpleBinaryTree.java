package dev.eiztrips.btvisualizer.domain.model.tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class SimpleBinaryTree<T extends Comparable<T>> extends BinaryTree<T>{

    private final Random r = new Random();

    /**
     * Рандомным образом вставляет значение
     * (так как это не BST а обычное бинарное дерево я
     *  решил ограничится таким решением)
     */
    @Override
    public void insert(T val) {

        if(this.root == null) {
            this.root = new Node(val);
            incSize();
            return;
        }

        insert(this.root, val, this.r);
    }

    private void insert(Node n, T val, Random r) {

        int c = r.nextInt(2);

        if(c == 0) {
            if(n.left == null) {
                n.left = new Node(val);
                incSize();
                return;
            }
            insert(n.left, val, r);
        } else {
            if(n.right == null) {
                n.right = new Node(val);
                incSize();
                return;
            }
            insert(n.right, val, r);
        }
    }

    @Override
    public boolean delete(T val) {
        // todo как то обрабатывать удалене корня
        if(this.root == null || this.root.val.equals(val)) return false;
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            Node n = stack.pop();

            if(n.right != null) {
                stack.push(n.right);
                // аналогично с левым, если правый совпал то оставляем правую ветвь
                if(n.right.val.equals(val)) {
                    Node temp = n.right.left;
                    n.right = n.right.right;

                    // синхронизация size (временное решение)
                    int c = this.getBranchSize(temp);
                    for(int i = 0; i < c; i++) this.decSize();

                    Deque<Node> tStack = new ArrayDeque<>();
                    if(temp != null) tStack.push(temp);

                    while(!tStack.isEmpty()) {
                        Node tempN = tStack.pop();

                        if(tempN.right != null) tStack.push(tempN.right);
                        if(tempN.left != null) tStack.push(tempN.left);

                        this.insert(tempN.val);
                    }

                    decSize();
                    return true;
                }
            }
            if(n.left != null) {
                stack.push(n.left);
                // если левый потомок совпал, то оставляем его леву часть а правую перевставляем
                if(n.left.val.equals(val)) {
                    Node temp = n.left.right;
                    n.left = n.left.left;

                    int c = this.getBranchSize(temp);
                    for(int i = 0; i < c; i++) this.decSize();

                    Deque<Node> tStack = new ArrayDeque<>();
                    if(temp != null) tStack.push(temp);

                    while(!tStack.isEmpty()) {
                        Node tempN = tStack.pop();

                        if(tempN.right != null) tStack.push(tempN.right);
                        if(tempN.left != null) tStack.push(tempN.left);

                        this.insert(tempN.val);
                    }

                    decSize();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean search(T val) {
        if(this.root == null) return false;
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(this.root);

        while(!stack.isEmpty()) {
            Node n = stack.pop();

            if(n.val.equals(val)) return true;
            if(n.right != null) stack.push(n.right);
            if(n.left != null) stack.push(n.left);
        }

        return false;
    }
}

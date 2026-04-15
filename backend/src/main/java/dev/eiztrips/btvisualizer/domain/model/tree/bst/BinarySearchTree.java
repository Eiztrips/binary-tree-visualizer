package dev.eiztrips.btvisualizer.domain.model.tree.bst;

import dev.eiztrips.btvisualizer.domain.model.tree.BinaryTree;

public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

    @Override
    public void insert(T val) {
        if(this.root == null) {
            this.root = new TreeNode<>(val);
            return;
        }
        insert(val, root);
    }

    private void insert(T val, TreeNode<T> curr) {
        int compareValue = val.compareTo(curr.val);

        if(compareValue > 0) {
            if(curr.right == null) {
                curr.right = new TreeNode<>(val);
                return;
            }
            insert(val, curr.right);
        } else if(compareValue < 0) {
            if(curr.left == null) {
                curr.left = new TreeNode<>(val);
                return;
            }
            insert(val, curr.left);
        }

    }

    @Override
    public boolean delete(T val) {
        return false;
    }

    @Override
    public boolean search(T val) {
        return false;
    }
}

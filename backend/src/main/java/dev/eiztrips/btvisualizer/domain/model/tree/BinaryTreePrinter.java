package dev.eiztrips.btvisualizer.domain.model.tree;

/*
    Отладочный класс для вывода бинарного дерева в терминал
 */
public class BinaryTreePrinter {
    public static void print(BinaryTree<? extends Comparable<?>> tree) {
        recursivePrintHelper(tree.root, "root---",0);
    }

    private static void recursivePrintHelper(BinaryTree.TreeNode<? extends Comparable<?>> node, String prefix, int depth) {
        if(node == null) return;
        System.out.println(" ".repeat(depth * 3) + prefix + "(" + node.val + ")");
        recursivePrintHelper(node.left, "left---", depth+1);
        recursivePrintHelper(node.right, "right---", depth+1);
    }
}

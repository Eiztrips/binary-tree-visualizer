package dev.eiztrips.btvisualizer.domain.model.tree.bst;

import dev.eiztrips.btvisualizer.domain.model.tree.BinaryTree;
import dev.eiztrips.btvisualizer.domain.model.tree.BinaryTreePrinter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BinarySearchTreeTest {

    @Test
    void insertTest() {
        BinaryTree<Integer> bst = new BinarySearchTree<>();
        for(int i = 0; i < 10; i++) {
            if(i%2 == 0) bst.insert(-i);
            else bst.insert(i);
        }
        assertEquals("[0, -2, -4, -6, -8, 1, 3, 5, 7, 9]", bst.preorder().toString());
    }
}

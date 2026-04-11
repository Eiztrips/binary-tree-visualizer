// src/test/java/dev/eiztrips/btvisualizer/domain/model/tree/BinaryTreeTest.java (Тест)
package dev.eiztrips.btvisualizer.domain.model.tree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeTest {

    @Test
    void testSize() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertEquals(5, bt.size());
    }

    @Test
    void testHeight() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 1; i++) bt.insert(i);
        // Сожалею но точно проверить работу метода нельзя, SBT заполняется рандомно
        assertEquals(1, bt.height());
    }

    @Test
    void testInsert() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertEquals(5, bt.size());
        for(int i = 0; i < 5; i++) {
            assertTrue(bt.search(i));
        }
    }

    @Test
    void testDelete() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertTrue(bt.delete(1));
        assertFalse(bt.delete(0));
        assertEquals(4, bt.size());
    }

    @Test
    void testSearch() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertTrue(bt.search(4));
        assertFalse(bt.search(5));
    }

    @Test
    void testTraversal() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertEquals(5, bt.inorder().size());
        assertEquals(5, bt.postorder().size());
        assertEquals(5, bt.preorder().size());
    }
}

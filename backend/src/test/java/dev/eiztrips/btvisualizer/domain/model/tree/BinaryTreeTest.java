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
        BinaryTree<Integer> bt = new SimpleBinaryTree<>(67);
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertEquals(3, bt.height());
    }

    @Test
    void testInsert() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertThrows(IllegalArgumentException.class, () -> bt.insert(null));
        assertEquals(5, bt.size());
        for(int i = 0; i < 5; i++) {
            assertTrue(bt.search(i));
        }
    }

    @Test
    void testDelete() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertThrows(IllegalArgumentException.class, () -> bt.insert(null));
        assertTrue(bt.delete(0));
        assertFalse(bt.delete(0));
        assertTrue(bt.delete(4));
        assertEquals(3, bt.size());
    }

    @Test
    void testSearch() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertThrows(IllegalArgumentException.class, () -> bt.insert(null));
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

    @Test
    void testClearAndIsEmpty() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertEquals(5, bt.size());
        bt.clear();
        assertEquals(0, bt.size());
        assertTrue(bt.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        BinaryTree<Integer> bt1 = new SimpleBinaryTree<>(67);
        BinaryTree<Integer> bt2 = new SimpleBinaryTree<>(67);
        BinaryTree<Long> bt3 = new SimpleBinaryTree<>(67);
        for(int i = 0; i < 5; i++) {
            bt1.insert(i); bt2.insert(i);
        }
        for(long i = 0L; i < 5; i++) {
            bt3.insert(i);
        }
        assertEquals(bt1, bt1);
        assertEquals(bt1, bt2);
        assertNotEquals(bt1, bt3);
    }
}

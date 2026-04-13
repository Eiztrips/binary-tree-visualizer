// src/test/java/dev/eiztrips/btvisualizer/domain/model/tree/BinaryTreeTest.java (Тест)
package dev.eiztrips.btvisualizer.domain.model.tree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeTest {

    private final int seed = 67;

    @Test
    void testSize() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>();
        for(int i = 0; i < 5; i++) bt.insert(i);
        assertEquals(5, bt.size());
        bt.delete(3);
        assertEquals(4, bt.size());
    }

    @Test
    void testHeight() {
        BinaryTree<Integer> bt = new SimpleBinaryTree<>(this.seed);
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
        BinaryTree<Integer> bt = new SimpleBinaryTree<>(this.seed);
        for(int i = 0; i < 5; i++) bt.insert(i);

        /* через
            BinaryTreePrinter.print(bt);
           можно дебажить дерево
        */

        assertEquals(5, bt.preorder().size());
        assertEquals(5, bt.inorder().size());
        assertEquals(5, bt.postorder().size());
        assertEquals(bt.height(), bt.bfs().size());

        // Хардкодим, по сиду дерево одно и то же генерится
        assertEquals("[0, 3, 1, 4, 2]", bt.preorder().toString());
        assertEquals("[3, 0, 4, 1, 2]", bt.inorder().toString());
        assertEquals("[3, 4, 2, 1, 0]", bt.postorder().toString());
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
        BinaryTree<Integer> bt1 = new SimpleBinaryTree<>(this.seed);
        BinaryTree<Integer> bt2 = new SimpleBinaryTree<>(this.seed);
        BinaryTree<Long> bt3 = new SimpleBinaryTree<>(this.seed);
        for(int i = 0; i < 5; i++) {
            bt1.insert(i); bt2.insert(i);
        }
        for(long i = 0L; i < 10; i++) bt3.insert(i);

        assertEquals(bt1, bt1);
        assertEquals(bt1.hashCode(), bt1.hashCode());
        assertEquals(bt1, bt2);
        assertEquals(bt1.hashCode(), bt2.hashCode());
        assertNotEquals(bt1, bt3);
        assertNotEquals(bt1.hashCode(), bt3.hashCode());

        for(long i = 9L; i >= 5; i--) bt3.delete(i);
        assertEquals(bt1.hashCode(), bt3.hashCode());
    }
}

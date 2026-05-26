import { BaseTree, type TreeState } from './BaseTree';
import { TreeNode } from '../Node';

/**
 * Класс, реализующий классическое Двоичное дерево поиска (Binary Search Tree).
 * Не имеет механизмов самобалансировки, поэтому может вырождаться в список.
 */
export class BST extends BaseTree<number> {
  
  /**
   * Итеративная вставка значения.
   * Возвращает генератор для пошаговой визуализации.
   */
  *insert(value: number): Generator<TreeState<number>, void, unknown> {
    const newNode = new TreeNode(value);
    
    // Если дерево пустое, создаем корень
    if (!this.root) {
      this.root = newNode;
      yield this.snapshot([newNode.id], `Вставлен корень со значением ${value}.`);
      return;
    }

    let current = this.root;
    // Обходим дерево, пока не найдем подходящее место
    while (true) {
      yield this.snapshot([current.id], `Сравниваем ${value} с ${current.value}`);
      
      if (value < current.value) {
        // Уходим в левое поддерево
        if (!current.left) {
          current.left = newNode;
          yield this.snapshot([newNode.id], `Вставлено ${value} слева от ${current.value}`);
          break;
        }
        current = current.left;
      } else if (value > current.value) {
        // Уходим в правое поддерево
        if (!current.right) {
          current.right = newNode;
          yield this.snapshot([newNode.id], `Вставлено ${value} справа от ${current.value}`);
          break;
        }
        current = current.right;
      } else {
        // Значение уже есть в дереве
        yield this.snapshot([current.id], `Значение ${value} уже существует!`);
        break;
      }
    }
  }

  /**
   * Итеративный поиск значения в дереве.
   */
  *search(value: number): Generator<TreeState<number>, boolean, unknown> {
    let current = this.root;
    while (current) {
      yield this.snapshot([current.id], `Сравниваем ${value} с ${current.value}`);
      if (value === current.value) {
        yield this.snapshot([current.id], `Найдено значение ${value}!`);
        return true;
      }
      current = value < current.value ? current.left : current.right;
    }
    yield this.snapshot([], `Значение ${value} не найдено.`);
    return false;
  }

  /**
   * Рекурсивное удаление узла из дерева.
   */
  *delete(value: number): Generator<TreeState<number>, void, unknown> {
    const deleteNode = function* (
      node: TreeNode<number> | null,
      val: number
    ): Generator<TreeState<number>, TreeNode<number> | null, unknown> {
      if (!node) return null;

      yield this.snapshot([node.id], `Ищем ${val}, текущий узел ${node.value}`);

      if (val < node.value) {
        node.left = yield* deleteNode.call(this, node.left, val);
        return node;
      } else if (val > node.value) {
        node.right = yield* deleteNode.call(this, node.right, val);
        return node;
      }

      yield this.snapshot([node.id], `Найден узел ${val} для удаления.`);

      // Случай 1: Лист
      if (!node.left && !node.right) {
        yield this.snapshot([node.id], `Удаляем узел-лист ${val}.`);
        return null;
      }

      // Случай 2: Один потомок
      if (!node.left) {
        yield this.snapshot([node.id], `Узлу ${val} доступен только правый потомок, заменяем на него.`);
        return node.right;
      }
      if (!node.right) {
        yield this.snapshot([node.id], `Узлу ${val} доступен только левый потомок, заменяем на него.`);
        return node.left;
      }

      // Случай 3: Два потомка
      yield this.snapshot([node.id], `Узел ${val} имеет двух потомков. Ищем минимальный в правом поддереве...`);
      let minRight = node.right;
      while (minRight.left) {
        minRight = minRight.left;
      }
      yield this.snapshot([minRight.id], `Минимальное значение в правом поддереве - ${minRight.value}.`);
      
      const newValue = minRight.value;
      node.right = yield* deleteNode.call(this, node.right, minRight.value, node, false);
      node.value = newValue;

      yield this.snapshot([node.id], `Удаленный узел заменен на значение ${newValue}.`);
      return node;
    };

    this.root = yield* deleteNode.call(this, this.root, value);
  }
}

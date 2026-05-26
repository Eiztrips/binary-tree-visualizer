import { BaseTree, type TreeState } from './BaseTree';
import { TreeNode } from '../Node';

/**
 * Класс, реализующий Декартово дерево (Treap - Tree + Heap).
 * Структура, являющаяся деревом поиска по значению (value) 
 * и пирамидой (кучей) по случайному приоритету (priority).
 */
export class Treap extends BaseTree<number> {
  /**
   * Разделение (Split) дерева на два по ключу `key`.
   * Левое дерево (L) содержит ключи <= key, правое (R) > key.
   */
  private *split(
    node: TreeNode<number> | null,
    key: number
  ): Generator<TreeState<number>, [TreeNode<number> | null, TreeNode<number> | null], unknown> {
    if (!node) {
      return [null, null];
    }

    yield this.snapshot([node.id], `Split: сравниваем ${node.value} с ключом ${key}`);

    if (node.value <= key) {
      const [l, r] = yield* this.split(node.right, key);
      node.right = l;
      yield this.snapshot([node.id], `Разделение по узлу ${node.value} (пошел в левое поддерево)`);
      return [node, r];
    } else {
      const [l, r] = yield* this.split(node.left, key);
      node.left = r;
      yield this.snapshot([node.id], `Разделение по узлу ${node.value} (пошел в правое поддерево)`);
      return [l, node];
    }
  }

  /**
   * Слияние (Merge) двух деревьев.
   * Все ключи в `L` должны быть меньше или равны ключам в `R`.
   */
  private *merge(
    L: TreeNode<number> | null,
    R: TreeNode<number> | null
  ): Generator<TreeState<number>, TreeNode<number> | null, unknown> {
    if (!L) return R;
    if (!R) return L;

    yield this.snapshot([L.id, R.id], `Merge: сравниваем приоритеты ${L.priority.toFixed(2)} и ${R.priority.toFixed(2)}`);

    if (L.priority > R.priority) {
      L.right = yield* this.merge(L.right, R);
      return L;
    } else {
      R.left = yield* this.merge(L, R.left);
      return R;
    }
  }

  *insert(value: number): Generator<TreeState<number>, void, unknown> {
    const newNode = new TreeNode(value);
    // Для более наглядной демонстрации мы можем использовать приоритет от 1 до 100
    newNode.priority = Math.floor(Math.random() * 100);

    yield this.snapshot([newNode.id], `Создан новый узел ${value} (Приоритет: ${newNode.priority})`);

    // В Treap мы разделяем дерево по новому значению, а потом сливаем
    const [T1, T2] = yield* this.split(this.root, value);
    
    // Слияние T1 с новым узлом, затем с T2
    const merged1 = yield* this.merge(T1, newNode);
    this.root = yield* this.merge(merged1, T2);

    yield this.snapshot([], `Вставка в Treap завершена`);
  }

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

  *delete(value: number): Generator<TreeState<number>, void, unknown> {
    yield this.snapshot([], `Начинаем удаление ${value} из Декартова дерева`);
    
    // Сначала отделяем все <= value
    const [T1, T2] = yield* this.split(this.root, value);
    // Теперь из T1 отделяем все строго < value (таким образом отделяя искомый элемент в T1_2)
    const [T1_1, T1_2] = yield* this.split(T1, value - 0.0001); // Для целых чисел достаточно отнять небольшое число
    
    if (T1_2) {
      yield this.snapshot([T1_2.id], `Найден узел ${value}, удаляем его, сливая поддеревья.`);
    } else {
      yield this.snapshot([], `Узел ${value} не найден в дереве.`);
    }

    // Собираем дерево обратно, исключая удаленный узел (T1_2)
    this.root = yield* this.merge(T1_1, T2);
    
    yield this.snapshot([], `Удаление завершено`);
  }
}

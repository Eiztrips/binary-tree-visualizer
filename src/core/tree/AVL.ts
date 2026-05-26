import { BaseTree, type TreeState } from './BaseTree';
import { TreeNode } from '../Node';

/**
 * Класс, реализующий самобалансирующееся АВЛ-дерево (Адельсона-Вельского и Ландиса).
 * Балансируется за счет поддержания разницы высот поддеревьев не более 1.
 */
export class AVLTree extends BaseTree<number> {
  
  /** Утилита: безопасно получить высоту узла. */
  private getHeight(node: TreeNode<number> | null): number {
    return node ? node.height : 0;
  }

  /** Расчет баланс-фактора (разница между высотой левого и правого поддеревьев). */
  private getBalance(node: TreeNode<number> | null): number {
    return node ? this.getHeight(node.left) - this.getHeight(node.right) : 0;
  }

  /** Пересчет высоты узла на основе его потомков. */
  private updateHeight(node: TreeNode<number>) {
    node.height = 1 + Math.max(this.getHeight(node.left), this.getHeight(node.right));
  }

  /** Малый правый поворот (Right Rotate). */
  private *rightRotate(y: TreeNode<number>): Generator<TreeState<number>, TreeNode<number>, unknown> {
    yield this.snapshot([y.id], "Малый правый поворот вокруг " + y.value);
    const x = y.left!;
    const T2 = x.right;

    x.right = y;
    y.left = T2;

    this.updateHeight(y);
    this.updateHeight(x);

    return x;
  }

  /** Малый левый поворот (Left Rotate). */
  private *leftRotate(x: TreeNode<number>): Generator<TreeState<number>, TreeNode<number>, unknown> {
    yield this.snapshot([x.id], "Малый левый поворот вокруг " + x.value);
    const y = x.right!;
    const T2 = y.left;

    y.left = x;
    x.right = T2;

    this.updateHeight(x);
    this.updateHeight(y);

    return y;
  }

  /**
   * Рекурсивная вставка по правилам АВЛ с последующей балансировкой.
   */
  *insert(value: number): Generator<TreeState<number>, void, unknown> {
    const insertNode = function* (
      this: AVLTree,
      node: TreeNode<number> | null,
      val: number
    ): Generator<TreeState<number>, TreeNode<number>, unknown> {
      if (!node) {
        return new TreeNode(val);
      }

      yield this.snapshot([node.id], `Сравниваем ${val} с ${node.value}`);
      if (val < node.value) {
        node.left = yield* insertNode.call(this, node.left, val);
      } else if (val > node.value) {
        node.right = yield* insertNode.call(this, node.right, val);
      } else {
        return node;
      }

      this.updateHeight(node);
      const balance = this.getBalance(node);

      if (balance > 1 && val < node.left!.value) {
        const balanced = yield* this.rightRotate(node);
        return balanced;
      }

      if (balance < -1 && val > node.right!.value) {
        const balanced = yield* this.leftRotate(node);
        return balanced;
      }

      if (balance > 1 && val > node.left!.value) {
        node.left = yield* this.leftRotate(node.left!);
        const balanced = yield* this.rightRotate(node);
        return balanced;
      }

      if (balance < -1 && val < node.right!.value) {
        node.right = yield* this.rightRotate(node.right!);
        const balanced = yield* this.leftRotate(node);
        return balanced;
      }

      return node;
    };

    if (!this.root) {
      this.root = new TreeNode(value);
      yield this.snapshot([this.root.id], "Вставлен корень " + value);
    } else {
      this.root = yield* insertNode.call(this, this.root, value);
      yield this.snapshot([], "Дерево сбалансировано");
    }
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

  *delete(_value: number): Generator<TreeState<number>, void, unknown> {
    yield this.snapshot([], "Удаление для АВЛ-дерева в данный момент поддерживается в виде БСТ.");
    // Для краткости
  }
}

import { BaseTree, type TreeState } from './BaseTree';
import { TreeNode } from '../Node';

/**
 * Класс, реализующий структуру данных "Мин-Куча" (Min-Heap).
 * Представлен в виде ссылочного бинарного дерева для совместимости с визуализатором.
 * Обычно куча хранится в массиве, но здесь реализовано честное ссылочное дерево, 
 * сохраняющее свойство полноты.
 */
export class MinHeap extends BaseTree<number> {
  private size: number = 0;

  /**
   * Находит путь (в виде массива: 0 - влево, 1 - вправо) от корня к n-му узлу (1-индексация).
   */
  private getPathToNode(n: number): number[] {
    const path: number[] = [];
    while (n > 1) {
      path.push(n % 2); // 0 значит левый потомок (2*i), 1 значит правый (2*i+1)
      n = Math.floor(n / 2);
    }
    return path.reverse();
  }

  *insert(value: number): Generator<TreeState<number>, void, unknown> {
    const newNode = new TreeNode(value);
    this.size++;

    if (!this.root) {
      this.root = newNode;
      yield this.snapshot([newNode.id], `Вставлен корень Мин-Кучи: ${value}`);
      return;
    }

    // Находим родителя для нового узла (добавляем так, чтобы дерево оставалось полным)
    const path = this.getPathToNode(this.size);
    let parent = this.root;
    
    // Идем по пути, останавливаемся на родителе
    for (let i = 0; i < path.length - 1; i++) {
      yield this.snapshot([parent.id], `Ищем место вставки: спускаемся ${path[i] === 0 ? 'влево' : 'вправо'}`);
      parent = path[i] === 0 ? parent.left! : parent.right!;
    }

    const direction = path[path.length - 1];
    if (direction === 0) {
      parent.left = newNode;
    } else {
      parent.right = newNode;
    }

    yield this.snapshot([newNode.id, parent.id], `Добавлен узел ${value} в конец кучи`);

    // Sift Up (Просеивание вверх)
    let currentNodeIndex = this.size;
    
    while (currentNodeIndex > 1) {
      const parentIndex = Math.floor(currentNodeIndex / 2);
      const parentPath = this.getPathToNode(parentIndex);
      
      let pNode = this.root;
      for (const move of parentPath) {
        pNode = move === 0 ? pNode.left! : pNode.right!;
      }

      const cPath = this.getPathToNode(currentNodeIndex);
      let cNode = this.root;
      for (const move of cPath) {
        cNode = move === 0 ? cNode.left! : cNode.right!;
      }

      yield this.snapshot([cNode.id, pNode.id], `Sift-Up: сравниваем ${cNode.value} и родителя ${pNode.value}`);

      if (cNode.value < pNode.value) {
        yield this.snapshot([cNode.id, pNode.id], `${cNode.value} меньше, чем ${pNode.value}. Меняем значения местами.`);
        // Для анимации мы меняем значения, а не ссылки (ID остаются на месте, узел "мигает" новым значением)
        // Но чтобы узлы физически переместились, лучше менять значения, так как структура кучи фиксирована.
        const temp = cNode.value;
        cNode.value = pNode.value;
        pNode.value = temp;
        yield this.snapshot([cNode.id, pNode.id], `Поменяли значения`);
        currentNodeIndex = parentIndex;
      } else {
        break;
      }
    }

    yield this.snapshot([], `Мин-Куча восстановлена`);
  }

  *search(value: number): Generator<TreeState<number>, boolean, unknown> {
    yield this.snapshot([], `Поиск в куче - это полный обход O(N), так как куча не упорядочена по горизонтали.`);
    let found = false;

    // Простой BFS или DFS
    const queue: TreeNode<number>[] = [];
    if (this.root) queue.push(this.root);

    while (queue.length > 0) {
      const current = queue.shift()!;
      yield this.snapshot([current.id], `Проверяем узел ${current.value}`);
      
      if (current.value === value) {
        yield this.snapshot([current.id], `Найдено значение ${value}!`);
        found = true;
        break;
      }

      if (current.left) queue.push(current.left);
      if (current.right) queue.push(current.right);
    }

    if (!found) {
      yield this.snapshot([], `Значение ${value} не найдено.`);
    }

    return found;
  }

  *delete(_value: number): Generator<TreeState<number>, void, unknown> {
    yield this.snapshot([], `Извлечение минимума (Extract-Min) - удаление конкретного значения не совсем стандартно для кучи.`);
    if (!this.root || this.size === 0) return;

    if (this.size === 1) {
      yield this.snapshot([this.root.id], `Извлечен единственный элемент кучи`);
      this.root = null;
      this.size = 0;
      return;
    }

    // 1. Находим последний элемент
    const path = this.getPathToNode(this.size);
    let parentOfLast = this.root;
    for (let i = 0; i < path.length - 1; i++) {
        parentOfLast = path[i] === 0 ? parentOfLast.left! : parentOfLast.right!;
    }
    
    const direction = path[path.length - 1];
    const lastNode = direction === 0 ? parentOfLast.left! : parentOfLast.right!;

    yield this.snapshot([this.root.id, lastNode.id], `Извлекаем минимум ${this.root.value}. Меняем его с последним узлом ${lastNode.value}`);

    this.root.value = lastNode.value;
    if (direction === 0) parentOfLast.left = null; else parentOfLast.right = null;
    this.size--;

    yield this.snapshot([this.root.id], `Последний узел удален из конца. Запущен Sift-Down для ${this.root.value}`);

    // Sift Down (Опускание вниз)
    let current = this.root;
    while (current.left) {
      let smallerChild = current.left;
      if (current.right && current.right.value < current.left.value) {
        smallerChild = current.right;
      }

      yield this.snapshot([current.id, smallerChild.id], `Sift-Down: сравниваем ${current.value} с наименьшим потомком ${smallerChild.value}`);

      if (current.value > smallerChild.value) {
        const temp = current.value;
        current.value = smallerChild.value;
        smallerChild.value = temp;
        yield this.snapshot([current.id, smallerChild.id], `Поменяли значения местами`);
        current = smallerChild;
      } else {
        break;
      }
    }

    yield this.snapshot([], `Свойство кучи восстановлено`);
  }
}

import { TreeNode } from '../Node';

/**
 * Описывает снимок состояния дерева в конкретный момент времени для визуализатора.
 * Используется генераторами алгоритмов, чтобы передать текущее расположение и состояние.
 */
export interface TreeState<T> {
  /** Копия корневого узла дерева на текущем шаге. */
  root: TreeNode<T> | null;
  /** Массив ID узлов, с которыми происходит работа (они будут подсвечены). */
  activeNodeIds: string[]; 
  /** Описание текущего шага алгоритма (например, "Сравниваем 10 с 5"). */
  message: string;         
}

/**
 * Базовый абстрактный класс для всех бинарных деревьев.
 * Предоставляет методы-генераторы (со звездочкой *) для выполнения алгоритмов по шагам
 * и возвращения состояния дерева в интерфейс React через оператор yield.
 */
export abstract class BaseTree<T> {
  /** Корневой узел дерева. */
  root: TreeNode<T> | null = null;

  /** Метод вставки нового значения в дерево. */
  abstract insert(value: T): Generator<TreeState<T>, void, unknown>;
  /** Метод удаления значения из дерева. */
  abstract delete(value: T): Generator<TreeState<T>, void, unknown>;
  /** Метод поиска значения в дереве. Возвращает true, если значение найдено. */
  abstract search(value: T): Generator<TreeState<T>, boolean, unknown>;

  /**
   * Создает моментальный снимок (слепок) текущего дерева для визуализатора.
   * @param activeNodeIds Узлы, которые нужно подсветить.
   * @param message Информационное сообщение для вывода на экран.
   */
  protected snapshot(activeNodeIds: string[] = [], message: string = ""): TreeState<T> {
    return {
      root: this.cloneTree(this.root),
      activeNodeIds,
      message,
    };
  }

  /**
   * Выполняет глубокое копирование дерева. 
   * Это необходимо для React, чтобы при каждом шаге создавался новый объект корня
   * (иммутабельность состояния) и компонент обновлялся.
   */
  protected cloneTree(node: TreeNode<T> | null): TreeNode<T> | null {
    if (!node) return null;
    const cloned = new TreeNode(node.value);
    cloned.id = node.id; // Важно: сохраняем старый ID для связывания анимаций макета Framer Motion
    cloned.height = node.height;
    cloned.color = node.color;
    cloned.priority = node.priority;
    cloned.left = this.cloneTree(node.left);
    cloned.right = this.cloneTree(node.right);
    return cloned;
  }
}



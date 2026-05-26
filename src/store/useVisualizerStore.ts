import { create } from 'zustand';
import { BaseTree, type TreeState } from '../core/tree/BaseTree';
import { BST } from '../core/tree/BST';
import { AVLTree } from '../core/tree/AVL';
import { Treap } from '../core/tree/Treap';
import { MinHeap } from '../core/tree/MinHeap';

export type TreeType = 'BST' | 'AVL' | 'Treap' | 'MinHeap';

interface VisualizerState {
  treeType: TreeType;
  tree: BaseTree<number>;
  state: TreeState<number>;
  isPlaying: boolean;
  speedMs: number;
  
  // Actions
  setTreeType: (type: TreeType) => void;
  insert: (value: number) => Promise<void>;
  delete: (value: number) => Promise<void>;
  search: (value: number) => Promise<void>;
  setSpeed: (ms: number) => void;
  reset: () => void;
}

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

const createTree = (type: TreeType) => {
  switch (type) {
    case 'AVL': return new AVLTree();
    case 'Treap': return new Treap();
    case 'MinHeap': return new MinHeap();
    case 'BST':
    default: return new BST();
  }
};

export const useVisualizerStore = create<VisualizerState>((set, get) => {
  const initialTree = createTree('BST');
  
  return {
    treeType: 'BST',
    tree: initialTree,
    state: { root: null, activeNodeIds: [], message: 'Готов' },
    isPlaying: false,
    speedMs: 800,

    setTreeType: (type) => {
      const { isPlaying } = get();
      if (isPlaying) return;
      set({ treeType: type, tree: createTree(type), state: { root: null, activeNodeIds: [], message: `Переключено на ${type}` } });
    },

    setSpeed: (ms) => set({ speedMs: ms }),
    
    reset: () => {
      const newTree = createTree(get().treeType);
      set({ tree: newTree, state: { root: null, activeNodeIds: [], message: 'Дерево очищено' }, isPlaying: false });
    },

    insert: async (value: number) => {
      const { tree, isPlaying } = get();
      if (isPlaying) return;
      
      set({ isPlaying: true });
      const generator = tree.insert(value);
      
      for (const step of generator) {
        set({ state: step });
        await sleep(get().speedMs);
      }
      set({ isPlaying: false, state: { ...get().state, activeNodeIds: [], message: `Вставка ${value} завершена` } });
    },

    delete: async (value: number) => {
      const { tree, isPlaying } = get();
      if (isPlaying) return;
      
      set({ isPlaying: true });
      const generator = tree.delete(value);
      
      for (const step of generator) {
        set({ state: step });
        await sleep(get().speedMs);
      }
      set({ isPlaying: false, state: { ...get().state, activeNodeIds: [], message: `Удаление ${value} завершено` } });
    },

    search: async (value: number) => {
      const { tree, isPlaying } = get();
      if (isPlaying) return;
      
      set({ isPlaying: true });
      const generator = tree.search(value);
      
      for (const step of generator) {
        set({ state: step });
        await sleep(get().speedMs);
      }
      set({ isPlaying: false, state: { ...get().state, activeNodeIds: [], message: `Поиск ${value} завершен` } });
    }
  };
});


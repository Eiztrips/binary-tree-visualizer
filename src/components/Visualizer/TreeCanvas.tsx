import React, { useMemo } from 'react';
import { motion } from 'framer-motion';
import { TransformWrapper, TransformComponent } from 'react-zoom-pan-pinch';
import { useVisualizerStore } from '../../store/useVisualizerStore';
import { TreeNode } from '../../core/Node';

interface LayoutNode {
  node: TreeNode<number>;
  x: number;
  y: number;
}

const LEVEL_HEIGHT = 80;
const NODE_RADIUS = 24;

export const TreeCanvas: React.FC = () => {
  const { state } = useVisualizerStore();

  const layout = useMemo(() => {
    const nodes: LayoutNode[] = [];
    const edges: { id: string; x1: number; y1: number; x2: number; y2: number }[] = [];

    const computeLayout = (
      node: TreeNode<number> | null,
      x: number,
      y: number,
      offset: number
    ) => {
      if (!node) return;
      nodes.push({ node, x, y });

      if (node.left) {
        const childX = x - offset;
        const childY = y + LEVEL_HEIGHT;
        edges.push({
          id: `${node.id}-${node.left.id}`,
          x1: x,
          y1: y,
          x2: childX,
          y2: childY,
        });
        computeLayout(node.left, childX, childY, offset / 2);
      }

      if (node.right) {
        const childX = x + offset;
        const childY = y + LEVEL_HEIGHT;
        edges.push({
          id: `${node.id}-${node.right.id}`,
          x1: x,
          y1: y,
          x2: childX,
          y2: childY,
        });
        computeLayout(node.right, childX, childY, offset / 2);
      }
    };

    computeLayout(state.root, 600, 50, 300); // Сместили старт для большего холста
    return { nodes, edges };
  }, [state.root]);

  return (
    <div className="w-full h-[600px] bg-white rounded-xl border-2 border-[#D4B88A] shadow-sm overflow-hidden touch-none">
      <TransformWrapper
        initialScale={1}
        minScale={0.2}
        maxScale={4}
        centerOnInit={true}
        limitToBounds={false}
        animation={{ animationTime: 200, animationType: "easeOut" }}
      >
        <TransformComponent wrapperClass="!w-full !h-full" contentClass="!w-full !h-full cursor-grab active:cursor-grabbing">
          <svg width="1200" height="800" viewBox="0 0 1200 800" className="w-full h-full pointer-events-none">
            {/* Ребра */}
            {layout.edges.map((edge) => (
              <motion.line
                key={edge.id}
                initial={{ opacity: 0, x1: edge.x1, y1: edge.y1, x2: edge.x2, y2: edge.y2 }}
                animate={{ opacity: 1, x1: edge.x1, y1: edge.y1, x2: edge.x2, y2: edge.y2 }}
                stroke="#D4B88A"
                strokeWidth={3}
                transition={{ duration: 0.4 }}
              />
            ))}

            {/* Узлы */}
            {layout.nodes.map(({ node, x, y }) => {
              const isActive = state.activeNodeIds.includes(node.id);

              return (
                <motion.g
                  key={node.id}
                  initial={{ opacity: 0, scale: 0.5, x, y }}
                  animate={{ opacity: 1, scale: 1, x, y }}
                  transition={{ type: 'spring', bounce: 0.3, duration: 0.6 }}
                >
                  <circle
                    r={NODE_RADIUS}
                    fill={isActive ? '#9B4F2C' : '#F5E8C7'}
                    stroke={isActive ? '#5C2F1B' : '#9B4F2C'}
                    strokeWidth={isActive ? 4 : 2}
                  />
                  <text
                    textAnchor="middle"
                    dy=".3em"
                    fill={isActive ? '#FFF' : '#5C2F1B'}
                    className="font-mono text-base font-bold select-none"
                  >
                    {node.value}
                  </text>
                  <text
                    textAnchor="middle"
                    dy="-2em"
                    fill="#9B4F2C"
                    className="font-mono text-[10px] font-bold select-none opacity-80"
                  >
                    {/* Показываем приоритет для Treap или высоту для AVL */}
                    {node.priority !== undefined && node.priority !== -1 && typeof node.priority === 'number' ? `pr:${node.priority}` : ''}
                  </text>
                </motion.g>
              );
            })}
          </svg>
        </TransformComponent>
      </TransformWrapper>
    </div>
  );
};

import React, { useState } from 'react';
import { useVisualizerStore, type TreeType } from '../../store/useVisualizerStore';
import { RotateCcw, Search, Plus, Trash2 } from 'lucide-react';

interface ActionButtonProps {
  onClick: () => void;
  disabled: boolean;
  icon: React.ReactNode;
  label: string;
  color: string;
}

export const Controls: React.FC = () => {
  const [inputValue, setInputValue] = useState('');
  const { insert, search, delete: del, isPlaying, speedMs, setSpeed, reset, treeType, setTreeType } = useVisualizerStore();

  const handleAction = (action: (val: number) => void) => {
    const val = parseInt(inputValue, 10);
    if (!isNaN(val)) {
      action(val);
      setInputValue('');
    }
  };

  return (
    <div className="flex flex-col md:flex-row gap-4 w-full bg-[#F5E8C7] p-5 rounded-xl border border-[#D4B88A] shadow-sm items-center justify-between">
      
      {/* Выбор типа дерева */}
      <div className="flex bg-white rounded-lg p-1 border border-[#D4B88A] w-full md:w-auto shadow-sm">
        <select 
          className="bg-transparent border-none outline-none text-[#5C2F1B] px-4 py-2 font-medium cursor-pointer"
          value={treeType}
          onChange={(e) => setTreeType(e.target.value as TreeType)}
          disabled={isPlaying}
        >
          <option value="BST">Двоичное дерево поиска</option>
          <option value="AVL">АВЛ-дерево</option>
          <option value="Treap">Декартово дерево (Treap)</option>
          <option value="MinHeap">Мин-Куча (Min-Heap)</option>
        </select>
      </div>

      {/* Панель ввода и команд */}
      <div className="flex bg-white rounded-lg p-1 border border-[#D4B88A] w-full md:w-auto shadow-sm relative">
        <input
          type="number"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          placeholder="Введите число"
          className="bg-transparent border-none outline-none px-4 text-[#5C2F1B] placeholder-[#D4B88A] w-40 font-mono"
          disabled={isPlaying}
          onKeyDown={(e) => {
            if (e.key === 'Enter') handleAction(insert);
          }}
        />
        
        <div className="flex gap-1 border-l border-[#D4B88A] pl-1 ml-1">
          <ActionButton onClick={() => handleAction(insert)} disabled={isPlaying} icon={<Plus size={18} />} label="Вставить" color="text-[#9B4F2C] hover:bg-[#D4B88A]/30" />
          <ActionButton onClick={() => handleAction(search)} disabled={isPlaying} icon={<Search size={18} />} label="Найти" color="text-[#5C2F1B] hover:bg-[#D4B88A]/30" />
          <ActionButton onClick={() => handleAction(del)} disabled={isPlaying} icon={<Trash2 size={18} />} label="Удалить" color="text-red-600 hover:bg-red-100" />
        </div>
      </div>

      {/* Управление плеером */}
      <div className="flex gap-4 items-center bg-white rounded-lg py-2 px-4 border border-[#D4B88A] shadow-sm">
        <div className="flex flex-col w-32 gap-1.5">
          <div className="flex justify-between text-[11px] uppercase tracking-wider text-[#9B4F2C] font-semibold">
            <span>Скорость</span>
            <span>{speedMs}мс</span>
          </div>
          <input
            type="range"
            min="100"
            max="2000"
            step="100"
            value={speedMs}
            onChange={(e) => setSpeed(Number(e.target.value))}
            className="w-full h-1.5 bg-[#F5E8C7] rounded-lg appearance-none cursor-pointer accent-[#9B4F2C]"
            disabled={isPlaying}
          />
        </div>
        
        <div className="w-px h-8 bg-[#D4B88A] mx-2" />
        
        <button
          onClick={reset}
          disabled={isPlaying}
          className="p-2 rounded-md text-[#9B4F2C] hover:text-[#5C2F1B] hover:bg-[#D4B88A]/30 transition-colors disabled:opacity-50"
          title="Сбросить дерево"
        >
          <RotateCcw size={20} />
        </button>
      </div>

    </div>
  );
};

const ActionButton: React.FC<ActionButtonProps> = ({ onClick, disabled, icon, label, color }) => (
  <button
    onClick={onClick}
    disabled={disabled}
    title={label}
    className={`p-2 rounded-md transition-all flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed ${color}`}
  >
    {icon}
  </button>
);

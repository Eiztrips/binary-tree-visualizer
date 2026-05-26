import React from 'react';
import { TreeCanvas } from './components/Visualizer/TreeCanvas';
import { Controls } from './components/Visualizer/Controls';
import { useVisualizerStore } from './store/useVisualizerStore';

function App() {
  const { state } = useVisualizerStore();

  return (
    <div className="min-h-screen bg-[#FAF6EB] text-[#5C2F1B] p-4 md:p-8 font-sans selection:bg-[#D4B88A]/30">
      <div className="max-w-6xl mx-auto space-y-6">
        
        {/* Header Section */}
        <header className="space-y-2 text-center md:text-left">
          <h1 className="text-4xl font-bold tracking-tight text-[#9B4F2C]">
            Визуализатор Бинарных Деревьев
          </h1>
          <p className="text-[#5C2F1B]/80 max-w-2xl text-lg">
            Интерактивная пошаговая визуализация работы алгоритмов двоичных деревьев.
          </p>
        </header>

        {/* Main Interface */}
        <main className="space-y-4">
          <Controls />
          
          {/* Информационное табло вынесено наружу, чтобы не перекрывать граф */}
          <div className="bg-[#F5E8C7] px-6 py-3 rounded-xl border border-[#D4B88A] text-[#9B4F2C] font-semibold text-center shadow-sm w-full mx-auto">
            {state.message || "Визуализатор готов к работе"}
          </div>

          <TreeCanvas />
        </main>
        
      </div>
    </div>
  );
}

export default App;


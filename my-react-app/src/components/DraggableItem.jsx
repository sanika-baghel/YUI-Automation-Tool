import React from 'react';
import { useDrag } from 'react-dnd';

const DraggableItem = ({ type, text }) => {
  const [, drag] = useDrag({
    type,
    item: { type, text },
  });

  return (
    <div ref={drag} className="draggable-item">
      {text}
    </div>
  );
};

export default DraggableItem;
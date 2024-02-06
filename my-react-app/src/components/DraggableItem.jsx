import React from 'react';
import { useDrag } from 'react-dnd';
 
const DraggableItem = ({ type, text }) => {
  const [, drag] = useDrag({
    type,
    item: { type, text },
  });
 
  return (
    <div ref={drag} style={{ padding: '8px', margin: '8px', cursor: 'move', border: '1px solid #ccc' }}>
      {text}
    </div>
  );
};
 
export default DraggableItem;
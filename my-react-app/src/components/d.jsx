import React, { useState, useEffect } from 'react';
import { useDrop } from 'react-dnd';
import Draggable from 'react-draggable';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const CustomContextMenu = ({ visible, x, y, options, onSelect }) => {
  return (
    <div
      className="custom-context-menu"
      style={{
        display: visible ? 'block' : 'none',
        position: 'fixed',
        top: y,
        left: x,
        background: 'white',
        border: '1px solid #ccc',
        padding: '8px',
        zIndex: 1000,
      }}
    >
      {options.map((option) => (
        <div
          key={option.label}
          onClick={() => onSelect(option.value)}
          className="context-menu-option"
        >
          {option.label}
        </div>
      ))}
    </div>
  );
};

const DropTargetPanel = ({ onHover, droppedItems, setDroppedItems }) => {
  const [contextMenu, setContextMenu] = useState({ visible: false, index: -1, x: 0, y: 0 });
  const [showLabelIdOptions, setShowLabelIdOptions] = useState(Array(droppedItems.length).fill(true));

  const [, drop] = useDrop({
    accept: ['BUTTON', 'TEXTBOX', 'RADIO', 'CHECKBOX', 'DROPDOWN', 'LOOKUP'],
    drop: (item, monitor) => handleDrop(item, monitor),
  });

  const handleClickOutside = (e) => {
    if (contextMenu.visible && !e.target.closest('.custom-context-menu')) {
      setContextMenu({ visible: false, index: -1, x: 0, y: 0 });
    }
  };

  useEffect(() => {
    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, [contextMenu]);

  const handleDrop = (item, monitor) => {
    const offset = monitor.getSourceClientOffset();
    const x = offset.x;
    const y = offset.y;

    // Add x, y coordinates to the dropped item
    const newItem = {
      type: item.type,
      id: droppedItems.length + 1,
      text: item.text,
      label: '',
      id: '',
      options: [],
      mandatory: false,
      // Include x, y coordinates in the dropped item
      x,
      y,
    };

    setDroppedItems([...droppedItems, newItem]);
    setShowLabelIdOptions([...showLabelIdOptions, true]);
  };

  // ... (rest of the code remains unchanged)

  return (
    <div ref={drop} className="col-lg-3 " style={panelStyle}>
      {/* ... (rest of the code remains unchanged) */}
    </div>
  );
};

export default DropTargetPanel;

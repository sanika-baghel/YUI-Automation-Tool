import React, { useState } from 'react';
import DraggableItem from './components/DraggableItem';
import DropTargetPanel from './components/DropTargetPanel';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
 
const App = () => {
  const [hoveredItemId, setHoveredItemId] = useState(null);
  const [hoveredItemLabel, setHoveredItemLabel] = useState(null);
  const [hoveredItemClass, setHoveredItemClass] = useState(null);
  const [hoveredItemReadOnly, setHoveredItemReadOnly] = useState(false);
  const [hoveredItemMandatory, setHoveredItemMandatory] = useState(false);
  const [droppedItems, setDroppedItems] = useState([]);
 
  const handleHover = (itemId,itemLabel,itemClass,itemReadOnly,itemMadetory) => {
    setHoveredItemId(itemId);
    setHoveredItemLabel(itemLabel);
    setHoveredItemClass(itemClass);
    setHoveredItemReadOnly(itemReadOnly);
    setHoveredItemMandatory(itemMadetory);
  };
 
 
  const downloadJsonFile = () => {
    const jsonContent = JSON.stringify(droppedItems, null, 2);
    const blob = new Blob([jsonContent], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'dropped_items.json';
    a.click();
  };
 
  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-md-3 sidebar">
          <h8>ToolBox</h8>
          <DraggableItem type="BUTTON" text="Button" />
          <DraggableItem type="TEXTBOX" text="Textbox" />
          <DraggableItem type="RADIO" text="Radio Button" />
          <DraggableItem type="CHECKBOX" text="Checkbox" />
          <DraggableItem type="DROPDOWN" text="Catalog" />
          <DraggableItem type="LOOKUP" text="Lookup" />
        </div>
 
        <div className="col-md-7 code-editor">
          <h8>Drop Target Panel</h8>
          <DropTargetPanel droppedItems={droppedItems} setDroppedItems={setDroppedItems} onHover={handleHover} />
        </div>
 
        <div className="col-md-2 output-window">
          <h8>Show Details</h8>
          {hoveredItemId && <p>Field ID: {hoveredItemId}</p>}
          {hoveredItemLabel && <p>Field Label: {hoveredItemLabel}</p>}
          {hoveredItemClass && <p>Field Class: {hoveredItemClass}</p>}
          {hoveredItemReadOnly && <p>IsReadOnly: {hoveredItemReadOnly.toString()}</p>}
          {hoveredItemMandatory && <p>IsMandatory: {hoveredItemMandatory.toString()}</p>}
        </div>
      </div>
 
      <div className="fixed-bottom fixed-left p-3">
        <button onClick={downloadJsonFile} className="btn btn-primary">
          Download JSON
        </button>
      </div>
    </div>
  );
};
 
export default App;
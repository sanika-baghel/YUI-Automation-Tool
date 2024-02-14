import React, { useState } from 'react';
import DraggableItem from './components/DraggableItem';
import DropTargetPanel from './components/DropTargetPanel';
import NavigationBar from './components/NavigationBar'; // Import the NavigationBar component
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import ListGroup from 'react-bootstrap/ListGroup';

const App = () => {
  const [hoveredItemId, setHoveredItemId] = useState(null);
  const [hoveredItemLabel, setHoveredItemLabel] = useState(null);
  const [hoveredItemClass, setHoveredItemClass] = useState(null);
  const [hoveredItemValue, setHoveredItemValue] = useState(null);
  const [hoveredItemReadOnly, setHoveredItemReadOnly] = useState(false);
  const [hoveredItemMandatory, setHoveredItemMandatory] = useState(false);
  const [droppedItems, setDroppedItems] = useState([]);

  const handleHover = (itemId, itemLabel, itemClass, itemReadOnly, itemMadetory, itemValue) => {
    setHoveredItemId(itemId);
    setHoveredItemLabel(itemLabel);
    setHoveredItemClass(itemClass);
    setHoveredItemReadOnly(itemReadOnly);
    setHoveredItemMandatory(itemMadetory);
    setHoveredItemValue(itemValue);
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
      <NavigationBar /> {/* Include the NavigationBar component here */}
      <div className="row">
        <div className="col-md-3 sidebar">
          <h8><b>ToolBox</b></h8>
          <DraggableItem type="BUTTON" text="Button" />
          <DraggableItem type="TEXTBOX" text="Textbox" />
          <DraggableItem type="RADIO" text="Radio Button" />
          <DraggableItem type="CHECKBOX" text="Checkbox" />
          <DraggableItem type="DROPDOWN" text="Catalog" />
          <DraggableItem type="LOOKUP" text="Lookup" />
          <DraggableItem type="TEXTAREA" text="Textarea" />
          <DraggableItem type="CALENDER" text="Calender" />
          <DraggableItem type="BARCODE" text="Barcode" />
          <DraggableItem type="ATTACHMENT" text="Attachment" />
          <DraggableItem type="LOOKUPANDBARCODE" text="Lookup & Barcode" />
          <DraggableItem type="HEADER" text="Header" />
          <DraggableItem type="FOOTER" text="Footer" />
        </div>

        <div className="col-md-7 code-editor">
          <h8>Drop Target Panel</h8>
          <DropTargetPanel droppedItems={droppedItems} setDroppedItems={setDroppedItems} onHover={handleHover} />
        </div>

        <div className="col-md-2 output-window">
          <div className="bottom left p-3">
            <button onClick={downloadJsonFile} className="bottom btn btn-dark">
              Download JSON
            </button>
          </div>
          <h8><b>Show Details</b></h8><br /><br />
          {hoveredItemId && <span><b>Field ID :</b>{hoveredItemId}</span>}<br />
          {hoveredItemLabel && <span><b>Field Label :</b> {hoveredItemLabel}</span>} <br />
          {hoveredItemMandatory && <span><b>IsMandatory :</b>{hoveredItemMandatory.toString()}</span>}<br />
          {hoveredItemClass && <h8><b>Field Class :</b> {hoveredItemClass}</h8>} <br />
          {hoveredItemValue && <h8><b>Field Value :</b> {hoveredItemValue}</h8>} <br />
          {hoveredItemReadOnly && <h8><b>IsReadOnly :</b>{hoveredItemReadOnly.toString()}</h8>}
        </div>
        
      </div>
    </div>
  );
};

export default App;

import React, { useState } from 'react';
import DraggableItem from './components/DraggableItem';
import DropTargetPanel from './components/DropTargetPanel';
import NavigationBar from './components/NavigationBar'; // Import the NavigationBar component
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

const App = () => {
  const [hoveredItem, setHoveredItem] = useState({
    id: null,
    label: null,
    class: null,
    value: null,
    readOnly: false,
    mandatory: false,
  });

  const [droppedItems, setDroppedItems] = useState([]);

  const handleHover = (itemId, itemLabel, itemClass, itemReadOnly, itemMandatory, itemValue) => {
    setHoveredItem({
      id: itemId,
      label: itemLabel,
      class: itemClass,
      value: itemValue,
      readOnly: itemReadOnly,
      mandatory: itemMandatory,
    });
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

  const [editedLabel, setEditedLabel] = useState('');
  const [editedClass, setEditedClass] = useState('');
  const [editedValue, setEditedValue] = useState('');


  // Function to handle changes in the input fields
  const handleLabelChange = (event) => {
    setEditedLabel(event.target.value);
    updateHoveredItem();
  };

  const handleClassChange = (event) => {
    setEditedClass(event.target.value);
    updateHoveredItem();
  };

  const handleValueChange = (event) => {
    setEditedValue(event.target.value);
    updateHoveredItem();
  };

  const updateHoveredItem = () => {
    setHoveredItem((prevItem) => ({
      ...prevItem,
      label: editedLabel,
      class: editedClass,
      value: editedValue,
    }));
  };


  return (
    <div className="container-fluid">
      <NavigationBar /> {/* Include the NavigationBar component here */}
      <div className="row">
        <div className="col-md-2 sidebar">
          <div style={{ overflowY: 'auto', maxHeight: '380px' }}>
            <h8 style={{ color: 'white' }}><b>ToolBox</b></h8>
            <DraggableItem type="BUTTON" text="Button" />
            <DraggableItem type="TEXTBOX" text="Textbox" />
            <DraggableItem type="RADIO" text="Radio Button" />
            <DraggableItem type="CHECKBOX" text="Checkbox" />
            <DraggableItem type="DROPDOWN" text="Catalog" />
            <DraggableItem type="LOOKUP" text="Lookup" />
            <DraggableItem type="TEXTAREA" text="Textarea" />
            <DraggableItem type="CALENDAR" text="Calendar" />
            <DraggableItem type="BARCODE" text="Barcode" />
            <DraggableItem type="ATTACHMENT" text="Attachment" />
            <DraggableItem type="LOOKUPANDBARCODE" text="Lookup & Barcode" />
            <DraggableItem type="HEADER" text="Header" />
            <DraggableItem type="FOOTER" text="Footer" />
          </div>
          <div className="bottom left p-3" style={{ position: 'fixed', bottom: '0', left: '0', padding: '15px' }}>
            <button onClick={downloadJsonFile} className="btn btn-light" style={{ color: 'black' }}>
              Download JSON
            </button>
          </div>
        </div>

        <div className="col-md-7 code-editor">
          <div style={{ overflowY: 'auto', maxHeight: '620px' }}>
            <h8 style={{ color: 'white' }}>Drop Target Panel</h8>
            <DropTargetPanel droppedItems={droppedItems} setDroppedItems={setDroppedItems} onHover={handleHover} />
          </div>
        </div>

        <div className="col-md-3 output-window">
          <h8 style={{ color: 'white' }}><b>Properties</b></h8><br /><br />
          <table className="table table-bordered" style={{ borderCollapse: 'collapse' }}>
            <tbody>
              <TableRow label="Field ID" value={hoveredItem.id} />
              <TableRow label="Field Label" value={hoveredItem.label} editable onChange={handleLabelChange} />
              <TableRow label="Is Mandatory" value={hoveredItem.mandatory.toString()} />
              <TableRow label="Field Class" value={hoveredItem.class} editable onChange={handleClassChange} />
              <TableRow label="Field Value" value={hoveredItem.value} editable onChange={handleValueChange} />
              <TableRow label="Is Read Only" value={hoveredItem.readOnly.toString()} />
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

const TableRow = ({ label, value, editable, onChange }) => (
  <tr>
    <td><b>{label}</b></td>
    <td>
      {editable ? (
        <input type="text" value={value} onChange={onChange} />
      ) : (
        value
      )}
    </td>
  </tr>
);

export default App;

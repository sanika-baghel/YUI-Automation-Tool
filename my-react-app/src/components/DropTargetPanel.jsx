import React, { useState, useEffect } from 'react';
import { useDrop } from 'react-dnd';
import Draggable from 'react-draggable';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Form, InputGroup } from 'react-bootstrap';
import { faBarcode, faCalendarDays, faSearch } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';

const CustomContextMenu = ({ visible, x, y, options, onSelect, readOnly, editable }) => {
  let filteredOptions = options;
  if (readOnly) {
    filteredOptions = options.filter(
      (option) => option.value !== "makeReadOnly"
    );
  } else if (editable) {
    filteredOptions = options.filter(
      (option) => option.value !== "makeEditable"
    );
  }
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
      {filteredOptions.map((option) => (
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
    accept: ['BUTTON', 'TEXTBOX', 'RADIO', 'CHECKBOX', 'DROPDOWN', 'LOOKUP', 'TEXTAREA', 'CALENDER', 'BARCODE', 'LOOKUPANDBARCODE', 'ATTACHMENT', 'HEADER', 'FOOTER'],
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
    let newItem;
  
    if (item.type === 'RADIO') {
      const count = prompt('Enter the number of radio buttons to create:');
      if (count && !isNaN(count)) {
        newItem = {
          type: item.type,
          id: droppedItems.length + 1,
          value: '',
          text: item.text,
          class: '',
          label: '',
          id: '',
          readOnly: false,
          options: [],
          mandatory: false,
        };
  
        setDroppedItems([...droppedItems, ...Array(parseInt(count)).fill(newItem)]);
        setShowLabelIdOptions([...showLabelIdOptions, ...Array(parseInt(count)).fill(true)]);
      }
    } else {
      newItem = {
        type: item.type,
        id: droppedItems.length + 1,
        value: '',
        text: item.text,
        class: '',
        label: '',
        id: '',
        readOnly: false,
        options: [],
        mandatory: false,
      };
  
      setDroppedItems([...droppedItems, newItem]);
      setShowLabelIdOptions([...showLabelIdOptions, true]);
    }
  };
  

  const handleMakeReadOnly = (index) => {
    const confirmed = window.confirm('Do you want this field Readonly?');
    if (confirmed) {
      const updatedItems = [...droppedItems];
      updatedItems[index] = {
        ...updatedItems[index],
        readOnly: true,
        color: '#f2f2f2',
      };
      setDroppedItems(updatedItems);
    }
    setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
  };

  const handleMakeEditable = (index) => {
    const updatedItems = [...droppedItems];
    updatedItems[index] = {
      ...updatedItems[index],
      readOnly: false, // Set readOnly to false when making the item editable again
    };
    setDroppedItems(updatedItems);
    const hoveredItem = droppedItems[index]; // Update the hovered item's read-only state to false without clearing other details
    onHover(
      hoveredItem.id,
      hoveredItem.label,
      hoveredItem.class,
      false // Indicate that the item is no longer read-only
    );
  };

  const handleContextMenu = (e, index) => {
    e.preventDefault();
    const showAddDropdownOption = droppedItems[index]?.type === 'DROPDOWN';
    setContextMenu({ visible: true, index, x: e.clientX, y: e.clientY, showAddDropdownOption });
  };

  const handleMouseEnter = (index) => {
    const currentItem = droppedItems[index];
    onHover(currentItem.id, currentItem.label, currentItem.class, currentItem.readOnly, currentItem.mandatory, currentItem.value);
  };

  const handleAddDropdownOption = (index) => {
    const heading = droppedItems[index].text || 'Heading';
    const updatedItems = [...droppedItems];
    const dropdownOptions = updatedItems[index].options || [];
    const groupIndex = dropdownOptions.findIndex((group) => group.heading === heading);
    const optionText = prompt(`Enter an option for "${heading}" dropdown:`);
    if (optionText !== null && optionText.trim() !== '') { // Check if optionText is not empty
      if (groupIndex !== -1) {
        updatedItems[index].options[groupIndex].options.push({ text: optionText });
      } else {
        updatedItems[index].options.push({ heading, options: [{ text: optionText }] });
      }
      setDroppedItems(updatedItems);
    }
    setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
  };

  const handleDeleteItem = (index) => {
    const updatedItems = droppedItems.filter((item, i) => i !== index);
    setShowLabelIdOptions(showLabelIdOptions.filter((_, i) => i !== index));
    setDroppedItems(updatedItems);
    setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
  };

  const handleAddLabelId = (index, value) => {
    const inputValue = prompt(`Enter ${value}`);
    if (inputValue !== null) {
      const updatedItems = [...droppedItems];
      if (value === 'addClass') {
        updatedItems[index]['class'] = inputValue;
      } else if (value === 'addValue') {
        updatedItems[index]['value'] = inputValue;
      } else {
        updatedItems[index][value === 'addLabel' ? 'label' : 'id'] = inputValue;
      }
      setDroppedItems(updatedItems);
      setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
    }
  };

  const handleAddAstrikMark = (index) => {
    if (!droppedItems[index].label) {
      alert('Please add a label before marking the field as mandatory.');
      return;
    }
    const isMandatory = window.confirm('Do you want this field to be mandatory?');
    const updatedItems = [...droppedItems];
    updatedItems[index]['mandatory'] = isMandatory;
    setDroppedItems(updatedItems);
    setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
  };

  const handleRemoveAsteriskMark = (index) => {
    const confirmed = window.confirm('Do you want to remove the asterisk mark?');
    if (confirmed) {
      const updatedItems = [...droppedItems];
      updatedItems[index]['mandatory'] = false; // Set mandatory to false to remove the asterisk mark
      setDroppedItems(updatedItems);
    }
    setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
  };

  const panelStyle = {
    border: '5px ',
    padding: '16px',
    marginTop: '10px',
    position: 'relative',
  };

  const [selectedFile, setSelectedFile] = useState(null);
   const handleFileUpload = (event) => {
     setSelectedFile(event.target.files[0]);
   };
   // Add the handleDeleteLabel function outside the DropTargetPanel component
const handleDeleteLabel = (index) => {
  const confirmed = window.confirm('Are you sure you want to delete this label?');
  if (confirmed) {
    const updatedItems = [...droppedItems];
    updatedItems[index]['label'] = ''; // Remove the label
    setDroppedItems(updatedItems);
  }
  setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
};
  return (
    <div ref={drop} className="col-lg-3 " style={panelStyle}>
      <CustomContextMenu
        visible={contextMenu.visible}
        x={contextMenu.x}
        y={contextMenu.y}
        options={[
          ...(droppedItems[contextMenu.index]?.label ?
            [{ label: 'Delete Label', value: 'deleteLabel' }] :
            [{ label: 'Add Label', value: 'addLabel' }] 
          ),
          { label: 'Add ID', value: 'addID' },
          { label: 'Add Class', value: 'addClass' },
          { label: 'Add Value', value: 'addValue' },
          { label: 'Make ReadOnly', value: 'makeReadOnly' },
          { label: 'Make Editable', value: 'makeEditable' },
          ...(droppedItems[contextMenu.index]?.mandatory ?
            [{ label: 'Remove Asterisk Mark', value: 'removeAsteriskMark' }] :
            [{ label: 'Add Asterisk Mark', value: 'addAsteriskMark' }]
          ),
          ...(contextMenu.showAddDropdownOption ? [{ label: 'Add Dropdown Option', value: 'addDropdownOption' }] : []),
          { label: 'Delete', value: 'delete' },
        ]}
        onSelect={(value) => {
          if (value === 'addLabel' || value === 'addID' || value === 'addClass' || value === 'addValue') {
            handleAddLabelId(contextMenu.index, value);
          } else if (value === 'addDropdownOption') {
            handleAddDropdownOption(contextMenu.index);
          } else if (value === 'delete') {
            handleDeleteItem(contextMenu.index);
          } else if (value === 'addAsteriskMark') {
            handleAddAstrikMark(contextMenu.index);
          } else if (value === 'removeAsteriskMark') {
            handleRemoveAsteriskMark(contextMenu.index);
          } else if (value === 'makeReadOnly') {
            handleMakeReadOnly(contextMenu.index);
          } else if (value === 'makeEditable') {
            handleMakeEditable(contextMenu.index);
          }else if (value === 'deleteLabel') {
            handleDeleteLabel(contextMenu.index);
          }
          
        }}
        readOnly={droppedItems[contextMenu.index]?.readOnly}
        editable={!droppedItems[contextMenu.index]?.readOnly}
      />
      {droppedItems.map((item, index) => (
        <Draggable key={item.id} bounds=".code-editor">
          <div
            style={{ marginBottom: '8px', position: 'relative' }}
            onContextMenu={(e) => handleContextMenu(e, index)}
            onMouseEnter={() => handleMouseEnter(index)}
          >
            {showLabelIdOptions[index] && item.label && (
              <div style={{ marginBottom: '4px' }}>
                {item.mandatory && <span style={{ color: 'red' }}>*</span>}
                {item.type !== 'BUTTON' && item.label}   {/* Conditionally hide label for BUTTON type */}
              </div>
            )}
            {showLabelIdOptions[index] && item.id && (
              <div className="panel-3-id" style={{ display: 'none', position: 'absolute', top: '0', right: '0', background: '#eee', padding: '4px' }}>
                ID: {item.id}
              </div>
            )}
            {item.type === 'BUTTON' && (
              <button>
                {item.label ? (
                  // If label is provided, display the label instead of text
                  <>
                    {item.label}
                    {item.text && <span style={{ display: 'none' }}>{item.text}</span>}
                  </>
                ) : (
                  // If no label is provided, display the text
                  item.text
                )}
              </button>
            )}
            {item.type === 'TEXTBOX' && (
              <input type="text"
                placeholder={item.text}
                readOnly={item.readOnly}
                style={{ backgroundColor: item.readOnly ? item.color : '' }}
              />
            )}
            {item.type === 'RADIO' && <input type="radio"/>}
            {item.type === 'CHECKBOX' && <input type="checkbox" />}
            {item.type === 'DROPDOWN' && (
              <select className="form-select">
                {item.options?.map((group, i) => (
                  <optgroup key={i}>
                    {group.options?.map((option, j) => (
                      <option key={j} value={option.id} className="dropdown-option">
                        {option.text}
                      </option>
                    ))}
                  </optgroup>
                ))}
              </select>
            )}
            {item.type === 'LOOKUP' && (
              <>
                <InputGroup className="mb-3" readOnly={item.readOnly}
                  style={{ backgroundColor: item.readOnly ? item.color : '' }} >
                  <Form.Control
                    placeholder="Search..."
                    aria-label="Search"
                    aria-describedby="search-icon"
                    readOnly={item.readonly}
                    style={{ backgroundColor: item.readonly ? item.color : '' }}
                  />
                  <InputGroup.Text id="search-icon" readOnly={item.readOnly}
                    style={{ backgroundColor: item.readOnly ? item.color : '' }}>
                    <FontAwesomeIcon icon={faSearch} />
                  </InputGroup.Text>
                </InputGroup>
              </>
            )}
            {item.type === 'ATTACHMENT' && (
              <div>
              <input type="file" onChange={handleFileUpload} />
            </div>
            )}
            {item.type === 'TEXTAREA' && (
              <textarea
                className="form-control mt-2"
                rows="3"
                placeholder="Enter text here..."
                style={{ width: '400%' }}
              />
            )}
            {item.type === 'CALENDER' && (
              <>
                <InputGroup className="mb-3" readOnly={item.readOnly}
                  style={{ backgroundColor: item.readOnly ? item.color : '' }} >
                  <Form.Control
                    aria-label="Search"
                    aria-describedby="search-icon"
                    readOnly={item.readonly}
                    style={{ backgroundColor: item.readonly ? item.color : '' }}
                  />
                  <InputGroup.Text id="search-icon" readOnly={item.readOnly}
                    style={{ backgroundColor: item.readOnly ? item.color : '' }}>
                    <FontAwesomeIcon icon={faCalendarDays} />
                  </InputGroup.Text>
                </InputGroup>
              </>
            )}
            {item.type === 'BARCODE' && (
              <>
                <InputGroup className="mb-3" readOnly={item.readOnly}
                  style={{ backgroundColor: item.readOnly ? item.color : '' }} >
                  <Form.Control
                    aria-label="Search"
                    aria-describedby="search-icon"
                    readOnly={item.readonly}
                    style={{ backgroundColor: item.readonly ? item.color : '' }}
                  />
                  <InputGroup.Text id="search-icon" readOnly={item.readOnly}
                    style={{ backgroundColor: item.readOnly ? item.color : '' }}>
                    <FontAwesomeIcon icon={faBarcode} />
                  </InputGroup.Text>
                </InputGroup>
              </>
            )}
            {item.type === 'LOOKUPANDBARCODE' && (
              <>
                <InputGroup className="mb-3" readOnly={item.readOnly}
                  style={{ backgroundColor: item.readOnly ? item.color : '' }} >
                  <Form.Control
                    aria-label="Search"
                    aria-describedby="search-icon"
                    readOnly={item.readonly}
                    style={{ backgroundColor: item.readonly ? item.color : '' }}
                  />
                  <InputGroup.Text id="search-icon" readOnly={item.readOnly}
                    style={{ backgroundColor: item.readOnly ? item.color : '' }}>
                    <FontAwesomeIcon icon={faSearch} />
                  </InputGroup.Text>
                  <InputGroup.Text id="search-icon" readOnly={item.readOnly}
                    style={{ backgroundColor: item.readOnly ? item.color : '' }}>
                    <FontAwesomeIcon icon={faBarcode} />
                  </InputGroup.Text>
                </InputGroup>
              </>
            )}
          </div>
        </Draggable>
      ))}
    </div>
  );
};

export default DropTargetPanel;

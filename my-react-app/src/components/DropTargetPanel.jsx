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
      coordinates: { x, y },
    };
 
    setDroppedItems([...droppedItems, newItem]);
    setShowLabelIdOptions([...showLabelIdOptions, true]);
  };
 
  const handleMakeReadOnly = (index) => {
    const confirmed = window.confirm('Do you want this field Readonly?');
    if (confirmed) {
      const updatedItems = [...droppedItems];
      updatedItems[index] = {
        ...updatedItems[index],
        readonly: true,
        color: '#f2f2f2',
      };
      setDroppedItems(updatedItems);
    }
    setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
  };
 
 
 
 
  const handleContextMenu = (e, index) => {
    e.preventDefault();
    const showAddDropdownOption = droppedItems[index]?.type === 'DROPDOWN';
    setContextMenu({ visible: true, index, x: e.clientX, y: e.clientY, showAddDropdownOption });
  };
 
  const handleMouseEnter = (index) => {
    const currentItem = droppedItems[index];
    onHover(currentItem.id, currentItem.label);
  };
 
  const handleAddDropdownOption = (index) => {
    const heading = droppedItems[index].text || 'Heading';
    const updatedItems = [...droppedItems];
    const dropdownOptions = updatedItems[index].options || [];
    const groupIndex = dropdownOptions.findIndex((group) => group.heading === heading);
 
    const optionText = prompt(`Enter an option for "${heading}" dropdown:`);
 
    if (groupIndex !== -1) {
      updatedItems[index].options[groupIndex].options.push({ text: optionText });
    } else {
      updatedItems[index].options.push({ heading, options: [{ text: optionText }] });
    }
 
    setDroppedItems(updatedItems);
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
      updatedItems[index][value === 'addLabel' ? 'label' : 'id'] = inputValue;
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
 
  const panelStyle = {
    border: '5px ',
    padding: '16px',
    marginTop: '10px',
    position: 'relative',
  };
 
  return (
    <div ref={drop} className="col-lg-3 " style={panelStyle}>
      <CustomContextMenu
        visible={contextMenu.visible}
        x={contextMenu.x}
        y={contextMenu.y}
        options={[
          { label: 'Add Label', value: 'addLabel', className: "option" },
          { label: 'Add ID', value: 'addID' },
          { label: 'Make ReadOnly', value: 'makeReadOnly' },
          { label: 'Add Astrik Mark', value: 'addAstrikMark' },
          ...(contextMenu.showAddDropdownOption ? [{ label: 'Add Dropdown Option', value: 'addDropdownOption' }] : []),
          { label: 'Delete', value: 'delete' },
        ]}
        onSelect={(value) => {
          if (value === 'addLabel' || value === 'addID') {
            handleAddLabelId(contextMenu.index, value);
          } else if (value === 'addDropdownOption') {
            handleAddDropdownOption(contextMenu.index);
          } else if (value === 'delete') {
            handleDeleteItem(contextMenu.index);
          } else if (value === 'addAstrikMark') {
            handleAddAstrikMark(contextMenu.index);
          } else if (value === 'makeReadOnly') {
            handleMakeReadOnly(contextMenu.index);
          }
        }}
      />
      {droppedItems.map((item, index) => (
        <Draggable key={item.id}>
          <div
            style={{ marginBottom: '8px', position: 'relative' }}
            onContextMenu={(e) => handleContextMenu(e, index)}
            onMouseEnter={() => handleMouseEnter(index)}
          >
            {showLabelIdOptions[index] && item.label && (
              <div style={{ marginBottom: '4px' }}>
                {item.mandatory && <span style={{ color: 'red' }}>*</span>}
                {item.label}
              </div>
            )}
            {item.type === 'BUTTON' && <button>{item.text}</button>}
            {item.type === 'TEXTBOX' && (
              <input type="text" placeholder={item.text} readOnly={item.readonly}
                style={{ backgroundColor: item.readonly ? item.color : '' }}
              />
            )}
            {item.type === 'RADIO' && <input type="radio" name="radioGroup" />}
            {item.type === 'CHECKBOX' && <input type="checkbox" />}
            {item.type === 'DROPDOWN' && (
              <select className="form-select">
                {item.options?.map((group, i) => (
                  <optgroup key={i} label={group.heading}>
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
                <input type="lookup" placeholder="Search.." readOnly={item.readonly} />
                <FontAwesomeIcon icon="search" />
              </>
            )}
            {showLabelIdOptions[index] && item.id && (
              <div className="panel-3-id" style={{ display: 'none', position: 'absolute', top: '0', right: '0', background: '#eee', padding: '4px' }}>
                ID: {item.id}
              </div>
            )}
          </div>
        </Draggable>
      ))}
    </div>
  );
};
 
export default DropTargetPanel;
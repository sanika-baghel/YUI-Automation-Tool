import React, { useState, useEffect } from 'react';
import { useDrop } from 'react-dnd';
import Draggable from 'react-draggable';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Form, InputGroup } from 'react-bootstrap';
import { faBarcode, faCalendarDays, faSearch } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import { Modal, Button } from 'react-bootstrap';
import { Form as BootstrapForm, Row, Col } from 'react-bootstrap';

const CustomContextMenu = ({ visible, x, y, options, onSelect, readOnly, editable, onAddAllDetails }) => {
  let filteredOptions = options;
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({ id: '', class: '', value: '' });

  const handleShowModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleAddAllDetails = () => {
    onAddAllDetails(formData);
    setFormData({ id: '', class: '', value: '' });
    handleCloseModal();
  };

  const handleCheckboxMandatory = (e) => {
    const { checked } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      isMandatory: checked,
    }));
  };

  const handleCheckboxChange = (e) => {
    const { checked } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      readOnly: checked,
    }));
  };



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
      {/* Existing options */}
      {filteredOptions.map((option) => (
        <div
          key={option.label}
          onClick={() => onSelect(option.value)}
          className="context-menu-option"
        >
          {option.label}
        </div>
      ))}

      <div
        onClick={() => handleShowModal()}
        className="context-menu-option"
      >
        Add All Details
      </div>


      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>Add Details</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Row>
              <Col md={5}>
                <Form.Group controlId="formId">
                  <Form.Label>Field ID</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter ID"
                    name="id"
                    value={formData.id}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group controlId="formClass">
                  <Form.Label>Class</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Class"
                    name="class"
                    value={formData.class}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group controlId="formTemplateName">
                  <Form.Label>Template Name</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Template Name"
                    name="TemplateName"
                    value={formData.templateName}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group controlId="formlabel">
                  <Form.Label>Field Label</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Label"
                    name="label"
                    value={formData.label}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group controlId="value">
                  <Form.Label>Value</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Value"
                    name="value"
                    value={formData.value}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group controlId="formCName">
                  <Form.Label>Collapse Name</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Collapse Name"
                    name="CName"
                    value={formData.CName}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group controlId="formCID">
                  <Form.Label>Collapse ID</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Collapse ID"
                    name="CID"
                    value={formData.CID}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group controlId="formFName">
                  <Form.Label>Input Field Name</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter Field"
                    name="FName"
                    value={formData.FName}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Group controlId="formMaxlen">
                  <Form.Label>Max Length</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter length"
                    name="Maxlen"
                    value={formData.maxLen}
                    onChange={handleFormChange}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Label></Form.Label>
                <Form.Group controlId="formIsMandatory">
                  <Form.Check
                    type="checkbox"
                    label="isMandatory?"
                    name="isMandatory"
                    id="isMandatoryCheckbox"
                    onChange={handleCheckboxMandatory}
                  />
                </Form.Group>
              </Col>
              <Col md={5}>
                <Form.Label></Form.Label>
                <Form.Group controlId="formReadOnly">
                  <Form.Check
                    type="checkbox"
                    label="ReadOnly"
                    name="readOnly"
                    id="readOnlyCheckbox"
                    onChange={handleCheckboxChange}
                  />
                </Form.Group>
              </Col>
            </Row>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={handleAddAllDetails}>
            Save
          </Button>
          <Button variant="secondary" onClick={handleCloseModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>

  );
};

const DropTargetPanel = ({ onHover, droppedItems, setDroppedItems, editedLabel, editedClass, editedValue }) => {
  const [contextMenu, setContextMenu] = useState({ visible: false, index: -1, x: 0, y: 0 });
  const [showLabelIdOptions, setShowLabelIdOptions] = useState(Array(droppedItems.length).fill(true));

  {/* New Added Code for add row and header*/}
  let headerCount = 1;
  let numberOfColumns = 0;
  const [inputTypes, setInputTypes] = useState(Array.from({ length: 4 }, () => ''));

  // Handler function to update the input type for a specific select element
  const handleInputChange = (index, value) => {
    setInputTypes(prevInputTypes => {
      const newInputTypes = [...prevInputTypes];
      newInputTypes[index] = value;
      return newInputTypes;
    });
  };
  const [rowCount, setRowCount] = useState(1); // Initial row count is 1

  // Function to handle adding a row
  const handleAddRow = () => {
    setRowCount(rowCount + 1); // Increment row count by 1
  };

  const handleDeleteRow = () => {
    setRowCount(rowCount - 1); // Increment row count by 1
  };
  
  const handleHeaderInput = () => {
    const headerCount = parseInt(prompt('Enter the number of table headers (th):'));
    if (!isNaN(headerCount)) {
      setInputTypes(Array.from({ length: headerCount }, () => ''));
    }
  };
 {/* New Added Code for add row and header*/}

  const [, drop] = useDrop({
    accept: ['BUTTON', 'TEXTBOX', 'RADIO', 'CHECKBOX', 'DROPDOWN', 'LOOKUP', 'TEXTAREA', 'CALENDAR', 'BARCODE', 'LOOKUPANDBARCODE', 'ATTACHMENT', 'HEADER', 'FOOTER', 'ADDROWHEADER', 'OLDADDROWS', 'ADDROWS'],
    drop: (item, monitor) => handleDrop(item, monitor),
  });

  const handleClickOutside = (e) => {
    if (contextMenu.visible && !e.target.closest('.custom-context-menu')) {
      setContextMenu({ visible: false, index: -1, x: 0, y: 0 });
    }
  };

  useEffect(() => {
    const updatedItems = droppedItems.map((item) => {
      if (item.id === contextMenu.index) {
        return {
          ...item,
          label: editedLabel,
          class: editedClass,
          value: editedValue,
        };
      }
      return item;
    });
    setDroppedItems(updatedItems);
    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, [contextMenu, editedLabel, editedClass, editedValue]);


  const handleDrop = (item, monitor) => {
    const offset = monitor.getSourceClientOffset();
    let newItem;

    if (item.type === 'RADIO' || item.type === 'CHECKBOX') {
      const count = prompt('Enter the number of radio buttons to create:');
      if (count && !isNaN(count)) {
        const radioButtons = [];
        for (let i = 0; i < parseInt(count); i++) {
          // Assign default label or empty label to the radio button
          const label = ''; // You can change this to assign default label if needed
          const radioButton = {
            type: item.type,
            id: droppedItems.length + i + 1,
            value: '',
            text: label,
            class: '',
            label: '',
            readOnly: false,
            options: [],
            mandatory: false,
          };
          radioButtons.push(radioButton);
        }

        setDroppedItems([...droppedItems, ...radioButtons]);
        setShowLabelIdOptions([...showLabelIdOptions, ...Array(parseInt(count)).fill(true)]);
      }
    }
    else if (item.type === 'ADDROWHEADER') {
      {/* New Added Code for add row and header*/}
      const count = prompt('Enter the number header:');
      headerCount = count;
      if (count && !isNaN(count)) {
        const headers = [];
        let headerGroup = {
          type: item.type,
          id: '',
          value: '',
          text: '',
          class: '',
          label: '',
          readOnly: false,
          options: [],
          mandatory: false,
        };
        for (let i = 0; i < parseInt(count); i++) {
          // Assign default label or empty label to the radio button
          //const label = ''; // You can change this to assign default label if needed

          const headerText = prompt(`Enter header text ${i + 1}:`);
          if (headerText !== null) {
            headers.push({
              data_ref: '',
              width: '',
              class: '',
              label: headerText,
            })
          }
        }
        headerGroup.value = headers;

        setDroppedItems([...droppedItems, headerGroup]);
        setShowLabelIdOptions([...showLabelIdOptions, ...Array(parseInt(count)).fill(true)]);

      }

    }

    else {
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

  const handleAddAllDetails = (formData) => {
    const updatedItems = droppedItems.map((item) => {
      if (item.id === contextMenu.index) {
        return {
          ...item,
          ...formData,
        };
      }
      return item;
    });
    setDroppedItems(updatedItems);
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
    setDroppedItems(updatedItems, () => {
      const hoveredItem = updatedItems[index]; // Use the updatedItems array
      onHover(
        hoveredItem.id,
        hoveredItem.label,
        hoveredItem.class,
        false // Indicate that the item is no longer read-only
      );
    });
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
      // Check if the entered label already exists
      const isDuplicateLabel = updatedItems.some(item => item.label === inputValue);
      if (!isDuplicateLabel) {
        if (value === 'addClass') {
          updatedItems[index]['class'] = inputValue;
        } else if (value === 'addValue') {
          updatedItems[index]['value'] = inputValue;
        } else {
          updatedItems[index][value === 'addLabel' ? 'label' : 'id'] = inputValue;
        }
        setDroppedItems(updatedItems);
        setContextMenu({ visible: false, index: -1, x: 0, y: 0, showAddDropdownOption: false });
      } else {
        alert(`Label "${inputValue}" already exists. Please enter a different label.`);
      }
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

  const [showModal, setShowModal] = useState(false);

  const handleShowModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

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
          } else if (value === 'deleteLabel') {
            handleDeleteLabel(contextMenu.index);
          } else if (value === 'onAddAllDetails') {
            handleAddAllDetails(contextMenu.index);
          }
        }}
        readOnly={droppedItems[contextMenu.index]?.readOnly}
        editable={!droppedItems[contextMenu.index]?.readOnly}
        onAddAllDetails={() => handleAddAllDetails()}
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
            {item.type === 'RADIO' && <input type="radio" name="radioGroup" />}
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
            {item.type === 'CALENDAR' && (
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
            {/* New Added Code for add row and header*/}
            {item.type === 'ADDROWHEADER' && (
              <>
                <div
                  onDrop={handleDrop}
                  onDragOver={(e) => e.preventDefault()}
                  style={{ width: '450%', height: '10%' }}
                >
                  {item.value.length > 0 && (
                    <table className="table">
                      <thead>
                        <tr>
                          {/* Mapping over the dropped headers */}
                          {item.value.map((header, index) => (
                            <th key={index} style={{ border: '1px solid black' }}>{header.label}</th>
                          ))}
                        </tr>
                      </thead>
                    </table>
                  )}
                </div>

              </>
            )}
            {/* New Added Code for add row and header*/}
            {item.type === 'OLDADDROWS' && (
              <div style={{ width: '450%', height: '10%' }}>
                <table className="table">
                  <thead>
                    
                    <tr>
                      {inputTypes.map((inputType, index) => (
                        <th key={index}>
                          <div>
                            <select onChange={(e) => handleInputChange(index, e.target.value)} value={inputTypes[index]}>
                              <option value=""></option>
                              <option value="inputGroup">Lookup</option>
                              <option value="inputType">TextInput</option>
                            </select>
                          </div>
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {[...Array(rowCount)].map((_, rowIndex) => ( // Map over rows using rowCount state
                      <tr key={rowIndex}>
                        {inputTypes.map((inputType, index) => (
                          <td key={index}>
                            {inputType === 'inputGroup' && (
                              <InputGroup className="mb-3" readOnly={item.readOnly} style={{ backgroundColor: item.readOnly ? item.color : '' }}>
                                <Form.Control
                                  placeholder="Search..."
                                  aria-label="Search"
                                  aria-describedby="search-icon"
                                  readOnly={item.readOnly}
                                  style={{ backgroundColor: item.readOnly ? item.color : '' }}
                                />
                                <InputGroup.Text id="search-icon" readOnly={item.readOnly} style={{ backgroundColor: item.readOnly ? item.color : '' }}>
                                  <FontAwesomeIcon icon={faSearch} />
                                </InputGroup.Text>
                              </InputGroup>
                            )}
                            {inputType === 'inputType' && (
                              <input
                                type="text"
                                placeholder={item.text}
                                readOnly={item.readOnly}
                                style={{ backgroundColor: item.readOnly ? item.color : '' }}
                              />
                            )}
                          </td>
                        ))}
                      </tr>
                    ))}
                  </tbody>
                </table>
                <button onClick={handleAddRow} style={{
                  padding: '5px 10px',
                  marginRight: '10px', // Add margin-right for space
                  backgroundColor: '#007bff',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer'
                }}>+</button>
                <button onClick={handleDeleteRow} style={{
                  padding: '5px 10px',
                  backgroundColor: '#007bff',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer'
                }}>-</button>
              </div>
            )}
            {/* New Added Code for add row and header*/}
          </div>
        </Draggable>
      ))}
    </div>
  );
};

export default DropTargetPanel;

import React, { useState, useRef } from 'react';
import DraggableItem from './components/DraggableItem';
import DropTargetPanel from './components/DropTargetPanel';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { Dropdown } from 'react-bootstrap';
import logoImage from './Prorigologo.png';
import axios from 'axios';


const App = () => {
  const [hoveredItem, setHoveredItem] = useState({
    id: null,
    label: null,
    class: null,
    value: null,
    readOnly: false,
    mandatory: false,
    tempname: null,
    cid: null,
    cname:null,
    fname: null,
    maxLen: null,
    addrowheaderkey: null,
    addrowkey: null,

  });

  const [droppedItems, setDroppedItems] = useState([]);
  const [fileDropdownVisible, setFileDropdownVisible] = useState(false);
  // const [editedLabel, setEditedLabel] = useState('');
  // const [editedClass, setEditedClass] = useState('');
  // const [editedValue, setEditedValue] = useState('');
  const [fileName, setFileName] = useState('');
  const [propertiesCollapsed, setPropertiesCollapsed] = useState(false);

  const handleHover = (itemId, itemLabel, itemClass, itemReadOnly, itemMandatory, itemValue, itemCid,itemCname, itemFName, itemMaxLen, itemAddrowheaderkey, itemAddrowkey) => {
    setHoveredItem({
      id: itemId,
      label: itemLabel,
      class: itemClass,
      value: itemValue,
      readOnly: itemReadOnly,
      mandatory: itemMandatory,
      cid: itemCid,
      cname: itemCname,
      fname: itemFName,
      maxLen: itemMaxLen,
      addrowheaderkey: itemAddrowheaderkey,
      addrowkey: itemAddrowkey,
    });
  };

  const downloadJsonFile = () => {
    const confirmation = window.confirm("Are you sure you want to download the file?");
    if (confirmation) {
      const jsonContent = JSON.stringify(droppedItems, null, 2);
      const blob = new Blob([jsonContent], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'dropped_items.json';
      a.click();
      alert("Download successful! ");
    }
  };

  const handleFileUpload = async (e) => {
    const file = e.target.files[0];

    if (file) {
      try {
        const fileContent = await readFileContent(file);
        const parsedJson = JSON.parse(fileContent);
        setDroppedItems(parsedJson);
        alert("File uploaded successfully!");
      } catch (error) {
        console.error("Error reading or parsing JSON file:", error);
        alert("Error reading or parsing JSON file. Please check the file format.");
      }
    }
  };

  const readFileContent = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = (e) => {
        resolve(e.target.result);
      };

      reader.onerror = (error) => {
        reject(error);
      };

      reader.readAsText(file);
    });
  };

  const handleToolSelect = (toolType) => {
    if (toolType === 'Exit') {
      window.close();
    }
    setFileDropdownVisible(false); // Close dropdown after selecting an option
  };

  const downloadYUITemplate = async (e) => {
    const confirmation = window.confirm("Are you sure you want to download the file?");
    if (confirmation) {
      const fileName = prompt("Enter file name:", ".template");

      if (!fileName) {
        return;
      }
      const jsonContent = JSON.stringify(droppedItems, null, 2);
      const blob = new Blob([jsonContent], { type: 'application/json' });
      // const url = URL.createObjectURL(blob);
      e.preventDefault();
      try {
        const formData = new FormData();
        formData.append('jsonFile', blob, 'dropped_items.json'); // 'file' should match the key name expected by the server
        const response = await axios.post('http://localhost:8080/convert/jsonToTemplate', formData, {
          headers: {
            'Content-Type': 'multipart/form-data' // Don't forget to set the content type
          }
        });

        const file = new Blob(
          [response.data],
          { type: response.headers['content-type'] }
        );
        const fileURL = URL.createObjectURL(file);
        const link = document.createElement('a');
        link.href = fileURL;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();

        link.parentNode.removeChild(link);
        URL.revokeObjectURL(fileURL);
        alert("Download successful! ");
      } catch (error) {
        console.error("Error during file download", error);
      }
    }
  };

  // const handleLabelChange = (newLabel) => {
  //   const updatedDroppedItems = [...droppedItems];
  //   const hoveredItemIndex = updatedDroppedItems.findIndex(item => item.id === hoveredItem.id);
  //   if (hoveredItemIndex !== -1) {
  //     updatedDroppedItems[hoveredItemIndex].label = newLabel;
  //     setDroppedItems(updatedDroppedItems);
  //     setEditedLabel(newLabel);
  //     updateHoveredItem();
  //   }
  // };

  // const handleClassChange = (newClass) => {
  //   const updatedDroppedItems = [...droppedItems];
  //   const hoveredItemIndex = updatedDroppedItems.findIndex(item => item.id === hoveredItem.id);
  //   if (hoveredItemIndex !== -1) {
  //     updatedDroppedItems[hoveredItemIndex].class = newClass;
  //     setDroppedItems(updatedDroppedItems);
  //     setEditedClass(newClass);
  //     updateHoveredItem();
  //   }
  // };

  // const handleValueChange = (newValue) => {
  //   const updatedDroppedItems = [...droppedItems];
  //   const hoveredItemIndex = updatedDroppedItems.findIndex(item => item.id === hoveredItem.id);
  //   if (hoveredItemIndex !== -1) {
  //     updatedDroppedItems[hoveredItemIndex].value = newValue;
  //     setDroppedItems(updatedDroppedItems);
  //     setEditedValue(newValue);
  //     updateHoveredItem();
  //   }
  // };

  // const updateHoveredItem = () => {
  //   setHoveredItem((prevItem) => ({
  //     ...prevItem,
  //     label: editedLabel,
  //     class: editedClass,
  //     value: editedValue,
  //     cname: editedCname,
  //     cid  : editedcid, 

  //   }));
  // };
  const fileInputRef = useRef(null);

  const openFileInput = () => {
    fileInputRef.current.click();
  };

  const handleReadOnlyChange = (e) => {
    setHoveredItem({
      ...hoveredItem,
      readOnly: e.target.value === 'true' ? true : false,
    });
  };


  const handleRadioChange = (e) => {
    setHoveredItem({
      ...hoveredItem,
      mandatory: e.target.value === 'true' ? true : false,
    });
  };


  return (
    <div className="container-fluid">

      {/* <NavigationBar /> Include the NavigationBar component here */}
      <nav className="navbar navbar-expand-lg navbar-light" style={{ backgroundColor: '#0fb6c9dc', height: '50px' }}>
        <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav mr-auto">
            <li className="nav-item">
              <img src={logoImage} alt="Logo" style={{ height: '40px', marginRight: '10px' }} /> {/* Add your image here */}
            </li>
            <li className="nav-item dropdown">
              <a
                className="nav-link dropdown-toggle"
                href="#!"
                id="fileDropdown"
                role="button"
                onClick={() => setFileDropdownVisible(!fileDropdownVisible)}
                style={navLinkStyle}
              >
                File
              </a>
              <Dropdown.Menu show={fileDropdownVisible} style={{ backgroundColor: '#bfdbdfdc' }}>
                <Dropdown.Item onClick={() => handleToolSelect('NewFile')}>File</Dropdown.Item>
                <Dropdown.Item onClick={() => handleToolSelect('Save')}>Save</Dropdown.Item>
                <Dropdown.Item onClick={() => handleToolSelect('Exit')}>Exit</Dropdown.Item>

              </Dropdown.Menu>
            </li>
            <li className="nav-item active">
              <a className="nav-link" href="#" style={navLinkStyle}>Download Protrak Screen</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" onClick={downloadYUITemplate} style={navLinkStyle}>Download YUI Screen</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" onClick={downloadJsonFile} style={navLinkStyle}>Download JSON</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" onClick={openFileInput} style={navLinkStyle}>JSON to UI</a>
              <input type="file" ref={fileInputRef} className="d-none" onChange={handleFileUpload} />
            </li>
          </ul>
        </div>
      </nav>
      <div className="row">
        <div className="col-md-2 sidebar">
        <h8 style={{ color: 'black' }}><b>Tool Box</b></h8>
          <div style={{ overflowY: 'auto', maxHeight: '468px' }}>
            <DraggableItem type="COLLAPSE" text="Collapse" />
            <DraggableItem type="BUTTON" text="Button" />
            <DraggableItem type="TEXTBOX" text="Textbox" />
            <DraggableItem type="TEXTAREA" text="Textarea" />
            <DraggableItem type="RADIO" text="Radio Button" />
            <DraggableItem type="CHECKBOX" text="Checkbox" />
            <DraggableItem type="DROPDOWN" text="Catalog" />
            <DraggableItem type="LOOKUP" text="Lookup" />
            <DraggableItem type="CALENDAR" text="Calendar" />
            <DraggableItem type="BARCODE" text="Barcode" />
            <DraggableItem type="LOOKUPANDBARCODE" text="Lookup & Barcode" />
            <DraggableItem type="ATTACHMENT" text="Attachment" />
            <DraggableItem type="HEADER" text="Header" />
            <DraggableItem type="FOOTER" text="Footer" />
            <DraggableItem type="ADDROWHEADER" text="Add Table Header" />
            <DraggableItem type="ADDROWS" text="Add Rows" />
          
             {/* New Added Code for add row and header*/}
          </div>
          {/* <div className="bottom left p-3" style={{ position: 'fixed', bottom: '0', left: '0', padding: '15px' }}>
            <button onClick={downloadJsonFile} className="btn btn-light" style={{ color: 'black' }}>
              Download JSON
            </button>
          </div> */}
        </div>

        <div className="col-md-7 code-editor">
          <div style={{ overflowY: 'auto', maxHeight: '620px' }}>
            <h8 style={{ color: 'black' }}>Drop Target Panel</h8>
            <DropTargetPanel droppedItems={droppedItems} setDroppedItems={setDroppedItems} onHover={handleHover} />
          </div>
        </div>


        <div className="col-md-3 output-window">
          <h8 style={{ color: 'black' }}><b>Properties</b></h8><br /><br />

          {/* <button
            className="btn btn-primary"
            type="button"
            onClick={() => setPropertiesCollapsed(!propertiesCollapsed)}
          >
            Properties
          </button> <br></br> */}

          {/* <div className={`collapse ${propertiesCollapsed ? 'show' : ''}`} id="propertiesPanel"> */}
          <div className="table-responsive">
            <table className="table table-bordered">
              <tr>
                <td>Template Name</td>
                <td>
                  <input type="text" value={hoveredItem.tempname} />
                </td>
              </tr>
              <tr>
                <td>Field ID</td>
                <td>
                  <input type="text" value={hoveredItem.id} editable />
                </td>
              </tr>
              <tr>
                <td>Field Label</td>
                <td>
                  <input type="text" value={hoveredItem.label}
                    editable />
                </td>
              </tr>
              <tr>
                <td>Is Mandatory</td>
                <td>
                  <input
                    type="radio"
                    id="trueRadio"
                    name="isMandatory"
                    value="true"
                    checked={hoveredItem.mandatory === true}
                    onChange={handleRadioChange}
                  />
                  <label htmlFor="trueRadio">True</label>

                  <input
                    type="radio"
                    id="falseRadio"
                    name="isMandatory"
                    value="false"
                    checked={hoveredItem.mandatory === false}
                    onChange={handleRadioChange}
                  />
                  <label htmlFor="falseRadio">False</label>
                </td>
              </tr>
              <tr>
                <td>Field Class</td>
                <td>
                  <input type="text" value={hoveredItem.class} />
                </td>
              </tr>
              <tr>
                <td>Field Value</td>
                <td>
                  <input type="text" value={hoveredItem.value} />
                </td>
              </tr>
              <tr>
                <td>Is Read Only</td>
                <td>
                  <input
                    type="radio"
                    id="readOnlyTrue"
                    name="isReadOnly"
                    value="true"
                    checked={hoveredItem.readOnly === true}
                    onChange={handleReadOnlyChange}
                  />
                  <label htmlFor="readOnlyTrue">True</label>

                  <input
                    type="radio"
                    id="readOnlyFalse"
                    name="isReadOnly"
                    value="false"
                    checked={hoveredItem.readOnly === false}
                    onChange={handleReadOnlyChange}
                  />
                  <label htmlFor="readOnlyFalse">False</label>
                </td>
              </tr>
              <tr>
                <td>Collapse Name</td>
                <td>
                  <input type="text" value={hoveredItem.cname} />
                </td>
              </tr>
              <tr>
                <td>Collapse ID</td>
                <td>
                  <input type="text" value={hoveredItem.cid} />
                </td>
              </tr>
              <tr>
                <td>Input Field Name</td>
                <td>
                  <input type="text" value={hoveredItem.fname} />
                </td>
              </tr>
              <tr>
                <td>Max Length</td>
                <td>
                  <input type="text" value={hoveredItem.maxLen} />
                </td>
              </tr>

            </table>
          </div>
        </div>
      </div>
    </div>
    // </div>


  );
};


// const TableRow = ({ label, value, editable, onChange }) => (
//   <tr>
//     <td><b>{label}</b></td>
//     <td>
//       {editable ? (
//         <input type="text" value={value} onChange={(e) => onChange(e.target.value)} />
//       ) : (
//         value
//       )}
//     </td>
//   </tr>
// );

const navLinkStyle = {
  color: 'white',
  textDecoration: 'none',
  padding: '14px 16px',
  transition: 'background-color 0.3s',
};

// Add hover effect
navLinkStyle[':hover'] = {
  backgroundColor: '#0fb6c9dc',
};
export default App;
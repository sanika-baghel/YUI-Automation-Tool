// NavigationBar.js
import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { Dropdown } from 'react-bootstrap';
import logoImage from './Prorigologo.png'; // Import your image
import axios from 'axios';
import downloadYUITemplate from './App.js'
import './App.js'


const NavigationBar = ({ onToolSelect }) => {
  const [fileDropdownVisible, setFileDropdownVisible] = useState(false);

  const handleToolSelect = (toolType) => {
    if (toolType === 'Exit') {
      // Close the window when 'Exit' is selected
      window.close();
    } else {
      onToolSelect(toolType);
    }
    setFileDropdownVisible(false); // Close dropdown after selecting an option
  };
  
  const [droppedItems, setDroppedItems] = useState([]);

  // const downloadYUITemplate = async (e)  => {
  //   const confirmation = window.confirm("Are you sure you want to download the file?");
  //       if (confirmation) {
  //       const jsonContent = JSON.stringify(droppedItems, null, 2);
  //       const blob = new Blob([jsonContent], { type: 'application/json' });
  //      // const url = URL.createObjectURL(blob);
  //      e.preventDefault();
  //       try {
  //         const formData = new FormData();
  //         formData.append('jsonFile', blob, 'dropped_items.json'); // 'file' should match the key name expected by the server
  //         const response = await axios.post('http://localhost:8080/convert/jsonToTemplate', formData, {
  //           headers: {
  //             'Content-Type': 'multipart/form-data' // Don't forget to set the content type
  //           }
  //         });

  //           // Create a new Blob object using the response data of the file
  //     const file = new Blob(
  //       [response.data],
  //       { type: response.headers['content-type'] }
  //     );
  //         const fileURL = URL.createObjectURL(file);
  //     const link = document.createElement('a');
  //     link.href = fileURL;
  //     link.setAttribute('download', 'filename.template'); // or any other extension
  //     document.body.appendChild(link);
  //     link.click();
 
  //     // Clean up and remove the link
  //     link.parentNode.removeChild(link);
  //     URL.revokeObjectURL(fileURL); // Free up memory by releasing the object URL
  //   } catch (error) {
  //     console.error("Error during file download", error);
  //   }
  //      }
  //     };

  return (
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
              <Dropdown.Item onClick={() => handleToolSelect('NewFile')}>New File</Dropdown.Item>
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
        </ul>
      </div>
    </nav>
  );
};

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

export default NavigationBar;

// NavigationBar.js
import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { Dropdown } from 'react-bootstrap';
import logoImage from './Prorigologo.png'; // Import your image

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
            <a className="nav-link" href="#" style={navLinkStyle}>Download YUI Screen</a>
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

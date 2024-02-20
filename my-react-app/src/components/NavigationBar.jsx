// NavigationBar.js
import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { Dropdown } from 'react-bootstrap';

const NavigationBar = ({ onToolSelect }) => {
  const [fileDropdownVisible, setFileDropdownVisible] = useState(false);

  const handleToolSelect = (toolType) => {
    onToolSelect(toolType);
    setFileDropdownVisible(false); // Close dropdown after selecting an option
  };

  return (
    <nav class="navbar navbar-expand-lg navbar-light" style={{ backgroundColor: '#575555', height: '50px' }}>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
    <li class="nav-item dropdown">
  
  <a
    className="nav-link dropdown-toggle"
    href="#!"
    id="fileDropdown"
    role="button"
    onClick={() => setFileDropdownVisible(!fileDropdownVisible)}
    style={navLinkStyle }
  >
    File
  </a>
  <Dropdown.Menu show={fileDropdownVisible} style={{ backgroundColor: '#c2bfbf' }}>
    <Dropdown.Item onClick={() => handleToolSelect('NewFile')}>New File</Dropdown.Item>
    <Dropdown.Item onClick={() => handleToolSelect('Save')}>Save</Dropdown.Item>
    <Dropdown.Item onClick={() => handleToolSelect('Exit')}>Exit</Dropdown.Item>
  </Dropdown.Menu>

</li>
      <li class="nav-item active">
        <a class="nav-link" href="#" style={navLinkStyle}>Download Protrak Screen</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#" style={navLinkStyle }>Download YUI Screen</a>
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
  backgroundColor: 'rgb(170, 165, 165)',
};
export default NavigationBar;

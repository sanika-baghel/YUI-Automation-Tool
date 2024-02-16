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
    <nav className="navbar navbar-dark custom-navbar" style={{ backgroundColor: '#575555' }}>
      <div className="nav-item dropdown">
        <a
          className="nav-link dropdown-toggle"
          href="#!"
          id="fileDropdown"
          role="button"
          onClick={() => setFileDropdownVisible(!fileDropdownVisible)}
          style={{ color: 'white' }}
        >
          File
        </a>
        <Dropdown.Menu show={fileDropdownVisible} style={{ backgroundColor: '#969595' }}>
          <Dropdown.Item onClick={() => handleToolSelect('NewFile')}>New File</Dropdown.Item>
          <Dropdown.Item onClick={() => handleToolSelect('Save')}>Save</Dropdown.Item>
          <Dropdown.Item onClick={() => handleToolSelect('DownloadTemplate')}>Download Template</Dropdown.Item>
          <Dropdown.Item onClick={() => handleToolSelect('Exit')}>Exit</Dropdown.Item>
        </Dropdown.Menu>
      </div>
      <span className="navbar-brand-sm text-white" style={{ marginTop: '-5px' }}>YUI-AUTOMATION TOOL</span>
    </nav>
  );
};

export default NavigationBar;

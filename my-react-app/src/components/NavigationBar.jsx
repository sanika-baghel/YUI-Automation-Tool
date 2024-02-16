// NavigationBar.js
import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const NavigationBar = ({ onToolSelect }) => {
  const handleToolSelect = (toolType) => {
    onToolSelect(toolType);
  };

  return (
    <nav className="navbar navbar-dark custom-navbar" style={{ backgroundColor:'#575555' }}>
      <div className="btn-group text-white">
        {/* <a href="#!" onClick={() => handleToolSelect('File')} style={{ color: 'white' }}>File</a>
        <a href="#!" onClick={() => handleToolSelect('Edit')} style={{ color: 'white' }}>Edit</a> */}
      </div>
      <span className="navbar-brand-sm text-white" style={{ marginTop: '-5px' }}>YUI-AUTOMATION TOOL</span>
    </nav>
  );
};

export default NavigationBar;

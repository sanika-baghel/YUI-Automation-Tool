import React, { useState, useRef } from "react";
import logoImage from './Prorigologo.png';
import { Dropdown } from "react-bootstrap";
import './App.css';
import SingleTabContent from './SingleTabContent';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

const App = () => {
  const [fileDropdownVisible, setFileDropdownVisible] = useState(false);
  const [showSingleTabContent, setShowSingleTabContent] = useState(false);

  const handleToolSelect = (toolType) => {
    if (toolType === "Exit") {
      window.close();
    }
    setFileDropdownVisible(false); // Close dropdown after selecting an option
    
    if (toolType === "Single Tab") {
      setShowSingleTabContent(true);
    } else {
      setShowSingleTabContent(false);
    }
  };
 
  return (
    <div style={{ backgroundColor: "#bfdbdfdc", minHeight: "100vh" }}>
      {showSingleTabContent ? (
        <SingleTabContent />
      ) : (
        <nav
          className="navbar navbar-expand-lg navbar-light"
          style={{ backgroundColor: "#0fb6c9dc", height: "50px" }}
        >
          <div className="collapse navbar-collapse">
            <ul className="nav navbar-nav ml-auto">
              <li className="nav-item">
                {/* Add your image here */}
                <img
                  src={logoImage}
                  alt="Logo"
                  style={{ height: "40px", marginRight: "10px" }}
                />{" "}
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
                  New Screen
                </a>
                <Dropdown.Menu
                  show={fileDropdownVisible}
                  style={{ backgroundColor: "#bfdbdfdc" }}
                >
                  <Dropdown.Item onClick={() => handleToolSelect("Single Tab")}>
                    Single Tab
                  </Dropdown.Item>

                  <Dropdown.Item onClick={() => handleToolSelect("Multiple Tab")}>
                    Multiple Tab
                  </Dropdown.Item>
                </Dropdown.Menu>

                <Dropdown.Menu
                  show={fileDropdownVisible}
                  style={{ backgroundColor: "#bfdbdfdc" }}
                >
                  <Dropdown.Item
                    onClick={() => {
                      const confirmation = window.confirm(
                        "Are you sure you want to download the Single Tab file?"
                      );
                      if (confirmation) {
                        handleToolSelect("Single Tab");
                      }
                    }}
                  >
                    Single Tab
                  </Dropdown.Item>
                  <Dropdown.Item
                    onClick={() => {
                      const confirmation = window.confirm(
                        "Are you sure you want to download the Multiple Tab file?"
                      );
                      if (confirmation) {
                        handleToolSelect("Multiple Tab");
                      }
                    }}
                  >
                    Multiple Tab
                  </Dropdown.Item>
                </Dropdown.Menu>
              </li>

              <li className="navbar-right">
                <b>
                  <a href="#">Open</a>
                </b>
              </li>
              <li className="navbar-right">
                <b>
                  <a href="#">Logout</a>
                </b>
              </li>
            </ul>
          </div>
        </nav>
      )}

      {!showSingleTabContent && (
        <div className="center-text">
          <div>
            <h1 style={{ color: "blue", fontWeight: "bold", marginTop: "250px" }}>
              YUI Screen Generator
            </h1>
          </div>
        </div>
      )}
    </div>
  );
};
 
const navLinkStyle = {
  color: "white",
  textDecoration: "none",
  padding: "14px 16px",
  transition: "background-color 0.3s",
};
 
// Add hover effect
navLinkStyle[":hover"] = {
  backgroundColor: "#0fb6c9dc",
};
export default App;

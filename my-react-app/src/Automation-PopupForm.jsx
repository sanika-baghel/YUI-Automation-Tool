import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { Form as BootstrapForm, Row, Col } from 'react-bootstrap';

const PopupForm = ({ show, handleClose, numberOfTabs, handleSave }) => {
  const [formData, setFormData] = useState({});

  // Function to handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  // Generate text fields based on number of tabs
  const generateTextFields = () => {
    const textFields = [];
    for (let i = 0; i < numberOfTabs; i++) {
      textFields.push(
        <Row key={i}>
          <Col>
            <BootstrapForm.Label style={{ fontWeight: 'bold' }}>{`Tab ${i + 1} Name:`}</BootstrapForm.Label>
            <BootstrapForm.Control
              type="text"
              name={`tabName${i}`}
              placeholder={`Enter Tab ${i + 1} Name`}
              onChange={handleInputChange}
            />
          </Col>
          <Col>
            <BootstrapForm.Label style={{ fontWeight: 'bold' }}>{`Template ${i + 1} Name:`}</BootstrapForm.Label>
            <BootstrapForm.Control
              type="text"
              name={`templateName${i}`}
              placeholder={`Enter Template ${i + 1} Name`}
              onChange={handleInputChange}
            />
          </Col>
        </Row>
      );
    }
    return textFields;
  };

  // Function to handle save button click
  const handleSaveClick = () => {
    handleSave(formData);
    handleClose();
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Add Tab Name and Template Name</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <BootstrapForm>
          {generateTextFields()}
        </BootstrapForm>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handleSaveClick}>Save</Button>
        <Button variant="secondary" onClick={handleClose}>Close</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default PopupForm;

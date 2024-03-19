import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';

const PopupForm = ({ show, handleClose }) => {
  const [formData, setFormData] = useState({
    tabName: '',
    templateName: ''
  });

  // Function to handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Create Form</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="formtabName">
            <Form.Label>Tab Name</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter TabName"
              name="tabName"
              value={formData.tabName}
              onChange={handleInputChange}
            />
          </Form.Group>
          <Form.Group controlId="formtemplateName">
            <Form.Label>Template Name</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter Template name"
              name="templateName"
              value={formData.templateName}
              onChange={handleInputChange}
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handleClose}>Save</Button>

        <Button variant="secondary" onClick={handleClose}>Close</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default PopupForm;

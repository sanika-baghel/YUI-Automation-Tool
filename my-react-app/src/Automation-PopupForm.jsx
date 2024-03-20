import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { Form as BootstrapForm, Row, Col } from 'react-bootstrap';

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
        <Modal.Title>Add Tab Name and Template Name</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <BootstrapForm>
          <Row>
            <Col>
              <BootstrapForm.Label style={{ fontWeight: 'bold' }}>Tab 1:</BootstrapForm.Label>
            </Col>
            <Col>
              <BootstrapForm.Label style={{ fontWeight: 'bold' }}>Tab Name:</BootstrapForm.Label>
              <BootstrapForm.Control
                type="text"
                name="label"
                placeholder="Enter TabName"
                onChange={handleInputChange}
              />
            </Col>
            <Col>
              <BootstrapForm.Label style={{ fontWeight: 'bold' }}>Template Name:</BootstrapForm.Label>
              <BootstrapForm.Control
                type="text"
                name="id"
                placeholder="Enter TemplateName"
                onChange={handleInputChange}
              />
            </Col>
          </Row>
        </BootstrapForm>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handleClose}>Save</Button>
        <Button variant="secondary" onClick={handleClose}>Close</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default PopupForm;

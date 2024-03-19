import React, { useState } from 'react';
import { Card, Button } from 'react-bootstrap';
import './MultipleCards.css'; 
import Options from './Options.jpeg';
import PopupForm from './PopupForm'; 

const MultipleCards = () => {
  const [showForm, setShowForm] = useState(false); 

  const handleShowForm = () => {
    setShowForm(true);
  };

  const handleCloseForm = () => {
    setShowForm(false);
  };

  return (
    <div className="container center-container"> 
      <div className="row justify-content-center"> 
        <div className="col-md-3">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>2 Tabs</Card.Title>
              <Card.Text>
                For 2 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={handleShowForm}>Create</Button> {/* Open the form */}
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-3">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>3 Tabs</Card.Title>
              <Card.Text>
                For 3 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={handleShowForm}>Create</Button> {/* Open the form */}
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-3">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>4 Tabs</Card.Title>
              <Card.Text>
                For 4 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={handleShowForm}>Create</Button> {/* Open the form */}
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-3">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>5 Tabs</Card.Title>
              <Card.Text>
                For 5 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={handleShowForm}>Create</Button> {/* Open the form */}
            </Card.Body>
          </Card>
        </div>
      </div>
     
      <PopupForm show={showForm} handleClose={handleCloseForm} />
    </div>
  );
};

export default MultipleCards;

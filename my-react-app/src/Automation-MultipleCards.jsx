import React, { useState } from 'react';
import { Card, Button } from 'react-bootstrap';
import './MultipleCards.css'; 
import Options from './Options.jpeg';
import PopupForm from './Automation-PopupForm'; 

const MultipleCards = () => {
  const [showForm, setShowForm] = useState(false);
  const [numberOfTabs, setNumberOfTabs] = useState(0); // State to keep track of the number of tabs clicked

  const handleShowForm = (numTabs) => {
    setShowForm(true);
    setNumberOfTabs(numTabs); 
  };

  const handleCloseForm = () => {
    setShowForm(false);
  };

  return (
    <div className="container center-container"> 
      <div className="row justify-content-center"> 
        <div className="col-md-4">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>1 Tabs</Card.Title>
              <Card.Text>
                For 1 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={() => handleShowForm(1)}>Create</Button> {/* Pass the number of tabs */}
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-4">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>2 Tabs</Card.Title>
              <Card.Text>
                For 2 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={() => handleShowForm(2)}>Create</Button> {/* Pass the number of tabs */}
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-4">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>3 Tabs</Card.Title>
              <Card.Text>
                For 3 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={() => handleShowForm(3)}>Create</Button> {/* Pass the number of tabs */}
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-4">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>4 Tabs</Card.Title>
              <Card.Text>
                For 4 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={() => handleShowForm(4)}>Create</Button> {/* Pass the number of tabs */}
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-4">
          <Card className="custom-card"> 
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>5 Tabs</Card.Title>
              <Card.Text>
                For 5 Tabs click here.
              </Card.Text>
              <Button variant="primary" onClick={() => handleShowForm(5)}>Create</Button> {/* Pass the number of tabs */}
            </Card.Body>
          </Card>
        </div>
      </div>
      <PopupForm show={showForm} handleClose={handleCloseForm} numberOfTabs={numberOfTabs} />
    </div>
  );
};

export default MultipleCards;

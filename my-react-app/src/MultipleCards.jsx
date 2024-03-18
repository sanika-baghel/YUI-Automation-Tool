import React from 'react';
import { Card, Button } from 'react-bootstrap';
import './MultipleCards.css'; // Import the CSS file for styling
import Options from './Options.jpeg';

const MultipleCards = () => {
  return (
    <div className="container center-container"> {/* Added center-container class */}
      <div className="row justify-content-center"> {/* Added justify-content-center class */}
        <div className="col-md-3">
          <Card className="custom-card"> {/* Added custom-card class */}
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>2 Tabs</Card.Title>
              <Card.Text>
                For 2 Tabs click here.
              </Card.Text>
              <Button variant="primary">Create</Button>
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-3">
          <Card className="custom-card"> {/* Added custom-card class */}
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>3 Tabs</Card.Title>
              <Card.Text>
              For 3 Tabs click here.
              </Card.Text>
              <Button variant="primary">Create</Button>
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-3">
          <Card className="custom-card"> {/* Added custom-card class */}
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>4 Tabs</Card.Title>
              <Card.Text>
              For 4 Tabs click here.
              </Card.Text>
              <Button variant="primary">Create</Button>
            </Card.Body>
          </Card>
        </div>
        <div className="col-md-3">
          <Card className="custom-card"> {/* Added custom-card class */}
            <Card.Img variant="top" src={Options} />
            <Card.Body>
              <Card.Title>5 Tabs</Card.Title>
              <Card.Text>
              For 5 Tabs click here.
              </Card.Text>
              <Button variant="primary">Create</Button>
            </Card.Body>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default MultipleCards;

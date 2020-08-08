import React from 'react';
import { Col, Card } from 'react-bootstrap';

class LegendDisplayer extends React.Component{
    constructor(props){
        super(props);
    }

    render(){
        const color = this.props.attribute[0];
        const style = {
            backgroundColor: color,
            color: "#FFFFFF"
        };
        return(
            <div>
                <span className="badge badge-pill badge-primary" style={style}>{this.props.attribute[1]}</span>                
            </div>
        );
    }
}

class LegendVisualizer extends React.Component{
    constructor(props){
        super(props);

        this.state = {
            legends: [  ["#FF0000", "Friend"],
                        ["#00FF00", "LivedIn"],
                        ["#0000FF", "Fan"]]
        }        
    }

    render(){
        return(
            <Col className='pl-0'>
                <Card
                    bg='secondary'
                    text='white'
                    style={{ width: '100%' }}
                    className="mb-2"
                >
                    <Card.Header>Legend</Card.Header>
                    <Card.Body>
                        <Card.Title>Legend</Card.Title>
                        {this.state.legends.map((item, index) => (
                            <LegendDisplayer key={index} attribute={item} />
                        ))}
                    </Card.Body>
                </Card>
            </Col>
        );
    }
}

export default LegendVisualizer;
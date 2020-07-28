import React, { Component } from "react";
import {Col, Row, Badge} from 'react-bootstrap';
import Slider from '@material-ui/core/Slider';

//ToDo: Esto hay que llenarlo con los dates que pueden usarse
const marks = [
    {
      value: 0,
      label: '0°C',
    },
    {
      value: 20,
      label: '20°C',
    },
    {
      value: 37,
      label: '37°C',
    },
    {
      value: 100,
      label: '100°C',
    }
];

//ToDo: Que formatee de la forma que corresponda la fecha
function valuetext(value) {
    return `${value}°C`;
}

class TempSlider extends Component {
    constructor(props) {
      super(props);
      this.state = {currentDate: this.props.currentDate};
    }

    render(){
        return(
            <Row>
                <Col md='10'>
                    <Slider
                        defaultValue={20}
                        getAriaValueText={valuetext}
                        aria-labelledby="discrete-slider-custom"
                        step={10}
                        valueLabelDisplay="auto"
                        marks={marks}
                    />
                </Col>
                <Col md='2'>
                    <Badge variant="primary">{this.state.currentDate}</Badge>
                </Col>
            </Row>
        );
    }
}
export default TempSlider;

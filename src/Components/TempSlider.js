import React from "react";
import {Col, Row, Badge} from 'react-bootstrap';
import Slider from '@material-ui/core/Slider';
import {api_getYears} from '../GraphService/graphQueryService';

const marks = [
    {
      value: 1900,
      label: '1900',
    },
    {
      value: 1965,
      label: '1965',
    },
    {
      value: 1980,
      label: '1980',
    },
    {
      value: 1995,
      label: '1995',
    }
];

export function sliderTest(){
    api_getYears(sliderCallback);
}

//Esta es la funcion que si te fijas en el constructor le bindeo el 'this' para que cuando llame a this.setState cambie
export function sliderCallback(years){
    let array = [];
    years.results[0].data[0].row[0].forEach(function(val){array.push({value:val})})
    this.setState({marcas: array});
}

//ToDo: Que formatee de la forma que corresponda la fecha
function valuetext(value) {
    return `${value}`;
}

class TempSlider extends React.Component {
    constructor(props) {
      super(props);
      this.state = {currentDate: this.props.currentDate, marcas: marks};
      sliderCallback = sliderCallback.bind(this);
    }

    render(){
        return(
            <Row>
                <Col md='10'>
                    <Slider
                        defaultValue={20}
                        aria-labelledby="discrete-slider-custom"
                        step={null}
                        marks={this.state.marcas}
                        min={1900}
                        max={2000}
                        valueLabelDisplay="auto"
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

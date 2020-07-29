import React from "react";
import {Col, Row, Badge} from 'react-bootstrap';
import Slider from '@material-ui/core/Slider';
import Input from '@material-ui/core/Input';
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

const min = 1900;
const max = 2000;

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
      this.state = {interval: [min, max], marcas: marks};
      sliderCallback = sliderCallback.bind(this);
    }

    render(){
        return(
            <Row>
                <Col md='10'>
                    <Slider                        
                        aria-labelledby="discrete-slider-custom"
                        step={null}
                        marks={this.state.marcas}
                        min={min}
                        max={max}
                        valueLabelDisplay="auto"
                        onChange={this.handleSliderChange}
                        value={this.state.interval}
                    />
                </Col>
                <Col md='2'>
                    <Badge variant="primary">{this.state.interval[0] + ' - ' + this.state.interval[1]}</Badge>
                </Col>
            </Row>
        );
    }

    handleSliderChange = (event, value) => this.setState({ interval: value });

}
export default TempSlider;

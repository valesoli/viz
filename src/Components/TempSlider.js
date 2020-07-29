import React from "react";
import {Col, Row, Badge} from 'react-bootstrap';
import Slider from '@material-ui/core/Slider';
import {api_getYears} from '../GraphService/graphQueryService'

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

export function sliderTest(){
    api_getYears(sliderCallback);
}

//Esta es la funcion que si te fijas en el constructor le bindeo el 'this' para que cuando llame a this.setState cambie
export function sliderCallback(years){
    let array = [];
    years.results[0].data[0].row.forEach(function(val){array.push({value:val,label:val.toString(),key:val.id})})
    this.setState({marcas: array});
}

//ToDo: Que formatee de la forma que corresponda la fecha
function valuetext(value) {
    return `${value}°C`;
}

class TempSlider extends React.Component {
    constructor(props) {
      super(props);
      this.state = {currentDate: this.props.currentDate, marcas:marks};
      sliderCallback = sliderCallback.bind(this);
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
                        marks={this.state.marcas}
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

import React from "react";
import {Col, Row, Badge} from 'react-bootstrap';
import Slider from '@material-ui/core/Slider';
import {api_getYears} from '../GraphService/graphQueryService';
import { refreshGraph } from "./NeoGraph";

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

//Esta es la funcion que si te fijas en el constructor le bindeo el 'this' para que cuando llame a this.setState cambie
export function sliderCallback(years){
    let array = [];
    var object = years.results[0].data[0].row[0];
    var min = null;
    var max = null;
    for (var i=0;i<object.length; i++) {
        var val = object[i];
        if(val < min || min == null)
            min = val;
        if(val > max || max == null)
            max = val;
        array.push({
            value:val
        });
    }
    this.setState({marca_maxima:max, marca_minima:min, marcas:array, interval: [min,max]});
}

class TempSlider extends React.Component {
    constructor(props) {
        super(props);
        this.state = {interval: [this.props.initMinDate, this.props.initMaxDate],
                    marcas: marks,
                    marca_minima:1900, 
                    marca_maxima:2000};
        sliderCallback = sliderCallback.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount(){
        api_getYears(sliderCallback);
    }

    handleChange(event, newValue){
        refreshGraph(newValue[0], newValue[1]);
        this.setState({interval: newValue});
    };

    render(){
        return(
            <Row>
                <Col md='10'>
                    <Slider                        
                        aria-labelledby="discrete-slider-custom"
                        step={null}
                        marks={this.state.marcas}
                        min={this.state.marca_minima}
                        max={this.state.marca_maxima}
                        valueLabelDisplay="auto"
                        onChange={this.handleChange}
                        value={this.state.interval}
                    />
                </Col>
                <Col md='2'>
                    <Badge variant="primary">{this.state.interval[0] + ' - ' + this.state.interval[1]}</Badge>
                </Col>
            </Row>
        );
    }
}
export default TempSlider;

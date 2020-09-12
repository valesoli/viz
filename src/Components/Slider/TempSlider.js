import React from "react";
import {Badge} from 'react-bootstrap';
import Slider from '@material-ui/core/Slider';
import {applyFilterAndSave} from 'App.js';

// const marks = [
//     {
//       value: 1900,
//       label: '1900',
//     },
//     {
//       value: 1965,
//       label: '1965',
//     },
//     {
//       value: 1980,
//       label: '1980',
//     },
//     {
//       value: 1995,
//       label: '1995',
//     }
// ];

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
        // TODO: Tengo que conseguir los extremos desde el estado. Los tiene que levantar en test connection
        this.state = {
            interval: [this.props.temporality.currentLow, this.props.temporality.currentHigh],
            marcas: this.buildMarks(this.props.temporality.minDate, this.props.temporality.maxDate, this.props.temporality.granularity),
            marca_minima: this.props.temporality.minDate, 
            marca_maxima: this.props.temporality.maxDate,
            should_have_text_input: false
        };
        
        sliderCallback = sliderCallback.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount(){
    }

    handleChange(event, newValue){
        //refreshGraph(newValue[0], newValue[1]);
        let query = `match (n:Object) where toInteger(split(n.interval[0], '—')[0]) > ${newValue[0]} AND toInteger(split(n.interval[0], '—')[0]) < ${newValue[1]}
                    with collect([id(n),n]) as nodes 
                    match (m:Object)-[r]->(o:Object) with nodes, collect([[id(m),id(o)],type(r)]) as edges return nodes, edges`;
        
        this.setState({interval: newValue});
        applyFilterAndSave(query,newValue[0],newValue[1]);
    };

    buildMarks(min, max, granularity){
        let marks = []
        //Supongo que 10 es un buen numero para ponerle label
        for(let i=min;i<max;i+=granularity){
            marks.push({
                value: i,
                label: (i-min)%10 === 0?i.toString():'',
            })
        }
        marks.push({
            value: max,
            label: max.toString()
        });
        return marks;
    }

    render(){
        return(
            <div style={{width:"100%"}}>
                <div style={{width:"80%"}}>
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
                </div>
                <div style={{width:"20%"}}>
                    <Badge variant="primary" style={{marginTop: '-60px', marginLeft: '25px'}}>
                        {this.state.interval[0] + ' - ' + this.state.interval[1]}
                    </Badge>
                </div>
            </div>
        );
    }
}
export default TempSlider;

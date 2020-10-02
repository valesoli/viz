import React, { useState, useContext } from 'react';
import {Badge} from 'react-bootstrap';
import Slider from '@material-ui/core/Slider';

import { TemporalityContext } from 'core/store/TemporalityContext';


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

const TempSliderContainer = () => {
    // Inicializo el intervalo con lo que traiga del contexto creo
    const { minDate, maxDate, interval, setInterval, granularity } = useContext(TemporalityContext);
    const [ marks, setMarks ] = useState(buildMarks(minDate, maxDate, granularity));
    const [ localInterval, setLocalInterval ] = useState([interval[0],interval[1]]);

    function buildMarks(min, max, granularity){
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

    function handleSubmit(event, newValue){
        setInterval(newValue);
    }

    function handleChange(event, newValue){
        //refreshGraph(newValue[0], newValue[1]);
        // let query = `match (n:Object) where toInteger(split(n.interval[0], '—')[0]) > ${newValue[0]} AND toInteger(split(n.interval[0], '—')[0]) < ${newValue[1]}
        //             with collect([id(n),n]) as nodes 
        //             match (m:Object)-[r]->(o:Object) with nodes, collect([[id(m),id(o)],type(r)]) as edges return nodes, edges`;
        
        setLocalInterval(newValue);
        // applyFilterAndSave(query,newValue[0],newValue[1]);
    };

    return (  
        <div style={{width:"100%"}}>
            <div style={{width:"80%"}}>
                <Slider                        
                    aria-labelledby="discrete-slider-custom"
                    step={null}
                    marks={marks}
                    min={minDate}
                    max={maxDate}
                    valueLabelDisplay="auto"
                    onChange={handleChange}
                    onChangeCommitted={handleSubmit}
                    value={localInterval}
                />
            </div>
            <div style={{width:"20%"}}>
                <Badge variant="primary" style={{marginTop: '-60px', marginLeft: '25px'}}>
                    {interval[0] + ' - ' + interval[1]}
                </Badge>
            </div>
        </div>
    );
}
 
export default TempSliderContainer;
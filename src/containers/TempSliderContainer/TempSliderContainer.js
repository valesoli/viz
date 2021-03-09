import React, { useState, useContext } from 'react';
import {Badge} from 'react-bootstrap';
import Slider from '@material-ui/core/Slider';

import { GraphContext } from 'core/store/GraphContext/GraphContext';
import { normalizeInterval } from 'core/services/graphBuildingService';


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
    const granularityTranslation = [3600,86400,2592000,31536000]
    const step = [10000,1000,100,10]
    // Inicializo el intervalo con lo que traiga del contexto creo
    const { dateExtremes, interval, setInterval, granularity } = useContext(GraphContext);
    const [ marks, setMarks ] = useState(buildMarks(dateExtremes[0], dateExtremes[1], granularity));
    const [ localInterval, setLocalInterval ] = useState([Date.parse(interval[0])/1000,Date.parse(interval[1])/1000]);

    function buildMarks(minNN, maxNN, granularity){
        let marks = [];
        let [min,max] = normalizeInterval([minNN, maxNN]); 
        if(granularity<2) return [min,max];
        //Supongo que 10 es un buen numero para ponerle label
        let granularityTimestamp = granularityTranslation[granularity];
        let count = 0;
        for(let i=min;i<max;i+=granularityTimestamp){
            marks.push({
                value: i,
                label: count%step[granularity] === 0?new Date(i*1000).toLocaleDateString():'',
            })
            count++;
        }
        marks.push({
            value: max,
            label: count%10<5?'':new Date(max*1000).toLocaleDateString()
        });
        return marks;
    }

    function handleSubmit(event, newValue){
        let parsedMin = new Date(newValue[0]*1000);
        let parsedMax = new Date(newValue[1]*1000);
        setInterval([parsedMin.toString(),parsedMax.toString()]);
    }

    function handleChange(event, newValue){
        //refreshGraph(newValue[0], newValue[1]);
        // let query = `match (n:Object) where toInteger(split(n.interval[0], '—')[0]) > ${newValue[0]} AND toInteger(split(n.interval[0], '—')[0]) < ${newValue[1]}
        //             with collect([id(n),n]) as nodes 
        //             match (m:Object)-[r]->(o:Object) with nodes, collect([[id(m),id(o)],type(r)]) as edges return nodes, edges`;
        
        //ToDo: Los valores tienen que ser timestamps pero los display Date
        setLocalInterval(newValue);
        // applyFilterAndSave(query,newValue[0],newValue[1]);
    };

    return (  
        <div style={{width:"100%"}}>
            <div style={{width:"80%"}}>
            {
                granularity >= 2?
                    <Slider                        
                        aria-labelledby="discrete-slider-custom"
                        step={null}
                        marks={marks}
                        min={marks[0].value}
                        max={marks[marks.length-1].value}
                        valueLabelDisplay="auto"
                        valueLabelFormat={(x) => new Date(x*1000).toLocaleDateString()}
                        onChange={handleChange}
                        onChangeCommitted={handleSubmit}
                        value={localInterval}
                    />
                    
                :
                    <Slider
                        aria-labelledby= "range-slider"
                        min={marks[0]}
                        max={marks[1]}
                        onChange={handleChange}
                        onChangeCommitted={handleSubmit}
                        value={localInterval}
                    />
            }
            </div>
            <div style={{width:"20%"}}>
                <Badge variant="primary" style={{marginTop: granularity>=2?'-60px':'-20px', marginLeft: '25px'}}>
                    {new Date(Date.parse(interval[0])).toDateString() + ' - ' + new Date(Date.parse(interval[1])).toDateString()}
                </Badge>
            </div>
        </div>
    );
}
 
export default TempSliderContainer;
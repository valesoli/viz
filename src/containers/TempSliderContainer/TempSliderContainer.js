import React, { useState } from 'react';
import TempSlider from 'components/TempSlider/TempSlider';

const TempSliderContainer = () => {
    // Inicializo el intervalo con lo que traiga del contexto creo
    const [ interval, setInterval ] = useState([]);
    const [ marks, setMarks ] = useState([]);


    // TODO: Tengo que conseguir los extremos desde el estado. Los tiene que levantar en test connection
    this.state = {
        interval: [this.props.temporality.currentLow, this.props.temporality.currentHigh],
        marcas: this.buildMarks(this.props.temporality.minDate, this.props.temporality.maxDate, this.props.temporality.granularity),
        marca_minima: this.props.temporality.minDate, 
        marca_maxima: this.props.temporality.maxDate
    };
    
    sliderCallback = sliderCallback.bind(this);
    this.handleChange = this.handleChange.bind(this);

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

    return (  
        <TempSlider

        />
    );
}
 
export default TempSliderContainer;
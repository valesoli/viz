import React from 'react';
import { Card } from "components/Card/Card.jsx";
import ChartistGraph from "react-chartist";
import {
    dataPie,
    legendPie
  } from "variables/Variables.jsx";

class FilterModule extends React.Component{
    constructor(props){
        super(props);
    }

    createLegend(json) {
        var legend = [];
        for (var i = 0; i < json["names"].length; i++) {
          var type = "fa fa-circle text-" + json["types"][i];
          legend.push(<i className={type} key={i} />);
          legend.push(" ");
          legend.push(json["names"][i]);
        }
        return legend;
    }
    
    render(){
        return(
            <Card
                statsIcon="fa fa-clock-o"
                title="Filter Module"
                category="Focus on the information that matters"
                stats="Campaign sent 2 days ago"
                content={
                    <div></div>
                }
            />
        );
    }
}

export default FilterModule;
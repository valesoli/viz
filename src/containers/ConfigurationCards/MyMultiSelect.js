import { tbdgQuery } from 'core/services/graphBuildingService';
import { GraphContext } from 'core/store/GraphContext/GraphContext';
import React, {useContext, useEffect, useState, useRef} from 'react';
import MultiSelect from "react-multi-select-component";

const MyMultiSelect = (props) => {
    const mounted = useRef(false);
    const {setOneQueryFilter, queryFilters} = useContext(GraphContext);
    const [selected, setSelected] = useState(queryFilters[props.type]);
    const [options, setOptions] = useState([]);
    //Tengo que levantar las opciones de la bdd así que tengo que hacer un call.

    useEffect(
        () => {
            if(mounted.current || queryFilters[props.type] == undefined){
                setOneQueryFilter(props.type, selected);
            } else {
                mounted.current = true;
            }
        },
        [selected]
    )

    function getOptions(type, attr){
        //Formatear la query
        let prop = 'a.'+ attr
        let query = 'select '+ prop + ' match (a:' + type + ')';
        tbdgQuery(query).then((val) => {
            let newOptions = [];    
            for(let element in val.data.data){
                newOptions.push({label: val.data.data[element][prop].value, value: val.data.data[element][prop].value});
            }
            setOptions(newOptions);
        });
    };

    useEffect(() => getOptions(props.type, props.attr), []);

    return (
        <MultiSelect
            options={options}
            value={selected}
            onChange={setSelected}
            labelledBy={"Selected"}
        />
    );

}
 
export default MyMultiSelect;
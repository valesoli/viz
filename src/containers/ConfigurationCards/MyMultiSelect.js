import { tbdgQuery } from 'core/services/graphBuildingService';
import { GraphContext } from 'core/store/GraphContext/GraphContext';
import React, {useContext, useEffect, useState} from 'react';
import MultiSelect from "react-multi-select-component";

const MyMultiSelect = (props) => {
    const {setOneQueryFilter} = useContext(GraphContext);
    const [selected, setSelected] = useState([]);
    const [options, setOptions] = useState([]);
    //Tengo que levantar las opciones de la bdd así que tengo que hacer un call.

    //Hay que armar un hook para cuando cambie selected y que genere una nueva query.
    useEffect(
        () => {
            setOneQueryFilter(props.type, selected);
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
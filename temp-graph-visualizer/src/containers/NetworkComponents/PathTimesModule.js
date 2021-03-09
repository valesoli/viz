import React, { useContext } from 'react';
import { Table } from 'react-bootstrap';
import Card from 'components/Card/Card.jsx';
import { GraphContext } from "core/store/GraphContext/GraphContext";

const TimeDisplayer = (props) => {
    return (
        <tr>
            <td>
                <span className="badge badge-pill badge-primary">{props.interval[0]}</span>
            </td>
            <td>
                <span className="badge badge-pill badge-primary">{props.interval[1]}</span>
            </td>
        </tr>
    );
}

const PathTimesModule = () => {
    const { pathTimes } = useContext(GraphContext);

    if(pathTimes == null){
        return '';
    } else {
        return ( 
            <Card
                title="Path's Times"
                content={
                    <Table striped hover style={{marginBottom: "0px"}}>
                        <thead>
                            <tr>
                                <td>Departure</td>
                                <td>Arrival</td>
                            </tr>
                        </thead>
                        <tbody>
                            {pathTimes.map((item, index) => (<TimeDisplayer key={index} interval={item}/>))}
                        </tbody>
                    </Table>
                }
            />
        );
    }
}
 
export default PathTimesModule;
import React, { useState, useContext } from 'react';
import Card from 'components/Card/Card.jsx';
import { UserCard } from "components/UserCard/UserCard.jsx";

import {Jumbotron} from 'react-bootstrap';
import { SelectedNodeContext } from 'core/store/SelectedNodeContext';
import { useQuery } from 'react-query';
import { fetchNeoQuery } from 'core/services/configQueryServices';
import { VisualConfigContext } from 'core/store/VisualConfigContext/VisualConfigContext';
import { ConnectionConfigContext } from 'core/store/ConnectionConfigContext/ConnectionConfigContext';

class AttributesDisplayer extends React.Component{
    render(){
        return(
            <>
                <hr/>
                <div className="container-fluid">
                    <div style={{float:"left", fontWeight:"bold"}}>
                        {this.props.attribute[0]}
                    </div>
                    <div style={{float:"right"}}>
                        <span className="badge badge-pill badge-primary">{this.props.attribute[1]}</span>
                        <span className="badge badge-pill badge-primary">{this.props.attribute[2]}</span>
                    </div>
                </div>
            </>
        );
    }
}

const NodeVisualizer = () => {
    const { selectedNodeId } = useContext(SelectedNodeContext);
    const { connectionConfig } = useContext(ConnectionConfigContext);
    const { visualConfig } = useContext(VisualConfigContext);
    const [ selectedNode, setSelectedNode ] = useState( {
        nodeType: "MockType",
        nodeAttributes: [["MockAttr1", "MockVal1"],["MockAttr2", "MockVal2"]]
    });

    function nodeSelectionCallback(response){
        if( selectedNodeId == 0){
            setSelectedNode(
                {
                    nodeType: "MockType"
                }
            )
            return;
        }
        let response_array = response.data.results[0].data;
        let formated_array = response_array.map((e) => {
            return [e.row[1], e.row[2], e.row[3]];
        })
        let nodeTitle = response_array[0].row[0];
        setSelectedNode({nodeType: nodeTitle, nodeAttributes: formated_array});
    }

    const { data, status } = useQuery(['selectedNode', connectionConfig, "match (o:Object)-->(a:Attribute)-->(v:Value) where o.id = " + selectedNodeId + " return o.title, a.title, v.value, v.interval" ], fetchNeoQuery, {
        onSuccess: nodeSelectionCallback
    });

    function buildContent(){
        if(selectedNode.nodeType !== "MockType"){
            return (
                <UserCard
                    bgColor={visualConfig.nodeColors[selectedNode.nodeType]}
                    avatar={visualConfig.nodeAvatars[selectedNode.nodeType]}
                    name={selectedNode.nodeType}
                    description={
                        <div>
                            {selectedNode.nodeAttributes.map((item, index) => (
                                <AttributesDisplayer key={index} attribute={item} />
                            ))}
                        </div>
                    }
                />
            );
        } else {
            return (
                <Jumbotron style={{paddingRight:'15px', paddingLeft:'15px', paddingTop:'5px', paddingBottom:'5px'}}>
                    <h1> Click a node! </h1>
                    <p>
                        If you'd like to visualize your node's attributes, please
                        click it.
                    </p>
                </Jumbotron>
            );
        }
    }

    return (
        <Card
            style={{marginBottom:'10px'}}
            statsIcon="fa pe-7s-magic-wand"
            title="Selector Module"
            content={
                buildContent() 
            }
        />
    );
}
 
export default NodeVisualizer;
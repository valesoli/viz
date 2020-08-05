import React from 'react';
import { Col, Card } from 'react-bootstrap';
import { api_cypherQuery } from '../GraphService/graphQueryService';

class AttributesDisplayer extends React.Component{
    constructor(props){
        super(props);
    }

    render(){
        return(
            <div>
                {this.props.attribute[0]}
                <span className="badge badge-pill badge-primary">{this.props.attribute[1]}</span>
            </div>
        );
    }
}

export function onClickUpdateSelectionVis(id){
    api_cypherQuery("match (o:Object)-->(a:Attribute)-->(v:Value) where id(o) = " +id+" return o.title, a.title, v.value", onNodeClickCallback);
}

function onNodeClickCallback(response){
    let response_array = response.results[0].data;
    let formated_array = response_array.map((e) => {
        return [e.row[1], e.row[2]];
    })
    let nodeTitle = response_array[0].row[0];
    this.setState({ nodeType: nodeTitle, nodeAttributes: formated_array});
}

class NodeVisualizer extends React.Component{
    constructor(props){
        super(props);

        this.state = {
            nodeType: "MockType",
            nodeAttributes: [["MockAttr1", "MockVal1"],["MockAttr2", "MockVal2"]]
        }
        
        onNodeClickCallback = onNodeClickCallback.bind(this);
    }

    render(){
        return(
            <Col md='3' className='pl-0'>
                <Card
                    bg='secondary'
                    text='white'
                    style={{ width: '100%' }}
                    className="mb-2"
                >
                    <Card.Header>Node Selection</Card.Header>
                    <Card.Body>
                        <Card.Title>{this.state.nodeType}</Card.Title>
                        <div>
                            {this.state.nodeAttributes.map((item, index) => (
                                <AttributesDisplayer key={index} attribute={item} />
                            ))}
                        </div>
                    </Card.Body>
                </Card>
            </Col>
        );
    }
}

export default NodeVisualizer;
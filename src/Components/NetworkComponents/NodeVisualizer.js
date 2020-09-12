import React from 'react';
import Card from 'components/Card/Card.jsx';
import { UserCard } from "components/UserCard/UserCard.jsx";
import { api_cypherQuery } from '../../Services/GraphService/graphQueryService';

import {Jumbotron} from 'react-bootstrap';

class AttributesDisplayer extends React.Component{
    render(){
        return(
            <div>
                {this.props.attribute[0] + "\t"}
                <span className="badge badge-pill badge-primary">{this.props.attribute[1] + "\t" + this.props.attribute[2]}</span>
            </div>
        );
    }
}

export function onClickUpdateSelectionVis(id, con_config){
    api_cypherQuery("match (o:Object)-->(a:Attribute)-->(v:Value) where id(o) = " + id + " return o.title, a.title, v.value, v.interval", onNodeClickCallback, con_config);
}
var onNodeClickCallback;

class NodeVisualizer extends React.Component{
    constructor(props){
        super(props);
        this.refs = React.createRef();

        this.state = {
            nodeType: "MockType",
            nodeAttributes: [["MockAttr1", "MockVal1"],["MockAttr2", "MockVal2"]]
        }
                
        console.log("Creación del Modulo NodeVis")
        onNodeClickCallback = this.onNodeClickCallbackInt.bind(this);
    }

    componentDidMount(){
    }

    componentWillUnmount(){
    }

    componentDidUpdate(){
    }

    onNodeClickCallbackInt(response){
        let response_array = response.results[0].data;
        let formated_array = response_array.map((e) => {
            return [e.row[1], e.row[2], e.row[3]];
        })
        let nodeTitle = response_array[0].row[0];
        this.setState({ nodeType: nodeTitle, nodeAttributes: formated_array});
    }

    buildContent(){
        if(this.state.nodeType !== "MockType"){
            return (
                <UserCard
                    bgColor={this.props.visual? this.props.visual.nodeColors[this.state.nodeType] : null}
                    avatar={this.props.visual? this.props.visual.nodeAvatars[this.state.nodeType] : "pe-7s-users"}
                    name={this.state.nodeType}
                    description={
                        <div>
                            {this.state.nodeAttributes.map((item, index) => (
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

    render(){
        let node_content = this.buildContent();

        return(
            <Card
                style={{marginBottom:'10px'}}
                statsIcon="fa pe-7s-magic-wand"
                title="Selector Module"
                content={
                    node_content
                }
            />
        );
    }
}

export default NodeVisualizer;
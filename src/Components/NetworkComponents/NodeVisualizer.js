import React from 'react';
import Card from 'components/Card/Card.jsx';
import { UserCard } from "components/UserCard/UserCard.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import { api_cypherQuery } from '../../Services/GraphService/graphQueryService';


import avatar from "assets/img/faces/face-3.jpg";

class AttributesDisplayer extends React.Component{
    constructor(props){
        super(props);
    }

    render(){
        return(
            <div>
                {this.props.attribute[0] + "\t"}
                <span className="badge badge-pill badge-primary">{this.props.attribute[1]}</span>
            </div>
        );
    }
}

export function onClickUpdateSelectionVis(id, con_config){
    api_cypherQuery("match (o:Object)-->(a:Attribute)-->(v:Value) where id(o) = " + id + " return o.title, a.title, v.value", onNodeClickCallback, con_config);
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
                
        onClickUpdateSelectionVis = onClickUpdateSelectionVis.bind(this);
        onNodeClickCallback = onNodeClickCallback.bind(this);
    }

    componentDidUpdate(){
    }

    render(){
        return(
            <Card
                statsIcon="fa fa-clock-o"
                title="Selector Module"
                category="Review the details of your nodes"
                stats="Click a node to view its details"
                content={
                    <UserCard
                        bgImage="https://ununsplash.imgix.net/photo-1431578500526-4d9613015464?fit=crop&fm=jpg&h=300&q=75&w=400"
                        avatar={avatar}
                        name={this.state.nodeType}
                        //userName="michael24"
                        description={
                            <div>
                                {this.state.nodeAttributes.map((item, index) => (
                                    <AttributesDisplayer key={index} attribute={item} />
                                ))}
                            </div>
                        }
                        socials={
                        <div>
                            <Button simple>
                            <i className="fa fa-facebook-square" />
                            </Button>
                            <Button simple>
                            <i className="fa fa-twitter" />
                            </Button>
                            <Button simple>
                            <i className="fa fa-google-plus-square" />
                            </Button>
                        </div>
                        }
                    />
                }
                // legend={
                //     <div className="legend">{}</div>
                // }
            />
        );
    }
}

export default NodeVisualizer;
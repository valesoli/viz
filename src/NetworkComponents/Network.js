import React from 'react';
import { runForceGraph } from "./forceGraphGenerator";
import styles from "./forceGraph.module.css";

class Network extends React.Component {
    constructor(props){
        super(props);
        this.updater = null;
        this.destroyer = null;
        this.containerRef = React.createRef(null);
    }

    componentDidMount(){
        let destroyFn;
        let updaterFn;
  
        if (this.containerRef.current) {
            let return_pkg = runForceGraph(this.containerRef.current, this.props.linksData, this.props.nodesData, this.props.nodeHoverTooltip);
            destroyFn = return_pkg.destroy;
            updaterFn = return_pkg.update;
        }
        this.updater = updaterFn;
        this.destroyer = destroyFn;
    }

    updateGraph(){
        //this.destroyer();
        this.updater({nodes: this.props.nodesData, links: this.props.linksData});
    }

    componentDidUpdate(prevProps) {
        if (prevProps.nodesData !== this.props.nodesData) {
            this.updateGraph();
        }
      }
  
    render(){
        return <div ref={this.containerRef} className={styles.container} />;
    }
}

export default Network;
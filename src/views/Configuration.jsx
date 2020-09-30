/*!
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard-react/blob/master/LICENSE.md)
*/
import React, { useContext } from "react";
import {
  Grid,
  Row,
  Col
} from "react-bootstrap";

import DbConfigCard from 'containers/ConfigurationCards/DbConfigCard';
import NodeConfigCard from "containers/ConfigurationCards/NodeConfigCard";
import EdgeConfigCard from "containers/ConfigurationCards/EdgeConfigCard";
import GranularityConfigCard from "containers/ConfigurationCards/GranularityConfigCard";
import { ConnectionConfigContext } from "core/store/ConnectionConfigContext";
import { VisualConfigContext } from "core/store/VisualConfigContext";

const Configuration = (props) => {
  const { connectionConfig } = useContext(ConnectionConfigContext);
  const { visualConfig } = useContext(VisualConfigContext);

  function getGranularityCard(){
    if(connectionConfig.connected){
      return (
        <Row style={{marginBottom:'15px'}}>
          <Col md={12}>
            <GranularityConfigCard/>
          </Col>
        </Row>
      );
    } else {
      return '';
    }
  }
  

  function getVisualConfig(){
    if(connectionConfig.connected){
      return (
        <Row style={{marginBottom:'15px'}}>
            <Col md={9} style={{paddingRight: '5px'}}>
              <NodeConfigCard visual={visualConfig}/>
            </Col>
            <Col md={3} style={{paddingLeft: '5px'}}>
              <EdgeConfigCard visual={visualConfig}/>
            </Col>
        </Row>
      );
    } else {
      return '';
    }
  }

  return (
    <div className="content">
      <Grid fluid>
        <Row style={{marginBottom:'15px'}}>
          <Col md={12}> 
            <DbConfigCard/> 
          </Col>
        </Row>
        {getGranularityCard()}
        {getVisualConfig()}
      </Grid>
    </div>
  );
}
 
export default Configuration;
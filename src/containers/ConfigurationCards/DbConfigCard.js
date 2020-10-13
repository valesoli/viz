import React, { useContext, useState } from "react";
import { useQuery } from 'react-query';
import { Card } from "components/Card/Card";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import { tryConnection, fetchNeoQuery } from "core/services/configQueryServices";
import { ConnectionConfigContext } from "core/store/ConnectionConfigContext/ConnectionConfigContext";
import { GraphContext } from "core/store/GraphContext/GraphContext";
import { RelationshipsContext } from "core/store/RelationshipsContext/RelationshipsContext";

//ToDo: Cubrir la contraseña del form
const DbConfigCard = (props) => {
  const { connectionConfig, dispatch } = useContext(ConnectionConfigContext);
  const { setOneDateExtreme, setInterval } = useContext(GraphContext);
  const [connectionUrl, setConnectionUrl] = useState(
    connectionConfig.url
  );
  const [username, setUsername] = useState(connectionConfig.user);
  const [password, setPassword] = useState(connectionConfig.pass);
  const { relationshipsConfig, dispatch2 } = useContext(RelationshipsContext);

  const handleSubmit = (e) => {
    e.preventDefault();
    tryConnection(connectionUrl, username, password)
      .then((response) => {
        let years = response.data.results[0].data[0].row[0];
        let min = years[0];
       // let today = new Date();
        // max = today.getFullyear() 
        // Entiendo que el máximo debería ser el día/año de la fecha 
        let max = "2020"; 
        setOneDateExtreme(0,min);
        setOneDateExtreme(1,max);
        setInterval([Date.parse(min),Date.parse(max)])
        dispatch({
          type: "CHANGE_CONFIG",
          config: {
            connected: null,
            url: connectionUrl,
            user: username,
            pass: password,
            info: { success: 1, val:"SUCCESS"}
          },
        });
      })
      .catch((err) => {
        console.log(err);
        dispatch({
          type: "CONNECTION_FAILED",
          config:{
            info: {success: 2, val: err.toString()}
          }
        });
      });

      fetchNeoQuery("relatioships", connectionConfig, "match (n:Object)-[r]->(m:Object) return distinct [n.title, m.title], type(r)")
      .then((response) => {
        let response_table = response.data.results[0].data;        
        for(let i=0; i<response_table.length; i++){
          let nodes = response_table[i].row[0];
          let relationship = response_table[i].row[1];
          relationshipsConfig.map.push([nodes, relationship]);
        }
        dispatch2({
          type: "CHANGE_MAP",
          config: {
            map: relationshipsConfig.map,
            info: {
              success: true,
              description: ''
            }
          }
        });
      });
  };

  return (
    <Card
      title="Database Connection"
      content={
        <form onSubmit={handleSubmit}>
          <FormInputs
            ncols={["col-md-5", "col-md-3", "col-md-4"]}
            properties={[
              {
                label: "Connection URL:port",
                type: "text",
                bsClass: "form-control",
                placeholder: "Connection URL",
                onChange: (e) => setConnectionUrl(e.target.value),
                value: connectionUrl,
              },
              {
                label: "Username",
                type: "text",
                bsClass: "form-control",
                placeholder: "Username",
                onChange: (e) => setUsername(e.target.value),
                value: username,
              },
              {
                label: "Password",
                type: "password",
                bsClass: "form-control",
                placeholder: "Password",
                onChange: (e) => setPassword(e.target.value),
                value: password,
              },
            ]}
          />
          <div className="pull-right">
            {connectionConfig.info.success == 2?<div className="callout pull-left" style={{marginTop: 0, marginBottom: 0, marginRight: '5px', paddingTop: '9px', paddingBottom: '9px'}}>{connectionConfig.info.val}</div>:''}
            <div className="pull-right" >
              <Button bsStyle="info" fill type="submit">
                Test Connection {connectionConfig.info.success == 1?<i className="fa fa-check-circle"/>:connectionConfig.info.success == 2?<i className="fa fa-times-circle"/>:''}
              </Button>
            </div>
          </div>
          <div className="clearfix" />
        </form>
      }
    />
  );
};

export default DbConfigCard;

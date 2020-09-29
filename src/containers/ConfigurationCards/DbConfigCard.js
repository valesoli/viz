import React, { useContext, useState } from 'react';
import { Card } from 'components/Card/Card';
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import {tryConnection} from "core/services/configQueryServices";

import { ConnectionConfigContext } from 'core/store/ConnectionConfigContext';
import { TemporalityContext } from 'core/store/TemporalityContext';

//ToDo: Cubrir la contraseña del form
const DbConfigCard = (props) => {
  const { dispatch } = useContext(ConnectionConfigContext);
  const { setMinDate, setMaxDate } = useContext(TemporalityContext);
  const [ connectionUrl, setConnectionUrl ] = useState('http://localhost:7474/db/data/transaction/commit');
  const [ username, setUsername ] = useState('neo4j');
  const [ password, setPassword ] = useState('admin');

  const handleSubmit = (e) => {
    e.preventDefault();
    tryConnection(connectionUrl, username, password)
    .then( response => {
      let years = response.data.results[0].data[0].row[0];
      let min = years[0];
      let max = years[years.length -1];
      setMinDate(min);
      setMaxDate(max);
      dispatch(
        {
          type: "CHANGE_CONFIG",
          config: {
            connected: null, 
            url: connectionUrl, 
            user: username, 
            pass: password 
          }
        }
      )
      } 
    )
    .catch(
      err => {
        console.log(`This is the error: ${err}`);
        dispatch({
          type: "CONNECTION_FAILED"
        });
      }
    );
    // TODO: Acá hay que empezar a trabajar con el resto de las variables que se inicializan en el connect
  }

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
                    onChange: e => setConnectionUrl(e.target.value),
                    value: connectionUrl
                  },
                  {
                    label: "Username",
                    type: "text",
                    bsClass: "form-control",
                    placeholder: "Username",
                    onChange: e => setUsername(e.target.value),
                    value: username
                  },
                  {
                    label: "Password",
                    type: "password",
                    bsClass: "form-control",
                    placeholder: "Password",
                    onChange: e => setPassword(e.target.value),
                    value: password
                  }
                ]}
              />
              <Button bsStyle="info" pullRight fill type="submit">
                Test Connection
              </Button>
              <div className="clearfix" />
            </form>
          }
      />
  );
}

export default DbConfigCard;
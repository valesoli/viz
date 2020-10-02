import React, { useContext, useState } from "react";
import { Card } from "components/Card/Card";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import { tryConnection } from "core/services/configQueryServices";

import { ConnectionConfigContext } from "core/store/ConnectionConfigContext";
import { TemporalityContext } from "core/store/TemporalityContext";

//ToDo: Cubrir la contraseña del form
const DbConfigCard = (props) => {
  const { connectionConfig, dispatch } = useContext(ConnectionConfigContext);
  const { setMinDate, setMaxDate, setInterval } = useContext(TemporalityContext);
  const [connectionUrl, setConnectionUrl] = useState(
    connectionConfig.url
  );
  const [username, setUsername] = useState(connectionConfig.user);
  const [password, setPassword] = useState(connectionConfig.pass);

  const handleSubmit = (e) => {
    e.preventDefault();
    tryConnection(connectionUrl, username, password)
      .then((response) => {
        let years = response.data.results[0].data[0].row[0];
        let min = years[0];
        let max = years[years.length - 1];
        setMinDate(min);
        setMaxDate(max);
        setInterval([min,max])
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

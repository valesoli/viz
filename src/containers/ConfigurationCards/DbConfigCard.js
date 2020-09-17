import React from 'react';
import { Card } from 'components/Card/Card';
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";

import { connect } from "App";

//ToDo: Cubrir la contraseña del form
class DbConfigCard extends React.Component{
    constructor(props){
        super(props);
        this.state = {
          connected: this.props.connection.connected,
          url: '',
          user: '',
          pass: ''
        }
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
      // ToDo: Deshardcodear las configuraciones y levantarlas desde el form
      var neo4j_config = {
        txUrl: "http://localhost:7474/db/data/transaction/commit",
        NEO_USER: 'neo4j',
        NEO_PASS: 'admin'
    }
      connect(neo4j_config);
      event.preventDefault();
    }

    setPassword(val){
      this.setState({pass: val});
    }

    setUser(val){
      this.setState({user: val});
    }

    render(){
        return(
            <Card
                title="Database Connection"
                content={
                  <form onSubmit={this.handleSubmit}>
                    <FormInputs
                      ncols={["col-md-5", "col-md-3", "col-md-4"]}
                      properties={[
                        {
                          label: "Connection URL:port",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "Company",
                          defaultValue: this.state.connected?this.state.url:"http://localhost:7474"
                        },
                        {
                          label: "Username",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "Username",
                          onChange: e => this.setUser(e.target.value),
                          defaultValue: this.state.connected?this.state.user:"admin"
                        },
                        {
                          label: "Password",
                          type: "password",
                          bsClass: "form-control",
                          placeholder: "Password",
                          onChange: e => this.setPassword(e.target.value),
                          defaultValue: this.state.connected?this.state.pass:""
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
}
export default DbConfigCard;
import React from 'react';
import { Card } from './Card';
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";

class DbConfigCard extends React.Component{
    constructor(props){
        super(props);
    }
    render(){
        return(
            <Card
                title="Database Connection"
                content={
                  <form>
                    <FormInputs
                      ncols={["col-md-5", "col-md-3", "col-md-4"]}
                      properties={[
                        {
                          label: "Connection URL:port",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "Company",
                          defaultValue: "http://localhost:7474"
                        },
                        {
                          label: "Username",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "Username",
                          defaultValue: "admin"
                        },
                        {
                          label: "Password",
                          type: "password",
                          bsClass: "form-control",
                          placeholder: "Password"
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
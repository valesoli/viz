import React, { useState, useContext } from "react";
import { useQuery } from "react-query";
import { ConnectionConfigContext } from "core/store/ConnectionConfigContext/ConnectionConfigContext";
import { VisualConfigContext } from "core/store/VisualConfigContext/VisualConfigContext";
import { fetchNeoQuery } from "core/services/configQueryServices";
import { Table } from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import MyColorPicker from "containers/CustomColorPicker/MyColorPicker";
import Loader from "react-loader-spinner";

const EdgeConfigCard = (props) => {
  const defaultColors = ['#66c2a5','#fc8d62','#8da0cb','#e78ac3','#a6d854','#ffd92f']; 
  const { connectionConfig } = useContext(ConnectionConfigContext);
  const { visualConfig, dispatch } = useContext(VisualConfigContext);
  const [edges, setEdges] = useState(null);

  const responseFormatter = (response) => {
    let response_table = response.data.results[0].data[0].row[0];
    let newEdges = [];
    let edgesColors = {};
    for (var i = 0; i < response_table.length; i++) {
      let td = { type: "", color: "" };
      td.type = response_table[i];
      td.color = visualConfig.edgeColors[response_table[i]]?visualConfig.edgeColors[response_table[i]]:defaultColors[i];
      edgesColors[td.type] = td.color;
      newEdges.push(td);
    }
    setEdges(newEdges);
    dispatch({ type: "CHANGE_EDGES", edgeColors: edgesColors });
    return newEdges;
  };

  const { data, status } = useQuery(
    [
      "edges",
      connectionConfig,
      "match (:Object)-[r]->(:Object) return collect(distinct type(r))",
    ],
    fetchNeoQuery,
    {
      onSuccess: responseFormatter,
    }
  );

  function receiveColor(type, color) {
    edges.forEach((element) => {
      if (element.type === type) {
        element.color = color.hex;
      }
    });
    let edgesColors = {};
    edges.forEach((element) => {
      edgesColors[element.type] = element.color;
    });
    dispatch({ type: "CHANGE_EDGES", edgeColors: edgesColors });
  }

  function confirmChanges() {
    let edgesColors = {};
    edges.forEach((element) => {
      edgesColors[element.type] = element.color;
    });
    dispatch({ type: "CHANGE_EDGES", edgeColors: edgesColors });
  }

  return (
    <>
      {edges == null ?  
        <div style={{ textAlign: "center" }}>
          <Loader type="ThreeDots" color="#00BFFF" height={80} width={80} />
        </div>
      :
        <Card
          title="Edges Settings"
          ctTableResponsive
          content={
            <Table striped hover>
              <thead>
                <tr>
                  <th key={1}>TYPE</th>
                  <th key={2}>COLOR</th>
                </tr>
              </thead>
              <tbody>
                {edges.map((prop, key) => {
                  return (
                    <tr key={key}>
                      <td key={key + "1"}>{prop.type}</td>
                      <td key={key + "2"}>
                        {
                          <MyColorPicker
                            color={prop.color}
                            myType={prop.type}
                            parentChange={receiveColor}
                          />
                        }
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </Table>
          }
        />
      }
    </>
  );
};

export default EdgeConfigCard;
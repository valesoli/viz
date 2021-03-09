import React, { useState, useContext } from 'react';
import {UnControlled as CodeMirror} from 'react-codemirror2';
import { GraphContext } from 'core/store/GraphContext/GraphContext';
import { helper_elapsedTimeMessage, helper_initializeTable } from 'containers/QueryBox/QueryBoxHelper';

const QueryBox = (props) => {
  const { userQuery, setQuery, setUserQuery, setVariables } = useContext(GraphContext);
  const [ localQuery, setLocalQuery ] = useState(userQuery);
  const [ message, setMessage ] = useState('');
  const [ result, setResult ] = useState(null);

  const handleResponse = (response) => {
    let new_message = '';
    let new_result = null;
    if (response.success === true) {
        if (response.data.length === 0) {
            new_message = "No results found. " + helper_elapsedTimeMessage(response);
        }
        else {
            new_message = helper_elapsedTimeMessage(response);
            new_result = helper_initializeTable(response.data);
        }
    } else {
        new_message = response.message;
    }
    setMessage(new_message);
    setResult(new_result);
  }

  const handleSubmit = (event) => {
      event.preventDefault();
      setUserQuery(localQuery);
      let parsedQuery = stripAttributes(localQuery);
      setVariables(getVariables(localQuery));
      setQuery(parsedQuery);      
  }

  const stripAttributes = (query) => {
    let loweredCase = query.toLowerCase();
    let selectStart = loweredCase.indexOf("select");
    let matchStart = loweredCase.indexOf("match");
    let selectClause = loweredCase.slice(selectStart, matchStart);
    let fields = selectClause.split(",");
    let newFields = [];
    for(let i = 0; i< fields.length; i++){
      let attrStart = fields[i].indexOf(".");
      let newField = fields[i];
      if(attrStart >= 0)
        newField = fields[i].slice(0, attrStart)
      newFields.push(newField);
    }
    let newSelectClause = "";
    for(let j = 0; j < newFields.length; j++){
      newSelectClause += newFields[j] + ",";
    }
    newSelectClause = newSelectClause.slice(0, newSelectClause.length - 1);
    let theRest = query.slice(matchStart);
    let newQuery = newSelectClause + " " + theRest;
    return newQuery;
  }
  
  const getVariables = (query) => {
    const regex = /\s?(\w+.?\w+|\w+\[\w+\])\s?=\s?('[\w\s-._]+'|\d+)/g;
    const regex2 = /(\w+).?\[?(\w+)\]?\s?=\s?('[\w\s-._]+'|\d+)/;
    const answer = query.match(regex);
    let match, object, attribute, value;
    let variables = [];
    if(answer !== null){
      for(let i=0; i<answer.length; i++){
        match = answer[i].match(regex2);
        object = match[1];
        attribute = match[2];
        value = match[3].replaceAll("'", "");
        variables.push([attribute, value]);
      }
    }
    return variables;
  }

  return (
    <div className="container-fluid">
      <form id="query-form" method="post" onSubmit={handleSubmit}>
        <div className="form-group" style={{float:"left"}}>
            {/* <label form="query">Query: </label> */}
            {/* <textarea class="form-control" id="query" name="query"></textarea> */}                        
          <CodeMirror
            value={userQuery}
            options={{
              mode: 'application/x-cypher-query',
              theme: 'default',
              lineNumbers: true,
              height: 'auto',
            }}
            onChange={(editor, data, value) => {
              setLocalQuery(value);
            }}
          />
        </div>
        <div style={{float:"right"}}>
          <button type="submit" className="btn btn-primary" >Submit</button>
        </div>
      </form>

      <div id="message">{message}</div>
      {result == null?'':result}
    </div>
  );
}
 
export default QueryBox;
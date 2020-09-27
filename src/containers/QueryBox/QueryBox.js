import React, { useState, useContext } from 'react';
import {UnControlled as CodeMirror} from 'react-codemirror2';
import Table from 'rc-table';
import {api_tbdgQuery} from 'core/services/tbdgQueryService';
import { GraphContext } from 'core/store/GraphContext';
import { helper_elapsedTimeMessage, helper_initializeTable } from 'containers/QueryBox/QueryBoxHelper';

const QueryBox = (props) => {
  const { query, setQuery } = useContext(GraphContext);
  const [ localQuery, setLocalQuery ] = useState(query);
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
      setQuery(localQuery);
      // api_tbdgQuery(this.state.chicos_query, this.handleResponse)
  }
  

  return (
    <div>
      <form id="query-form" method="post" onSubmit={handleSubmit}>
        <div className="form-group">
            {/* <label form="query">Query: </label> */}
            {/* <textarea class="form-control" id="query" name="query"></textarea> */}                        
          <CodeMirror
            value={query}
            options={{
              mode: 'application/x-cypher-query',
              theme: 'default',
              lineNumbers: true,
              height: 'auto'
            }}
            onChange={(editor, data, value) => {
              setLocalQuery(value);
            }}
          />
        </div>
        <button type="submit" className="btn btn-primary">Submit</button>
      </form>

      <div id="message">{message}</div>
      {result == null?'':result}
    </div>
  );
}
 
export default QueryBox;
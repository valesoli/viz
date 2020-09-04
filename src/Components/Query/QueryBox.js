import React from 'react';
import {UnControlled as CodeMirror} from 'react-codemirror2';
import {api_tbdgQuery} from 'Services/GraphService/tbdgQueryService';

class QueryBox extends React.Component{
    constructor(props){
        super(props);
        this.state = {        
          chicos_query: 'nada'
        }
        this.handleSubmit = this.handleSubmit.bind(this)
    }
    handleSubmit(event) {
        event.preventDefault();
        alert('A name was submitted: ' + this.state.chicos_query);
        //api_tbdgQuery()
    }    
    render(){
        return(
            <div>
                  <form id="query-form" method="post" onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        {/* <label form="query">Query: </label> */}
                        {/* <textarea class="form-control" id="query" name="query"></textarea> */}                        
                      <CodeMirror
                        value={this.state.chicos_query}
                        options={{
                          mode: 'application/x-cypher-query',
                          theme: 'default',
                          lineNumbers: true,
                          height: 'auto'
                        }}
                        onChange={(editor, data, value) => {
                          this.setState({chicos_query: value});
                        }}
                      />
                    </div>
                    <button type="submit" className="btn btn-primary">Submit</button>
                  </form>

                  <div id="message"></div>
                  <table id="query-result-table" className="table table-striped table-bordered"></table>
                </div>
        );
    }
}
export default QueryBox;
import React from 'react';
import {UnControlled as CodeMirror} from 'react-codemirror2';
import Table from 'rc-table';
import {api_tbdgQuery} from 'Services/GraphService/tbdgQueryService';

class QueryBox extends React.Component{
    constructor(props){
        super(props);
        this.state = {        
          chicos_query: 'select p\nmatch (p:Person)',
          message: '',
          result: null,
        }
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleResponse = this.handleResponse.bind(this);
    }
    handleResponse(response){
      let new_message = '';
      let new_result = null;
      if (response.success === true) {
          if (response.data.length === 0) {
              new_message = "No results found. " + this.helper_elapsedTimeMessage(response);
          }
          else {
              new_message = this.helper_elapsedTimeMessage(response);
              new_result = this.helper_initializeTable(response.data);
          }
      } else {
          new_message = response.message;
      }
      this.setState({message:new_message, result:new_result})
    }
    handleSubmit(event) {
        event.preventDefault();
        api_tbdgQuery(this.state.chicos_query, this.handleResponse)
    }
    helper_elapsedTimeMessage(response) {
        return "Translation time: " + response.translationTime + ". Execution time: " + response.executionTime
    }
    helper_initializeTable(data) {

      if (data.length === 0) {
          return;
      }
  
      //let table = $("#query-result-table");
      let columns = [];
      let columnNames = Object.keys(data[0]);
      for (let i in columnNames) {
          let dataColumn = columnNames[i];
  
          if (dataColumn.includes(".")) { // Escape dots, otherwise it treats it as objects
              dataColumn = dataColumn.replace(/\./g, "\\.")
          }
  
          columns.push({
              title: columnNames[i],
              dataIndex: dataColumn,
              key: dataColumn,
              width: 100
          });
      }
      //TODO: Poner este resultado un poco mas lindo
      data.forEach((val, index) => {data[index].key = index;data[index].p=JSON.stringify(data[index].p, null, 2)});
  
      // let dataTable = table.DataTable({
      //     autoWidth: false,
      //     data: data,
      //     columns: columns,
      //     columnDefs: [
      //         {
      //             "render": function (data, _type, _row) {
      //                 return '<pre>'+ JSON.stringify(data, null, 2) +'</pre>';
      //             },
      //             "targets": "_all"
      //         }
      //     ]
      // });
      // table.show();
      // dataTable.columns.adjust().draw();
      let table = <Table columns={columns} data={data}/>;
      console.log(columns);
      console.log(data);
      return table;
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

                  <div id="message">{this.state.message}</div>
                  {this.state.result == null?'':this.state.result}
                </div>
        );
    }
}
export default QueryBox;
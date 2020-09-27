import React from 'react';
import Table from 'rc-table';
import {api_tbdgQuery} from 'core/services/tbdgQueryService';

export const helper_elapsedTimeMessage = (response) => {
    return "Translation time: " + response.translationTime + ". Execution time: " + response.executionTime
}

export const helper_initializeTable = (data) => {

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
import { post } from "request";
var txUrl = "http://localhost:7474/db/data/transaction/commit";

const NEO_USER = 'neo4j';
const NEO_PASS = 'admin';

function cypher(query,params,cb) {
  post({
        uri:txUrl,
        json:{statements:[{statement:query,parameters:params}]},
        headers: {'Accept':'application/json;charset=UTF-8',
                    'Authorization':'Basic ' + btoa(NEO_USER + ':' + NEO_PASS),
                    'Content-Type':'application/json'}},
        function(err,res) { cb(err,res.body) })
}

var params={limit: 10}
var cb=function(err,data) { console.log(JSON.stringify(data)) }

export function consoleLogCypher(query){ return cypher(query,params,cb); }



import { post } from "request";
var txUrl = "http://localhost:7474/db/data/transaction/commit";

const NEO_USER = 'neo4j';
const NEO_PASS = 'admin';

//Deprecated
function cypherQuery(query,params, processResponse) {
  post({
        uri:txUrl,
        json: {statements:[{statement:query,parameters:params}]},
        headers: {'Accept':'application/json;charset=UTF-8',
                    'Authorization':'Basic ' + btoa(NEO_USER + ':' + NEO_PASS),
                    'Content-Type':'application/json'}
        }, function(err,res) { processResponse(err,res.body) }
  )
}
var cb=function(err,data) { console.log(JSON.stringify(data)) }
export function consoleLogCypher(query){ 
    var params={};
    return cypherQuery(query,params,cb);
}
export function fetchEveryYear(callback){
    var params={};
    cypherQuery(
        "match (o:Object) with distinct toInteger(split(o.interval[0], '—')[0]) as Años order by Años return collect(Años)",
        params,
        callback
    )
}

//Esta es la que vá
export function api_getYears(callback){
    let query = "match (o:Object) with distinct toInteger(split(o.interval[0], '—')[0]) as Años order by Años return collect(Años)";
    let params = {};
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function() {
        if(this.readyState !==4){
            return;
        }
        if (this.status === 200) {
            let year = JSON.parse(this.responseText);
            callback(year);
        } else {
            callback(null);
        }
    };
    xhttp.open("POST", txUrl, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.setRequestHeader("Authorization", "Basic "+ btoa(NEO_USER + ':' + NEO_PASS));
    xhttp.setRequestHeader("Data-type", "json");
    xhttp.send(JSON.stringify({
        statements:[{statement:query,parameters:params}]
        }));
}
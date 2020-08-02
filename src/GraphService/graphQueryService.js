var txUrl = "http://localhost:7474/db/data/transaction/commit";
const NEO_USER = 'neo4j';
const NEO_PASS = 'admin';

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

export function api_getNodesAndEdges(callback){
    let query = "match (n) with collect(id(n)) as nodes match (m)-[r]->(o) with nodes, collect([id(m),id(o)]) as edges return nodes, edges";
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

export function api_cypherQuery(queryParam, callback){
    let query = queryParam;
    let params = {};
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function() {
        if(this.readyState !==4){
            return;
        }
        if (this.status === 200) {
            let results = JSON.parse(this.responseText);
            callback(results);
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
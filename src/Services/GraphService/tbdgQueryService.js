export function api_tbdgQuery(query, callback){
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
        return true;
    };
    xhttp.open("POST", "http://localhost:7474/query", true);
    xhttp.setRequestHeader("Content-type", "application/json");
    // xhttp.setRequestHeader("Authorization", "Basic "+ btoa(con_config.NEO_USER + ':' + con_config.NEO_PASS));
    xhttp.setRequestHeader("Data-type", "json");
    xhttp.send(JSON.stringify({
        statements:[query.serialize()]
        }));        
}
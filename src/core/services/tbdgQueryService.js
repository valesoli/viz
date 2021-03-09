export function api_tbdgQuery(query, callback){
    let xhttp = new XMLHttpRequest();
    let the_query = encodeURI(query)

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
    xhttp.open("POST", "http://localhost:7000/query", true);
    xhttp.setRequestHeader("Content-type", "application/json");
    // xhttp.setRequestHeader("Authorization", "Basic "+ btoa(con_config.NEO_USER + ':' + con_config.NEO_PASS));
    xhttp.setRequestHeader("Data-type", "json");
    xhttp.send("query="+the_query);        
}
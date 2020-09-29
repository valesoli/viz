import axios from 'axios';

export const fetchNeoQuery = async (key, connectionConfig, inputQuery) => {
    const query = inputQuery;
    const config = {
        method: 'post',
        url: connectionConfig.url,
        headers: { 
            "Content-type": "application/json",
            "Authorization": "Basic "+ btoa(connectionConfig.user + ':' + connectionConfig.pass),
            "Content-type": "application/json"
        },
        data: {
            "statements":[{"statement":query}]
        }
    }

    let response = await axios(config);
    return response;
}

export const tryConnection = async (connectionUrl, user, pass) => {
    let query = "match (o:Object) with distinct toInteger(split(o.interval[0], '—')[0]) as Años order by Años return collect(Años)";
    const config = {
        method: 'post',
        url: connectionUrl,
        headers: { 
            "Content-type": "application/json",
            "Authorization": "Basic "+ btoa(user + ':' + pass),
            "Content-type": "application/json"
        },
        data: {
            "statements":[{"statement":query}]
        }
    }
    let response = await axios(config);
    return response; 
}
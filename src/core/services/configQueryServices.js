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
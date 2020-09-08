import axios from 'axios';

export function useAuthorizationRequest() {

    const request = () => {
        const token = localStorage.getItem('jwt-token');
        let headers = {};

        if (token !== null) headers = {
            Authorization: token
        };
        return axios.create({
            headers: headers
        })
    };

    return {request};

}
import { IUser } from "@/types/schema";
import { useMutation } from "@tanstack/react-query";


const createUser = async (user:IUser) => {
    const response = await fetch('http://localhost:8080/api/v1/users', {
        method: 'POST',
        headers: {
        'accept': '*/*',
        'Authorization': `Bearer  ${localStorage.getItem('qtToken')}`,
        'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    });
    
    return await response.json();
}

export const useCreateUser = () => {
    return useMutation({
        mutationFn: createUser
    })
}


const updateUser = async (user:IUser) => {
    const response = await fetch(`http://localhost:8080/api/v1/users/${user.id}`, {
        method: 'PUT',
        headers: {
        'accept': '*/*',
        'Authorization': `Bearer  ${localStorage.getItem('qtToken')}`,
        'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    });
    
    return await response.json();
}

export const useUpdateUser = () => {
    return useMutation({
        mutationFn: updateUser
    })
}
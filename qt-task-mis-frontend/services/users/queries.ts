import { ApiResponse, EUserStatus, IUser, Optional } from "@/types/schema";
import { useQuery } from "@tanstack/react-query";


interface FetchUsersOptions { page: number, limit: number,q:string,status:EUserStatus }

const fetchUsers = async ({page,limit,q,status}:Optional<FetchUsersOptions>) => {

  // build url with params
  let url = new URL("http://localhost:8080/api/v1/users");
  let params = new URLSearchParams();
  page && params.append("page", page.toString());
  limit && params.append("limit", limit.toString());
  q && params.append("q", q);
  status && params.append("status", status.toString());

  url.search = params.toString();


    const response = await fetch(url, {
      headers: {
        'accept': '*/*',
        'Authorization': `Bearer  ${localStorage.getItem('qtToken')}`,
        'Content-Type': 'application/json'
      },
    });
  
    return await response.json();
  };


export const useGetUsers = ({ page, limit,q,status }: Optional<FetchUsersOptions> ) => {
    return useQuery<ApiResponse<IUser>>({queryKey: ['users', {page, limit,q,status}], queryFn: () => fetchUsers({page, limit,q,status})});
  };


const fetchUser = async (id:string) => {
  const response = await fetch(`http://localhost:8080/api/v1/users/${id}`, {
    headers: {
      'accept': '*/*',
      'Authorization': `Bearer  ${localStorage.getItem('qtToken')}`,
      'Content-Type': 'application/json'
    },
  });

  return await response.json();
}

export const useGetUser = (id:string) => {
  return useQuery({
    queryKey: ['user', id],
    queryFn: () => fetchUser(id),
  })
}
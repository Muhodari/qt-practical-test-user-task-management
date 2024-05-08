
import { ApiResponse, IProject } from "@/types/schema";
import { useMutation, useQuery } from "@tanstack/react-query";

const getProjects = async (page: number, limit: number) => {
    const response = await fetch(`http://localhost:8080/api/v1/projects/search?page=${page}&limit=${limit}`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('qtToken')}`,
            'accept': '*/*',
            'Content-Type': 'application/json'
        }
});

    return await response.json();
}

export const useGetProject = (id:string) => {
    const project = useQuery<IProject>({
      queryKey: ["task",id],
      queryFn: () => getProject(id),
    });
  
    return project;
  }

  async function getProject(id: string): Promise<IProject> {
    console.log('get project')
    const res =  await fetch(`http://localhost:8080/api/v1/projects/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem("qtToken")}`,
        'Content-Type': 'application/json',
        'Accept': '*/*'
      }
    })
    return await res.json()
  }
  

export const useGetProjects = ({ page, limit }: { page: number, limit: number }) => {
    return useQuery<ApiResponse<IProject>>({queryKey: ['projects', {page, limit}], queryFn: () => getProjects(page, limit)});
}


const updateProject = async (projectData: IProject) : Promise<IProject> => {
    const response = await fetch(`http://localhost:8080/api/v1/projects/${projectData.id}`, {
      method: 'PUT',
      headers: {
        'accept': '*/*',
        'Authorization': `Bearer ${localStorage.getItem("qtToken")}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(projectData)
    });
  
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
  
    return await response.json();
  }
  
  export function useUpdateProject() {
    return useMutation({
      mutationFn: updateProject,
    });
  }
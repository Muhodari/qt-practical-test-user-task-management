import { IProject } from "@/types/schema";
import { useMutation } from "@tanstack/react-query";




const createProject = async (projectData: IProject) : Promise<IProject> => {
  const response = await fetch('http://localhost:8080/api/v1/projects', {
    method: 'POST',
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
};

export function useCreateProject() {
  return useMutation({
    mutationFn: createProject,
  });
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
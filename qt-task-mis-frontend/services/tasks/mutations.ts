import { ITask } from "@/types/schema";
import { useMutation } from "@tanstack/react-query";




const createTask = async (taskData: ITask) : Promise<ITask> => {
  const response = await fetch('http://localhost:8080/api/v1/tasks', {
    method: 'POST',
    headers: {
      'accept': '*/*',
      'Authorization': `Bearer ${localStorage.getItem("qtToken")}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(taskData)
  });

  if (!response.ok) {
    throw new Error('Network response was not ok');
  }

  return await response.json();
};

export function useCreateTask() {
  return useMutation({
    mutationFn: createTask,
  });
}


const updateTask = async (taskData: ITask) : Promise<ITask> => {
  const response = await fetch(`http://localhost:8080/api/v1/tasks/${taskData.id}`, {
    method: 'PUT',
    headers: {
      'accept': '*/*',
      'Authorization': `Bearer ${localStorage.getItem("qtToken")}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(taskData)
  });

  if (!response.ok) {
    throw new Error('Network response was not ok');
  }

  return await response.json();
}

export function useUpdateTask() {
  return useMutation({
    mutationFn: updateTask,
  });
}
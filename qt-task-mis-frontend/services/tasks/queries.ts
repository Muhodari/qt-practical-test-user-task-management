import { ApiResponse, EPriority, EStatus, Optional, ITask, UnSortedApiResponse } from "@/types/schema";
import { useQuery } from "@tanstack/react-query";

type FetchTaskOptions = {
    q: string
    page: number
    limit: number
    sort: string
    status: EStatus
    priority: EPriority
}

async function fetchTasks({q,priority,status,page,limit}: Optional<FetchTaskOptions> = {}): Promise<ApiResponse<ITask>> {

  let url = new URL("http://localhost:8080/api/v1/tasks");
  let params = new URLSearchParams();

  q && params.append("q", q);
  priority && params.append("priority", priority);
  status && params.append("status", status);
  page && params.append("page", page.toString());
  limit && params.append("limit", limit.toString());

  url.search = params.toString();

  const  response = await fetch(url, {
      headers: {
          'Authorization': `Bearer ${localStorage.getItem("qtToken")}`,
          'Content-Type': 'application/json',
          'Accept': '*/*'
      }
  }); 

  return await response.json();
}

export const useGetTasks = (options:Optional<FetchTaskOptions>) => {
  const tasks = useQuery<ApiResponse<ITask>>({
    queryKey: ["tasks",{...options}],
    queryFn: () => fetchTasks(options),
  });

  return tasks;
}


async function getTask(id: string): Promise<ITask> {
  console.log('get Task')
  const res =  await fetch(`http://localhost:8080/api/v1/tasks/${id}`, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem("qtToken")}`,
      'Content-Type': 'application/json',
      'Accept': '*/*'
    }
  })
  
  return await res.json()
}

export const useGetTask = (id:string) => {
  const tasks = useQuery<ITask>({
    queryKey: ["task",id],
    queryFn: () => getTask(id),
  });

  return tasks;
}

export async function downloadTasksFile({q,priority,status,page,limit}: Optional<FetchTaskOptions> = {}) {
  let url = new URL("http://localhost:8080/api/v1/tasks/download");
  let params = new URLSearchParams();
  q && params.append("q", q);
  priority && params.append("priority", priority);
  status && params.append("status", status);
  page && params.append("page", page.toString());
  limit && params.append("limit", limit.toString());

  url.search = params.toString();

  const res = await fetch(url, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem("qtToken")}`,
      'Accept': '*/*'
    }
  })
  const blob = await res.blob();

  const blobUrl = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = blobUrl;
  link.download = 'tasks.xlsx';
  document.body.appendChild(link);
  link.click();

  // Remove the link when done
  document.body.removeChild(link);
}


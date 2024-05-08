import { z } from 'zod';

export const userSchema = z.object({
  id: z.string().optional(),
  firstName: z.string().min(3, 'First name must be at least 3 characters'),
  lastName: z.string().min(3, 'Last name must be at least 3 characters'),
  emailAddress: z.string().email('Invalid email address'),
  phoneNumber: z.string().min(10, 'Phone number must be at least 10 characters'),
  gender: z.string(),
  status: z.string(),
  fullName: z.string().optional(),
  deletedFlag: z.boolean().optional(),
  lastLogin: z.string().optional(),
  createdAt: z.string().optional(),
  password: z.string().min(4,"Password should be atleast 4 characters").optional(),
});

export const projectSchema = z.object({
  id: z.string(),
  name: z.string(),
  description: z.string(),
  status: z.string(),
  authority: z.string(),
});

export const taskSchema = z.object({
  id: z.string().optional(),
  name: z.string().min(5, 'Task name must be at least 5 characters'),
  description: z.string().min(100, 'Task description must be at least 100 characters').max(200, 'Task description must be at most 200 characters'),
  status: z.string(),
  priority: z.string(),
  startDate: z.date(),
  endDate: z.date(),
  projectsId: z.array(z.string()).optional(),
  selectedProjectId:z.array(z.object({value:z.string(),label:z.string()})).optional(),
  projects: z.array(projectSchema).optional(),
  assigneesId: z.array(z.string()).optional(),
  selectedAssigneesId:z.array(z.object({value:z.string(),label:z.string()})).optional(),
  assignees: z.array(userSchema).optional(),
});

// Infer the types
export type IUser = z.infer<typeof userSchema>;
export type IProject = z.infer<typeof projectSchema>;
export type ITask = z.infer<typeof taskSchema>;

export  interface ApiResponse<T> {
    data: PageData<T>;
    message: string;
    status: string;
    timestamp: string;
  }

export interface UnSortedApiResponse<T> {
    data: T;
    message: string;
    status: string;
    timestamp: string;
  }
  
export  interface PageData<T> {
    content: T[];
    pageable: Pageable;
    totalPages: number;
    totalElements: number;
    last: boolean;
    size: number;
    number: number;
    sort: Sort;
    numberOfElements: number;
    first: boolean;
    empty: boolean;
  }
  
  interface Pageable {
    sort: Sort;
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    unpaged: boolean;
  }
  
  interface Sort {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  }
  

export  enum EStatus {
    ACTIVE = "ACTIVE",
    INACTIVE = "INACTIVE",
    DELETED = "DELETED",
  }

export  enum EPriority {
    LOW = "LOW",
    MEDIUM = "MEDIUM",
    HIGH = "HIGH",
  }


export enum EUserStatus {
  PENDING, ACTIVE, INACTIVE, DELETED,LOGIN_LOCKED,ADMIN_LOCKED
}

export type Optional<T> = {
  [P in keyof T]?: T[P];
};
"use client"


import { useGetProjects } from "@/services/projects"
import { useGetUsers } from "@/services/users"
import { useMemo } from "react"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { ITask, taskSchema } from "@/types/schema"
import { zodResolver } from "@hookform/resolvers/zod"
import { addDays, format } from "date-fns"

import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
  } from "@/components/ui/form"
import { MultiSelect } from "./ui/multi-select"
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover"
import { cn } from "@/lib/utils"
import { Calendar } from "./ui/calendar"
import { Input } from "./ui/input"
import { Button } from "./ui/button"
import { CalendarIcon } from "@radix-ui/react-icons"
import { Textarea } from "./ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select"
import { priorities } from "@/lib/data"
import { useCreateTask, useGetTask, useUpdateTask } from "@/services/tasks"
import { useToast } from "./ui/use-toast"

interface UpdateTaskProps{
    task:ITask
}

export function UpdateTask({task}: UpdateTaskProps) {

    const updateTask = useUpdateTask();
    const {toast} = useToast()

    const userData = useGetUsers({page:1,limit:100})
    const projectData = useGetProjects({page:1,limit:100})

    const users = useMemo(() => userData?.data?.data.content.map(user => ({
        value:user.id,
        label:user.fullName
    })) ?? [], [userData.data])

    const defaultUsers  = useMemo(() =>  task.assignees?.map(user => ({
        value:user.id,
        label:user.fullName
    })), [task])

    const projects = useMemo(() => projectData?.data?.data.content.map(project => ({
        value:project.id,
        label:project.name
    })) ?? [], [projectData.data])

    const defaultProjects = useMemo(() => task.projects?.map(project => ({
        value:project.id,
        label:project.name
    })), [task])

    const now = new Date();

    
    
  const taskForm = useForm<z.infer<typeof taskSchema>>({
    resolver: zodResolver(taskSchema),
    defaultValues: {
        id: task?.id,
        name: task?.name ,
        description: task?.description ,
        priority: task?.priority ,
        status: task?.status,
        projectsId: [],
        assigneesId: [],
        selectedAssigneesId:defaultUsers,
        selectedProjectId:defaultProjects,
        startDate: new Date(task?.startDate ??  now),
        endDate: new Date(task?.endDate ?? addDays(now, 7)) ,
    },
  })

    // 2. Define a submit handler.
    function onSubmit(values: z.infer<typeof taskSchema>) {

        let synthetizedValues = {
            ...values,
            projectsId: values.selectedProjectId?.map(proj => proj.value),
            assigneesId: values.selectedAssigneesId?.map(assignee => assignee.value),
        }

        updateTask.mutate(synthetizedValues,{
            onSuccess: ()=>{
                toast({
                    title:"Task Updated successfully"
                })
            },
            onError: (err)=>{
                toast({
                    title:err.message
                })
            }
        })
      }

  return (
        <Form {...taskForm}>
            <form onSubmit={taskForm.handleSubmit(onSubmit)} className=" space-y-8">
                <FormField
                    control={taskForm.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Task Name</FormLabel>
                            <FormControl>
                                <Input placeholder="Review project status..." {...field} defaultValue={field.value} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <div className="grid grid-cols-2 gap-4">
                    <FormField
                        control={taskForm.control}
                        name="selectedProjectId"
                        render={({ field: { ...field } }) => (
                            <FormItem className="mb-5">
                            <FormLabel>Projects</FormLabel>
                            <MultiSelect
                                selected={field.value}
                                options={projects}
                                {...field}
                                />
                                <FormMessage />
                            </FormItem>
                            
                        )}
                    />
                    <FormField
                        control={taskForm.control}
                        name="selectedAssigneesId"
                        render={({ field: { ...field } }) => (
                            <FormItem className="mb-5">
                            <FormLabel>Assignees</FormLabel>
                            <MultiSelect
                                selected={field.value}
                                options={users}
                                {...field} />
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                </div>

                    <FormField
                        control={taskForm.control}
                        name="description"
                        render={({ field }) => (
                            <FormItem>
                            <FormLabel>Task Description</FormLabel>
                            <FormControl>
                                <Textarea
                                placeholder="Provide a detailed description of the task...."
                                className="resize-none"
                                {...field}
                                />
                            </FormControl>
                            <FormDescription>
                                <div className="flex justify-between">
                                    <p>You can <span>@mention</span> other project and assignees.</p>
                                    <p>{field.value?.length}/200</p>
                                </div>
                            </FormDescription>
                            <FormMessage />
                            </FormItem>
                        )}
                        />
   
   <FormField
          control={taskForm.control}
          name="priority"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Priority</FormLabel>
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="What is the priority" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {priorities.map((priority) => (
                    <SelectItem key={priority.value} value={priority.value}>
                      {priority.label}
                    </SelectItem>
                  ))}
                  
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

                    <FormField
                        control={taskForm.control}
                        name="startDate"
                        render={({ field }) => (
                            <FormItem className="flex flex-col">
                            <FormLabel>Start</FormLabel>
                            <Popover>
                                <PopoverTrigger asChild>
                                <FormControl>
                                    <Button
                                    variant={"outline"}
                                    className={cn(
                                        "w-[240px] pl-3 text-left font-normal",
                                        !field.value && "text-muted-foreground"
                                    )}
                                    >
                                    {field.value ? (
                                        format(new Date(field.value), "PPP")
                                    ) : (
                                        <span>Pick a date</span>
                                    )}
                                    <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                                    </Button>
                                </FormControl>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto p-0" align="start">
                                <Calendar
                                    mode="single"
                                    selected={field.value}
                                    onSelect={field.onChange}
                                    disabled={(date) =>
                                            date > new Date() || date < new Date("1900-01-01")
                                            }
                                            initialFocus
                                        />
                                        </PopoverContent>
                                    </Popover>
                                    <FormDescription>
                                        When the task starts.
                                    </FormDescription>
                                    <FormMessage />
                                    </FormItem>
                                )}
                    />
                    <FormField
                        control={taskForm.control}
                        name="endDate"
                        render={({ field }) => (
                            <FormItem className="flex flex-col">
                            <FormLabel>End</FormLabel>
                            <Popover>
                                <PopoverTrigger asChild>
                                <FormControl>
                                    <Button
                                    variant={"outline"}
                                    className={cn(
                                        "w-[240px] pl-3 text-left font-normal",
                                        !field.value && "text-muted-foreground"
                                    )}
                                    >
                                    {field.value ? (
                                        format(new Date(field.value), "PPP")
                                    ) : (
                                        <span>Pick a date</span>
                                    )}
                                    <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                                    </Button>
                                </FormControl>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto p-0" align="start">
                                <Calendar
                                    mode="single"
                                    selected={field.value}
                                    onSelect={field.onChange}
                                    disabled={(date) =>
                                            date > new Date() || date < new Date("1900-01-01")
                                            }
                                            initialFocus
                                        />
                                        </PopoverContent>
                                    </Popover>
                                    <FormDescription>
                                        When the task starts.
                                    </FormDescription>
                                    <FormMessage />
                                    </FormItem>
                                )}
                    />
              <Button type="submit">Update</Button>

            </form>
        </Form>

  )
}
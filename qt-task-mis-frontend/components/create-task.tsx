"use client"


import { useGetProjects } from "@/services/projects"
import { useGetUsers } from "@/services/users"
import { useMemo } from "react"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { taskSchema } from "@/types/schema"
import { zodResolver } from "@hookform/resolvers/zod"
import { addDays, format } from "date-fns"
import { Label } from "@/components/ui/label"

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
import { useCreateTask } from "@/services/tasks"
import { useToast } from "./ui/use-toast"
import { useQueryClient } from "@tanstack/react-query"

export function CreateTaskForm() {

    const createTask = useCreateTask();
    const {toast} = useToast()

    const queryClient = useQueryClient()

    const userData = useGetUsers({page:1,limit:100})
    const projectData = useGetProjects({page:1,limit:100})

    const users = useMemo(() => userData?.data?.data.content.map(user => ({
        value:user.id,
        label:user.fullName
    })) ?? [], [userData.data])
    const projects = useMemo(() => projectData?.data?.data.content.map(project => ({
        value:project.id,
        label:project.name
    })) ?? [], [projectData.data])

    const now = new Date();

    
  const form = useForm<z.infer<typeof taskSchema>>({
    resolver: zodResolver(taskSchema),
    defaultValues: {
        name: "",
        description: "",
        priority: 'NORMAL',
        status:'ACTIVE',
        projectsId: [],
        assigneesId: [],
        selectedAssigneesId:[],
        selectedProjectId:[],
        startDate: now,
        endDate: addDays(now, 7),
    },
  })

    function onSubmit(values: z.infer<typeof taskSchema>) {

        let synthetizedValues = {
            ...values,
            projectsId: values.selectedProjectId?.map(proj => proj.value),
            assigneesId: values.selectedAssigneesId?.map(assignee => assignee.value),
        }

        createTask.mutate(synthetizedValues,{
            onSuccess: ()=>{
                toast({
                    title:"Task created successfully"
                })

                queryClient.invalidateQueries({ queryKey: ['users'] })
            },
            onError: (err)=>{
                toast({
                    title:err.message
                })
            }
        })
      }

  return (
   
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="container space-y-8">
                <FormField
                    control={form.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Task Name</FormLabel>
                            <FormControl>
                                <Input placeholder="Review project status..." {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                  
                  <div className="flex w-full">
                  <div className="flex-1 flex items-center justify-center">
     
                        <FormField
                        control={form.control}
                        name="startDate"
                        render={({ field }) => (
                            <FormItem className="">
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
                                    <FormMessage />
                                    </FormItem>
                                )}
                    />
                    </div>
                    <div className="flex-1 flex items-center justify-center">
                    <FormField
                        control={form.control}
                        name="endDate"
                        render={({ field }) => (
                            <FormItem className="">
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
                                    <FormMessage />
                                    </FormItem>
                                )}
                    />
                
                    </div>
                    </div>

                                                  

                    <FormField
                        control={form.control}
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

                    <FormField
                        control={form.control}
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
                        control={form.control}
                        name="description"
                        render={({ field }) => (
                            <FormItem>
                            <FormLabel>Description</FormLabel>
                            <FormControl>
                                <Textarea
                                placeholder="Provide a detailed description of the task...."
                                className="resize-none"
                                {...field}
                                />
                            </FormControl>
                            <FormDescription>
                                <div className="flex justify-between">
                                    <p>{field.value.length}/30</p>
                                </div>
                            </FormDescription>
                            <FormMessage />
                            </FormItem>
                        )}
                        />
   
   <FormField
          control={form.control}
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
                    
              <Button type="submit">Submit</Button>

            </form>
        </Form>

  )
}
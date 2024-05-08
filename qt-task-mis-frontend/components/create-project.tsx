"use client"


import { useGetProjects } from "@/services/projects"
import { useGetUsers } from "@/services/users"
import { useMemo } from "react"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { projectSchema } from "@/types/schema"
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
import { useCreateTask } from "@/services/tasks"
import { useToast } from "./ui/use-toast"
import { useQueryClient } from "@tanstack/react-query"
import { useCreateProject } from "@/services/projects/mutations"

export function CreateProjectForm() {

    const createProject = useCreateProject();
    const {toast} = useToast()

    const queryClient = useQueryClient()

    const userData = useGetUsers({page:1,limit:100})
    const projectData = useGetProjects({page:1,limit:100})

    const users = useMemo(() => userData?.data?.data.content.map(user => ({
        value:user.id,
        label:user.fullName
    })) ?? [], [userData.data])

    
  const form = useForm<z.infer<typeof projectSchema>>({
    resolver: zodResolver(projectSchema),
    defaultValues: {
        name: "",
        description: "",
        status:"ACTIVE",
        authority:""
    },
  })

    function onSubmit(values: z.infer<typeof projectSchema>) {
        console.log("in the submiit function")

    

        createProject.mutate(values,{
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
            <form onSubmit={form.handleSubmit(onSubmit)} className=" space-y-8">
                <FormField
                    control={form.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>project Name</FormLabel>
                            <FormControl>
                                <Input placeholder="provide project name...." {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

            
            

                       <FormField
                        control={form.control}
                        name="description"
                        render={({ field }) => (
                            <FormItem>
                            <FormLabel>project Description</FormLabel>
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
                                    <p>{field.value.length}/200</p>
                                </div>
                            </FormDescription>
                            <FormMessage />
                            </FormItem>
                        )}
                        
                        />
              <Button type="submit">Submit</Button>

            </form>
        </Form>

  )
}
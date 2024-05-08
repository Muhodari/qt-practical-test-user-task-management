'use client'

import { CreateTaskForm } from "@/components/create-task"
import { DataTable } from "@/components/data-table"
import { DataTableColumnHeader } from "@/components/data-table-column-header"
import { DataTableRowActions } from "@/components/data-table-row-actions"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import { Dialog, DialogTrigger, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog"
import { useToast } from "@/components/ui/use-toast"
import { statuses, priorities } from "@/lib/data"
import { useGetProjects,useUpdateProject} from "@/services/projects"
import { EPriority, EStatus, IProject } from "@/types/schema"
import { DownloadIcon } from "@radix-ui/react-icons"
import { ColumnDef } from "@tanstack/react-table"
import { useRouter } from "next/navigation"
import { useMemo, useState } from "react"


  


export default function Projects(){
    const router = useRouter()
    const {toast} = useToast();
    const [page, setPage] = useState(1)
    const [limit, setLimit] = useState(100)
    const [q, setQ] = useState('')
    const [status, setStatus] = useState<EStatus>()
    const [priority, setPriority] = useState<EPriority>()

    const projectsData = useGetProjects({page,limit})
    const updateProject = useUpdateProject();

    const columns: ColumnDef<IProject>[] = [
      {
        id: "select",
        header: ({ table }) => (
          <Checkbox
            checked={
              table.getIsAllPageRowsSelected() ||
              (table.getIsSomePageRowsSelected() && "indeterminate")
            }
            onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
            aria-label="Select all"
            className="translate-y-[2px]"
          />
        ),
        cell: ({ row }) => (
          <Checkbox
            checked={row.getIsSelected()}
            onCheckedChange={(value) => row.toggleSelected(!!value)}
            aria-label="Select row"
            className="translate-y-[2px]"
          />
        ),
        enableSorting: false,
        enableHiding: false,
      },
      {
        accessorKey: "name",
        header: ({ column }) => (
          <DataTableColumnHeader column={column} title="Name" />
        ),
        cell: ({ row }) => {
  
          return (
            <div className="flex space-x-2">
              <span className="max-w-[500px] truncate font-medium">
                {row.getValue("name")}
              </span>
            </div>
          )
        },
      },
      {
          accessorKey: "description",
          header: ({ column }) => (
            <DataTableColumnHeader column={column} title="description" />
          ),
          cell: ({ row }) => {
    
            return (
              <div className="flex space-x-2">
                <span className="max-w-[500px] truncate font-medium">
                  {row.getValue("description")}
                </span>
              </div>
            )
          },
        },
      {
        accessorKey: "status",
        header: ({ column }) => (
          <DataTableColumnHeader column={column} title="Status" />
        ),
        cell: ({ row }) => {
          const status = statuses.find(
            (status) => status.value === row.getValue("status")
          )
    
          if (!status) {
            return null
          }
    
          return (
            <div className="flex w-[100px] items-center">
              {status.icon && (
                <status.icon className="mr-2 h-4 w-4 text-muted-foreground" />
              )}
              <span>{status.label}</span>
            </div>
          )
        },
        filterFn: (row, id, value) => {
          return value.includes(row.getValue(id))
        },
      },

      {
        accessorKey: "authority",
        header: ({ column }) => (
          <DataTableColumnHeader column={column} title="authority" />
        ),
        cell: ({ row }) => {
  
          return (
            <div className="flex space-x-2">
              <span className="max-w-[500px] truncate font-medium">
                {row.getValue("authority")}
              </span>
            </div>
          )
        },
      },

      {
        id: "actions",
        cell: ({ row }) => <DataTableRowActions onAction={handleOnAction as (action:string,data:IProject) => void} row={row} />,
      },
    ]

    function handleOnAction(action: 'EDIT' | 'DELETE' | 'COMPLETE', data: IProject){
      switch(action){
        case 'EDIT':
          router.push(`/dashboard/projects/${data.id}`);
          break;
        case 'DELETE':
            updateProject.mutate({...data, status: EStatus.INACTIVE}, {
              onSuccess: () => {
                toast({
                  title:'project Deleted',
                })
                projectsData.refetch();
              },
              onError: () => toast({
                title:'Error Deleting project',
              }),
            })
          break;
        case 'COMPLETE':
          updateProject.mutate({...data, status: EStatus.DELETED}, {
            onSuccess: () => {
              toast({
                title:'project Completed',
              })
              projectsData.refetch();
            },
            onError: () => toast({
              title:'Error Completing project',
            }),
          })
          break;
        default:
          break;
      }
    }


    const tasks = useMemo(() => projectsData.data?.data,[projectsData.data?.data])

    // async function handleDownload(){
    //   await downloadTasksFile({page, limit,q,status,priority})
    // }

    return(
        <div>

            <div className="flex justify-between pb-6 pt-4">
                <h1>Projects</h1>
                
                <div className="flex space-x-4 items-center">
                <Button onClick={() => router.push('/dashboard/projects/new')} className="bg-blue-900">Create Project</Button>
                </div>
            </div>
        
            {tasks && <DataTable pagination={tasks} columns={columns}
              onSearch={value => setQ(value)}
              onLimitChange={value => setLimit(parseInt(value))}
              onPageIndexChange={value => setPage(value+1)}
              onStatusChange={value => setStatus(value[0] as EStatus)}
              onPriorityChange={value => setPriority(value[0] as EPriority)}
            />}
        </div>
    )
}
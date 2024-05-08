

'use client'

import { CreateUserForm } from "@/components/create-user"
import { DataTable } from "@/components/data-table"
import { DataTableColumnHeader } from "@/components/data-table-column-header"
import { DataTableRowActions } from "@/components/data-table-row-actions"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import { Dialog, DialogTrigger, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog"
import { useToast } from "@/components/ui/use-toast"
import { statuses, priorities, userStatuses } from "@/lib/data"
import { useGetUsers, useUpdateUser } from "@/services/users"
import { EPriority, EStatus, EUserStatus, IUser } from "@/types/schema"
import { ColumnDef } from "@tanstack/react-table"
import { useRouter } from "next/navigation"
import { useMemo, useState } from "react"
import {  formatRelative, subDays } from 'date-fns'


  


export default function Users(){
    const router = useRouter()
    const {toast} = useToast();
    const [page, setPage] = useState(1)
    const [limit, setLimit] = useState(100)
    const [q, setQ] = useState('')
    const [status, setStatus] = useState<EUserStatus>()

    const usersData = useGetUsers({page, limit,q,  status})
    const updateUser = useUpdateUser();

    const columns: ColumnDef<IUser>[] = [
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
        accessorKey: "fullName",
        header: ({ column }) => (
          <DataTableColumnHeader column={column} title="Names" />
        ),
        cell: ({ row }) => {
  
          return (
            <div className="flex space-x-2">
              <span className="max-w-[500px] truncate font-medium">
                {row.getValue("fullName")}
              </span>
            </div>
          )
        },
      },
      {
          accessorKey: "emailAddress",
          header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Email" />
          ),
          cell: ({ row }) => {
    
            return (
              <div className="flex space-x-2">
                <span className="max-w-[500px] truncate font-medium">
                  {row.getValue("emailAddress")}
                </span>
              </div>
            )
          },
        },
        {
            accessorKey: "phoneNumber",
            header: ({ column }) => (
              <DataTableColumnHeader column={column} title="Phone" />
            ),
            cell: ({ row }) => {
      
              return (
                <div className="flex space-x-2">
                  <span className="max-w-[500px] truncate font-medium">
                    {row.getValue("phoneNumber")}
                  </span>
                </div>
              )
            },
          },
        {
            accessorKey: "gender",
            header: ({ column }) => (
              <DataTableColumnHeader column={column} title="Gender" />
            ),
            cell: ({ row }) => {
      
              return (
                <div className="flex space-x-2">
                  <span className="max-w-[500px] truncate font-medium">
                    {row.getValue("gender")}
                  </span>
                </div>
              )
            },
          },
          {
            accessorKey: "lastLogin",
            header: ({ column }) => (
              <DataTableColumnHeader column={column} title="Last Login" />
            ),
            cell: ({ row }) => {
      
              return (
                <div className="flex space-x-2">
                  <span className="max-w-[500px] truncate font-medium">
                    {row.getValue("lastLogin")? formatRelative(new Date(row.getValue("lastLogin")),new Date()) : 'never'}
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
          const status = userStatuses.find(
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
        id: "actions",
        cell: ({ row }) => <DataTableRowActions onAction={handleOnAction as (action:string,data:IUser) => void} row={row} />,
      },
    ]

    function handleOnAction(action: 'EDIT' | 'DELETE' | 'COMPLETE', data: IUser){
      switch(action){
        case 'EDIT':
          router.push(`/dashboard/users/${data.id}`);
          break;
        case 'DELETE':
            updateUser.mutate({...data, status: EStatus.DELETED}, {
              onSuccess: () => {
                toast({
                  title:'User Deleted',
                })
                usersData.refetch();
              },
              onError: () => toast({
                title:'Error Deleting User',
              }),
            })
          break;
        default:
          break;
      }
    }


    const users = useMemo(() => usersData.data?.data,[usersData.data?.data])
    return(
        <div>

            <div className="flex justify-between pb-6 pt-4">
                <h1>Users</h1>
                
                <Dialog>
                  <DialogTrigger><Button className="bg-blue-900">Create User</Button></DialogTrigger>
                  <DialogContent>
                    <DialogHeader>
                      <DialogTitle>Create a new User</DialogTitle>
                      <DialogDescription>
                        <CreateUserForm />
                      </DialogDescription>
                    </DialogHeader>
                  </DialogContent>
                </Dialog>
            </div>
            
            {users && <DataTable pagination={users} columns={columns}
              onSearch={value => setQ(value)}
              onLimitChange={value => setLimit(parseInt(value))}
              onPageIndexChange={value => setPage(value+1)}
              onStatusChange={value => setStatus(value[0] as EUserStatus)}
            />}
        </div>
    )
}
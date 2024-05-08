"use client"

import { DotsHorizontalIcon } from "@radix-ui/react-icons"
import { Row } from "@tanstack/react-table"
import {
  CheckCircledIcon,
  Pencil2Icon,
} from "@radix-ui/react-icons"

import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuRadioGroup,
  DropdownMenuRadioItem,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { taskSchema } from "@/types/schema"


interface DataTableRowActionsProps<TData> {
  row: Row<TData>,
  onAction?: (action: string, data:TData ) => void
}

export function DataTableRowActions<TData>({
  row,
  onAction,
}: DataTableRowActionsProps<TData>) {
  const task = row.original;

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="ghost"
          className="flex h-8 w-8 p-0 data-[state=open]:bg-muted"
        >
          <DotsHorizontalIcon className="h-4 w-4" />
          <span className="sr-only">Open menu</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-[160px]">
        <DropdownMenuItem onClick={() => onAction && onAction('EDIT',task)} className="cursor-pointer space-x-4">
          <Pencil2Icon />
          <span>Edit</span>
        </DropdownMenuItem>
        <DropdownMenuItem onClick={() => onAction && onAction('COMPLETE',task)} className="cursor-pointer space-x-4">
          <CheckCircledIcon />
          <span>Complete</span>
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem className="cursor-pointer" onClick={() => onAction && onAction('DELETE', task)}>
          Delete
          <DropdownMenuShortcut>⌘⌫</DropdownMenuShortcut>
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}

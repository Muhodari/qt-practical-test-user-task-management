"use client"

import { Cross2Icon } from "@radix-ui/react-icons"
import { Table } from "@tanstack/react-table"

import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"



import { DataTableFacetedFilter } from "./data-table-faceted-filter"
import { priorities, statuses } from "@/lib/data"
import { DataTableViewOptions } from "./data-table-view-options"

interface DataTableToolbarProps<TData> {
  table: Table<TData>,
  onSearch?: (value: string) => void
  onStatusChange?: (value: string[]) => void
  onPriorityChange?: (value: string[]) => void
}

export function DataTableToolbar<TData>({
  table,
  onSearch,
  onStatusChange,
  onPriorityChange,
}: DataTableToolbarProps<TData>) {
  const isFiltered = table.getState().columnFilters.length > 0

  return (
    <div className="flex items-center justify-between">
      <div className="flex flex-1 items-center space-x-2">
        <Input
          placeholder="Filter tasks..."
          value={""}
          onChange={(event) =>
            onSearch?.(event.currentTarget.value)
          }
          className="h-8 w-[150px] lg:w-[250px]"
        />
        {onStatusChange && table.getColumn("status") && (
          <DataTableFacetedFilter
            column={table.getColumn("status")}
            title="Status"
            options={statuses}
            onFilterChange={onStatusChange}
          />
        )}
        { onPriorityChange && table.getColumn("priority") && (
          <DataTableFacetedFilter
            column={table.getColumn("priority")}
            title="Priority"
            options={priorities}
            onFilterChange={onPriorityChange}
          />
        )}
        {isFiltered && (
          <Button
            variant="ghost"
            onClick={() => {
              table.resetColumnFilters()
              table.resetSorting()
              onSearch?.("")
              onStatusChange?.([])
              onPriorityChange?.([])
            }}
            className="h-8 px-2 lg:px-3"
          >
            Reset
            <Cross2Icon className="ml-2 h-4 w-4" />
          </Button>
        )}
      </div>
      <DataTableViewOptions table={table} />
    </div>
  )
}

import {
    ArrowDownIcon,
    ArrowRightIcon,
    ArrowUpIcon,
    CheckCircledIcon,
    CircleIcon,
    CrossCircledIcon,
  } from "@radix-ui/react-icons"
import { DeleteIcon } from "lucide-react"

export const statuses = [
    {
      value: "ACTIVE",
      label: "Todo",
      icon: CircleIcon,
    },
    {
      value: "DELETED",
      label: "Done",
      icon: CheckCircledIcon,
    },
    {
      value: "INACTIVE",
      label: "Canceled",
      icon: CrossCircledIcon,
    },
  ]

  export const userStatuses = [
    {
      value: "ACTIVE",
      label: "Active",
      icon: CheckCircledIcon,
    },
    {
      value: "DELETED",
      label: "Deleted",
      icon: DeleteIcon,
    },
    {
      value: "INACTIVE",
      label: "Inactive",
      icon: CrossCircledIcon,
    },
    {
      value:'PENDING',
      label:'Active',
      icon: CheckCircledIcon,
    }
  ]

  export const priorities = [
    {
      label: "Low",
      value: "LOW",
      icon: ArrowDownIcon,
    },
    {
      label: "Normal",
      value: "NORMAL",
      icon: ArrowRightIcon,
    },
    {
      label: "High",
      value: "HIGH",
      icon: ArrowUpIcon,
    },
  ]
import { cn } from "@/lib/utils"
import Link from "next/link"


export function MainNav({
  className,
  ...props
}: React.HTMLAttributes<HTMLElement>) {
  return (
    <nav
      className={cn("flex items-center space-x-4 lg:space-x-6", className)}
      {...props}
    >
      <Link
        href="/dashboard/users"
        className="text-sm font-medium transition-colors hover:text-primary"
      >
        Users
      </Link>

      <Link
        href="/dashboard/projects"
        className="text-sm font-medium text-muted-foreground transition-colors hover:text-primary"
      >Projects</Link>
  
      <Link
        href="/dashboard/tasks"
        className="text-sm font-medium text-muted-foreground transition-colors hover:text-primary"
      >
        Tasks
      </Link>

      
    </nav>
  )
}

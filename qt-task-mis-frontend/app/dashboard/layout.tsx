"use client"
import { MainNav } from '@/components/main-nav'
import { Button } from '@/components/ui/button'
import { Toaster } from '@/components/ui/toaster'
import { UserNav } from '@/components/user-nav'
import { useRouter } from 'next/navigation'

interface DashboardLayoutProps {
    children: React.ReactNode
  }

export default function DashboardLayout({ children }: DashboardLayoutProps) {

    const token = localStorage.getItem('qtToken')
    const router = useRouter();
    
    if(!token){
        return <div className='grid w-full items-center'>
            <div className='m-auto py-8 space-y-6'>
                <div>Not Authenticated</div>
                <Button onClick={() => router.push('/')}>Go To Sign In</Button>
            </div>
        </div>
    }

    return (
       <>
      <div className="hidden flex-col md:flex">
        <div className="border-b">
          <div className="flex h-16 items-center px-4">
            <MainNav className="mx-6" />
            <div className="ml-auto flex items-center space-x-4">
              <UserNav />
            </div>
          </div>
        </div>
        <div className="flex-1 space-y-4 p-8 pt-6">
          <div >{children}</div>
        </div>
      </div>
      <Toaster />

    </>
        
    )
  }
  
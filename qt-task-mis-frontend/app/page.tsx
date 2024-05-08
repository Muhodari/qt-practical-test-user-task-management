"use client"

import { Button } from "@/components/ui/button"
import React, { use } from "react"
import { Icons } from "@/components/icons"
import { cn } from "@/lib/utils"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useRouter } from "next/navigation"
import { useAuthStore } from "@/store/auth"
import { IUser } from "@/types/schema"
import Image from 'next/image'
import Link from "next/link"


export default function IndexPage() {
  const [isLoading, setIsLoading] = React.useState<boolean>(false)
  const router = useRouter()
  const [authInfo, setAuthInfo] = React.useState({
    email: "",
    password: "",
  })

  const authStore = useAuthStore();

  function handleAuthInfoChange(event: React.ChangeEvent<HTMLInputElement>) {
    setAuthInfo((prev) => ({
      ...prev,
      [event.target.id]: event.target.value,
    }))
  }

  async function onSubmit(event: React.SyntheticEvent) { 
    event.preventDefault()
    setIsLoading(true)
    

    const response = await fetch("http://localhost:8080/api/v1/auth/signin", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "accept": "*/*",
      },
      body: JSON.stringify({
        login: authInfo.email,
        password: authInfo.password,
      }),
    })

    const res = await response.json()

    if(response.status === 500) {
      alert(res.error)
    }
    if(response.status === 200){
      localStorage.setItem('qtToken', res.token.accessToken)
      setTimeout(async () => {
        const user = await getCurrentUser()
        authStore.setUser(user)
        router.push('/dashboard/users')
      },500)

    }
    
    setIsLoading(false)
  }



  return (
    <section className="flex w-full h-screen">
      <div className="flex-1 flex items-center justify-center">
      <div className='max-w-md w-2/3'>
        
        <form onSubmit={onSubmit}  className="w-full">
          <div className="grid gap-2">
            <div className="grid gap-1 p-2">
              <Label className="sr-only" htmlFor="email">
                Email
              </Label>
              <Input
                id="email"
                placeholder="name@example.com"
                type="email"
                autoCapitalize="none"
                autoComplete="email"
                autoCorrect="off"
                disabled={isLoading}
                onChange={handleAuthInfoChange}
                className="h-12"
              />
            </div>
            <div className="grid gap-1 p-2">
              <Label className="sr-only" htmlFor="password">
                Password
              </Label>
              <Input
                id="password"
                placeholder="********"
                type="password"
                autoCapitalize="none"
                autoComplete="password"
                autoCorrect="off"
                disabled={isLoading}
                onChange={handleAuthInfoChange}
                className="h-12"
              />
            </div>
            <Button disabled={isLoading} className="bg-blue-900 h-12">
              {isLoading && (
                <Icons.spinner className="mr-2 h-4 w-4 animate-spin p-2 bg-orage-700" />
              )}
              Login
            </Button>
          </div>
        </form>
      </div>
      </div>

      <div className="bg-blue-500 flex-1 flex items-center justify-center">
      <Image
      src="/tasks.svg"
      width={500}
      height={500}
      alt="Picture of the author"
     />
      </div>
     
    </section>
  )
}

async function getCurrentUser(): IUser{
  const token = localStorage.getItem('qtToken')
  const response = await fetch("http://localhost:8080/api/v1/auth/currentUser", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "accept": "*/*",
      "Authorization": `Bearer ${token}`
    },
  })

  const res = await response.json()
  console.log(res)
  return res
}
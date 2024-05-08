'use client'
import { UpdateUserForm } from "@/components/update-user";
import { useGetUser } from "@/services/users";
import { Suspense } from "react";


export default function UpdateUser({ params }: { params: { id: string }}) {
    console.log(params)
    const userById = useGetUser(params.id)

    return (
        <div>
            <div className="space-y-10 px-8">
                <h1>Edit User</h1>
                
                <Suspense fallback={<div>Loading...</div>}>
                    {userById.data?.data && <UpdateUserForm user={userById.data.data} />}
                </Suspense>

            </div>
        </div>
    )
}
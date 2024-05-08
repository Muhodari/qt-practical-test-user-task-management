'use client'
import { UpdateTask } from "@/components/update-task";
import { useGetTask } from "@/services/tasks";
import { Suspense } from "react";


export default function EditTask({ params }: { params: { id: string }}){
    const taskById = useGetTask(params.id)

return (
    <div className="space-y-10">
        <h1> Edit Task</h1>

        <div>
            <Suspense fallback={<div>Loading...</div>}>
                {taskById?.data?.data && <UpdateTask task={taskById.data.data} /> }
            </Suspense>
        </div>
        
    </div>
)

}
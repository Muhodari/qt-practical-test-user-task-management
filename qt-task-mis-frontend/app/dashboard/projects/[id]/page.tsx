'use client'
import { UpdateTask } from "@/components/update-task";
import { useGetProject } from "@/services/projects";
import { Suspense } from "react";


export default function EditProject({ params }: { params: { id: string }}){
    const projectId = useGetProject(params.id)

return (
    <div className="space-y-10">
        <h1> Edit Task</h1>

        <div>
            <Suspense fallback={<div>Loading...</div>}>
                {projectId?.data?.data && <UpdateTask task={projectId.data.data} /> }
            </Suspense>
        </div>
        
    </div>
)

}
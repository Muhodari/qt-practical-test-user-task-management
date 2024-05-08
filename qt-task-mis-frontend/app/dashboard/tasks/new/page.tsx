import { CreateTaskForm } from "@/components/create-task";


export default function NewTask() {
    return (
        <div>
            <div className="space-y-10 px-8">
                <h1>New Task</h1>
                <CreateTaskForm />
            </div>
        </div>
    )
}
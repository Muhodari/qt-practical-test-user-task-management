import { CreateProjectForm } from "@/components/create-project";


export default function NewProject() {
    return (
        <div>
            <div className="space-y-10 px-8">
                <h1>New Project</h1>
                <CreateProjectForm />
            </div>
        </div>
    )
}
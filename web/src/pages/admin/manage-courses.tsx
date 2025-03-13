import type { FetchCoursesResponse } from '@/api/course/fetch-courses'
import { registerCourse } from '@/api/course/register-course'
import { CourseCard } from '@/components/course-card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useCourses } from '@/hooks/use-courses'
import { zodResolver } from '@hookform/resolvers/zod'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { Plus } from 'lucide-react'
import { useForm } from 'react-hook-form'
import { toast } from 'sonner'
import { z } from 'zod'

const formSchema = z.object({
  name: z.string().min(1, 'Campo obrigatório'),
  level: z.string().min(1, 'Campo obrigatório'),
})

type FormSubmitCourse = z.infer<typeof formSchema>

export function ManageCourses() {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({
    resolver: zodResolver(formSchema),
  })
  const queryClient = useQueryClient()

  const { data: courses } = useCourses()

  const { mutateAsync: mutationRegisterCourse, isPending: isSubmittingForm } =
    useMutation({
      mutationFn: registerCourse,
      onSuccess: ({ id, level, name }) => {
        const cached = queryClient.getQueryData<FetchCoursesResponse>([
          'courses',
        ])

        console.log(cached)

        if (cached) {
          queryClient.setQueryData<FetchCoursesResponse>(
            ['courses'],
            [
              ...cached,
              {
                id,
                name,
                level,
              },
            ]
          )
        }
      },
    })

  async function handleCreateCourse({ name, level }: FormSubmitCourse) {
    try {
      await mutationRegisterCourse({ name, level })
      toast.success('Curso criado')
      reset()
    } catch {
      toast.error('Erro ao cadastrar o curso.')
    }
  }

  return (
    <div className='flex flex-col gap-6 w-full max-w-4xl'>
      <h1 className='text-xl font-bold'>Cursos</h1>
      <form
        onSubmit={handleSubmit(handleCreateCourse)}
        className='card flex flex-col gap-4'
      >
        <h1 className='font-bold text-lg mb-2'>Cadastro</h1>
        <fieldset className='flex flex-col gap-1'>
          <Label>Nome:</Label>
          <Input {...register('name')} error={errors.name?.message} />
        </fieldset>

        <fieldset className='flex flex-col gap-1'>
          <Label>Nível:</Label>
          <Input {...register('level')} error={errors.level?.message} />
        </fieldset>
        <Button className='self-end' type='submit' disabled={isSubmittingForm}>
          Adicionar Curso
          <Plus className='size-4' strokeWidth={3} />
        </Button>
      </form>
      {courses?.map((course) => (
        <CourseCard key={course.id} course={course} />
      ))}
    </div>
  )
}

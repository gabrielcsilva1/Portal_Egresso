import type { ShortCourseResponse } from '@/api/@responses/course/short-course-response'
import { deleteCourse } from '@/api/course/delete-course'
import type { FetchCoursesResponse } from '@/api/course/fetch-courses'
import { cn } from '@/lib/utils'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { X } from 'lucide-react'
import { toast } from 'sonner'
import { Button } from './ui/button'

type Props = {
  className?: string
  course: ShortCourseResponse
}

export function CourseCard({ course, className }: Props) {
  const queryClient = useQueryClient()

  const { mutateAsync: mutationDeleteCourse, isPending: isDeleting } =
    useMutation({
      mutationFn: deleteCourse,
      onSuccess: () => {
        const cached = queryClient.getQueryData<FetchCoursesResponse>([
          'courses',
        ])

        if (cached) {
          queryClient.setQueryData(
            ['courses'],
            [
              ...cached.filter((cachedCourse) => {
                if (cachedCourse.id === course.id) {
                  return false
                }

                return true
              }),
            ]
          )
        }
      },
    })

  async function handleDeleteCourse() {
    try {
      const isDeletionConfirmed = confirm(
        `Deseja mesmo excluir o curso [${course.level}] - ${course.name}`
      )

      if (isDeletionConfirmed) {
        await mutationDeleteCourse(course.id)
        toast.success('Curso deletado')
      }
    } catch {
      console.error('Erro ao deletar o curso')
    }
  }

  return (
    <div className={cn('card flex justify-between items-center', className)}>
      <div className='flex flex-col gap-2'>
        <div className='flex gap-2'>
          <p className='font-bold text-sm'>Name:</p>
          <span className='text-sm'>{course.name}</span>
        </div>
        <div className='flex gap-2'>
          <p className='font-bold text-sm'>Nível:</p>
          <span className='text-sm'>{course.level}</span>
        </div>
      </div>

      <div className='flex gap-2'>
        <Button
          className='p-3'
          variant='destructive'
          disabled={isDeleting}
          onClick={handleDeleteCourse}
        >
          <X className='size-4' />
        </Button>
      </div>
    </div>
  )
}

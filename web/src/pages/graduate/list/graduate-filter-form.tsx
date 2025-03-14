import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Skeleton } from '@/components/ui/skeleton'
import { useCourses } from '@/hooks/use-courses'
import { Search } from 'lucide-react'
import { Controller, useForm } from 'react-hook-form'
import { useSearchParams } from 'react-router'
import { z } from 'zod'

const formSchema = z.object({
  year: z.number().optional(),
  courseName: z.string().optional(),
  query: z.string().optional(),
})

type GraduateFilterFormSchema = z.infer<typeof formSchema>

export function GraduateFilterForm() {
  const [searchParams, setSearchParams] = useSearchParams()
  const { data: courses, isLoading: isFetchingCourses } = useCourses()

  const year = searchParams.get('year')
  const courseName = searchParams.get('courseName')
  const query = searchParams.get('query')

  const { register, handleSubmit, control } = useForm<GraduateFilterFormSchema>({
    defaultValues: {
      courseName: courseName ?? '',
      query: query ?? '',
      year: year ? Number(year) : undefined,
    },
  })

  function handleFilter(data: GraduateFilterFormSchema) {
    const year = data.year?.toString()
    const courseName = data.courseName?.toString()
    const query = data.query?.toString()

    setSearchParams((prev) => {
      if (year) {
        prev.set('year', year)
      } else {
        prev.delete('year')
      }

      if (courseName) {
        prev.set('courseName', courseName)
      } else {
        prev.delete('courseName')
      }

      if (query) {
        prev.set('query', query)
      } else {
        prev.delete('query')
      }

      prev.set('page', '1')

      return prev
    })
  }

  if (isFetchingCourses) {
    return (
      <div className="flex items-center gap-2 mt-8">
        <Skeleton className="w-10 h-9" />
        <Skeleton className="w-72 h-9" />
        <Skeleton className="w-72 h-9" />
      </div>
    )
  }

  return (
    <form onSubmit={handleSubmit(handleFilter)} className="flex items-center gap-2 mt-8">
      <span className="text-sm font-semibold">Filtros:</span>
      <Input placeholder="Nome ou cargo" {...register('query')} />
      {courses && (
        <Controller
          control={control}
          name="courseName"
          render={({ field: { name, onChange, value } }) => (
            <Select name={name} onValueChange={onChange} value={value}>
              <SelectTrigger className="h-8">
                <SelectValue placeholder="Curso" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                {courses.map((course) => (
                  <SelectItem key={course.id} value={course.name}>
                    {course.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          )}
        />
      )}
      <Input placeholder="Ano" type="number" {...register('year')} className="w-36" />
      <Button type="submit" size="sm">
        <Search className="h-4 w-4" />
        Buscar
      </Button>
    </form>
  )
}

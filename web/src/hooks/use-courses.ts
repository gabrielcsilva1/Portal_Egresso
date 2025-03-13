import { fetchCourses } from '@/api/course/fetch-courses'
import { useQuery } from '@tanstack/react-query'

export function useCourses() {
  return useQuery({
    queryKey: ['courses'],
    queryFn: fetchCourses,
    staleTime: Number.POSITIVE_INFINITY,
  })
}

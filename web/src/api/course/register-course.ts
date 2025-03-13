import { api } from '@/lib/axios'
import type { ShortCourseResponse } from '../@responses/course/short-course-response'

export type RegisterCourseRequest = {
  name: string
  level: string
}

export async function registerCourse({ name, level }: RegisterCourseRequest) {
  const { data } = await api.post<ShortCourseResponse>('/course', {
    name,
    level,
  })

  return data
}

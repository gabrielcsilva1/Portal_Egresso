import { api } from '@/lib/axios'
import type { ShortCourseResponse } from '../@responses/course/short-course-response'

export type FetchCoursesResponse = Array<ShortCourseResponse>

export async function fetchCourses() {
  const { data } = await api.get<FetchCoursesResponse>('/course')

  return data
}

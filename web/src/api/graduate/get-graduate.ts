import { api } from '@/lib/axios'
import type { ShortCourseResponse } from '../@responses/course/short-course-response'

export type PositionResponse = {
  id: string
  description: string
  location: string
  startYear: number
  endYear: number | null
}

export type GetGraduateByIdResponse = {
  id: string
  courses: Array<ShortCourseResponse>
  positions: Array<PositionResponse>
  name: string
  email: string
  description: string | null
  avatarUrl: string | null
  linkedin: string | null
  instagram: string | null
  curriculum: string | null
}

export async function getGraduate(id: string) {
  const { data } = await api.get<GetGraduateByIdResponse>(`/graduate/${id}`)

  return data
}

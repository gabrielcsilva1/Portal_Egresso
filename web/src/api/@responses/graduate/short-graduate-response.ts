import type { StatusEnum } from '../enums/statusEnum'

type GraduateCourseResponse = {
  registerId: string
  courseId: string
  name: string
  level: string
  startYear: number
  endYear: number | null
}

export type ShortGraduateResponse = {
  id: string
  name: string
  avatarUrl: string | null
  courses: Array<GraduateCourseResponse>
  registrationStatus: StatusEnum
}

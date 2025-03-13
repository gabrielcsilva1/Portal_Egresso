import { api } from '@/lib/axios'

export interface RegisterGraduateBody {
  name: string
  email: string
  courseId: string
  startYear: number
  endYear?: number
  password: string
}

export async function registerGraduate({
  name,
  email,
  courseId,
  startYear,
  endYear,
  password,
}: RegisterGraduateBody) {
  await api.post('/graduate', {
    name,
    email,
    courseId,
    startYear,
    endYear,
    password,
  })
}

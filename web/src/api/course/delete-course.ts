import { api } from '@/lib/axios'

export async function deleteCourse(courseId: string) {
  await api.delete(`/course/${courseId}`)
}

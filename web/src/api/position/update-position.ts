import { api } from '@/lib/axios'

export type UpdatePositionRequest = {
  id: string
  description: string
  location: string
  startYear: number
  endYear?: number | null
}

export async function updatePosition({
  id,
  description,
  location,
  startYear,
  endYear,
}: UpdatePositionRequest) {
  await api.put(`/position/${id}`, {
    description,
    location,
    startYear,
    endYear,
  })
}

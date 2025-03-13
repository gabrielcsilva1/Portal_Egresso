import { api } from '@/lib/axios'
import type { PositionResponse } from '../graduate/get-graduate'

export type RegisterPositionRequest = {
  description: string
  location: string
  startYear: number
  endYear?: number | null
}

export type RegisterPositionResponse = PositionResponse

export async function registerPosition({
  description,
  location,
  startYear,
  endYear,
}: RegisterPositionRequest) {
  const { data } = await api.post<RegisterPositionResponse>('/position', {
    description,
    location,
    startYear,
    endYear,
  })

  return data
}

import { api } from '@/lib/axios'
import type { PositionResponse } from '../graduate/get-graduate'

export type FetchGraduatePositionsRequest = {
  graduateId: string
}

export type FetchGraduatePositionsResponse = Array<PositionResponse>

export async function fetchGraduatePositions({
  graduateId,
}: FetchGraduatePositionsRequest) {
  const { data } = await api.get<FetchGraduatePositionsResponse>(
    `/position/graduate/${graduateId}`
  )
  return data
}

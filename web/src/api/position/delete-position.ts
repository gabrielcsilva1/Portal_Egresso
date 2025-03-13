import { api } from '@/lib/axios'

export type DeletePositionRequest = {
  positionId: string
}

export async function deletePosition({ positionId }: DeletePositionRequest) {
  await api.delete(`/position/${positionId}`)
}

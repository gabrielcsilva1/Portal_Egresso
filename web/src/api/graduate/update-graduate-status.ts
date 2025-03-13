import { api } from '@/lib/axios'
import type { StatusEnum } from '../@responses/enums/statusEnum'

export type UpdateGraduateStatus = {
  graduateId: string
  newStatus: StatusEnum
}

export async function updateGraduateStatus({
  graduateId,
  newStatus,
}: UpdateGraduateStatus) {
  await api.put(`/graduate/${graduateId}/status`, {
    newStatus,
  })
}

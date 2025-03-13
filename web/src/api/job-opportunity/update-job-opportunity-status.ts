import { api } from '@/lib/axios'
import type { StatusEnum } from '../@responses/enums/statusEnum'

export type UpdateJobOpportunityStatusRequest = {
  jobId: string
  newStatus: StatusEnum
}

export async function updateJobOpportunityStatus({
  jobId,
  newStatus,
}: UpdateJobOpportunityStatusRequest) {
  await api.put(`/job-opportunity/${jobId}/status`, {
    newStatus,
  })
}

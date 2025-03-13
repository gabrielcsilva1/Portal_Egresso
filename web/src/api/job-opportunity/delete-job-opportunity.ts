import { api } from '@/lib/axios'

export type DeleteJobOpportunityRequest = {
  id: string
}

export async function deleteJobOpportunity({
  id,
}: DeleteJobOpportunityRequest) {
  await api.delete(`/job-opportunity/${id}`)
}

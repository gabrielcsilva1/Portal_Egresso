import { api } from '@/lib/axios'
import type { ShortJobOpportunityResponse } from '../@responses/job-opportunity/short-job-opportunity-response'

export type UpdateJobOpportunityRequest = {
  id: string
  title: string
  description: string
}

export type UpdateJobOpportunityResponse = Omit<
  ShortJobOpportunityResponse,
  'createdAt'
> & {
  createdAt: Date
}

export async function updateJobOpportunity({
  id,
  title,
  description,
}: UpdateJobOpportunityRequest) {
  await api.put(`/job-opportunity/${id}`, {
    title,
    description,
  })
}

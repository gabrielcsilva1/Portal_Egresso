import { api } from '@/lib/axios'
import type { ShortJobOpportunityResponse } from '../@responses/job-opportunity/short-job-opportunity-response'

export type RegisterJobOpportunityRequest = {
  title: string
  description: string
}

export type RegisterJobOpportunityResponse = Omit<
  ShortJobOpportunityResponse,
  'createdAt'
> & {
  createdAt: Date
}

export async function registerJobOpportunity({
  title,
  description,
}: RegisterJobOpportunityRequest) {
  const { data } = await api.post<ShortJobOpportunityResponse>(
    '/job-opportunity',
    {
      title,
      description,
    }
  )

  return {
    ...data,
    createdAt: new Date(data.createdAt),
  }
}

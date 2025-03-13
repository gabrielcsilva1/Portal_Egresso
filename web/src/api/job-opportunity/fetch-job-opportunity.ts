import { api } from '@/lib/axios'
import type { JobOpportunityResponse } from '../@responses/job-opportunity/job-opportunity-response'
import type { PaginatedResponse } from '../@responses/paginated/paginated-response'

export type FetchJobOpportunityRequest = {
  pageIndex?: number
}

export type FetchJobOpportunityResponse =
  PaginatedResponse<JobOpportunityResponse>

export async function fetchJobOpportunity({
  pageIndex = 0,
}: FetchJobOpportunityRequest) {
  const { data } = await api.get<FetchJobOpportunityResponse>(
    '/job-opportunity',
    {
      params: {
        pageIndex,
      },
    }
  )

  const response = {
    ...data,
    content: data.content.map((opportunity) => ({
      ...opportunity,
      createdAt: new Date(opportunity.createdAt),
    })),
  }

  return response
}

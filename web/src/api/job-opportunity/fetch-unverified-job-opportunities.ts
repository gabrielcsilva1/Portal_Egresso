import { api } from '@/lib/axios'
import type { JobOpportunityResponse } from '../@responses/job-opportunity/job-opportunity-response'
import type { PaginatedResponse } from '../@responses/paginated/paginated-response'

type FetchJobOpportunitiesRequest = {
  pageIndex?: number
}

export type FetchUnverifiedJobOpportunitiesResponse =
  PaginatedResponse<JobOpportunityResponse>

export async function fetchUnverifiedJobOpportunities({
  pageIndex = 0,
}: FetchJobOpportunitiesRequest) {
  const { data } = await api.get<FetchUnverifiedJobOpportunitiesResponse>(
    '/job-opportunity/unverified',
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

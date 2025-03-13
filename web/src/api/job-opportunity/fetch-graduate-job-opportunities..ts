import { api } from '@/lib/axios'
import type { ShortJobOpportunityResponse } from '../@responses/job-opportunity/short-job-opportunity-response'

export type FetchGraduateJobOpportunityRequest = {
  graduateId: string
}

type ApiResponse = Array<ShortJobOpportunityResponse>

type JobOpportunityResponse = Omit<ShortJobOpportunityResponse, 'createdAt'> & {
  createdAt: Date
}

export type FetchGraduateJobOpportunitiesResponse =
  Array<JobOpportunityResponse>

export async function fetchGraduateJobOpportunities({
  graduateId,
}: FetchGraduateJobOpportunityRequest) {
  const { data } = await api.get<ApiResponse>(
    `/job-opportunity/graduate/${graduateId}`
  )

  return data.map((jobOpportunity) => {
    return {
      ...jobOpportunity,
      createdAt: new Date(jobOpportunity.createdAt),
    }
  })
}

import {
  type FetchGraduateJobOpportunityRequest,
  fetchGraduateJobOpportunities,
} from '@/api/job-opportunity/fetch-graduate-job-opportunities.'
import { useQuery } from '@tanstack/react-query'

export function useGraduateJobOpportunities({
  graduateId,
}: FetchGraduateJobOpportunityRequest) {
  return useQuery({
    queryKey: ['job-opportunities', graduateId],
    queryFn: async () => await fetchGraduateJobOpportunities({ graduateId }),
    staleTime: Number.POSITIVE_INFINITY,
  })
}

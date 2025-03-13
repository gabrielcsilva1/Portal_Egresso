import {
  type FetchJobOpportunityRequest,
  fetchJobOpportunity,
} from '@/api/job-opportunity/fetch-job-opportunity'
import { useQuery } from '@tanstack/react-query'

export function useJobOpportunities({ ...props }: FetchJobOpportunityRequest) {
  const valuesArray = Object.values(props)

  return useQuery({
    queryKey: ['opportunities', ...valuesArray],
    queryFn: async () => {
      return await fetchJobOpportunity({
        ...props,
      })
    },
  })
}

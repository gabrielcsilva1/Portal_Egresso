import {
  type FetchGraduatePositionsRequest,
  fetchGraduatePositions,
} from '@/api/position/fetch-graduate-positions'
import { useQuery } from '@tanstack/react-query'

export function useGraduatePositions({
  graduateId,
}: FetchGraduatePositionsRequest) {
  return useQuery({
    queryKey: ['positions', graduateId],
    queryFn: async () => await fetchGraduatePositions({ graduateId }),
    staleTime: 1000 * 60 * 5, // 5 minutos
  })
}

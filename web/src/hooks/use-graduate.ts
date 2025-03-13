import { getGraduate } from '@/api/graduate/get-graduate'
import { useQuery } from '@tanstack/react-query'

export function useGraduate(id: string) {
  return useQuery({
    queryKey: ['graduate', id],
    queryFn: async () => await getGraduate(id),
    staleTime: 1000 * 60 * 5, // 5 minutos
  })
}

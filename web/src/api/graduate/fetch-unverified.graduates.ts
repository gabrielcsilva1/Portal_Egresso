import { api } from '@/lib/axios'
import type { ShortGraduateResponse } from '../@responses/graduate/short-graduate-response'
import type { PaginatedResponse } from '../@responses/paginated/paginated-response'

export type FetchUnverifiedGraduatesRequest = {
  pageIndex: number
}

export type FetchUnverifiedGraduatesResponse =
  PaginatedResponse<ShortGraduateResponse>

export async function fetchUnverifiedGraduates({
  pageIndex,
}: FetchUnverifiedGraduatesRequest) {
  const { data } = await api.get<FetchUnverifiedGraduatesResponse>(
    '/graduate/unverified',
    {
      params: {
        pageIndex,
      },
    }
  )

  return data
}

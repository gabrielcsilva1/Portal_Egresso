import { api } from '@/lib/axios'
import type { ShortGraduateResponse } from '../@responses/graduate/short-graduate-response'
import type { PaginatedResponse } from '../@responses/paginated/paginated-response'

export type FetchGraduatesRequest = {
  pageIndex?: number | null
  query?: string | null
  courseName?: string | null
  year?: number | null
}

export type FetchGraduatesResponse = PaginatedResponse<ShortGraduateResponse>

export async function fetchGraduates({
  pageIndex = 0,
  query,
  courseName,
  year,
}: FetchGraduatesRequest) {
  const { data } = await api.get<FetchGraduatesResponse>('/graduate', {
    params: {
      pageIndex,
      query,
      courseName,
      year,
    },
  })

  return data
}

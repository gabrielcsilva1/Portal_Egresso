import { api } from '@/lib/axios'
import type { PaginatedResponse } from '../@responses/paginated/paginated-response'
import type { TestimonialResponse } from '../@responses/testimonial/testimonial-response'

export type FetchUnverifiedTestimonialsRequest = {
  pageIndex?: number
}

export type FetchUnverifiedTestimonialsResponse =
  PaginatedResponse<TestimonialResponse>

export async function fetchUnverifiedTestimonials({
  pageIndex,
}: FetchUnverifiedTestimonialsRequest) {
  const { data } = await api.get<FetchUnverifiedTestimonialsResponse>(
    '/testimonial/unverified',
    {
      params: {
        pageIndex,
      },
    }
  )

  return data
}

import { api } from '@/lib/axios'
import type { PaginatedResponse } from '../@responses/paginated/paginated-response'
import type { TestimonialResponse } from '../@responses/testimonial/testimonial-response'

export type FetchTestimonialsRequest = {
  pageIndex?: number
  year?: number
}

export type FetchTestimonialsResponse = PaginatedResponse<TestimonialResponse>

export async function fetchTestimonials({
  pageIndex = 0,
  year,
}: FetchTestimonialsRequest) {
  const { data } = await api.get<FetchTestimonialsResponse>('/testimonial', {
    params: {
      pageIndex,
      year,
    },
  })

  const response = {
    ...data,
    content: data.content.map((testimonial) => ({
      ...testimonial,
      createdAt: new Date(testimonial.createdAt),
    })),
  }
  return response
}

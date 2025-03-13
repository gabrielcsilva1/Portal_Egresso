import { api } from '@/lib/axios'
import type { ShortTestimonialResponse } from '../@responses/testimonial/short-testimonial-json'

type ApiResponse = ShortTestimonialResponse[]

type Testimonial = Omit<ShortTestimonialResponse, 'createdAt'> & {
  createdAt: Date
}

export type FetchGraduateTestimonialsResponse = Testimonial[]

export async function fetchGraduateTestimonials(graduateId: string) {
  const { data } = await api.get<ApiResponse>(
    `/testimonial/graduate/${graduateId}`
  )

  const response = data.map((testimonial) => {
    return {
      ...testimonial,
      createdAt: new Date(testimonial.createdAt),
    }
  })
  return response
}

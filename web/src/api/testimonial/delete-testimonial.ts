import { api } from '@/lib/axios'

export type DeleteTestimonialRequest = {
  testimonialId: string
}

export async function deleteTestimonial({
  testimonialId,
}: DeleteTestimonialRequest) {
  await api.delete(`/testimonial/${testimonialId}`)
}

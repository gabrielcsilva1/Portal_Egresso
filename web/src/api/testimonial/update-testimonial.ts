import { api } from '@/lib/axios'

export type UpdateTestimonialRequest = {
  testimonialId: string
  text: string
}

export async function updateTestimonial({
  testimonialId,
  text,
}: UpdateTestimonialRequest) {
  await api.put(`/testimonial/${testimonialId}`, {
    text,
  })
}

import { api } from '@/lib/axios'
import type { StatusEnum } from '../@responses/enums/statusEnum'

export type UpdateTestimonialStatusRequest = {
  testimonialId: string
  newStatus: StatusEnum
}

export async function updateTestimonialStatus({
  testimonialId,
  newStatus,
}: UpdateTestimonialStatusRequest) {
  await api.put(`/testimonial/${testimonialId}/status`, {
    newStatus,
  })
}

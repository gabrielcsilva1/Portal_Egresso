import { api } from '@/lib/axios'
import type { StatusEnum } from '../@responses/enums/statusEnum'

export type RegisterTestimonialRequest = {
  text: string
}

type ApiResponse = {
  id: string
  text: string
  createdAt: string
  registrationStatus: StatusEnum
}

export type RegisterTestimonialResponse = Omit<ApiResponse, 'createdAt'> & {
  createdAt: Date
}

export async function registerTestimonial({
  text,
}: RegisterTestimonialRequest): Promise<RegisterTestimonialResponse> {
  const { data } = await api.post<ApiResponse>('/testimonial', {
    text,
  })

  const response = {
    ...data,
    createdAt: new Date(data.createdAt),
  }

  return response
}

import type { StatusEnum } from '../enums/statusEnum'

export type ShortTestimonialResponse = {
  id: string
  text: string
  createdAt: string
  registrationStatus: StatusEnum
}

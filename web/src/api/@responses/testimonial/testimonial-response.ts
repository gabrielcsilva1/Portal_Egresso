import type { StatusEnum } from '../enums/statusEnum'
import type { ShortGraduateResponse } from '../graduate/short-graduate-response'

export type TestimonialResponse = {
  id: string
  graduate: ShortGraduateResponse
  text: string
  createdAt: string
  registrationStatus: StatusEnum
}

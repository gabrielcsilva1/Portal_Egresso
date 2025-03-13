import type { StatusEnum } from '../enums/statusEnum'
import type { ShortGraduateResponse } from '../graduate/short-graduate-response'

export type JobOpportunityResponse = {
  id: string
  title: string
  description: string
  graduate: ShortGraduateResponse
  registrationStatus: StatusEnum
  createdAt: string
}

import type { StatusEnum } from '../enums/statusEnum'

export type ShortJobOpportunityResponse = {
  id: string
  title: string
  description: string
  registrationStatus: StatusEnum
  createdAt: string
}

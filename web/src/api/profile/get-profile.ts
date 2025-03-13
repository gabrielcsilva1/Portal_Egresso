import { api } from '@/lib/axios'
import type { RoleEnum } from '../@responses/enums/roleEnum'
import type { ShortGraduateResponse } from '../@responses/graduate/short-graduate-response'

export type CoordinatorProfileResponse = {
  role: RoleEnum.COORDINATOR
}

export type GraduateProfileResponse = {
  role: RoleEnum.GRADUATE
  user: ShortGraduateResponse
}

export type GetProfileResponse =
  | CoordinatorProfileResponse
  | GraduateProfileResponse
  | null

export async function getProfile() {
  try {
    const { data } = await api.get<GetProfileResponse>('/me')

    return data
  } catch (error) {
    return null
  }
}

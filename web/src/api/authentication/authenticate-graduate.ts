import { api } from '@/lib/axios'
import type { ShortGraduateResponse } from '../@responses/graduate/short-graduate-response'

export type AuthenticateGraduateRequest = {
  email: string
  password: string
}

export type AuthenticateGraduateResponse = {
  token: string
  graduate: ShortGraduateResponse
}

export async function authenticateGraduate({
  email,
  password,
}: AuthenticateGraduateRequest) {
  const { data } = await api.post<AuthenticateGraduateResponse>(
    '/graduate/session',
    {
      email,
      password,
    }
  )

  return data
}

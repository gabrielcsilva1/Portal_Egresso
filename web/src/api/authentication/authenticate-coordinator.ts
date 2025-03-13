import { api } from '@/lib/axios'

export type AuthenticateGraduateRequest = {
  login: string
  password: string
}

export async function authenticateCoordinator({
  login,
  password,
}: AuthenticateGraduateRequest) {
  await api.post('/coordinator/session', {
    login,
    password,
  })
}

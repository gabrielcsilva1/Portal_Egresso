import { api } from '@/lib/axios'

export type UpdateGraduateRequest = {
  name: string
  email: string
  description?: string
  avatarUrl?: string
  linkedin?: string
  instagram?: string
  curriculum?: string
}

type Props = UpdateGraduateRequest & {
  graduateId: string
}

export async function updateGraduate({ graduateId, ...props }: Props) {
  await api.put(`/graduate/${graduateId}`, {
    ...props,
  })
}

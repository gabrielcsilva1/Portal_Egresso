import { z } from 'zod'

const envSchema = z.object({
  VITE_API_URL: z.string().url(),
})

const _env = envSchema.safeParse(import.meta.env)

if (_env.success === false) {
  console.error(
    '❌ Variáveis ambientes incorretas',
    _env.error.flatten().fieldErrors
  )
  throw new Error('Variáveis ambientes incorretas.')
}

export const env = _env.data

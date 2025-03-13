import { authenticateCoordinator } from '@/api/authentication/authenticate-coordinator'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { zodResolver } from '@hookform/resolvers/zod'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { AxiosError } from 'axios'
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router'
import { toast } from 'sonner'
import { z } from 'zod'

const schema = z.object({
  login: z.string().min(1, 'campo obrigatório'),
  password: z.string().min(1, 'campo obrigatório'),
})

type SignInFormSchema = z.infer<typeof schema>

export function SignInAdmin() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(schema),
  })

  const {
    mutateAsync: mutateAuthenticateCoordinator,
    isPending: isSubmittingForm,
  } = useMutation({
    mutationFn: authenticateCoordinator,
  })

  async function handleSignIn({ login, password }: SignInFormSchema) {
    try {
      await mutateAuthenticateCoordinator({ login, password })

      await queryClient.invalidateQueries({
        queryKey: ['profile'],
      })

      navigate('/', {
        replace: true,
      })
    } catch (error) {
      if (error instanceof AxiosError && error.response?.data.error) {
        return toast.error(error.response.data.error)
      }
      toast.error('Ocorreu um erro ao fazer login')
    }
  }

  return (
    <div className='flex justify-center'>
      <div className='mt-33 min-w-134 rounded-xl border border-slate-100 bg-white px-16 py-11'>
        <form onSubmit={handleSubmit(handleSignIn)}>
          <h1 className='text-4xl font-bold'>Entrar</h1>
          <fieldset className='mt-10 flex flex-col gap-2'>
            <Label htmlFor='email' className='font-bold'>
              Login:
            </Label>
            <Input
              id='login'
              type='login'
              {...register('login')}
              error={errors.login?.message}
            />
          </fieldset>

          <fieldset className='mt-5 flex flex-col gap-2'>
            <Label htmlFor='email' className='font-bold'>
              Senha:
            </Label>
            <Input
              id='password'
              type='password'
              {...register('password')}
              error={errors.password?.message}
            />
          </fieldset>

          <Button
            className='mt-10 w-full justify-center rounded-full'
            disabled={isSubmittingForm}
          >
            Entrar
          </Button>
        </form>
      </div>
    </div>
  )
}

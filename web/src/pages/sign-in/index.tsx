import { StatusEnum } from '@/api/@responses/enums/statusEnum'
import { authenticateGraduate } from '@/api/authentication/authenticate-graduate'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { zodResolver } from '@hookform/resolvers/zod'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { AxiosError } from 'axios'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router'
import { toast } from 'sonner'
import { z } from 'zod'
import { RegistrationStatusDialog } from './registration-status-dialog'

const schema = z.object({
  email: z.string().email('email inválido'),
  password: z.string().min(1, 'campo obrigatório'),
})

type SignInFormSchema = z.infer<typeof schema>

export function SignIn() {
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
    mutateAsync: mutateAuthenticateGraduate,
    data: authenticateResponse,
    isPending: isAuthenticationPending,
  } = useMutation({
    mutationFn: authenticateGraduate,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['profile'],
      })
    },
  })

  async function handleSignIn({ email, password }: SignInFormSchema) {
    try {
      const { graduate } = await mutateAuthenticateGraduate({ email, password })

      if (graduate.registrationStatus === StatusEnum.ACCEPTED) {
        navigate('/', { replace: true })
      }
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
              Email:
            </Label>
            <Input
              id='email'
              type='email'
              {...register('email')}
              error={errors.email?.message}
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
            disabled={isAuthenticationPending}
          >
            Entrar
          </Button>
        </form>

        <p className='mt-10 text-center text-sm text-muted-foreground'>
          Não tem uma conta?{' '}
          <Link
            to='/sign-up'
            className='hover:text-accent-foreground underline transition-colors'
          >
            Cadastre-se
          </Link>
        </p>
      </div>

      {authenticateResponse && (
        <RegistrationStatusDialog
          registrationStatus={authenticateResponse.graduate.registrationStatus}
        />
      )}
    </div>
  )
}

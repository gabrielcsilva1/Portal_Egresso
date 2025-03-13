import { registerGraduate } from '@/api/graduate/register-graduate'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { useCourses } from '@/hooks/use-courses'
import { zodResolver } from '@hookform/resolvers/zod'
import { useMutation } from '@tanstack/react-query'
import { AxiosError } from 'axios'
import { Controller, useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router'
import { toast } from 'sonner'
import zod from 'zod'

const schema = zod.object({
  name: zod.string().min(1, 'Campo obrigatório'),
  email: zod.string().email('Email Inválido'),
  course: zod
    .string({ message: 'Campo obrigatório' })
    .min(1, 'Campo obrigatório'),
  startYear: zod.coerce.number().min(1, 'Campo obrigatório'),
  endYear: zod.preprocess(
    (value) => (!value ? undefined : Number(value)),
    zod.number().optional()
  ),
  password: zod.string().min(1, 'Campo obrigatório'),
})

type SignUpFormSchema = zod.infer<typeof schema>

export function SignUp() {
  const navigate = useNavigate()

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    control,
    reset,
  } = useForm({
    resolver: zodResolver(schema),
  })

  const { mutateAsync: registerGraduateFn } = useMutation({
    mutationFn: registerGraduate,
  })

  const { data: courses } = useCourses()

  async function handleSignUp({
    name,
    email,
    course,
    startYear,
    endYear,
    password,
  }: SignUpFormSchema) {
    try {
      await registerGraduateFn({
        name,
        email,
        courseId: course,
        startYear,
        endYear,
        password,
      })

      toast.success('Cadastro realizado com sucesso', {
        action: {
          label: 'Ir para Login',
          onClick: () => {
            navigate('/sign-in')
          },
        },
      })
      reset()
    } catch (error) {
      if (
        error instanceof AxiosError &&
        error.response &&
        error.response.data
      ) {
        if (error.status === 409) {
          return toast.error(error.response.data.error)
        }
      }

      toast.error('Falha ao registrar')
    }
  }

  return (
    <div className='flex justify-center'>
      <div className='mt-33 min-w-134 rounded-xl border border-slate-100 bg-white px-16 py-11'>
        <form className='mt-10 space-y-5' onSubmit={handleSubmit(handleSignUp)}>
          <h1 className='text-4xl font-bold'>Cadastrar</h1>
          <fieldset className='flex flex-col gap-2'>
            <Label htmlFor='name' className='font-bold'>
              Nome*:
            </Label>
            <Input
              id='name'
              {...register('name')}
              error={errors.name?.message}
            />
          </fieldset>

          <fieldset className='flex flex-col gap-2'>
            <Label htmlFor='email' className='font-bold'>
              Email*:
            </Label>
            <Input
              id='email'
              type='email'
              {...register('email')}
              error={errors.email?.message}
            />
          </fieldset>

          <fieldset className='flex flex-col gap-2'>
            <Label htmlFor='course' className='font-bold'>
              Curso*:
            </Label>
            {errors.course?.message && (
              <span className='text-xs text-red-500'>
                {errors.course.message}
              </span>
            )}
            <Controller
              control={control}
              name='course'
              render={({ field: { onChange, value, name } }) => (
                <Select onValueChange={onChange} value={value} name={name}>
                  <SelectTrigger>
                    <SelectValue placeholder='Selecione um curso' />
                  </SelectTrigger>
                  <SelectContent>
                    {courses?.map((course) => (
                      <SelectItem
                        key={course.id}
                        value={course.id}
                      >{`[${course.level}] - ${course.name}`}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              )}
            />
          </fieldset>

          <div className='flex gap-2'>
            <fieldset className='flex flex-col gap-2'>
              <Label htmlFor='password' className='font-bold'>
                Ano de início*:
              </Label>
              <Input
                id='startYear'
                type='number'
                min={0}
                {...register('startYear')}
                error={errors.startYear?.message}
              />
            </fieldset>

            <fieldset className='flex flex-col gap-2'>
              <Label htmlFor='password' className='font-bold'>
                Ano de termino:
              </Label>
              {errors.startYear && <div className='h-2' />}
              <Input
                id='endYear'
                type='number'
                min={0}
                {...register('endYear')}
                error={errors.endYear?.message}
              />
            </fieldset>
          </div>

          <fieldset className='flex flex-col gap-2'>
            <Label htmlFor='password' className='font-bold'>
              Senha*:
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
            disabled={isSubmitting}
          >
            Cadastrar
          </Button>
        </form>

        <p className='mt-10 text-center text-sm'>
          <Link
            to='/sign-in'
            className=' text-muted-foreground hover:text-accent-foreground underline transition-colors'
          >
            Já possui uma conta?
          </Link>
        </p>
      </div>
    </div>
  )
}

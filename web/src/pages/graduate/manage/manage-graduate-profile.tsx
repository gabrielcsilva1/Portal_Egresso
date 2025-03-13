import type { GetGraduateByIdResponse } from '@/api/graduate/get-graduate'
import { updateGraduate } from '@/api/graduate/update-graduate'
import type { GraduateProfileResponse } from '@/api/profile/get-profile'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Skeleton } from '@/components/ui/skeleton'
import { Textarea } from '@/components/ui/text-area'
import { useGraduate } from '@/hooks/use-graduate'
import type { GraduateConfigLayoutContextProps } from '@/pages/_layouts/graduate-config-layout'
import { zodResolver } from '@hookform/resolvers/zod'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'
import { Navigate, useOutletContext } from 'react-router'
import { toast } from 'sonner'
import { z } from 'zod'

const formSchema = z.object({
  name: z.string().min(1, 'Campo obrigatório'),
  email: z.string().min(1, 'Campo obrigatório').email('email inválido'),
  description: z.string().optional(),
  avatarUrl: z.string().optional(),
  linkedin: z.string().optional(),
  instagram: z.string().optional(),
  curriculum: z.string().optional(),
})

type UpdateFormSchema = z.infer<typeof formSchema>

export function ManageGraduateProfile() {
  const { graduate } = useOutletContext<GraduateConfigLayoutContextProps>()

  const queryClient = useQueryClient()

  const { data: profileInfo, isLoading } = useGraduate(graduate.id)

  const { mutateAsync: mutateUpdateGraduate, isPending: isUpdatingGraduate } =
    useMutation({
      mutationFn: updateGraduate,
      onSuccess: (_, { graduateId, ...rest }) => {
        // Atualiza o profile
        const profileCached = queryClient.getQueryData<GraduateProfileResponse>(
          ['profile']
        )

        if (profileCached) {
          queryClient.setQueryData<GraduateProfileResponse>(['profile'], {
            ...profileCached,
            user: {
              ...profileCached.user,
              avatarUrl: rest.avatarUrl || null,
              name: rest.name,
            },
          })
        }

        // Atualiza os dados do egresso
        const graduateCached =
          queryClient.getQueryData<GetGraduateByIdResponse>([
            'graduate',
            graduateId,
          ])

        if (graduateCached) {
          queryClient.setQueryData<GetGraduateByIdResponse>(
            ['graduate', graduateId],
            {
              ...graduateCached,
              ...rest,
            }
          )
        }
      },
    })

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(formSchema),
    values: {
      avatarUrl: profileInfo?.avatarUrl ?? '',
      name: graduate.name,
      description: profileInfo?.description ?? '',
      email: profileInfo?.email ?? '',
      instagram: profileInfo?.instagram ?? '',
      linkedin: profileInfo?.linkedin ?? '',
      curriculum: profileInfo?.curriculum ?? '',
    },
  })

  if (isLoading) {
    return <Skeleton className='w-full h-40' />
  }
  if (!profileInfo) {
    return <Navigate to='/' replace />
  }

  async function handleUpdateProfile({ ...props }: UpdateFormSchema) {
    try {
      await mutateUpdateGraduate({ graduateId: graduate.id, ...props })
      toast.success('Atualização feita com sucesso')
    } catch {
      toast.error('Erro ao atualizar o egresso')
    }
  }

  return (
    <div className='width-full'>
      <h1 className='text-xl font-bold'>Perfil</h1>

      <form
        className='flex flex-col gap-6 mt-6'
        onSubmit={handleSubmit(handleUpdateProfile)}
      >
        <Card className='p-6'>
          <p className='font-bold mb-4'>Avatar</p>
          <Label htmlFor='avatarUrl'>URL</Label>
          <Input className='mt-1' {...register('avatarUrl')} />
        </Card>

        <Card className='p-6 space-y-3.5'>
          <p className='font-bold mb-4'>Informações Pessoais</p>
          <div>
            <Label htmlFor='name'>Nome</Label>
            <Input
              id='name'
              className='mt-1'
              {...register('name')}
              error={errors.name?.message}
            />
          </div>
          <div>
            <Label htmlFor='email'>Email</Label>
            <Input
              id='email'
              type='email'
              className='mt-1'
              {...register('email')}
              error={errors.email?.message}
            />
          </div>
          <div>
            <Label htmlFor='description'>Descrição</Label>
            <Textarea
              id='description'
              className='mt-1'
              {...register('description')}
            />
          </div>
          <div>
            <Label htmlFor='curriculum'>Currículo (URL)</Label>
            <Input
              id='curriculum'
              className='mt-1'
              {...register('curriculum')}
              error={errors.name?.message}
            />
          </div>
        </Card>

        <Card className='p-6 space-y-3.5'>
          <p className='font-bold mb-4'>Redes Sociais</p>
          <div>
            <Label htmlFor='name'>Linkedin</Label>
            <Input id='linkedin' className='mt-1' {...register('linkedin')} />
          </div>
          <div>
            <Label htmlFor='instagram'>Instagram</Label>
            <Input id='instagram' className='mt-1' {...register('instagram')} />
          </div>
        </Card>

        <Button className='self-end w-36' disabled={isUpdatingGraduate}>
          Salvar
        </Button>
      </form>
    </div>
  )
}

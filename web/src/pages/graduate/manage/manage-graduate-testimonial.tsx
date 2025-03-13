import { StatusEnum } from '@/api/@responses/enums/statusEnum'
import {
  type DeleteTestimonialRequest,
  deleteTestimonial,
} from '@/api/testimonial/delete-testimonial'
import type { FetchGraduateTestimonialsResponse } from '@/api/testimonial/fetch-graduate-testimonials'
import {
  type RegisterTestimonialRequest,
  registerTestimonial,
} from '@/api/testimonial/register-testimonial'
import {
  type UpdateTestimonialRequest,
  updateTestimonial,
} from '@/api/testimonial/update-testimonial'
import { Dialog } from '@/components/dialog'
import { TestimonialCard } from '@/components/testimonial-card'
import { Button } from '@/components/ui/button'
import { Skeleton } from '@/components/ui/skeleton'
import { useGraduateTestimonials } from '@/hooks/use-graduate-testimonials'
import type { GraduateConfigLayoutContextProps } from '@/pages/_layouts/graduate-config-layout'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { Plus, SquarePen, Trash2 } from 'lucide-react'
import { Navigate, useOutletContext } from 'react-router'
import { toast } from 'sonner'
import { TestimonialForm } from './components/testimonial-form'

export function ManageGraduateTestimonial() {
  const { graduate } = useOutletContext<GraduateConfigLayoutContextProps>()
  const queryClient = useQueryClient()
  const { data: testimonials, isLoading: isFetchingTestimonials } =
    useGraduateTestimonials(graduate.id)
  const {
    mutateAsync: mutateRegisterTestimonial,
    isPending: isRegisteringTestimonial,
  } = useMutation({
    mutationFn: registerTestimonial,
    onSuccess: ({ id, text, createdAt, registrationStatus }) => {
      const cached =
        queryClient.getQueryData<FetchGraduateTestimonialsResponse>([
          'testimonials',
          graduate.id,
        ])
      if (cached) {
        queryClient.setQueryData<FetchGraduateTestimonialsResponse>(
          ['testimonials', graduate.id],
          [
            { id, text, createdAt: new Date(createdAt), registrationStatus },
            ...cached,
          ]
        )
      }
    },
  })

  const {
    mutateAsync: mutateDeleteTestimonial,
    isPending: isDeletingTestimonial,
  } = useMutation({
    mutationFn: async ({ testimonialId }: DeleteTestimonialRequest) => {
      const confirmed = confirm(
        'Você tem certeza que deseja excluir esse item?'
      )
      if (confirmed) {
        await deleteTestimonial({ testimonialId })
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['testimonials'],
      })
    },
  })

  const {
    mutateAsync: mutateUpdateTestimonial,
    isPending: isUpdatingTestimonial,
  } = useMutation({
    mutationFn: updateTestimonial,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['testimonials'],
      })
    },
  })

  async function handleCreateTestimonial({ text }: RegisterTestimonialRequest) {
    try {
      await mutateRegisterTestimonial({ text })
      toast.success('Depoimento cadastrado')
    } catch {
      toast.error('Erro ao cadastrar depoimento')
    }
  }

  async function handleUpdateTestimonial({
    testimonialId,
    text,
  }: UpdateTestimonialRequest) {
    try {
      await mutateUpdateTestimonial({ text, testimonialId })
      toast.success('Depoimento atualizado')
    } catch {
      toast.error('Erro ao atualizar depoimento')
    }
  }

  if (isFetchingTestimonials) {
    return <Skeleton className='w-full h-40' />
  }
  if (!graduate) {
    return <Navigate to='/' replace />
  }

  return (
    <div className='flex flex-col gap-6'>
      <h1 className='font-bold text-xl'>Depoimentos</h1>

      <Dialog.Root>
        <Dialog.Trigger asChild>
          <Button className='self-start'>
            <Plus strokeWidth={3} />
            Adicionar Depoimento
          </Button>
        </Dialog.Trigger>

        <Dialog.Content>
          <Dialog.Title>Criar Depoimento</Dialog.Title>
          <TestimonialForm
            onSubmit={handleCreateTestimonial}
            isSubmitting={isRegisteringTestimonial}
          />
        </Dialog.Content>
      </Dialog.Root>

      {testimonials?.map((testimonial) => (
        <div key={testimonial.id}>
          <TestimonialCard
            className='border-b-0 shadow-none rounded-b-none'
            testimonial={testimonial}
            graduate={graduate}
            showStatus
          />
          <div className='flex justify-center gap-4 border-x border-b border-border rounded-xl pb-4'>
            <Button
              variant='destructive'
              className='w-28'
              disabled={isDeletingTestimonial}
              onClick={() =>
                mutateDeleteTestimonial({
                  testimonialId: testimonial.id,
                })
              }
            >
              <Trash2 />
              Excluir
            </Button>
            <Dialog.Root>
              <Dialog.Trigger asChild>
                <Button
                  className='w-28 bg-amber-500 disabled:bg-amber-500/50 hover:bg-amber-500/90'
                  disabled={
                    isDeletingTestimonial ||
                    testimonial.registrationStatus !== StatusEnum.PENDING
                  }
                >
                  <SquarePen />
                  Editar
                </Button>
              </Dialog.Trigger>
              <Dialog.Content>
                <Dialog.Title>Editar Depoimento</Dialog.Title>
                <TestimonialForm
                  defaultValues={testimonial}
                  onSubmit={(props: RegisterTestimonialRequest) =>
                    handleUpdateTestimonial({
                      testimonialId: testimonial.id,
                      ...props,
                    })
                  }
                  isSubmitting={isUpdatingTestimonial}
                />
              </Dialog.Content>
            </Dialog.Root>
          </div>
        </div>
      ))}
    </div>
  )
}

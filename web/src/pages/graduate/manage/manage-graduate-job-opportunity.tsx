import { StatusEnum } from '@/api/@responses/enums/statusEnum'
import {
  type DeleteJobOpportunityRequest,
  deleteJobOpportunity,
} from '@/api/job-opportunity/delete-job-opportunity'
import type { FetchGraduateJobOpportunitiesResponse } from '@/api/job-opportunity/fetch-graduate-job-opportunities.'
import {
  type RegisterJobOpportunityRequest,
  registerJobOpportunity,
} from '@/api/job-opportunity/register-job-opportunity'
import {
  type UpdateJobOpportunityRequest,
  updateJobOpportunity,
} from '@/api/job-opportunity/update-job-opportunity'
import { Dialog } from '@/components/dialog'
import { JobOpportunityCard } from '@/components/job-opportunity-card'
import { Button } from '@/components/ui/button'
import { Skeleton } from '@/components/ui/skeleton'
import { useGraduateJobOpportunities } from '@/hooks/use-graduate-job-opportunities'
import type { GraduateConfigLayoutContextProps } from '@/pages/_layouts/graduate-config-layout'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { Plus, SquarePen, Trash2 } from 'lucide-react'
import { Navigate, useOutletContext } from 'react-router'
import { toast } from 'sonner'
import { JobOpportunityForm } from './components/job-opportunity-form'

export function ManageGraduateJobOpportunity() {
  const { graduate } = useOutletContext<GraduateConfigLayoutContextProps>()
  const queryClient = useQueryClient()

  const { data: jobOpportunities, isLoading: isFetchingJobOpportunities } =
    useGraduateJobOpportunities({ graduateId: graduate.id })
  const {
    mutateAsync: mutateRegisterJobOpportunity,
    isPending: isRegisteringJobOpportunity,
  } = useMutation({
    mutationFn: registerJobOpportunity,
    onSuccess: ({ id, title, description, createdAt, registrationStatus }) => {
      const cached =
        queryClient.getQueryData<FetchGraduateJobOpportunitiesResponse>([
          'job-opportunities',
          graduate.id,
        ])
      if (cached) {
        queryClient.setQueryData<FetchGraduateJobOpportunitiesResponse>(
          ['job-opportunities', graduate.id],
          [
            {
              id,
              title,
              description,
              createdAt: new Date(createdAt),
              registrationStatus,
            },
            ...cached,
          ]
        )
      }
    },
  })

  const {
    mutateAsync: mutateDeleteJobOpportunity,
    isPending: isDeletingJobOpportunity,
  } = useMutation({
    mutationFn: async ({ id }: DeleteJobOpportunityRequest) => {
      const confirmed = confirm(
        'Você tem certeza que deseja excluir esse item?'
      )
      if (confirmed) {
        await deleteJobOpportunity({ id })
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['job-opportunities'],
      })
    },
  })

  const {
    mutateAsync: mutateUpdateJobOpportunity,
    isPending: isUpdatingJobOpportunity,
  } = useMutation({
    mutationFn: updateJobOpportunity,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['job-opportunities'],
      })
    },
  })

  async function handleCreateJobOpportunity({
    title,
    description,
  }: RegisterJobOpportunityRequest) {
    try {
      await mutateRegisterJobOpportunity({ title, description })
      toast.success('Cadastrado realizado')
    } catch {
      toast.error('Erro no cadastro')
    }
  }

  async function handleUpdateJobOpportunity({
    id,
    title,
    description,
  }: UpdateJobOpportunityRequest) {
    try {
      await mutateUpdateJobOpportunity({ id, title, description })
      toast.success('Atualização feita com sucesso')
    } catch {
      toast.error('Erro ao atualizar')
    }
  }

  if (isFetchingJobOpportunities) {
    return <Skeleton className='w-full h-40' />
  }
  if (!graduate) {
    return <Navigate to='/' replace />
  }

  return (
    <div className='flex flex-col gap-6'>
      <h1 className='font-bold text-xl'>Oportunidades de empregos</h1>

      <Dialog.Root>
        <Dialog.Trigger asChild>
          <Button className='self-start'>
            <Plus strokeWidth={3} />
            Adicionar Oportunidade de emprego
          </Button>
        </Dialog.Trigger>

        <Dialog.Content>
          <Dialog.Title>Criar Oportunidade de Emprego</Dialog.Title>
          <JobOpportunityForm
            onSubmit={handleCreateJobOpportunity}
            isSubmitting={isRegisteringJobOpportunity}
          />
        </Dialog.Content>
      </Dialog.Root>

      {jobOpportunities?.map((jobOpportunity) => (
        <div key={jobOpportunity.id}>
          <JobOpportunityCard
            className='border-b-0 shadow-none rounded-b-none'
            jobOpportunity={jobOpportunity}
            graduate={graduate}
            showStatus
          />
          <div className='flex justify-center gap-4 border-x border-b border-border rounded-xl pb-4'>
            <Button
              variant='destructive'
              className='w-28'
              disabled={isDeletingJobOpportunity}
              onClick={() =>
                mutateDeleteJobOpportunity({
                  id: jobOpportunity.id,
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
                    isDeletingJobOpportunity ||
                    jobOpportunity.registrationStatus !== StatusEnum.PENDING
                  }
                >
                  <SquarePen />
                  Editar
                </Button>
              </Dialog.Trigger>
              <Dialog.Content>
                <Dialog.Title>Editar Oportunidade de Emprego</Dialog.Title>
                <JobOpportunityForm
                  defaultValues={jobOpportunity}
                  onSubmit={(props: RegisterJobOpportunityRequest) =>
                    handleUpdateJobOpportunity({
                      id: jobOpportunity.id,
                      ...props,
                    })
                  }
                  isSubmitting={isUpdatingJobOpportunity}
                />
              </Dialog.Content>
            </Dialog.Root>
          </div>
        </div>
      ))}
    </div>
  )
}

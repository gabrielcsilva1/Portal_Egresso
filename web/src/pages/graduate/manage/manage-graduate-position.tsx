import { type DeletePositionRequest, deletePosition } from '@/api/position/delete-position'
import type { FetchGraduatePositionsResponse } from '@/api/position/fetch-graduate-positions'
import {
  type RegisterPositionRequest,
  registerPosition,
} from '@/api/position/register-position'
import { type UpdatePositionRequest, updatePosition } from '@/api/position/update-position'
import { Dialog } from '@/components/dialog'
import { PositionCard } from '@/components/position-card'
import { Button } from '@/components/ui/button'
import { Skeleton } from '@/components/ui/skeleton'
import { useGraduatePositions } from '@/hooks/use-graduate-positions'
import type { GraduateConfigLayoutContextProps } from '@/pages/_layouts/graduate-config-layout'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { Plus, SquarePen, Trash2 } from 'lucide-react'
import { Navigate, useOutletContext } from 'react-router'
import { toast } from 'sonner'
import { PositionForm } from './components/position-form'

export function ManageGraduatePosition() {
  const { graduate } = useOutletContext<GraduateConfigLayoutContextProps>()
  const queryClient = useQueryClient()

  const { data: positions, isLoading: isFetchingPositions } = useGraduatePositions({
    graduateId: graduate.id,
  })
  const { mutateAsync: mutateRegisterPosition, isPending: isRegisteringPosition } =
    useMutation({
      mutationFn: registerPosition,
      onSuccess: ({ id, description, location, startYear, endYear }) => {
        const cached = queryClient.getQueryData<FetchGraduatePositionsResponse>([
          'positions',
          graduate.id,
        ])
        if (cached) {
          queryClient.setQueryData<FetchGraduatePositionsResponse>(
            ['positions', graduate.id],
            [
              ...cached,
              {
                id,
                description,
                location,
                startYear,
                endYear,
              },
            ]
          )
        }

        queryClient.invalidateQueries({
          queryKey: ['graduate', graduate.id],
        })
      },
    })

  const { mutateAsync: mutateDeletePosition, isPending: isDeletingPosition } = useMutation({
    mutationFn: async ({ positionId }: DeletePositionRequest) => {
      const confirmed = confirm('Você tem certeza que deseja excluir esse item?')
      if (confirmed) {
        await deletePosition({ positionId })
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['positions'],
      })
      queryClient.invalidateQueries({
        queryKey: ['graduate', graduate.id],
      })
    },
  })

  const { mutateAsync: mutateUpdatePosition, isPending: isUpdatingPosition } = useMutation({
    mutationFn: updatePosition,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['positions'],
      })

      queryClient.invalidateQueries({
        queryKey: ['graduate', graduate.id],
      })
    },
  })

  async function handleCreatePosition({
    description,
    location,
    startYear,
    endYear,
  }: RegisterPositionRequest) {
    try {
      await mutateRegisterPosition({
        description,
        location,
        startYear,
        endYear,
      })
      toast.success('Cadastrado realizado')
    } catch {
      toast.error('Erro no cadastro')
    }
  }

  async function handleUpdatePosition({
    id,
    description,
    location,
    startYear,
    endYear,
  }: UpdatePositionRequest) {
    try {
      await mutateUpdatePosition({
        id,
        description,
        location,
        startYear,
        endYear,
      })
      toast.success('Atualização feita com sucesso')
    } catch {
      toast.error('Erro ao atualizar')
    }
  }

  if (isFetchingPositions) {
    return <Skeleton className="w-full h-40" />
  }
  if (!graduate) {
    return <Navigate to="/" replace />
  }

  return (
    <div className="flex flex-col gap-6">
      <h1 className="font-bold text-xl">Cargos</h1>

      <Dialog.Root>
        <Dialog.Trigger asChild>
          <Button className="self-start">
            <Plus strokeWidth={3} />
            Adicionar Cargo
          </Button>
        </Dialog.Trigger>

        <Dialog.Content>
          <Dialog.Title>Criar Cargo</Dialog.Title>
          <PositionForm onSubmit={handleCreatePosition} isSubmitting={isRegisteringPosition} />
        </Dialog.Content>
      </Dialog.Root>

      {positions?.map((position) => (
        <div key={position.id}>
          <PositionCard
            className="border-b-0 shadow-none rounded-b-none"
            position={position}
          />
          <div className="flex justify-center gap-4 border-x border-b border-border rounded-xl pb-4">
            <Button
              variant="destructive"
              className="w-28"
              disabled={isDeletingPosition}
              onClick={() =>
                mutateDeletePosition({
                  positionId: position.id,
                })
              }
            >
              <Trash2 />
              Excluir
            </Button>
            <Dialog.Root>
              <Dialog.Trigger asChild>
                <Button
                  className="w-28 bg-amber-500 disabled:bg-amber-500/50 hover:bg-amber-500/90"
                  disabled={isDeletingPosition}
                >
                  <SquarePen />
                  Editar
                </Button>
              </Dialog.Trigger>
              <Dialog.Content>
                <Dialog.Title>Editar Depoimento</Dialog.Title>
                <PositionForm
                  defaultValues={position}
                  onSubmit={(props: RegisterPositionRequest) =>
                    handleUpdatePosition({
                      id: position.id,
                      ...props,
                    })
                  }
                  isSubmitting={isUpdatingPosition}
                />
              </Dialog.Content>
            </Dialog.Root>
          </div>
        </div>
      ))}
    </div>
  )
}

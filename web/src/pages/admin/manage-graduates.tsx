import { StatusEnum } from '@/api/@responses/enums/statusEnum'
import { fetchUnverifiedGraduates } from '@/api/graduate/fetch-unverified.graduates'
import { updateGraduateStatus } from '@/api/graduate/update-graduate-status'
import { GraduateCard } from '@/components/graduate-card'
import { Pagination } from '@/components/pagination'
import { Button } from '@/components/ui/button'
import { Skeleton } from '@/components/ui/skeleton'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useSearchParams } from 'react-router'
import { toast } from 'sonner'
import { z } from 'zod'

export function ManageGraduates() {
  const [searchParams, setSearchParams] = useSearchParams()
  const queryClient = useQueryClient()

  const pageIndex = z.coerce
    .number()
    .transform((page) => page - 1)
    .parse(searchParams.get('page') ?? '1')

  const { data: pageGraduate, isLoading: isFetchingGraduates } = useQuery({
    queryKey: ['graduates', 'unverified', pageIndex],
    queryFn: async () => {
      return await fetchUnverifiedGraduates({
        pageIndex,
      })
    },
  })

  const {
    isPending: isUpdatingGraduateStatus,
    mutateAsync: mutateUpdateGraduateStatus,
  } = useMutation({
    mutationFn: updateGraduateStatus,
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: ['graduates'],
      })
    },
  })

  function handlePaginate(pageIndex: number) {
    setSearchParams((prev) => {
      prev.set('page', (pageIndex + 1).toString())

      return prev
    })
  }

  async function handleRejectGraduate(graduateId: string) {
    try {
      await mutateUpdateGraduateStatus({
        graduateId,
        newStatus: StatusEnum.REJECTED,
      })
      toast.success('Registro atualizado')
    } catch {
      toast.error('Falha ao atualizar o egresso')
    }
  }

  async function handleAcceptGraduate(graduateId: string) {
    try {
      await mutateUpdateGraduateStatus({
        graduateId,
        newStatus: StatusEnum.ACCEPTED,
      })
      toast.success('Registro atualizado')
    } catch {
      toast.error('Falha ao atualizar o egresso')
    }
  }

  return (
    <div className='space-y-6 w-full'>
      <h1 className='text-xl font-bold'>Egressos</h1>

      {isFetchingGraduates && <Skeleton className='h-40' />}

      {pageGraduate?.content.map((graduate) => (
        <div
          className='rounded-xl shadow border-px border-border pb-6'
          key={graduate.id}
        >
          <GraduateCard
            graduate={graduate}
            className='border-none shadow-none pb-2'
            showStatus
          />
          <div className='flex items-center justify-center gap-4'>
            <Button
              variant='destructive'
              disabled={isUpdatingGraduateStatus}
              onClick={() => handleRejectGraduate(graduate.id)}
            >
              Rejeitar
            </Button>
            <Button
              disabled={isUpdatingGraduateStatus}
              onClick={() => handleAcceptGraduate(graduate.id)}
            >
              Aceitar
            </Button>
          </div>
        </div>
      ))}

      {pageGraduate && (
        <Pagination
          onPageChange={handlePaginate}
          pageIndex={pageGraduate.pageIndex}
          totalElements={pageGraduate.totalElements}
          totalPages={pageGraduate.totalPages}
        />
      )}
    </div>
  )
}

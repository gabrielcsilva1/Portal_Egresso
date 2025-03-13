import { StatusEnum } from '@/api/@responses/enums/statusEnum'
import { fetchUnverifiedJobOpportunities } from '@/api/job-opportunity/fetch-unverified-job-opportunities'
import { updateJobOpportunityStatus } from '@/api/job-opportunity/update-job-opportunity-status'
import { JobOpportunityCard } from '@/components/job-opportunity-card'
import { Pagination } from '@/components/pagination'
import { Button } from '@/components/ui/button'
import { Skeleton } from '@/components/ui/skeleton'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useSearchParams } from 'react-router'
import { toast } from 'sonner'
import { z } from 'zod'

export function ManageJobOpportunities() {
  const [searchParams, setSearchParams] = useSearchParams()
  const queryClient = useQueryClient()

  const pageIndex = z.coerce
    .number()
    .transform((page) => page - 1)
    .parse(searchParams.get('page') ?? '1')

  const { data: pageJobOpportunities, isLoading: isFetchingJobOpportunities } =
    useQuery({
      queryKey: ['job-opportunities', 'unverified', pageIndex],
      queryFn: async () => {
        return await fetchUnverifiedJobOpportunities({
          pageIndex,
        })
      },
    })

  const {
    isPending: isUpdatingJobOpportunitiesStatus,
    mutateAsync: mutateUpdateJobOpportunityStatus,
  } = useMutation({
    mutationFn: updateJobOpportunityStatus,
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: ['job-opportunities'],
      })
    },
  })

  function handlePaginate(pageIndex: number) {
    setSearchParams((prev) => {
      prev.set('page', (pageIndex + 1).toString())

      return prev
    })
  }

  async function handleRejectJobOpportunity(jobId: string) {
    try {
      await mutateUpdateJobOpportunityStatus({
        jobId,
        newStatus: StatusEnum.REJECTED,
      })
      toast.success('Registro atualizado')
    } catch {
      toast.error('Falha ao atualizar')
    }
  }

  async function handleAcceptJobOpportunity(jobId: string) {
    try {
      await mutateUpdateJobOpportunityStatus({
        jobId,
        newStatus: StatusEnum.ACCEPTED,
      })
      toast.success('Registro atualizado')
    } catch {
      toast.error('Falha ao atualizar')
    }
  }

  return (
    <div className='space-y-6 w-full'>
      <h1 className='text-xl font-bold'>Egressos</h1>

      {isFetchingJobOpportunities && <Skeleton className='h-40' />}

      {pageJobOpportunities?.content.map((jobOpportunity) => (
        <div
          className='rounded-xl shadow border-px border-border pb-6'
          key={jobOpportunity.id}
        >
          <JobOpportunityCard
            graduate={jobOpportunity.graduate}
            jobOpportunity={jobOpportunity}
            className='border-none shadow-none pb-2'
            showStatus
          />
          <div className='flex items-center justify-center gap-4'>
            <Button
              variant='destructive'
              disabled={isUpdatingJobOpportunitiesStatus}
              onClick={() => handleRejectJobOpportunity(jobOpportunity.id)}
            >
              Rejeitar
            </Button>
            <Button
              disabled={isUpdatingJobOpportunitiesStatus}
              onClick={() => handleAcceptJobOpportunity(jobOpportunity.id)}
            >
              Aceitar
            </Button>
          </div>
        </div>
      ))}

      {pageJobOpportunities && (
        <Pagination
          onPageChange={handlePaginate}
          pageIndex={pageJobOpportunities.pageIndex}
          totalElements={pageJobOpportunities.totalElements}
          totalPages={pageJobOpportunities.totalPages}
        />
      )}
    </div>
  )
}

import { StatusEnum } from '@/api/@responses/enums/statusEnum'
import { fetchUnverifiedTestimonials } from '@/api/testimonial/fetch-unverified.testimonials'
import { updateTestimonialStatus } from '@/api/testimonial/update-testimonial-status'
import { Pagination } from '@/components/pagination'
import { TestimonialCard } from '@/components/testimonial-card'
import { Button } from '@/components/ui/button'
import { Skeleton } from '@/components/ui/skeleton'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useSearchParams } from 'react-router'
import { toast } from 'sonner'
import { z } from 'zod'

export function ManageTestimonials() {
  const [searchParams, setSearchParams] = useSearchParams()
  const queryClient = useQueryClient()

  const pageIndex = z.coerce
    .number()
    .transform((page) => page - 1)
    .parse(searchParams.get('page') ?? '1')

  const { data: pageTestimonials, isLoading: isFetchingTestimonials } =
    useQuery({
      queryKey: ['testimonials', 'unverified', pageIndex],
      queryFn: async () => {
        return await fetchUnverifiedTestimonials({
          pageIndex,
        })
      },
    })

  const {
    isPending: isUpdatingTestimonialStatus,
    mutateAsync: mutateUpdateTestimonialStatus,
  } = useMutation({
    mutationFn: updateTestimonialStatus,
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: ['testimonials'],
      })
    },
  })

  function handlePaginate(pageIndex: number) {
    setSearchParams((prev) => {
      prev.set('page', (pageIndex + 1).toString())

      return prev
    })
  }

  async function handleRejectTestimonial(testimonialId: string) {
    try {
      await mutateUpdateTestimonialStatus({
        testimonialId,
        newStatus: StatusEnum.REJECTED,
      })
      toast.success('Registro atualizado')
    } catch {
      toast.error('Falha ao atualizar')
    }
  }

  async function handleAcceptTestimonial(testimonialId: string) {
    try {
      await mutateUpdateTestimonialStatus({
        testimonialId,
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

      {isFetchingTestimonials && <Skeleton className='h-40' />}

      {pageTestimonials?.content.map((testimonial) => (
        <div
          className='rounded-xl shadow border-px border-border pb-6'
          key={testimonial.id}
        >
          <TestimonialCard
            graduate={testimonial.graduate}
            testimonial={testimonial}
            className='border-none shadow-none pb-2'
            showStatus
          />
          <div className='flex items-center justify-center gap-4'>
            <Button
              variant='destructive'
              disabled={isUpdatingTestimonialStatus}
              onClick={() => handleRejectTestimonial(testimonial.id)}
            >
              Rejeitar
            </Button>
            <Button
              disabled={isUpdatingTestimonialStatus}
              onClick={() => handleAcceptTestimonial(testimonial.id)}
            >
              Aceitar
            </Button>
          </div>
        </div>
      ))}

      {pageTestimonials && (
        <Pagination
          onPageChange={handlePaginate}
          pageIndex={pageTestimonials.pageIndex}
          totalElements={pageTestimonials.totalElements}
          totalPages={pageTestimonials.totalPages}
        />
      )}
    </div>
  )
}

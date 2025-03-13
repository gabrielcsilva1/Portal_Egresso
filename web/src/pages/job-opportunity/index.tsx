import { JobOpportunityCard } from '@/components/job-opportunity-card'
import { Pagination } from '@/components/pagination'
import { Skeleton } from '@/components/ui/skeleton'
import { useJobOpportunities } from '@/hooks/use-job-opportunities'
import { Navigate, useSearchParams } from 'react-router'
import { z } from 'zod'

export function JobOpportunity() {
  const [searchParams, setSearchParams] = useSearchParams()
  const pageIndex = z.coerce
    .number()
    .transform((page) => page - 1)
    .parse(searchParams.get('page') ?? '1')

  const { data: pageJobOpportunities, isLoading: isFetchingJobOpportunities } =
    useJobOpportunities({ pageIndex })

  function handlePaginate(pageIndex: number) {
    setSearchParams((prev) => {
      prev.set('page', (pageIndex + 1).toString())

      return prev
    })
  }

  if (isFetchingJobOpportunities) {
    return (
      <>
        <Skeleton className='w-64 h-14 mx-auto mb-6' />
        <Skeleton className='w-full h-96 max-w-5xl mx-auto' />
        <Skeleton className='w-full h-96 max-w-5xl mx-auto mt-6' />
      </>
    )
  }

  if (!pageJobOpportunities) {
    return <Navigate to='/' />
  }

  return (
    <div className='w-full px-20'>
      <div className='max-w-5xl mx-auto my-8'>
        <h1 className='text-center text-3xl font-bold'>
          Oportunidades de emprego
        </h1>
        <div className='mt-6 flex flex-col gap-8 mb-4'>
          {pageJobOpportunities.content.map((jobOpportunity) => (
            <JobOpportunityCard
              jobOpportunity={jobOpportunity}
              graduate={jobOpportunity.graduate}
              key={jobOpportunity.id}
            />
          ))}
        </div>
        <Pagination
          onPageChange={handlePaginate}
          pageIndex={pageJobOpportunities.pageIndex}
          totalElements={pageJobOpportunities.totalElements}
          totalPages={pageJobOpportunities.totalPages}
        />
      </div>
    </div>
  )
}

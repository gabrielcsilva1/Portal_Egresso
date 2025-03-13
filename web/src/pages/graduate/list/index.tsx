import { fetchGraduates } from '@/api/graduate/fetch-graduates'
import { GraduateCard } from '@/components/graduate-card'
import { Pagination } from '@/components/pagination'
import { Skeleton } from '@/components/ui/skeleton'
import { useQuery } from '@tanstack/react-query'
import { Navigate, useSearchParams } from 'react-router'
import { z } from 'zod'
import { GraduateFilterForm } from './graduate-filter-form'

export function Graduates() {
  const [searchParams, setSearchParams] = useSearchParams()

  const stringYear = searchParams.get('year')
  const year = stringYear ? Number(stringYear) : null
  const courseName = searchParams.get('courseName')
  const query = searchParams.get('query')

  const pageIndex = z.coerce
    .number()
    .transform((page) => page - 1)
    .parse(searchParams.get('page') ?? '1')

  const { data: pageGraduates, isLoading: isFetchingGraduates } = useQuery({
    queryKey: ['graduates', pageIndex, year, courseName, query],
    queryFn: async () => fetchGraduates({ pageIndex, courseName, query, year }),
  })

  function handlePaginate(pageIndex: number) {
    setSearchParams((prev) => {
      prev.set('page', (pageIndex + 1).toString())

      return prev
    })
  }

  if (isFetchingGraduates) {
    return (
      <>
        <Skeleton className='w-64 h-14 mx-auto mb-6' />
        <Skeleton className='w-full h-96 max-w-5xl mx-auto' />
        <Skeleton className='w-full h-96 max-w-5xl mx-auto mt-6' />
      </>
    )
  }

  if (!pageGraduates) {
    return <Navigate to='/' />
  }

  return (
    <div className='w-full px-20'>
      <div className='max-w-5xl mx-auto my-8'>
        <h1 className='text-center text-3xl font-bold'>Egressos</h1>
        <GraduateFilterForm />
        <div className='mt-6 flex flex-col gap-8 mb-4'>
          {pageGraduates.content.map((graduate) => (
            <GraduateCard graduate={graduate} key={graduate.id} />
          ))}
        </div>
        <Pagination
          onPageChange={handlePaginate}
          pageIndex={pageGraduates.pageIndex}
          totalElements={pageGraduates.totalElements}
          totalPages={pageGraduates.totalPages}
        />
      </div>
    </div>
  )
}

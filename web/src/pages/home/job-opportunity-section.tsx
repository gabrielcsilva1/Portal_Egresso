import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'
import { useJobOpportunities } from '@/hooks/use-job-opportunities'
import { formatDistanceToNow } from '@/lib/utils'
import { Link } from 'react-router'

type Props = {
  className?: string
}

export function JobOpportunitySection({ className }: Props) {
  const {
    data: paginatedJobOpportunities,
    isLoading: isFetchingJobOpportunities,
  } = useJobOpportunities({ pageIndex: 0 })

  const showNoResultsMessage =
    paginatedJobOpportunities && paginatedJobOpportunities.totalElements === 0

  return (
    <div className={className}>
      <h1 className='text-4xl font-bold text-center'>Oportunidades</h1>
      {isFetchingJobOpportunities && <Skeleton className='w-full h-48 my-9' />}
      {showNoResultsMessage && (
        <span className='my-9 flex justify-center'>Sem resultados</span>
      )}

      {paginatedJobOpportunities && (
        <div className='flex flex-col gap-9 justify-center my-9 max-w-4xl mx-auto'>
          {paginatedJobOpportunities.content.map((jobOpportunity) => {
            return (
              <Card className='flex flex-col gap-6 p-6' key={jobOpportunity.id}>
                <div className='flex gap-4 items-center'>
                  <div className='space-y-1.5'>
                    <h1 className='font-semibold leading-none tracking-tight'>
                      {jobOpportunity.title}
                    </h1>
                    <p className='text-xs text-muted-foreground'>
                      publicado por{' '}
                      <Link
                        className='underline hover:text-primary transition-colors'
                        target='_blank'
                        to={`/graduate/${jobOpportunity.graduate.id}`}
                      >
                        {jobOpportunity.graduate.name}
                      </Link>
                    </p>
                  </div>
                  <p className='text-muted-foreground text-xs self-start ml-auto'>
                    {formatDistanceToNow(jobOpportunity.createdAt)}
                  </p>
                </div>
                <p className='text-sm tracking-normal leading-relaxed'>
                  {jobOpportunity.description}
                </p>
              </Card>
            )
          })}
          <Button asChild variant='link'>
            <Link to='/job-opportunity'>Ver Todos</Link>
          </Button>
        </div>
      )}
    </div>
  )
}

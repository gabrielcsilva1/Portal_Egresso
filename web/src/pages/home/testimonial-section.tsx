import { TestimonialCard } from '@/components/testimonial-card'
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel'
import { Skeleton } from '@/components/ui/skeleton'
import { useTestimonials } from '@/hooks/use-testimonials'

type Props = {
  className?: string
}

export function TestimonialSection({ className }: Props) {
  const { data: pageTestimonials, isLoading: isFetchingTestimonials } =
    useTestimonials({ pageIndex: 0 })

  const showNoResultsMessage =
    pageTestimonials && pageTestimonials.totalElements === 0

  return (
    <div className={className}>
      <h1 className='font-bold text-4xl text-center max-w-164 mx-auto leading-11'>
        Veja alguns dos depoimentos de nossos alunos
      </h1>

      {isFetchingTestimonials && <Skeleton className='w-full h-48' />}
      {showNoResultsMessage && (
        <span className='mt-9 flex justify-center'>Sem resultados</span>
      )}

      {/* Container dos cards */}
      {pageTestimonials && pageTestimonials.totalElements > 0 && (
        <Carousel opts={{ align: 'center' }} className='w-full mt-9'>
          <CarouselContent className='-ml-2'>
            {pageTestimonials.content.map((testimonial) => (
              <CarouselItem
                key={testimonial.id}
                className='xl:basis-1/2 2xl:basis-1/3'
              >
                <div className='px-2'>
                  <TestimonialCard
                    className='h-45'
                    testimonial={testimonial}
                    graduate={testimonial.graduate}
                  />
                </div>
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious />
          <CarouselNext />
        </Carousel>
      )}
      {/* Fim - Container dos cards */}
    </div>
  )
}

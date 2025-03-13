import {
  type FetchTestimonialsRequest,
  fetchTestimonials,
} from '@/api/testimonial/fetch-testimonials'
import { useQuery } from '@tanstack/react-query'

export function useTestimonials({ ...props }: FetchTestimonialsRequest) {
  const valuesArray = Object.values(props)

  return useQuery({
    queryKey: ['testimonials', ...valuesArray],
    queryFn: async () => {
      return await fetchTestimonials({
        ...props,
      })
    },
  })
}

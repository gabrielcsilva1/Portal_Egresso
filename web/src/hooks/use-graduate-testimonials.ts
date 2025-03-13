import { fetchGraduateTestimonials } from '@/api/testimonial/fetch-graduate-testimonials'
import { useQuery } from '@tanstack/react-query'

export function useGraduateTestimonials(graduateId: string) {
  return useQuery({
    queryKey: ['testimonials', graduateId],
    queryFn: async () => fetchGraduateTestimonials(graduateId),
    staleTime: Number.POSITIVE_INFINITY,
  })
}

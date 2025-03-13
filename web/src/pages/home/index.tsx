import heroSvg from '@/assets/hero.svg'
import { Button } from '@/components/ui/button'
import { Separator } from '@/components/ui/separator'
import { Link } from 'react-router'
import { JobOpportunitySection } from './job-opportunity-section'
import { TestimonialSection } from './testimonial-section'
import { ArrowRight } from 'lucide-react'

export function Home() {
  return (
    <div>
      {/* Portal Egresso Destaque */}
      <div className="flex flex-row gap-18 px-24 items-center justify-between bg-muted">
        <div className="py-14">
          <h1 className="text-4xl font-bold">Portal Egresso</h1>
          <p className="mt-4 leading-6.5 text-justify">
            Mantenha-se conectado! Nosso portal foi criado para reunir egressos do curso de
            Ciência da Computação, acompanhar suas trajetórias e fortalecer nossa comunidade
            acadêmica e profissional.
          </p>
          <Button className="mt-6" size="lg" asChild>
            <Link to="/graduate">
              Conheça nossos egressos
              <ArrowRight className="size-5" fontWeight={3} />
            </Link>
          </Button>
        </div>

        <img src={heroSvg} alt="" />
      </div>
      {/* fim - Portal Egresso Destaque */}
      <div className="px-24">
        {/* Sessão de depoimento */}
        <TestimonialSection className="mt-20" />
        {/* Fim - Sessão de depoimento */}

        <Separator className="my-14" />

        {/* Seção de oportunidades */}
        <JobOpportunitySection />
      </div>
    </div>
  )
}

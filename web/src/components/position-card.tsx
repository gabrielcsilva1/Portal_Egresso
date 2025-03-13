import { cn } from '@/lib/utils'
import { Card } from './ui/card'

type Props = {
  position: {
    location: string
    description: string
    startYear: number
    endYear: number | null
  }
  className?: string
}

export function PositionCard({ position, className }: Props) {
  return (
    <Card className={cn('p-4', className)}>
      <p className='text-sm'>
        <span className='font-bold'>Local: </span>
        {position.location}
      </p>
      <p className='text-sm mt-2'>
        <span className='font-bold'>Descrição: </span>
        {position.description}
      </p>

      <div className='flex gap-4 text-muted-foreground text-sm mt-4'>
        <p>Inicio: {position.startYear}</p>
        <p>Fim: {position.endYear ?? '?'}</p>
      </div>
    </Card>
  )
}

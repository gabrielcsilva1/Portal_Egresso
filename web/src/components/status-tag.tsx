import { StatusEnum } from '@/api/@responses/enums/statusEnum'
import { cn } from '@/lib/utils'

type Props = {
  status: StatusEnum
  className?: string
}

export function StatusTag({ status, className }: Props) {
  return (
    <div className={cn('flex gap-1 items-center text-xs', className)}>
      <div
        className={cn('size-1.5 rounded-full', {
          'bg-emerald-500': status === StatusEnum.ACCEPTED,
          'bg-red-500': status === StatusEnum.REJECTED,
          'bg-amber-500': status === StatusEnum.PENDING,
        })}
      />

      {status === StatusEnum.ACCEPTED && 'aceito'}
      {status === StatusEnum.REJECTED && 'recusado'}
      {status === StatusEnum.PENDING && 'pendente'}
    </div>
  )
}

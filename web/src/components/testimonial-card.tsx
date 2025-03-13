import type { StatusEnum } from '@/api/@responses/enums/statusEnum'
import { cn, formatDate } from '@/lib/utils'
import { formatCourseName } from '@/utils/format-course-name'
import { Link } from 'react-router'
import { StatusTag } from './status-tag'
import { Avatar, AvatarFallback, AvatarImage } from './ui/avatar'
import { Card } from './ui/card'

interface Props {
  testimonial: {
    text: string
    registrationStatus: StatusEnum
    createdAt: Date | string
  }
  graduate: {
    id: string
    avatarUrl: string | null
    name: string
    courses: Array<{
      name: string
      level: string
    }>
  }
  showStatus?: boolean
  className?: string
}

export function TestimonialCard({
  testimonial,
  graduate,
  showStatus = false,
  className,
}: Props) {
  return (
    <Card className={cn('p-4 flex flex-col gap-4', className)}>
      <div className='flex justify-between items-center gap-2'>
        <div className='flex items-center gap-1'>
          <Link to={`/graduate/${graduate.id}`}>
            <Avatar className='size-12'>
              <AvatarImage src={graduate.avatarUrl || ''} />
              <AvatarFallback avatarName={graduate.name} />
            </Avatar>
          </Link>
          <div className='space-y-1'>
            <Link
              className='font-semibold hover:underline hover:text-primary transition-colors'
              to={`/graduate/${graduate.id}`}
            >
              {graduate.name}
            </Link>
            <p className='text-xs text-muted-foreground'>
              {formatCourseName({ ...graduate.courses[0] })}
            </p>
          </div>
        </div>

        {showStatus ? (
          <StatusTag
            status={testimonial.registrationStatus}
            className='self-start'
          />
        ) : (
          <span className='text-xs text-muted-foreground self-start'>
            {formatDate(testimonial.createdAt)}
          </span>
        )}
      </div>
      <p className='text-justify text-sm'>"{testimonial.text}"</p>
    </Card>
  )
}

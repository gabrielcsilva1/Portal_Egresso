import type { ShortGraduateResponse } from '@/api/@responses/graduate/short-graduate-response'
import { cn } from '@/lib/utils'
import { formatCourseName } from '@/utils/format-course-name'
import { Link } from 'react-router'
import { StatusTag } from './status-tag'
import { Avatar, AvatarFallback, AvatarImage } from './ui/avatar'
import { Card } from './ui/card'
import { Separator } from './ui/separator'

type Props = {
  graduate: ShortGraduateResponse
  showStatus?: boolean
  className?: string
}

export function GraduateCard({
  graduate,
  showStatus = false,
  className,
}: Props) {
  return (
    <Card className={cn('p-6', className)}>
      <div className='flex gap-2 items-center'>
        <Link to={`/graduate/${graduate.id}`}>
          <Avatar>
            <AvatarImage src={graduate.avatarUrl || ''} />
            <AvatarFallback avatarName={graduate.name} />
          </Avatar>
        </Link>
        <Link
          to={`/graduate/${graduate.id}`}
          className='font-bold flex-1 text-xl hover:underline hover:text-primary transition-colors'
        >
          {graduate.name}
        </Link>

        {showStatus && (
          <StatusTag
            status={graduate.registrationStatus}
            className='self-start'
          />
        )}
      </div>

      <Separator className='h-px my-3' />

      <h2 className='font-bold mb-2'>Cursos:</h2>
      <ul className='flex flex-col gap-3  text-sm'>
        {graduate.courses.map((course) => (
          <li className='text-sm flex flex-col gap-1' key={course.registerId}>
            <p>
              {formatCourseName({
                name: course.name,
                level: course.level,
              })}
            </p>
            <span className='text-muted-foreground'>
              Início: {course.startYear}
            </span>
            <span className='text-muted-foreground'>
              Término: {course.endYear ?? '-'}
            </span>
          </li>
        ))}
      </ul>
    </Card>
  )
}

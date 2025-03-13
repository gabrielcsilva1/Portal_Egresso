import { PositionCard } from '@/components/position-card'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Separator } from '@/components/ui/separator'
import { Skeleton } from '@/components/ui/skeleton'
import { useGraduate } from '@/hooks/use-graduate'
import { formatCourseName } from '@/utils/format-course-name'
import { InstagramLogo, LinkedinLogo } from '@phosphor-icons/react'
import { ExternalLink } from 'lucide-react'
import { Link, Navigate, useParams } from 'react-router'

export function Profile() {
  const { graduateId } = useParams<{ graduateId: string }>()

  const { data: graduate, isLoading } = useGraduate(graduateId ?? '')

  if (isLoading) {
    return (
      <div className='flex gap-4 justify-center px-3 max-w-7xl flex-1 w-full mx-auto pb-8'>
        <Skeleton className='w-96 h-auto ' />
        <div className='w-full space-y-5'>
          <Skeleton className='flex w-full h-36' />
          <Skeleton className='flex w-full h-48' />
        </div>
      </div>
    )
  }

  if (!graduate) {
    return <Navigate to='/' />
  }

  return (
    <div className='min-h-screen bg-background py-8'>
      <div className='max-w-7xl mx-auto px-3 grid grid-cols-[320px_1fr] gap-8'>
        {/* Informação principal do perfil*/}
        <div className='flex flex-col items-start border border-border rounded-md p-4'>
          <Avatar className='size-72'>
            <AvatarImage src={graduate.avatarUrl || ''} />
            <AvatarFallback className='text-4xl' avatarName={graduate.name} />
          </Avatar>
          <h1 className='font-bold text-2xl my-4 text-foreground'>
            {graduate.name}
          </h1>

          {/* Tags */}
          <div className='flex flex-wrap gap-4 text-secondary-foreground text-sm'>
            {graduate.linkedin && (
              <div className='flex gap-2 items-center'>
                <LinkedinLogo className='size-4' weight='fill' />
                <p>{graduate.linkedin}</p>
              </div>
            )}
            {graduate.instagram && (
              <div className='flex gap-2 items-center'>
                <InstagramLogo className='size-4' weight='fill' />
                <p>{graduate.instagram}</p>
              </div>
            )}
          </div>
          <Separator className='h-px my-4' />
          {graduate.curriculum && (
            <Link
              target='_blank'
              to={graduate.curriculum}
              className='text-primary font-semibold hover:underline flex gap-2 items-center self-end'
            >
              Currículo
              <ExternalLink className='size-5' />
            </Link>
          )}
        </div>
        {/* fim - Informação principal do perfil*/}

        <div className='space-y-6'>
          {/* Sessão de descrição */}
          <div className='space-y-4 border border-border rounded-md p-6 shadow'>
            <h1 className='font-bold text-2xl'>{graduate.name}</h1>
            <Separator />
            <p className='text-sm text-foreground leading-relaxed tracking-normal'>
              {graduate.description}
            </p>
          </div>
          {/* fim - Sessão de descrição */}
          {/* Sessão de cursos */}
          <div className='space-y-4 border border-border rounded-md p-6 shadow'>
            <h1 className='font-bold text-2xl'>Cursos</h1>
            <Separator />
            {/* Tags dos cursos */}
            <div className='flex gap-2.5 flex-wrap text-sm'>
              {graduate.courses.map((course) => (
                <div
                  className='px-2 py-1 rounded-full bg-secondary text-secondary-foreground'
                  key={course.id}
                >
                  <p>{formatCourseName({ ...course })}</p>
                </div>
              ))}
            </div>
          </div>
          {/* fim - Sessão de cursos */}
          {/* Sessão de descrição */}
          <div className='space-y-4 border border-border rounded-md p-6 shadow'>
            <h1 className='font-bold text-2xl'>Cargos</h1>
            <Separator />
            <div className='grid grid-cols-1 xl:grid-cols-2 gap-x-2 gap-y-4'>
              {graduate.positions.map((position) => (
                <PositionCard position={position} key={position.id} />
              ))}
            </div>
          </div>
          {/* fim - Sessão de descrição */}
        </div>
      </div>
    </div>
  )
}

import type { ShortGraduateResponse } from '@/api/@responses/graduate/short-graduate-response'
import { logout } from '@/api/profile/logout'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import {
  BriefcaseBusiness,
  LogOut,
  PlaneTakeoff,
  Quote,
  Settings,
  UsersRound,
} from 'lucide-react'
import { Link } from 'react-router'
import { Avatar, AvatarFallback, AvatarImage } from './ui/avatar'
import { Button } from './ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from './ui/drop-down-menu'

type Props = {
  graduate: ShortGraduateResponse
}

export function GraduateMenu({ graduate }: Props) {
  const queryClient = useQueryClient()
  const { mutateAsync: mutateLogout, isPending: isLogoutPending } = useMutation(
    {
      mutationFn: logout,
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ['profile'],
        })
      },
    }
  )
  return (
    <DropdownMenu>
      <DropdownMenuTrigger className='p-2 rounded-full'>
        <Avatar>
          <AvatarImage src={graduate.avatarUrl || ''} />
          <AvatarFallback avatarName={graduate.name} />
        </Avatar>
      </DropdownMenuTrigger>
      <DropdownMenuContent className='px-2'>
        <DropdownMenuLabel className='mb-2'>{graduate.name}</DropdownMenuLabel>
        <DropdownMenuGroup className='w-48'>
          <DropdownMenuItem>
            <Link
              to={`/graduate/${graduate.id}`}
              className='flex flex-1 gap-2 items-center'
            >
              <UsersRound className='size-4' />
              Perfil
            </Link>
          </DropdownMenuItem>
          <DropdownMenuItem>
            <Link
              to='/graduate/manage/profile'
              className='flex gap-2 items-center flex-1'
            >
              <Settings className='size-4' />
              Configurar Perfil
            </Link>
          </DropdownMenuItem>
          <DropdownMenuItem>
            <Link
              to='/graduate/manage/testimonial'
              className='flex flex-1 gap-2 items-center '
            >
              <Quote className='size-4' />
              Depoimentos
            </Link>
          </DropdownMenuItem>

          <DropdownMenuItem>
            <Link
              to='/graduate/manage/job-opportunity'
              className='flex flex-1 gap-2 items-center'
            >
              <PlaneTakeoff className='size-4 shrink-0' />
              Oportunidades de emprego
            </Link>
          </DropdownMenuItem>

          <DropdownMenuItem>
            <Link
              to='/graduate/manage/position'
              className='flex flex-1 gap-2 items-center'
            >
              <BriefcaseBusiness className='size-4' />
              Cargos
            </Link>
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem>
            <Button
              onClick={() => mutateLogout()}
              className='p-0 flex-1 justify-start bg-background shadow-none text-foreground hover:bg-background/90 disabled:bg-background/50'
              disabled={isLogoutPending}
            >
              <LogOut className='size-4' />
              Sair
            </Button>
          </DropdownMenuItem>
        </DropdownMenuGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}

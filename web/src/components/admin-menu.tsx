import { logout } from '@/api/profile/logout'
import { DropdownMenuSeparator } from '@radix-ui/react-dropdown-menu'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import {
  BookText,
  LockKeyhole,
  LogOut,
  PlaneTakeoff,
  Quote,
  UsersRound,
} from 'lucide-react'
import { Link } from 'react-router'
import { Button } from './ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from './ui/drop-down-menu'

export function AdminMenu() {
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
      <DropdownMenuTrigger className='p-2 border-2 border-primary rounded-full'>
        <LockKeyhole className='size-6' />
      </DropdownMenuTrigger>
      <DropdownMenuContent className='px-2'>
        <DropdownMenuLabel className='mb-2'>Admin</DropdownMenuLabel>
        <DropdownMenuGroup className='w-48'>
          <DropdownMenuItem>
            <Link
              to='/manage/graduate'
              className='flex flex-1 gap-2 items-center'
            >
              <UsersRound className='size-4' />
              Egressos
            </Link>
          </DropdownMenuItem>
          <DropdownMenuItem>
            <Link
              to='/manage/testimonial'
              className='flex flex-1 gap-2 items-center'
            >
              <Quote className='size-4' />
              Depoimentos
            </Link>
          </DropdownMenuItem>

          <DropdownMenuItem>
            <Link
              to='/manage/course'
              className='flex flex-1 gap-2 items-center'
            >
              <BookText className='size-4' />
              Cursos
            </Link>
          </DropdownMenuItem>

          <DropdownMenuItem>
            <Link
              to='/manage/job-opportunity'
              className='flex flex-1 gap-2 items-center'
            >
              <PlaneTakeoff className='size-4 shrink-0' />
              Oportunidades de emprego
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

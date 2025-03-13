import { RoleEnum } from '@/api/@responses/enums/roleEnum'
import { useProfile } from '@/hooks/use-profile'
import { Link } from 'react-router'
import { AdminMenu } from './admin-menu'
import { GraduateMenu } from './graduate-menu'
import { Logo } from './logo'
import { NavLink } from './nav-link'
import { Button } from './ui/button'
import { Skeleton } from './ui/skeleton'

export function Header() {
  const { data: profileInfo, isLoading: isFetchingProfile } = useProfile()

  const isGraduateAuth = profileInfo && profileInfo.role === RoleEnum.GRADUATE
  const isCoordinatorAuth =
    profileInfo && profileInfo.role === RoleEnum.COORDINATOR
  const isCommonUser = !isFetchingProfile && !profileInfo

  return (
    <header className='gap-8 h-20 px-10 flex items-center bg-background sticky top-0 z-10'>
      {/* Navegação */}
      <Link to='/' aria-label='Voltar para página principal'>
        <Logo />
      </Link>
      <nav className='lg:ml-10 flex flex-1'>
        <ul className='flex items-center gap-6 font-medium'>
          <li>
            <NavLink to='/graduate'>Egressos</NavLink>
          </li>
          <li>
            <NavLink to='/job-opportunity'>Oportunidades</NavLink>
          </li>
        </ul>
      </nav>

      {isFetchingProfile && <Skeleton className='h-10 w-24' />}

      {isCoordinatorAuth && <AdminMenu />}
      {isGraduateAuth && <GraduateMenu graduate={profileInfo.user} />}

      {isCommonUser && (
        <div className='flex gap-6'>
          <>
            <Button asChild variant='ghost'>
              <Link to='/sign-up'>Cadastrar-se</Link>
            </Button>

            <Button asChild>
              <Link to='/sign-in'>Entrar</Link>
            </Button>
          </>
        </div>
      )}
    </header>
  )
}

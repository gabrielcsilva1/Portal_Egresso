import { RoleEnum } from '@/api/@responses/enums/roleEnum'
import type { ShortGraduateResponse } from '@/api/@responses/graduate/short-graduate-response'
import { NavLink } from '@/components/nav-link'
import { Spinner } from '@/components/spinner'
import { useProfile } from '@/hooks/use-profile'
import {
  BriefcaseBusiness,
  PlaneTakeoff,
  Quote,
  UsersRound,
} from 'lucide-react'
import { Navigate, Outlet } from 'react-router'

export type GraduateConfigLayoutContextProps = {
  graduate: ShortGraduateResponse
}

export function GraduateConfigLayout() {
  const { data: profileInfo, isLoading } = useProfile()

  if (isLoading) {
    return (
      <div className='flex flex-1 justify-center items-center'>
        <Spinner />
      </div>
    )
  }

  if (!profileInfo || (profileInfo && profileInfo.role !== RoleEnum.GRADUATE)) {
    return <Navigate to='/' replace />
  }

  return (
    <div className='flex flex-1'>
      {/* Sidebar */}
      <aside className='px-10 py-4 max-w-56 border-r border-border'>
        <h1 className='font-bold'>Configuração</h1>
        <ul className='space-y-4 pl-2 mt-4'>
          <li>
            <NavLink to='/graduate/manage/profile'>
              <UsersRound className='size-5 shrink-0' />
              Perfil
            </NavLink>
          </li>
          <li>
            <NavLink to='/graduate/manage/testimonial'>
              <Quote className='size-5 shrink-0' />
              Depoimentos
            </NavLink>
          </li>
          <li>
            <NavLink to='/graduate/manage/job-opportunity'>
              <PlaneTakeoff className='size-5 shrink-0' />
              Oportunidades de emprego
            </NavLink>
          </li>
          <li>
            <NavLink to='/graduate/manage/position'>
              <BriefcaseBusiness className='size-5 shrink-0' />
              Cargos
            </NavLink>
          </li>
        </ul>
      </aside>
      {/* fim - Sidebar */}

      <div className='px-6 py-4 w-full max-w-4xl'>
        <Outlet
          context={{
            graduate: profileInfo.user,
          }}
        />
      </div>
    </div>
  )
}

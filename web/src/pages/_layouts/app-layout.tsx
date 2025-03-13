import { Header } from '@/components/header'
import { Outlet } from 'react-router'

export function AppLayout() {
  return (
    <div className='min-h-screen flex flex-col'>
      <Header />
      <Outlet />
    </div>
  )
}

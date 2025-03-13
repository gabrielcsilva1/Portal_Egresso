import { NavLink } from '@/components/nav-link'
import { Button } from '@/components/ui/button'
import { ArrowLeft } from 'lucide-react'
import { Outlet } from 'react-router'

export function AuthLayout() {
  return (
    <div className="grid min-h-screen grid-cols-2">
      <div className="flex flex-col justify-between items-start px-10 py-5 bg-muted">
        <Button variant="ghost" className="px-0" asChild>
          <NavLink to="/">
            <ArrowLeft className="size-5" />
            Página Principal
          </NavLink>
        </Button>
        <p className="text-muted-foreground text-sm">
          PortalEgresso © {new Date().getFullYear()} Todos os direitos reservados
        </p>
      </div>

      <Outlet />
    </div>
  )
}

import { cn } from '@/lib/utils'
import { Link, type LinkProps, useLocation } from 'react-router'

export function NavLink({ className, ...rest }: LinkProps) {
  const { pathname } = useLocation()
  return (
    <Link
      data-active={pathname === rest.to}
      className={cn(
        'flex gap-2 items-center text-muted-foreground hover:text-foreground pb-1 text-sm transition-colors border-b-2 border-transparent data-[active=true]:border-primary data-[active=true]:text-primary',
        className
      )}
      {...rest}
    />
  )
}

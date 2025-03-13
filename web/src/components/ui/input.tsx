import type { ComponentProps } from 'react'

import { cn } from '@/lib/utils'

interface Props extends ComponentProps<'input'> {
  error?: string
}

const Input = ({ error, className, ...props }: Props) => {
  const hasError = Boolean(error)
  return (
    <>
      <span className='text-xs text-destructive'>{hasError && error}</span>
      <input
        className={cn(
          'flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-sm transition-colors file:border-0 file:bg-transparent file:text-sm file:font-medium file:text-foreground placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm',
          className
        )}
        {...props}
      />
    </>
  )
}

Input.displayName = 'Input'

export { Input }

import type * as React from 'react'

import { cn } from '@/lib/utils'

type Props = React.ComponentProps<'textarea'> & {
  error?: string
}

const Textarea = ({ error, className, ...props }: Props) => {
  const hasError = Boolean(error)
  return (
    <>
      <span className='text-xs text-red-500'>{hasError && error}</span>
      <textarea
        className={cn(
          'flex min-h-36 w-full rounded-md border border-input bg-transparent px-3 py-2 text-base shadow-sm placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm',
          className
        )}
        {...props}
      />
    </>
  )
}
Textarea.displayName = 'Textarea'

export { Textarea }

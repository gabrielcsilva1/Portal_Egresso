import { cn } from '@/lib/utils'
import * as RadixDialog from '@radix-ui/react-dialog'
import type { ComponentProps } from 'react'

function Root({ children, ...rest }: ComponentProps<typeof RadixDialog.Root>) {
  return (
    <RadixDialog.Root {...rest}>
      <RadixDialog.Overlay className='bg-zinc-950/25 fixed inset-0 z-30' />
      {children}
    </RadixDialog.Root>
  )
}
const Trigger = RadixDialog.Trigger

const Close = RadixDialog.Close

function Title({
  className,
  ...rest
}: ComponentProps<typeof RadixDialog.Title>) {
  return <RadixDialog.Title className={cn('font-bold', className)} {...rest} />
}

function Description({
  className,
  ...rest
}: ComponentProps<typeof RadixDialog.Description>) {
  return (
    <RadixDialog.Description className={cn('text-sm', className)} {...rest} />
  )
}

function Content({
  className,
  ...props
}: ComponentProps<typeof RadixDialog.Content>) {
  return (
    <RadixDialog.Content
      className={cn(
        'bg-background border border-border focus:outline-0 rounded-lg fixed top-1/2 left-1/2 -translate-1/2 p-4 shadow z-50',
        className
      )}
      {...props}
    />
  )
}

export const Dialog = {
  Root,
  Trigger,
  Content,
  Title,
  Description,
  Close,
}

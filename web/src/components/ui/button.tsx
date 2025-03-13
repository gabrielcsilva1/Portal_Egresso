import { Slot } from '@radix-ui/react-slot'
import { type VariantProps, cva } from 'class-variance-authority'
import type { ComponentProps } from 'react'

import { cn } from '@/lib/utils'

const buttonVariants = cva(
  'inline-flex items-center cursor-pointer justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0',
  {
    variants: {
      variant: {
        default:
          'bg-primary text-primary-foreground shadow hover:bg-primary/90 disabled:bg-primary/50',
        destructive:
          'bg-destructive text-destructive-foreground shadow-sm hover:bg-destructive/90 disabled:bg-destructive/50',
        outline:
          'border border-input bg-background shadow-sm hover:bg-accent hover:text-accent-foreground disabled:bg-background/50',
        secondary:
          'bg-secondary text-secondary-foreground shadow-sm hover:bg-secondary/80 disabled:bg-secondary/50',
        ghost:
          'hover:bg-accent hover:text-accent-foreground disabled:bg-background/50',
        link: 'text-primary underline-offset-4 hover:underline disabled:bg-background/50',
      },
      size: {
        default: 'h-10 px-8',
        xs: 'h-8 px-3 text-xs',
        sm: 'h-9 px-4 py-2',
        lg: 'h-12 px-5 text-md',
        icon: 'h-9 w-9',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  }
)

export interface ButtonProps
  extends ComponentProps<'button'>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean
}

const Button = ({
  className,
  variant,
  size,
  asChild = false,
  ...props
}: ButtonProps) => {
  const Comp = asChild ? Slot : 'button'
  return (
    <Comp
      className={cn(buttonVariants({ variant, size, className }))}
      {...props}
    />
  )
}
Button.displayName = 'Button'

export { Button, buttonVariants }

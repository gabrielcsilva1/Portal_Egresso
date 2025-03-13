import * as LabelPrimitive from '@radix-ui/react-label'
import { type VariantProps, cva } from 'class-variance-authority'

import { cn } from '@/lib/utils'
import type { ComponentProps } from 'react'

// https://ui.shadcn.com/docs/components/label
const labelVariants = cva(
  'text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70'
)

type Props = VariantProps<typeof labelVariants> &
  ComponentProps<typeof LabelPrimitive.Root>
const Label = ({ className, ...props }: Props) => (
  <LabelPrimitive.Root className={cn(labelVariants(), className)} {...props} />
)
Label.displayName = LabelPrimitive.Root.displayName

export { Label }

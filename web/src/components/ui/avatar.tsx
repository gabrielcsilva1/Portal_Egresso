import * as AvatarPrimitive from '@radix-ui/react-avatar'

import { cn } from '@/lib/utils'
import type { ComponentProps } from 'react'

const Avatar = ({
  className,
  ...props
}: ComponentProps<typeof AvatarPrimitive.Root>) => (
  <AvatarPrimitive.Root
    className={cn(
      'relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full',
      className
    )}
    {...props}
  />
)

Avatar.displayName = AvatarPrimitive.Root.displayName

const AvatarImage = ({
  className,
  ...props
}: ComponentProps<typeof AvatarPrimitive.Image>) => (
  <AvatarPrimitive.Image
    className={cn('aspect-square h-full w-full', className)}
    {...props}
  />
)
AvatarImage.displayName = AvatarPrimitive.Image.displayName

type AvatarFallbackProps = ComponentProps<typeof AvatarPrimitive.Fallback> & {
  avatarName?: string
}

const AvatarFallback = ({
  avatarName,
  className,
  children,
  ...props
}: AvatarFallbackProps) => {
  function getNameInitials(name: string) {
    const words = name.trim().split(' ')
    const initials = words
      .slice(0, 2)
      .map((word) => word[0].toUpperCase())
      .join('')

    return initials
  }

  return (
    <AvatarPrimitive.Fallback
      className={cn(
        'flex h-full w-full items-center justify-center rounded-full bg-muted',
        className
      )}
      {...props}
    >
      {avatarName ? getNameInitials(avatarName) : children}
    </AvatarPrimitive.Fallback>
  )
}

AvatarFallback.displayName = AvatarPrimitive.Fallback.displayName

export { Avatar, AvatarImage, AvatarFallback }

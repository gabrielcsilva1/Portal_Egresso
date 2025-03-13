type Props = {
  name: string
  level: string
}

export function formatCourseName({ name, level }: Props) {
  return `[${level}] - ${name}`
}

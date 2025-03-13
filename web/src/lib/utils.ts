import { type ClassValue, clsx } from 'clsx'
import type { DateArg, FormatDistanceToNowOptions } from 'date-fns'
import {
  formatDistanceToNow as dateFnsFormatDistanceToNow,
  format,
} from 'date-fns'
import { ptBR } from 'date-fns/locale'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatDistanceToNow(
  date: DateArg<Date> & {},
  options?: FormatDistanceToNowOptions
) {
  return dateFnsFormatDistanceToNow(date, {
    addSuffix: true,
    locale: ptBR,
    ...options,
  })
}

export function formatDate(date: DateArg<Date>) {
  return format(date, 'dd/MM/yyyy')
}

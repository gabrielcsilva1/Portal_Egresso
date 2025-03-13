import { Dialog } from '@/components/dialog'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/text-area'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

const formSchema = z.object({
  text: z
    .string()
    .min(1, 'Campo obrigatório')
    .max(255, 'limite de caractere atingido'),
})

type TestimonialFormSchema = z.infer<typeof formSchema>

type Props = {
  defaultValues?: TestimonialFormSchema
  onSubmit?: (props: TestimonialFormSchema) => Promise<void>
  isSubmitting?: boolean
}

export function TestimonialForm({
  defaultValues,
  onSubmit = async () => {},
  isSubmitting = false,
}: Props) {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    watch,
  } = useForm({
    resolver: zodResolver(formSchema),
    defaultValues,
  })

  const text = watch('text')
  const textLength = text?.length ?? 0

  async function handleOnSubmit(props: TestimonialFormSchema) {
    await onSubmit(props)
    if (!defaultValues) {
      reset()
    }
  }

  return (
    <form
      className='mt-4 flex flex-col gap-2 w-2xl'
      onSubmit={handleSubmit(handleOnSubmit)}
    >
      <Label>
        Depoimento{' '}
        <span className='text-muted-foreground text-xs'>
          ({textLength}/255)
        </span>
        :
      </Label>
      <Textarea id='text' {...register('text')} error={errors.text?.message} />
      <div className='flex gap-4 justify-center'>
        <Dialog.Close asChild>
          <Button
            variant='destructive'
            className='w-32'
            disabled={isSubmitting}
          >
            Cancelar
          </Button>
        </Dialog.Close>
        <Button className='w-32' disabled={isSubmitting}>
          Salvar
        </Button>
      </div>
    </form>
  )
}

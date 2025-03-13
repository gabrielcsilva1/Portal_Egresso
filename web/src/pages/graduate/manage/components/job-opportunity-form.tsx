import { Dialog } from '@/components/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/text-area'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

const formSchema = z.object({
  title: z.string().min(1, 'Campo obrigatório'),
  description: z
    .string()
    .min(1, 'Campo obrigatório')
    .max(1000, 'limite de caractere atingido'),
})

type JobOpportunityFormSchema = z.infer<typeof formSchema>

type Props = {
  defaultValues?: JobOpportunityFormSchema
  onSubmit?: (props: JobOpportunityFormSchema) => Promise<void>
  isSubmitting?: boolean
}

export function JobOpportunityForm({
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

  const text = watch('description')
  const textLength = text?.length ?? 0

  async function handleOnSubmit(props: JobOpportunityFormSchema) {
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
      <Label>Título:</Label>
      <Input id='title' {...register('title')} error={errors.title?.message} />
      <Label>
        Descrição:{' '}
        <span className='text-muted-foreground text-xs'>
          ({textLength}/1000)
        </span>
        :
      </Label>
      <Textarea
        id='text'
        {...register('description')}
        error={errors.description?.message}
      />
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

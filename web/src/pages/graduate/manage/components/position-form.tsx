import { Dialog } from '@/components/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/text-area'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

const formSchema = z.object({
  description: z
    .string()
    .min(1, 'Campo obrigatório')
    .max(255, 'limite de caractere atingido'),
  location: z.string().min(1, 'Campo obrigatório'),
  startYear: z.coerce.number().min(1, 'Campo obrigatório'),
  endYear: z.preprocess(
    (value) => (!value ? undefined : Number(value)),
    z.number().optional().nullable()
  ),
})

type PositionFormSchema = z.infer<typeof formSchema>

type Props = {
  defaultValues?: PositionFormSchema
  onSubmit?: (props: PositionFormSchema) => Promise<void>
  isSubmitting?: boolean
}

export function PositionForm({
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
    defaultValues: defaultValues ?? { description: '' },
  })

  const description = watch('description')
  const descriptionLength = description?.length ?? 0

  async function handleOnSubmit(props: PositionFormSchema) {
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
        Descrição*:{' '}
        <span className='text-muted-foreground text-xs'>
          ({descriptionLength}/255)
        </span>
        :
      </Label>
      <Textarea
        id='text'
        {...register('description')}
        error={errors.description?.message}
      />
      <Label>Local*:</Label>
      <Input
        id='location'
        {...register('location')}
        error={errors.location?.message}
      />

      <div className='flex gap-4 mt-2'>
        <div className='flex flex-col gap-2'>
          <Label>Ano de Início*:</Label>
          <Input
            id='startYear'
            {...register('startYear')}
            error={errors.startYear?.message}
          />
        </div>
        <div className='flex flex-col gap-2'>
          <Label>Ano de Término:</Label>
          <Input
            id='endYear'
            {...register('endYear')}
            error={errors.endYear?.message}
          />
        </div>
      </div>
      <div className='flex gap-4 justify-center mt-4'>
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

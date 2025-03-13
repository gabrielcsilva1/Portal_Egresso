import { StatusEnum } from '@/api/@responses/enums/statusEnum'
import { Dialog } from '@/components/dialog'
import { StatusTag } from '@/components/status-tag'
import { Button } from '@/components/ui/button'
import { DialogPortal } from '@radix-ui/react-dialog'

type Props = {
  registrationStatus: StatusEnum
}

export function RegistrationStatusDialog({ registrationStatus }: Props) {
  const isOpenDialog = registrationStatus !== StatusEnum.ACCEPTED

  return (
    <Dialog.Root defaultOpen={isOpenDialog}>
      <DialogPortal>
        <Dialog.Content className='flex flex-col gap-4'>
          <div className='flex items-center justify-between gap-8'>
            <Dialog.Title className='text-xl font-bold'>
              Informações do login
            </Dialog.Title>
            <StatusTag status={registrationStatus} />
          </div>
          <Dialog.Description className='text-justify'>
            {registrationStatus === StatusEnum.PENDING &&
              'O seu cadastro ainda não foi validado pelo coordenador do curso. O login só poderá ser realizado caso o cadastro seja aceito pelo coordenador.'}
            {registrationStatus === StatusEnum.REJECTED &&
              'Seu registro foi recusado pelo coordenador do curso, você pode tentar fazer novamente o cadastro ou entrar em contato com a instituição'}
          </Dialog.Description>

          <Dialog.Close asChild>
            <Button className='ml-auto mt-2'>OK</Button>
          </Dialog.Close>
        </Dialog.Content>
      </DialogPortal>
    </Dialog.Root>
  )
}

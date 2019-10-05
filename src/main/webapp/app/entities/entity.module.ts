import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'chat-user',
        loadChildren: () => import('./chat-user/chat-user.module').then(m => m.SpingularchatChatUserModule)
      },
      {
        path: 'chat-room',
        loadChildren: () => import('./chat-room/chat-room.module').then(m => m.SpingularchatChatRoomModule)
      },
      {
        path: 'chat-room-allowed-user',
        loadChildren: () =>
          import('./chat-room-allowed-user/chat-room-allowed-user.module').then(m => m.SpingularchatChatRoomAllowedUserModule)
      },
      {
        path: 'chat-message',
        loadChildren: () => import('./chat-message/chat-message.module').then(m => m.SpingularchatChatMessageModule)
      },
      {
        path: 'chat-offensive-message',
        loadChildren: () =>
          import('./chat-offensive-message/chat-offensive-message.module').then(m => m.SpingularchatChatOffensiveMessageModule)
      },
      {
        path: 'chat-notification',
        loadChildren: () => import('./chat-notification/chat-notification.module').then(m => m.SpingularchatChatNotificationModule)
      },
      {
        path: 'chat-invitation',
        loadChildren: () => import('./chat-invitation/chat-invitation.module').then(m => m.SpingularchatChatInvitationModule)
      },
      {
        path: 'chat-photo',
        loadChildren: () => import('./chat-photo/chat-photo.module').then(m => m.SpingularchatChatPhotoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class SpingularchatEntityModule {}

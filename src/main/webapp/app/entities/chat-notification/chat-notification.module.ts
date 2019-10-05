import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularchatSharedModule } from 'app/shared/shared.module';
import { ChatNotificationComponent } from './chat-notification.component';
import { ChatNotificationDetailComponent } from './chat-notification-detail.component';
import { ChatNotificationUpdateComponent } from './chat-notification-update.component';
import { ChatNotificationDeletePopupComponent, ChatNotificationDeleteDialogComponent } from './chat-notification-delete-dialog.component';
import { chatNotificationRoute, chatNotificationPopupRoute } from './chat-notification.route';

const ENTITY_STATES = [...chatNotificationRoute, ...chatNotificationPopupRoute];

@NgModule({
  imports: [SpingularchatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChatNotificationComponent,
    ChatNotificationDetailComponent,
    ChatNotificationUpdateComponent,
    ChatNotificationDeleteDialogComponent,
    ChatNotificationDeletePopupComponent
  ],
  entryComponents: [ChatNotificationDeleteDialogComponent]
})
export class SpingularchatChatNotificationModule {}

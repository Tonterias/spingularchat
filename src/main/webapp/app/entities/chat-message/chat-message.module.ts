import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularchatSharedModule } from 'app/shared/shared.module';
import { ChatMessageComponent } from './chat-message.component';
import { ChatMessageDetailComponent } from './chat-message-detail.component';
import { ChatMessageUpdateComponent } from './chat-message-update.component';
import { ChatMessageDeletePopupComponent, ChatMessageDeleteDialogComponent } from './chat-message-delete-dialog.component';
import { chatMessageRoute, chatMessagePopupRoute } from './chat-message.route';

const ENTITY_STATES = [...chatMessageRoute, ...chatMessagePopupRoute];

@NgModule({
  imports: [SpingularchatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChatMessageComponent,
    ChatMessageDetailComponent,
    ChatMessageUpdateComponent,
    ChatMessageDeleteDialogComponent,
    ChatMessageDeletePopupComponent
  ],
  entryComponents: [ChatMessageDeleteDialogComponent]
})
export class SpingularchatChatMessageModule {}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularchatSharedModule } from 'app/shared/shared.module';
import { ChatOffensiveMessageComponent } from './chat-offensive-message.component';
import { ChatOffensiveMessageDetailComponent } from './chat-offensive-message-detail.component';
import { ChatOffensiveMessageUpdateComponent } from './chat-offensive-message-update.component';
import {
  ChatOffensiveMessageDeletePopupComponent,
  ChatOffensiveMessageDeleteDialogComponent
} from './chat-offensive-message-delete-dialog.component';
import { chatOffensiveMessageRoute, chatOffensiveMessagePopupRoute } from './chat-offensive-message.route';

const ENTITY_STATES = [...chatOffensiveMessageRoute, ...chatOffensiveMessagePopupRoute];

@NgModule({
  imports: [SpingularchatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChatOffensiveMessageComponent,
    ChatOffensiveMessageDetailComponent,
    ChatOffensiveMessageUpdateComponent,
    ChatOffensiveMessageDeleteDialogComponent,
    ChatOffensiveMessageDeletePopupComponent
  ],
  entryComponents: [ChatOffensiveMessageDeleteDialogComponent]
})
export class SpingularchatChatOffensiveMessageModule {}

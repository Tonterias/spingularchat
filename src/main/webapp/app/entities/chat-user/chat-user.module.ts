import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularchatSharedModule } from 'app/shared/shared.module';
import { ChatUserComponent } from './chat-user.component';
import { ChatUserDetailComponent } from './chat-user-detail.component';
import { ChatUserUpdateComponent } from './chat-user-update.component';
import { ChatUserDeletePopupComponent, ChatUserDeleteDialogComponent } from './chat-user-delete-dialog.component';
import { chatUserRoute, chatUserPopupRoute } from './chat-user.route';

const ENTITY_STATES = [...chatUserRoute, ...chatUserPopupRoute];

@NgModule({
  imports: [SpingularchatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChatUserComponent,
    ChatUserDetailComponent,
    ChatUserUpdateComponent,
    ChatUserDeleteDialogComponent,
    ChatUserDeletePopupComponent
  ],
  entryComponents: [ChatUserDeleteDialogComponent]
})
export class SpingularchatChatUserModule {}

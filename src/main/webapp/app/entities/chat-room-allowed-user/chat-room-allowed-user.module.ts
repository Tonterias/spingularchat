import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularchatSharedModule } from 'app/shared/shared.module';
import { ChatRoomAllowedUserComponent } from './chat-room-allowed-user.component';
import { ChatRoomAllowedUserDetailComponent } from './chat-room-allowed-user-detail.component';
import { ChatRoomAllowedUserUpdateComponent } from './chat-room-allowed-user-update.component';
import {
  ChatRoomAllowedUserDeletePopupComponent,
  ChatRoomAllowedUserDeleteDialogComponent
} from './chat-room-allowed-user-delete-dialog.component';
import { chatRoomAllowedUserRoute, chatRoomAllowedUserPopupRoute } from './chat-room-allowed-user.route';

const ENTITY_STATES = [...chatRoomAllowedUserRoute, ...chatRoomAllowedUserPopupRoute];

@NgModule({
  imports: [SpingularchatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChatRoomAllowedUserComponent,
    ChatRoomAllowedUserDetailComponent,
    ChatRoomAllowedUserUpdateComponent,
    ChatRoomAllowedUserDeleteDialogComponent,
    ChatRoomAllowedUserDeletePopupComponent
  ],
  entryComponents: [ChatRoomAllowedUserDeleteDialogComponent]
})
export class SpingularchatChatRoomAllowedUserModule {}

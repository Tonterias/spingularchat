import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularchatSharedModule } from 'app/shared/shared.module';
import { ChatRoomComponent } from './chat-room.component';
import { ChatRoomDetailComponent } from './chat-room-detail.component';
import { ChatRoomUpdateComponent } from './chat-room-update.component';
import { ChatRoomDeletePopupComponent, ChatRoomDeleteDialogComponent } from './chat-room-delete-dialog.component';
import { chatRoomRoute, chatRoomPopupRoute } from './chat-room.route';

const ENTITY_STATES = [...chatRoomRoute, ...chatRoomPopupRoute];

@NgModule({
  imports: [SpingularchatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChatRoomComponent,
    ChatRoomDetailComponent,
    ChatRoomUpdateComponent,
    ChatRoomDeleteDialogComponent,
    ChatRoomDeletePopupComponent
  ],
  entryComponents: [ChatRoomDeleteDialogComponent]
})
export class SpingularchatChatRoomModule {}

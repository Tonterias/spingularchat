import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularchatSharedModule } from 'app/shared/shared.module';
import { ChatInvitationComponent } from './chat-invitation.component';
import { ChatInvitationDetailComponent } from './chat-invitation-detail.component';
import { ChatInvitationUpdateComponent } from './chat-invitation-update.component';
import { ChatInvitationDeletePopupComponent, ChatInvitationDeleteDialogComponent } from './chat-invitation-delete-dialog.component';
import { chatInvitationRoute, chatInvitationPopupRoute } from './chat-invitation.route';

const ENTITY_STATES = [...chatInvitationRoute, ...chatInvitationPopupRoute];

@NgModule({
  imports: [SpingularchatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChatInvitationComponent,
    ChatInvitationDetailComponent,
    ChatInvitationUpdateComponent,
    ChatInvitationDeleteDialogComponent,
    ChatInvitationDeletePopupComponent
  ],
  entryComponents: [ChatInvitationDeleteDialogComponent]
})
export class SpingularchatChatInvitationModule {}

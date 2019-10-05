import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChatInvitation } from 'app/shared/model/chat-invitation.model';

@Component({
  selector: 'jhi-chat-invitation-detail',
  templateUrl: './chat-invitation-detail.component.html'
})
export class ChatInvitationDetailComponent implements OnInit {
  chatInvitation: IChatInvitation;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatInvitation }) => {
      this.chatInvitation = chatInvitation;
    });
  }

  previousState() {
    window.history.back();
  }
}

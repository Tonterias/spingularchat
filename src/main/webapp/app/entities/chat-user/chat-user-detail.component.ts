import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChatUser } from 'app/shared/model/chat-user.model';

@Component({
  selector: 'jhi-chat-user-detail',
  templateUrl: './chat-user-detail.component.html'
})
export class ChatUserDetailComponent implements OnInit {
  chatUser: IChatUser;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatUser }) => {
      this.chatUser = chatUser;
    });
  }

  previousState() {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';

@Component({
  selector: 'jhi-chat-room-allowed-user-detail',
  templateUrl: './chat-room-allowed-user-detail.component.html'
})
export class ChatRoomAllowedUserDetailComponent implements OnInit {
  chatRoomAllowedUser: IChatRoomAllowedUser;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatRoomAllowedUser }) => {
      this.chatRoomAllowedUser = chatRoomAllowedUser;
    });
  }

  previousState() {
    window.history.back();
  }
}

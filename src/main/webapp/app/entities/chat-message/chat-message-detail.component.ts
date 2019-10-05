import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChatMessage } from 'app/shared/model/chat-message.model';

@Component({
  selector: 'jhi-chat-message-detail',
  templateUrl: './chat-message-detail.component.html'
})
export class ChatMessageDetailComponent implements OnInit {
  chatMessage: IChatMessage;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatMessage }) => {
      this.chatMessage = chatMessage;
    });
  }

  previousState() {
    window.history.back();
  }
}

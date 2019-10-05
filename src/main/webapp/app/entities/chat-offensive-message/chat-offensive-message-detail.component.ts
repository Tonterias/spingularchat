import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';

@Component({
  selector: 'jhi-chat-offensive-message-detail',
  templateUrl: './chat-offensive-message-detail.component.html'
})
export class ChatOffensiveMessageDetailComponent implements OnInit {
  chatOffensiveMessage: IChatOffensiveMessage;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatOffensiveMessage }) => {
      this.chatOffensiveMessage = chatOffensiveMessage;
    });
  }

  previousState() {
    window.history.back();
  }
}

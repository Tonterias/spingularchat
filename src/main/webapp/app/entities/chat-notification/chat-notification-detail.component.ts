import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChatNotification } from 'app/shared/model/chat-notification.model';

@Component({
  selector: 'jhi-chat-notification-detail',
  templateUrl: './chat-notification-detail.component.html'
})
export class ChatNotificationDetailComponent implements OnInit {
  chatNotification: IChatNotification;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatNotification }) => {
      this.chatNotification = chatNotification;
    });
  }

  previousState() {
    window.history.back();
  }
}

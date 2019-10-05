import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IChatRoom } from 'app/shared/model/chat-room.model';

@Component({
  selector: 'jhi-chat-room-detail',
  templateUrl: './chat-room-detail.component.html'
})
export class ChatRoomDetailComponent implements OnInit {
  chatRoom: IChatRoom;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatRoom }) => {
      this.chatRoom = chatRoom;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}

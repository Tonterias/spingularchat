import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IChatPhoto } from 'app/shared/model/chat-photo.model';

@Component({
  selector: 'jhi-chat-photo-detail',
  templateUrl: './chat-photo-detail.component.html'
})
export class ChatPhotoDetailComponent implements OnInit {
  chatPhoto: IChatPhoto;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatPhoto }) => {
      this.chatPhoto = chatPhoto;
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

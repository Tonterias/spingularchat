import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChatRoom } from 'app/shared/model/chat-room.model';
import { ChatRoomService } from './chat-room.service';

@Component({
  selector: 'jhi-chat-room-delete-dialog',
  templateUrl: './chat-room-delete-dialog.component.html'
})
export class ChatRoomDeleteDialogComponent {
  chatRoom: IChatRoom;

  constructor(protected chatRoomService: ChatRoomService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.chatRoomService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'chatRoomListModification',
        content: 'Deleted an chatRoom'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-chat-room-delete-popup',
  template: ''
})
export class ChatRoomDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatRoom }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChatRoomDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.chatRoom = chatRoom;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/chat-room', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/chat-room', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}

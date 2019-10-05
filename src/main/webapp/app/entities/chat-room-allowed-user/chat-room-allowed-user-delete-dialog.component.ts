import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';
import { ChatRoomAllowedUserService } from './chat-room-allowed-user.service';

@Component({
  selector: 'jhi-chat-room-allowed-user-delete-dialog',
  templateUrl: './chat-room-allowed-user-delete-dialog.component.html'
})
export class ChatRoomAllowedUserDeleteDialogComponent {
  chatRoomAllowedUser: IChatRoomAllowedUser;

  constructor(
    protected chatRoomAllowedUserService: ChatRoomAllowedUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.chatRoomAllowedUserService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'chatRoomAllowedUserListModification',
        content: 'Deleted an chatRoomAllowedUser'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-chat-room-allowed-user-delete-popup',
  template: ''
})
export class ChatRoomAllowedUserDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatRoomAllowedUser }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChatRoomAllowedUserDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.chatRoomAllowedUser = chatRoomAllowedUser;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/chat-room-allowed-user', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/chat-room-allowed-user', { outlets: { popup: null } }]);
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

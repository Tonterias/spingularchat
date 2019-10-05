import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChatUser } from 'app/shared/model/chat-user.model';
import { ChatUserService } from './chat-user.service';

@Component({
  selector: 'jhi-chat-user-delete-dialog',
  templateUrl: './chat-user-delete-dialog.component.html'
})
export class ChatUserDeleteDialogComponent {
  chatUser: IChatUser;

  constructor(protected chatUserService: ChatUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.chatUserService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'chatUserListModification',
        content: 'Deleted an chatUser'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-chat-user-delete-popup',
  template: ''
})
export class ChatUserDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatUser }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChatUserDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.chatUser = chatUser;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/chat-user', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/chat-user', { outlets: { popup: null } }]);
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

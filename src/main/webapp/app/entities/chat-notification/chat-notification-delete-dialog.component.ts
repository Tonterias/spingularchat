import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChatNotification } from 'app/shared/model/chat-notification.model';
import { ChatNotificationService } from './chat-notification.service';

@Component({
  selector: 'jhi-chat-notification-delete-dialog',
  templateUrl: './chat-notification-delete-dialog.component.html'
})
export class ChatNotificationDeleteDialogComponent {
  chatNotification: IChatNotification;

  constructor(
    protected chatNotificationService: ChatNotificationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.chatNotificationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'chatNotificationListModification',
        content: 'Deleted an chatNotification'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-chat-notification-delete-popup',
  template: ''
})
export class ChatNotificationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatNotification }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChatNotificationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.chatNotification = chatNotification;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/chat-notification', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/chat-notification', { outlets: { popup: null } }]);
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

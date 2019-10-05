import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';
import { ChatOffensiveMessageService } from './chat-offensive-message.service';

@Component({
  selector: 'jhi-chat-offensive-message-delete-dialog',
  templateUrl: './chat-offensive-message-delete-dialog.component.html'
})
export class ChatOffensiveMessageDeleteDialogComponent {
  chatOffensiveMessage: IChatOffensiveMessage;

  constructor(
    protected chatOffensiveMessageService: ChatOffensiveMessageService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.chatOffensiveMessageService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'chatOffensiveMessageListModification',
        content: 'Deleted an chatOffensiveMessage'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-chat-offensive-message-delete-popup',
  template: ''
})
export class ChatOffensiveMessageDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatOffensiveMessage }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChatOffensiveMessageDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.chatOffensiveMessage = chatOffensiveMessage;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/chat-offensive-message', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/chat-offensive-message', { outlets: { popup: null } }]);
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

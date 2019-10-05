import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChatInvitation } from 'app/shared/model/chat-invitation.model';
import { ChatInvitationService } from './chat-invitation.service';

@Component({
  selector: 'jhi-chat-invitation-delete-dialog',
  templateUrl: './chat-invitation-delete-dialog.component.html'
})
export class ChatInvitationDeleteDialogComponent {
  chatInvitation: IChatInvitation;

  constructor(
    protected chatInvitationService: ChatInvitationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.chatInvitationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'chatInvitationListModification',
        content: 'Deleted an chatInvitation'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-chat-invitation-delete-popup',
  template: ''
})
export class ChatInvitationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatInvitation }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChatInvitationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.chatInvitation = chatInvitation;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/chat-invitation', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/chat-invitation', { outlets: { popup: null } }]);
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

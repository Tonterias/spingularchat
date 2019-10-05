import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChatPhoto } from 'app/shared/model/chat-photo.model';
import { ChatPhotoService } from './chat-photo.service';

@Component({
  selector: 'jhi-chat-photo-delete-dialog',
  templateUrl: './chat-photo-delete-dialog.component.html'
})
export class ChatPhotoDeleteDialogComponent {
  chatPhoto: IChatPhoto;

  constructor(protected chatPhotoService: ChatPhotoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.chatPhotoService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'chatPhotoListModification',
        content: 'Deleted an chatPhoto'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-chat-photo-delete-popup',
  template: ''
})
export class ChatPhotoDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chatPhoto }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChatPhotoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.chatPhoto = chatPhoto;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/chat-photo', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/chat-photo', { outlets: { popup: null } }]);
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

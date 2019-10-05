import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatInvitationDeleteDialogComponent } from 'app/entities/chat-invitation/chat-invitation-delete-dialog.component';
import { ChatInvitationService } from 'app/entities/chat-invitation/chat-invitation.service';

describe('Component Tests', () => {
  describe('ChatInvitation Management Delete Component', () => {
    let comp: ChatInvitationDeleteDialogComponent;
    let fixture: ComponentFixture<ChatInvitationDeleteDialogComponent>;
    let service: ChatInvitationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatInvitationDeleteDialogComponent]
      })
        .overrideTemplate(ChatInvitationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatInvitationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatInvitationService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});

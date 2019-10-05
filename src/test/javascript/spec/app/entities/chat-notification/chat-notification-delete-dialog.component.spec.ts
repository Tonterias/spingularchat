import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatNotificationDeleteDialogComponent } from 'app/entities/chat-notification/chat-notification-delete-dialog.component';
import { ChatNotificationService } from 'app/entities/chat-notification/chat-notification.service';

describe('Component Tests', () => {
  describe('ChatNotification Management Delete Component', () => {
    let comp: ChatNotificationDeleteDialogComponent;
    let fixture: ComponentFixture<ChatNotificationDeleteDialogComponent>;
    let service: ChatNotificationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatNotificationDeleteDialogComponent]
      })
        .overrideTemplate(ChatNotificationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatNotificationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatNotificationService);
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

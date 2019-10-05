import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatMessageDeleteDialogComponent } from 'app/entities/chat-message/chat-message-delete-dialog.component';
import { ChatMessageService } from 'app/entities/chat-message/chat-message.service';

describe('Component Tests', () => {
  describe('ChatMessage Management Delete Component', () => {
    let comp: ChatMessageDeleteDialogComponent;
    let fixture: ComponentFixture<ChatMessageDeleteDialogComponent>;
    let service: ChatMessageService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatMessageDeleteDialogComponent]
      })
        .overrideTemplate(ChatMessageDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatMessageDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatMessageService);
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

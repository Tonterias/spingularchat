import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatOffensiveMessageDeleteDialogComponent } from 'app/entities/chat-offensive-message/chat-offensive-message-delete-dialog.component';
import { ChatOffensiveMessageService } from 'app/entities/chat-offensive-message/chat-offensive-message.service';

describe('Component Tests', () => {
  describe('ChatOffensiveMessage Management Delete Component', () => {
    let comp: ChatOffensiveMessageDeleteDialogComponent;
    let fixture: ComponentFixture<ChatOffensiveMessageDeleteDialogComponent>;
    let service: ChatOffensiveMessageService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatOffensiveMessageDeleteDialogComponent]
      })
        .overrideTemplate(ChatOffensiveMessageDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatOffensiveMessageDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatOffensiveMessageService);
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

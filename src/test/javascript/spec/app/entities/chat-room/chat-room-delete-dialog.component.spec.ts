import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatRoomDeleteDialogComponent } from 'app/entities/chat-room/chat-room-delete-dialog.component';
import { ChatRoomService } from 'app/entities/chat-room/chat-room.service';

describe('Component Tests', () => {
  describe('ChatRoom Management Delete Component', () => {
    let comp: ChatRoomDeleteDialogComponent;
    let fixture: ComponentFixture<ChatRoomDeleteDialogComponent>;
    let service: ChatRoomService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatRoomDeleteDialogComponent]
      })
        .overrideTemplate(ChatRoomDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatRoomDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatRoomService);
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

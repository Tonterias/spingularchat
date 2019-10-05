import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatRoomAllowedUserDeleteDialogComponent } from 'app/entities/chat-room-allowed-user/chat-room-allowed-user-delete-dialog.component';
import { ChatRoomAllowedUserService } from 'app/entities/chat-room-allowed-user/chat-room-allowed-user.service';

describe('Component Tests', () => {
  describe('ChatRoomAllowedUser Management Delete Component', () => {
    let comp: ChatRoomAllowedUserDeleteDialogComponent;
    let fixture: ComponentFixture<ChatRoomAllowedUserDeleteDialogComponent>;
    let service: ChatRoomAllowedUserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatRoomAllowedUserDeleteDialogComponent]
      })
        .overrideTemplate(ChatRoomAllowedUserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatRoomAllowedUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatRoomAllowedUserService);
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

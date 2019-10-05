import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatPhotoDeleteDialogComponent } from 'app/entities/chat-photo/chat-photo-delete-dialog.component';
import { ChatPhotoService } from 'app/entities/chat-photo/chat-photo.service';

describe('Component Tests', () => {
  describe('ChatPhoto Management Delete Component', () => {
    let comp: ChatPhotoDeleteDialogComponent;
    let fixture: ComponentFixture<ChatPhotoDeleteDialogComponent>;
    let service: ChatPhotoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatPhotoDeleteDialogComponent]
      })
        .overrideTemplate(ChatPhotoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatPhotoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatPhotoService);
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

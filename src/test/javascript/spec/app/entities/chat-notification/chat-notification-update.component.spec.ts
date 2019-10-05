import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatNotificationUpdateComponent } from 'app/entities/chat-notification/chat-notification-update.component';
import { ChatNotificationService } from 'app/entities/chat-notification/chat-notification.service';
import { ChatNotification } from 'app/shared/model/chat-notification.model';

describe('Component Tests', () => {
  describe('ChatNotification Management Update Component', () => {
    let comp: ChatNotificationUpdateComponent;
    let fixture: ComponentFixture<ChatNotificationUpdateComponent>;
    let service: ChatNotificationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatNotificationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatNotificationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatNotificationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatNotificationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatNotification(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatNotification();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

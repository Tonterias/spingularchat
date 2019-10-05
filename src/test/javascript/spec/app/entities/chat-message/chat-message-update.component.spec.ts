import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatMessageUpdateComponent } from 'app/entities/chat-message/chat-message-update.component';
import { ChatMessageService } from 'app/entities/chat-message/chat-message.service';
import { ChatMessage } from 'app/shared/model/chat-message.model';

describe('Component Tests', () => {
  describe('ChatMessage Management Update Component', () => {
    let comp: ChatMessageUpdateComponent;
    let fixture: ComponentFixture<ChatMessageUpdateComponent>;
    let service: ChatMessageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatMessageUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatMessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatMessageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatMessageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatMessage(123);
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
        const entity = new ChatMessage();
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

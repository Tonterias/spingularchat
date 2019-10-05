import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatOffensiveMessageUpdateComponent } from 'app/entities/chat-offensive-message/chat-offensive-message-update.component';
import { ChatOffensiveMessageService } from 'app/entities/chat-offensive-message/chat-offensive-message.service';
import { ChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';

describe('Component Tests', () => {
  describe('ChatOffensiveMessage Management Update Component', () => {
    let comp: ChatOffensiveMessageUpdateComponent;
    let fixture: ComponentFixture<ChatOffensiveMessageUpdateComponent>;
    let service: ChatOffensiveMessageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatOffensiveMessageUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatOffensiveMessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatOffensiveMessageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatOffensiveMessageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatOffensiveMessage(123);
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
        const entity = new ChatOffensiveMessage();
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

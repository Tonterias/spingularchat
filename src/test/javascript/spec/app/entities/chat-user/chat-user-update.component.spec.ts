import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatUserUpdateComponent } from 'app/entities/chat-user/chat-user-update.component';
import { ChatUserService } from 'app/entities/chat-user/chat-user.service';
import { ChatUser } from 'app/shared/model/chat-user.model';

describe('Component Tests', () => {
  describe('ChatUser Management Update Component', () => {
    let comp: ChatUserUpdateComponent;
    let fixture: ComponentFixture<ChatUserUpdateComponent>;
    let service: ChatUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatUser(123);
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
        const entity = new ChatUser();
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

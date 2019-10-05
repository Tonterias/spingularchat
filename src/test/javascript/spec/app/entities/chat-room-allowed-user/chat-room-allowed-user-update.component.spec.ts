import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatRoomAllowedUserUpdateComponent } from 'app/entities/chat-room-allowed-user/chat-room-allowed-user-update.component';
import { ChatRoomAllowedUserService } from 'app/entities/chat-room-allowed-user/chat-room-allowed-user.service';
import { ChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';

describe('Component Tests', () => {
  describe('ChatRoomAllowedUser Management Update Component', () => {
    let comp: ChatRoomAllowedUserUpdateComponent;
    let fixture: ComponentFixture<ChatRoomAllowedUserUpdateComponent>;
    let service: ChatRoomAllowedUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatRoomAllowedUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatRoomAllowedUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatRoomAllowedUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatRoomAllowedUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatRoomAllowedUser(123);
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
        const entity = new ChatRoomAllowedUser();
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

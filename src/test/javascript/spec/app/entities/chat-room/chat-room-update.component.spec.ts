import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatRoomUpdateComponent } from 'app/entities/chat-room/chat-room-update.component';
import { ChatRoomService } from 'app/entities/chat-room/chat-room.service';
import { ChatRoom } from 'app/shared/model/chat-room.model';

describe('Component Tests', () => {
  describe('ChatRoom Management Update Component', () => {
    let comp: ChatRoomUpdateComponent;
    let fixture: ComponentFixture<ChatRoomUpdateComponent>;
    let service: ChatRoomService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatRoomUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatRoomUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatRoomUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatRoomService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatRoom(123);
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
        const entity = new ChatRoom();
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

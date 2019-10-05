import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatInvitationUpdateComponent } from 'app/entities/chat-invitation/chat-invitation-update.component';
import { ChatInvitationService } from 'app/entities/chat-invitation/chat-invitation.service';
import { ChatInvitation } from 'app/shared/model/chat-invitation.model';

describe('Component Tests', () => {
  describe('ChatInvitation Management Update Component', () => {
    let comp: ChatInvitationUpdateComponent;
    let fixture: ComponentFixture<ChatInvitationUpdateComponent>;
    let service: ChatInvitationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatInvitationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatInvitationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatInvitationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatInvitationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatInvitation(123);
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
        const entity = new ChatInvitation();
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

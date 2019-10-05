import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatPhotoUpdateComponent } from 'app/entities/chat-photo/chat-photo-update.component';
import { ChatPhotoService } from 'app/entities/chat-photo/chat-photo.service';
import { ChatPhoto } from 'app/shared/model/chat-photo.model';

describe('Component Tests', () => {
  describe('ChatPhoto Management Update Component', () => {
    let comp: ChatPhotoUpdateComponent;
    let fixture: ComponentFixture<ChatPhotoUpdateComponent>;
    let service: ChatPhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatPhotoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatPhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatPhotoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatPhotoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChatPhoto(123);
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
        const entity = new ChatPhoto();
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

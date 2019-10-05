import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatPhotoDetailComponent } from 'app/entities/chat-photo/chat-photo-detail.component';
import { ChatPhoto } from 'app/shared/model/chat-photo.model';

describe('Component Tests', () => {
  describe('ChatPhoto Management Detail Component', () => {
    let comp: ChatPhotoDetailComponent;
    let fixture: ComponentFixture<ChatPhotoDetailComponent>;
    const route = ({ data: of({ chatPhoto: new ChatPhoto(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatPhotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChatPhotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatPhotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chatPhoto).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

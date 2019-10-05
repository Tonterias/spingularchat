import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatUserDetailComponent } from 'app/entities/chat-user/chat-user-detail.component';
import { ChatUser } from 'app/shared/model/chat-user.model';

describe('Component Tests', () => {
  describe('ChatUser Management Detail Component', () => {
    let comp: ChatUserDetailComponent;
    let fixture: ComponentFixture<ChatUserDetailComponent>;
    const route = ({ data: of({ chatUser: new ChatUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChatUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chatUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

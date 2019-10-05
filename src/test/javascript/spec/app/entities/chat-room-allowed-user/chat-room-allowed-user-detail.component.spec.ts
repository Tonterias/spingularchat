import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatRoomAllowedUserDetailComponent } from 'app/entities/chat-room-allowed-user/chat-room-allowed-user-detail.component';
import { ChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';

describe('Component Tests', () => {
  describe('ChatRoomAllowedUser Management Detail Component', () => {
    let comp: ChatRoomAllowedUserDetailComponent;
    let fixture: ComponentFixture<ChatRoomAllowedUserDetailComponent>;
    const route = ({ data: of({ chatRoomAllowedUser: new ChatRoomAllowedUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatRoomAllowedUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChatRoomAllowedUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatRoomAllowedUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chatRoomAllowedUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

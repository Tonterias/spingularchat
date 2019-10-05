import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatRoomDetailComponent } from 'app/entities/chat-room/chat-room-detail.component';
import { ChatRoom } from 'app/shared/model/chat-room.model';

describe('Component Tests', () => {
  describe('ChatRoom Management Detail Component', () => {
    let comp: ChatRoomDetailComponent;
    let fixture: ComponentFixture<ChatRoomDetailComponent>;
    const route = ({ data: of({ chatRoom: new ChatRoom(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatRoomDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChatRoomDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatRoomDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chatRoom).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

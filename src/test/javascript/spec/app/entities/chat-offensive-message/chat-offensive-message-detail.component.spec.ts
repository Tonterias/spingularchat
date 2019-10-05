import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatOffensiveMessageDetailComponent } from 'app/entities/chat-offensive-message/chat-offensive-message-detail.component';
import { ChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';

describe('Component Tests', () => {
  describe('ChatOffensiveMessage Management Detail Component', () => {
    let comp: ChatOffensiveMessageDetailComponent;
    let fixture: ComponentFixture<ChatOffensiveMessageDetailComponent>;
    const route = ({ data: of({ chatOffensiveMessage: new ChatOffensiveMessage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatOffensiveMessageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChatOffensiveMessageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatOffensiveMessageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chatOffensiveMessage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatInvitationDetailComponent } from 'app/entities/chat-invitation/chat-invitation-detail.component';
import { ChatInvitation } from 'app/shared/model/chat-invitation.model';

describe('Component Tests', () => {
  describe('ChatInvitation Management Detail Component', () => {
    let comp: ChatInvitationDetailComponent;
    let fixture: ComponentFixture<ChatInvitationDetailComponent>;
    const route = ({ data: of({ chatInvitation: new ChatInvitation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatInvitationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChatInvitationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatInvitationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chatInvitation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

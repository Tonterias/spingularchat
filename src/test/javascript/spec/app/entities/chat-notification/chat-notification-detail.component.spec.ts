import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularchatTestModule } from '../../../test.module';
import { ChatNotificationDetailComponent } from 'app/entities/chat-notification/chat-notification-detail.component';
import { ChatNotification } from 'app/shared/model/chat-notification.model';

describe('Component Tests', () => {
  describe('ChatNotification Management Detail Component', () => {
    let comp: ChatNotificationDetailComponent;
    let fixture: ComponentFixture<ChatNotificationDetailComponent>;
    const route = ({ data: of({ chatNotification: new ChatNotification(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularchatTestModule],
        declarations: [ChatNotificationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChatNotificationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatNotificationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chatNotification).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

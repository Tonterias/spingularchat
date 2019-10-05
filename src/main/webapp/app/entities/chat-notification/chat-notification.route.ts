import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChatNotification } from 'app/shared/model/chat-notification.model';
import { ChatNotificationService } from './chat-notification.service';
import { ChatNotificationComponent } from './chat-notification.component';
import { ChatNotificationDetailComponent } from './chat-notification-detail.component';
import { ChatNotificationUpdateComponent } from './chat-notification-update.component';
import { ChatNotificationDeletePopupComponent } from './chat-notification-delete-dialog.component';
import { IChatNotification } from 'app/shared/model/chat-notification.model';

@Injectable({ providedIn: 'root' })
export class ChatNotificationResolve implements Resolve<IChatNotification> {
  constructor(private service: ChatNotificationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChatNotification> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChatNotification>) => response.ok),
        map((chatNotification: HttpResponse<ChatNotification>) => chatNotification.body)
      );
    }
    return of(new ChatNotification());
  }
}

export const chatNotificationRoute: Routes = [
  {
    path: '',
    component: ChatNotificationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'spingularchatApp.chatNotification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChatNotificationDetailComponent,
    resolve: {
      chatNotification: ChatNotificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatNotification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChatNotificationUpdateComponent,
    resolve: {
      chatNotification: ChatNotificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatNotification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChatNotificationUpdateComponent,
    resolve: {
      chatNotification: ChatNotificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatNotification.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chatNotificationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChatNotificationDeletePopupComponent,
    resolve: {
      chatNotification: ChatNotificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatNotification.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

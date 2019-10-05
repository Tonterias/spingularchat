import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';
import { ChatOffensiveMessageService } from './chat-offensive-message.service';
import { ChatOffensiveMessageComponent } from './chat-offensive-message.component';
import { ChatOffensiveMessageDetailComponent } from './chat-offensive-message-detail.component';
import { ChatOffensiveMessageUpdateComponent } from './chat-offensive-message-update.component';
import { ChatOffensiveMessageDeletePopupComponent } from './chat-offensive-message-delete-dialog.component';
import { IChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';

@Injectable({ providedIn: 'root' })
export class ChatOffensiveMessageResolve implements Resolve<IChatOffensiveMessage> {
  constructor(private service: ChatOffensiveMessageService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChatOffensiveMessage> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChatOffensiveMessage>) => response.ok),
        map((chatOffensiveMessage: HttpResponse<ChatOffensiveMessage>) => chatOffensiveMessage.body)
      );
    }
    return of(new ChatOffensiveMessage());
  }
}

export const chatOffensiveMessageRoute: Routes = [
  {
    path: '',
    component: ChatOffensiveMessageComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'spingularchatApp.chatOffensiveMessage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChatOffensiveMessageDetailComponent,
    resolve: {
      chatOffensiveMessage: ChatOffensiveMessageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatOffensiveMessage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChatOffensiveMessageUpdateComponent,
    resolve: {
      chatOffensiveMessage: ChatOffensiveMessageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatOffensiveMessage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChatOffensiveMessageUpdateComponent,
    resolve: {
      chatOffensiveMessage: ChatOffensiveMessageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatOffensiveMessage.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chatOffensiveMessagePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChatOffensiveMessageDeletePopupComponent,
    resolve: {
      chatOffensiveMessage: ChatOffensiveMessageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatOffensiveMessage.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

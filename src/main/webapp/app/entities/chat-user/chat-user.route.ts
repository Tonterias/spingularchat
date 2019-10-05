import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChatUser } from 'app/shared/model/chat-user.model';
import { ChatUserService } from './chat-user.service';
import { ChatUserComponent } from './chat-user.component';
import { ChatUserDetailComponent } from './chat-user-detail.component';
import { ChatUserUpdateComponent } from './chat-user-update.component';
import { ChatUserDeletePopupComponent } from './chat-user-delete-dialog.component';
import { IChatUser } from 'app/shared/model/chat-user.model';

@Injectable({ providedIn: 'root' })
export class ChatUserResolve implements Resolve<IChatUser> {
  constructor(private service: ChatUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChatUser> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChatUser>) => response.ok),
        map((chatUser: HttpResponse<ChatUser>) => chatUser.body)
      );
    }
    return of(new ChatUser());
  }
}

export const chatUserRoute: Routes = [
  {
    path: '',
    component: ChatUserComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'spingularchatApp.chatUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChatUserDetailComponent,
    resolve: {
      chatUser: ChatUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChatUserUpdateComponent,
    resolve: {
      chatUser: ChatUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChatUserUpdateComponent,
    resolve: {
      chatUser: ChatUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chatUserPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChatUserDeletePopupComponent,
    resolve: {
      chatUser: ChatUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatUser.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

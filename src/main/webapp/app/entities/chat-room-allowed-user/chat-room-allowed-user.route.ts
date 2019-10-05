import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';
import { ChatRoomAllowedUserService } from './chat-room-allowed-user.service';
import { ChatRoomAllowedUserComponent } from './chat-room-allowed-user.component';
import { ChatRoomAllowedUserDetailComponent } from './chat-room-allowed-user-detail.component';
import { ChatRoomAllowedUserUpdateComponent } from './chat-room-allowed-user-update.component';
import { ChatRoomAllowedUserDeletePopupComponent } from './chat-room-allowed-user-delete-dialog.component';
import { IChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';

@Injectable({ providedIn: 'root' })
export class ChatRoomAllowedUserResolve implements Resolve<IChatRoomAllowedUser> {
  constructor(private service: ChatRoomAllowedUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChatRoomAllowedUser> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChatRoomAllowedUser>) => response.ok),
        map((chatRoomAllowedUser: HttpResponse<ChatRoomAllowedUser>) => chatRoomAllowedUser.body)
      );
    }
    return of(new ChatRoomAllowedUser());
  }
}

export const chatRoomAllowedUserRoute: Routes = [
  {
    path: '',
    component: ChatRoomAllowedUserComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'spingularchatApp.chatRoomAllowedUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChatRoomAllowedUserDetailComponent,
    resolve: {
      chatRoomAllowedUser: ChatRoomAllowedUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatRoomAllowedUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChatRoomAllowedUserUpdateComponent,
    resolve: {
      chatRoomAllowedUser: ChatRoomAllowedUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatRoomAllowedUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChatRoomAllowedUserUpdateComponent,
    resolve: {
      chatRoomAllowedUser: ChatRoomAllowedUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatRoomAllowedUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chatRoomAllowedUserPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChatRoomAllowedUserDeletePopupComponent,
    resolve: {
      chatRoomAllowedUser: ChatRoomAllowedUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatRoomAllowedUser.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

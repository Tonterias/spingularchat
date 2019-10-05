import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChatRoom } from 'app/shared/model/chat-room.model';
import { ChatRoomService } from './chat-room.service';
import { ChatRoomComponent } from './chat-room.component';
import { ChatRoomDetailComponent } from './chat-room-detail.component';
import { ChatRoomUpdateComponent } from './chat-room-update.component';
import { ChatRoomDeletePopupComponent } from './chat-room-delete-dialog.component';
import { IChatRoom } from 'app/shared/model/chat-room.model';

@Injectable({ providedIn: 'root' })
export class ChatRoomResolve implements Resolve<IChatRoom> {
  constructor(private service: ChatRoomService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChatRoom> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChatRoom>) => response.ok),
        map((chatRoom: HttpResponse<ChatRoom>) => chatRoom.body)
      );
    }
    return of(new ChatRoom());
  }
}

export const chatRoomRoute: Routes = [
  {
    path: '',
    component: ChatRoomComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'spingularchatApp.chatRoom.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChatRoomDetailComponent,
    resolve: {
      chatRoom: ChatRoomResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatRoom.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChatRoomUpdateComponent,
    resolve: {
      chatRoom: ChatRoomResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatRoom.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChatRoomUpdateComponent,
    resolve: {
      chatRoom: ChatRoomResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatRoom.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chatRoomPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChatRoomDeletePopupComponent,
    resolve: {
      chatRoom: ChatRoomResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatRoom.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChatInvitation } from 'app/shared/model/chat-invitation.model';
import { ChatInvitationService } from './chat-invitation.service';
import { ChatInvitationComponent } from './chat-invitation.component';
import { ChatInvitationDetailComponent } from './chat-invitation-detail.component';
import { ChatInvitationUpdateComponent } from './chat-invitation-update.component';
import { ChatInvitationDeletePopupComponent } from './chat-invitation-delete-dialog.component';
import { IChatInvitation } from 'app/shared/model/chat-invitation.model';

@Injectable({ providedIn: 'root' })
export class ChatInvitationResolve implements Resolve<IChatInvitation> {
  constructor(private service: ChatInvitationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChatInvitation> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChatInvitation>) => response.ok),
        map((chatInvitation: HttpResponse<ChatInvitation>) => chatInvitation.body)
      );
    }
    return of(new ChatInvitation());
  }
}

export const chatInvitationRoute: Routes = [
  {
    path: '',
    component: ChatInvitationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'spingularchatApp.chatInvitation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChatInvitationDetailComponent,
    resolve: {
      chatInvitation: ChatInvitationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatInvitation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChatInvitationUpdateComponent,
    resolve: {
      chatInvitation: ChatInvitationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatInvitation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChatInvitationUpdateComponent,
    resolve: {
      chatInvitation: ChatInvitationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatInvitation.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chatInvitationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChatInvitationDeletePopupComponent,
    resolve: {
      chatInvitation: ChatInvitationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatInvitation.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

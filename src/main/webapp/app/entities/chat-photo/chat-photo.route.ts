import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChatPhoto } from 'app/shared/model/chat-photo.model';
import { ChatPhotoService } from './chat-photo.service';
import { ChatPhotoComponent } from './chat-photo.component';
import { ChatPhotoDetailComponent } from './chat-photo-detail.component';
import { ChatPhotoUpdateComponent } from './chat-photo-update.component';
import { ChatPhotoDeletePopupComponent } from './chat-photo-delete-dialog.component';
import { IChatPhoto } from 'app/shared/model/chat-photo.model';

@Injectable({ providedIn: 'root' })
export class ChatPhotoResolve implements Resolve<IChatPhoto> {
  constructor(private service: ChatPhotoService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChatPhoto> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChatPhoto>) => response.ok),
        map((chatPhoto: HttpResponse<ChatPhoto>) => chatPhoto.body)
      );
    }
    return of(new ChatPhoto());
  }
}

export const chatPhotoRoute: Routes = [
  {
    path: '',
    component: ChatPhotoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'spingularchatApp.chatPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChatPhotoDetailComponent,
    resolve: {
      chatPhoto: ChatPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChatPhotoUpdateComponent,
    resolve: {
      chatPhoto: ChatPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChatPhotoUpdateComponent,
    resolve: {
      chatPhoto: ChatPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatPhoto.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chatPhotoPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChatPhotoDeletePopupComponent,
    resolve: {
      chatPhoto: ChatPhotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'spingularchatApp.chatPhoto.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

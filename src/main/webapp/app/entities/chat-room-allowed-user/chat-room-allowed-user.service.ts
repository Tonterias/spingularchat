import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';

type EntityResponseType = HttpResponse<IChatRoomAllowedUser>;
type EntityArrayResponseType = HttpResponse<IChatRoomAllowedUser[]>;

@Injectable({ providedIn: 'root' })
export class ChatRoomAllowedUserService {
  public resourceUrl = SERVER_API_URL + 'api/chat-room-allowed-users';

  constructor(protected http: HttpClient) {}

  create(chatRoomAllowedUser: IChatRoomAllowedUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatRoomAllowedUser);
    return this.http
      .post<IChatRoomAllowedUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chatRoomAllowedUser: IChatRoomAllowedUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatRoomAllowedUser);
    return this.http
      .put<IChatRoomAllowedUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChatRoomAllowedUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChatRoomAllowedUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(chatRoomAllowedUser: IChatRoomAllowedUser): IChatRoomAllowedUser {
    const copy: IChatRoomAllowedUser = Object.assign({}, chatRoomAllowedUser, {
      creationDate:
        chatRoomAllowedUser.creationDate != null && chatRoomAllowedUser.creationDate.isValid()
          ? chatRoomAllowedUser.creationDate.toJSON()
          : null,
      bannedDate:
        chatRoomAllowedUser.bannedDate != null && chatRoomAllowedUser.bannedDate.isValid() ? chatRoomAllowedUser.bannedDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
      res.body.bannedDate = res.body.bannedDate != null ? moment(res.body.bannedDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((chatRoomAllowedUser: IChatRoomAllowedUser) => {
        chatRoomAllowedUser.creationDate = chatRoomAllowedUser.creationDate != null ? moment(chatRoomAllowedUser.creationDate) : null;
        chatRoomAllowedUser.bannedDate = chatRoomAllowedUser.bannedDate != null ? moment(chatRoomAllowedUser.bannedDate) : null;
      });
    }
    return res;
  }
}

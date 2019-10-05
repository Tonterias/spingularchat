import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChatUser } from 'app/shared/model/chat-user.model';

type EntityResponseType = HttpResponse<IChatUser>;
type EntityArrayResponseType = HttpResponse<IChatUser[]>;

@Injectable({ providedIn: 'root' })
export class ChatUserService {
  public resourceUrl = SERVER_API_URL + 'api/chat-users';

  constructor(protected http: HttpClient) {}

  create(chatUser: IChatUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatUser);
    return this.http
      .post<IChatUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chatUser: IChatUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatUser);
    return this.http
      .put<IChatUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChatUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChatUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(chatUser: IChatUser): IChatUser {
    const copy: IChatUser = Object.assign({}, chatUser, {
      creationDate: chatUser.creationDate != null && chatUser.creationDate.isValid() ? chatUser.creationDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((chatUser: IChatUser) => {
        chatUser.creationDate = chatUser.creationDate != null ? moment(chatUser.creationDate) : null;
      });
    }
    return res;
  }
}

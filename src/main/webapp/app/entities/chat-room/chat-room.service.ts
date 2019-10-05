import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChatRoom } from 'app/shared/model/chat-room.model';

type EntityResponseType = HttpResponse<IChatRoom>;
type EntityArrayResponseType = HttpResponse<IChatRoom[]>;

@Injectable({ providedIn: 'root' })
export class ChatRoomService {
  public resourceUrl = SERVER_API_URL + 'api/chat-rooms';

  constructor(protected http: HttpClient) {}

  create(chatRoom: IChatRoom): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatRoom);
    return this.http
      .post<IChatRoom>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chatRoom: IChatRoom): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatRoom);
    return this.http
      .put<IChatRoom>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChatRoom>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChatRoom[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(chatRoom: IChatRoom): IChatRoom {
    const copy: IChatRoom = Object.assign({}, chatRoom, {
      creationDate: chatRoom.creationDate != null && chatRoom.creationDate.isValid() ? chatRoom.creationDate.toJSON() : null
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
      res.body.forEach((chatRoom: IChatRoom) => {
        chatRoom.creationDate = chatRoom.creationDate != null ? moment(chatRoom.creationDate) : null;
      });
    }
    return res;
  }
}

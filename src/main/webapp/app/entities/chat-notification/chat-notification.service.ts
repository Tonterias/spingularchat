import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChatNotification } from 'app/shared/model/chat-notification.model';

type EntityResponseType = HttpResponse<IChatNotification>;
type EntityArrayResponseType = HttpResponse<IChatNotification[]>;

@Injectable({ providedIn: 'root' })
export class ChatNotificationService {
  public resourceUrl = SERVER_API_URL + 'api/chat-notifications';

  constructor(protected http: HttpClient) {}

  create(chatNotification: IChatNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatNotification);
    return this.http
      .post<IChatNotification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chatNotification: IChatNotification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatNotification);
    return this.http
      .put<IChatNotification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChatNotification>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChatNotification[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(chatNotification: IChatNotification): IChatNotification {
    const copy: IChatNotification = Object.assign({}, chatNotification, {
      creationDate:
        chatNotification.creationDate != null && chatNotification.creationDate.isValid() ? chatNotification.creationDate.toJSON() : null
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
      res.body.forEach((chatNotification: IChatNotification) => {
        chatNotification.creationDate = chatNotification.creationDate != null ? moment(chatNotification.creationDate) : null;
      });
    }
    return res;
  }
}
